// Generated from /Users/pem/github/tom/src/tom/engine/parser/antlr4/TomIslandParser.g4 by ANTLR 4.5.3
package tom.engine.parser.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TomIslandParser}.
 */
public interface TomIslandParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(TomIslandParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(TomIslandParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#island}.
	 * @param ctx the parse tree
	 */
	void enterIsland(TomIslandParser.IslandContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#island}.
	 * @param ctx the parse tree
	 */
	void exitIsland(TomIslandParser.IslandContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#metaquote}.
	 * @param ctx the parse tree
	 */
	void enterMetaquote(TomIslandParser.MetaquoteContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#metaquote}.
	 * @param ctx the parse tree
	 */
	void exitMetaquote(TomIslandParser.MetaquoteContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#matchStatement}.
	 * @param ctx the parse tree
	 */
	void enterMatchStatement(TomIslandParser.MatchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#matchStatement}.
	 * @param ctx the parse tree
	 */
	void exitMatchStatement(TomIslandParser.MatchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#strategyStatement}.
	 * @param ctx the parse tree
	 */
	void enterStrategyStatement(TomIslandParser.StrategyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#strategyStatement}.
	 * @param ctx the parse tree
	 */
	void exitStrategyStatement(TomIslandParser.StrategyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#includeStatement}.
	 * @param ctx the parse tree
	 */
	void enterIncludeStatement(TomIslandParser.IncludeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#includeStatement}.
	 * @param ctx the parse tree
	 */
	void exitIncludeStatement(TomIslandParser.IncludeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#gomStatement}.
	 * @param ctx the parse tree
	 */
	void enterGomStatement(TomIslandParser.GomStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#gomStatement}.
	 * @param ctx the parse tree
	 */
	void exitGomStatement(TomIslandParser.GomStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#gomOptions}.
	 * @param ctx the parse tree
	 */
	void enterGomOptions(TomIslandParser.GomOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#gomOptions}.
	 * @param ctx the parse tree
	 */
	void exitGomOptions(TomIslandParser.GomOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#visit}.
	 * @param ctx the parse tree
	 */
	void enterVisit(TomIslandParser.VisitContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#visit}.
	 * @param ctx the parse tree
	 */
	void exitVisit(TomIslandParser.VisitContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#actionRule}.
	 * @param ctx the parse tree
	 */
	void enterActionRule(TomIslandParser.ActionRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#actionRule}.
	 * @param ctx the parse tree
	 */
	void exitActionRule(TomIslandParser.ActionRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(TomIslandParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(TomIslandParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#water}.
	 * @param ctx the parse tree
	 */
	void enterWater(TomIslandParser.WaterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#water}.
	 * @param ctx the parse tree
	 */
	void exitWater(TomIslandParser.WaterContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#slotList}.
	 * @param ctx the parse tree
	 */
	void enterSlotList(TomIslandParser.SlotListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#slotList}.
	 * @param ctx the parse tree
	 */
	void exitSlotList(TomIslandParser.SlotListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#slot}.
	 * @param ctx the parse tree
	 */
	void enterSlot(TomIslandParser.SlotContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#slot}.
	 * @param ctx the parse tree
	 */
	void exitSlot(TomIslandParser.SlotContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#patternlist}.
	 * @param ctx the parse tree
	 */
	void enterPatternlist(TomIslandParser.PatternlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#patternlist}.
	 * @param ctx the parse tree
	 */
	void exitPatternlist(TomIslandParser.PatternlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#constraint}.
	 * @param ctx the parse tree
	 */
	void enterConstraint(TomIslandParser.ConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#constraint}.
	 * @param ctx the parse tree
	 */
	void exitConstraint(TomIslandParser.ConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(TomIslandParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(TomIslandParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#bqterm}.
	 * @param ctx the parse tree
	 */
	void enterBqterm(TomIslandParser.BqtermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#bqterm}.
	 * @param ctx the parse tree
	 */
	void exitBqterm(TomIslandParser.BqtermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#pairSlotBqterm}.
	 * @param ctx the parse tree
	 */
	void enterPairSlotBqterm(TomIslandParser.PairSlotBqtermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#pairSlotBqterm}.
	 * @param ctx the parse tree
	 */
	void exitPairSlotBqterm(TomIslandParser.PairSlotBqtermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#bqcomposite}.
	 * @param ctx the parse tree
	 */
	void enterBqcomposite(TomIslandParser.BqcompositeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#bqcomposite}.
	 * @param ctx the parse tree
	 */
	void exitBqcomposite(TomIslandParser.BqcompositeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#composite}.
	 * @param ctx the parse tree
	 */
	void enterComposite(TomIslandParser.CompositeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#composite}.
	 * @param ctx the parse tree
	 */
	void exitComposite(TomIslandParser.CompositeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#waterexceptparen}.
	 * @param ctx the parse tree
	 */
	void enterWaterexceptparen(TomIslandParser.WaterexceptparenContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#waterexceptparen}.
	 * @param ctx the parse tree
	 */
	void exitWaterexceptparen(TomIslandParser.WaterexceptparenContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPattern(TomIslandParser.PatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPattern(TomIslandParser.PatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#fsymbol}.
	 * @param ctx the parse tree
	 */
	void enterFsymbol(TomIslandParser.FsymbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#fsymbol}.
	 * @param ctx the parse tree
	 */
	void exitFsymbol(TomIslandParser.FsymbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#headSymbol}.
	 * @param ctx the parse tree
	 */
	void enterHeadSymbol(TomIslandParser.HeadSymbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#headSymbol}.
	 * @param ctx the parse tree
	 */
	void exitHeadSymbol(TomIslandParser.HeadSymbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(TomIslandParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(TomIslandParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#explicitArgs}.
	 * @param ctx the parse tree
	 */
	void enterExplicitArgs(TomIslandParser.ExplicitArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#explicitArgs}.
	 * @param ctx the parse tree
	 */
	void exitExplicitArgs(TomIslandParser.ExplicitArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#implicitArgs}.
	 * @param ctx the parse tree
	 */
	void enterImplicitArgs(TomIslandParser.ImplicitArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#implicitArgs}.
	 * @param ctx the parse tree
	 */
	void exitImplicitArgs(TomIslandParser.ImplicitArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#typeterm}.
	 * @param ctx the parse tree
	 */
	void enterTypeterm(TomIslandParser.TypetermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#typeterm}.
	 * @param ctx the parse tree
	 */
	void exitTypeterm(TomIslandParser.TypetermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(TomIslandParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(TomIslandParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#oplist}.
	 * @param ctx the parse tree
	 */
	void enterOplist(TomIslandParser.OplistContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#oplist}.
	 * @param ctx the parse tree
	 */
	void exitOplist(TomIslandParser.OplistContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#oparray}.
	 * @param ctx the parse tree
	 */
	void enterOparray(TomIslandParser.OparrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#oparray}.
	 * @param ctx the parse tree
	 */
	void exitOparray(TomIslandParser.OparrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#implement}.
	 * @param ctx the parse tree
	 */
	void enterImplement(TomIslandParser.ImplementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#implement}.
	 * @param ctx the parse tree
	 */
	void exitImplement(TomIslandParser.ImplementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#equalsTerm}.
	 * @param ctx the parse tree
	 */
	void enterEqualsTerm(TomIslandParser.EqualsTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#equalsTerm}.
	 * @param ctx the parse tree
	 */
	void exitEqualsTerm(TomIslandParser.EqualsTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#isSort}.
	 * @param ctx the parse tree
	 */
	void enterIsSort(TomIslandParser.IsSortContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#isSort}.
	 * @param ctx the parse tree
	 */
	void exitIsSort(TomIslandParser.IsSortContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#isFsym}.
	 * @param ctx the parse tree
	 */
	void enterIsFsym(TomIslandParser.IsFsymContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#isFsym}.
	 * @param ctx the parse tree
	 */
	void exitIsFsym(TomIslandParser.IsFsymContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#make}.
	 * @param ctx the parse tree
	 */
	void enterMake(TomIslandParser.MakeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#make}.
	 * @param ctx the parse tree
	 */
	void exitMake(TomIslandParser.MakeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#makeEmptyList}.
	 * @param ctx the parse tree
	 */
	void enterMakeEmptyList(TomIslandParser.MakeEmptyListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#makeEmptyList}.
	 * @param ctx the parse tree
	 */
	void exitMakeEmptyList(TomIslandParser.MakeEmptyListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#makeEmptyArray}.
	 * @param ctx the parse tree
	 */
	void enterMakeEmptyArray(TomIslandParser.MakeEmptyArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#makeEmptyArray}.
	 * @param ctx the parse tree
	 */
	void exitMakeEmptyArray(TomIslandParser.MakeEmptyArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#makeAppendArray}.
	 * @param ctx the parse tree
	 */
	void enterMakeAppendArray(TomIslandParser.MakeAppendArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#makeAppendArray}.
	 * @param ctx the parse tree
	 */
	void exitMakeAppendArray(TomIslandParser.MakeAppendArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#makeInsertList}.
	 * @param ctx the parse tree
	 */
	void enterMakeInsertList(TomIslandParser.MakeInsertListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#makeInsertList}.
	 * @param ctx the parse tree
	 */
	void exitMakeInsertList(TomIslandParser.MakeInsertListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#getSlot}.
	 * @param ctx the parse tree
	 */
	void enterGetSlot(TomIslandParser.GetSlotContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#getSlot}.
	 * @param ctx the parse tree
	 */
	void exitGetSlot(TomIslandParser.GetSlotContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#getHead}.
	 * @param ctx the parse tree
	 */
	void enterGetHead(TomIslandParser.GetHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#getHead}.
	 * @param ctx the parse tree
	 */
	void exitGetHead(TomIslandParser.GetHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#getTail}.
	 * @param ctx the parse tree
	 */
	void enterGetTail(TomIslandParser.GetTailContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#getTail}.
	 * @param ctx the parse tree
	 */
	void exitGetTail(TomIslandParser.GetTailContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#getElement}.
	 * @param ctx the parse tree
	 */
	void enterGetElement(TomIslandParser.GetElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#getElement}.
	 * @param ctx the parse tree
	 */
	void exitGetElement(TomIslandParser.GetElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#isEmptyList}.
	 * @param ctx the parse tree
	 */
	void enterIsEmptyList(TomIslandParser.IsEmptyListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#isEmptyList}.
	 * @param ctx the parse tree
	 */
	void exitIsEmptyList(TomIslandParser.IsEmptyListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#getSize}.
	 * @param ctx the parse tree
	 */
	void enterGetSize(TomIslandParser.GetSizeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#getSize}.
	 * @param ctx the parse tree
	 */
	void exitGetSize(TomIslandParser.GetSizeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomIslandParser#getDefault}.
	 * @param ctx the parse tree
	 */
	void enterGetDefault(TomIslandParser.GetDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomIslandParser#getDefault}.
	 * @param ctx the parse tree
	 */
	void exitGetDefault(TomIslandParser.GetDefaultContext ctx);
}