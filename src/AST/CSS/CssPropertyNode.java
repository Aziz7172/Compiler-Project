package AST.CSS;

import AST.ASTNode;
import AST.ASTVisitor;

public class CssPropertyNode extends ASTNode {
    private final ASTNode key;
    private final ASTNode value;

    public CssPropertyNode(ASTNode key, ASTNode value) {
        this.key = key;
        this.value = value;
    }

    public ASTNode getKey() {
        return key;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        if (key != null) {
            builder.append(key.generateCode()).append(": ");
        }
        if (value != null) {
            builder.append(value.generateCode());
        }
        builder.append(";");
        return builder.toString();
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append(isTail ? "└── " : "├── ").append("Property: ");

        builder.append(key != null ? key.toString().trim() : "null");
        builder.append(": ");
        builder.append(value != null ? value.toString().trim() : "null");
        builder.append("\n");
        return builder.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
    }
}
