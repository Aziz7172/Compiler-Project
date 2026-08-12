package AST;

import java.util.ArrayList;
import java.util.List;

public class ElifNode extends ConnectorNode {
    private ASTNode condition;

    public ElifNode(ASTNode condition, List<ASTNode> body) {
        super();
        this.condition = condition;
        if (body != null) {
            addChildren(body);
        }
    }

    public ASTNode getCondition() { return condition; }
    public List<ASTNode> getBody() { return children; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "ElifNode (Line: " + lineNumber + ")"));
        String childPrefix = nextPrefix(prefix, isTail);
        if (condition != null) {
            sb.append(formatLine(childPrefix, false, "Condition"));
            sb.append(condition.toTreeString(nextPrefix(childPrefix, false), true));
        }
        for (int i = 0; i < children.size(); i++) {
            sb.append(children.get(i).toTreeString(childPrefix, i == children.size() - 1));
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("elif ").append(condition != null ? condition.generateCode() : "").append(":\n");
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