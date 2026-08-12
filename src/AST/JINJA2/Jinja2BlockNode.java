package AST.JINJA2;

import AST.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class Jinja2BlockNode extends Jinja2Node {
    private String name;
    private List<Jinja2Node> body;

    public Jinja2BlockNode(String name, List<Jinja2Node> body) {
        super("Jinja2BlockNode", 0, 0);
        this.name = name;
        this.body = body != null ? body : new ArrayList<>();
    }

    public String getName() { return name; }
    public String getNameExpr() { return name; }
    public List<Jinja2Node> getBody() { return body; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "Jinja2BlockNode (Line: " + lineNumber
                + ", name='" + (name != null ? name : "") + "')"));
        String childPrefix = nextPrefix(prefix, isTail);
        for (int i = 0; i < body.size(); i++) {
            boolean last = i == body.size() - 1;
            sb.append(body.get(i).toTreeString(childPrefix, last));
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        return "{% block " + name + " %}";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"name\": ").append(jsonString(name)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}