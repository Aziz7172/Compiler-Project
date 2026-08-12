package AST;

public class AssignmentNode extends DefinitionNode {
    public ASTNode value;

    public AssignmentNode(String variableName, ASTNode value) {
        super("VariableDefNode", variableName, 0, 0);
        this.value = value;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder builder = new StringBuilder();
        builder.append(formatLine(prefix, isTail, getNodeType() + " (" + name + ") (Line: " + lineNumber + ")"));
        if (value != null) {
            builder.append(value.toTreeString(nextPrefix(prefix, isTail), true));
        }
        return builder.toString();
    }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append(" = ");
        if (value != null) {
            builder.append(value.generateCode());
        }
        return builder.toString();
    }

    @Override
    public String toJson() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        builder.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        builder.append("  \"name\": ").append(jsonString(name)).append(",\n");
        builder.append("  \"dataType\": ").append(dataType != null ? jsonString(dataType) : "null").append(",\n");
        builder.append("  \"value\": ").append(value != null ? value.toJson() : "null").append("\n");
        builder.append("}");
        return builder.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}