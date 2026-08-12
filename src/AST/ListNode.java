package AST;

import java.util.ArrayList;
import java.util.List;

public class ListNode extends ASTNode {
    public List<ASTNode> elements = new ArrayList<>();

    public void addElement(ASTNode element) {
        this.elements.add(element);
    }

    public List<ASTNode> getElements() {
        return elements;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "ListNode"));
        for (int i = 0; i < elements.size(); i++)
            sb.append(elements.get(i).toTreeString(nextPrefix(prefix, isTail), i == elements.size() - 1));
        return sb.toString();
    }


    @Override
    public String generateCode() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i).generateCode());
            if (i < elements.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ListNode{" +
                "elements=" + elements.toString() +
                '}';
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeName\": ").append(jsonString(nodeType)).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"elements\": [\n");
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i).toJson());
            if (i < elements.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
