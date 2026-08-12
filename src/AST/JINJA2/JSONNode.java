package AST.JINJA2;

import AST.ASTNode;
import AST.ASTVisitor;

import java.util.Map;

public class JSONNode extends Jinja2Node {
    private Map<String, ASTNode> data;

    public JSONNode(Map<String, ASTNode> data) {
        super("JSONNode", 0, 0);
        this.data = data;
    }

    public Map<String, ASTNode> getData() { return data; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        return formatLine(prefix, isTail, "JSONNode (Line: " + lineNumber + ")");
    }

    @Override
    public String generateCode() {
        return data != null ? data.toString() : "{}";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"data\": {}\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}