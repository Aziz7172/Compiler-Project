package AST;

public class IdentifierNode extends ASTNode {
    private String name;

    public IdentifierNode(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        return formatLine(prefix, isTail, "IdentifierNode(" + name + ")");
    }

    @Override
    public String generateCode() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeName\": ").append(jsonString(nodeType)).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"name\": ").append(jsonString(name)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
