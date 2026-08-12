package ErrorHandling;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Custom ANTLR error listener that collects syntax errors
 * and writes them to compiler_output/syntax_report.txt.
 *
 * Attach to both Python and Jinja2 Lexer/Parser instances.
 * The compiler MUST NOT crash on the first error — all errors
 * are collected and reported after parsing completes.
 */
public class CompilerErrorListener implements ANTLRErrorListener {

    private final String sourceName;
    private final List<SyntaxError> errors;

    /**
     * A single syntax error record.
     */
    public static class SyntaxError {
        public final int line;
        public final int column;
        public final String message;
        public final String sourceName;

        public SyntaxError(int line, int column, String message, String sourceName) {
            this.line = line;
            this.column = column;
            this.message = message;
            this.sourceName = sourceName;
        }

        @Override
        public String toString() {
            return String.format(Locale.ROOT,
                    "[%s] Line %d, Col %d: %s",
                    sourceName, line, column, message);
        }
    }

    public CompilerErrorListener(String sourceName) {
        this.sourceName = sourceName;
        this.errors = new ArrayList<>();
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg,
                            RecognitionException e) {
        errors.add(new SyntaxError(line, charPositionInLine, msg, sourceName));
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, java.util.BitSet ambigAlts, ATNConfigSet configs) {
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, java.util.BitSet conflictingAlts, ATNConfigSet configs) {
    }

    public List<SyntaxError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Write all collected syntax errors to compiler_output/syntax_report.txt.
     */
    public void writeReport(String outputDir) {
        if (errors.isEmpty()) return;

        try {
            Path outDir = Paths.get(outputDir);
            if (!Files.exists(outDir)) {
                Files.createDirectories(outDir);
            }
            Path reportPath = outDir.resolve("syntax_report.txt");

            StringBuilder sb = new StringBuilder();
            sb.append("=== Syntax Error Report ===\n");
            sb.append("Date: ").append(new Date()).append("\n");
            sb.append("Total errors: ").append(errors.size()).append("\n\n");

            for (int i = 0; i < errors.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(errors.get(i).toString()).append("\n");
            }

            Files.writeString(reportPath, sb.toString(), StandardCharsets.UTF_8);
            System.out.println("Syntax report written to: " + reportPath);
        } catch (IOException e) {
            System.err.println("Failed to write syntax report: " + e.getMessage());
        }
    }
}
