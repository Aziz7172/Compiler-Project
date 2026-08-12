// Generated from src/ANTLR/Jinja2Parser.g4 by ANTLR 4.13.2
package gen.ANTLR;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link Jinja2Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface Jinja2ParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#template}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplate(Jinja2Parser.TemplateContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#textChunk}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextChunk(Jinja2Parser.TextChunkContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(Jinja2Parser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(Jinja2Parser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfContent(Jinja2Parser.IfContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForContent(Jinja2Parser.ForContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SetContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetContent(Jinja2Parser.SetContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BlockContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockContent(Jinja2Parser.BlockContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExtendsContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtendsContent(Jinja2Parser.ExtendsContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IncludeContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncludeContent(Jinja2Parser.IncludeContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WithContent}
	 * labeled alternative in {@link Jinja2Parser#stmtContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithContent(Jinja2Parser.WithContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NoBodyComment}
	 * labeled alternative in {@link Jinja2Parser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoBodyComment(Jinja2Parser.NoBodyCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BodyComment}
	 * labeled alternative in {@link Jinja2Parser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyComment(Jinja2Parser.BodyCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CommentText}
	 * labeled alternative in {@link Jinja2Parser#commentBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentText(Jinja2Parser.CommentTextContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CommentHash}
	 * labeled alternative in {@link Jinja2Parser#commentBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentHash(Jinja2Parser.CommentHashContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(Jinja2Parser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#elifClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElifClause(Jinja2Parser.ElifClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#elseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseClause(Jinja2Parser.ElseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#forStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStmt(Jinja2Parser.ForStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(Jinja2Parser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#idList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdList(Jinja2Parser.IdListContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#setStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetStmt(Jinja2Parser.SetStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStmt(Jinja2Parser.BlockStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#extendsStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtendsStmt(Jinja2Parser.ExtendsStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#includeStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncludeStmt(Jinja2Parser.IncludeStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#withStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithStmt(Jinja2Parser.WithStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TextContent}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextContent(Jinja2Parser.TextContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprContent}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprContent(Jinja2Parser.ExprContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CommentContent}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentContent(Jinja2Parser.CommentContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfEmbedded(Jinja2Parser.IfEmbeddedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForEmbedded(Jinja2Parser.ForEmbeddedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SetEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetEmbedded(Jinja2Parser.SetEmbeddedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BlockEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockEmbedded(Jinja2Parser.BlockEmbeddedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExtendsEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtendsEmbedded(Jinja2Parser.ExtendsEmbeddedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IncludeEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncludeEmbedded(Jinja2Parser.IncludeEmbeddedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WithEmbedded}
	 * labeled alternative in {@link Jinja2Parser#bodyContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithEmbedded(Jinja2Parser.WithEmbeddedContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(Jinja2Parser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#orExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(Jinja2Parser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#andExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(Jinja2Parser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link Jinja2Parser#notExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(Jinja2Parser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ComparisonExpr}
	 * labeled alternative in {@link Jinja2Parser#notExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpr(Jinja2Parser.ComparisonExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison(Jinja2Parser.ComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LessThan}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThan(Jinja2Parser.LessThanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GreaterThan}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreaterThan(Jinja2Parser.GreaterThanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LessEqual}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessEqual(Jinja2Parser.LessEqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GreaterEqual}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreaterEqual(Jinja2Parser.GreaterEqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Equal}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqual(Jinja2Parser.EqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotEqual}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotEqual(Jinja2Parser.NotEqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InOp}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInOp(Jinja2Parser.InOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IsOp}
	 * labeled alternative in {@link Jinja2Parser#compOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsOp(Jinja2Parser.IsOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#concat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcat(Jinja2Parser.ConcatContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#pipeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPipeExpr(Jinja2Parser.PipeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(Jinja2Parser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(Jinja2Parser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryOp}
	 * labeled alternative in {@link Jinja2Parser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOp(Jinja2Parser.UnaryOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AtomUnary}
	 * labeled alternative in {@link Jinja2Parser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomUnary(Jinja2Parser.AtomUnaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IndexAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexAtom(Jinja2Parser.IndexAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CallAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallAtom(Jinja2Parser.CallAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ListAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListAtom(Jinja2Parser.ListAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenAtom(Jinja2Parser.ParenAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralAtom(Jinja2Parser.LiteralAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FieldAccess}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldAccess(Jinja2Parser.FieldAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdAtom}
	 * labeled alternative in {@link Jinja2Parser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdAtom(Jinja2Parser.IdAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NumberLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberLit(Jinja2Parser.NumberLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLit(Jinja2Parser.StringLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueLit(Jinja2Parser.TrueLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseLit(Jinja2Parser.FalseLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NoneLit}
	 * labeled alternative in {@link Jinja2Parser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoneLit(Jinja2Parser.NoneLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#argList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgList(Jinja2Parser.ArgListContext ctx);
}