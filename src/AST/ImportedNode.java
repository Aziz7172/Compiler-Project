package AST;

public class ImportedNode extends DefinitionNode {
    private ASTNode alias;

    public ImportedNode(ASTNode name, ASTNode alias) {
        super("ImportDefNode", name != null ? name.toString() : null, 0, 0);
        this.alias = alias;
    }

    public ASTNode getAlias() { return alias; }

    @Override
    public String generateCode() {
        return "import " + (name != null ? name : "") + (alias != null ? " as " + alias.generateCode() : "");
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"name\": ").append(name != null ? jsonString(name.toString()) : "null").append(",\n");
        sb.append("  \"alias\": ").append(alias != null ? alias.toJson() : "null").append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}