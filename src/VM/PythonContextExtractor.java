package VM;

import AST.*;
import AST.HTML.*;
import AST.CSS.*;
import AST.JINJA2.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Extracts ALL global variable assignments from Python source into a Context.
 * The Context acts as a global pool of data - like a static site generator
 * (Jekyll, Hugo) that reads data files and applies them to all templates.
 */
public class PythonContextExtractor implements ASTVisitor {
    private final Context context;
    private final List<String> warnings;

    public PythonContextExtractor() {
        this.context = new Context();
        this.warnings = new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    /**
     * Entry point: walk the top-level ProgramNode and extract all global
     * variable assignments into the Context.
     */
    public void extract(ProgramNode program) {
        if (program == null) return;
        visit(program);
    }

    @Override
    public void visit(ProgramNode node) {
        if (node == null) return;
        for (ASTNode stmt : node.getStatements()) {
            if (stmt != null) {
                stmt.accept(this);
            }
        }
    }

    @Override
    public void visit(FunctionNode node) {
        // Do NOT enter function bodies - we only extract globals.
        // Function-local assignments are irrelevant for template rendering.
    }

    @Override
    public void visit(AssignmentNode node) {
        if (node != null && node.value != null) {
            String name = node.getName();
            Object value = evaluate(node.value);
            context.assign(name, value);
        }
    }

    @Override
    public void visit(FunctionCallNode node) {
        // Ignore all function calls - no render_template extraction.
        // This is a static site generator; we don't trace render calls.
    }

    @Override
    public void visit(ListNode node) {
        // handled by evaluate()
    }

    @Override
    public void visit(TupleNode node) {
        // handled by evaluate()
    }

    @Override
    public void visit(JSONNode node) {
        // handled by evaluate()
    }

    @Override
    public void visit(IfNode node) {
        // Ignore control flow at global level
    }

    @Override
    public void visit(ForNode node) {
        // Ignore control flow at global level
    }

    @Override
    public void visit(WhileNode node) {
        // Ignore control flow at global level
    }

    @Override
    public void visit(ReturnNode node) {
        // Ignore returns at global level (semantic error, not our concern here)
    }

    // ─── All other ASTVisitor methods are no-ops ──────────────

    @Override public void visit(BinaryOpNode node) {}
    @Override public void visit(UnaryNode node) {}
    @Override public void visit(IdentifierNode node) {}
    @Override public void visit(StringNode node) {}
    @Override public void visit(NumberNode node) {}
    @Override public void visit(BooleanNode node) {}
    @Override public void visit(AttributeAccessNode node) {}
    @Override public void visit(IndexAccessNode node) {}
    @Override public void visit(FromImportNode node) {}
    @Override public void visit(ParameterNode node) {}
    @Override public void visit(ArgumentNode node) {}
    @Override public void visit(DecoratorNode node) {}
    @Override public void visit(GlobalNode node) {}
    @Override public void visit(PrintNode node) {}
    @Override public void visit(ListComprehensionNode node) {}
    @Override public void visit(ImportedNode node) {}
    @Override public void visit(ImportedListNode node) {}
    @Override public void visit(ElifNode node) {}
    @Override public void visit(ElseNode node) {}
    @Override public void visit(HtmlElementNode node) {}
    @Override public void visit(HtmlTextNode node) {}
    @Override public void visit(CssNode node) {}
    @Override public void visit(CssBlockNode node) {}
    @Override public void visit(CssPropertyNode node) {}
    @Override public void visit(ExtendNode node) {}
    @Override public void visit(IncludeNode node) {}
    @Override public void visit(Jinja2TemplateNode node) {}
    @Override public void visit(Jinja2TextNode node) {}
    @Override public void visit(Jinja2ExprNode node) {}
    @Override public void visit(Jinja2CommentNode node) {}
    @Override public void visit(Jinja2IfNode node) {}
    @Override public void visit(Jinja2ForNode node) {}
    @Override public void visit(Jinja2BlockNode node) {}
    @Override public void visit(Jinja2SetNode node) {}
    @Override public void visit(Jinja2ExtendsNode node) {}
    @Override public void visit(Jinja2IncludeNode node) {}
    @Override public void visit(Jinja2WithNode node) {}
    @Override public void visit(JinjaExpressionNode node) {}
    @Override public void visit(ClassNode node) {}

    // ─── Value Evaluation ──────────────────────────────────────

    /**
     * Evaluate an ASTNode to a concrete Java object that can be stored
     * in the Context and later used by template rendering.
     */
    private Object evaluate(ASTNode node) {
        if (node == null) return null;

        if (node instanceof StringNode) {
            return ((StringNode) node).getValue();
        }

        if (node instanceof NumberNode) {
            String val = ((NumberNode) node).getValue();
            if (val.contains(".")) {
                try { return Double.parseDouble(val); }
                catch (NumberFormatException e) { return val; }
            } else {
                try { return Long.parseLong(val); }
                catch (NumberFormatException e) { return val; }
            }
        }

        if (node instanceof BooleanNode) {
            String code = node.generateCode();
            return "True".equals(code);
        }

        if (node instanceof IdentifierNode) {
            String name = ((IdentifierNode) node).getName();
            return context.lookup(name);
        }

        if (node instanceof ListNode) {
            Context.PythonList list = new Context.PythonList();
            for (ASTNode elem : ((ListNode) node).getElements()) {
                list.add(evaluate(elem));
            }
            return list;
        }

        if (node instanceof TupleNode) {
            Context.PythonList tuple = new Context.PythonList();
            for (ASTNode elem : ((TupleNode) node).getElements()) {
                tuple.add(evaluate(elem));
            }
            return tuple;
        }

        if (node instanceof JSONNode) {
            Context.PythonDict dict = new Context.PythonDict();
            Map<String, ASTNode> data = ((JSONNode) node).getData();
            if (data != null) {
                for (Map.Entry<String, ASTNode> entry : data.entrySet()) {
                    dict.put(entry.getKey(), evaluate(entry.getValue()));
                }
            }
            return dict;
        }

        if (node instanceof FunctionCallNode) {
            FunctionCallNode call = (FunctionCallNode) node;
            // Evaluate arguments but don't call the function
            if (call.getArguments() != null) {
                for (ASTNode arg : call.getArguments()) {
                    if (arg != null) evaluate(arg);
                }
            }
            return null;
        }

        if (node instanceof ArgumentNode) {
            return evaluate(((ArgumentNode) node).getValue());
        }

        if (node instanceof AssignmentNode) {
            Object value = evaluate(((AssignmentNode) node).value);
            context.assign(((AssignmentNode) node).getName(), value);
            return value;
        }

        if (node instanceof ReturnNode) {
            return evaluate(((ReturnNode) node).getValue());
        }

        if (node instanceof AttributeAccessNode) {
            return evaluate(((AttributeAccessNode) node).object);
        }

        if (node instanceof IndexAccessNode) {
            return null;
        }

        if (node instanceof BinaryOpNode) {
            Object left = evaluate(((BinaryOpNode) node).left);
            Object right = evaluate(((BinaryOpNode) node).right);
            return evaluateBinaryOp(((BinaryOpNode) node).operator, left, right);
        }

        if (node instanceof UnaryNode) {
            return evaluate(((UnaryNode) node).expression);
        }

        if (node instanceof PrintNode) {
            List<ASTNode> printNodes = ((PrintNode) node).getNodes();
            if (printNodes != null) {
                for (ASTNode n : printNodes) {
                    if (n != null) evaluate(n);
                }
            }
            return null;
        }

        return null;
    }

    private Object evaluateBinaryOp(String operator, Object left, Object right) {
        if (left == null || right == null) return null;

        if (left instanceof Number && right instanceof Number) {
            double l = ((Number) left).doubleValue();
            double r = ((Number) right).doubleValue();
            switch (operator) {
                case "+": return l + r;
                case "-": return l - r;
                case "*": return l * r;
                case "/": return r != 0 ? l / r : null;
                case "%": return r != 0 ? l % r : null;
                case "==": return l == r;
                case "!=": return l != r;
                case "<":  return l < r;
                case "<=": return l <= r;
                case ">":  return l > r;
                case ">=": return l >= r;
                default:   return null;
            }
        }

        if ("+".equals(operator)) {
            return left.toString() + right.toString();
        }
        if ("==".equals(operator)) return left.equals(right);
        if ("!=".equals(operator)) return !left.equals(right);

        return null;
    }
}
