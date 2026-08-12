package AST;

import java.util.List;

public class WhileNode extends ConnectorNode {
    private ASTNode condition;

    public WhileNode(ASTNode condition, List<ASTNode> body) {
        super();
        this.condition = condition;
        if (body != null) {
            addChildren(body);
        }
    }

    public ASTNode getCondition() { return condition; }
    public ASTNode getExpression() { return condition; }
    public List<ASTNode> getBody() { return children; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "WhileNode (Line: " + lineNumber
                + ", condition='" + truncate(condition != null ? condition.generateCode() : "", 40) + "')"));
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
        builder.append("while ").append(condition != null ? condition.generateCode() : "").append(":\n");
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
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}