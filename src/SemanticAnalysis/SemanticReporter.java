package SemanticAnalysis;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * SemanticReporter writes all detected semantic errors to a file
 * at compiler_output/semantic_report.txt.
 *
 * The report includes:
 *   - A header with the report date and total error count
 *   - One line per error: [Line: N, Col: M] (TYPE): description
 *   - A summary of errors by type
 */
public class SemanticReporter {

    private final String outputDir;

    /**
     * Creates a reporter that writes to the given output directory.
     *
     * @param outputDir the path to the compiler_output directory
     */
    public SemanticReporter(String outputDir) {
        this.outputDir = outputDir;
    }

    /**
     * Writes the semantic report to compiler_output/semantic_report.txt.
     *
     * @param errors the list of SemanticError objects to write
     * @throws IOException if the file cannot be written
     */
    public void writeReport(List<SemanticError> errors) throws IOException {
        Path outputPath = Paths.get(outputDir, "semantic_report.txt");

        // Ensure the output directory exists
        Path parentDir = outputPath.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }

        StringBuilder report = new StringBuilder();

        // Header
        report.append("=== Semantic Analysis Report ===\n");
        report.append("Date: ").append(new Date()).append("\n\n");

        if (errors.isEmpty()) {
            report.append("No semantic errors found.\n");
        } else {
            report.append("Errors found: ").append(errors.size()).append("\n\n");

            // Group errors by type for summary
            java.util.Map<String, Integer> errorCounts = new java.util.LinkedHashMap<>();
            for (SemanticError error : errors) {
                String typeName = error.getType().name();
                errorCounts.merge(typeName, 1, Integer::sum);
            }

            // Detail listing
            for (int i = 0; i < errors.size(); i++) {
                SemanticError error = errors.get(i);
                report.append("  ").append(i + 1).append(". ")
                        .append(error.toString()).append("\n");
            }

            // Summary by type
            report.append("\n--- Summary by Type ---\n");
            for (java.util.Map.Entry<String, Integer> entry : errorCounts.entrySet()) {
                report.append(String.format(Locale.ROOT, "  %-30s : %d\n", entry.getKey(), entry.getValue()));
            }
        }

        // Write to file
        Files.writeString(outputPath, report.toString(), StandardCharsets.UTF_8);
    }

    /**
     * Utility method to write errors from any SemanticAnalyzer instance.
     *
     * @param outputDir the output directory path
     * @param errors the list of semantic errors
     * @throws IOException if the file cannot be written
     */
    public static void writeErrors(String outputDir, List<SemanticError> errors) throws IOException {
        new SemanticReporter(outputDir).writeReport(errors);
    }
}