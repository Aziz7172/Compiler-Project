package AST;

import java.util.List;

public class DecoratorNode extends ASTNode {
    private ASTNode name;
    private ASTNode target;

    public DecoratorNode(ASTNode name, ASTNode target) {
        this.name = name;
        this.target = target;
    }

    public void setTarget(ASTNode target) {
        this.target = target;
    }

    @Override
    public String generateCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        if (name != null) {
            sb.append(name.generateCode());
        }
        return sb.toString();
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "DecoratorNode"));

        if (name != null) {
            sb.append(name.toTreeString(nextPrefix(prefix, true), false));
        }
        if (target != null) {
            String targetInfo = target.getNodeType();
            if (target instanceof FunctionNode) {
                targetInfo += " (" + ((FunctionNode) target).getName() + ")";
            }
            sb.append(formatLine(nextPrefix(prefix, true), true, "Target: " + targetInfo));
        }

        return sb.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeName\": ").append(jsonString(nodeType)).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"name\": ").append(name != null ? name.toJson() : "null").append(",\n");
        sb.append("  \"target\": ").append(target != null ? target.toJson() : "null").append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
