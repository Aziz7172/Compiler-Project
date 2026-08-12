package AST;

public class NumberNode extends ASTNode {
    public String value;

    public NumberNode(String value) {
        this.value = value;
    }

    public String getType() {
        if (value.contains("."))
            return "double";
        else
            return "int";
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        return formatLine(prefix, isTail, "NumberNode(" + value + ")");
    }

    @Override
    public String generateCode() {
        return value;
    }

    @Override
    public String toString() {
        return "NumberNode{" +
                "value=" + value +
                '}';
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeName\": ").append(jsonString(nodeType)).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"value\": ").append(value).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
