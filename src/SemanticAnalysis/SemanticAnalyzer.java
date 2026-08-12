package SemanticAnalysis;

import SymbolTable.Symbol;
import SymbolTable.SymbolTable;
import AST.*;
import AST.JINJA2.*;
import AST.CSS.*;
import AST.HTML.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;

public class SemanticAnalyzer implements ASTVisitor {
    private static final Logger logger = Logger.getLogger(SemanticAnalyzer.class.getName());

    private final SymbolTable symbolTable;
    private final List<SemanticError> semanticErrors;
    private final Set<String> declaredRoutes;
    private final Set<String> jinjaKnownVariables;
    private int functionNestingDepth;
    private boolean inFlaskRoute;

    public SemanticAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.semanticErrors = new ArrayList<>();
        this.declaredRoutes = new HashSet<>();
        this.jinjaKnownVariables = new HashSet<>();
        this.functionNestingDepth = 0;
        this.inFlaskRoute = false;
    }

    public List<SemanticError> getSemanticErrors() {
        return semanticErrors;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void analyze(ASTNode root) {
        logger.info("Starting semantic analysis...");
        try {
            if (root != null) {
                root.accept(this);
            }
        } catch (Exception e) {
            // PANIC MODE: log and continue — compiler must not crash
            reportError(SemanticError.ErrorType.TYPE_ERROR,
                    "PANIC_MODE: Unexpected error during analysis — " + e.getMessage(), root);
            System.err.println("[PANIC_MODE] Skipping subtree due to: " + e.getMessage());
        }
        checkFlaskRouteRequirements();
        printSymbolTable();
        logger.info("Semantic analysis complete. Errors found: " + semanticErrors.size());
    }

    private void reportError(SemanticError.ErrorType type, String message, ASTNode node) {
        int line = (node != null) ? node.getLineNumber() : 0;
        semanticErrors.add(new SemanticError(type, line, message));
        logger.warning("Semantic error [" + type + "] at line " + line + ": " + message);
    }

    private void printSymbolTable() {
        System.out.println("\n========== SYMBOL TABLE ==========");
        symbolTable.printAllScopes();
        System.out.println("==================================\n");
    }

    // ── SCOPE_ERROR ──
    private void checkScopeError(IdentifierNode node) {
        if (node.getName() == null) return;
        Symbol sym = symbolTable.resolve(node.getName());
        if (sym == null) return;
        int defDepth = sym.getScopeDepth();
        int curDepth = symbolTable.scopeDepth();
        if (defDepth > curDepth) {
            reportError(SemanticError.ErrorType.SCOPE_ERROR,
                    "Variable '" + node.getName() + "' is not accessible in this scope (defined at depth "
                    + defDepth + ", accessed at depth " + curDepth + ")", node);
        }
    }

    // ── TYPE_ERROR ──
    private void checkTypeError(BinaryOpNode node) {
        if (node.left == null || node.right == null) return;
        String op = node.operator;
        if (op == null) return;

        String leftType = inferType(node.left);
        String rightType = inferType(node.right);

        if (leftType == null || rightType == null) return;

        if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("//") || op.equals("%")) {
            if (!leftType.equals("number") && !leftType.equals("string")) {
                reportError(SemanticError.ErrorType.TYPE_ERROR,
                        "Cannot apply operator '" + op + "' to type '" + leftType + "'", node);
            }
            if (!rightType.equals("number") && !(op.equals("+") && rightType.equals("string"))) {
                reportError(SemanticError.ErrorType.TYPE_ERROR,
                        "Cannot apply operator '" + op + "' to type '" + rightType + "'", node);
            }
            if (op.equals("+") && leftType.equals("number") && rightType.equals("string")) {
                reportError(SemanticError.ErrorType.TYPE_ERROR,
                        "Cannot add string to number: use str() to convert", node);
            }
            if (op.equals("+") && leftType.equals("string") && rightType.equals("number")) {
                reportError(SemanticError.ErrorType.TYPE_MISMATCH,
                        "Cannot add number to string: use str() to convert", node);
            }
        }

        if (op.equals("==") || op.equals("!=") || op.equals("<") || op.equals(">") || op.equals("<=") || op.equals(">=")) {
            boolean equalityOnly = op.equals("==") || op.equals("!=");
            boolean special = leftType.equals("none") || rightType.equals("none")
                    || (equalityOnly && (leftType.equals("builtin") || rightType.equals("builtin")
                    || leftType.equals("unknown") || rightType.equals("unknown")));
            if (!leftType.equals(rightType) && !special) {
                reportError(SemanticError.ErrorType.TYPE_ERROR,
                        "Cannot compare types '" + leftType + "' and '" + rightType + "' with '" + op + "'", node);
            }
        }

        if (op.equals("and") || op.equals("or")) {
            if (!leftType.equals("boolean") || !rightType.equals("boolean")) {
                reportError(SemanticError.ErrorType.TYPE_ERROR,
                        "Operator '" + op + "' requires boolean operands, found '" + leftType + "' and '" + rightType + "'", node);
            }
        }
    }

    private String inferType(ASTNode node) {
        if (node instanceof NumberNode) {
            return "number";
        } else if (node instanceof StringNode) {
            return "string";
        } else if (node instanceof BooleanNode) {
            return "boolean";
        } else if (node instanceof ListNode) {
            return "list";
        } else if (node instanceof TupleNode) {
            return "tuple";
        } else if (node instanceof IdentifierNode) {
            Symbol sym = symbolTable.resolve(((IdentifierNode) node).getName());
            if (sym != null && sym.getDataType() != null) {
                return sym.getDataType();
            }
            return "unknown";
        } else if (node instanceof BinaryOpNode) {
            String op = ((BinaryOpNode) node).operator;
            if (op != null && (op.equals("==") || op.equals("!=") || op.equals("<")
                    || op.equals(">") || op.equals("<=") || op.equals(">=")
                    || op.equals("and") || op.equals("or"))) {
                return "boolean";
            }
            return "number";
        } else if (node instanceof FunctionCallNode) {
            String name = ((FunctionCallNode) node).getFunctionName();
            if (name != null) {
                if (name.equals("str")) return "string";
                if (name.equals("int") || name.equals("float") || name.equals("len")
                        || name.equals("range")) return "number";
                if (name.equals("list")) return "list";
                if (name.equals("dict")) return "dict";
            }
            return "unknown";
        }
        return "unknown";
    }

    // ── MISSING_FLASK_VARIABLE ──
    private void checkFlaskRouteRequirements() {
        for (String route : declaredRoutes) {
            if (!symbolTable.contains("Flask")) {
                reportError(SemanticError.ErrorType.MISSING_FLASK_VARIABLE,
                        "Flask class 'Flask' is not defined — required for route: " + route, null);
            }
            if (!symbolTable.contains("app")) {
                reportError(SemanticError.ErrorType.MISSING_FLASK_VARIABLE,
                        "Flask app instance 'app' is not defined — required for route: " + route, null);
            }
        }
    }

    // ──────────────────────── Core ASTVisitor methods ────────────────────────

    @Override
    public void visit(ProgramNode node) {
        if (node == null) return;
        if (node.getStatements() != null) {
            for (ASTNode stmt : node.getStatements()) {
                if (stmt != null) {
                    try {
                        stmt.accept(this);
                    } catch (Exception e) {
                        reportError(SemanticError.ErrorType.TYPE_ERROR,
                                "PANIC_MODE: Skipping statement — " + e.getMessage(), stmt);
                        System.err.println("[PANIC_MODE] Line " + stmt.getLineNumber() + ": Skipping statement — " + e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void visit(FunctionNode node) {
        if (node == null) return;
        try {
            if (node.getName() != null) {
                symbolTable.define(new Symbol(node.getName(), Symbol.SymbolType.FUNCTION, "function", null, node.getLineNumber()));
            }
            if (node.getName() != null && node.getName().startsWith("route_")) {
                String routePath = node.getName().substring(6);
                inFlaskRoute = true;
                if (!declaredRoutes.add(routePath)) {
                    reportError(SemanticError.ErrorType.DUPLICATE_FLASK_ROUTE, "Duplicate Flask route declared: " + routePath, node);
                }
            }

            symbolTable.enterScope();
            if (node.getParameters() != null) {
                for (ParameterNode param : node.getParameters()) {
                    if (param != null && param.getName() != null) {
                        if (symbolTable.isDefinedInCurrentScope(param.getName())) {
                            reportError(SemanticError.ErrorType.SCOPE_ERROR, "Duplicate parameter declaration: " + param.getName(), param);
                        } else {
                            symbolTable.define(new Symbol(param.getName(), Symbol.SymbolType.VARIABLE, "parameter", null, param.getLineNumber()));
                        }
                    }
                }
            }

            functionNestingDepth++;
            if (node.getBody() != null) {
                for (ASTNode stmt : node.getBody()) {
                    if (stmt != null) {
                        try {
                            stmt.accept(this);
                        } catch (Exception e) {
                            reportError(SemanticError.ErrorType.TYPE_ERROR,
                                    "PANIC_MODE: Skipping statement in function '" + node.getName() + "' — " + e.getMessage(), stmt);
                            System.err.println("[PANIC_MODE] Line " + stmt.getLineNumber() + ": Skipping statement — " + e.getMessage());
                        }
                    }
                }
            }
            functionNestingDepth--;
            inFlaskRoute = false;
            symbolTable.exitScope();
        } catch (Exception e) {
            reportError(SemanticError.ErrorType.TYPE_ERROR,
                    "PANIC_MODE: Error in function '" + node.getName() + "' — " + e.getMessage(), node);
            System.err.println("[PANIC_MODE] Line " + node.getLineNumber() + ": Error in function — " + e.getMessage());
        }
    }

    @Override
    public void visit(ClassNode node) {
        if (node.getName() != null) {
            symbolTable.define(new Symbol(node.getName(), Symbol.SymbolType.CLASS, "class", null, node.getLineNumber()));
        }
    }

    @Override
    public void visit(AssignmentNode node) {
        if (node == null) return;
        try {
            if (node.getName() != null) {
                String inferredType = (node.value != null) ? inferType(node.value) : "unknown";
                symbolTable.define(new Symbol(node.getName(), Symbol.SymbolType.VARIABLE, null, inferredType, node.getLineNumber()));
            }
            if (node.value != null) node.value.accept(this);
        } catch (Exception e) {
            reportError(SemanticError.ErrorType.TYPE_ERROR,
                    "PANIC_MODE: Error in assignment '" + node.getName() + "' — " + e.getMessage(), node);
            System.err.println("[PANIC_MODE] Line " + node.getLineNumber() + ": Skipping assignment — " + e.getMessage());
        }
    }

    @Override
    public void visit(IdentifierNode node) {
        if (node == null) return;
        if (node.getName() != null && !symbolTable.contains(node.getName())) {
            reportError(SemanticError.ErrorType.UNDEFINED_VARIABLE, "Undefined variable: " + node.getName(), node);
        } else {
            checkScopeError(node);
        }
    }

    @Override
    public void visit(FunctionCallNode node) {
        if (node == null) return;
        try {
            String callee = node.getFunctionName();
            if (callee != null && !symbolTable.contains(callee)) {
                reportError(SemanticError.ErrorType.UNDEFINED_VARIABLE, "Undefined function or variable: " + callee, node);
            }
            if (node.getArguments() != null) {
                for (ASTNode arg : node.getArguments()) {
                    if (arg != null) arg.accept(this);
                }
            }
        } catch (Exception e) {
            reportError(SemanticError.ErrorType.TYPE_ERROR,
                    "PANIC_MODE: Error in function call — " + e.getMessage(), node);
            System.err.println("[PANIC_MODE] Line " + node.getLineNumber() + ": Skipping function call — " + e.getMessage());
        }
    }

    @Override
    public void visit(ForNode node) {
        if (node == null) return;
        try {
            String varName = node.getVariable();
            if (varName != null) {
                if (symbolTable.isDefinedInCurrentScope(varName)) {
                    reportError(SemanticError.ErrorType.SCOPE_ERROR, "Duplicate loop variable declaration: " + varName, node);
                } else {
                    symbolTable.define(new Symbol(varName, Symbol.SymbolType.VARIABLE, "loop_variable", null, node.getLineNumber()));
                }
            }

            if (node.getIterable() != null) {
                node.getIterable().accept(this);
                if (node.getIterable() instanceof IdentifierNode) {
                    String iterName = ((IdentifierNode) node.getIterable()).getName();
                    if (iterName != null && !symbolTable.contains(iterName) && !jinjaKnownVariables.contains(iterName)) {
                        reportError(SemanticError.ErrorType.TYPE_ERROR, "Cannot iterate over undefined: " + iterName, node);
                    } else if (iterName != null) {
                        String iterType = inferType(node.getIterable());
                        if (iterType != null && !iterType.equals("list") && !iterType.equals("tuple")
                                && !iterType.equals("string") && !iterType.equals("unknown")) {
                            reportError(SemanticError.ErrorType.TYPE_ERROR,
                                    "Cannot iterate over type '" + iterType + "'", node);
                        }
                    }
                }
            }

            symbolTable.enterScope();
            if (node.getBody() != null) {
                for (ASTNode stmt : node.getBody()) {
                    if (stmt != null) {
                        try {
                            stmt.accept(this);
                        } catch (Exception e) {
                            reportError(SemanticError.ErrorType.TYPE_ERROR,
                                    "PANIC_MODE: Skipping statement in for-loop body — " + e.getMessage(), stmt);
                            System.err.println("[PANIC_MODE] Line " + stmt.getLineNumber() + ": Skipping statement — " + e.getMessage());
                        }
                    }
                }
            }
            symbolTable.exitScope();
        } catch (Exception e) {
            reportError(SemanticError.ErrorType.TYPE_ERROR,
                    "PANIC_MODE: Error in for-loop — " + e.getMessage(), node);
            System.err.println("[PANIC_MODE] Line " + node.getLineNumber() + ": Error in for-loop — " + e.getMessage());
        }
    }

    @Override
    public void visit(WhileNode node) {
        if (node == null) return;
        try {
            if (node.getCondition() != null) node.getCondition().accept(this);
            symbolTable.enterScope();
            if (node.getBody() != null) {
                for (ASTNode stmt : node.getBody()) {
                    if (stmt != null) {
                        try {
                            stmt.accept(this);
                        } catch (Exception e) {
                            reportError(SemanticError.ErrorType.TYPE_ERROR,
                                    "PANIC_MODE: Skipping statement in while-loop — " + e.getMessage(), stmt);
                            System.err.println("[PANIC_MODE] Line " + stmt.getLineNumber() + ": Skipping statement — " + e.getMessage());
                        }
                    }
                }
            }
            symbolTable.exitScope();
        } catch (Exception e) {
            reportError(SemanticError.ErrorType.TYPE_ERROR,
                    "PANIC_MODE: Error in while-loop — " + e.getMessage(), node);
            System.err.println("[PANIC_MODE] Line " + node.getLineNumber() + ": Error in while-loop — " + e.getMessage());
        }
    }

    // FIX: IfNode has getChildren() for then-body, getElifClauses(), getElseBody()
    @Override
    public void visit(IfNode node) {
        if (node == null) return;
        try {
            if (node.getCondition() != null) node.getCondition().accept(this);
            // Then-body is stored in children via ConnectorNode.getChildren()
            List<ASTNode> thenBody = node.getChildren();
            if (thenBody != null) {
                symbolTable.enterScope();
                for (ASTNode stmt : thenBody) {
                    if (stmt != null) {
                        try {
                            stmt.accept(this);
                        } catch (Exception e) {
                            reportError(SemanticError.ErrorType.TYPE_ERROR,
                                    "PANIC_MODE: Skipping statement in if-body — " + e.getMessage(), stmt);
                            System.err.println("[PANIC_MODE] Line " + stmt.getLineNumber() + ": Skipping statement — " + e.getMessage());
                        }
                    }
                }
                symbolTable.exitScope();
            }
            // Elif clauses
            if (node.getElifClauses() != null) {
                for (ASTNode elif : node.getElifClauses()) {
                    if (elif != null) {
                        try {
                            elif.accept(this);
                        } catch (Exception e) {
                            reportError(SemanticError.ErrorType.TYPE_ERROR,
                                    "PANIC_MODE: Error in elif-clause — " + e.getMessage(), elif);
                            System.err.println("[PANIC_MODE] Line " + elif.getLineNumber() + ": Skipping elif — " + e.getMessage());
                        }
                    }
                }
            }
            // Else body
            if (node.getElseBody() != null) {
                symbolTable.enterScope();
                for (ASTNode stmt : node.getElseBody()) {
                    if (stmt != null) {
                        try {
                            stmt.accept(this);
                        } catch (Exception e) {
                            reportError(SemanticError.ErrorType.TYPE_ERROR,
                                    "PANIC_MODE: Skipping statement in else-body — " + e.getMessage(), stmt);
                            System.err.println("[PANIC_MODE] Line " + stmt.getLineNumber() + ": Skipping statement — " + e.getMessage());
                        }
                    }
                }
                symbolTable.exitScope();
            }
        } catch (Exception e) {
            reportError(SemanticError.ErrorType.TYPE_ERROR,
                    "PANIC_MODE: Error in if-statement — " + e.getMessage(), node);
            System.err.println("[PANIC_MODE] Line " + node.getLineNumber() + ": Error in if-statement — " + e.getMessage());
        }
    }

    @Override
    public void visit(ElifNode node) {
        if (node.getCondition() != null) node.getCondition().accept(this);
        if (node.getBody() != null) {
            for (ASTNode stmt : node.getBody()) {
                if (stmt != null) stmt.accept(this);
            }
        }
    }

    @Override
    public void visit(ElseNode node) {
        if (node.getBody() != null) {
            for (ASTNode stmt : node.getBody()) {
                if (stmt != null) stmt.accept(this);
            }
        }
    }

    @Override
    public void visit(ReturnNode node) {
        if (node.getValue() != null) node.getValue().accept(this);
    }

    @Override
    public void visit(BinaryOpNode node) {
        if (node == null) return;
        try {
            if (node.left != null) node.left.accept(this);
            if (node.right != null) node.right.accept(this);
            checkTypeError(node);
        } catch (Exception e) {
            reportError(SemanticError.ErrorType.TYPE_ERROR,
                    "PANIC_MODE: Error in binary expression — " + e.getMessage(), node);
            System.err.println("[PANIC_MODE] Line " + node.getLineNumber() + ": Skipping binary expression — " + e.getMessage());
        }
    }

    // FIX: UnaryNode has public field 'expression', no getOperand()
    @Override
    public void visit(UnaryNode node) {
        if (node.expression != null) node.expression.accept(this);
    }

    // FIX: PrintNode has getNodes() returning List<ASTNode>, no getValue()
    @Override
    public void visit(PrintNode node) {
        if (node == null) return;
        try {
            if (node.getNodes() != null) {
                for (ASTNode n : node.getNodes()) {
                    if (n != null) n.accept(this);
                }
            }
        } catch (Exception e) {
            reportError(SemanticError.ErrorType.TYPE_ERROR,
                    "PANIC_MODE: Error in print statement — " + e.getMessage(), node);
            System.err.println("[PANIC_MODE] Line " + node.getLineNumber() + ": Skipping print — " + e.getMessage());
        }
    }

    @Override
    public void visit(StringNode node) {
    }

    @Override
    public void visit(NumberNode node) {
    }

    @Override
    public void visit(BooleanNode node) {
    }

    @Override
    public void visit(ListNode node) {
        if (node == null) return;
        if (node.getElements() != null) {
            for (ASTNode elt : node.getElements()) {
                if (elt != null) elt.accept(this);
            }
        }
    }

    @Override
    public void visit(TupleNode node) {
        if (node == null) return;
        if (node.getElements() != null) {
            for (ASTNode elt : node.getElements()) {
                if (elt != null) elt.accept(this);
            }
        }
    }

    // FIX: AttributeAccessNode has public field 'object', no getTarget()
    @Override
    public void visit(AttributeAccessNode node) {
        if (node == null) return;
        try {
            if (node.object != null) node.object.accept(this);
            if (node.attribute != null) node.attribute.accept(this);
        } catch (Exception e) {
            reportError(SemanticError.ErrorType.TYPE_ERROR,
                    "PANIC_MODE: Error in attribute access — " + e.getMessage(), node);
            System.err.println("[PANIC_MODE] Line " + node.getLineNumber() + ": Skipping attribute access — " + e.getMessage());
        }
    }

    // FIX: IndexAccessNode has public fields 'object' and 'index', no getTarget()/getIndex()
    @Override
    public void visit(IndexAccessNode node) {
        if (node == null) return;
        try {
            if (node.object != null) node.object.accept(this);
            if (node.index != null) node.index.accept(this);
        } catch (Exception e) {
            reportError(SemanticError.ErrorType.TYPE_ERROR,
                    "PANIC_MODE: Error in index access — " + e.getMessage(), node);
            System.err.println("[PANIC_MODE] Line " + node.getLineNumber() + ": Skipping index access — " + e.getMessage());
        }
    }

    @Override
    public void visit(FromImportNode node) {
    }

    @Override
    public void visit(ParameterNode node) {
    }

    @Override
    public void visit(ArgumentNode node) {
    }

    @Override
    public void visit(DecoratorNode node) {
    }

    @Override
    public void visit(GlobalNode node) {
    }

    @Override
    public void visit(ListComprehensionNode node) {
    }

    @Override
    public void visit(ImportedNode node) {
    }

    @Override
    public void visit(ImportedListNode node) {
    }

    // ──────────────── HTML visitor methods ────────────────

    @Override
    public void visit(HtmlElementNode node) {
    }

    @Override
    public void visit(HtmlTextNode node) {
    }

    // ──────────────── CSS visitor methods ────────────────

    @Override
    public void visit(CssNode node) {
    }

    @Override
    public void visit(CssBlockNode node) {
    }

    @Override
    public void visit(CssPropertyNode node) {
    }

    // ──────────────── Extend/Include/JSON/JinjaExpression visitor methods ────────────────

    @Override
    public void visit(ExtendNode node) {
    }

    @Override
    public void visit(IncludeNode node) {
    }

    @Override
    public void visit(JSONNode node) {
    }

    @Override
    public void visit(JinjaExpressionNode node) {
    }

    // ──────────────── Jinja2-specific visitor methods ────────────────

    @Override
    public void visit(Jinja2TemplateNode node) {
    }

    @Override
    public void visit(Jinja2TextNode node) {
    }

    @Override
    public void visit(Jinja2ExprNode node) {
    }

    @Override
    public void visit(Jinja2CommentNode node) {
    }

    @Override
    public void visit(Jinja2IfNode node) {
    }

    @Override
    public void visit(Jinja2ForNode node) {
    }

    @Override
    public void visit(Jinja2BlockNode node) {
    }

    @Override
    public void visit(Jinja2SetNode node) {
    }

    @Override
    public void visit(Jinja2ExtendsNode node) {
    }

    @Override
    public void visit(Jinja2IncludeNode node) {
    }

    @Override
    public void visit(Jinja2WithNode node) {
    }

    // ──────────────── Jinja2 analysis helpers (non-visitor) ────────────────

    public void analyzeJinjaForNode(List<String> loopVars, String iterableExpr) {
        for (String var : loopVars) {
            if (var != null && !symbolTable.contains(var) && !jinjaKnownVariables.contains(var)) {
                reportError(SemanticError.ErrorType.UNDEFINED_CONTEXT_VARIABLE,
                        "Undefined Jinja context variable in loop: " + var, null);
            }
            if (var != null) {
                symbolTable.define(new Symbol(var, Symbol.SymbolType.VARIABLE, "jinja_loop_var", null, 0));
            }
        }
        if (iterableExpr != null && !iterableExpr.trim().isEmpty()) {
            if (!symbolTable.contains(iterableExpr.trim()) && !jinjaKnownVariables.contains(iterableExpr.trim())) {
                reportError(SemanticError.ErrorType.TYPE_ERROR,
                        "Cannot iterate over undefined: " + iterableExpr, null);
            }
        }
    }

    public void analyzeJinjaIfNode(String condition) {
        if (condition != null && !condition.trim().isEmpty()) {
            String trimmed = condition.trim();
            String[] parts = trimmed.split("\\s+");
            for (String part : parts) {
                String clean = part.replaceAll("[^a-zA-Z_][a-zA-Z0-9_]*", "").replaceAll("[^a-zA-Z_]", "");
                if (!clean.isEmpty() && !symbolTable.contains(clean) && !jinjaKnownVariables.contains(clean)) {
                    reportError(SemanticError.ErrorType.UNDEFINED_CONTEXT_VARIABLE,
                            "Undefined Jinja context variable: " + clean, null);
                }
            }
        }
    }

    public void analyzeJinjaExpression(String expr) {
        if (expr != null && !expr.trim().isEmpty()) {
            String trimmed = expr.trim();
            String[] parts = trimmed.split("[\\s\\+\\-\\*\\/\\%\\(\\)\\[\\]\\{\\}\\.\\,\\:\\;\\!\\=\\<\\>]+");
            for (String part : parts) {
                String clean = part.replaceAll("[^a-zA-Z_][a-zA-Z0-9_]*$", "").trim();
                if (!clean.isEmpty() && !clean.matches("^\\d+$") && !symbolTable.contains(clean) && !jinjaKnownVariables.contains(clean)) {
                    reportError(SemanticError.ErrorType.UNDEFINED_CONTEXT_VARIABLE,
                            "Undefined Jinja context variable: " + clean, null);
                }
            }
        }
    }

    public void registerJinjaKnownVariable(String varName) {
        if (varName != null) {
            jinjaKnownVariables.add(varName);
        }
    }

    public void registerJinjaKnownVariables(List<String> varNames) {
        if (varNames != null) {
            jinjaKnownVariables.addAll(varNames);
        }
    }

    /**
     * Write all collected semantic errors to compiler_output/semantic_report.txt.
     * Convenience method for Panic Mode recovery — compiler must not crash.
     */
    public void writeReport(String outputDir) {
        try {
            SemanticReporter.writeErrors(outputDir, semanticErrors);
            System.out.println("Semantic report written to: " + outputDir + "/semantic_report.txt");
        } catch (Exception e) {
            System.err.println("Failed to write semantic report: " + e.getMessage());
        }
    }
}
