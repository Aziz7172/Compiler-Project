package AST;

public class ArgumentNode extends ASTNode {
    private String name;
    private ASTNode value;

    public ArgumentNode(String name, ASTNode value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }

    public ASTNode getValue() { return value; }

    @Override
    public String generateCode() {
        if (value == null) {
            return name != null ? name : "";
        }
        if (name != null && !name.isEmpty()) {
            return name + "=" + value.generateCode();
        }
        return value.generateCode();
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder builder = new StringBuilder();
        builder.append(formatLine(prefix, isTail, "ArgumentNode(" + name + ")"));
        if (value != null) {
            builder.append(value.toTreeString(nextPrefix(prefix, true), true));
        }
        return builder.toString();
    }

    @Override
    public String toJson() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("  \"nodeName\": ").append(jsonString(nodeType)).append(",\n");
        builder.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        builder.append("  \"name\": ").append(jsonString(name)).append(",\n");
        builder.append("  \"value\": ").append(value != null ? value.toJson() : "null").append("\n");
        builder.append("}");
        return builder.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
