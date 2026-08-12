package AST.JINJA2;

import AST.ASTVisitor;

public class Jinja2IncludeNode extends Jinja2Node {
    private String template;
    private boolean ignoreMissing;
    private boolean withContext;

    public Jinja2IncludeNode(String template, boolean ignoreMissing, boolean withContext) {
        super("Jinja2IncludeNode", 0, 0);
        this.template = template;
        this.ignoreMissing = ignoreMissing;
        this.withContext = withContext;
    }

    public String getTemplate() { return template; }
    public boolean isIgnoreMissing() { return ignoreMissing; }
    public boolean isWithContext() { return withContext; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        return formatLine(prefix, isTail, "Jinja2IncludeNode (Line: " + lineNumber
                + ", template='" + (template != null ? template : "") + "')");
    }

    @Override
    public String generateCode() {
        return "{% include " + template + " %}";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"template\": ").append(jsonString(template)).append(",\n");
        sb.append("  \"ignoreMissing\": ").append(ignoreMissing).append(",\n");
        sb.append("  \"withContext\": ").append(withContext).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}