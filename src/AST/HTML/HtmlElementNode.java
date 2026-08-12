package AST.HTML;

import AST.ASTNode;
import AST.ConnectorNode;
import AST.ASTVisitor;
import AST.HTML.Behaviors.FormBehavior;
import AST.HTML.Behaviors.TagBehavior;
import AST.IdentifierNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlElementNode extends ConnectorNode {
    private static final Map<String, TagBehavior> behaviorTable = new HashMap<>();

    private IdentifierNode tagName;
    private Map<String, String> attributes;

    static {
        behaviorTable.put("form", new FormBehavior());
    }

    public HtmlElementNode(IdentifierNode tagName) {
        super();
        this.tagName = tagName;
        this.attributes = new HashMap<>();
    }

    public String getTagName() {
        return tagName != null ? tagName.generateCode() : "div";
    }

    public void setTagName(IdentifierNode tagName) {
        this.tagName = tagName;
    }

    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }
    public IdentifierNode getTagNameNode() { return tagName; }
    public void setTagNameNode(IdentifierNode tagName) { this.tagName = tagName; }
    public static Map<String, TagBehavior> getBehaviorTable() { return behaviorTable; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder builder = new StringBuilder();
        builder.append(formatLine(prefix, isTail, getNodeType() + " (" + getTagName() + ") (Line: " + lineNumber + ")"));
        String childPrefix = nextPrefix(prefix, isTail);
        int index = 0;
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            boolean last = index == attributes.size() - 1 && children.isEmpty();
            builder.append(formatLine(childPrefix, last, "Attribute(" + entry.getKey() + ")=" + entry.getValue()));
            index++;
        }
        for (int i = 0; i < children.size(); i++) {
            builder.append(children.get(i).toTreeString(childPrefix, i == children.size() - 1));
        }
        return builder.toString();
    }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        String tag = tagName != null ? tagName.generateCode() : "div";
        builder.append("<").append(tag);
        for (Map.Entry<String, String> attribute : attributes.entrySet()) {
            builder.append(" ").append(attribute.getKey()).append("=\"").append(attribute.getValue()).append("\"");
        }
        builder.append(">");
        for (ASTNode child : children) {
            builder.append(child.generateCode());
        }
        builder.append("</").append(tag).append(">");
        return builder.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"tagName\": ").append(jsonString(getTagName())).append(",\n");
        sb.append("  \"attributes\": {\n");
        int attrIdx = 0;
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            sb.append("    ").append(jsonString(entry.getKey())).append(": ").append(jsonString(entry.getValue()));
            if (attrIdx < attributes.size() - 1) sb.append(",");
            sb.append("\n");
            attrIdx++;
        }
        sb.append("  },\n");
        sb.append("  \"children\": [\n");
        for (int i = 0; i < children.size(); i++) {
            sb.append(children.get(i).toJson());
            if (i < children.size() - 1) sb.append(",");
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