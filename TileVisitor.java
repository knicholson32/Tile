// Generated from Tile.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TileParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TileVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TileParser#parse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParse(TileParser.ParseContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(TileParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(TileParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listFunctionStatment}
	 * labeled alternative in {@link TileParser#functionStatment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListFunctionStatment(TileParser.ListFunctionStatmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#importStatmemt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportStatmemt(TileParser.ImportStatmemtContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#runStatment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRunStatment(TileParser.RunStatmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code regularAssignment}
	 * labeled alternative in {@link TileParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegularAssignment(TileParser.RegularAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code singularAssignment}
	 * labeled alternative in {@link TileParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingularAssignment(TileParser.SingularAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fullAssignment}
	 * labeled alternative in {@link TileParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullAssignment(TileParser.FullAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code structAssignment}
	 * labeled alternative in {@link TileParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructAssignment(TileParser.StructAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierFunctionCall}
	 * labeled alternative in {@link TileParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierFunctionCall(TileParser.IdentifierFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assertFunctionCall}
	 * labeled alternative in {@link TileParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssertFunctionCall(TileParser.AssertFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(TileParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#ifStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStat(TileParser.IfStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#elseIfStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfStat(TileParser.ElseIfStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#elseStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStat(TileParser.ElseStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#functionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDecl(TileParser.FunctionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#structDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructDecl(TileParser.StructDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forStepStatment}
	 * labeled alternative in {@link TileParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStepStatment(TileParser.ForStepStatmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forSTDStatment}
	 * labeled alternative in {@link TileParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForSTDStatment(TileParser.ForSTDStatmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forEachStatment}
	 * labeled alternative in {@link TileParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForEachStatment(TileParser.ForEachStatmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(TileParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(TileParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#continueStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(TileParser.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#idList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdList(TileParser.IdListContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#exprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprList(TileParser.ExprListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code gtExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGtExpression(TileParser.GtExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code isaTypeExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsaTypeExpression(TileParser.IsaTypeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberExpression(TileParser.NumberExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExpression(TileParser.IdentifierExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(TileParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code gtEqExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGtEqExpression(TileParser.GtEqExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(TileParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringExpression(TileParser.StringExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionRefExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionRefExpression(TileParser.FunctionRefExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionExpression(TileParser.ExpressionExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullExpression(TileParser.NullExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCallExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallExpression(TileParser.FunctionCallExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListExpression(TileParser.ListExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ltEqExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLtEqExpression(TileParser.LtEqExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ltExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLtExpression(TileParser.LtExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolExpression(TileParser.BoolExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notEqExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotEqExpression(TileParser.NotEqExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code accessStructExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccessStructExpression(TileParser.AccessStructExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignmentExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentExpression(TileParser.AssignmentExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(TileParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryMinusExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinusExpression(TileParser.UnaryMinusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code powerExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowerExpression(TileParser.PowerExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multiplyDivideModExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplyDivideModExpression(TileParser.MultiplyDivideModExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code eqExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExpression(TileParser.EqExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInExpression(TileParser.InExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addSubExpression}
	 * labeled alternative in {@link TileParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSubExpression(TileParser.AddSubExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#listFunctions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListFunctions(TileParser.ListFunctionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(TileParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#indexes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexes(TileParser.IndexesContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(TileParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#addSub}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(TileParser.AddSubContext ctx);
	/**
	 * Visit a parse tree produced by {@link TileParser#mulDivMod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivMod(TileParser.MulDivModContext ctx);
}