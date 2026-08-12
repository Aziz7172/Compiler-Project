package AST;

public class IndexAccessNode extends ASTNode {
    public ASTNode object;
    public ASTNode index;

    public IndexAccessNode(ASTNode object, ASTNode index) {
        this.object = object;
        this.index = index;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "IndexAccessNode"));
        sb.append(object.toTreeString(nextPrefix(prefix, false), false));
        sb.append(index.toTreeString(nextPrefix(prefix, true), true));
        return sb.toString();
    }


    @Override
    public String generateCode() {
        return object.generateCode() + "[" + index.generateCode() + "]";
    }

    @Override
    public String toString() {
        return "IndexAccessNode{" +
                "object=" + object +
                ", index=" + index +
                '}';
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeName\": ").append(jsonString(nodeType)).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"object\": ").append(object != null ? object.toJson() : "null").append(",\n");
        sb.append("  \"index\": ").append(index != null ? index.toJson() : "null").append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
