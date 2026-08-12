package AST.JINJA2;

import AST.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class Jinja2ForNode extends Jinja2Node {
    private List<String> loopVariables;
    private String iterable;
    private List<Jinja2Node> body;

    public Jinja2ForNode(List<String> loopVariables, String iterable, List<Jinja2Node> body) {
        super("Jinja2ForNode", 0, 0);
        this.loopVariables = loopVariables != null ? loopVariables : new ArrayList<>();
        this.iterable = iterable;
        this.body = body != null ? body : new ArrayList<>();
    }

    public List<String> getLoopVariables() { return loopVariables; }
    public List<String> getTarget() { return loopVariables; }
    public String getIterable() { return iterable; }
    public List<Jinja2Node> getBody() { return body; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "Jinja2ForNode (Line: " + lineNumber
                + ", target='" + String.join(" ", loopVariables) + "'"
                + ", iterable='" + (iterable != null ? iterable : "") + "')"));
        String childPrefix = nextPrefix(prefix, isTail);
        for (int i = 0; i < body.size(); i++) {
            boolean last = i == body.size() - 1;
            sb.append(body.get(i).toTreeString(childPrefix, last));
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        return "{% for " + String.join(" ", loopVariables) + " in " + iterable + " %}";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"variables\": ").append(jsonString(String.join(" ", loopVariables))).append(",\n");
        sb.append("  \"iterable\": ").append(jsonString(iterable)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}