package SemanticAnalysis;

import java.util.Locale;

public class SemanticError {
    public enum ErrorType {
        UNDEFINED_VARIABLE,
        TYPE_ERROR,
        TYPE_MISMATCH,
        SCOPE_ERROR,
        MISSING_FLASK_VARIABLE,
        DUPLICATE_FLASK_ROUTE,
        RETURN_OUTSIDE_FUNCTION,
        FUNCTION_ARGUMENT_MISMATCH,
        UNDEFINED_CONTEXT_VARIABLE,
        UNCLOSED_JINJA_BLOCK,
        INVALID_ITERATION_TYPE
    }

    private final ErrorType type;
    private final int line;
    private final int column;
    private final String message;

    public SemanticError(ErrorType type, int line, int column, String message) {
        this.type = type;
        this.line = line;
        this.column = column;
        this.message = message;
    }

    public SemanticError(ErrorType type, int line, String message) {
        this(type, line, 0, message);
    }

    public ErrorType getType() { return type; }
    public int getLine() { return line; }
    public int getColumn() { return column; }
    public String getMessage() { return message; }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[Line: %d, Col: %d] (%s): %s", line, column, type, message);
    }
}