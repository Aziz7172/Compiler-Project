package AST.CSS;

import AST.ASTNode;
import AST.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class CssNode extends ASTNode {
    private ASTNode selector;
    private List<CssPropertyNode> properties = new ArrayList<>();

    public CssNode(ASTNode selector) {
        this.selector = selector;
    }

    public void setSelector(ASTNode selector) {
        this.selector = selector;
    }

    public void addProperty(CssPropertyNode property) {
        this.properties.add(property);
    }

    public void setProperties(List<CssPropertyNode> properties) {
        this.properties = properties;
    }

    public List<CssPropertyNode> getProperties() {
        return properties;
    }

    public ASTNode getSelector() {
        return selector;
    }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        if (selector != null) {
            builder.append(selector.generateCode()).append(" {\n");
        }
        for (CssPropertyNode property : properties) {
            builder.append("  ").append(property.generateCode()).append("\n");
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append(isTail ? "└── " : "├── ").append("CssNode\n");

        String childPrefix = prefix + (isTail ? "    " : "│   ");
        if (selector != null) {
            builder.append(childPrefix)
                    .append("├── Selector: ")
                    .append(selector.getClass().getSimpleName())
                    .append("(")
                    .append(selector.toString().trim())
                    .append(")\n");
        }

        for (int index = 0; index < properties.size(); index++) {
            CssPropertyNode property = properties.get(index);
            builder.append(property.toTreeString(childPrefix, index == properties.size() - 1));
        }

        return builder.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
    }
}
