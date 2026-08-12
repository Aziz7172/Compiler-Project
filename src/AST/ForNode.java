package AST;

import java.util.List;

public class ForNode extends ConnectorNode {
    private String variable;
    private ASTNode iterable;

    public ForNode(String variable, ASTNode iterable, List<ASTNode> body) {
        super();
        this.variable = variable;
        this.iterable = iterable;
        if (body != null) {
            addChildren(body);
        }
    }

    public String getVariable() { return variable; }
    public ASTNode getIterable() { return iterable; }
    public List<ASTNode> getBody() { return children; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "ForNode (Line: " + lineNumber
                + ", var='" + variable + "'"
                + ", iterable='" + truncate(iterable != null ? iterable.generateCode() : "", 40) + "')"));
        String childPrefix = nextPrefix(prefix, isTail);
        for (int i = 0; i < children.size(); i++) {
            boolean last = i == children.size() - 1;
            sb.append(children.get(i).toTreeString(childPrefix, last));
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("for ").append(variable).append(" in ").append(iterable != null ? iterable.generateCode() : "").append(":\n");
        for (ASTNode node : children) {
            String code = node.generateCode();
            if (code != null && !code.isEmpty()) {
                for (String line : code.split("\n")) {
                    builder.append("    ").append(line).append("\n");
                }
            }
        }
        return builder.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"variable\": ").append(jsonString(variable)).append(",\n");
        sb.append("  \"iterable\": ").append(iterable != null ? iterable.toJson() : "null").append(",\n");
        sb.append("  \"children\": [\n");
        for (int i = 0; i < children.size(); i++) {
            sb.append(children.get(i).toJson());
            if (i < children.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}