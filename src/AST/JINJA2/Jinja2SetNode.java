package AST.JINJA2;

import AST.ASTVisitor;

public class Jinja2SetNode extends Jinja2Node {
    private String assignment;

    public Jinja2SetNode(String assignment) {
        super("Jinja2SetNode", 0, 0);
        this.assignment = assignment;
    }

    public String getAssignment() { return assignment; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        return formatLine(prefix, isTail, "Jinja2SetNode (Line: " + lineNumber
                + ", assignment='" + (assignment != null ? assignment : "") + "')");
    }

    @Override
    public String generateCode() {
        return "{% set " + assignment + " %}";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"assignment\": ").append(jsonString(assignment)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}