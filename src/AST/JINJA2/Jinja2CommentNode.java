package AST.JINJA2;

import AST.ASTVisitor;

public class Jinja2CommentNode extends Jinja2Node {
    public Jinja2CommentNode() {
        super("Jinja2CommentNode", 0, 0);
    }

    public Jinja2CommentNode(String comment) {
        super("Jinja2CommentNode", 0, 0);
        this.rawText = comment;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        return formatLine(prefix, isTail, "Jinja2CommentNode (Line: " + lineNumber + ") = \"" + truncate(rawText, 40) + "\"");
    }

    @Override
    public String generateCode() {
        return "";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"comment\": ").append(jsonString(rawText)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}