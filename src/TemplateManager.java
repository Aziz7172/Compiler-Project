import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import gen.ANTLR.PythonLexer;
import gen.ANTLR.PythonParser;
import gen.ANTLR.Jinja2Lexer;
import gen.ANTLR.Jinja2Parser;
import org.antlr.v4.runtime.*;

import AST.*;
import AST.ASTBuilder;
import AST.JINJA2.Jinja2ASTBuilder;
import AST.JINJA2.Jinja2Node;
import ErrorHandling.CompilerErrorListener;
import SemanticAnalysis.Jinja2SemanticChecker;
import SemanticAnalysis.SemanticAnalyzer;
import SemanticAnalysis.SemanticError;
import SymbolTable.SymbolTable;
import CodeGenerator.CodeGenerator;
import CodeGenerator.HtmlCodeGenerator;

import VM.*;

/**
 * Orchestrates the full compiler pipeline:
 *   Phase 1-4: Parse Python, build AST, semantic analysis
 *   Phase 5:   Extract global variables from app.py into Context
 *   Phase 6:   Scan templates/*.jinja, render each with the global Context
 *
 * This follows a static-site-generator architecture:
 * the compiler reads data (Context) and applies it to ALL templates,
 * regardless of whether render_template() calls exist.
 */
public class TemplateManager {
    private final String projectDir;
    private final List<String> templateFiles;
    private final List<String> outputFiles;
    private final SymbolTable symbolTable;
    private final SemanticAnalyzer semanticAnalyzer;
    private final List<String> errors;
    private String pythonAstJson;
    private String jinjaAstJson;
    private final PythonContextExtractor pythonContextExtractor;
    private final List<String> pythonSourceFiles;
    private final List<String> jinjaOutputFiles;

    public TemplateManager(String projectDir) {
        this.projectDir = projectDir;
        this.templateFiles = new ArrayList<>();
        this.outputFiles = new ArrayList<>();
        this.symbolTable = new SymbolTable();
        this.semanticAnalyzer = new SemanticAnalyzer(symbolTable);
        this.errors = new ArrayList<>();
        this.pythonAstJson = "";
        this.jinjaAstJson = "";
        this.pythonContextExtractor = new PythonContextExtractor();
        this.pythonSourceFiles = new ArrayList<>();
        this.jinjaOutputFiles = new ArrayList<>();
    }

    // ─── Phase 1-2: Discover & Parse Python Sources ───────────

    public void discoverPythonSources() throws IOException {
        Path dir = Paths.get(projectDir);
        if (!Files.exists(dir)) {
            errors.add("Project directory does not exist: " + projectDir);
            return;
        }
        // Only parse app.py in the project root — the single source file
        // that defines context variables for templates.
        Path appPy = dir.resolve("app.py");
        if (Files.exists(appPy)) {
            pythonSourceFiles.add(appPy.toString());
            System.out.println("Python source: " + appPy);
        } else {
            errors.add("app.py not found in project directory: " + projectDir);
        }
    }

    // ─── Phase 5: Extract Global Variables into Context ────────

    public void extractPythonContext() {
        CompilerErrorListener syntaxListener = new CompilerErrorListener("app.py");
        for (String pyFile : pythonSourceFiles) {
            try {
                String source = Files.readString(Paths.get(pyFile));
                PythonLexer lexer = new PythonLexer(CharStreams.fromString(source));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                PythonParser parser = new PythonParser(tokens);
                parser.removeErrorListeners();
                parser.addErrorListener(syntaxListener);
                ParserRuleContext tree = parser.program();
                // Defensive null check (parser currently never returns null)
                if (tree == null) continue;
                ASTNode ast = new ASTBuilder().visit(tree);
                if (ast instanceof ProgramNode) {
                    ProgramNode pn = (ProgramNode) ast;
                }
                pythonContextExtractor.extract((ProgramNode) ast);
            } catch (Exception e) {
                errors.add("Python context extraction failed for " + pyFile + ": " + e.getMessage());
            }
        }
        if (syntaxListener.hasErrors()) {
            syntaxListener.writeReport(projectDir + "/compiler_output");
        }
        if (!pythonContextExtractor.getWarnings().isEmpty()) {
            for (String warn : pythonContextExtractor.getWarnings()) {
                System.out.println("Warning: " + warn);
            }
        }
    }

    // ─── Phase 3: Discover Jinja2 Templates ───────────────────

