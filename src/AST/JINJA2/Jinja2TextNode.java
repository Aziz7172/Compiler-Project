package AST.JINJA2;

import AST.ASTVisitor;

public class Jinja2TextNode extends Jinja2Node {
    public Jinja2TextNode() {
        super("Jinja2TextNode", 0, 0);
    }

    public Jinja2TextNode(String text) {
        super("Jinja2TextNode", 0, 0);
        this.rawText = text;
    }

    public String getText() { return rawText; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        return formatLine(prefix, isTail, "Jinja2TextNode (Line: " + lineNumber + ") = \"" + truncate(rawText, 40) + "\"");
    }

    @Override
    public String generateCode() {
        return rawText != null ? rawText : "";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"text\": ").append(jsonString(rawText)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}