package AST;

import java.util.List;

public class ImportedListNode extends DefinitionNode {
    private List<ASTNode> imports;

    public ImportedListNode(List<ASTNode> imports) {
        super("ImportListDefNode", null, 0, 0);
        this.imports = imports;
    }

    public List<ASTNode> getImports() { return imports; }

    @Override
    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        if (imports != null) {
            for (int i = 0; i < imports.size(); i++) {
                builder.append(imports.get(i).generateCode());
                if (i < imports.size() - 1) builder.append(", ");
            }
        }
        return builder.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"imports\": [\n");
        if (imports != null) {
            for (int i = 0; i < imports.size(); i++) {
                sb.append(imports.get(i).toJson());
                if (i < imports.size() - 1) sb.append(",");
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