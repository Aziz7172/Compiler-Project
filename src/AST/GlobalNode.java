package AST;

import java.util.List;

public class GlobalNode extends ASTNode {
    private List<IdentifierNode> variables;

    public GlobalNode(List<IdentifierNode> variables) {
        this.variables = variables;
    }

    public List<IdentifierNode> getVariables() {
        return variables;
    }

    @Override
    public String generateCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("global ");
        for (int i = 0; i < variables.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(variables.get(i).generateCode());
        }
        return sb.toString();
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix)
                .append(isTail ? "└── " : "├── ")
                .append("GlobalNode\n");

        String childPrefix = prefix + (isTail ? "    " : "│   ");
        for (int i = 0; i < variables.size(); i++) {
            sb.append(childPrefix)
                    .append(i == variables.size() - 1 ? "└── " : "├── ")
                    .append(variables.get(i))
                    .append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeName\": ").append(jsonString(nodeType)).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"variables\": [\n");
        if (variables != null) {
            for (int i = 0; i < variables.size(); i++) {
                sb.append(variables.get(i).toJson());
                if (i < variables.size() - 1) sb.append(",");
                sb.append("\n");
            }
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
