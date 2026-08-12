import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import AST.ASTBuilder;
import AST.ASTNode;
import AST.ProgramNode;
import ErrorHandling.CompilerErrorListener;
import gen.ANTLR.PythonLexer;
import gen.ANTLR.PythonParser;
import SemanticAnalysis.SemanticAnalyzer;
import SymbolTable.SymbolTable;

public class Main {
    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.WARNING);
        String projectDir = ".";
        String singleFile = null;
        boolean singleFileMode = false;

        if (args.length > 0) {
            String arg = args[0];
            if (arg.endsWith(".py") || arg.endsWith(".txt") || arg.endsWith(".jinja")) {
                singleFileMode = true;
                singleFile = arg;
            } else {
                projectDir = arg;
            }
        }

        try {
            if (singleFileMode) {
                runSingleFile(singleFile);
            } else {
                CompilerPipeline pipeline = new CompilerPipeline(projectDir);
                pipeline.run();
                System.out.println("Pipeline finished successfully.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void runSingleFile(String filePath) throws IOException {
        System.out.println("=== Academic Test Runner Mode ===");
        System.out.println("Processing: " + filePath);

        String source = Files.readString(Paths.get(filePath));

        CompilerErrorListener syntaxListener = new CompilerErrorListener(filePath);
        PythonLexer lexer = new PythonLexer(CharStreams.fromString(source));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PythonParser parser = new PythonParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(syntaxListener);

        ParserRuleContext tree = parser.program();
        if (syntaxListener.hasErrors()) {
            syntaxListener.writeReport("./compiler_output");
        }
        // Defensive null check (parser currently never returns null)
        if (tree == null) {
            System.err.println("Parsing failed: tree is null");
            return;
        }

        System.out.println("\n=== AST Tree ===");
        ASTBuilder astBuilder = new ASTBuilder();
        ASTNode ast = astBuilder.visit(tree);
        if (ast != null) {
            ast.printTree(0);
        } else {
            System.out.println("(AST root is null)");
        }

        System.out.println("\n=== Semantic Analysis ===");
        SymbolTable symbolTable = new SymbolTable();
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(symbolTable);
        if (ast instanceof ProgramNode) {
            symbolTable.enterScope("global");
            semanticAnalyzer.analyze(ast);
            symbolTable.exitScope();
        } else {
            semanticAnalyzer.analyze(ast);
        }

        List<SemanticAnalysis.SemanticError> errors = semanticAnalyzer.getSemanticErrors();
        System.out.println("Semantic errors found: " + errors.size());
        for (SemanticAnalysis.SemanticError err : errors) {
            System.out.println("  " + err.toString());
        }

        try {
            semanticAnalyzer.writeReport("./compiler_output");
        } catch (Exception e) {
            System.err.println("Failed to write semantic report: " + e.getMessage());
        }
    }
}