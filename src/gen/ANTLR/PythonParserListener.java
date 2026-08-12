// Generated from src/ANTLR/PythonParser.g4 by ANTLR 4.13.2
package gen.ANTLR;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PythonParser}.
 */
public interface PythonParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PythonParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(PythonParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(PythonParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#statementBlock}.
	 * @param ctx the parse tree
	 */
	void enterStatementBlock(PythonParser.StatementBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#statementBlock}.
	 * @param ctx the parse tree
	 */
	void exitStatementBlock(PythonParser.StatementBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SimpleSeqStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleSeqStmt(PythonParser.SimpleSeqStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SimpleSeqStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleSeqStmt(PythonParser.SimpleSeqStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CompoundStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStmt(PythonParser.CompoundStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CompoundStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStmt(PythonParser.CompoundStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CssStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCssStmt(PythonParser.CssStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CssStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCssStmt(PythonParser.CssStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HtmlStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterHtmlStmt(PythonParser.HtmlStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HtmlStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitHtmlStmt(PythonParser.HtmlStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterJinjaStmt(PythonParser.JinjaStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaStmt}
	 * labeled alternative in {@link PythonParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitJinjaStmt(PythonParser.JinjaStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImportStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void enterImportStmt(PythonParser.ImportStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImportStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void exitImportStmt(PythonParser.ImportStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssignStmt(PythonParser.AssignStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssignStmt(PythonParser.AssignStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PrintStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void enterPrintStmt(PythonParser.PrintStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrintStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void exitPrintStmt(PythonParser.PrintStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReturnStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(PythonParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReturnStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(PythonParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ValueStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void enterValueStmt(PythonParser.ValueStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ValueStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void exitValueStmt(PythonParser.ValueStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GlobalStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void enterGlobalStmt(PythonParser.GlobalStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GlobalStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void exitGlobalStmt(PythonParser.GlobalStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PassStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void enterPassStmt(PythonParser.PassStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PassStmt}
	 * labeled alternative in {@link PythonParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void exitPassStmt(PythonParser.PassStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#passStatement}.
	 * @param ctx the parse tree
	 */
	void enterPassStatement(PythonParser.PassStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#passStatement}.
	 * @param ctx the parse tree
	 */
	void exitPassStatement(PythonParser.PassStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfCompound}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfCompound(PythonParser.IfCompoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfCompound}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfCompound(PythonParser.IfCompoundContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForCompound}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterForCompound(PythonParser.ForCompoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForCompound}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitForCompound(PythonParser.ForCompoundContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WhileCompound}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileCompound(PythonParser.WhileCompoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileCompound}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileCompound(PythonParser.WhileCompoundContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FuncDef}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterFuncDef(PythonParser.FuncDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FuncDef}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitFuncDef(PythonParser.FuncDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ClassDefStmt}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterClassDefStmt(PythonParser.ClassDefStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ClassDefStmt}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitClassDefStmt(PythonParser.ClassDefStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprCompound}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterExprCompound(PythonParser.ExprCompoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprCompound}
	 * labeled alternative in {@link PythonParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitExprCompound(PythonParser.ExprCompoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(PythonParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(PythonParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AtomVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 */
	void enterAtomVal(PythonParser.AtomValContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AtomVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 */
	void exitAtomVal(PythonParser.AtomValContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 */
	void enterExprVal(PythonParser.ExprValContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 */
	void exitExprVal(PythonParser.ExprValContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 */
	void enterListVal(PythonParser.ListValContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 */
	void exitListVal(PythonParser.ListValContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TupleVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 */
	void enterTupleVal(PythonParser.TupleValContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TupleVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 */
	void exitTupleVal(PythonParser.TupleValContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsonVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 */
	void enterJsonVal(PythonParser.JsonValContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsonVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 */
	void exitJsonVal(PythonParser.JsonValContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListCompVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 */
	void enterListCompVal(PythonParser.ListCompValContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListCompVal}
	 * labeled alternative in {@link PythonParser#value}.
	 * @param ctx the parse tree
	 */
	void exitListCompVal(PythonParser.ListCompValContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(PythonParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(PythonParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#listComprehension}.
	 * @param ctx the parse tree
	 */
	void enterListComprehension(PythonParser.ListComprehensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#listComprehension}.
	 * @param ctx the parse tree
	 */
	void exitListComprehension(PythonParser.ListComprehensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#tuple}.
	 * @param ctx the parse tree
	 */
	void enterTuple(PythonParser.TupleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#tuple}.
	 * @param ctx the parse tree
	 */
	void exitTuple(PythonParser.TupleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#elements}.
	 * @param ctx the parse tree
	 */
	void enterElements(PythonParser.ElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#elements}.
	 * @param ctx the parse tree
	 */
	void exitElements(PythonParser.ElementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#json}.
	 * @param ctx the parse tree
	 */
	void enterJson(PythonParser.JsonContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#json}.
	 * @param ctx the parse tree
	 */
	void exitJson(PythonParser.JsonContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jsonData}.
	 * @param ctx the parse tree
	 */
	void enterJsonData(PythonParser.JsonDataContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jsonData}.
	 * @param ctx the parse tree
	 */
	void exitJsonData(PythonParser.JsonDataContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicalExp}
	 * labeled alternative in {@link PythonParser#expressions}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExp(PythonParser.LogicalExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalExp}
	 * labeled alternative in {@link PythonParser#expressions}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExp(PythonParser.LogicalExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ComparisonExp}
	 * labeled alternative in {@link PythonParser#expressions}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExp(PythonParser.ComparisonExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ComparisonExp}
	 * labeled alternative in {@link PythonParser#expressions}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExp(PythonParser.ComparisonExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MathExp}
	 * labeled alternative in {@link PythonParser#expressions}.
	 * @param ctx the parse tree
	 */
	void enterMathExp(PythonParser.MathExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MathExp}
	 * labeled alternative in {@link PythonParser#expressions}.
	 * @param ctx the parse tree
	 */
	void exitMathExp(PythonParser.MathExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LtExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void enterLtExp(PythonParser.LtExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LtExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void exitLtExp(PythonParser.LtExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GtExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void enterGtExp(PythonParser.GtExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GtExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void exitGtExp(PythonParser.GtExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LteExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void enterLteExp(PythonParser.LteExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LteExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void exitLteExp(PythonParser.LteExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GteExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void enterGteExp(PythonParser.GteExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GteExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void exitGteExp(PythonParser.GteExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void enterEqExp(PythonParser.EqExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void exitEqExp(PythonParser.EqExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StrictEqExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void enterStrictEqExp(PythonParser.StrictEqExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StrictEqExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void exitStrictEqExp(PythonParser.StrictEqExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NeExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void enterNeExp(PythonParser.NeExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NeExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void exitNeExp(PythonParser.NeExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StrictNeqExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void enterStrictNeqExp(PythonParser.StrictNeqExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StrictNeqExp}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void exitStrictNeqExp(PythonParser.StrictNeqExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdComparison}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void enterIdComparison(PythonParser.IdComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdComparison}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void exitIdComparison(PythonParser.IdComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MembershipTest}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void enterMembershipTest(PythonParser.MembershipTestContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MembershipTest}
	 * labeled alternative in {@link PythonParser#comparisonExpressions}.
	 * @param ctx the parse tree
	 */
	void exitMembershipTest(PythonParser.MembershipTestContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ValueExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 */
	void enterValueExp(PythonParser.ValueExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ValueExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 */
	void exitValueExp(PythonParser.ValueExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 */
	void enterMulExp(PythonParser.MulExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 */
	void exitMulExp(PythonParser.MulExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ModExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 */
	void enterModExp(PythonParser.ModExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 */
	void exitModExp(PythonParser.ModExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 */
	void enterAddExp(PythonParser.AddExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 */
	void exitAddExp(PythonParser.AddExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DivExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 */
	void enterDivExp(PythonParser.DivExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DivExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 */
	void exitDivExp(PythonParser.DivExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SubExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 */
	void enterSubExp(PythonParser.SubExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SubExp}
	 * labeled alternative in {@link PythonParser#mathematicalExpressions}.
	 * @param ctx the parse tree
	 */
	void exitSubExp(PythonParser.SubExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExp}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 */
	void enterNotExp(PythonParser.NotExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExp}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 */
	void exitNotExp(PythonParser.NotExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExp}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 */
	void enterAndExp(PythonParser.AndExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExp}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 */
	void exitAndExp(PythonParser.AndExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CompAsLogical}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 */
	void enterCompAsLogical(PythonParser.CompAsLogicalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CompAsLogical}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 */
	void exitCompAsLogical(PythonParser.CompAsLogicalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ValuesAsLogical}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 */
	void enterValuesAsLogical(PythonParser.ValuesAsLogicalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ValuesAsLogical}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 */
	void exitValuesAsLogical(PythonParser.ValuesAsLogicalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExp}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 */
	void enterOrExp(PythonParser.OrExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExp}
	 * labeled alternative in {@link PythonParser#logicalExpressions}.
	 * @param ctx the parse tree
	 */
	void exitOrExp(PythonParser.OrExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#valuesExp}.
	 * @param ctx the parse tree
	 */
	void enterValuesExp(PythonParser.ValuesExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#valuesExp}.
	 * @param ctx the parse tree
	 */
	void exitValuesExp(PythonParser.ValuesExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AtomWithAccess}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtomWithAccess(PythonParser.AtomWithAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AtomWithAccess}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtomWithAccess(PythonParser.AtomWithAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Id}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 */
	void enterId(PythonParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Id}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 */
	void exitId(PythonParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Number}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 */
	void enterNumber(PythonParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 */
	void exitNumber(PythonParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 */
	void enterString(PythonParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 */
	void exitString(PythonParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code True}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 */
	void enterTrue(PythonParser.TrueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code True}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 */
	void exitTrue(PythonParser.TrueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code False}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 */
	void enterFalse(PythonParser.FalseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code False}
	 * labeled alternative in {@link PythonParser#primaryAtom}.
	 * @param ctx the parse tree
	 */
	void exitFalse(PythonParser.FalseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DotAccess}
	 * labeled alternative in {@link PythonParser#postfix}.
	 * @param ctx the parse tree
	 */
	void enterDotAccess(PythonParser.DotAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DotAccess}
	 * labeled alternative in {@link PythonParser#postfix}.
	 * @param ctx the parse tree
	 */
	void exitDotAccess(PythonParser.DotAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IndexAccess}
	 * labeled alternative in {@link PythonParser#postfix}.
	 * @param ctx the parse tree
	 */
	void enterIndexAccess(PythonParser.IndexAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IndexAccess}
	 * labeled alternative in {@link PythonParser#postfix}.
	 * @param ctx the parse tree
	 */
	void exitIndexAccess(PythonParser.IndexAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FuncCallPostfix}
	 * labeled alternative in {@link PythonParser#postfix}.
	 * @param ctx the parse tree
	 */
	void enterFuncCallPostfix(PythonParser.FuncCallPostfixContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FuncCallPostfix}
	 * labeled alternative in {@link PythonParser#postfix}.
	 * @param ctx the parse tree
	 */
	void exitFuncCallPostfix(PythonParser.FuncCallPostfixContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#globalStatement}.
	 * @param ctx the parse tree
	 */
	void enterGlobalStatement(PythonParser.GlobalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#globalStatement}.
	 * @param ctx the parse tree
	 */
	void exitGlobalStatement(PythonParser.GlobalStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImportSyntaxStmt}
	 * labeled alternative in {@link PythonParser#pythonImport}.
	 * @param ctx the parse tree
	 */
	void enterImportSyntaxStmt(PythonParser.ImportSyntaxStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImportSyntaxStmt}
	 * labeled alternative in {@link PythonParser#pythonImport}.
	 * @param ctx the parse tree
	 */
	void exitImportSyntaxStmt(PythonParser.ImportSyntaxStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FromImportStmt}
	 * labeled alternative in {@link PythonParser#pythonImport}.
	 * @param ctx the parse tree
	 */
	void enterFromImportStmt(PythonParser.FromImportStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FromImportStmt}
	 * labeled alternative in {@link PythonParser#pythonImport}.
	 * @param ctx the parse tree
	 */
	void exitFromImportStmt(PythonParser.FromImportStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SimpleImport}
	 * labeled alternative in {@link PythonParser#importSyntax}.
	 * @param ctx the parse tree
	 */
	void enterSimpleImport(PythonParser.SimpleImportContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SimpleImport}
	 * labeled alternative in {@link PythonParser#importSyntax}.
	 * @param ctx the parse tree
	 */
	void exitSimpleImport(PythonParser.SimpleImportContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringImport}
	 * labeled alternative in {@link PythonParser#importSyntax}.
	 * @param ctx the parse tree
	 */
	void enterStringImport(PythonParser.StringImportContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringImport}
	 * labeled alternative in {@link PythonParser#importSyntax}.
	 * @param ctx the parse tree
	 */
	void exitStringImport(PythonParser.StringImportContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdFromImportStmt}
	 * labeled alternative in {@link PythonParser#fromImport}.
	 * @param ctx the parse tree
	 */
	void enterIdFromImportStmt(PythonParser.IdFromImportStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdFromImportStmt}
	 * labeled alternative in {@link PythonParser#fromImport}.
	 * @param ctx the parse tree
	 */
	void exitIdFromImportStmt(PythonParser.IdFromImportStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StrFromImportStmt}
	 * labeled alternative in {@link PythonParser#fromImport}.
	 * @param ctx the parse tree
	 */
	void enterStrFromImportStmt(PythonParser.StrFromImportStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StrFromImportStmt}
	 * labeled alternative in {@link PythonParser#fromImport}.
	 * @param ctx the parse tree
	 */
	void exitStrFromImportStmt(PythonParser.StrFromImportStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#importedNames}.
	 * @param ctx the parse tree
	 */
	void enterImportedNames(PythonParser.ImportedNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#importedNames}.
	 * @param ctx the parse tree
	 */
	void exitImportedNames(PythonParser.ImportedNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#importsAliases}.
	 * @param ctx the parse tree
	 */
	void enterImportsAliases(PythonParser.ImportsAliasesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#importsAliases}.
	 * @param ctx the parse tree
	 */
	void exitImportsAliases(PythonParser.ImportsAliasesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#printStatement}.
	 * @param ctx the parse tree
	 */
	void enterPrintStatement(PythonParser.PrintStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#printStatement}.
	 * @param ctx the parse tree
	 */
	void exitPrintStatement(PythonParser.PrintStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ValuePrintArgs}
	 * labeled alternative in {@link PythonParser#printArgs}.
	 * @param ctx the parse tree
	 */
	void enterValuePrintArgs(PythonParser.ValuePrintArgsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ValuePrintArgs}
	 * labeled alternative in {@link PythonParser#printArgs}.
	 * @param ctx the parse tree
	 */
	void exitValuePrintArgs(PythonParser.ValuePrintArgsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprPrintArgs}
	 * labeled alternative in {@link PythonParser#printArgs}.
	 * @param ctx the parse tree
	 */
	void enterExprPrintArgs(PythonParser.ExprPrintArgsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprPrintArgs}
	 * labeled alternative in {@link PythonParser#printArgs}.
	 * @param ctx the parse tree
	 */
	void exitExprPrintArgs(PythonParser.ExprPrintArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(PythonParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(PythonParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(PythonParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(PythonParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#ifBlock}.
	 * @param ctx the parse tree
	 */
	void enterIfBlock(PythonParser.IfBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#ifBlock}.
	 * @param ctx the parse tree
	 */
	void exitIfBlock(PythonParser.IfBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#elifBlock}.
	 * @param ctx the parse tree
	 */
	void enterElifBlock(PythonParser.ElifBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#elifBlock}.
	 * @param ctx the parse tree
	 */
	void exitElifBlock(PythonParser.ElifBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#elseBlock}.
	 * @param ctx the parse tree
	 */
	void enterElseBlock(PythonParser.ElseBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#elseBlock}.
	 * @param ctx the parse tree
	 */
	void exitElseBlock(PythonParser.ElseBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#forLoop}.
	 * @param ctx the parse tree
	 */
	void enterForLoop(PythonParser.ForLoopContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#forLoop}.
	 * @param ctx the parse tree
	 */
	void exitForLoop(PythonParser.ForLoopContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#whileLoop}.
	 * @param ctx the parse tree
	 */
	void enterWhileLoop(PythonParser.WhileLoopContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#whileLoop}.
	 * @param ctx the parse tree
	 */
	void exitWhileLoop(PythonParser.WhileLoopContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(PythonParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(PythonParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#parameters}.
	 * @param ctx the parse tree
	 */
	void enterParameters(PythonParser.ParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#parameters}.
	 * @param ctx the parse tree
	 */
	void exitParameters(PythonParser.ParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(PythonParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(PythonParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(PythonParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(PythonParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(PythonParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(PythonParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(PythonParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(PythonParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Decorator}
	 * labeled alternative in {@link PythonParser#decorator_rule}.
	 * @param ctx the parse tree
	 */
	void enterDecorator(PythonParser.DecoratorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Decorator}
	 * labeled alternative in {@link PythonParser#decorator_rule}.
	 * @param ctx the parse tree
	 */
	void exitDecorator(PythonParser.DecoratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#classDef}.
	 * @param ctx the parse tree
	 */
	void enterClassDef(PythonParser.ClassDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#classDef}.
	 * @param ctx the parse tree
	 */
	void exitClassDef(PythonParser.ClassDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#baseClass}.
	 * @param ctx the parse tree
	 */
	void enterBaseClass(PythonParser.BaseClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#baseClass}.
	 * @param ctx the parse tree
	 */
	void exitBaseClass(PythonParser.BaseClassContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaExprBody}
	 * labeled alternative in {@link PythonParser#jinjaBody}.
	 * @param ctx the parse tree
	 */
	void enterJinjaExprBody(PythonParser.JinjaExprBodyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaExprBody}
	 * labeled alternative in {@link PythonParser#jinjaBody}.
	 * @param ctx the parse tree
	 */
	void exitJinjaExprBody(PythonParser.JinjaExprBodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaStmtBody}
	 * labeled alternative in {@link PythonParser#jinjaBody}.
	 * @param ctx the parse tree
	 */
	void enterJinjaStmtBody(PythonParser.JinjaStmtBodyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaStmtBody}
	 * labeled alternative in {@link PythonParser#jinjaBody}.
	 * @param ctx the parse tree
	 */
	void exitJinjaStmtBody(PythonParser.JinjaStmtBodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaCommentBody}
	 * labeled alternative in {@link PythonParser#jinjaBody}.
	 * @param ctx the parse tree
	 */
	void enterJinjaCommentBody(PythonParser.JinjaCommentBodyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaCommentBody}
	 * labeled alternative in {@link PythonParser#jinjaBody}.
	 * @param ctx the parse tree
	 */
	void exitJinjaCommentBody(PythonParser.JinjaCommentBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jinjaExpression}.
	 * @param ctx the parse tree
	 */
	void enterJinjaExpression(PythonParser.JinjaExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jinjaExpression}.
	 * @param ctx the parse tree
	 */
	void exitJinjaExpression(PythonParser.JinjaExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jinjaFilter}.
	 * @param ctx the parse tree
	 */
	void enterJinjaFilter(PythonParser.JinjaFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jinjaFilter}.
	 * @param ctx the parse tree
	 */
	void exitJinjaFilter(PythonParser.JinjaFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EmptyComment}
	 * labeled alternative in {@link PythonParser#jinjaComment}.
	 * @param ctx the parse tree
	 */
	void enterEmptyComment(PythonParser.EmptyCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EmptyComment}
	 * labeled alternative in {@link PythonParser#jinjaComment}.
	 * @param ctx the parse tree
	 */
	void exitEmptyComment(PythonParser.EmptyCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CommentWithBody}
	 * labeled alternative in {@link PythonParser#jinjaComment}.
	 * @param ctx the parse tree
	 */
	void enterCommentWithBody(PythonParser.CommentWithBodyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CommentWithBody}
	 * labeled alternative in {@link PythonParser#jinjaComment}.
	 * @param ctx the parse tree
	 */
	void exitCommentWithBody(PythonParser.CommentWithBodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CcContent}
	 * labeled alternative in {@link PythonParser#commentBody}.
	 * @param ctx the parse tree
	 */
	void enterCcContent(PythonParser.CcContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CcContent}
	 * labeled alternative in {@link PythonParser#commentBody}.
	 * @param ctx the parse tree
	 */
	void exitCcContent(PythonParser.CcContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CcHash}
	 * labeled alternative in {@link PythonParser#commentBody}.
	 * @param ctx the parse tree
	 */
	void enterCcHash(PythonParser.CcHashContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CcHash}
	 * labeled alternative in {@link PythonParser#commentBody}.
	 * @param ctx the parse tree
	 */
	void exitCcHash(PythonParser.CcHashContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jinjaStatement}.
	 * @param ctx the parse tree
	 */
	void enterJinjaStatement(PythonParser.JinjaStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jinjaStatement}.
	 * @param ctx the parse tree
	 */
	void exitJinjaStatement(PythonParser.JinjaStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaIfContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void enterJinjaIfContent(PythonParser.JinjaIfContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaIfContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void exitJinjaIfContent(PythonParser.JinjaIfContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaForContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void enterJinjaForContent(PythonParser.JinjaForContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaForContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void exitJinjaForContent(PythonParser.JinjaForContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaSetContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void enterJinjaSetContent(PythonParser.JinjaSetContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaSetContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void exitJinjaSetContent(PythonParser.JinjaSetContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaExtendsContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void enterJinjaExtendsContent(PythonParser.JinjaExtendsContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaExtendsContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void exitJinjaExtendsContent(PythonParser.JinjaExtendsContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaIncludeContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void enterJinjaIncludeContent(PythonParser.JinjaIncludeContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaIncludeContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void exitJinjaIncludeContent(PythonParser.JinjaIncludeContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaBlockContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void enterJinjaBlockContent(PythonParser.JinjaBlockContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaBlockContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void exitJinjaBlockContent(PythonParser.JinjaBlockContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaLocalVarContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void enterJinjaLocalVarContent(PythonParser.JinjaLocalVarContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaLocalVarContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void exitJinjaLocalVarContent(PythonParser.JinjaLocalVarContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaImportContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void enterJinjaImportContent(PythonParser.JinjaImportContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaImportContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void exitJinjaImportContent(PythonParser.JinjaImportContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaFromImportContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void enterJinjaFromImportContent(PythonParser.JinjaFromImportContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaFromImportContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void exitJinjaFromImportContent(PythonParser.JinjaFromImportContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaPrintContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void enterJinjaPrintContent(PythonParser.JinjaPrintContentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaPrintContent}
	 * labeled alternative in {@link PythonParser#jinjaStatementContent}.
	 * @param ctx the parse tree
	 */
	void exitJinjaPrintContent(PythonParser.JinjaPrintContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jinjaIfStatements}.
	 * @param ctx the parse tree
	 */
	void enterJinjaIfStatements(PythonParser.JinjaIfStatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jinjaIfStatements}.
	 * @param ctx the parse tree
	 */
	void exitJinjaIfStatements(PythonParser.JinjaIfStatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jinjaIf}.
	 * @param ctx the parse tree
	 */
	void enterJinjaIf(PythonParser.JinjaIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jinjaIf}.
	 * @param ctx the parse tree
	 */
	void exitJinjaIf(PythonParser.JinjaIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jinjaElif}.
	 * @param ctx the parse tree
	 */
	void enterJinjaElif(PythonParser.JinjaElifContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jinjaElif}.
	 * @param ctx the parse tree
	 */
	void exitJinjaElif(PythonParser.JinjaElifContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jinjaElse}.
	 * @param ctx the parse tree
	 */
	void enterJinjaElse(PythonParser.JinjaElseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jinjaElse}.
	 * @param ctx the parse tree
	 */
	void exitJinjaElse(PythonParser.JinjaElseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jinjaFor}.
	 * @param ctx the parse tree
	 */
	void enterJinjaFor(PythonParser.JinjaForContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jinjaFor}.
	 * @param ctx the parse tree
	 */
	void exitJinjaFor(PythonParser.JinjaForContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jinjaSet}.
	 * @param ctx the parse tree
	 */
	void enterJinjaSet(PythonParser.JinjaSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jinjaSet}.
	 * @param ctx the parse tree
	 */
	void exitJinjaSet(PythonParser.JinjaSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jiniaExtends}.
	 * @param ctx the parse tree
	 */
	void enterJiniaExtends(PythonParser.JiniaExtendsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jiniaExtends}.
	 * @param ctx the parse tree
	 */
	void exitJiniaExtends(PythonParser.JiniaExtendsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jinjaInclude}.
	 * @param ctx the parse tree
	 */
	void enterJinjaInclude(PythonParser.JinjaIncludeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jinjaInclude}.
	 * @param ctx the parse tree
	 */
	void exitJinjaInclude(PythonParser.JinjaIncludeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jinjaBlock}.
	 * @param ctx the parse tree
	 */
	void enterJinjaBlock(PythonParser.JinjaBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jinjaBlock}.
	 * @param ctx the parse tree
	 */
	void exitJinjaBlock(PythonParser.JinjaBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#jinjaLocalVariable}.
	 * @param ctx the parse tree
	 */
	void enterJinjaLocalVariable(PythonParser.JinjaLocalVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#jinjaLocalVariable}.
	 * @param ctx the parse tree
	 */
	void exitJinjaLocalVariable(PythonParser.JinjaLocalVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#templateBody}.
	 * @param ctx the parse tree
	 */
	void enterTemplateBody(PythonParser.TemplateBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#templateBody}.
	 * @param ctx the parse tree
	 */
	void exitTemplateBody(PythonParser.TemplateBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#html}.
	 * @param ctx the parse tree
	 */
	void enterHtml(PythonParser.HtmlContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#html}.
	 * @param ctx the parse tree
	 */
	void exitHtml(PythonParser.HtmlContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HtmlTagElem}
	 * labeled alternative in {@link PythonParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterHtmlTagElem(PythonParser.HtmlTagElemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HtmlTagElem}
	 * labeled alternative in {@link PythonParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitHtmlTagElem(PythonParser.HtmlTagElemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SelfClosingElem}
	 * labeled alternative in {@link PythonParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterSelfClosingElem(PythonParser.SelfClosingElemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SelfClosingElem}
	 * labeled alternative in {@link PythonParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitSelfClosingElem(PythonParser.SelfClosingElemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HtmlTextElem}
	 * labeled alternative in {@link PythonParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterHtmlTextElem(PythonParser.HtmlTextElemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HtmlTextElem}
	 * labeled alternative in {@link PythonParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitHtmlTextElem(PythonParser.HtmlTextElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#htmlDoctype}.
	 * @param ctx the parse tree
	 */
	void enterHtmlDoctype(PythonParser.HtmlDoctypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#htmlDoctype}.
	 * @param ctx the parse tree
	 */
	void exitHtmlDoctype(PythonParser.HtmlDoctypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StyleHtmlTag}
	 * labeled alternative in {@link PythonParser#htmlTag}.
	 * @param ctx the parse tree
	 */
	void enterStyleHtmlTag(PythonParser.StyleHtmlTagContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StyleHtmlTag}
	 * labeled alternative in {@link PythonParser#htmlTag}.
	 * @param ctx the parse tree
	 */
	void exitStyleHtmlTag(PythonParser.StyleHtmlTagContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GenericHtmlTag}
	 * labeled alternative in {@link PythonParser#htmlTag}.
	 * @param ctx the parse tree
	 */
	void enterGenericHtmlTag(PythonParser.GenericHtmlTagContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GenericHtmlTag}
	 * labeled alternative in {@link PythonParser#htmlTag}.
	 * @param ctx the parse tree
	 */
	void exitGenericHtmlTag(PythonParser.GenericHtmlTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#styleTag}.
	 * @param ctx the parse tree
	 */
	void enterStyleTag(PythonParser.StyleTagContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#styleTag}.
	 * @param ctx the parse tree
	 */
	void exitStyleTag(PythonParser.StyleTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#genericHtml}.
	 * @param ctx the parse tree
	 */
	void enterGenericHtml(PythonParser.GenericHtmlContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#genericHtml}.
	 * @param ctx the parse tree
	 */
	void exitGenericHtml(PythonParser.GenericHtmlContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#selfClosingTag}.
	 * @param ctx the parse tree
	 */
	void enterSelfClosingTag(PythonParser.SelfClosingTagContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#selfClosingTag}.
	 * @param ctx the parse tree
	 */
	void exitSelfClosingTag(PythonParser.SelfClosingTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#htmlAttributes}.
	 * @param ctx the parse tree
	 */
	void enterHtmlAttributes(PythonParser.HtmlAttributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#htmlAttributes}.
	 * @param ctx the parse tree
	 */
	void exitHtmlAttributes(PythonParser.HtmlAttributesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AttrNameId}
	 * labeled alternative in {@link PythonParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void enterAttrNameId(PythonParser.AttrNameIdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AttrNameId}
	 * labeled alternative in {@link PythonParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void exitAttrNameId(PythonParser.AttrNameIdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AttrNameClass}
	 * labeled alternative in {@link PythonParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void enterAttrNameClass(PythonParser.AttrNameClassContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AttrNameClass}
	 * labeled alternative in {@link PythonParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void exitAttrNameClass(PythonParser.AttrNameClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#attributeValue}.
	 * @param ctx the parse tree
	 */
	void enterAttributeValue(PythonParser.AttributeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#attributeValue}.
	 * @param ctx the parse tree
	 */
	void exitAttributeValue(PythonParser.AttributeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#htmlBody}.
	 * @param ctx the parse tree
	 */
	void enterHtmlBody(PythonParser.HtmlBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#htmlBody}.
	 * @param ctx the parse tree
	 */
	void exitHtmlBody(PythonParser.HtmlBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#htmlText}.
	 * @param ctx the parse tree
	 */
	void enterHtmlText(PythonParser.HtmlTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#htmlText}.
	 * @param ctx the parse tree
	 */
	void exitHtmlText(PythonParser.HtmlTextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CssBlock}
	 * labeled alternative in {@link PythonParser#css}.
	 * @param ctx the parse tree
	 */
	void enterCssBlock(PythonParser.CssBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CssBlock}
	 * labeled alternative in {@link PythonParser#css}.
	 * @param ctx the parse tree
	 */
	void exitCssBlock(PythonParser.CssBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CssAnnotation}
	 * labeled alternative in {@link PythonParser#css}.
	 * @param ctx the parse tree
	 */
	void enterCssAnnotation(PythonParser.CssAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CssAnnotation}
	 * labeled alternative in {@link PythonParser#css}.
	 * @param ctx the parse tree
	 */
	void exitCssAnnotation(PythonParser.CssAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#cssSelector}.
	 * @param ctx the parse tree
	 */
	void enterCssSelector(PythonParser.CssSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#cssSelector}.
	 * @param ctx the parse tree
	 */
	void exitCssSelector(PythonParser.CssSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#cssKeyValue}.
	 * @param ctx the parse tree
	 */
	void enterCssKeyValue(PythonParser.CssKeyValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#cssKeyValue}.
	 * @param ctx the parse tree
	 */
	void exitCssKeyValue(PythonParser.CssKeyValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#cssKey}.
	 * @param ctx the parse tree
	 */
	void enterCssKey(PythonParser.CssKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#cssKey}.
	 * @param ctx the parse tree
	 */
	void exitCssKey(PythonParser.CssKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cssVNumber}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 */
	void enterCssVNumber(PythonParser.CssVNumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cssVNumber}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 */
	void exitCssVNumber(PythonParser.CssVNumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cssVId}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 */
	void enterCssVId(PythonParser.CssVIdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cssVId}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 */
	void exitCssVId(PythonParser.CssVIdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cssVColor}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 */
	void enterCssVColor(PythonParser.CssVColorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cssVColor}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 */
	void exitCssVColor(PythonParser.CssVColorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cssVStr}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 */
	void enterCssVStr(PythonParser.CssVStrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cssVStr}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 */
	void exitCssVStr(PythonParser.CssVStrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cssVJinja}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 */
	void enterCssVJinja(PythonParser.CssVJinjaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cssVJinja}
	 * labeled alternative in {@link PythonParser#cssValue}.
	 * @param ctx the parse tree
	 */
	void exitCssVJinja(PythonParser.CssVJinjaContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#cssComment}.
	 * @param ctx the parse tree
	 */
	void enterCssComment(PythonParser.CssCommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#cssComment}.
	 * @param ctx the parse tree
	 */
	void exitCssComment(PythonParser.CssCommentContext ctx);
}