    public void discoverTemplates() throws IOException {
        Path templatesDir = Paths.get(projectDir, "templates");
        if (!Files.exists(templatesDir)) {
            errors.add("templates/ directory does not exist: " + templatesDir);
            return;
        }
        // Only scan templates/ directory for .jinja files — not recursively
        try (Stream<Path> walk = Files.walk(templatesDir, 1)) {
            walk.filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".jinja"))
                .map(Path::toString)
                .sorted()
                .forEach(templateFiles::add);
        }
    }

    // ─── Phase 4: Parse Templates (AST Builder) ───────────────

    public void compileAll() {
        for (String templatePath : templateFiles) {
            compileTemplate(templatePath);
        }
    }

    public void compileTemplate(String templatePath) {
        try {
            String source = Files.readString(Paths.get(templatePath));
            compileTemplate(templatePath, source);
        } catch (IOException e) {
            errors.add("Failed to read template: " + templatePath + " - " + e.getMessage());
        }
    }

    public void compileTemplate(String templatePath, String source) {
        CompilerErrorListener syntaxListener = new CompilerErrorListener(templatePath);
        try {
            Jinja2Lexer lexer = new Jinja2Lexer(CharStreams.fromString(source));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            Jinja2Parser parser = new Jinja2Parser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(syntaxListener);

            Jinja2ASTBuilder jinjaBuilder = new Jinja2ASTBuilder();
            Jinja2Node jinjaAst = jinjaBuilder.build(templatePath, source);

            if (jinjaBuilder.getSyntaxListener() != null && jinjaBuilder.getSyntaxListener().hasErrors()) {
                syntaxListener.writeReport(projectDir + "/compiler_output");
            }
            if (jinjaAst == null) {
                errors.add("Jinja2 AST construction failed for: " + templatePath);
                return;
            }

            System.out.println("=== Jinja2 AST for: " + templatePath + " ===");
            jinjaAst.printTree(0);
            System.out.println();

            jinjaAstJson = jinjaAst.toJson();

            Context ctx = pythonContextExtractor.getContext();
            HtmlCodeGenerator htmlGen = new HtmlCodeGenerator(ctx);
            String htmlOutput = htmlGen.generate(jinjaAst);

            String outputPath = generateHtmlOutputPath(templatePath);
            try {
                Path outDir = Paths.get(projectDir, "output");
                if (!Files.exists(outDir)) {
                    Files.createDirectories(outDir);
                }
                Files.writeString(Paths.get(outputPath), htmlOutput);
                outputFiles.add(outputPath);
            } catch (IOException e) {
                errors.add("Failed to write HTML output for " + templatePath + " - " + e.getMessage());
            }

        } catch (Exception e) {
            errors.add("Compilation error for " + templatePath + ": " + e.getMessage());
        }
    }

    // ─── Phase 6: Jinja2 → HTML (Static Site Generator Style) ─

    /**
     * Phase 6: Render ALL .jinja templates using the global Context.
     *
     * This follows a static-site-generator architecture:
     *   1. Scan the templates/ directory for ALL .jinja files
     *   2. For EACH file, parse it into a Jinja2 AST
     *   3. Pass the global Context (from app.py) to the HtmlCodeGenerator
     *   4. Write the rendered HTML to output/<basename>.html
     *
     * No render_template() calls are needed - the compiler reads data
     * and applies it to every template automatically.
     */
    public void compileJinjaTemplates() {
        Context ctx = pythonContextExtractor.getContext();
        System.out.println("\n=== Phase 6: Jinja2 → HTML Generation (Static Site Generator) ===");
        System.out.println("Global Context entries: " + ctx.size());

        // Collect all .jinja files from templates/ directory
        List<Path> jinjaFiles = findJinjaFiles();
        System.out.println("Found " + jinjaFiles.size() + " .jinja template(s) in templates/");

        // Inject derived dashboard stats that templates reference (e.g. index.jinja)
        ctx.assign("context_size", ctx.size());
        Context.PythonList templateList = new Context.PythonList();
        for (Path p : jinjaFiles) {
            templateList.add(p.getFileName().toString());
        }
        ctx.assign("templates", templateList);

        int successCount = 0;
        int failCount = 0;
        List<CompilerErrorListener> jinjaSyntaxListeners = new ArrayList<>();

        for (Path templatePath : jinjaFiles) {
            String templateName = templatePath.getFileName().toString();
            System.out.println("\nCompiling template: " + templateName);

            try {
                String source = Files.readString(templatePath);
                Jinja2ASTBuilder jinjaBuilder = new Jinja2ASTBuilder();
                Jinja2Node jinjaAst = jinjaBuilder.build(templatePath.toString(), source);

                if (jinjaBuilder.getSyntaxListener() != null && jinjaBuilder.getSyntaxListener().hasErrors()) {
                    jinjaSyntaxListeners.add(jinjaBuilder.getSyntaxListener());
                }

                if (jinjaAst == null) {
                    System.out.println("  Jinja2 AST construction failed for: " + templateName);
                    failCount++;
                    continue;
                }

                System.out.println("  Jinja2 AST built successfully");

                // Semantic analysis of this template against the global Context
                Jinja2SemanticChecker semanticChecker = new Jinja2SemanticChecker(source, templateName, ctx.snapshot());
                List<SemanticError> semanticIssues = semanticChecker.check();
                if (!semanticIssues.isEmpty()) {
                    System.out.println("  Semantic issues (" + templateName + "):");
                    for (SemanticError se : semanticIssues) {
                        System.out.println("    - " + se);
                    }
                }

                // Use the global Context directly - no template-specific overrides
                HtmlCodeGenerator htmlGen = new HtmlCodeGenerator(ctx);
                String htmlOutput = htmlGen.generate(jinjaAst);

                // Write to output/<basename>.html
                String baseName = templateName;
                int dotIdx = baseName.lastIndexOf('.');
                if (dotIdx > 0) {
                    baseName = baseName.substring(0, dotIdx);
                }
                Path outDir = Paths.get(projectDir, "output");
                if (!Files.exists(outDir)) {
                    Files.createDirectories(outDir);
                }
                Path outFile = outDir.resolve(baseName + ".html");
                Files.writeString(outFile, htmlOutput);
                System.out.println("  HTML written to: " + outFile);
                System.out.println("  Output size: " + htmlOutput.length() + " characters");

                jinjaOutputFiles.add(outFile.toString());
                successCount++;

            } catch (Exception e) {
                failCount++;
                String errorMsg = "Jinja2 HTML generation failed for " + templateName + ": " + e.getMessage();
                errors.add(errorMsg);
                System.err.println("  ⚠️  Failed to process template: " + templateName + ". Skipping...");
                System.err.println("  Error: " + e.getMessage());
            }
        }

        // Write Jinja2 syntax reports
        for (CompilerErrorListener listener : jinjaSyntaxListeners) {
            listener.writeReport(projectDir + "/compiler_output");
        }

        // Copy static assets (script.js, style.css) into output/ so every
        // generated page can reference them with a plain relative <script>/<link>.
        copyStaticAssets();

        System.out.println("\n=== Phase 6 Complete ===");
        System.out.println("Templates compiled: " + successCount + " succeeded, " + failCount + " failed");
    }

    /**
     * Copy static assets (script.js, style.css) from the project root into
     * the output/ directory.
     *
     * The templates reference these files with relative paths
     * (e.g. &lt;script src="script.js"&gt;&lt;/script&gt;), so they must be placed
     * alongside the generated HTML for the site to be fully self-contained.
     *
     * Missing assets are skipped with a warning instead of failing the build.
     */
    public void copyStaticAssets() {
        Path outDir = Paths.get(projectDir, "output");
        String[] assets = {"script.js", "style.css"};
        for (String asset : assets) {
            Path src = Paths.get(projectDir, asset);
            if (!Files.exists(src)) {
                System.out.println("  [static] Skipping missing asset: " + asset);
                continue;
            }
            try {
                if (!Files.exists(outDir)) {
                    Files.createDirectories(outDir);
                }
                Path dest = outDir.resolve(asset);
                Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("  [static] Copied " + asset + " -> " + dest);
            } catch (IOException e) {
                errors.add("Failed to copy static asset " + asset + ": " + e.getMessage());
            }
        }
    }

    /**
     * Scan the templates/ directory for ALL .jinja files.
     */
    private List<Path> findJinjaFiles() {
        List<Path> jinjaFiles = new ArrayList<>();
        Path templatesDir = Paths.get(projectDir, "templates");
        if (!Files.exists(templatesDir)) {
            System.out.println("  templates/ directory not found at: " + templatesDir);
            // Fallback: check all discovered template files
            for (String tf : templateFiles) {
                Path p = Paths.get(tf);
                if (p.getFileName().toString().toLowerCase().endsWith(".jinja")) {
                    jinjaFiles.add(p);
                }
            }
            return jinjaFiles;
        }
        try (Stream<Path> walk = Files.walk(templatesDir, 1)) {
            walk.filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".jinja"))
                .sorted()
                .forEach(jinjaFiles::add);
        } catch (IOException e) {
            errors.add("Failed to scan templates/ directory: " + e.getMessage());
        }
        return jinjaFiles;
    }

    // ─── Generation Log ───────────────────────────────────────

    public void writeGenerationLog() {
        try {
            Path outDir = Paths.get(projectDir).resolve("compiler_output");
            if (!Files.exists(outDir)) {
                Files.createDirectories(outDir);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("=== Global Context (extracted from Python source) ===\n\n");
            sb.append("Context Summary:\n");
            sb.append(pythonContextExtractor.getContext().toString()).append("\n\n");

            sb.append("Templates discovered (Phase 3):\n");
            if (templateFiles.isEmpty()) {
                sb.append("  (none)\n");
            } else {
                for (int i = 0; i < templateFiles.size(); i++) {
                    sb.append("  ").append(i + 1).append(". ").append(templateFiles.get(i)).append("\n");
                }
            }

            sb.append("\nCode-gen outputs (Phase 4):\n");
            if (outputFiles.isEmpty()) {
                sb.append("  (none)\n");
            } else {
                for (int i = 0; i < outputFiles.size(); i++) {
                    sb.append("  ").append(i + 1).append(". ").append(outputFiles.get(i)).append("\n");
                }
            }

            sb.append("\nHTML outputs (Phase 6 - Static Site Generator):\n");
            if (jinjaOutputFiles.isEmpty()) {
                sb.append("  (none)\n");
            } else {
                for (int i = 0; i < jinjaOutputFiles.size(); i++) {
                    sb.append("  ").append(i + 1).append(". ").append(jinjaOutputFiles.get(i)).append("\n");
                }
            }

            Files.writeString(outDir.resolve("generation_log.txt"), sb.toString());
            System.out.println("Generation log written to: compiler_output/generation_log.txt");
        } catch (Exception e) {
            errors.add("Failed to write generation log: " + e.getMessage());
        }
    }

    // ─── Helpers ──────────────────────────────────────────────

    private String generateHtmlOutputPath(String templatePath) {
        Path input = Paths.get(templatePath);
        String filename = input.getFileName().toString();
        String baseName = filename;
        int dotIdx = baseName.lastIndexOf('.');
        if (dotIdx > 0) {
            baseName = baseName.substring(0, dotIdx);
        }
        String outDir = projectDir + "/output";
        return Paths.get(outDir, baseName + ".html").toString();
    }

    private String generateOutputPath(String templatePath) {
        Path input = Paths.get(templatePath);
        String filename = input.getFileName().toString();
        String baseName = filename;
        int dotIdx = baseName.lastIndexOf('.');
        if (dotIdx > 0) {
            baseName = baseName.substring(0, dotIdx);
        }
        String outDir = projectDir + "/out";
        Path outPath = Paths.get(outDir, baseName + ".py");
        return outPath.toString();
    }

    private void generateCode(ASTNode ast, String outputPath) {
        try {
            CodeGenerator codeGen = new CodeGenerator((ProgramNode) ast);
            String pythonCode = codeGen.generate();

            Path outDir = Paths.get(outputPath).getParent();
            if (outDir != null && !Files.exists(outDir)) {
                Files.createDirectories(outDir);
            }
            Files.writeString(Paths.get(outputPath), pythonCode);
        } catch (IOException e) {
            errors.add("Failed to write output: " + outputPath + " - " + e.getMessage());
        } catch (ClassCastException e) {
            errors.add("AST root is not a ProgramNode for: " + outputPath);
        }
    }

    public List<String> getTemplateFiles() {
        return Collections.unmodifiableList(templateFiles);
    }

    public List<String> getOutputFiles() {
        return Collections.unmodifiableList(outputFiles);
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public int getErrorCount() {
        return errors.size();
    }

    public String getPythonAstJson() { return pythonAstJson; }
    public String getJinjaAstJson() { return jinjaAstJson; }
}
