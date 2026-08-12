package AST;

public class BinaryOpNode extends ASTNode {
    public String operator;
    public ASTNode left;
    public ASTNode right;

    public BinaryOpNode(String operator, ASTNode left, ASTNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "BinaryOpNode(" + operator + ")"));
        sb.append(left.toTreeString(nextPrefix(prefix, false), false));
        sb.append(right.toTreeString(nextPrefix(prefix, true), true));
        return sb.toString();
    }


    @Override
    public String generateCode() {
        return left.generateCode() + " " + operator + " " + right.generateCode();
    }

    @Override
    public String toString() {
        return "BinaryOpNode{" +
                "operator='" + operator + '\'' +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeName\": ").append(jsonString(nodeType)).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"operator\": ").append(jsonString(operator)).append(",\n");
        sb.append("  \"left\": ").append(left != null ? left.toJson() : "null").append(",\n");
        sb.append("  \"right\": ").append(right != null ? right.toJson() : "null").append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
