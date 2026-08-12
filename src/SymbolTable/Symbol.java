package SymbolTable;

import AST.ASTNode;

public class Symbol {
    public String name;
    public enum SymbolType {
        VARIABLE,
        FUNCTION,
        CLASS,
        IDENTIFIER,
        DECORATOR
    }
    private SymbolType type;
    private Object value;
    private String dataType;
    private int line;
    private int scopeDepth;

    public Symbol(String name, SymbolType type, Object value, String dataType, int line) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.dataType = dataType;
        this.line = line;
        this.scopeDepth = 0;
    }

    public Symbol(String name, SymbolType type, Object value, String dataType, int line, int scopeDepth) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.dataType = dataType;
        this.line = line;
        this.scopeDepth = scopeDepth;
    }

    public String getName() { return name; }
    public SymbolType getType() { return type; }
    public Object getValue() { return value; }
    public String getDataType() { return dataType; }
    public int getLine() { return line; }
    public int getScopeDepth() { return scopeDepth; }
    public void setScopeDepth(int depth) { this.scopeDepth = depth; }

    @Override
    public String toString() {
        return String.format("%-10s | %-10s | %-19s | %-10s | line %-4d | scope %d",
                name,
                type,
                value,
                dataType,
                line,
                scopeDepth);
    }
}
