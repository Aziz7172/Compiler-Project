package CodeGenerator;

import AST.*;
import AST.HTML.*;
import AST.CSS.*;
import AST.JINJA2.*;
import VM.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlCodeGenerator implements ASTVisitor {
    private static final String LOG_DIR = "compiler_output";
    private static final String LOG_FILE = LOG_DIR + "/generation_log.txt";

    private final Context rootContext;
    private final List<String> logEntries;
    private int indentLevel;
    private Context currentCtx;
    private String lastResult;

    public HtmlCodeGenerator(Context context) {
        this.rootContext = context;
        this.logEntries = new ArrayList<>();
        this.indentLevel = 0;
        this.currentCtx = context;
        this.lastResult = "";
    }

    // ─── Public API ───────────────────────────────────────────

    public String generate(ASTNode root) {
        log("=== HTML Code Generation Started (Visitor Pattern) ===");
        log("Context entries: " + rootContext.size());
        StringBuilder output = new StringBuilder();
        indentLevel = 0;
        currentCtx = rootContext;
        if (root != null) {
            root.accept(this);
            output.append(lastResult);
        }
        String result = output.toString();
        log("=== HTML Code Generation Complete ===");
        log("Output length: " + result.length() + " characters");
        writeLog();
        return result;
    }

    public String generate(Jinja2Node root) {
        if (root == null) return "";
        root.accept(this);
        String rendered = lastResult;
        if (rendered != null && !rendered.isEmpty()) {
            rendered = rewriteLinks(rendered);
        }
        return rendered;
    }

    /**
     * Rewrite Flask-style absolute root paths inside href=/action= attributes
     * into static-site relative .html links.
     *
     * Handles attribute values that may contain embedded Jinja2 expressions
     * (e.g. href="/product/{{ product.id }}"), which the text/expr split of the
     * Jinja2 lexer would otherwise leave as "/product/".
     *
     * Examples:
     *   href="/"               -> href="index.html"
     *   href="/products"       -> href="products.html"
     *   href="/add"            -> href="add_product.html"
     *   href="/product/7"      -> href="product_details.html?id=7"
     *   href="/edit/7"         -> href="edit_product.html?id=7"
     *   href="/delete/7"       -> href="index.html?delete=7"
     */
    private String rewriteLinks(String html) {
        if (html == null || html.isEmpty()) return html;
        StringBuilder out = new StringBuilder(html.length() + 128);
        int i = 0;
        int n = html.length();
        while (i < n) {
            // Search for the next href= or action= attribute
            int attrIdx = indexOfAttrStart(html, i);
            if (attrIdx < 0) {
                out.append(html.substring(i));
                break;
            }
            out.append(html, i, attrIdx);
            i = attrIdx;
            // Consume attribute name + '='
            int eq = html.indexOf('=', attrIdx);
            if (eq < 0) {
                out.append(html.substring(i));
                break;
            }
            out.append(html, i, eq + 1);
            i = eq + 1;
            // Skip whitespace after '='
            while (i < n && Character.isWhitespace(html.charAt(i))) {
                out.append(html.charAt(i));
                i++;
            }
            if (i >= n || html.charAt(i) != '"') {
                // Not a double-quoted value; just continue scanning
                continue;
            }
            out.append('"');
            i++;
            // Extract the raw value (may contain '{{ ... }}')
            int endQuote = html.indexOf('"', i);
            if (endQuote < 0) {
                out.append(html.substring(i));
                break;
            }
            String rawValue = html.substring(i, endQuote);
            String resolved = resolveLink(rawValue);
            out.append(resolved).append('"');
            i = endQuote + 1;
        }
        return out.toString();
    }

    private int indexOfAttrStart(String html, int from) {
        int n = html.length();
        for (int i = from; i + 1 < n; i++) {
            char c = html.charAt(i);
            if (c == 'h' && html.charAt(i + 1) == 'r') {
                if (i + 4 < n && html.substring(i, i + 5).equals("href=")) {
                    return i;
                }
            } else if (c == 'a') {
                if (i + 6 < n && html.substring(i, i + 7).equals("action=")) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Convert a Flask-style absolute root path (optionally containing embedded
     * Jinja2 expressions) into a static .html relative path.
     *
     * Only paths that start with "/" are rewritten. External URLs (http, https,
     * mailto, #fragment, data:, javascript:) are left untouched, as are already
     * relative or already-.html targets.
     */
    private String resolveLink(String rawLink) {
        if (rawLink == null || rawLink.isEmpty()) return rawLink;
        String link = rawLink.trim();
        if (link.isEmpty()) return link;

        // Leave external/absolute/existing-relative links alone
        if (!link.startsWith("/")) return rawLink;

        // Split off any existing query string / fragment
        String query = "";
        String fragment = "";
        int qIdx = link.indexOf('?');
        int hIdx = link.indexOf('#');
        int cut = link.length();
        if (qIdx >= 0 && qIdx < cut) cut = qIdx;
        if (hIdx >= 0 && hIdx < cut) cut = hIdx;
        if (cut < link.length()) {
            String tail = link.substring(cut);
            if (tail.startsWith("?")) {
                int fIdx = tail.indexOf('#');
                if (fIdx >= 0) {
                    query = tail.substring(0, fIdx);
                    fragment = tail.substring(fIdx);
                } else {
                    query = tail;
                }
            } else {
                fragment = tail;
            }
            link = link.substring(0, cut);
        }

        // Root -> index.html
        if (link.equals("/")) return "index.html" + query + fragment;

        // Already a .html target
        if (link.endsWith(".html")) return link + query + fragment;

        // Strip leading slash
        String path = link.substring(1);

        // Split path segments, keeping the raw last segment intact so embedded
        // expressions like "{{ product.id }}" are preserved for later passes.
        int slashIdx = path.indexOf('/');
        String first;
        String rest;
        if (slashIdx >= 0) {
            first = path.substring(0, slashIdx);
            rest = path.substring(slashIdx + 1);
        } else {
            first = path;
            rest = "";
        }

        switch (first) {
            case "product":
                if (!rest.isEmpty()) {
                    return "product_details.html" + joinQuery("id=" + rest, query) + fragment;
                }
                return "product_details.html" + query + fragment;
            case "edit":
                if (!rest.isEmpty()) {
                    return "edit_product.html" + joinQuery("id=" + rest, query) + fragment;
                }
                return "edit_product.html" + query + fragment;
            case "delete":
                if (!rest.isEmpty()) {
                    return "index.html" + joinQuery("delete=" + rest, query) + fragment;
                }
                return "index.html" + query + fragment;
            case "add":
                return "add_product.html" + query + fragment;
            case "products":
                return "products.html" + query + fragment;
            case "students":
                return "students.html" + query + fragment;
            default:
                // Unknown single-segment route -> <name>.html
                if (rest.isEmpty()) {
                    return first + ".html" + query + fragment;
                }
                return first + "_" + rest.replace('/', '_') + ".html" + query + fragment;
        }
    }

    private String joinQuery(String newQuery, String existingQuery) {
        if (existingQuery == null || existingQuery.isEmpty()) {
            return "?" + newQuery;
        }
        if (existingQuery.startsWith("?")) {
            return existingQuery + "&" + newQuery;
        }
        return existingQuery + "&" + newQuery;
    }

    // ─── Context Helpers ──────────────────────────────────────

    private void pushCtx(Context ctx) {
        ctx.pushScope();
        this.currentCtx = ctx;
    }

    private void popCtx() {
        currentCtx.popScope();
    }

    private void emit(String html) {
        lastResult = (lastResult == null ? "" : lastResult) + html;
    }

    private void emitIndent() {
        emit(indent());
    }

    private void emitLine(String line) {
        emit(indent() + line + "\n");
    }

    /**
     * Dot-notation value resolver.
     * Handles "product.name" by splitting on '.' and traversing Maps.
     * Handles pipe filters like "products|length".
     * Also handles "loop.index", "loop.index0", etc.
     */
    private Object resolveDotNotation(String expression, Context ctx) {
        if (expression == null || expression.isEmpty()) return null;
        String trimmed = expression.trim();

        // Handle pipe filters: "var|filter"
        int pipeIdx = trimmed.indexOf('|');
        if (pipeIdx >= 0) {
            String varName = trimmed.substring(0, pipeIdx).trim();
            String filterName = trimmed.substring(pipeIdx + 1).trim();

            // Resolve the variable before the pipe
            Object value = resolveDotNotation(varName, ctx);

            // Apply the filter
            return applyFilter(value, filterName);
        }

        // Special loop variables
        if (trimmed.startsWith("loop.")) {
            // loop.index, loop.index0, etc. — caller must set these
            return ctx.lookup(trimmed);
        }

        // Try direct lookup first
        Object direct = ctx.lookup(trimmed);
        if (direct != null) return direct;

        // Dot-notation: split and traverse
        List<String> parts = splitOnDot(trimmed);
        if (parts.size() > 1) {
            Object current = ctx.lookup(parts.get(0));
            if (current == null) return null;
            for (int i = 1; i < parts.size(); i++) {
                if (current instanceof Map) {
                    current = ((Map<?, ?>) current).get(parts.get(i));
                } else {
                    return null;
                }
            }
            return current;
        }

        return null;
    }

    private List<String> splitOnDot(String s) {
        List<String> result = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                result.add(s.substring(start, i));
                start = i + 1;
            }
        }
        result.add(s.substring(start));
        return result;
    }

    /**
     * Apply a Jinja2 filter to a value.
     * Supports: length, upper, lower, capitalize, trim, first, last, reverse, join
     */
    private Object applyFilter(Object value, String filterName) {
        if (value == null) {
            return filterName.equals("length") ? 0 : null;
        }

        switch (filterName) {
            case "length":
                if (value instanceof Collection) return ((Collection<?>) value).size();
                if (value instanceof Map) return ((Map<?, ?>) value).size();
                if (value instanceof String) return ((String) value).length();
                if (value instanceof Object[]) return ((Object[]) value).length;
                return 0;

            case "upper":
                return value.toString().toUpperCase();

            case "lower":
                return value.toString().toLowerCase();

            case "capitalize":
                String s = value.toString();
                return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);

            case "trim":
                return value.toString().trim();

            case "first":
                if (value instanceof List) return ((List<?>) value).isEmpty() ? null : ((List<?>) value).get(0);
                return null;

            case "last":
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    return list.isEmpty() ? null : list.get(list.size() - 1);
                }
                return null;

            case "reverse":
                if (value instanceof List) {
                    List<Object> reversed = new ArrayList<>((List<?>) value);
                    Collections.reverse(reversed);
                    return reversed;
                }
                return value.toString();

            case "join":
                if (value instanceof Collection) {
                    StringBuilder sb = new StringBuilder();
                    boolean first = true;
                    for (Object item : (Collection<?>) value) {
                        if (!first) sb.append(", ");
                        sb.append(item);
                        first = false;
                    }
                    return sb.toString();
                }
                return value.toString();

            default:
                return value;
        }
    }

    // ─── ASTVisitor: Python Nodes ─────────────────────────────

    @Override
    public void visit(ProgramNode node) {
        log("Visiting ProgramNode");
        StringBuilder sb = new StringBuilder();
        if (node.getStatements() != null) {
            for (ASTNode stmt : node.getStatements()) {
                if (stmt != null) {
                    stmt.accept(this);
                    sb.append(lastResult);
                }
            }
        }
        lastResult = sb.toString();
    }

    @Override
    public void visit(FunctionNode node) {
        log("Visiting FunctionNode");
        StringBuilder sb = new StringBuilder();
        if (node.getBody() != null) {
            for (ASTNode stmt : node.getBody()) {
                if (stmt != null) {
                    stmt.accept(this);
                    sb.append(lastResult);
                }
            }
        }
        lastResult = sb.toString();
    }

    @Override
    public void visit(ClassNode node) { lastResult = ""; }

    @Override
    public void visit(AssignmentNode node) { lastResult = ""; }

    @Override
    public void visit(IfNode node) {
        log("Visiting Python IfNode");
        StringBuilder sb = new StringBuilder();
        ASTNode condition = node.getCondition();
        List<ASTNode> thenBranch = node.getChildren();
        List<ASTNode> elseBranch = node.getElseBody();
        if (condition != null) {
            Object conditionValue = resolveFromNode(condition);
            boolean isTrue = isTruthy(conditionValue);
            log("Python if condition evaluated to: " + isTrue);
            if (isTrue && thenBranch != null) {
                for (ASTNode stmt : thenBranch) {
                    if (stmt != null) {
                        stmt.accept(this);
                        sb.append(lastResult);
                    }
                }
            } else if (!isTrue && elseBranch != null) {
                for (ASTNode stmt : elseBranch) {
                    if (stmt != null) {
                        stmt.accept(this);
                        sb.append(lastResult);
                    }
                }
            }
        }
        lastResult = sb.toString();
    }

    @Override
    public void visit(ForNode node) {
        log("Visiting Python ForNode");
        StringBuilder sb = new StringBuilder();
        String varName = node.getVariable();
        ASTNode iterable = node.getIterable();
        List<ASTNode> body = node.getBody();
        if (varName != null && iterable != null && body != null) {
            Object items = resolveFromNode(iterable);
            List<?> itemList = toList(items);
            if (itemList != null) {
                for (Object item : itemList) {
                    Context childCtx = new Context(currentCtx.snapshot());
                    childCtx.assign(varName, item);
                    pushCtx(childCtx);
                    for (ASTNode stmt : body) {
                        if (stmt != null) {
                            stmt.accept(this);
                            sb.append(lastResult);
                        }
                    }
                    popCtx();
                }
            }
        }
        lastResult = sb.toString();
    }

    @Override
    public void visit(WhileNode node) {
        lastResult = "";
    }

    @Override
    public void visit(ReturnNode node) { lastResult = ""; }

    @Override
    public void visit(BinaryOpNode node) { lastResult = ""; }

    @Override
    public void visit(UnaryNode node) { lastResult = ""; }

    @Override
    public void visit(FunctionCallNode node) {
        lastResult = "";
    }

    @Override
    public void visit(IdentifierNode node) { lastResult = ""; }

    @Override
    public void visit(StringNode node) { lastResult = ""; }

    @Override
    public void visit(NumberNode node) { lastResult = ""; }

    @Override
    public void visit(BooleanNode node) { lastResult = ""; }

    @Override
    public void visit(ListNode node) { lastResult = ""; }

    @Override
    public void visit(TupleNode node) { lastResult = ""; }

    @Override
    public void visit(AttributeAccessNode node) { lastResult = ""; }

    @Override
    public void visit(IndexAccessNode node) { lastResult = ""; }

    @Override
    public void visit(FromImportNode node) { lastResult = ""; }

    @Override
    public void visit(ParameterNode node) { lastResult = ""; }

    @Override
    public void visit(ArgumentNode node) { lastResult = ""; }

    @Override
    public void visit(DecoratorNode node) { lastResult = ""; }

    @Override
    public void visit(GlobalNode node) { lastResult = ""; }

    @Override
    public void visit(PrintNode node) { lastResult = ""; }

    @Override
    public void visit(ListComprehensionNode node) { lastResult = ""; }

    @Override
    public void visit(ImportedNode node) { lastResult = ""; }

    @Override
    public void visit(ImportedListNode node) { lastResult = ""; }

    @Override
    public void visit(ElifNode node) { lastResult = ""; }

    @Override
    public void visit(ElseNode node) { lastResult = ""; }

    // ─── ASTVisitor: HTML Nodes ───────────────────────────────

    @Override
    public void visit(HtmlElementNode node) {
        String tagName = node.getTagName();
        log("Generating HTML element: <" + tagName + ">");
        StringBuilder sb = new StringBuilder();
        sb.append(indent());
        sb.append('<').append(tagName);
        Map<String, String> attrs = node.getAttributes();
        if (attrs != null && !attrs.isEmpty()) {
            for (Map.Entry<String, String> entry : attrs.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (("href".equalsIgnoreCase(key) || "action".equalsIgnoreCase(key))
                        && value != null && value.startsWith("/")) {
                    value = resolveLink(value);
                }
                sb.append(' ').append(key).append("=\"")
                  .append(htmlEscape(value)).append('"');
            }
        }
        List<ASTNode> children = node.getChildren();
        if (children != null && !children.isEmpty()) {
            sb.append(">\n");
            indentLevel++;
            for (ASTNode child : children) {
                if (child != null) {
                    child.accept(this);
                    sb.append(lastResult);
                }
            }
            indentLevel--;
            sb.append(indent()).append("</").append(tagName).append(">\n");
        } else {
            sb.append("></").append(tagName).append(">\n");
        }
        lastResult = sb.toString();
    }

    @Override
    public void visit(HtmlTextNode node) {
        String text = node.getText();
        if (text != null && !text.isEmpty()) {
            lastResult = indent() + htmlEscape(text) + "\n";
        } else {
            lastResult = "";
        }
    }

    // ─── ASTVisitor: CSS Nodes ────────────────────────────────

    @Override
    public void visit(CssNode node) { lastResult = ""; }

    @Override
    public void visit(CssBlockNode node) { lastResult = ""; }

    @Override
    public void visit(CssPropertyNode node) { lastResult = ""; }

    // ─── ASTVisitor: Extends/Include/JSON ─────────────────────

    @Override
    public void visit(ExtendNode node) { lastResult = ""; }

    @Override
    public void visit(IncludeNode node) { lastResult = ""; }

    @Override
    public void visit(JSONNode node) { lastResult = ""; }

    @Override
    public void visit(JinjaExpressionNode node) {
        ASTNode expr = node.getInnerExpression();
        if (expr == null) { lastResult = ""; return; }
        Object value = resolveFromNode(expr);
        String valueStr = objectToString(value);
        log("JinjaExpression resolved to: " + (valueStr != null && !valueStr.isEmpty() ? valueStr : "null"));
        lastResult = indent() + htmlEscape(valueStr != null ? valueStr : "");
    }

    // ─── ASTVisitor: Jinja2 Nodes ─────────────────────────────

    @Override
    public void visit(Jinja2TemplateNode node) {
        log("Visiting Jinja2TemplateNode: " + node.getTemplateName());
        StringBuilder sb = new StringBuilder();
        List<ASTNode> astChildren = node.getChildren();
        if (astChildren != null) {
            for (ASTNode child : astChildren) {
                if (child instanceof Jinja2Node) {
                    ((Jinja2Node) child).accept(this);
                    sb.append(lastResult);
                }
            }
        }
        lastResult = sb.toString();
    }

    @Override
    public void visit(Jinja2TextNode node) {
        String text = node.getRawText();
        if (text != null && !text.isEmpty()) {
            lastResult = text;
        } else {
            lastResult = "";
        }
    }

    @Override
    public void visit(Jinja2ExprNode node) {
        String expr = node.getExpression();
        if (expr == null || expr.trim().isEmpty()) { lastResult = ""; return; }
        String trimmed = expr.trim();
        Object value = resolveDotNotation(trimmed, currentCtx);
        String valueStr = objectToString(value);
        lastResult = htmlEscape(valueStr != null ? valueStr : "");
    }

    @Override
    public void visit(Jinja2CommentNode node) {
        lastResult = "";
    }

    @Override
    public void visit(Jinja2IfNode node) {
        String condition = node.getCondition();
        List<Jinja2Node> body = node.getBody();
        List<Jinja2IfNode.Jinja2ElifClause> elifClauses = node.getElifClauses();
        List<Jinja2Node> elseBody = node.getElseBody();
        log("Evaluating Jinja if condition: " + condition);
        if (condition == null) { lastResult = ""; return; }

        Object condValue = resolveDotNotation(condition.trim(), currentCtx);
        boolean isTrue = isTruthy(condValue);
        log("If condition evaluated to: " + isTrue);

        if (isTrue && body != null) {
            log("Taking true branch of if-statement");
            lastResult = renderJinjaBody(body, currentCtx);
        } else if (!isTrue && elifClauses != null && !elifClauses.isEmpty()) {
            lastResult = evaluateElifChain(elifClauses, currentCtx);
        } else if (!isTrue && elseBody != null && !elseBody.isEmpty()) {
            log("Taking else branch of if-statement");
            lastResult = renderJinjaBody(elseBody, currentCtx);
        } else {
            lastResult = "";
        }
    }

    private String evaluateElifChain(List<Jinja2IfNode.Jinja2ElifClause> elifClauses, Context ctx) {
        for (int i = 0; i < elifClauses.size(); i++) {
            Jinja2IfNode.Jinja2ElifClause clause = elifClauses.get(i);
            String elifCond = clause.getCondition();
            List<Jinja2Node> elifBody = clause.getBody();
            log("Evaluating elif condition " + (i + 1) + ": " + elifCond);
            if (elifCond != null) {
                Object condVal = resolveDotNotation(elifCond.trim(), ctx);
                if (isTruthy(condVal)) {
                    log("elif condition true, rendering branch");
                    if (elifBody != null) return renderJinjaBody(elifBody, ctx);
                }
            }
        }
        log("No elif branch matched");
        return "";
    }

    @Override
    public void visit(Jinja2ForNode node) {
        List<String> loopVars = node.getLoopVariables();
        String iterableExpr = node.getIterable();
        List<Jinja2Node> body = node.getBody();
        log("Entering Jinja for-loop: vars=" + loopVars + ", iterable=" + iterableExpr);
        if (loopVars == null || loopVars.isEmpty() || iterableExpr == null || body == null || body.isEmpty()) {
            log("Incomplete for-loop node, skipping");
            lastResult = "";
            return;
        }

        Object iterableObj = resolveDotNotation(iterableExpr.trim(), currentCtx);
        if (iterableObj == null) {
            log("For-loop iterable '" + iterableExpr + "' not found in context");
            lastResult = "";
            return;
        }
        List<?> items = toList(iterableObj);
        if (items == null) {
            log("For-loop iterable is not a list, skipping");
            lastResult = "";
            return;
        }

        String loopVar = loopVars.get(0);
        log("For-loop: iterating over " + items.size() + " item(s) with variable '" + loopVar + "'");

        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < items.size(); idx++) {
            Object item = items.get(idx);
            Context childCtx = new Context(currentCtx.snapshot());
            childCtx.assign(loopVar, item);
            Context.PythonDict loopMeta = new Context.PythonDict();
            loopMeta.put("index", idx + 1);
            loopMeta.put("index0", idx);
            loopMeta.put("first", idx == 0);
            loopMeta.put("last", idx == items.size() - 1);
            loopMeta.put("length", items.size());
            childCtx.assign("loop", loopMeta);

            pushCtx(childCtx);
            indentLevel++;
            for (Jinja2Node childNode : body) {
                if (childNode != null) {
                    childNode.accept(this);
                    sb.append(lastResult);
                }
            }
            indentLevel--;
            popCtx();
        }
        log("Exited Jinja for-loop");
        lastResult = sb.toString();
    }

    @Override
    public void visit(Jinja2BlockNode node) {
        String blockName = node.getName();
        List<Jinja2Node> body = node.getBody();
        log("Entering Jinja block: " + blockName);
        lastResult = renderJinjaBody(body, currentCtx);
        log("Exited Jinja block: " + blockName);
    }

    @Override
    public void visit(Jinja2SetNode node) {
        String assignment = node.getAssignment();
        log("Processing Jinja set: " + assignment);
        if (assignment != null && assignment.contains("=")) {
            int eqIdx = assignment.indexOf('=');
            String varName = assignment.substring(0, eqIdx).trim();
            String valueExpr = assignment.substring(eqIdx + 1).trim();
            Object value = resolveDotNotation(valueExpr, currentCtx);
            currentCtx.assign(varName, value);
            log("Set variable '" + varName + "' = " + objectToString(value));
        }
        lastResult = "";
    }

    @Override
    public void visit(Jinja2ExtendsNode node) {
        log("Jinja extends directive (skipped in static generation)");
        lastResult = "";
    }

    @Override
    public void visit(Jinja2IncludeNode node) {
        log("Jinja include directive (skipped in static generation)");
        lastResult = "";
    }

    @Override
    public void visit(Jinja2WithNode node) {
        String assignment = node.getAssignment();
        List<Jinja2Node> body = node.getBody();
        log("Entering Jinja with block: " + assignment);
        if (assignment != null && assignment.contains("=")) {
            int eqIdx = assignment.indexOf('=');
            String varName = assignment.substring(0, eqIdx).trim();
            String valueExpr = assignment.substring(eqIdx + 1).trim();
            Object value = resolveDotNotation(valueExpr, currentCtx);
            Context childCtx = new Context(currentCtx.snapshot());
            childCtx.assign(varName, value);
            pushCtx(childCtx);
            lastResult = renderJinjaBody(body, currentCtx);
            popCtx();
        } else {
            lastResult = renderJinjaBody(body, currentCtx);
        }
        log("Exited Jinja with block");
    }

    // ─── Rendering Helpers ────────────────────────────────────

    private String renderJinjaBody(List<Jinja2Node> body, Context ctx) {
        StringBuilder sb = new StringBuilder();
        if (body != null) {
            for (Jinja2Node childNode : body) {
                if (childNode != null) {
                    childNode.accept(this);
                    sb.append(lastResult);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Resolve an ASTNode expression to a value using the current context.
     * Supports IdentifierNode, StringNode, NumberNode, BooleanNode,
     * AttributeAccessNode, IndexAccessNode, and FunctionCallNode.
     */
    private Object resolveFromNode(ASTNode exprNode) {
        if (exprNode == null) return null;

        if (exprNode instanceof IdentifierNode) {
            String name = ((IdentifierNode) exprNode).getName();
            return resolveDotNotation(name, currentCtx);
        }
        if (exprNode instanceof StringNode) {
            return ((StringNode) exprNode).getValue();
        }
        if (exprNode instanceof NumberNode) {
            String val = ((NumberNode) exprNode).getValue();
            if (val.contains(".")) {
                try { return Double.parseDouble(val); } catch (NumberFormatException e) { return val; }
            } else {
                try { return Long.parseLong(val); } catch (NumberFormatException e) { return val; }
            }
        }
        if (exprNode instanceof BooleanNode) {
            return ((BooleanNode) exprNode).getValue();
        }
        if (exprNode instanceof AttributeAccessNode) {
            AttributeAccessNode attrNode = (AttributeAccessNode) exprNode;
            Object target = resolveFromNode(attrNode.object);
            String attrName = attrNode.attribute != null ? attrNode.attribute.generateCode() : "";
            if (target instanceof Map && !attrName.isEmpty()) {
                return ((Map<?, ?>) target).get(attrName);
            }
            return null;
        }
        if (exprNode instanceof IndexAccessNode) {
            IndexAccessNode idxNode = (IndexAccessNode) exprNode;
            Object target = resolveFromNode(idxNode.object);
            Object index = resolveFromNode(idxNode.index);
            if (target instanceof Map && index != null) {
                return ((Map<?, ?>) target).get(index.toString());
            }
            if (target instanceof List && index instanceof Number) {
                try {
                    return ((List<?>) target).get(((Number) index).intValue());
                } catch (IndexOutOfBoundsException e) {
                    return null;
                }
            }
            return null;
        }
        if (exprNode instanceof FunctionCallNode) {
            FunctionCallNode call = (FunctionCallNode) exprNode;
            String funcName = call.getFunctionName();
            if ("str".equals(funcName)) {
                List<ASTNode> args = call.getArguments();
                if (args != null && !args.isEmpty()) {
                    return objectToString(resolveFromNode(args.get(0)));
                }
            }
            if ("len".equals(funcName)) {
                List<ASTNode> args = call.getArguments();
                if (args != null && !args.isEmpty()) {
                    Object val = resolveFromNode(args.get(0));
                    if (val instanceof String) return String.valueOf(((String) val).length());
                    if (val instanceof Collection) return String.valueOf(((Collection<?>) val).size());
                }
            }
            if ("list".equals(funcName)) {
                List<ASTNode> args = call.getArguments();
                if (args != null && !args.isEmpty()) {
                    return resolveFromNode(args.get(0));
                }
            }
            return "";
        }
        // Fallback: use generateCode()
        String code = exprNode.generateCode();
        if (code != null && !code.isEmpty()) {
            return resolveDotNotation(code, currentCtx);
        }
        return null;
    }

    // ─── Utility Methods ──────────────────────────────────────

    private boolean isTruthy(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof String) return !((String) value).isEmpty();
        if (value instanceof Number) return ((Number) value).doubleValue() != 0;
        if (value instanceof Collection) return !((Collection<?>) value).isEmpty();
        if (value instanceof Map) return !((Map<?, ?>) value).isEmpty();
        return true;
    }

    @SuppressWarnings("unchecked")
    private List<Object> toList(Object obj) {
        if (obj instanceof List) return (List<Object>) obj;
        if (obj instanceof Object[]) return Arrays.asList((Object[]) obj);
        if (obj instanceof Iterable) {
            List<Object> result = new ArrayList<>();
            for (Object item : (Iterable<?>) obj) {
                result.add(item);
            }
            return result;
        }
        return null;
    }

    private String objectToString(Object obj) {
        if (obj == null) return "";
        if (obj instanceof String) return (String) obj;
        if (obj instanceof Number || obj instanceof Boolean) return obj.toString();
        if (obj instanceof Collection) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            boolean first = true;
            for (Object item : (Collection<?>) obj) {
                if (!first) sb.append(", ");
                sb.append(objectToString(item));
                first = false;
            }
            sb.append("]");
            return sb.toString();
        }
        if (obj instanceof Map) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                if (!first) sb.append(", ");
                sb.append(objectToString(entry.getKey())).append(": ").append(objectToString(entry.getValue()));
                first = false;
            }
            sb.append("}");
            return sb.toString();
        }
        return obj.toString();
    }

    private String htmlEscape(String value) {
        if (value == null) return "";
        StringBuilder escaped = new StringBuilder(value.length() + 16);
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '&':  escaped.append("&amp;");  break;
                case '<':  escaped.append("&lt;");   break;
                case '>':  escaped.append("&gt;");   break;
                case '"':  escaped.append("&quot;"); break;
                case '\'': escaped.append("&#39;");  break;
                default:   escaped.append(c);        break;
            }
        }
        return escaped.toString();
    }

    private String indent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            sb.append("    ");
        }
        return sb.toString();
    }

    private void log(String message) {
        int col = indentLevel * 4 + 1;
        String entry = "[" + String.format("%" + col + "s", "") + "] " + message;
        logEntries.add(entry);
    }

    private void writeLog() {
        try {
            Path logDir = Paths.get(LOG_DIR);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            Path logPath = Paths.get(LOG_FILE);
            StringBuilder logContent = new StringBuilder();
            logContent.append("=== HTML Code Generation Log (Visitor Pattern) ===\n");
            logContent.append("Generated at: ").append(new Date()).append("\n");
            logContent.append("Context size: ").append(rootContext.size()).append("\n");
            logContent.append("Max indentation reached: ").append(indentLevel).append("\n");
            logContent.append("\n--- Generation Steps ---\n");
            for (String entry : logEntries) {
                logContent.append(entry).append('\n');
            }
            Files.writeString(logPath, logContent.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to write generation log: " + e.getMessage());
        }
    }
}
