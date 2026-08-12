import java.util.List;

public class CompilerPipeline {
    private final String projectDir;

    public CompilerPipeline(String projectDir) {
        this.projectDir = projectDir;
    }

    public void run() {
        System.out.println("CompilerPipeline running for: " + projectDir);
        try {
            TemplateManager manager = new TemplateManager(projectDir);

            // Phase 1-2: Parse Python source files
            System.out.println("\n--- Phase 1-2: Discovering & Parsing Python sources ---");
            manager.discoverPythonSources();

            // Phase 3-4: Parse templates (AST builder)
            System.out.println("\n--- Phase 3-4: Discovering & Parsing Jinja2 templates ---");
            manager.discoverTemplates();
            List<String> templates = manager.getTemplateFiles();
            System.out.println("Found " + templates.size() + " template(s).");
            if (!templates.isEmpty()) {
                System.out.println("Compiling templates...");
                manager.compileAll();
                List<String> errors = manager.getErrors();
                if (!errors.isEmpty()) {
                    System.out.println("Errors encountered during compilation: " + errors.size());
                    for (String err : errors) {
                        System.err.println("  - " + err);
                    }
                } else {
                    System.out.println("All templates compiled successfully.");
                }
            }

            // Phase 5: Extract global variables from Python into Context
            System.out.println("\n--- Phase 5: Extracting global variables into Context ---");
            manager.extractPythonContext();

            // Phase 6: Jinja2 → HTML (Static Site Generator style)
            System.out.println("\n--- Phase 6: Rendering templates with global Context ---");
            manager.compileJinjaTemplates();

            List<String> errors = manager.getErrors();
            if (!errors.isEmpty()) {
                System.out.println("\nTotal errors: " + errors.size());
                for (String err : errors) {
                    System.err.println("  - " + err);
                }
            }

            List<String> outputs = manager.getOutputFiles();
            System.out.println("\nGenerated " + outputs.size() + " code-gen output file(s):");
            for (String out : outputs) {
                System.out.println("  -> " + out);
            }

            manager.writeGenerationLog();

        } catch (Exception e) {
            System.err.println("Pipeline error: " + e.getMessage());
        }
    }
}
