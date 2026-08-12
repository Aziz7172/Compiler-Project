package AST.JINJA2;

import AST.ASTVisitor;

public class Jinja2ExprNode extends Jinja2Node {
    private String expression;

    public Jinja2ExprNode() {
        super("Jinja2ExprNode", 0, 0);
        this.expression = null;
    }

    public Jinja2ExprNode(String expression) {
        super("Jinja2ExprNode", 0, 0);
        this.expression = expression;
    }

    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        return formatLine(prefix, isTail, "Jinja2ExprNode (Line: " + lineNumber + ") = " + (expression != null ? expression.substring(0, Math.min(expression.length(), 40)) : ""));
    }

    @Override
    public String generateCode() {
        return expression != null ? "{{ " + expression + " }}" : "";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"expression\": ").append(jsonString(expression)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}