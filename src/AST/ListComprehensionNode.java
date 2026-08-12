package AST;

public class ListComprehensionNode extends ASTNode {
    private ASTNode elementExpression;
    private ASTNode variable;
    private ASTNode iterable;
    private ASTNode condition;

    public ListComprehensionNode(ASTNode elementExpression, ASTNode variable, ASTNode iterable, ASTNode condition) {
        this.elementExpression = elementExpression;
        this.variable = variable;
        this.iterable = iterable;
        this.condition = condition;
    }

    public ASTNode getElementExpression() {
        return elementExpression;
    }

    public ASTNode getVariable() {
        return variable;
    }

    public ASTNode getIterable() {
        return iterable;
    }

    public ASTNode getCondition() {
        return condition;
    }

    @Override
    public String generateCode() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(elementExpression.generateCode());
        sb.append(" for ");
        sb.append(variable.generateCode());
        sb.append(" in ");
        sb.append(iterable.generateCode());
        if (condition != null) {
            sb.append(" if ");
            sb.append(condition.generateCode());
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix)
                .append(isTail ? "└── " : "├── ")
                .append("ListComprehensionNode(for " + variable + " in ...)\n");

        String childPrefix = prefix + (isTail ? "    " : "│   ");
        sb.append(elementExpression.toTreeString(childPrefix, false));
        sb.append(iterable.toTreeString(childPrefix, condition == null));
        if (condition != null)
            sb.append(condition.toTreeString(childPrefix, true));
        return sb.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeName\": ").append(jsonString(nodeType)).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"elementExpression\": ").append(elementExpression != null ? elementExpression.toJson() : "null").append(",\n");
        sb.append("  \"variable\": ").append(variable != null ? variable.toJson() : "null").append(",\n");
        sb.append("  \"iterable\": ").append(iterable != null ? iterable.toJson() : "null").append(",\n");
        sb.append("  \"condition\": ").append(condition != null ? condition.toJson() : "null").append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
