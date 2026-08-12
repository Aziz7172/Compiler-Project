// Generated from src/ANTLR/PythonParser.g4 by ANTLR 4.13.2
package gen.ANTLR;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PythonParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PythonParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PythonParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(PythonParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#statementBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementBlock(PythonParser.StatementBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleSeqStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleSeqStmt(PythonParser.SimpleSeqStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CompoundStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStmt(PythonParser.CompoundStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CssStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssStmt(PythonParser.CssStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code HtmlStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtmlStmt(PythonParser.HtmlStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaStmt(PythonParser.JinjaStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImportStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportStmt(PythonParser.ImportStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStmt(PythonParser.AssignStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrintStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintStmt(PythonParser.PrintStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReturnStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(PythonParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ValueStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueStmt(PythonParser.ValueStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GlobalStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalStmt(PythonParser.GlobalStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PassStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPassStmt(PythonParser.PassStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#passStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPassStatement(PythonParser.PassStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfCompound}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfCompound(PythonParser.IfCompoundContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForCompound}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForCompound(PythonParser.ForCompoundContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileCompound}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileCompound(PythonParser.WhileCompoundContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FuncDef}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDef(PythonParser.FuncDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ClassDefStmt}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDefStmt(PythonParser.ClassDefStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprCompound}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCompound(PythonParser.ExprCompoundContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(PythonParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AtomVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomVal(PythonParser.AtomValContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprVal(PythonParser.ExprValContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ListVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListVal(PythonParser.ListValContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TupleVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleVal(PythonParser.TupleValContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JsonVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonVal(PythonParser.JsonValContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ListCompVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListCompVal(PythonParser.ListCompValContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(PythonParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#listComprehension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListComprehension(PythonParser.ListComprehensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#tuple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTuple(PythonParser.TupleContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElements(PythonParser.ElementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#json}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson(PythonParser.JsonContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jsonData}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonData(PythonParser.JsonDataContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalExp}
	 * labeled alternative in {@link PythonParser#expressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalExp(PythonParser.LogicalExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ComparisonExp}
	 * labeled alternative in {@link PythonParser#expressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExp(PythonParser.ComparisonExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MathExp}
	 * labeled alternative in {@link PythonParser#expressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathExp(PythonParser.MathExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LtExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLtExp(PythonParser.LtExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GtExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGtExp(PythonParser.GtExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LteExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLteExp(PythonParser.LteExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GteExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGteExp(PythonParser.GteExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExp(PythonParser.EqExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StrictEqExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrictEqExp(PythonParser.StrictEqExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NeExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNeExp(PythonParser.NeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StrictNeqExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrictNeqExp(PythonParser.StrictNeqExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdComparison}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdComparison(PythonParser.IdComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MembershipTest}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembershipTest(PythonParser.MembershipTestContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ValueExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueExp(PythonParser.ValueExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulExp(PythonParser.MulExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModExp(PythonParser.ModExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExp(PythonParser.AddExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DivExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDivExp(PythonParser.DivExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SubExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubExp(PythonParser.SubExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExp}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExp(PythonParser.NotExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExp}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExp(PythonParser.AndExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CompAsLogical}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompAsLogical(PythonParser.CompAsLogicalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ValuesAsLogical}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuesAsLogical(PythonParser.ValuesAsLogicalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExp}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExp(PythonParser.OrExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#valuesExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuesExp(PythonParser.ValuesExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AtomWithAccess}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomWithAccess(PythonParser.AtomWithAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Id}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(PythonParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(PythonParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code String}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(PythonParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code True}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrue(PythonParser.TrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code False}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalse(PythonParser.FalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DotAccess}
	 * labeled alternative in {@link PythonParser#postfix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotAccess(PythonParser.DotAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IndexAccess}
	 * labeled alternative in {@link PythonParser#postfix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexAccess(PythonParser.IndexAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FuncCallPostfix}
	 * labeled alternative in {@link PythonParser#postfix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCallPostfix(PythonParser.FuncCallPostfixContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#globalStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalStatement(PythonParser.GlobalStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImportSyntaxStmt}
	 * labeled alternative in {@link PythonParser#pythonImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportSyntaxStmt(PythonParser.ImportSyntaxStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FromImportStmt}
	 * labeled alternative in {@link PythonParser#pythonImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromImportStmt(PythonParser.FromImportStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleImport}
	 * labeled alternative in {@link PythonParser#importSyntax}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleImport(PythonParser.SimpleImportContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringImport}
	 * labeled alternative in {@link PythonParser#importSyntax}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringImport(PythonParser.StringImportContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdFromImportStmt}
	 * labeled alternative in {@link PythonParser#fromImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdFromImportStmt(PythonParser.IdFromImportStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StrFromImportStmt}
	 * labeled alternative in {@link PythonParser#fromImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrFromImportStmt(PythonParser.StrFromImportStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#importedNames}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportedNames(PythonParser.ImportedNamesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#importsAliases}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportsAliases(PythonParser.ImportsAliasesContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#printStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintStatement(PythonParser.PrintStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ValuePrintArgs}
	 * labeled alternative in {@link PythonParser#printArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuePrintArgs(PythonParser.ValuePrintArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprPrintArgs}
	 * labeled alternative in {@link PythonParser#printArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprPrintArgs(PythonParser.ExprPrintArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(PythonParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(PythonParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#ifBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfBlock(PythonParser.IfBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#elifBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElifBlock(PythonParser.ElifBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#elseBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseBlock(PythonParser.ElseBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#forLoop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForLoop(PythonParser.ForLoopContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#whileLoop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileLoop(PythonParser.WhileLoopContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(PythonParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(PythonParser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(PythonParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(PythonParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(PythonParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(PythonParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Decorator}
	 * labeled alternative in {@link PythonParser#decorator_rule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecorator(PythonParser.DecoratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#classDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDef(PythonParser.ClassDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#baseClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseClass(PythonParser.BaseClassContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaExprBody}
	 * labeled alternative in {@link PythonParser#jinjaBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaExprBody(PythonParser.JinjaExprBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaStmtBody}
	 * labeled alternative in {@link PythonParser#jinjaBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaStmtBody(PythonParser.JinjaStmtBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaCommentBody}
	 * labeled alternative in {@link PythonParser#jinjaBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaCommentBody(PythonParser.JinjaCommentBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jinjaExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaExpression(PythonParser.JinjaExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jinjaFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaFilter(PythonParser.JinjaFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EmptyComment}
	 * labeled alternative in {@link PythonParser#jinjaComment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyComment(PythonParser.EmptyCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CommentWithBody}
	 * labeled alternative in {@link PythonParser#jinjaComment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentWithBody(PythonParser.CommentWithBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CcContent}
	 * labeled alternative in {@link PythonParser#commentBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCcContent(PythonParser.CcContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CcHash}
	 * labeled alternative in {@link PythonParser#commentBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCcHash(PythonParser.CcHashContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jinjaStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaStatement(PythonParser.JinjaStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaIfContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaIfContent(PythonParser.JinjaIfContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaForContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaForContent(PythonParser.JinjaForContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaSetContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaSetContent(PythonParser.JinjaSetContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaExtendsContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaExtendsContent(PythonParser.JinjaExtendsContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaIncludeContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaIncludeContent(PythonParser.JinjaIncludeContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaBlockContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaBlockContent(PythonParser.JinjaBlockContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaLocalVarContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaLocalVarContent(PythonParser.JinjaLocalVarContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaImportContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaImportContent(PythonParser.JinjaImportContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaFromImportContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaFromImportContent(PythonParser.JinjaFromImportContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaPrintContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaPrintContent(PythonParser.JinjaPrintContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jinjaIfStatements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaIfStatements(PythonParser.JinjaIfStatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jinjaIf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaIf(PythonParser.JinjaIfContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jinjaElif}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaElif(PythonParser.JinjaElifContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jinjaElse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaElse(PythonParser.JinjaElseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jinjaFor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaFor(PythonParser.JinjaForContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jinjaSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaSet(PythonParser.JinjaSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jiniaExtends}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJiniaExtends(PythonParser.JiniaExtendsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jinjaInclude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaInclude(PythonParser.JinjaIncludeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jinjaBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaBlock(PythonParser.JinjaBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#jinjaLocalVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaLocalVariable(PythonParser.JinjaLocalVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#templateBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateBody(PythonParser.TemplateBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#html}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtml(PythonParser.HtmlContext ctx);
	/**
	 * Visit a parse tree produced by the {@code HtmlTagElem}
	 * labeled alternative in {@link PythonParser#htmlElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtmlTagElem(PythonParser.HtmlTagElemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SelfClosingElem}
	 * labeled alternative in {@link PythonParser#htmlElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelfClosingElem(PythonParser.SelfClosingElemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code HtmlTextElem}
	 * labeled alternative in {@link PythonParser#htmlElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtmlTextElem(PythonParser.HtmlTextElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#htmlDoctype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtmlDoctype(PythonParser.HtmlDoctypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StyleHtmlTag}
	 * labeled alternative in {@link PythonParser#htmlTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyleHtmlTag(PythonParser.StyleHtmlTagContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GenericHtmlTag}
	 * labeled alternative in {@link PythonParser#htmlTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericHtmlTag(PythonParser.GenericHtmlTagContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#styleTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyleTag(PythonParser.StyleTagContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#genericHtml}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericHtml(PythonParser.GenericHtmlContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#selfClosingTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelfClosingTag(PythonParser.SelfClosingTagContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#htmlAttributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtmlAttributes(PythonParser.HtmlAttributesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AttrNameId}
	 * labeled alternative in {@link PythonParser#attributeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrNameId(PythonParser.AttrNameIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AttrNameClass}
	 * labeled alternative in {@link PythonParser#attributeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrNameClass(PythonParser.AttrNameClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#attributeValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeValue(PythonParser.AttributeValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#htmlBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtmlBody(PythonParser.HtmlBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#htmlText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtmlText(PythonParser.HtmlTextContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CssBlock}
	 * labeled alternative in {@link PythonParser#css}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssBlock(PythonParser.CssBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CssAnnotation}
	 * labeled alternative in {@link PythonParser#css}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssAnnotation(PythonParser.CssAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#cssSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssSelector(PythonParser.CssSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#cssKeyValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssKeyValue(PythonParser.CssKeyValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#cssKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssKey(PythonParser.CssKeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cssVNumber}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssVNumber(PythonParser.CssVNumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cssVId}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssVId(PythonParser.CssVIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cssVColor}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssVColor(PythonParser.CssVColorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cssVStr}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssVStr(PythonParser.CssVStrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cssVJinja}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssVJinja(PythonParser.CssVJinjaContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#cssComment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssComment(PythonParser.CssCommentContext ctx);
}