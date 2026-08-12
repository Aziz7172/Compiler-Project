package SymbolTable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * SymbolTable manages nested scopes using a Deque of Scope objects.
 * Each Scope is a HashMap with parent-chain resolution via Scope.resolve().
 * Supports enterScope / exitScope for function and Jinja block scoping.
 *
 * Pre-loads Python built-in functions (Flask, str, len, range, app, etc.)
 * into the global scope to avoid false-positive UNDEFINED_VARIABLE errors.
 */
public class SymbolTable {
    private static final Logger logger = Logger.getLogger(SymbolTable.class.getName());

    private final Deque<Scope> scopeStack;
    private final Scope globalScope;

    public SymbolTable() {
        this.scopeStack = new ArrayDeque<>();
        this.globalScope = new Scope("global", null);
        scopeStack.push(globalScope);
        preloadBuiltins();
    }

    private void preloadBuiltins() {
        String[] builtins = {
                "Flask", "str", "len", "range", "app",
                "request", "render_template", "redirect", "url_for", "session",
                "True", "False", "None", "int", "float", "dict", "list",
                "__name__"
        };
        for (String name : builtins) {
            globalScope.define(new Symbol(name, Symbol.SymbolType.IDENTIFIER, null, "builtin", 0));
        }
    }

    public void enterScope() {
        Scope newScope = new Scope("scope_" + scopeStack.size(), scopeStack.peek());
        scopeStack.push(newScope);
        logger.info("Entered new scope. Depth: " + scopeStack.size());
    }

    public void enterScope(String name) {
        Scope newScope = new Scope(name, scopeStack.peek());
        scopeStack.push(newScope);
        logger.info("Entered scope: " + name + ". Depth: " + scopeStack.size());
    }

    public void exitScope() {
        if (scopeStack.size() > 1) {
            scopeStack.pop();
            logger.info("Exited scope. Depth: " + scopeStack.size());
        } else {
            logger.warning("Attempted to exit the global scope.");
        }
    }

    public void define(Symbol sym) {
        sym.setScopeDepth(scopeStack.size());
        scopeStack.peek().define(sym);
        logger.info("Defined: " + sym.name + " in scope depth " + scopeStack.size());
    }

    public Symbol resolve(String name) {
        return scopeStack.peek().resolve(name);
    }

    public boolean contains(String name) {
        return resolve(name) != null;
    }

    public boolean isDefinedInCurrentScope(String name) {
        return scopeStack.peek().symbols.containsKey(name);
    }

    public void registerFlaskRoute(String routeName, int line) {
        Symbol existing = resolve(routeName);
        if (existing != null && existing.getType() == Symbol.SymbolType.FUNCTION) {
            logger.warning("Duplicate Flask route detected: " + routeName + " at line " + line);
        }
    }

    public int scopeDepth() {
        return scopeStack.size();
    }

    public Scope getCurrentScope() {
        return scopeStack.peek();
    }

    public Deque<Scope> getScopeStack() {
        return scopeStack;
    }

    /**
     * Prints all scopes and their symbols to the console.
     * Called by SemanticAnalyzer after analysis completes.
     */
    public void printAllScopes() {
        int depth = 0;
        for (Scope scope : scopeStack) {
            StringBuilder sb = new StringBuilder();
            sb.append("Scope #").append(depth).append(" [").append(scope.name).append("]: ");
            if (scope.symbols.isEmpty()) {
                sb.append("(empty)");
            } else {
                for (Map.Entry<String, Symbol> entry : scope.symbols.entrySet()) {
                    Symbol s = entry.getValue();
                    sb.append("\n    ").append(s.toString());
                }
            }
            System.out.println(sb);
            depth++;
        }
    }
}