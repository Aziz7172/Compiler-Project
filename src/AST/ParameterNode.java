package AST;

public class ParameterNode extends DefinitionNode {
    private ASTNode defaultValue;

    public ParameterNode(String name, ASTNode defaultValue) {
        super("ParameterNode", name, 0, 0);
        this.defaultValue = defaultValue;
    }

    public ASTNode getDefaultValue() { return defaultValue; }
    public void setDefaultValue(ASTNode defaultValue) { this.defaultValue = defaultValue; }

    @Override
    public String generateCode() {
        if (defaultValue != null) {
            return name + "=" + defaultValue.generateCode();
        }
        return name;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}