package AST.JINJA2;

import AST.ASTNode;
import AST.ASTVisitor;

public class IncludeNode extends ASTNode {
    private ASTNode template;
    private boolean ignoreMissing;
    private boolean withContext;

    public IncludeNode(ASTNode template, boolean ignoreMissing, boolean withContext) {
        this.template = template;
        this.ignoreMissing = ignoreMissing;
        this.withContext = withContext;
    }

    public ASTNode getTemplate() { return template; }
    public boolean isIgnoreMissing() { return ignoreMissing; }
    public boolean isWithContext() { return withContext; }

    @Override
    public String generateCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("{% include ");
        if (template != null) {
            sb.append(template.generateCode());
        }
        if (ignoreMissing) {
            sb.append(" ignore missing");
        }
        if (!withContext) {
            sb.append(" without context");
        }
        sb.append(" %}");
        return sb.toString();
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "IncludeNode"));
        sb.append(formatLine(nextPrefix(prefix, isTail), false,
                "ignoreMissing=" + ignoreMissing + ", withContext=" + withContext));
        sb.append(template.toTreeString(nextPrefix(prefix, true), true));
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
    }
}
