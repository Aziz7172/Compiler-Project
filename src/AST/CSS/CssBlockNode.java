package AST.CSS;

import AST.ASTNode;
import AST.ASTVisitor;

import java.util.List;

public class CssBlockNode extends ASTNode {
    private final List<CssNode> elements;

    public CssBlockNode(List<CssNode> elements) {
        this.elements = elements;
    }

    public List<CssNode> getElements() {
        return elements;
    }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("<style>\n");
        if (elements != null) {
            for (CssNode node : elements) {
                builder.append(node.generateCode()).append("\n");
            }
        }
        builder.append("</style>");
        return builder.toString();
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append(isTail ? "└── " : "├── ").append("CssBlockNode\n");

        String childPrefix = prefix + (isTail ? "    " : "│   ");
        for (int index = 0; index < elements.size(); index++) {
            CssNode node = elements.get(index);
            builder.append(node.toTreeString(childPrefix, index == elements.size() - 1));
        }

        return builder.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
    }
}
