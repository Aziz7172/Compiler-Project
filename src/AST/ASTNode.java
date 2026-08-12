package AST;

import java.util.*;

public abstract class ASTNode {
    protected int lineNumber;
    protected int columnNumber;
    protected String nodeType;
    protected List<ASTNode> children;

    public ASTNode() {
        this("ASTNode", 0, 0);
    }

    public ASTNode(String nodeType, int lineNumber) {
        this(nodeType, lineNumber, 0);
    }

    public ASTNode(String nodeType, int lineNumber, int columnNumber) {
        this.nodeType = nodeType;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.children = new ArrayList<>();
    }

    public int getLineNumber() { return lineNumber; }
    public int getColumnNumber() { return columnNumber; }
    public String getNodeType() { return nodeType; }
    public void setNodeType(String nodeType) { this.nodeType = nodeType; }
    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }
    public void setColumnNumber(int columnNumber) { this.columnNumber = columnNumber; }
    public List<ASTNode> getChildren() { return children; }
    public void addChild(ASTNode child) { if (child != null) children.add(child); }

    public void setChildren(List<ASTNode> children) {
        this.children = children != null ? children : new ArrayList<>();
    }

    public void printTree(int indentLevel) {
        printTree(indentLevel, new HashSet<>());
    }

    public void printTree(int indentLevel, Set<ASTNode> visited) {
        System.out.println(this.toTreeString());
    }

    public String toTreeString() {
        return toTreeString("", true);
    }

    public abstract String toTreeString(String prefix, boolean isTail);

    public String generateCode() {
        return "";
    }

    public abstract void accept(ASTVisitor visitor);

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        toJson(sb, new HashSet<>(), 0);
        return sb.toString();
    }

    protected void toJson(StringBuilder sb, Set<ASTNode> visited, int depth) {
        if (visited.contains(this)) {
            sb.append("{\"nodeType\": \"").append(getNodeType()).append("\", \"ref\": true}");
            return;
        }
        if (depth > 50) {
            sb.append("{\"nodeType\": \"").append(getNodeType()).append("\", \"maxDepth\": true}");
            return;
        }
        visited.add(this);
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"columnNumber\": ").append(columnNumber).append("\n");
        sb.append("}");
    }

    protected String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    protected String jsonString(String val) {
        if (val == null) return "null";
        return "\"" + escapeJson(val) + "\"";
    }

    protected String formatLine(String prefix, boolean isTail, String text) {
        return prefix + (isTail ? "└── " : "├── ") + text;
    }

    protected static String truncate(String s, int max) {
        if (s == null) return "";
        String clean = s.replace("\\", "\\\\")
                .replace("\r\n", "\\n")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
        if (clean.length() <= max) return clean;
        return clean.substring(0, max) + "...";
    }

    protected String nextPrefix(String prefix, boolean isTail) {
        return prefix + (isTail ? "    " : "│   ");
    }
}