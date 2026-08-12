package AST;

import java.util.ArrayList;
import java.util.List;

public class IfNode extends ConnectorNode {
    private ASTNode condition;
    private List<ASTNode> body;
    private List<ASTNode> elifClauses;
    private List<ASTNode> elseBody;

    public IfNode(ASTNode condition) {
        super();
        this.condition = condition;
        this.body = new ArrayList<>();
        this.elifClauses = new ArrayList<>();
        this.elseBody = new ArrayList<>();
    }

    public ASTNode getCondition() { return condition; }
    public void setCondition(ASTNode condition) { this.condition = condition; }
    public List<ASTNode> getBody() { return body; }
    public void setBody(List<ASTNode> body) { this.body = body != null ? body : new ArrayList<>(); }
    public List<ASTNode> getElifClauses() { return elifClauses; }
    public void addElif(ASTNode elifNode) { elifClauses.add(elifNode); }
    public List<ASTNode> getElseBody() { return elseBody; }
    public void setElse(ElseNode elseNode) {
        this.elseBody = elseNode != null ? elseNode.getBody() : new ArrayList<>();
    }

    private void collectAllChildren() {
        List<ASTNode> all = new ArrayList<>();
        if (condition != null) all.add(condition);
        all.addAll(elifClauses);
        all.addAll(elseBody);
        setChildren(all);
    }

    @Override
    public void addChild(ASTNode child) {
        super.addChild(child);
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, "IfNode (Line: " + lineNumber + ")"));
        String childPrefix = nextPrefix(prefix, isTail);
        if (condition != null) {
            sb.append(formatLine(childPrefix, false, "Condition"));
            sb.append(condition.toTreeString(nextPrefix(childPrefix, false), true));
        }
        if (!elifClauses.isEmpty()) {
            sb.append(formatLine(childPrefix, false, "Elif Clauses"));
            for (int i = 0; i < elifClauses.size(); i++) {
                sb.append(elifClauses.get(i).toTreeString(nextPrefix(childPrefix, false), i == elifClauses.size() - 1));
            }
        }
        if (!elseBody.isEmpty()) {
            sb.append(formatLine(childPrefix, true, "Else Body"));
            for (int i = 0; i < elseBody.size(); i++) {
                sb.append(elseBody.get(i).toTreeString(nextPrefix(childPrefix, true), i == elseBody.size() - 1));
            }
        }
        return sb.toString();
    }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("if ").append(condition != null ? condition.generateCode() : "").append(":\n");
        for (ASTNode node : children) {
            String code = node.generateCode();
            if (code != null && !code.isEmpty()) {
                for (String line : code.split("\n")) {
                    builder.append("    ").append(line).append("\n");
                }
            }
        }
        return builder.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}