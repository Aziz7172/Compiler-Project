package AST;

import java.util.ArrayList;
import java.util.List;

public class ElseNode extends ConnectorNode {
    public ElseNode() {
        super();
    }

    public ElseNode(List<ASTNode> body) {
        super();
        if (body != null) {
            addChildren(body);
        }
    }

    public List<ASTNode> getBody() { return children; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "ElseNode (Line: " + lineNumber + ")"));
        String childPrefix = nextPrefix(prefix, isTail);
        for (int i = 0; i < children.size(); i++) {
            sb.append(children.get(i).toTreeString(childPrefix, i == children.size() - 1));
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("else:\n");
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