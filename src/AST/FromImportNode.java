package AST;

import java.util.List;

public class FromImportNode extends DefinitionNode {
    private ASTNode module;
    private ASTNode importedList;

    public FromImportNode(ASTNode module, ASTNode importedList) {
        super("FromImportDefNode", module != null ? module.toString() : null, 0, 0);
        this.module = module;
        this.importedList = importedList;
    }

    public ASTNode getModule() { return module; }
    public ASTNode getImportedList() { return importedList; }

    @Override
    public String generateCode() {
        return "from " + (module != null ? module.generateCode() : "") + " import " + (importedList != null ? importedList.generateCode() : "");
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nodeType\": ").append(jsonString(getNodeType())).append(",\n");
        sb.append("  \"lineNumber\": ").append(lineNumber).append(",\n");
        sb.append("  \"name\": ").append(jsonString(name)).append(",\n");
        sb.append("  \"module\": ").append(module != null ? module.toJson() : "null").append(",\n");
        sb.append("  \"importedList\": ").append(importedList != null ? importedList.toJson() : "null").append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}