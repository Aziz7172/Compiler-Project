package AST;

import java.util.List;

public class PrintNode extends ASTNode {
    private List<ASTNode> nodes;

    public PrintNode(List<ASTNode> nodes) {
        super("PrintNode", 0, 0);
        this.nodes = nodes;
    }

    public List<ASTNode> getNodes() { return nodes; }

    public List<ASTNode> getContents() { return nodes; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "PrintNode (Line: " + lineNumber + ")"));
        String childPrefix = nextPrefix(prefix, isTail);
        if (nodes != null) {
            for (int i = 0; i < nodes.size(); i++) {
                sb.append(nodes.get(i).toTreeString(childPrefix, i == nodes.size() - 1));
            }
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("print(");
        if (nodes != null) {
            for (int i = 0; i < nodes.size(); i++) {
                if (i > 0) builder.append(", ");
                builder.append(nodes.get(i).generateCode());
            }
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"nodes\": [\n");
        if (nodes != null) {
            for (int i = 0; i < nodes.size(); i++) {
                sb.append(nodes.get(i).toJson());
                if (i < nodes.size() - 1) sb.append(",");
                sb.append("\n");
            }
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