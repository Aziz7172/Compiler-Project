package AST;

import AST.CSS.CssBlockNode;
import AST.CSS.CssNode;
import AST.CSS.CssPropertyNode;
import AST.HTML.HtmlElementNode;
import AST.HTML.HtmlTextNode;
import AST.JINJA2.*;
import SymbolTable.Symbol;
import SymbolTable.SymbolTable;
import gen.ANTLR.PythonParser;
import gen.ANTLR.PythonParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ASTBuilder extends PythonParserBaseVisitor<ASTNode> {
    public SymbolTable table = new SymbolTable();

    @Override
    public ASTNode visitProgram(PythonParser.ProgramContext ctx) {
        ProgramNode program = new ProgramNode(ctx.start.getLine(), ctx.start.getCharPositionInLine() + 1);
        table.enterScope("global");

        if (ctx.statementBlock() != null) {
            for (PythonParser.StatementContext statementContext : ctx.statementBlock().statement()) {
                ASTNode node = visit(statementContext);
                if (node != null) {
                    program.addStatement(node);
                }
            }
        }

        table.exitScope();
        return program;
    }

    @Override
    public ASTNode visitAssignment(PythonParser.AssignmentContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        ASTNode value = visit(ctx.value());
        Object actualValue = getActualValue(value);

        if (actualValue != null) {
            String typeName = actualValue.getClass().getSimpleName();
            table.define(new Symbol(name, Symbol.SymbolType.VARIABLE, actualValue, typeName, ctx.start.getLine()));
        } else {
            String typeName = value != null ? value.getClass().getSimpleName() : null;
            table.define(new Symbol(name, Symbol.SymbolType.VARIABLE, typeName, null, ctx.start.getLine()));
        }

        return withLine(new AssignmentNode(name, value), ctx.start.getLine());
    }

    @Override
    public ASTNode visitAtomVal(PythonParser.AtomValContext ctx) {
        return visit(ctx.atom());
    }

    @Override
    public ASTNode visitExprVal(PythonParser.ExprValContext ctx) {
        return visit(ctx.expressions());
    }

    @Override
    public ASTNode visitListVal(PythonParser.ListValContext ctx) {
        return visit(ctx.list());
    }

    @Override
    public ASTNode visitTupleVal(PythonParser.TupleValContext ctx) {
        return visit(ctx.tuple());
    }

    @Override
    public ASTNode visitJsonVal(PythonParser.JsonValContext ctx) {
        return visit(ctx.json());
    }

    @Override
    public ASTNode visitListCompVal(PythonParser.ListCompValContext ctx) {
        return visit(ctx.listComprehension());
    }

    @Override
    public ASTNode visitNumber(PythonParser.NumberContext ctx) {
        return withLine(new NumberNode(ctx.NUMBER().getText()), ctx.start.getLine());
    }

    @Override
    public ASTNode visitId(PythonParser.IdContext ctx) {
        return withLine(new IdentifierNode(ctx.IDENTIFIER().getText()), ctx.start.getLine());
    }

    @Override
    public ASTNode visitTrue(PythonParser.TrueContext ctx) {
        return withLine(new BooleanNode(true), ctx.start.getLine());
    }

    @Override
    public ASTNode visitFalse(PythonParser.FalseContext ctx) {
        return withLine(new BooleanNode(false), ctx.start.getLine());
    }

    @Override
    public ASTNode visitString(PythonParser.StringContext ctx) {
        return withLine(new StringNode(ctx.STRING().getText()), ctx.start.getLine());
    }

    @Override
    public ASTNode visitAtomWithAccess(PythonParser.AtomWithAccessContext ctx) {
        ASTNode node = visit(ctx.primaryAtom());

        for (PythonParser.PostfixContext postfix : ctx.postfix()) {
            if (postfix instanceof PythonParser.DotAccessContext) {
                PythonParser.DotAccessContext dotAccess = (PythonParser.DotAccessContext) postfix;
                if (dotAccess.IDENTIFIER() != null) {
                    ASTNode attribute = withLine(new IdentifierNode(dotAccess.IDENTIFIER().getText()), ctx.start.getLine());
                    node = withLine(new AttributeAccessNode(node, attribute), ctx.start.getLine());
                } else if (dotAccess.functionCall() != null) {
                    ASTNode call = visit(dotAccess.functionCall());
                    node = withLine(new AttributeAccessNode(node, call), ctx.start.getLine());
                }
            } else if (postfix instanceof PythonParser.IndexAccessContext) {
                PythonParser.IndexAccessContext indexAccess = (PythonParser.IndexAccessContext) postfix;
                ASTNode index = indexAccess.expressions() != null ? visit(indexAccess.expressions()) : visit(indexAccess.atom());
                node = withLine(new IndexAccessNode(node, index), ctx.start.getLine());
            } else if (postfix instanceof PythonParser.FuncCallPostfixContext) {
                PythonParser.FuncCallPostfixContext callPostfix = (PythonParser.FuncCallPostfixContext) postfix;
                List<ASTNode> arguments = new ArrayList<>();
                for (PythonParser.ArgumentContext argument : callPostfix.argument()) {
                    arguments.add(visit(argument));
                }
                node = withLine(new FunctionCallNode(node, arguments), ctx.start.getLine());
            }
        }

        return node;
    }

    @Override
    public ASTNode visitGlobalStatement(PythonParser.GlobalStatementContext ctx) {
        List<IdentifierNode> variables = new ArrayList<>();
        for (ParseTree identifier : ctx.IDENTIFIER()) {
            variables.add((IdentifierNode) withLine(new IdentifierNode(identifier.getText()), ctx.start.getLine()));
        }
        return new GlobalNode(variables);
    }

    private ASTNode buildBinary(ASTNode left, ASTNode right, String operator, int line) {
        BinaryOpNode node = new BinaryOpNode(operator, left, right);
        node.setLineNumber(line);
        return node;
    }

    private ASTNode buildUnary(ASTNode expression, String operator, int line) {
        UnaryNode node = new UnaryNode(operator, expression);
        node.setLineNumber(line);
        return node;
    }

    public static Object getActualValue(ASTNode node) {
        if (node == null) return null;

        if (node instanceof NumberNode) {
            String text = ((NumberNode) node).getValue();
            return text.contains(".") ? Double.parseDouble(text) : Integer.parseInt(text);
        }
        if (node instanceof StringNode) {
            return ((StringNode) node).getValue();
        }
        if (node instanceof BooleanNode) {
            return ((BooleanNode) node).getValue();
        }
        if (node instanceof ListNode) {
            List<Object> values = new ArrayList<>();
            for (ASTNode element : ((ListNode) node).getElements()) {
                values.add(getActualValue(element));
            }
            return values;
        }
        return null;
    }

    private List<ASTNode> collectBodyElements(PythonParser.HtmlBodyContext ctx) {
        List<ASTNode> elements = new ArrayList<>();
        if (ctx == null) return elements;

        if (ctx.htmlElement() != null) {
            for (PythonParser.HtmlElementContext childCtx : ctx.htmlElement()) {
                ASTNode node = visit(childCtx);
                if (node != null) {
                    elements.add(node);
                }
            }
        }

        if (ctx.htmlText() != null) {
            for (PythonParser.HtmlTextContext childCtx : ctx.htmlText()) {
                ASTNode node = visit(childCtx);
                if (node != null) {
                    elements.add(node);
                }
            }
        }
        return elements;
    }

    private Map.Entry<String, ASTNode> getJsonData(PythonParser.JsonDataContext ctx) {
        String key = ctx.STRING().getText().replace("\"", "");
        ASTNode value = visit(ctx.value());
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    private String cleanString(String value) {
        if ((value.startsWith("\"") && value.endsWith("\"")) ||
                value.startsWith("\'") && value.endsWith("\'")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private ASTNode withLine(ASTNode node, int line) {
        node.setLineNumber(line);
        return node;
    }

    // =========================
    // END Helper Functions
    // =========================

    @Override
    public ASTNode visitSimpleImport(PythonParser.SimpleImportContext ctx) {
        ASTNode name = withLine(new IdentifierNode(ctx.IDENTIFIER(0).toString()), ctx.start.getLine());
        ASTNode alias = null;
        if (ctx.IDENTIFIER(1) != null) {
            alias = visit(ctx.IDENTIFIER(1));
        }
        return withLine(new ImportedNode(name, alias), ctx.start.getLine());
    }

    @Override
    public ASTNode visitStringImport(PythonParser.StringImportContext ctx) {
        ASTNode name = withLine(new StringNode(ctx.STRING().getText()), ctx.start.getLine());
        ASTNode alias = null;
        if (ctx.IDENTIFIER() != null) {
            alias = visit(ctx.IDENTIFIER());
        }
        return withLine(new ImportedNode(name, alias), ctx.start.getLine());
    }

    @Override
    public ASTNode visitIdFromImportStmt(PythonParser.IdFromImportStmtContext ctx) {
        ASTNode name = withLine(new IdentifierNode(ctx.IDENTIFIER().toString()), ctx.start.getLine());
        ASTNode importedList = visit(ctx.importedNames());

        return withLine(new FromImportNode(name, importedList), ctx.start.getLine());
    }

    @Override
    public ASTNode visitStrFromImportStmt(PythonParser.StrFromImportStmtContext ctx) {
        ASTNode name = withLine(new StringNode(ctx.STRING().getText()), ctx.start.getLine());
        ASTNode importedList = visit(ctx.importedNames());

        return withLine(new FromImportNode(name, importedList), ctx.start.getLine());
    }

    @Override
    public ASTNode visitImportedNames(PythonParser.ImportedNamesContext ctx) {
        List<ASTNode> imports = new ArrayList<>();
        for (PythonParser.ImportsAliasesContext aliasCtx : ctx.importsAliases()) {
            imports.add(visit(aliasCtx));
        }
        return new ImportedListNode(imports);
    }

    @Override
    public ASTNode visitImportsAliases(PythonParser.ImportsAliasesContext ctx) {
        String originalName = ctx.IDENTIFIER(0).getText();
        ASTNode nameNode = withLine(new IdentifierNode(originalName), ctx.start.getLine());

        ASTNode aliasNode = null;
        String symbolName = originalName;

        if (ctx.IDENTIFIER().size() > 1) {
            String aliasName = ctx.IDENTIFIER(1).getText();
            aliasNode = withLine(new IdentifierNode(aliasName), ctx.start.getLine());
            symbolName = aliasName;
        }
        ASTNode importedNameNode = withLine(new ImportedNode(nameNode, aliasNode), ctx.start.getLine());

        Symbol symbol = new Symbol(
                symbolName,
                Symbol.SymbolType.IDENTIFIER,
                ImportedNode.class.getSimpleName(),
                "ImportedFunction",
                ctx.start.getLine()
        );
        table.define(symbol);
        return importedNameNode;
    }

    // =========================
    // Mathematical Operations
    // =========================
    @Override
    public ASTNode visitAddExp(PythonParser.AddExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "+", ctx.start.getLine());
    }

    @Override
    public ASTNode visitSubExp(PythonParser.SubExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "-", ctx.start.getLine());
    }

    @Override
    public ASTNode visitMulExp(PythonParser.MulExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "*", ctx.start.getLine());
    }

    @Override
    public ASTNode visitDivExp(PythonParser.DivExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "/", ctx.start.getLine());
    }

    @Override
    public ASTNode visitModExp(PythonParser.ModExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "%", ctx.start.getLine());
    }

    // =========================
    // Comparison Operations
    // =========================
    @Override
    public ASTNode visitLtExp(PythonParser.LtExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "<", ctx.start.getLine());
    }

    @Override
    public ASTNode visitGtExp(PythonParser.GtExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), ">", ctx.start.getLine());
    }

    @Override
    public ASTNode visitLteExp(PythonParser.LteExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "<=", ctx.start.getLine());
    }

    @Override
    public ASTNode visitGteExp(PythonParser.GteExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), ">=", ctx.start.getLine());
    }

    @Override
    public ASTNode visitEqExp(PythonParser.EqExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "==", ctx.start.getLine());
    }

    @Override
    public ASTNode visitNeExp(PythonParser.NeExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "!=", ctx.start.getLine());
    }

    @Override
    public ASTNode visitStrictEqExp(PythonParser.StrictEqExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "===", ctx.start.getLine());
    }

    @Override
    public ASTNode visitStrictNeqExp(PythonParser.StrictNeqExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "!==", ctx.start.getLine());
    }

    @Override
    public ASTNode visitIdComparison(PythonParser.IdComparisonContext ctx) {
        String operator = ctx.NOT() != null ? "is" : "is not";
        return buildBinary(visit(ctx.left), visit(ctx.right), operator, ctx.start.getLine());
    }

    @Override
    public ASTNode visitMembershipTest(PythonParser.MembershipTestContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "in", ctx.start.getLine());
    }

    // =========================
    // Logical Operations
    // =========================
    @Override
    public ASTNode visitAndExp(PythonParser.AndExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "and", ctx.start.getLine());
    }

    @Override
    public ASTNode visitOrExp(PythonParser.OrExpContext ctx) {
        return buildBinary(visit(ctx.left), visit(ctx.right), "or", ctx.start.getLine());
    }

    @Override
    public ASTNode visitNotExp(PythonParser.NotExpContext ctx) {
        return buildUnary(visit(ctx.item), "not", ctx.start.getLine());
    }

    // =========================
    // List Nodes
    // =========================
    @Override
    public ASTNode visitElements(PythonParser.ElementsContext ctx) {
        ListNode list = (ListNode) withLine(new ListNode(), ctx.start.getLine());
        for (PythonParser.ValueContext valCtx : ctx.value()) {
            list.addElement(visit(valCtx));
        }
        return list;
    }

    @Override
    public ASTNode visitList(PythonParser.ListContext ctx) {
        ListNode list = (ListNode) withLine(new ListNode(), ctx.start.getLine());
        if (ctx.elements() != null) {
            ListNode elementsList = (ListNode) visit(ctx.elements());
            for (ASTNode elem : elementsList.getElements()) {
                list.addElement(elem);
            }
        }
        return list;
    }

    @Override
    public ASTNode visitListComprehension(PythonParser.ListComprehensionContext ctx) {
        ASTNode elementExpression = visit(ctx.atom());
        ASTNode variable = withLine(new IdentifierNode(ctx.IDENTIFIER().getText()), ctx.start.getLine());
        ASTNode iterable = visit(ctx.value());
        ASTNode condition = null;
        if (ctx.expressions() != null) {
            condition = visit(ctx.expressions());
        }
        return withLine(new ListComprehensionNode(elementExpression, variable, iterable, condition), ctx.start.getLine());
    }

    @Override
    public ASTNode visitTuple(PythonParser.TupleContext ctx) {
        TupleNode tuple = (TupleNode) withLine(new TupleNode(), ctx.start.getLine());
        if (ctx.elements() != null) {
            TupleNode elementsTuple = (TupleNode) visit(ctx.elements());
            for (ASTNode elem : elementsTuple.getElements()) {
                tuple.addElement(elem);
            }
        }
        return tuple;
    }

    @Override
    public ASTNode visitJson(PythonParser.JsonContext ctx) {
        Map<String, ASTNode> data = new LinkedHashMap<>();
        for (PythonParser.JsonDataContext jData : ctx.jsonData()) {
            Map.Entry<String, ASTNode> entry = getJsonData(jData);
            data.put(entry.getKey(), entry.getValue());
        }
        return withLine(new JSONNode(data), ctx.start.getLine());
    }


    // =========================
    // Condition Nodes (if/elif/else)
    // =========================
    @Override
    public ASTNode visitBlock(PythonParser.BlockContext ctx) {
        ProgramNode block = new ProgramNode(ctx.start.getLine(), ctx.start.getCharPositionInLine() + 1);
        if (ctx.statementBlock() != null) {
            for (PythonParser.StatementContext stmtCtx : ctx.statementBlock().statement()) {
                ASTNode stmtNode = visit(stmtCtx);
                if (stmtNode != null) {
                    block.addStatement(stmtNode);
                }
            }
        }
        return block;
    }

    @Override
    public ASTNode visitIfStatement(PythonParser.IfStatementContext ctx) {
        IfNode ifNode = (IfNode) visit(ctx.ifBlock());

        if (ctx.elifBlock() != null) {
            for (PythonParser.ElifBlockContext elifCtx : ctx.elifBlock()) {
                ElifNode elifNode = (ElifNode) visit(elifCtx);
                ifNode.addElif(elifNode);
            }
        }

        if (ctx.elseBlock() != null) {
            ElseNode elseNode = (ElseNode) visit(ctx.elseBlock());
            ifNode.setElse(elseNode);
        }

        return ifNode;
    }

    @Override
    public ASTNode visitIfBlock(PythonParser.IfBlockContext ctx) {
        ASTNode condition = visit(ctx.condition);

        IfNode ifNode = (IfNode) withLine(new IfNode(condition), ctx.start.getLine());

        table.enterScope("if_" + ctx.start.getLine());
        List<ASTNode> body = ((ProgramNode) visit(ctx.block())).getStatements();
        ifNode.setBody(body);
        table.exitScope();

        return ifNode;
    }

    @Override
    public ASTNode visitElifBlock(PythonParser.ElifBlockContext ctx) {
        ASTNode condition = visit(ctx.condition);
        table.enterScope("elif_" + ctx.start.getLine());
        List<ASTNode> body = ((ProgramNode) visit(ctx.block())).getStatements();
        table.exitScope();

        return (ElifNode) withLine(new ElifNode(condition, body), ctx.start.getLine());
    }

    @Override
    public ASTNode visitElseBlock(PythonParser.ElseBlockContext ctx) {
        table.enterScope("else_" + ctx.start.getLine());
        List<ASTNode> elseBody = ((ProgramNode) visit(ctx.block())).getStatements();
        table.exitScope();

        return (ElseNode) withLine(new ElseNode(elseBody), ctx.start.getLine());
    }

    // =========================
    // Function Nodes
    // =========================
    @Override
    public ASTNode visitFunction(PythonParser.FunctionContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        List<ParameterNode> params = new ArrayList<>();
        List<DecoratorNode> decorators = new ArrayList<>();

        if (ctx.decorator_rule() != null) {
            for (PythonParser.Decorator_ruleContext dec : ctx.decorator_rule()) {
                ASTNode decoName = visit(dec);
                decorators.add((DecoratorNode) withLine(new DecoratorNode(decoName, null), ctx.start.getLine()));
                table.define(new Symbol(decoName.toString(), Symbol.SymbolType.DECORATOR, null, "Decorator", ctx.start.getLine()));
            }
        }

        if (ctx.parameters() != null) {
            for (PythonParser.ParameterContext paramCtx : ctx.parameters().parameter()) {
                String paramName = paramCtx.IDENTIFIER().getText();
                ASTNode defaultValue = null;
                if (paramCtx.value() != null)
                    defaultValue = visit(paramCtx.value());
                params.add((ParameterNode) withLine(new ParameterNode(paramName, defaultValue), ctx.start.getLine()));
            }
        }
        table.enterScope(name);

        List<ASTNode> body = ((ProgramNode) visit(ctx.block())).getStatements();

        table.exitScope();

        FunctionNode funcNode = (FunctionNode) withLine(new FunctionNode(name, params, body), ctx.start.getLine());

        for (DecoratorNode deco : decorators) {
            deco.setTarget(funcNode);
        }
        funcNode.setDecorators(decorators);
        table.define(new Symbol(name, Symbol.SymbolType.FUNCTION, null, "Function", ctx.start.getLine() + ctx.decorator_rule().size()));
        return funcNode;
    }

    @Override
    public ASTNode visitReturnStatement(PythonParser.ReturnStatementContext ctx) {
        ReturnNode returnNode = null;
        if (ctx.value() != null) {
            returnNode = (ReturnNode) withLine(new ReturnNode(visit(ctx.value())), ctx.start.getLine());
        }
        return returnNode;
    }

    @Override
    public ASTNode visitFunctionCall(PythonParser.FunctionCallContext ctx) {
        String funName = ctx.IDENTIFIER().getText();
        List<ASTNode> arguments = new ArrayList<>();
        for (PythonParser.ArgumentContext arg : ctx.argument()) {
            arguments.add(visit(arg));
        }
        return withLine(new FunctionCallNode(funName, arguments), ctx.start.getLine());
    }

    @Override
    public ASTNode visitArgument(PythonParser.ArgumentContext ctx) {
        String varName = "";
        if (ctx.IDENTIFIER() != null) {
            varName = ctx.IDENTIFIER().getText();
        }
        ASTNode value = visit(ctx.value());

        return withLine(new ArgumentNode(varName, value), ctx.start.getLine());
    }

    // =========================
    // Loop Nodes (for/while)
    // =========================
    @Override
    public ASTNode visitForLoop(PythonParser.ForLoopContext ctx) {
        String variable = ctx.IDENTIFIER().getText();
        ASTNode value = visit(ctx.value());
        table.enterScope("for_" + ctx.start.getLine());
        List<ASTNode> body = ((ProgramNode) visit(ctx.block())).getStatements();
        table.exitScope();

        return withLine(new ForNode(variable, value, body), ctx.start.getLine());
    }

    @Override
    public ASTNode visitWhileLoop(PythonParser.WhileLoopContext ctx) {
        ASTNode expression = visit(ctx.expressions());
        table.enterScope("while_" + ctx.start.getLine());
        List<ASTNode> body = ((ProgramNode) visit(ctx.block())).getStatements();
        table.exitScope();
        return withLine(new WhileNode(expression, body), ctx.start.getLine());
    }

    @Override
    public ASTNode visitDecorator(PythonParser.DecoratorContext ctx) {
        return visit(ctx.atom());
    }

    // =========================
    // Class Nodes
    // =========================

    @Override
    public ASTNode visitClassDef(PythonParser.ClassDefContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        String baseClass = "";
        if (ctx.baseClass() != null) {
            baseClass = ctx.baseClass().IDENTIFIER().getText();
        }

        List<DecoratorNode> decorators = new ArrayList<>();

        if (ctx.decorator_rule() != null) {
            for (PythonParser.Decorator_ruleContext dec : ctx.decorator_rule()) {
                ASTNode decoName = visit(dec);
                decorators.add((DecoratorNode) withLine(new DecoratorNode(decoName, null), ctx.start.getLine()));
                table.define(new Symbol(decoName.toString(), Symbol.SymbolType.DECORATOR, null, "Decorator", ctx.start.getLine()));
            }
        }

        ClassNode classNode = (ClassNode) withLine(new ClassNode(name, baseClass), ctx.start.getLine());

        for (DecoratorNode deco : decorators) {
            deco.setTarget(classNode);
        }
        classNode.setDecorators(decorators);
        table.define(new Symbol(name, Symbol.SymbolType.CLASS, null, "Class", ctx.start.getLine()));

        table.enterScope(name);

        if (ctx.block().statementBlock() != null) {
            for (PythonParser.StatementContext stmtCtx : ctx.block().statementBlock().statement()) {
            ASTNode node = visit(stmtCtx);

            if (node instanceof AssignmentNode) {
                classNode.addVariable((AssignmentNode) node);
            } else if (node instanceof FunctionNode) {
                classNode.addMethod((FunctionNode) node);
            } else if (node instanceof ClassNode) {
                classNode.addNestedClass((ClassNode) node);
            } else {
                System.err.println("Unhandled node type in class body: " + node);
            }
        }
        }

        table.exitScope();
        return classNode;
    }

    // =========================
    // Print
    // =========================
    @Override
    public ASTNode visitPrintStatement(PythonParser.PrintStatementContext ctx) {
        List<ASTNode> nodes = new ArrayList<>();
        if (ctx.printArgs() != null) {
            for (ParseTree arg : ctx.printArgs().children) {
                if (arg.getText().equals(",")) continue;
                nodes.add(visit(arg));
            }
        }
        return withLine(new PrintNode(nodes), ctx.start.getLine());
    }

    // =========================
    // JINJA2
    // =========================


    @Override
    public ASTNode visitTemplateBody(PythonParser.TemplateBodyContext ctx) {
        ProgramNode block = new ProgramNode(ctx.start.getLine(), ctx.start.getCharPositionInLine() + 1);

        // Handle statements
        for (PythonParser.StatementContext stmtCtx : ctx.statement()) {
            ASTNode stmtNode = visit(stmtCtx);
            if (stmtNode != null)
                block.addStatement(stmtNode);
        }

        // Handle HTML elements
        for (PythonParser.HtmlContext htmlCtx : ctx.html()) {
            ASTNode htmlNode = visit(htmlCtx);
            if (htmlNode != null)
                block.addStatement(htmlNode);
        }

        // Handle Jinja body elements
        for (PythonParser.JinjaBodyContext jinjaCtx : ctx.jinjaBody()) {
            ASTNode jinjaNode = visit(jinjaCtx);
            if (jinjaNode != null)
                block.addStatement(jinjaNode);
        }

        return block;
    }

    @Override
    public ASTNode visitJinjaSet(PythonParser.JinjaSetContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        ASTNode value = visit(ctx.expressions());
        Object actualValue = getActualValue(value);

        if (actualValue != null) {
            String type = actualValue.getClass().getSimpleName();
            table.define(new Symbol(varName, Symbol.SymbolType.VARIABLE, actualValue, type, ctx.start.getLine()));
        } else {
            table.define(new Symbol(varName, Symbol.SymbolType.VARIABLE, value.getClass().getSimpleName(), null, ctx.start.getLine()));
        }

        return withLine(new AssignmentNode(varName, value), ctx.start.getLine());
    }

    @Override
    public ASTNode visitJinjaExpression(PythonParser.JinjaExpressionContext ctx) {
        ASTNode innerExpression = null;

        if (ctx.expressions() != null) {
            innerExpression = visit(ctx.expressions());
        } else if (ctx.atom() != null) {
            innerExpression = visit(ctx.atom());
        } else if (ctx.value() != null) {
            innerExpression = visit(ctx.value());
        }

        return withLine(new JinjaExpressionNode(innerExpression), ctx.start.getLine());
    }

    @Override
    public ASTNode visitJinjaIfStatements(PythonParser.JinjaIfStatementsContext ctx) {
        IfNode ifNode = (IfNode) visit(ctx.jinjaIf());

        if (ctx.jinjaElif() != null) {
            for (PythonParser.JinjaElifContext elifCtx : ctx.jinjaElif()) {
                ElifNode elifNode = (ElifNode) visit(elifCtx);
                ifNode.addElif(elifNode);
            }
        }

        if (ctx.jinjaElse() != null) {
            ElseNode elseNode = (ElseNode) visit(ctx.jinjaElse());
            ifNode.setElse(elseNode);
        }

        return ifNode;
    }

    @Override
    public ASTNode visitJinjaIf(PythonParser.JinjaIfContext ctx) {
        ASTNode condition = visit(ctx.condition);
        IfNode ifNode = (IfNode) withLine(new IfNode(condition), ctx.start.getLine());

        table.enterScope("jinjaIf_" + ctx.start.getLine());
        List<ASTNode> body = ((ProgramNode) visit(ctx.templateBody())).getStatements();
        ifNode.setBody(body);
        table.exitScope();

        return ifNode;
    }

    @Override
    public ASTNode visitJinjaElif(PythonParser.JinjaElifContext ctx) {
        ASTNode condition = visit(ctx.condition);
        table.enterScope("jinjaElif_" + ctx.start.getLine());
        List<ASTNode> body = ((ProgramNode) visit(ctx.templateBody())).getStatements();
        table.exitScope();

        return (ElifNode) withLine(new ElifNode(condition, body), ctx.start.getLine());
    }

    @Override
    public ASTNode visitJinjaElse(PythonParser.JinjaElseContext ctx) {
        table.enterScope("jinjaElse_" + ctx.start.getLine());
        List<ASTNode> body = ((ProgramNode) visit(ctx.templateBody())).getStatements();
        table.exitScope();

        return (ElseNode) withLine(new ElseNode(body), ctx.start.getLine());
    }

    @Override
    public ASTNode visitJinjaFor(PythonParser.JinjaForContext ctx) {
        String variable = ctx.IDENTIFIER().getText();
        ASTNode iterable = visit(ctx.value());
        table.enterScope("jinjaFor_" + ctx.start.getLine());
        List<ASTNode> body = ((ProgramNode) visit(ctx.templateBody())).getStatements();
        table.exitScope();

        return withLine(new ForNode(variable, iterable, body), ctx.start.getLine());
    }

    @Override
    public ASTNode visitJiniaExtends(PythonParser.JiniaExtendsContext ctx) {
        ASTNode template = visit(ctx.atom());
        return withLine(new ExtendNode(template), ctx.start.getLine());
    }

    @Override
    public ASTNode visitJinjaInclude(PythonParser.JinjaIncludeContext ctx) {
        ASTNode template = visit(ctx.atom());
        boolean ignoreMissing = ctx.IGNORE() != null;
        boolean withContext = ctx.WITH() != null;
        return withLine(new IncludeNode(template, ignoreMissing, withContext), ctx.start.getLine());
    }

    // =========================
    // HTML
    // =========================


    @Override
    public ASTNode visitStyleHtmlTag(PythonParser.StyleHtmlTagContext ctx) {
        return visitStyleTag(ctx.styleTag());
    }

    @Override
    public ASTNode visitGenericHtmlTag(PythonParser.GenericHtmlTagContext ctx) {
        return visitGenericHtml(ctx.genericHtml());
    }

    @Override
    public ASTNode visitStyleTag(PythonParser.StyleTagContext ctx) {
        HtmlElementNode element = (HtmlElementNode) withLine(new HtmlElementNode(new IdentifierNode("style")), ctx.start.getLine());
        Map<String, String> attributes = new HashMap<>();
        for (PythonParser.HtmlAttributesContext attr : ctx.htmlAttributes()) {
            attributes.put(
                    attr.attributeName().getText(),
                    attr.attributeValue().getText()
            );
        }
        element.setAttributes(attributes);

        List<ASTNode> children = new ArrayList<>();
        for (PythonParser.CssContext css : ctx.css()) {
            children.add(visit(css));
        }
        element.setChildren(children);
//        element.getBehaviorTable().get(element.getTagName()).render(ctx);
        return element;
    }

    @Override
    public ASTNode visitGenericHtml(PythonParser.GenericHtmlContext ctx) {
        HtmlElementNode element = (HtmlElementNode) withLine(new HtmlElementNode(new IdentifierNode(ctx.IDENTIFIER(0).getText())), ctx.start.getLine());
        Map<String, String> attributes = new HashMap<>();
        for (PythonParser.HtmlAttributesContext attr : ctx.htmlAttributes()) {
            attributes.put(
                    attr.attributeName().getText(),
                    attr.attributeValue().getText()
            );
        }
        element.setAttributes(attributes);

        List<ASTNode> children = new ArrayList<>();
        children.addAll(collectBodyElements(ctx.htmlBody()));
        element.setChildren(children);
//        element.getBehaviorTable().get(element.getTagName()).render(ctx);
        return element;
    }

    @Override
    public ASTNode visitHtmlText(PythonParser.HtmlTextContext ctx) {
        StringBuilder text = new StringBuilder();
        if (ctx.IDENTIFIER() != null) {
            for (var id : ctx.IDENTIFIER()) {
                text.append(id.getText());
            }
        }
        if (ctx.STRING() != null) {
            for (var str : ctx.STRING()) {
                String clean = cleanString(str.getText());
                text.append(clean);
            }
        }
        if (ctx.BANG() != null) {
            for (var t : ctx.BANG()) text.append(t.getText());
        }
        if (ctx.AMPERSAND() != null) {
            for (var t : ctx.AMPERSAND()) text.append(t.getText());
        }
        if (ctx.DOLLAR() != null) {
            for (var t : ctx.DOLLAR()) text.append(t.getText());
        }
        if (ctx.HASHTAG_VALUE() != null) {
            for (var t : ctx.HASHTAG_VALUE()) text.append(t.getText());
        }
        if (ctx.HASHTAG() != null) {
            for (var t : ctx.HASHTAG()) text.append(t.getText());
        }
        if (ctx.SEMI() != null) {
            for (var t : ctx.SEMI()) text.append(t.getText());
        }
        return withLine(new HtmlTextNode(text.toString()), ctx.start.getLine());
    }

    @Override
    public ASTNode visitHtmlDoctype(PythonParser.HtmlDoctypeContext ctx) {
        return withLine(new HtmlTextNode(ctx.HTML_DOCTYPE().getText()), ctx.start.getLine());
    }

    // =========================
    // CSS
    // =========================

    @Override
    public ASTNode visitCssBlock(PythonParser.CssBlockContext ctx) {
        List<CssNode> cssNodes = new ArrayList<>();
        List<CssPropertyNode> properties = new ArrayList<>();
        for (PythonParser.CssSelectorContext selectorContext : ctx.cssSelector()) {
            cssNodes.add((CssNode) visit(selectorContext));
        }

        for (PythonParser.CssKeyValueContext cssKeyValueContext : ctx.cssKeyValue()) {
            properties.add((CssPropertyNode) visit(cssKeyValueContext));
        }

        for (CssNode node : cssNodes) {
            node.setProperties(properties);
        }
        return withLine(new CssBlockNode(cssNodes), ctx.start.getLine());
    }

    @Override
    public ASTNode visitCssAnnotation(PythonParser.CssAnnotationContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitCssSelector(PythonParser.CssSelectorContext ctx) {
        StringBuilder selectorName = new StringBuilder();

        if (ctx.DOT() != null) {
            selectorName.append(".");
        } else if (ctx.HASHTAG() != null) {
            selectorName.append("#");
        }

        selectorName.append(((IdentifierNode) visit(ctx.cssKey(0))).getName());

        for (int i = 1; i < ctx.cssKey().size(); i++) {
            selectorName.append(":")
                    .append(((IdentifierNode) visit(ctx.cssKey(i))).getName());
        }

        IdentifierNode selector = (IdentifierNode) withLine(new IdentifierNode(selectorName.toString()), ctx.start.getLine());
        return (CssNode) withLine(new CssNode(selector), ctx.start.getLine());
    }

    @Override
    public ASTNode visitCssKey(PythonParser.CssKeyContext ctx) {
        StringBuilder id = new StringBuilder();
        id.append(ctx.IDENTIFIER(0).getText());
        for (int i = 1; i < ctx.IDENTIFIER().size(); i++) {
            id.append("-").append(ctx.IDENTIFIER(1).getText());
        }
        return withLine(new IdentifierNode(id.toString()), ctx.start.getLine());
    }

    @Override
    public ASTNode visitCssKeyValue(PythonParser.CssKeyValueContext ctx) {
        ASTNode key = visit(ctx.cssKey());
        ASTNode value = visit(ctx.cssValue());
        return withLine(new CssPropertyNode(key, value), ctx.start.getLine());
    }

    @Override
    public ASTNode visitCssVNumber(PythonParser.CssVNumberContext ctx) {
        StringBuilder value = new StringBuilder();
        value.append(ctx.NUMBER().getText());
        if (ctx.TYPE() != null) {
            value.append(ctx.TYPE().getText());
        }
        return withLine(new NumberNode(value.toString()), ctx.start.getLine());
    }

    @Override
    public ASTNode visitCssVId(PythonParser.CssVIdContext ctx) {
        return withLine(new IdentifierNode(ctx.IDENTIFIER().getText()), ctx.start.getLine());
    }

    @Override
    public ASTNode visitCssVColor(PythonParser.CssVColorContext ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append('#').append(ctx.HASHTAG_VALUE().getText());
        return withLine(new IdentifierNode(sb.toString()), ctx.start.getLine());
    }

    @Override
    public ASTNode visitCssVStr(PythonParser.CssVStrContext ctx) {
        String str = ctx.STRING().getText();
        return withLine(new StringNode(cleanString(str)), ctx.start.getLine());
    }
}
