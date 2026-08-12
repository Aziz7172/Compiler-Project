package AST.JINJA2;

import AST.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class Jinja2IfNode extends Jinja2Node {
    private String condition;
    private List<Jinja2Node> body;
    private List<Jinja2ElifClause> elifClauses;
    private List<Jinja2Node> elseBody;

    public Jinja2IfNode(String condition, List<Jinja2Node> body, List<Jinja2ElifClause> elifClauses, List<Jinja2Node> elseBody) {
        super("Jinja2IfNode", 0, 0);
        this.condition = condition;
        this.body = body != null ? body : new ArrayList<>();
        this.elifClauses = elifClauses != null ? elifClauses : new ArrayList<>();
        this.elseBody = elseBody != null ? elseBody : new ArrayList<>();
    }

    public static class Jinja2ElifClause {
        private String condition;
        private List<Jinja2Node> body;

        public Jinja2ElifClause(String condition, List<Jinja2Node> body) {
            this.condition = condition;
            this.body = body != null ? body : new ArrayList<>();
        }

        public String getCondition() { return condition; }
        public List<Jinja2Node> getBody() { return body; }
    }

    public String getCondition() { return condition; }
    public List<Jinja2Node> getBody() { return body; }
    public List<Jinja2Node> getThenBranch() { return body; }
    public List<Jinja2ElifClause> getElifClauses() { return elifClauses; }
    public List<Jinja2Node> getElseBody() { return elseBody; }
    public List<Jinja2Node> getElseBranch() { return elseBody; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "Jinja2IfNode (Line: " + lineNumber
                + ", condition='" + (condition != null ? condition : "") + "')"));
        String childPrefix = nextPrefix(prefix, isTail);
        for (int i = 0; i < body.size(); i++) {
            boolean isLast = i == body.size() - 1;
            sb.append(body.get(i).toTreeString(childPrefix, isLast && elifClauses.isEmpty() && elseBody.isEmpty()));
        }
        for (int e = 0; e < elifClauses.size(); e++) {
            Jinja2ElifClause clause = elifClauses.get(e);
            boolean isLastElif = e == elifClauses.size() - 1 && elseBody.isEmpty();
            sb.append(formatLine(childPrefix, isLastElif, "Jinja2ElifClause (condition='" + clause.getCondition() + "')"));
            String elifPrefix = nextPrefix(childPrefix, isLastElif);
            for (int i = 0; i < clause.getBody().size(); i++) {
                boolean last = i == clause.getBody().size() - 1;
                sb.append(clause.getBody().get(i).toTreeString(elifPrefix, last));
            }
        }
        if (!elseBody.isEmpty()) {
            sb.append(formatLine(childPrefix, true, "Jinja2ElseClause"));
            String elsePrefix = nextPrefix(childPrefix, true);
            for (int i = 0; i < elseBody.size(); i++) {
                boolean last = i == elseBody.size() - 1;
                sb.append(elseBody.get(i).toTreeString(elsePrefix, last));
            }
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        return "{% if " + condition + " %}";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"condition\": ").append(jsonString(condition)).append(",\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}