package AST.JINJA2;

import AST.ASTNode;
import AST.ASTVisitor;

public class ExtendNode extends ASTNode {
    private ASTNode template;

    public ExtendNode(ASTNode template) {
        this.template = template;
    }

    public ASTNode getTemplate() {
        return template;
    }

    @Override
    public String generateCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("{% extends ");
        if (template != null) {
            sb.append(template.generateCode());
        }
        sb.append(" %}");
        return sb.toString();
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "JinjaExtendsNode"));
        sb.append(template.toTreeString(nextPrefix(prefix, true), true));
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
    }
}
