package AST;

public class UnaryNode extends ASTNode {
    public String operator;
    public ASTNode expression;

    public UnaryNode(String operator, ASTNode expression) {
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "UnaryNode(" + operator + ")"));
        sb.append(expression.toTreeString(nextPrefix(prefix, true), true));
        return sb.toString();
    }

    @Override
    public String generateCode() {
        return operator + expression.generateCode();
    }

    @Override
    public String toString() {
        return "UnaryNode{" +
                "operator='" + operator + '\'' +
                ", expression=" + expression +
                '}';
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeName\": ").append(jsonString(nodeType)).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"operator\": ").append(jsonString(operator)).append(",\n");
        sb.append("  \"expression\": ").append(expression != null ? expression.toJson() : "null").append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
