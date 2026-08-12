package AST;

public class AttributeAccessNode extends ASTNode {
    public ASTNode object;
    public ASTNode attribute;

    public AttributeAccessNode(ASTNode object, ASTNode attribute) {
        this.object = object;
        this.attribute = attribute;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "AttributeAccessNode"));

        if (object != null) {
            sb.append(object.toTreeString(nextPrefix(prefix, isTail), false));
        }

        if (attribute != null) {
            sb.append(attribute.toTreeString(nextPrefix(prefix, true), true));
        }

        return sb.toString();
    }

    @Override
    public String generateCode() {
        return object.generateCode() + "." + attribute.generateCode();
    }

    @Override
    public String toString() {
        return object.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeName\": ").append(jsonString(nodeType)).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"object\": ").append(object != null ? object.toJson() : "null").append(",\n");
        sb.append("  \"attribute\": ").append(attribute != null ? attribute.toJson() : "null").append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}