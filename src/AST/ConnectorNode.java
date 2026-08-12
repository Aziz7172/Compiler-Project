package AST;

import java.util.ArrayList;
import java.util.List;

public abstract class ConnectorNode extends ASTNode {
    protected List<ASTNode> children;

    public ConnectorNode() {
        super();
        this.children = new ArrayList<>();
    }

    public ConnectorNode(String nodeName, int lineNumber, int columnNumber) {
        super(nodeName, lineNumber, columnNumber);
        this.children = new ArrayList<>();
    }

    public void addChild(ASTNode child) {
        if (child != null) {
            children.add(child);
        }
    }

    public void addChildren(List<ASTNode> nodes) {
        if (nodes != null) {
            children.addAll(nodes);
        }
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public void setChildren(List<ASTNode> children) {
        this.children = children;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, getNodeType() + (lineNumber > 0 ? " (Line: " + lineNumber + ")" : "")));
        String childPrefix = nextPrefix(prefix, isTail);
        for (int i = 0; i < children.size(); i++) {
            boolean last = i == children.size() - 1;
            sb.append(children.get(i).toTreeString(childPrefix, last));
        }
        return sb.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"columnNumber\": ").append(columnNumber).append(",\n");
        sb.append("  \"children\": [\n");
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                sb.append(children.get(i).toJson());
                if (i < children.size() - 1) sb.append(",");
                sb.append("\n");
            }
        }
        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }
}