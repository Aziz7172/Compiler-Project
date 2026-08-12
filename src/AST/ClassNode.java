package AST;

import java.util.ArrayList;
import java.util.List;

public class ClassNode extends DefinitionNode {
    private String baseClass;
    private List<AssignmentNode> variables;
    private List<FunctionNode> methods;
    private List<ClassNode> nestedClasses;
    private List<DecoratorNode> decorators;

    public ClassNode(String name, String baseClass) {
        super("ClassDefNode", name, 0, 0);
        this.baseClass = baseClass;
        this.variables = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.nestedClasses = new ArrayList<>();
        this.decorators = new ArrayList<>();
    }

    public void addVariable(AssignmentNode var) { variables.add(var); }
    public void addMethod(FunctionNode fn) { methods.add(fn); }
    public void addNestedClass(ClassNode cls) { nestedClasses.add(cls); }
    public void setDecorators(List<DecoratorNode> decorators) { this.decorators = decorators; }
    public String getBaseClass() { return baseClass; }
    public List<AssignmentNode> getVariables() { return variables; }
    public List<FunctionNode> getMethods() { return methods; }
    public List<ClassNode> getNestedClasses() { return nestedClasses; }
    public List<DecoratorNode> getDecorators() { return decorators; }

    @Override
    public String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine(prefix, isTail, getNodeType() + " (" + name + (baseClass != null ? " : " + baseClass : "") + ") (Line: " + lineNumber + ")"));
        String newPrefix = nextPrefix(prefix, isTail);
        if (!variables.isEmpty()) {
            sb.append(formatLine(newPrefix, false, "Variables"));
            for (int i = 0; i < variables.size(); i++)
                sb.append(variables.get(i).toTreeString(nextPrefix(newPrefix, false), i == variables.size() - 1));
        }
        if (!methods.isEmpty()) {
            sb.append(formatLine(newPrefix, false, "Methods"));
            for (int i = 0; i < methods.size(); i++)
                sb.append(methods.get(i).toTreeString(nextPrefix(newPrefix, false), i == methods.size() - 1));
        }
        if (!nestedClasses.isEmpty()) {
            sb.append(formatLine(newPrefix, false, "NestedClasses"));
            for (int i = 0; i < nestedClasses.size(); i++)
                sb.append(nestedClasses.get(i).toTreeString(nextPrefix(newPrefix, false), i == nestedClasses.size() - 1));
        }
        if (!decorators.isEmpty()) {
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
        sb.append("class ").append(name);
        if (baseClass != null && !baseClass.isEmpty()) {
            sb.append("(").append(baseClass).append(")");
        }
        sb.append(":\n");
        for (AssignmentNode var : variables) {
            sb.append("    ").append(var.generateCode()).append("\n");
        }
        for (FunctionNode method : methods) {
            sb.append(method.generateCode()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}