package AST;

public class ReturnNode extends ASTNode {
    private ASTNode value;

    public ReturnNode(ASTNode value) {
        super("ReturnNode", 0, 0);
        this.value = value;
    }

    public ASTNode getValue() { return value; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "ReturnNode (Line: " + lineNumber + ")"));
        if (value != null) {
            sb.append(value.toTreeString(nextPrefix(prefix, isTail), true));
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        return "return " + (value != null ? value.generateCode() : "") + "";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"value\": ").append(value != null ? value.toJson() : "null").append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}