package AST.JINJA2;

import AST.ASTNode;
import AST.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class Jinja2TemplateNode extends Jinja2Node {
    private String templateName;
    private List<Jinja2Node> children;

    public Jinja2TemplateNode(String name, String source) {
        super("Jinja2TemplateNode", 0, 0);
        this.templateName = name;
        this.source = source;
        this.children = new ArrayList<>();
    }

    private String source;

    public String getTemplateName() { return templateName; }
    public String getSource() { return source; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, getNodeType() + " (" + templateName + ") (Line: " + lineNumber + ")"));
        String childPrefix = nextPrefix(prefix, isTail);
        for (int i = 0; i < getChildren().size(); i++) {
            sb.append(getChildren().get(i).toTreeString(childPrefix, i == getChildren().size() - 1));
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        StringBuilder sb = new StringBuilder();
        for (Jinja2Node child : children) {
            sb.append(child.generateCode());
        }
        return sb.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"templateName\": ").append(jsonString(templateName)).append(",\n");
        sb.append("  \"children\": [\n");
        for (int i = 0; i < getChildren().size(); i++) {
            sb.append(getChildren().get(i).toJson());
            if (i < getChildren().size() - 1) sb.append(",");
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