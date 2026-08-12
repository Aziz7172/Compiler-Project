package AST;

import java.util.List;

public class FunctionCallNode extends ASTNode {
    private String functionName;
    private ASTNode functionTarget;
    private List<ASTNode> arguments;

    public FunctionCallNode(String functionName, List<ASTNode> arguments) {
        this.functionName = functionName;
        this.functionTarget = null;
        this.arguments = arguments;
    }

    public FunctionCallNode(ASTNode functionTarget, List<ASTNode> arguments) {
        this.functionName = null;
        this.functionTarget = functionTarget;
        this.arguments = arguments;
    }

    public String getFunctionName() {
        if (functionTarget instanceof IdentifierNode) {
            return ((IdentifierNode) functionTarget).getName();
        }
        return functionName;
    }

    public ASTNode getFunctionTarget() {
        return functionTarget;
    }

    public List<ASTNode> getArguments() {
        return arguments;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder builder = new StringBuilder();
        String label = functionTarget != null ? "FunctionCallNode" : "FunctionCallNode(" + functionName + ")";
        builder.append(formatLine(prefix, isTail, label));

        String childPrefix = nextPrefix(prefix, isTail);
        if (functionTarget != null) {
            builder.append(formatLine(childPrefix, arguments != null && !arguments.isEmpty() ? false : true, "Target"));
            builder.append(functionTarget.toTreeString(nextPrefix(childPrefix, arguments != null && !arguments.isEmpty() ? false : true), true));
        }
        if (arguments != null && !arguments.isEmpty()) {
            builder.append(formatLine(childPrefix, true, "Arguments"));
            for (int index = 0; index < arguments.size(); index++) {
                builder.append(arguments.get(index).toTreeString(nextPrefix(childPrefix, true), index == arguments.size() - 1));
            }
        }
        return builder.toString();
    }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        if (functionTarget != null) {
            builder.append(functionTarget.generateCode());
        } else {
            builder.append(functionName);
        }
        builder.append("(");
        if (arguments != null) {
            for (int index = 0; index < arguments.size(); index++) {
                if (index > 0) builder.append(", ");
                builder.append(arguments.get(index).generateCode());
            }
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String toString() {
        String name = functionTarget != null ? functionTarget.toString() : functionName;
        return "FunctionCallNode(" + name + arguments + ")";
    }

    @Override
    public String toJson() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("  \"nodeName\": ").append(jsonString(nodeType)).append(",\n");
        builder.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        builder.append("  \"functionName\": ").append(jsonString(functionName)).append(",\n");
        builder.append("  \"functionTarget\": ").append(functionTarget != null ? functionTarget.toJson() : "null").append(",\n");
        builder.append("  \"arguments\": [\n");
        if (arguments != null) {
            for (int index = 0; index < arguments.size(); index++) {
                builder.append(arguments.get(index).toJson());
                if (index < arguments.size() - 1) builder.append(",");
                builder.append("\n");
            }
        }
        builder.append("  ]\n");
        builder.append("}");
        return builder.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}





