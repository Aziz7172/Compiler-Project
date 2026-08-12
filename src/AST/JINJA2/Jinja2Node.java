package AST.JINJA2;

import AST.ASTNode;
import AST.ASTVisitor;

public abstract class Jinja2Node extends ASTNode {
    protected String rawText;

    public Jinja2Node() {
        super("Jinja2Node", 0, 0);
    }

    public Jinja2Node(String nodeType, int lineNumber, int columnNumber) {
        super(nodeType, lineNumber, columnNumber);
        this.rawText = null;
    }

    public String getRawText() { return rawText; }
    public void setRawText(String rawText) { this.rawText = rawText; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        return formatLine(prefix, isTail, getNodeType() + (rawText != null ? " = \"" + truncate(rawText, 40) + "\"" : "") + " (Line: " + lineNumber + ")");
    }

    @Override
    public String generateCode() {
        return rawText != null ? rawText : "";
    }

    @Override
    public void accept(ASTVisitor visitor) {
        throw new UnsupportedOperationException("Jinja2Node.accept() should be overridden by subclasses");
    }
}