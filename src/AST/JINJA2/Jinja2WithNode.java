package AST.JINJA2;

import AST.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class Jinja2WithNode extends Jinja2Node {
    private String assignment;
    private List<Jinja2Node> body;

    public Jinja2WithNode(String assignment, List<Jinja2Node> body) {
        super("Jinja2WithNode", 0, 0);
        this.assignment = assignment;
        this.body = body != null ? body : new ArrayList<>();
    }

    public String getAssignment() { return assignment; }
    public String getAssignmentExpr() { return assignment; }
    public List<Jinja2Node> getBody() { return body; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "Jinja2WithNode (Line: " + lineNumber
                + ", assignment='" + (assignment != null ? assignment : "") + "')"));
        String childPrefix = nextPrefix(prefix, isTail);
        for (int i = 0; i < body.size(); i++) {
            boolean last = i == body.size() - 1;
            sb.append(body.get(i).toTreeString(childPrefix, last));
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        return "{% with " + assignment + " %}";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"assignment\": ").append(jsonString(assignment)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}