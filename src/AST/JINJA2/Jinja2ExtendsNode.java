package AST.JINJA2;

import AST.ASTVisitor;

public class Jinja2ExtendsNode extends Jinja2Node {
    private String template;

    public Jinja2ExtendsNode(String template) {
        super("Jinja2ExtendsNode", 0, 0);
        this.template = template;
    }

    public String getTemplate() { return template; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        return formatLine(prefix, isTail, "Jinja2ExtendsNode (Line: " + lineNumber
                + ", template='" + (template != null ? template : "") + "')");
    }

    @Override
    public String generateCode() {
        return "{% extends " + template + " %}";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"template\": ").append(jsonString(template)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}