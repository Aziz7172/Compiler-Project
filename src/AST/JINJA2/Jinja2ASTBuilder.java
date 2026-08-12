package AST.JINJA2;

import AST.ASTNode;
import gen.ANTLR.Jinja2Lexer;
import gen.ANTLR.Jinja2Parser;
import ErrorHandling.CompilerErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class Jinja2ASTBuilder {

    private CompilerErrorListener syntaxListener;

    private static void applyLine(ASTNode node, ParserRuleContext context) {
        if (node != null && context != null && context.getStart() != null) {
            node.setLineNumber(context.getStart().getLine());
        }
    }

    public Jinja2Node build(String name, String source) {
        CharStream input = CharStreams.fromString(source);
        Jinja2Lexer lexer = new Jinja2Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Jinja2Parser parser = new Jinja2Parser(tokens);
        parser.removeErrorListeners();
        syntaxListener = new CompilerErrorListener(name);
        parser.addErrorListener(syntaxListener);

        Jinja2Parser.TemplateContext tree = parser.template();
        if (syntaxListener.hasErrors()) {
            System.out.println("  [Jinja2] " + syntaxListener.getErrors().size() + " syntax error(s) in " + name);
        }
        Jinja2TemplateNode template = new Jinja2TemplateNode(name, source);
        applyLine(template, tree);
        for (Jinja2Node child : buildChildren(tree)) {
            template.addChild(child);
        }
        return template;
    }

    public CompilerErrorListener getSyntaxListener() {
        return syntaxListener;
    }

    private List<Jinja2Node> buildChildren(RuleContext context) {
        List<Jinja2Node> nodes = new ArrayList<>();
        for (int index = 0; index < context.getChildCount(); index++) {
            ParseTree child = context.getChild(index);
            Jinja2Node node = buildNode(child);
            if (node != null) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    private Jinja2Node buildNode(ParseTree tree) {
        if (tree instanceof TerminalNode) {
            return null;
        }
        if (tree instanceof Jinja2Parser.TextChunkContext) {
            Jinja2Parser.TextChunkContext tc = (Jinja2Parser.TextChunkContext) tree;
            String text = tc.TEXT().getText();
            Jinja2TextNode node = new Jinja2TextNode(text);
            applyLine(node, tc);
            return node;
        }
        if (tree instanceof Jinja2Parser.ExpressionContext) {
            Jinja2Parser.ExpressionContext ec = (Jinja2Parser.ExpressionContext) tree;
            Jinja2ExprNode node = new Jinja2ExprNode(ec.expr().getText());
            applyLine(node, ec);
            return node;
        }
        if (tree instanceof Jinja2Parser.CommentContext) {
            String text = "";
            Jinja2Parser.CommentContext comment = (Jinja2Parser.CommentContext) tree;
            if (comment instanceof Jinja2Parser.BodyCommentContext) {
                text = ((Jinja2Parser.BodyCommentContext) comment).commentBody().getText();
            }
            Jinja2CommentNode node = new Jinja2CommentNode(text);
            applyLine(node, comment);
            return node;
        }
        if (tree instanceof Jinja2Parser.StatementContext) {
            return buildStatement(((Jinja2Parser.StatementContext) tree).stmtContent());
        }
        if (tree instanceof Jinja2Parser.BodyContentContext) {
            return buildNodeFromBodyContent((Jinja2Parser.BodyContentContext) tree);
        }
        return null;
    }

    private Jinja2Node buildNodeFromBodyContent(Jinja2Parser.BodyContentContext context) {
        if (context instanceof Jinja2Parser.TextContentContext) return buildNode(((Jinja2Parser.TextContentContext) context).textChunk());
        if (context instanceof Jinja2Parser.ExprContentContext) return buildNode(((Jinja2Parser.ExprContentContext) context).expression());
        if (context instanceof Jinja2Parser.CommentContentContext) return buildNode(((Jinja2Parser.CommentContentContext) context).comment());

        for (int index = 0; index < context.getChildCount(); index++) {
            ParseTree child = context.getChild(index);
            if (child instanceof Jinja2Parser.IfStmtContext) return buildIf((Jinja2Parser.IfStmtContext) child);
            if (child instanceof Jinja2Parser.ForStmtContext) return buildFor((Jinja2Parser.ForStmtContext) child);
            if (child instanceof Jinja2Parser.SetStmtContext) return buildSet((Jinja2Parser.SetStmtContext) child);
            if (child instanceof Jinja2Parser.BlockStmtContext) return buildBlock((Jinja2Parser.BlockStmtContext) child);
            if (child instanceof Jinja2Parser.ExtendsStmtContext) return buildExtends((Jinja2Parser.ExtendsStmtContext) child);
            if (child instanceof Jinja2Parser.IncludeStmtContext) return buildInclude((Jinja2Parser.IncludeStmtContext) child);
            if (child instanceof Jinja2Parser.WithStmtContext) return buildWith((Jinja2Parser.WithStmtContext) child);
        }
        return null;
    }

    private Jinja2Node buildStatement(Jinja2Parser.StmtContentContext context) {
        if (context instanceof Jinja2Parser.IfContentContext) return buildIf(((Jinja2Parser.IfContentContext) context).ifStmt());
        if (context instanceof Jinja2Parser.ForContentContext) return buildFor(((Jinja2Parser.ForContentContext) context).forStmt());
        if (context instanceof Jinja2Parser.SetContentContext) return buildSet(((Jinja2Parser.SetContentContext) context).setStmt());
        if (context instanceof Jinja2Parser.BlockContentContext) return buildBlock(((Jinja2Parser.BlockContentContext) context).blockStmt());
        if (context instanceof Jinja2Parser.ExtendsContentContext) return buildExtends(((Jinja2Parser.ExtendsContentContext) context).extendsStmt());
        if (context instanceof Jinja2Parser.IncludeContentContext) return buildInclude(((Jinja2Parser.IncludeContentContext) context).includeStmt());
        if (context instanceof Jinja2Parser.WithContentContext) return buildWith(((Jinja2Parser.WithContentContext) context).withStmt());
        return null;
    }

    private Jinja2IfNode buildIf(Jinja2Parser.IfStmtContext context) {
        String condition = extractExpressionText(context);
        List<Jinja2Node> body = buildChildren(context);

        List<Jinja2IfNode.Jinja2ElifClause> elifClauses = new ArrayList<>();
        for (Jinja2Parser.ElifClauseContext elifContext : context.elifClause()) {
            String elifCondition = extractExpressionText(elifContext);
            List<Jinja2Node> elifBody = buildChildren(elifContext);
            elifClauses.add(new Jinja2IfNode.Jinja2ElifClause(elifCondition, elifBody));
        }

        List<Jinja2Node> elseBody = new ArrayList<>();
        if (context.elseClause() != null) {
            elseBody = buildChildren(context.elseClause());
        }

        Jinja2IfNode node = new Jinja2IfNode(condition, body, elifClauses, elseBody);
        applyLine(node, context);
        return node;
    }

    private Jinja2ForNode buildFor(Jinja2Parser.ForStmtContext context) {
        List<String> loopVariables = new ArrayList<>();
        String iterable = "";

        // The forInit rule grammar has IN_KW inside compOp, so expr() consumes
        // the 'in' keyword as a comparison operator, breaking forInit parsing.
        // Workaround: parse the raw forInit text with regex.
        // The forInit getText() returns something like "productinproducts" or "iin5".
        // We need to find the 'in' keyword boundary.
        if (context.forInit() != null) {
            String initText = context.forInit().getText();
            // Match: variableName in iterableName (or comma-separated vars)
            // The 'in' keyword appears as literal 'in' in the concatenated text.
            // Find the first occurrence of 'in' that separates variable from iterable.
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^([a-zA-Z_][a-zA-Z0-9_]*(?:,[a-zA-Z_][a-zA-Z0-9_]*)*)in(.+)$");
            java.util.regex.Matcher matcher = pattern.matcher(initText);
            if (matcher.matches()) {
                String varPart = matcher.group(1);
                iterable = matcher.group(2);
                for (String v : varPart.split(",")) {
                    loopVariables.add(v.trim());
                }
            } else {
                // Fallback: use the original broken approach
                List<Jinja2Parser.ExprContext> exprs = context.forInit().expr();
                if (!exprs.isEmpty()) {
                    String varText = exprs.get(0).getText();
                    for (String v : varText.split(",")) {
                        loopVariables.add(v.trim());
                    }
                }
                if (context.forInit().IN_KW() != null && exprs.size() > 1) {
                    iterable = exprs.get(1).getText();
                }
            }
        }
        List<Jinja2Node> body = buildChildren(context);
        Jinja2ForNode node = new Jinja2ForNode(loopVariables, iterable, body);
        applyLine(node, context);
        return node;
    }

    private Jinja2SetNode buildSet(Jinja2Parser.SetStmtContext context) {
        Jinja2SetNode node = new Jinja2SetNode(extractExpressionText(context));
        applyLine(node, context);
        return node;
    }

    private Jinja2BlockNode buildBlock(Jinja2Parser.BlockStmtContext context) {
        String nameExpression = extractExpressionText(context);
        List<Jinja2Node> body = buildChildren(context);
        Jinja2BlockNode node = new Jinja2BlockNode(nameExpression, body);
        applyLine(node, context);
        return node;
    }

    private Jinja2ExtendsNode buildExtends(Jinja2Parser.ExtendsStmtContext context) {
        Jinja2ExtendsNode node = new Jinja2ExtendsNode(extractExpressionText(context));
        applyLine(node, context);
        return node;
    }

    private Jinja2IncludeNode buildInclude(Jinja2Parser.IncludeStmtContext context) {
        Jinja2IncludeNode node = new Jinja2IncludeNode(extractExpressionText(context), false, false);
        applyLine(node, context);
        return node;
    }

    private Jinja2WithNode buildWith(Jinja2Parser.WithStmtContext context) {
        String assignment = extractExpressionText(context);
        List<Jinja2Node> body = buildChildren(context);
        Jinja2WithNode node = new Jinja2WithNode(assignment, body);
        applyLine(node, context);
        return node;
    }

    private String extractExpressionText(ParserRuleContext context) {
        if (context == null || context.getChildCount() == 0) return "";

        for (int index = 0; index < context.getChildCount(); index++) {
            ParseTree child = context.getChild(index);
            if (child instanceof Jinja2Parser.ExprContext) {
                return child.getText();
            }
            if (child instanceof TerminalNode) {
                continue;
            }
            if (child.getText() != null && !child.getText().isEmpty()) {
                return child.getText();
            }
        }
        return context.getText();
    }
}
