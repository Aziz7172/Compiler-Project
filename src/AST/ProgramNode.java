package AST;

import java.util.*;

public class ProgramNode extends ConnectorNode {
    public List<ASTNode> statements;

    public ProgramNode() {
        super("ProgramNode", 0, 0);
        this.statements = new ArrayList<>();
    }

    public ProgramNode(int lineNumber, int columnNumber) {
        super("ProgramNode", lineNumber, columnNumber);
        this.statements = new ArrayList<>();
    }

    public void addStatement(ASTNode node) {
        statements.add(node);
    }

    public List<ASTNode> getStatements() {
        return statements;
    }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder builder = new StringBuilder();
        builder.append(formatLine(prefix, isTail, getNodeType() + (lineNumber > 0 ? " (Line: " + lineNumber + ")" : "")));
        String childPrefix = nextPrefix(prefix, isTail);
        for (int index = 0; index < statements.size(); index++) {
            builder.append(statements.get(index).toTreeString(childPrefix, index == statements.size() - 1));
        }
        return builder.toString();
    }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        for (ASTNode node : statements) {
            builder.append(node.generateCode()).append("\n");
        }
        return builder.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}