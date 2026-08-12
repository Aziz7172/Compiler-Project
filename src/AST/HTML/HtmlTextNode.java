package AST.HTML;

import AST.ASTNode;
import AST.ASTVisitor;

public class HtmlTextNode extends ASTNode {
    private String text;

    public HtmlTextNode() {
        super("HtmlTextNode", 0, 0);
    }

    public HtmlTextNode(String text) {
        super("HtmlTextNode", 0, 0);
        this.text = text;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        return formatLine(prefix, isTail, "HtmlTextNode (Line: " + lineNumber + ") = " + (text != null ? text.substring(0, Math.min(text.length(), 50)) : ""));
    }

    @Override
    public String generateCode() {
        return text != null ? text : "";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"text\": ").append(jsonString(text)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}