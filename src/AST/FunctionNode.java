package AST;

import java.util.ArrayList;

import java.util.List;

public class FunctionNode extends DefinitionNode {
    private List<ParameterNode> parameters;
    private List<ASTNode> body;
    private List<DecoratorNode> decorators;

    public FunctionNode(String name, List<ParameterNode> parameters, List<ASTNode> body) {
        super("FunctionDefNode", name, 0, 0);
        this.parameters = parameters;
        this.body = body;
        this.decorators = new ArrayList<>();
    }

    public String getName() { return name; }
    public List<ParameterNode> getParameters() { return parameters; }
    public List<ASTNode> getBody() { return body; }
    public List<DecoratorNode> getDecorators() { return decorators; }

    public void setDecorators(List<DecoratorNode> decorators) {
        this.decorators = decorators;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, getNodeType() + " (" + name + ") (Line: " + lineNumber + ")"));
        String newPrefix = nextPrefix(prefix, isTail);
        if (parameters != null && !parameters.isEmpty()) {
            sb.append(formatLine(newPrefix, false, "Parameters"));
            for (int i = 0; i < parameters.size(); i++)
                sb.append(parameters.get(i).toTreeString(nextPrefix(newPrefix, false), i == parameters.size() - 1));
        }
        if (body != null && !body.isEmpty()) {
            sb.append(formatLine(newPrefix, true, "Body"));
            for (int i = 0; i < body.size(); i++)
                sb.append(body.get(i).toTreeString(nextPrefix(newPrefix, true), i == body.size() - 1));
        }
        if (decorators != null && !decorators.isEmpty()) {
            sb.append(formatLine(newPrefix, true, "Decorators"));
            for (int i = 0; i < decorators.size(); i++)
                sb.append(decorators.get(i).toTreeString(nextPrefix(newPrefix, true), i == decorators.size() - 1));
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        StringBuilder sb = new StringBuilder();
        if (decorators != null) {
            for (DecoratorNode dec : decorators) {
                sb.append(dec.generateCode()).append("\n");
            }
        }
        sb.append("def ").append(name).append("(");
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(parameters.get(i).generateCode());
            }
        }
        sb.append("):\n");
        if (body != null) {
            for (ASTNode node : body) {
                String code = node.generateCode();
                if (code != null && !code.isEmpty()) {
                    for (String line : code.split("\n")) {
                        sb.append("    ").append(line).append("\n");
                    }
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"name\": ").append(jsonString(name)).append(",\n");
        sb.append("  \"dataType\": ").append(dataType != null ? jsonString(dataType) : "null").append(",\n");
        sb.append("  \"parameters\": [\n");
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                sb.append(parameters.get(i).toJson());
                if (i < parameters.size() - 1) sb.append(",");
                sb.append("\n");
            }
        }
        sb.append("  ],\n");
        sb.append("  \"body\": [\n");
        if (body != null) {
            for (int i = 0; i < body.size(); i++) {
                sb.append(body.get(i).toJson());
                if (i < body.size() - 1) sb.append(",");
                sb.append("\n");
            }
        }
        sb.append("  ],\n");
        sb.append("  \"decorators\": [\n");
        if (decorators != null) {
            for (int i = 0; i < decorators.size(); i++) {
                sb.append(decorators.get(i).toJson());
                if (i < decorators.size() - 1) sb.append(",");
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