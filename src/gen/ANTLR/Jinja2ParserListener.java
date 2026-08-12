// Generated from src/ANTLR/Jinja2Parser.g4 by ANTLR 4.13.2
package gen.ANTLR;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link Jinja2Parser}.
 */
public interface Jinja2ParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#template}.
	 * @param ctx the parse tree
	 */
	void enterTemplate(Jinja2Parser.TemplateContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#template}.
	 * @param ctx the parse tree
	 */
	void exitTemplate(Jinja2Parser.TemplateContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#textChunk}.
	 * @param ctx the parse tree
	 */
	void enterTextChunk(Jinja2Parser.TextChunkContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#textChunk}.
	 * @param ctx the parse tree
	 */
	void exitTextChunk(Jinja2Parser.TextChunkContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(Jinja2Parser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(Jinja2Parser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(Jinja2Parser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(Jinja2Parser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void enterIfContent(Jinja2Parser.IfContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void exitIfContent(Jinja2Parser.IfContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void enterForContent(Jinja2Parser.ForContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void exitForContent(Jinja2Parser.ForContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SetContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void enterSetContent(Jinja2Parser.SetContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SetContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void exitSetContent(Jinja2Parser.SetContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BlockContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void enterBlockContent(Jinja2Parser.BlockContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BlockContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void exitBlockContent(Jinja2Parser.BlockContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExtendsContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void enterExtendsContent(Jinja2Parser.ExtendsContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExtendsContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void exitExtendsContent(Jinja2Parser.ExtendsContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IncludeContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void enterIncludeContent(Jinja2Parser.IncludeContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IncludeContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void exitIncludeContent(Jinja2Parser.IncludeContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WithContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void enterWithContent(Jinja2Parser.WithContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WithContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 */
	void exitWithContent(Jinja2Parser.WithContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NoBodyComment}
	 * labeled alternative in {@link Jinja2Parser#comment}.
	 * @param ctx the parse tree
	 */
	void enterNoBodyComment(Jinja2Parser.NoBodyCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NoBodyComment}
	 * labeled alternative in {@link Jinja2Parser#comment}.
	 * @param ctx the parse tree
	 */
	void exitNoBodyComment(Jinja2Parser.NoBodyCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BodyComment}
	 * labeled alternative in {@link Jinja2Parser#comment}.
	 * @param ctx the parse tree
	 */
	void enterBodyComment(Jinja2Parser.BodyCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BodyComment}
	 * labeled alternative in {@link Jinja2Parser#comment}.
	 * @param ctx the parse tree
	 */
	void exitBodyComment(Jinja2Parser.BodyCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CommentText}
	 * labeled alternative in {@link Jinja2Parser#commentBody}.
	 * @param ctx the parse tree
	 */
	void enterCommentText(Jinja2Parser.CommentTextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CommentText}
	 * labeled alternative in {@link Jinja2Parser#commentBody}.
	 * @param ctx the parse tree
	 */
	void exitCommentText(Jinja2Parser.CommentTextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CommentHash}
	 * labeled alternative in {@link Jinja2Parser#commentBody}.
	 * @param ctx the parse tree
	 */
	void enterCommentHash(Jinja2Parser.CommentHashContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CommentHash}
	 * labeled alternative in {@link Jinja2Parser#commentBody}.
	 * @param ctx the parse tree
	 */
	void exitCommentHash(Jinja2Parser.CommentHashContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(Jinja2Parser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(Jinja2Parser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#elifClause}.
	 * @param ctx the parse tree
	 */
	void enterElifClause(Jinja2Parser.ElifClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#elifClause}.
	 * @param ctx the parse tree
	 */
	void exitElifClause(Jinja2Parser.ElifClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#elseClause}.
	 * @param ctx the parse tree
	 */
	void enterElseClause(Jinja2Parser.ElseClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#elseClause}.
	 * @param ctx the parse tree
	 */
	void exitElseClause(Jinja2Parser.ElseClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#forStmt}.
	 * @param ctx the parse tree
	 */
	void enterForStmt(Jinja2Parser.ForStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#forStmt}.
	 * @param ctx the parse tree
	 */
	void exitForStmt(Jinja2Parser.ForStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(Jinja2Parser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(Jinja2Parser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#idList}.
	 * @param ctx the parse tree
	 */
	void enterIdList(Jinja2Parser.IdListContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#idList}.
	 * @param ctx the parse tree
	 */
	void exitIdList(Jinja2Parser.IdListContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#setStmt}.
	 * @param ctx the parse tree
	 */
	void enterSetStmt(Jinja2Parser.SetStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#setStmt}.
	 * @param ctx the parse tree
	 */
	void exitSetStmt(Jinja2Parser.SetStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterBlockStmt(Jinja2Parser.BlockStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitBlockStmt(Jinja2Parser.BlockStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#extendsStmt}.
	 * @param ctx the parse tree
	 */
	void enterExtendsStmt(Jinja2Parser.ExtendsStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#extendsStmt}.
	 * @param ctx the parse tree
	 */
	void exitExtendsStmt(Jinja2Parser.ExtendsStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#includeStmt}.
	 * @param ctx the parse tree
	 */
	void enterIncludeStmt(Jinja2Parser.IncludeStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#includeStmt}.
	 * @param ctx the parse tree
	 */
	void exitIncludeStmt(Jinja2Parser.IncludeStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#withStmt}.
	 * @param ctx the parse tree
	 */
	void enterWithStmt(Jinja2Parser.WithStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#withStmt}.
	 * @param ctx the parse tree
	 */
	void exitWithStmt(Jinja2Parser.WithStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TextContent}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void enterTextContent(Jinja2Parser.TextContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TextContent}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void exitTextContent(Jinja2Parser.TextContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprContent}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void enterExprContent(Jinja2Parser.ExprContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprContent}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void exitExprContent(Jinja2Parser.ExprContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CommentContent}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void enterCommentContent(Jinja2Parser.CommentContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CommentContent}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void exitCommentContent(Jinja2Parser.CommentContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void enterIfEmbedded(Jinja2Parser.IfEmbeddedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void exitIfEmbedded(Jinja2Parser.IfEmbeddedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void enterForEmbedded(Jinja2Parser.ForEmbeddedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void exitForEmbedded(Jinja2Parser.ForEmbeddedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SetEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void enterSetEmbedded(Jinja2Parser.SetEmbeddedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SetEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void exitSetEmbedded(Jinja2Parser.SetEmbeddedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BlockEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void enterBlockEmbedded(Jinja2Parser.BlockEmbeddedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BlockEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void exitBlockEmbedded(Jinja2Parser.BlockEmbeddedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExtendsEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void enterExtendsEmbedded(Jinja2Parser.ExtendsEmbeddedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExtendsEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void exitExtendsEmbedded(Jinja2Parser.ExtendsEmbeddedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IncludeEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void enterIncludeEmbedded(Jinja2Parser.IncludeEmbeddedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IncludeEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void exitIncludeEmbedded(Jinja2Parser.IncludeEmbeddedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WithEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void enterWithEmbedded(Jinja2Parser.WithEmbeddedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WithEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 */
	void exitWithEmbedded(Jinja2Parser.WithEmbeddedContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(Jinja2Parser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(Jinja2Parser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#orExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(Jinja2Parser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#orExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(Jinja2Parser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(Jinja2Parser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(Jinja2Parser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link Jinja2Parser#notExpr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(Jinja2Parser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link Jinja2Parser#notExpr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(Jinja2Parser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ComparisonExpr}
	 * labeled alternative in {@link Jinja2Parser#notExpr}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpr(Jinja2Parser.ComparisonExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ComparisonExpr}
	 * labeled alternative in {@link Jinja2Parser#notExpr}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpr(Jinja2Parser.ComparisonExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterComparison(Jinja2Parser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitComparison(Jinja2Parser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessThan}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void enterLessThan(Jinja2Parser.LessThanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThan}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void exitLessThan(Jinja2Parser.LessThanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreaterThan}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThan(Jinja2Parser.GreaterThanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterThan}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThan(Jinja2Parser.GreaterThanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessEqual}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void enterLessEqual(Jinja2Parser.LessEqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessEqual}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void exitLessEqual(Jinja2Parser.LessEqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreaterEqual}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void enterGreaterEqual(Jinja2Parser.GreaterEqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterEqual}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void exitGreaterEqual(Jinja2Parser.GreaterEqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Equal}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void enterEqual(Jinja2Parser.EqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Equal}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void exitEqual(Jinja2Parser.EqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotEqual}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void enterNotEqual(Jinja2Parser.NotEqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotEqual}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void exitNotEqual(Jinja2Parser.NotEqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InOp}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void enterInOp(Jinja2Parser.InOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InOp}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void exitInOp(Jinja2Parser.InOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsOp}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void enterIsOp(Jinja2Parser.IsOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsOp}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 */
	void exitIsOp(Jinja2Parser.IsOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#concat}.
	 * @param ctx the parse tree
	 */
	void enterConcat(Jinja2Parser.ConcatContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#concat}.
	 * @param ctx the parse tree
	 */
	void exitConcat(Jinja2Parser.ConcatContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#pipeExpr}.
	 * @param ctx the parse tree
	 */
	void enterPipeExpr(Jinja2Parser.PipeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#pipeExpr}.
	 * @param ctx the parse tree
	 */
	void exitPipeExpr(Jinja2Parser.PipeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(Jinja2Parser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(Jinja2Parser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(Jinja2Parser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(Jinja2Parser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryOp}
	 * labeled alternative in {@link Jinja2Parser#unary}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOp(Jinja2Parser.UnaryOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryOp}
	 * labeled alternative in {@link Jinja2Parser#unary}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOp(Jinja2Parser.UnaryOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AtomUnary}
	 * labeled alternative in {@link Jinja2Parser#unary}.
	 * @param ctx the parse tree
	 */
	void enterAtomUnary(Jinja2Parser.AtomUnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AtomUnary}
	 * labeled alternative in {@link Jinja2Parser#unary}.
	 * @param ctx the parse tree
	 */
	void exitAtomUnary(Jinja2Parser.AtomUnaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IndexAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void enterIndexAtom(Jinja2Parser.IndexAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IndexAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void exitIndexAtom(Jinja2Parser.IndexAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void enterCallAtom(Jinja2Parser.CallAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void exitCallAtom(Jinja2Parser.CallAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void enterListAtom(Jinja2Parser.ListAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void exitListAtom(Jinja2Parser.ListAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void enterParenAtom(Jinja2Parser.ParenAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void exitParenAtom(Jinja2Parser.ParenAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void enterLiteralAtom(Jinja2Parser.LiteralAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void exitLiteralAtom(Jinja2Parser.LiteralAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FieldAccess}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void enterFieldAccess(Jinja2Parser.FieldAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FieldAccess}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void exitFieldAccess(Jinja2Parser.FieldAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void enterIdAtom(Jinja2Parser.IdAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 */
	void exitIdAtom(Jinja2Parser.IdAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NumberLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 */
	void enterNumberLit(Jinja2Parser.NumberLitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NumberLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 */
	void exitNumberLit(Jinja2Parser.NumberLitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 */
	void enterStringLit(Jinja2Parser.StringLitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 */
	void exitStringLit(Jinja2Parser.StringLitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TrueLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 */
	void enterTrueLit(Jinja2Parser.TrueLitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TrueLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 */
	void exitTrueLit(Jinja2Parser.TrueLitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FalseLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 */
	void enterFalseLit(Jinja2Parser.FalseLitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FalseLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 */
	void exitFalseLit(Jinja2Parser.FalseLitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NoneLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 */
	void enterNoneLit(Jinja2Parser.NoneLitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NoneLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 */
	void exitNoneLit(Jinja2Parser.NoneLitContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgList(Jinja2Parser.ArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgList(Jinja2Parser.ArgListContext ctx);
}