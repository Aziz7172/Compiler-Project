package AST.JINJA2;

import AST.ASTNode;
import AST.ASTVisitor;

public class JinjaExpressionNode extends Jinja2Node {
    private ASTNode innerExpression;

    public JinjaExpressionNode(ASTNode innerExpression) {
        super("JinjaExpressionNode", 0, 0);
        this.innerExpression = innerExpression;
    }

    public ASTNode getInnerExpression() { return innerExpression; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "JinjaExpressionNode (Line: " + lineNumber + ")"));
        if (innerExpression != null) {
            sb.append(innerExpression.toTreeString(nextPrefix(prefix, isTail), true));
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        return innerExpression != null ? innerExpression.generateCode() : "";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"innerExpression\": ").append(innerExpression != null ? innerExpression.toJson() : "null").append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}