package AST;

public abstract class DefinitionNode extends ASTNode {
    protected String name;
    protected String dataType;

    public DefinitionNode() {
        super();
        this.name = null;
        this.dataType = null;
    }

    public DefinitionNode(String nodeName, int lineNumber, int columnNumber) {
        super(nodeName, lineNumber, columnNumber);
        this.name = null;
        this.dataType = null;
    }

    public DefinitionNode(String nodeName, String name, int lineNumber, int columnNumber) {
        super(nodeName, lineNumber, columnNumber);
        this.name = name;
        this.dataType = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, getNodeType() + (name != null ? " (" + name + ")" : "") + " (Line: " + lineNumber + ")"));
        return sb.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"columnNumber\": ").append(columnNumber).append(",\n");
        sb.append("  \"name\": ").append(name != null ? jsonString(name) : "null").append(",\n");
        sb.append("  \"dataType\": ").append(dataType != null ? jsonString(dataType) : "null").append("\n");
        sb.append("}");
        return sb.toString();
    }
}