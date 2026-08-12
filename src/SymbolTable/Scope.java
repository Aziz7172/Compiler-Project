package SymbolTable;

import java.util.*;
import java.util.logging.Logger;

public class Scope {
    private static final Logger logger = Logger.getLogger(Scope.class.getName());
    String name;
    public Map<String, Symbol> symbols = new HashMap<>();
    Scope parent;

    public Scope(String name, Scope parent) {
        this.name = name;
        this.parent = parent;
    }

    public void define(Symbol sym) {
        symbols.put(sym.name, sym);
    }

    public Symbol resolve(String name) {
        Symbol sym = symbols.get(name);
        if (sym != null) return sym;
        if (parent != null) return parent.resolve(name);
        return null;
    }

    public void printScope(String indent) {
        logger.info(indent + "Scope: " + name);
        logger.info(indent + "Symbols:");
        for (Symbol sym : symbols.values()) {
            logger.info(indent + "  " + sym);
        }
        logger.info("");
    }

}
