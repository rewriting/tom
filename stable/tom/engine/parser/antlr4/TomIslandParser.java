// Generated from /Users/pem/github/tom/src/tom/engine/parser/antlr4/TomIslandParser.g4 by ANTLR 4.5.3
package tom.engine.parser.antlr4;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TomIslandParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		MATCH=1, STRATEGY=2, EXTENDS=3, VISIT=4, INCLUDE=5, GOM=6, OP=7, OPARRAY=8, 
		OPLIST=9, TYPETERM=10, IS_FSYM=11, IS_SORT=12, MAKE=13, MAKE_EMPTY=14, 
		MAKE_APPEND=15, MAKE_INSERT=16, GET_SLOT=17, GET_DEFAULT=18, GET_ELEMENT=19, 
		GET_HEAD=20, GET_TAIL=21, GET_SIZE=22, IS_EMPTY=23, IMPLEMENT=24, EQUALS=25, 
		MATCH_SYMBOL=26, EQUAL=27, LBRACE=28, RBRACE=29, LPAREN=30, RPAREN=31, 
		LSQUAREBR=32, RSQUAREBR=33, ARROW=34, COMMA=35, COLON=36, STAR=37, UNDERSCORE=38, 
		ANTI=39, AT=40, LMETAQUOTE=41, RMETAQUOTE=42, ATAT=43, GREATEROREQ=44, 
		LOWEROREQ=45, GREATERTHAN=46, LOWERTHAN=47, DOUBLEEQ=48, DIFFERENT=49, 
		AND=50, OR=51, PIPE=52, QMARK=53, DQMARK=54, SLASH=55, BACKSLASH=56, DOT=57, 
		BQUOTE=58, ID=59, DMINUSID=60, INTEGER=61, DOUBLE=62, LONG=63, STRING=64, 
		CHAR=65, ACTION_ESCAPE=66, ACTION_STRING_LITERAL=67, MLCOMMENT=68, SLCOMMENT=69, 
		WS=70, ANY=71;
	public static final int
		RULE_start = 0, RULE_island = 1, RULE_metaquote = 2, RULE_matchStatement = 3, 
		RULE_strategyStatement = 4, RULE_includeStatement = 5, RULE_gomStatement = 6, 
		RULE_gomOptions = 7, RULE_visit = 8, RULE_actionRule = 9, RULE_block = 10, 
		RULE_water = 11, RULE_slotList = 12, RULE_slot = 13, RULE_patternlist = 14, 
		RULE_constraint = 15, RULE_term = 16, RULE_bqterm = 17, RULE_pairSlotBqterm = 18, 
		RULE_bqcomposite = 19, RULE_composite = 20, RULE_waterexceptparen = 21, 
		RULE_pattern = 22, RULE_fsymbol = 23, RULE_headSymbol = 24, RULE_constant = 25, 
		RULE_explicitArgs = 26, RULE_implicitArgs = 27, RULE_typeterm = 28, RULE_operator = 29, 
		RULE_oplist = 30, RULE_oparray = 31, RULE_implement = 32, RULE_equalsTerm = 33, 
		RULE_isSort = 34, RULE_isFsym = 35, RULE_make = 36, RULE_makeEmptyList = 37, 
		RULE_makeEmptyArray = 38, RULE_makeAppendArray = 39, RULE_makeInsertList = 40, 
		RULE_getSlot = 41, RULE_getHead = 42, RULE_getTail = 43, RULE_getElement = 44, 
		RULE_isEmptyList = 45, RULE_getSize = 46, RULE_getDefault = 47;
	public static final String[] ruleNames = {
		"start", "island", "metaquote", "matchStatement", "strategyStatement", 
		"includeStatement", "gomStatement", "gomOptions", "visit", "actionRule", 
		"block", "water", "slotList", "slot", "patternlist", "constraint", "term", 
		"bqterm", "pairSlotBqterm", "bqcomposite", "composite", "waterexceptparen", 
		"pattern", "fsymbol", "headSymbol", "constant", "explicitArgs", "implicitArgs", 
		"typeterm", "operator", "oplist", "oparray", "implement", "equalsTerm", 
		"isSort", "isFsym", "make", "makeEmptyList", "makeEmptyArray", "makeAppendArray", 
		"makeInsertList", "getSlot", "getHead", "getTail", "getElement", "isEmptyList", 
		"getSize", "getDefault"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'%match'", "'%strategy'", "'extends'", "'visit'", "'%include'", 
		"'%gom'", "'%op'", "'%oparray'", "'%oplist'", "'%typeterm'", "'is_fsym'", 
		"'is_sort'", "'make'", "'make_empty'", "'make_append'", "'make_insert'", 
		"'get_slot'", "'get_default'", "'get_element'", "'get_head'", "'get_tail'", 
		"'get_size'", "'is_empty'", "'implement'", "'equals'", "'<<'", "'='", 
		"'{'", "'}'", "'('", "')'", "'['", "']'", "'->'", "','", "':'", "'*'", 
		"'_'", "'!'", "'@'", "'%['", "']%'", "'@@'", "'>='", "'<='", "'>'", "'<'", 
		"'=='", "'!='", "'&&'", "'||'", "'|'", "'?'", "'??'", "'/'", "'\\'", "'.'", 
		"'`'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "MATCH", "STRATEGY", "EXTENDS", "VISIT", "INCLUDE", "GOM", "OP", 
		"OPARRAY", "OPLIST", "TYPETERM", "IS_FSYM", "IS_SORT", "MAKE", "MAKE_EMPTY", 
		"MAKE_APPEND", "MAKE_INSERT", "GET_SLOT", "GET_DEFAULT", "GET_ELEMENT", 
		"GET_HEAD", "GET_TAIL", "GET_SIZE", "IS_EMPTY", "IMPLEMENT", "EQUALS", 
		"MATCH_SYMBOL", "EQUAL", "LBRACE", "RBRACE", "LPAREN", "RPAREN", "LSQUAREBR", 
		"RSQUAREBR", "ARROW", "COMMA", "COLON", "STAR", "UNDERSCORE", "ANTI", 
		"AT", "LMETAQUOTE", "RMETAQUOTE", "ATAT", "GREATEROREQ", "LOWEROREQ", 
		"GREATERTHAN", "LOWERTHAN", "DOUBLEEQ", "DIFFERENT", "AND", "OR", "PIPE", 
		"QMARK", "DQMARK", "SLASH", "BACKSLASH", "DOT", "BQUOTE", "ID", "DMINUSID", 
		"INTEGER", "DOUBLE", "LONG", "STRING", "CHAR", "ACTION_ESCAPE", "ACTION_STRING_LITERAL", 
		"MLCOMMENT", "SLCOMMENT", "WS", "ANY"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "TomIslandParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TomIslandParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class StartContext extends ParserRuleContext {
		public List<IslandContext> island() {
			return getRuleContexts(IslandContext.class);
		}
		public IslandContext island(int i) {
			return getRuleContext(IslandContext.class,i);
		}
		public List<WaterContext> water() {
			return getRuleContexts(WaterContext.class);
		}
		public WaterContext water(int i) {
			return getRuleContext(WaterContext.class,i);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitStart(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					setState(98);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
					case 1:
						{
						setState(96);
						island();
						}
						break;
					case 2:
						{
						setState(97);
						water();
						}
						break;
					}
					} 
				}
				setState(102);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IslandContext extends ParserRuleContext {
		public MatchStatementContext matchStatement() {
			return getRuleContext(MatchStatementContext.class,0);
		}
		public StrategyStatementContext strategyStatement() {
			return getRuleContext(StrategyStatementContext.class,0);
		}
		public IncludeStatementContext includeStatement() {
			return getRuleContext(IncludeStatementContext.class,0);
		}
		public GomStatementContext gomStatement() {
			return getRuleContext(GomStatementContext.class,0);
		}
		public TypetermContext typeterm() {
			return getRuleContext(TypetermContext.class,0);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public OplistContext oplist() {
			return getRuleContext(OplistContext.class,0);
		}
		public OparrayContext oparray() {
			return getRuleContext(OparrayContext.class,0);
		}
		public BqcompositeContext bqcomposite() {
			return getRuleContext(BqcompositeContext.class,0);
		}
		public MetaquoteContext metaquote() {
			return getRuleContext(MetaquoteContext.class,0);
		}
		public IslandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_island; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterIsland(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitIsland(this);
		}
	}

	public final IslandContext island() throws RecognitionException {
		IslandContext _localctx = new IslandContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_island);
		try {
			setState(113);
			switch (_input.LA(1)) {
			case MATCH:
				enterOuterAlt(_localctx, 1);
				{
				setState(103);
				matchStatement();
				}
				break;
			case STRATEGY:
				enterOuterAlt(_localctx, 2);
				{
				setState(104);
				strategyStatement();
				}
				break;
			case INCLUDE:
				enterOuterAlt(_localctx, 3);
				{
				setState(105);
				includeStatement();
				}
				break;
			case GOM:
				enterOuterAlt(_localctx, 4);
				{
				setState(106);
				gomStatement();
				}
				break;
			case TYPETERM:
				enterOuterAlt(_localctx, 5);
				{
				setState(107);
				typeterm();
				}
				break;
			case OP:
				enterOuterAlt(_localctx, 6);
				{
				setState(108);
				operator();
				}
				break;
			case OPLIST:
				enterOuterAlt(_localctx, 7);
				{
				setState(109);
				oplist();
				}
				break;
			case OPARRAY:
				enterOuterAlt(_localctx, 8);
				{
				setState(110);
				oparray();
				}
				break;
			case BQUOTE:
				enterOuterAlt(_localctx, 9);
				{
				setState(111);
				bqcomposite();
				}
				break;
			case LMETAQUOTE:
				enterOuterAlt(_localctx, 10);
				{
				setState(112);
				metaquote();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MetaquoteContext extends ParserRuleContext {
		public TerminalNode LMETAQUOTE() { return getToken(TomIslandParser.LMETAQUOTE, 0); }
		public TerminalNode RMETAQUOTE() { return getToken(TomIslandParser.RMETAQUOTE, 0); }
		public List<TerminalNode> AT() { return getTokens(TomIslandParser.AT); }
		public TerminalNode AT(int i) {
			return getToken(TomIslandParser.AT, i);
		}
		public List<WaterContext> water() {
			return getRuleContexts(WaterContext.class);
		}
		public WaterContext water(int i) {
			return getRuleContext(WaterContext.class,i);
		}
		public List<CompositeContext> composite() {
			return getRuleContexts(CompositeContext.class);
		}
		public CompositeContext composite(int i) {
			return getRuleContext(CompositeContext.class,i);
		}
		public List<BqcompositeContext> bqcomposite() {
			return getRuleContexts(BqcompositeContext.class);
		}
		public BqcompositeContext bqcomposite(int i) {
			return getRuleContext(BqcompositeContext.class,i);
		}
		public MetaquoteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metaquote; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterMetaquote(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitMetaquote(this);
		}
	}

	public final MetaquoteContext metaquote() throws RecognitionException {
		MetaquoteContext _localctx = new MetaquoteContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_metaquote);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			match(LMETAQUOTE);
			setState(126);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(124);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						setState(116);
						match(AT);
						setState(119);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
						case 1:
							{
							setState(117);
							composite();
							}
							break;
						case 2:
							{
							setState(118);
							bqcomposite();
							}
							break;
						}
						setState(121);
						match(AT);
						}
						break;
					case 2:
						{
						setState(123);
						water();
						}
						break;
					}
					} 
				}
				setState(128);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(129);
			match(RMETAQUOTE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MatchStatementContext extends ParserRuleContext {
		public TerminalNode MATCH() { return getToken(TomIslandParser.MATCH, 0); }
		public TerminalNode LBRACE() { return getToken(TomIslandParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomIslandParser.RBRACE, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public List<ActionRuleContext> actionRule() {
			return getRuleContexts(ActionRuleContext.class);
		}
		public ActionRuleContext actionRule(int i) {
			return getRuleContext(ActionRuleContext.class,i);
		}
		public List<BqtermContext> bqterm() {
			return getRuleContexts(BqtermContext.class);
		}
		public BqtermContext bqterm(int i) {
			return getRuleContext(BqtermContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomIslandParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomIslandParser.COMMA, i);
		}
		public MatchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterMatchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitMatchStatement(this);
		}
	}

	public final MatchStatementContext matchStatement() throws RecognitionException {
		MatchStatementContext _localctx = new MatchStatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_matchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131);
			match(MATCH);
			setState(144);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(132);
				match(LPAREN);
				setState(141);
				_la = _input.LA(1);
				if (((((_la - 38)) & ~0x3f) == 0 && ((1L << (_la - 38)) & ((1L << (UNDERSCORE - 38)) | (1L << (BQUOTE - 38)) | (1L << (ID - 38)) | (1L << (INTEGER - 38)) | (1L << (DOUBLE - 38)) | (1L << (LONG - 38)) | (1L << (STRING - 38)) | (1L << (CHAR - 38)))) != 0)) {
					{
					setState(133);
					bqterm();
					setState(138);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(134);
						match(COMMA);
						setState(135);
						bqterm();
						}
						}
						setState(140);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(143);
				match(RPAREN);
				}
			}

			setState(146);
			match(LBRACE);
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (LPAREN - 30)) | (1L << (UNDERSCORE - 30)) | (1L << (ANTI - 30)) | (1L << (ID - 30)) | (1L << (INTEGER - 30)) | (1L << (DOUBLE - 30)) | (1L << (LONG - 30)) | (1L << (STRING - 30)) | (1L << (CHAR - 30)))) != 0)) {
				{
				{
				setState(147);
				actionRule();
				}
				}
				setState(152);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(153);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StrategyStatementContext extends ParserRuleContext {
		public TerminalNode STRATEGY() { return getToken(TomIslandParser.STRATEGY, 0); }
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public TerminalNode EXTENDS() { return getToken(TomIslandParser.EXTENDS, 0); }
		public BqtermContext bqterm() {
			return getRuleContext(BqtermContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(TomIslandParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomIslandParser.RBRACE, 0); }
		public SlotListContext slotList() {
			return getRuleContext(SlotListContext.class,0);
		}
		public List<VisitContext> visit() {
			return getRuleContexts(VisitContext.class);
		}
		public VisitContext visit(int i) {
			return getRuleContext(VisitContext.class,i);
		}
		public StrategyStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_strategyStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterStrategyStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitStrategyStatement(this);
		}
	}

	public final StrategyStatementContext strategyStatement() throws RecognitionException {
		StrategyStatementContext _localctx = new StrategyStatementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_strategyStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			match(STRATEGY);
			setState(156);
			match(ID);
			setState(157);
			match(LPAREN);
			setState(159);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(158);
				slotList();
				}
			}

			setState(161);
			match(RPAREN);
			setState(162);
			match(EXTENDS);
			setState(163);
			bqterm();
			setState(164);
			match(LBRACE);
			setState(168);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VISIT) {
				{
				{
				setState(165);
				visit();
				}
				}
				setState(170);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(171);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IncludeStatementContext extends ParserRuleContext {
		public TerminalNode INCLUDE() { return getToken(TomIslandParser.INCLUDE, 0); }
		public TerminalNode LBRACE() { return getToken(TomIslandParser.LBRACE, 0); }
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public TerminalNode RBRACE() { return getToken(TomIslandParser.RBRACE, 0); }
		public TerminalNode DOT() { return getToken(TomIslandParser.DOT, 0); }
		public List<TerminalNode> SLASH() { return getTokens(TomIslandParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(TomIslandParser.SLASH, i);
		}
		public List<TerminalNode> BACKSLASH() { return getTokens(TomIslandParser.BACKSLASH); }
		public TerminalNode BACKSLASH(int i) {
			return getToken(TomIslandParser.BACKSLASH, i);
		}
		public IncludeStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_includeStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterIncludeStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitIncludeStatement(this);
		}
	}

	public final IncludeStatementContext includeStatement() throws RecognitionException {
		IncludeStatementContext _localctx = new IncludeStatementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_includeStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			match(INCLUDE);
			setState(174);
			match(LBRACE);
			setState(175);
			match(ID);
			setState(180);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SLASH || _la==BACKSLASH) {
				{
				{
				setState(176);
				_la = _input.LA(1);
				if ( !(_la==SLASH || _la==BACKSLASH) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(177);
				match(ID);
				}
				}
				setState(182);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(185);
			_la = _input.LA(1);
			if (_la==DOT) {
				{
				setState(183);
				match(DOT);
				setState(184);
				match(ID);
				}
			}

			setState(187);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GomStatementContext extends ParserRuleContext {
		public TerminalNode GOM() { return getToken(TomIslandParser.GOM, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public GomOptionsContext gomOptions() {
			return getRuleContext(GomOptionsContext.class,0);
		}
		public GomStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gomStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterGomStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitGomStatement(this);
		}
	}

	public final GomStatementContext gomStatement() throws RecognitionException {
		GomStatementContext _localctx = new GomStatementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_gomStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			match(GOM);
			setState(191);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(190);
				gomOptions();
				}
			}

			setState(193);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GomOptionsContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public List<TerminalNode> DMINUSID() { return getTokens(TomIslandParser.DMINUSID); }
		public TerminalNode DMINUSID(int i) {
			return getToken(TomIslandParser.DMINUSID, i);
		}
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(TomIslandParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomIslandParser.COMMA, i);
		}
		public GomOptionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gomOptions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterGomOptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitGomOptions(this);
		}
	}

	public final GomOptionsContext gomOptions() throws RecognitionException {
		GomOptionsContext _localctx = new GomOptionsContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_gomOptions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(LPAREN);
			setState(196);
			match(DMINUSID);
			setState(201);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(197);
				match(COMMA);
				setState(198);
				match(DMINUSID);
				}
				}
				setState(203);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(204);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VisitContext extends ParserRuleContext {
		public TerminalNode VISIT() { return getToken(TomIslandParser.VISIT, 0); }
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode LBRACE() { return getToken(TomIslandParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomIslandParser.RBRACE, 0); }
		public List<ActionRuleContext> actionRule() {
			return getRuleContexts(ActionRuleContext.class);
		}
		public ActionRuleContext actionRule(int i) {
			return getRuleContext(ActionRuleContext.class,i);
		}
		public VisitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_visit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterVisit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitVisit(this);
		}
	}

	public final VisitContext visit() throws RecognitionException {
		VisitContext _localctx = new VisitContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_visit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			match(VISIT);
			setState(207);
			match(ID);
			setState(208);
			match(LBRACE);
			setState(212);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (LPAREN - 30)) | (1L << (UNDERSCORE - 30)) | (1L << (ANTI - 30)) | (1L << (ID - 30)) | (1L << (INTEGER - 30)) | (1L << (DOUBLE - 30)) | (1L << (LONG - 30)) | (1L << (STRING - 30)) | (1L << (CHAR - 30)))) != 0)) {
				{
				{
				setState(209);
				actionRule();
				}
				}
				setState(214);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(215);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ActionRuleContext extends ParserRuleContext {
		public ConstraintContext c;
		public PatternlistContext patternlist() {
			return getRuleContext(PatternlistContext.class,0);
		}
		public TerminalNode ARROW() { return getToken(TomIslandParser.ARROW, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ConstraintContext constraint() {
			return getRuleContext(ConstraintContext.class,0);
		}
		public TerminalNode AND() { return getToken(TomIslandParser.AND, 0); }
		public TerminalNode OR() { return getToken(TomIslandParser.OR, 0); }
		public BqtermContext bqterm() {
			return getRuleContext(BqtermContext.class,0);
		}
		public ActionRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actionRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterActionRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitActionRule(this);
		}
	}

	public final ActionRuleContext actionRule() throws RecognitionException {
		ActionRuleContext _localctx = new ActionRuleContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_actionRule);
		int _la;
		try {
			setState(241);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(217);
				patternlist();
				setState(220);
				_la = _input.LA(1);
				if (_la==AND || _la==OR) {
					{
					setState(218);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(219);
					constraint(0);
					}
				}

				setState(222);
				match(ARROW);
				setState(223);
				block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(225);
				patternlist();
				setState(228);
				_la = _input.LA(1);
				if (_la==AND || _la==OR) {
					{
					setState(226);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(227);
					constraint(0);
					}
				}

				setState(230);
				match(ARROW);
				setState(231);
				bqterm();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(233);
				((ActionRuleContext)_localctx).c = constraint(0);
				setState(234);
				match(ARROW);
				setState(235);
				block();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(237);
				((ActionRuleContext)_localctx).c = constraint(0);
				setState(238);
				match(ARROW);
				setState(239);
				bqterm();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(TomIslandParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomIslandParser.RBRACE, 0); }
		public List<IslandContext> island() {
			return getRuleContexts(IslandContext.class);
		}
		public IslandContext island(int i) {
			return getRuleContext(IslandContext.class,i);
		}
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public List<WaterContext> water() {
			return getRuleContexts(WaterContext.class);
		}
		public WaterContext water(int i) {
			return getRuleContext(WaterContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitBlock(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_block);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			match(LBRACE);
			setState(249);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					setState(247);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
					case 1:
						{
						setState(244);
						island();
						}
						break;
					case 2:
						{
						setState(245);
						block();
						}
						break;
					case 3:
						{
						setState(246);
						water();
						}
						break;
					}
					} 
				}
				setState(251);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			}
			setState(252);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WaterContext extends ParserRuleContext {
		public WaterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_water; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterWater(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitWater(this);
		}
	}

	public final WaterContext water() throws RecognitionException {
		WaterContext _localctx = new WaterContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_water);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			matchWildcard();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SlotListContext extends ParserRuleContext {
		public List<SlotContext> slot() {
			return getRuleContexts(SlotContext.class);
		}
		public SlotContext slot(int i) {
			return getRuleContext(SlotContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomIslandParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomIslandParser.COMMA, i);
		}
		public SlotListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_slotList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterSlotList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitSlotList(this);
		}
	}

	public final SlotListContext slotList() throws RecognitionException {
		SlotListContext _localctx = new SlotListContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_slotList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			slot();
			setState(261);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(257);
				match(COMMA);
				setState(258);
				slot();
				}
				}
				setState(263);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SlotContext extends ParserRuleContext {
		public Token id1;
		public Token id2;
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public TerminalNode COLON() { return getToken(TomIslandParser.COLON, 0); }
		public SlotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_slot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterSlot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitSlot(this);
		}
	}

	public final SlotContext slot() throws RecognitionException {
		SlotContext _localctx = new SlotContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_slot);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(264);
			((SlotContext)_localctx).id1 = match(ID);
			setState(266);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(265);
				match(COLON);
				}
			}

			setState(268);
			((SlotContext)_localctx).id2 = match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PatternlistContext extends ParserRuleContext {
		public List<PatternContext> pattern() {
			return getRuleContexts(PatternContext.class);
		}
		public PatternContext pattern(int i) {
			return getRuleContext(PatternContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomIslandParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomIslandParser.COMMA, i);
		}
		public PatternlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_patternlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterPatternlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitPatternlist(this);
		}
	}

	public final PatternlistContext patternlist() throws RecognitionException {
		PatternlistContext _localctx = new PatternlistContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_patternlist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(270);
			pattern();
			setState(275);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(271);
				match(COMMA);
				setState(272);
				pattern();
				}
				}
				setState(277);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstraintContext extends ParserRuleContext {
		public ConstraintContext c;
		public PatternContext pattern() {
			return getRuleContext(PatternContext.class,0);
		}
		public TerminalNode MATCH_SYMBOL() { return getToken(TomIslandParser.MATCH_SYMBOL, 0); }
		public BqtermContext bqterm() {
			return getRuleContext(BqtermContext.class,0);
		}
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public TerminalNode GREATERTHAN() { return getToken(TomIslandParser.GREATERTHAN, 0); }
		public TerminalNode GREATEROREQ() { return getToken(TomIslandParser.GREATEROREQ, 0); }
		public TerminalNode LOWERTHAN() { return getToken(TomIslandParser.LOWERTHAN, 0); }
		public TerminalNode LOWEROREQ() { return getToken(TomIslandParser.LOWEROREQ, 0); }
		public TerminalNode DOUBLEEQ() { return getToken(TomIslandParser.DOUBLEEQ, 0); }
		public TerminalNode DIFFERENT() { return getToken(TomIslandParser.DIFFERENT, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public List<ConstraintContext> constraint() {
			return getRuleContexts(ConstraintContext.class);
		}
		public ConstraintContext constraint(int i) {
			return getRuleContext(ConstraintContext.class,i);
		}
		public TerminalNode AND() { return getToken(TomIslandParser.AND, 0); }
		public TerminalNode OR() { return getToken(TomIslandParser.OR, 0); }
		public ConstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitConstraint(this);
		}
	}

	public final ConstraintContext constraint() throws RecognitionException {
		return constraint(0);
	}

	private ConstraintContext constraint(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ConstraintContext _localctx = new ConstraintContext(_ctx, _parentState);
		ConstraintContext _prevctx = _localctx;
		int _startState = 30;
		enterRecursionRule(_localctx, 30, RULE_constraint, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				{
				setState(279);
				pattern();
				setState(280);
				match(MATCH_SYMBOL);
				setState(281);
				bqterm();
				}
				break;
			case 2:
				{
				setState(283);
				term();
				setState(284);
				match(GREATERTHAN);
				setState(285);
				term();
				}
				break;
			case 3:
				{
				setState(287);
				term();
				setState(288);
				match(GREATEROREQ);
				setState(289);
				term();
				}
				break;
			case 4:
				{
				setState(291);
				term();
				setState(292);
				match(LOWERTHAN);
				setState(293);
				term();
				}
				break;
			case 5:
				{
				setState(295);
				term();
				setState(296);
				match(LOWEROREQ);
				setState(297);
				term();
				}
				break;
			case 6:
				{
				setState(299);
				term();
				setState(300);
				match(DOUBLEEQ);
				setState(301);
				term();
				}
				break;
			case 7:
				{
				setState(303);
				term();
				setState(304);
				match(DIFFERENT);
				setState(305);
				term();
				}
				break;
			case 8:
				{
				setState(307);
				match(LPAREN);
				setState(308);
				((ConstraintContext)_localctx).c = constraint(0);
				setState(309);
				match(RPAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(321);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(319);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
					case 1:
						{
						_localctx = new ConstraintContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_constraint);
						setState(313);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(314);
						match(AND);
						setState(315);
						constraint(11);
						}
						break;
					case 2:
						{
						_localctx = new ConstraintContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_constraint);
						setState(316);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(317);
						match(OR);
						setState(318);
						constraint(10);
						}
						break;
					}
					} 
				}
				setState(323);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public Token var;
		public Token fsym;
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode STAR() { return getToken(TomIslandParser.STAR, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomIslandParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomIslandParser.COMMA, i);
		}
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitTerm(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_term);
		int _la;
		try {
			setState(342);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(324);
				((TermContext)_localctx).var = match(ID);
				setState(326);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
				case 1:
					{
					setState(325);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(328);
				((TermContext)_localctx).fsym = match(ID);
				setState(329);
				match(LPAREN);
				setState(338);
				_la = _input.LA(1);
				if (((((_la - 59)) & ~0x3f) == 0 && ((1L << (_la - 59)) & ((1L << (ID - 59)) | (1L << (INTEGER - 59)) | (1L << (DOUBLE - 59)) | (1L << (LONG - 59)) | (1L << (STRING - 59)) | (1L << (CHAR - 59)))) != 0)) {
					{
					setState(330);
					term();
					setState(335);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(331);
						match(COMMA);
						setState(332);
						term();
						}
						}
						setState(337);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(340);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(341);
				constant();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BqtermContext extends ParserRuleContext {
		public Token codomain;
		public Token fsym;
		public Token var;
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public TerminalNode BQUOTE() { return getToken(TomIslandParser.BQUOTE, 0); }
		public List<BqtermContext> bqterm() {
			return getRuleContexts(BqtermContext.class);
		}
		public BqtermContext bqterm(int i) {
			return getRuleContext(BqtermContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomIslandParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomIslandParser.COMMA, i);
		}
		public TerminalNode LSQUAREBR() { return getToken(TomIslandParser.LSQUAREBR, 0); }
		public TerminalNode RSQUAREBR() { return getToken(TomIslandParser.RSQUAREBR, 0); }
		public List<PairSlotBqtermContext> pairSlotBqterm() {
			return getRuleContexts(PairSlotBqtermContext.class);
		}
		public PairSlotBqtermContext pairSlotBqterm(int i) {
			return getRuleContext(PairSlotBqtermContext.class,i);
		}
		public TerminalNode STAR() { return getToken(TomIslandParser.STAR, 0); }
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public TerminalNode UNDERSCORE() { return getToken(TomIslandParser.UNDERSCORE, 0); }
		public BqtermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bqterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterBqterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitBqterm(this);
		}
	}

	public final BqtermContext bqterm() throws RecognitionException {
		BqtermContext _localctx = new BqtermContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_bqterm);
		int _la;
		try {
			setState(397);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(345);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
				case 1:
					{
					setState(344);
					((BqtermContext)_localctx).codomain = match(ID);
					}
					break;
				}
				setState(348);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(347);
					match(BQUOTE);
					}
				}

				setState(350);
				((BqtermContext)_localctx).fsym = match(ID);
				setState(351);
				match(LPAREN);
				setState(360);
				_la = _input.LA(1);
				if (((((_la - 38)) & ~0x3f) == 0 && ((1L << (_la - 38)) & ((1L << (UNDERSCORE - 38)) | (1L << (BQUOTE - 38)) | (1L << (ID - 38)) | (1L << (INTEGER - 38)) | (1L << (DOUBLE - 38)) | (1L << (LONG - 38)) | (1L << (STRING - 38)) | (1L << (CHAR - 38)))) != 0)) {
					{
					setState(352);
					bqterm();
					setState(357);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(353);
						match(COMMA);
						setState(354);
						bqterm();
						}
						}
						setState(359);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(362);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(364);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
				case 1:
					{
					setState(363);
					((BqtermContext)_localctx).codomain = match(ID);
					}
					break;
				}
				setState(367);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(366);
					match(BQUOTE);
					}
				}

				setState(369);
				((BqtermContext)_localctx).fsym = match(ID);
				setState(370);
				match(LSQUAREBR);
				setState(379);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(371);
					pairSlotBqterm();
					setState(376);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(372);
						match(COMMA);
						setState(373);
						pairSlotBqterm();
						}
						}
						setState(378);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(381);
				match(RSQUAREBR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(383);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
				case 1:
					{
					setState(382);
					((BqtermContext)_localctx).codomain = match(ID);
					}
					break;
				}
				setState(386);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(385);
					match(BQUOTE);
					}
				}

				setState(388);
				((BqtermContext)_localctx).var = match(ID);
				setState(390);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
				case 1:
					{
					setState(389);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(393);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(392);
					((BqtermContext)_localctx).codomain = match(ID);
					}
				}

				setState(395);
				constant();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(396);
				match(UNDERSCORE);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairSlotBqtermContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode EQUAL() { return getToken(TomIslandParser.EQUAL, 0); }
		public BqtermContext bqterm() {
			return getRuleContext(BqtermContext.class,0);
		}
		public PairSlotBqtermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pairSlotBqterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterPairSlotBqterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitPairSlotBqterm(this);
		}
	}

	public final PairSlotBqtermContext pairSlotBqterm() throws RecognitionException {
		PairSlotBqtermContext _localctx = new PairSlotBqtermContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_pairSlotBqterm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(399);
			match(ID);
			setState(400);
			match(EQUAL);
			setState(401);
			bqterm();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BqcompositeContext extends ParserRuleContext {
		public Token fsym;
		public TerminalNode BQUOTE() { return getToken(TomIslandParser.BQUOTE, 0); }
		public TerminalNode LSQUAREBR() { return getToken(TomIslandParser.LSQUAREBR, 0); }
		public TerminalNode RSQUAREBR() { return getToken(TomIslandParser.RSQUAREBR, 0); }
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public List<PairSlotBqtermContext> pairSlotBqterm() {
			return getRuleContexts(PairSlotBqtermContext.class);
		}
		public PairSlotBqtermContext pairSlotBqterm(int i) {
			return getRuleContext(PairSlotBqtermContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomIslandParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomIslandParser.COMMA, i);
		}
		public CompositeContext composite() {
			return getRuleContext(CompositeContext.class,0);
		}
		public BqcompositeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bqcomposite; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterBqcomposite(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitBqcomposite(this);
		}
	}

	public final BqcompositeContext bqcomposite() throws RecognitionException {
		BqcompositeContext _localctx = new BqcompositeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_bqcomposite);
		int _la;
		try {
			setState(419);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(403);
				match(BQUOTE);
				setState(404);
				((BqcompositeContext)_localctx).fsym = match(ID);
				setState(405);
				match(LSQUAREBR);
				setState(414);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(406);
					pairSlotBqterm();
					setState(411);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(407);
						match(COMMA);
						setState(408);
						pairSlotBqterm();
						}
						}
						setState(413);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(416);
				match(RSQUAREBR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(417);
				match(BQUOTE);
				setState(418);
				composite();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CompositeContext extends ParserRuleContext {
		public Token fsym;
		public Token var;
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public List<CompositeContext> composite() {
			return getRuleContexts(CompositeContext.class);
		}
		public CompositeContext composite(int i) {
			return getRuleContext(CompositeContext.class,i);
		}
		public TerminalNode STAR() { return getToken(TomIslandParser.STAR, 0); }
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public TerminalNode UNDERSCORE() { return getToken(TomIslandParser.UNDERSCORE, 0); }
		public WaterexceptparenContext waterexceptparen() {
			return getRuleContext(WaterexceptparenContext.class,0);
		}
		public CompositeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_composite; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterComposite(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitComposite(this);
		}
	}

	public final CompositeContext composite() throws RecognitionException {
		CompositeContext _localctx = new CompositeContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_composite);
		int _la;
		try {
			setState(445);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(421);
				((CompositeContext)_localctx).fsym = match(ID);
				setState(422);
				match(LPAREN);
				setState(426);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MATCH) | (1L << STRATEGY) | (1L << EXTENDS) | (1L << VISIT) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE) | (1L << MAKE_EMPTY) | (1L << MAKE_APPEND) | (1L << MAKE_INSERT) | (1L << GET_SLOT) | (1L << GET_DEFAULT) | (1L << GET_ELEMENT) | (1L << GET_HEAD) | (1L << GET_TAIL) | (1L << GET_SIZE) | (1L << IS_EMPTY) | (1L << IMPLEMENT) | (1L << EQUALS) | (1L << MATCH_SYMBOL) | (1L << EQUAL) | (1L << LBRACE) | (1L << RBRACE) | (1L << LPAREN) | (1L << LSQUAREBR) | (1L << RSQUAREBR) | (1L << ARROW) | (1L << COMMA) | (1L << COLON) | (1L << STAR) | (1L << UNDERSCORE) | (1L << ANTI) | (1L << AT) | (1L << LMETAQUOTE) | (1L << RMETAQUOTE) | (1L << ATAT) | (1L << GREATEROREQ) | (1L << LOWEROREQ) | (1L << GREATERTHAN) | (1L << LOWERTHAN) | (1L << DOUBLEEQ) | (1L << DIFFERENT) | (1L << AND) | (1L << OR) | (1L << PIPE) | (1L << QMARK) | (1L << DQMARK) | (1L << SLASH) | (1L << BACKSLASH) | (1L << DOT) | (1L << BQUOTE) | (1L << ID) | (1L << DMINUSID) | (1L << INTEGER) | (1L << DOUBLE) | (1L << LONG))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (STRING - 64)) | (1L << (CHAR - 64)) | (1L << (ACTION_ESCAPE - 64)) | (1L << (ACTION_STRING_LITERAL - 64)) | (1L << (MLCOMMENT - 64)) | (1L << (SLCOMMENT - 64)) | (1L << (WS - 64)) | (1L << (ANY - 64)))) != 0)) {
					{
					{
					setState(423);
					composite();
					}
					}
					setState(428);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(429);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(430);
				match(LPAREN);
				setState(434);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MATCH) | (1L << STRATEGY) | (1L << EXTENDS) | (1L << VISIT) | (1L << INCLUDE) | (1L << GOM) | (1L << OP) | (1L << OPARRAY) | (1L << OPLIST) | (1L << TYPETERM) | (1L << IS_FSYM) | (1L << IS_SORT) | (1L << MAKE) | (1L << MAKE_EMPTY) | (1L << MAKE_APPEND) | (1L << MAKE_INSERT) | (1L << GET_SLOT) | (1L << GET_DEFAULT) | (1L << GET_ELEMENT) | (1L << GET_HEAD) | (1L << GET_TAIL) | (1L << GET_SIZE) | (1L << IS_EMPTY) | (1L << IMPLEMENT) | (1L << EQUALS) | (1L << MATCH_SYMBOL) | (1L << EQUAL) | (1L << LBRACE) | (1L << RBRACE) | (1L << LPAREN) | (1L << LSQUAREBR) | (1L << RSQUAREBR) | (1L << ARROW) | (1L << COMMA) | (1L << COLON) | (1L << STAR) | (1L << UNDERSCORE) | (1L << ANTI) | (1L << AT) | (1L << LMETAQUOTE) | (1L << RMETAQUOTE) | (1L << ATAT) | (1L << GREATEROREQ) | (1L << LOWEROREQ) | (1L << GREATERTHAN) | (1L << LOWERTHAN) | (1L << DOUBLEEQ) | (1L << DIFFERENT) | (1L << AND) | (1L << OR) | (1L << PIPE) | (1L << QMARK) | (1L << DQMARK) | (1L << SLASH) | (1L << BACKSLASH) | (1L << DOT) | (1L << BQUOTE) | (1L << ID) | (1L << DMINUSID) | (1L << INTEGER) | (1L << DOUBLE) | (1L << LONG))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (STRING - 64)) | (1L << (CHAR - 64)) | (1L << (ACTION_ESCAPE - 64)) | (1L << (ACTION_STRING_LITERAL - 64)) | (1L << (MLCOMMENT - 64)) | (1L << (SLCOMMENT - 64)) | (1L << (WS - 64)) | (1L << (ANY - 64)))) != 0)) {
					{
					{
					setState(431);
					composite();
					}
					}
					setState(436);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(437);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(438);
				((CompositeContext)_localctx).var = match(ID);
				setState(440);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
				case 1:
					{
					setState(439);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(442);
				constant();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(443);
				match(UNDERSCORE);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(444);
				waterexceptparen();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WaterexceptparenContext extends ParserRuleContext {
		public List<TerminalNode> LPAREN() { return getTokens(TomIslandParser.LPAREN); }
		public TerminalNode LPAREN(int i) {
			return getToken(TomIslandParser.LPAREN, i);
		}
		public List<TerminalNode> RPAREN() { return getTokens(TomIslandParser.RPAREN); }
		public TerminalNode RPAREN(int i) {
			return getToken(TomIslandParser.RPAREN, i);
		}
		public WaterexceptparenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_waterexceptparen; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterWaterexceptparen(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitWaterexceptparen(this);
		}
	}

	public final WaterexceptparenContext waterexceptparen() throws RecognitionException {
		WaterexceptparenContext _localctx = new WaterexceptparenContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_waterexceptparen);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(448); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(447);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==LPAREN || _la==RPAREN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(450); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PatternContext extends ParserRuleContext {
		public Token var;
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode AT() { return getToken(TomIslandParser.AT, 0); }
		public PatternContext pattern() {
			return getRuleContext(PatternContext.class,0);
		}
		public TerminalNode ANTI() { return getToken(TomIslandParser.ANTI, 0); }
		public FsymbolContext fsymbol() {
			return getRuleContext(FsymbolContext.class,0);
		}
		public ExplicitArgsContext explicitArgs() {
			return getRuleContext(ExplicitArgsContext.class,0);
		}
		public ImplicitArgsContext implicitArgs() {
			return getRuleContext(ImplicitArgsContext.class,0);
		}
		public TerminalNode STAR() { return getToken(TomIslandParser.STAR, 0); }
		public TerminalNode UNDERSCORE() { return getToken(TomIslandParser.UNDERSCORE, 0); }
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public PatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitPattern(this);
		}
	}

	public final PatternContext pattern() throws RecognitionException {
		PatternContext _localctx = new PatternContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_pattern);
		int _la;
		try {
			setState(475);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(452);
				match(ID);
				setState(453);
				match(AT);
				setState(454);
				pattern();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(455);
				match(ANTI);
				setState(456);
				pattern();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(457);
				fsymbol();
				setState(458);
				explicitArgs();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(460);
				fsymbol();
				setState(461);
				implicitArgs();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(463);
				((PatternContext)_localctx).var = match(ID);
				setState(465);
				_la = _input.LA(1);
				if (_la==STAR) {
					{
					setState(464);
					match(STAR);
					}
				}

				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(467);
				match(UNDERSCORE);
				setState(469);
				_la = _input.LA(1);
				if (_la==STAR) {
					{
					setState(468);
					match(STAR);
					}
				}

				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(471);
				constant();
				setState(473);
				_la = _input.LA(1);
				if (_la==STAR) {
					{
					setState(472);
					match(STAR);
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FsymbolContext extends ParserRuleContext {
		public List<HeadSymbolContext> headSymbol() {
			return getRuleContexts(HeadSymbolContext.class);
		}
		public HeadSymbolContext headSymbol(int i) {
			return getRuleContext(HeadSymbolContext.class,i);
		}
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public List<TerminalNode> PIPE() { return getTokens(TomIslandParser.PIPE); }
		public TerminalNode PIPE(int i) {
			return getToken(TomIslandParser.PIPE, i);
		}
		public FsymbolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fsymbol; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterFsymbol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitFsymbol(this);
		}
	}

	public final FsymbolContext fsymbol() throws RecognitionException {
		FsymbolContext _localctx = new FsymbolContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_fsymbol);
		int _la;
		try {
			setState(489);
			switch (_input.LA(1)) {
			case ID:
			case INTEGER:
			case DOUBLE:
			case LONG:
			case STRING:
			case CHAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(477);
				headSymbol();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(478);
				match(LPAREN);
				setState(479);
				headSymbol();
				setState(484);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PIPE) {
					{
					{
					setState(480);
					match(PIPE);
					setState(481);
					headSymbol();
					}
					}
					setState(486);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(487);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeadSymbolContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode QMARK() { return getToken(TomIslandParser.QMARK, 0); }
		public TerminalNode DQMARK() { return getToken(TomIslandParser.DQMARK, 0); }
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public HeadSymbolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_headSymbol; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterHeadSymbol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitHeadSymbol(this);
		}
	}

	public final HeadSymbolContext headSymbol() throws RecognitionException {
		HeadSymbolContext _localctx = new HeadSymbolContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_headSymbol);
		int _la;
		try {
			setState(500);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(491);
				match(ID);
				setState(493);
				_la = _input.LA(1);
				if (_la==QMARK) {
					{
					setState(492);
					match(QMARK);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(495);
				match(ID);
				setState(497);
				_la = _input.LA(1);
				if (_la==DQMARK) {
					{
					setState(496);
					match(DQMARK);
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(499);
				constant();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(TomIslandParser.INTEGER, 0); }
		public TerminalNode LONG() { return getToken(TomIslandParser.LONG, 0); }
		public TerminalNode CHAR() { return getToken(TomIslandParser.CHAR, 0); }
		public TerminalNode DOUBLE() { return getToken(TomIslandParser.DOUBLE, 0); }
		public TerminalNode STRING() { return getToken(TomIslandParser.STRING, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitConstant(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_constant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(502);
			_la = _input.LA(1);
			if ( !(((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (INTEGER - 61)) | (1L << (DOUBLE - 61)) | (1L << (LONG - 61)) | (1L << (STRING - 61)) | (1L << (CHAR - 61)))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExplicitArgsContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public List<PatternContext> pattern() {
			return getRuleContexts(PatternContext.class);
		}
		public PatternContext pattern(int i) {
			return getRuleContext(PatternContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomIslandParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomIslandParser.COMMA, i);
		}
		public ExplicitArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitArgs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterExplicitArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitExplicitArgs(this);
		}
	}

	public final ExplicitArgsContext explicitArgs() throws RecognitionException {
		ExplicitArgsContext _localctx = new ExplicitArgsContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_explicitArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(504);
			match(LPAREN);
			setState(513);
			_la = _input.LA(1);
			if (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (LPAREN - 30)) | (1L << (UNDERSCORE - 30)) | (1L << (ANTI - 30)) | (1L << (ID - 30)) | (1L << (INTEGER - 30)) | (1L << (DOUBLE - 30)) | (1L << (LONG - 30)) | (1L << (STRING - 30)) | (1L << (CHAR - 30)))) != 0)) {
				{
				setState(505);
				pattern();
				setState(510);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(506);
					match(COMMA);
					setState(507);
					pattern();
					}
					}
					setState(512);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(515);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImplicitArgsContext extends ParserRuleContext {
		public TerminalNode LSQUAREBR() { return getToken(TomIslandParser.LSQUAREBR, 0); }
		public TerminalNode RSQUAREBR() { return getToken(TomIslandParser.RSQUAREBR, 0); }
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public List<TerminalNode> EQUAL() { return getTokens(TomIslandParser.EQUAL); }
		public TerminalNode EQUAL(int i) {
			return getToken(TomIslandParser.EQUAL, i);
		}
		public List<PatternContext> pattern() {
			return getRuleContexts(PatternContext.class);
		}
		public PatternContext pattern(int i) {
			return getRuleContext(PatternContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomIslandParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomIslandParser.COMMA, i);
		}
		public ImplicitArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_implicitArgs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterImplicitArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitImplicitArgs(this);
		}
	}

	public final ImplicitArgsContext implicitArgs() throws RecognitionException {
		ImplicitArgsContext _localctx = new ImplicitArgsContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_implicitArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(517);
			match(LSQUAREBR);
			setState(530);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(518);
				match(ID);
				setState(519);
				match(EQUAL);
				setState(520);
				pattern();
				setState(527);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(521);
					match(COMMA);
					setState(522);
					match(ID);
					setState(523);
					match(EQUAL);
					setState(524);
					pattern();
					}
					}
					setState(529);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(532);
			match(RSQUAREBR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypetermContext extends ParserRuleContext {
		public Token type;
		public Token supertype;
		public TerminalNode TYPETERM() { return getToken(TomIslandParser.TYPETERM, 0); }
		public TerminalNode LBRACE() { return getToken(TomIslandParser.LBRACE, 0); }
		public ImplementContext implement() {
			return getRuleContext(ImplementContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(TomIslandParser.RBRACE, 0); }
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public TerminalNode EXTENDS() { return getToken(TomIslandParser.EXTENDS, 0); }
		public IsSortContext isSort() {
			return getRuleContext(IsSortContext.class,0);
		}
		public EqualsTermContext equalsTerm() {
			return getRuleContext(EqualsTermContext.class,0);
		}
		public TypetermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterTypeterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitTypeterm(this);
		}
	}

	public final TypetermContext typeterm() throws RecognitionException {
		TypetermContext _localctx = new TypetermContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_typeterm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(534);
			match(TYPETERM);
			setState(535);
			((TypetermContext)_localctx).type = match(ID);
			setState(538);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(536);
				match(EXTENDS);
				setState(537);
				((TypetermContext)_localctx).supertype = match(ID);
				}
			}

			setState(540);
			match(LBRACE);
			setState(541);
			implement();
			setState(543);
			_la = _input.LA(1);
			if (_la==IS_SORT) {
				{
				setState(542);
				isSort();
				}
			}

			setState(546);
			_la = _input.LA(1);
			if (_la==EQUALS) {
				{
				setState(545);
				equalsTerm();
				}
			}

			setState(548);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperatorContext extends ParserRuleContext {
		public Token codomain;
		public Token opname;
		public TerminalNode OP() { return getToken(TomIslandParser.OP, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public TerminalNode LBRACE() { return getToken(TomIslandParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomIslandParser.RBRACE, 0); }
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public SlotListContext slotList() {
			return getRuleContext(SlotListContext.class,0);
		}
		public List<IsFsymContext> isFsym() {
			return getRuleContexts(IsFsymContext.class);
		}
		public IsFsymContext isFsym(int i) {
			return getRuleContext(IsFsymContext.class,i);
		}
		public List<MakeContext> make() {
			return getRuleContexts(MakeContext.class);
		}
		public MakeContext make(int i) {
			return getRuleContext(MakeContext.class,i);
		}
		public List<GetSlotContext> getSlot() {
			return getRuleContexts(GetSlotContext.class);
		}
		public GetSlotContext getSlot(int i) {
			return getRuleContext(GetSlotContext.class,i);
		}
		public List<GetDefaultContext> getDefault() {
			return getRuleContexts(GetDefaultContext.class);
		}
		public GetDefaultContext getDefault(int i) {
			return getRuleContext(GetDefaultContext.class,i);
		}
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitOperator(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(550);
			match(OP);
			setState(551);
			((OperatorContext)_localctx).codomain = match(ID);
			setState(552);
			((OperatorContext)_localctx).opname = match(ID);
			setState(553);
			match(LPAREN);
			setState(555);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(554);
				slotList();
				}
			}

			setState(557);
			match(RPAREN);
			setState(558);
			match(LBRACE);
			setState(565);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IS_FSYM) | (1L << MAKE) | (1L << GET_SLOT) | (1L << GET_DEFAULT))) != 0)) {
				{
				setState(563);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(559);
					isFsym();
					}
					break;
				case MAKE:
					{
					setState(560);
					make();
					}
					break;
				case GET_SLOT:
					{
					setState(561);
					getSlot();
					}
					break;
				case GET_DEFAULT:
					{
					setState(562);
					getDefault();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(567);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(568);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OplistContext extends ParserRuleContext {
		public Token codomain;
		public Token opname;
		public Token domain;
		public TerminalNode OPLIST() { return getToken(TomIslandParser.OPLIST, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode STAR() { return getToken(TomIslandParser.STAR, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public TerminalNode LBRACE() { return getToken(TomIslandParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomIslandParser.RBRACE, 0); }
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public List<IsFsymContext> isFsym() {
			return getRuleContexts(IsFsymContext.class);
		}
		public IsFsymContext isFsym(int i) {
			return getRuleContext(IsFsymContext.class,i);
		}
		public List<MakeEmptyListContext> makeEmptyList() {
			return getRuleContexts(MakeEmptyListContext.class);
		}
		public MakeEmptyListContext makeEmptyList(int i) {
			return getRuleContext(MakeEmptyListContext.class,i);
		}
		public List<MakeInsertListContext> makeInsertList() {
			return getRuleContexts(MakeInsertListContext.class);
		}
		public MakeInsertListContext makeInsertList(int i) {
			return getRuleContext(MakeInsertListContext.class,i);
		}
		public List<GetHeadContext> getHead() {
			return getRuleContexts(GetHeadContext.class);
		}
		public GetHeadContext getHead(int i) {
			return getRuleContext(GetHeadContext.class,i);
		}
		public List<GetTailContext> getTail() {
			return getRuleContexts(GetTailContext.class);
		}
		public GetTailContext getTail(int i) {
			return getRuleContext(GetTailContext.class,i);
		}
		public List<IsEmptyListContext> isEmptyList() {
			return getRuleContexts(IsEmptyListContext.class);
		}
		public IsEmptyListContext isEmptyList(int i) {
			return getRuleContext(IsEmptyListContext.class,i);
		}
		public OplistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oplist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterOplist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitOplist(this);
		}
	}

	public final OplistContext oplist() throws RecognitionException {
		OplistContext _localctx = new OplistContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_oplist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(570);
			match(OPLIST);
			setState(571);
			((OplistContext)_localctx).codomain = match(ID);
			setState(572);
			((OplistContext)_localctx).opname = match(ID);
			setState(573);
			match(LPAREN);
			setState(574);
			((OplistContext)_localctx).domain = match(ID);
			setState(575);
			match(STAR);
			setState(576);
			match(RPAREN);
			setState(577);
			match(LBRACE);
			setState(586);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IS_FSYM) | (1L << MAKE_EMPTY) | (1L << MAKE_INSERT) | (1L << GET_HEAD) | (1L << GET_TAIL) | (1L << IS_EMPTY))) != 0)) {
				{
				setState(584);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(578);
					isFsym();
					}
					break;
				case MAKE_EMPTY:
					{
					setState(579);
					makeEmptyList();
					}
					break;
				case MAKE_INSERT:
					{
					setState(580);
					makeInsertList();
					}
					break;
				case GET_HEAD:
					{
					setState(581);
					getHead();
					}
					break;
				case GET_TAIL:
					{
					setState(582);
					getTail();
					}
					break;
				case IS_EMPTY:
					{
					setState(583);
					isEmptyList();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(588);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(589);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OparrayContext extends ParserRuleContext {
		public Token codomain;
		public Token opname;
		public Token domain;
		public TerminalNode OPARRAY() { return getToken(TomIslandParser.OPARRAY, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode STAR() { return getToken(TomIslandParser.STAR, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public TerminalNode LBRACE() { return getToken(TomIslandParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(TomIslandParser.RBRACE, 0); }
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public List<IsFsymContext> isFsym() {
			return getRuleContexts(IsFsymContext.class);
		}
		public IsFsymContext isFsym(int i) {
			return getRuleContext(IsFsymContext.class,i);
		}
		public List<MakeEmptyArrayContext> makeEmptyArray() {
			return getRuleContexts(MakeEmptyArrayContext.class);
		}
		public MakeEmptyArrayContext makeEmptyArray(int i) {
			return getRuleContext(MakeEmptyArrayContext.class,i);
		}
		public List<MakeAppendArrayContext> makeAppendArray() {
			return getRuleContexts(MakeAppendArrayContext.class);
		}
		public MakeAppendArrayContext makeAppendArray(int i) {
			return getRuleContext(MakeAppendArrayContext.class,i);
		}
		public List<GetElementContext> getElement() {
			return getRuleContexts(GetElementContext.class);
		}
		public GetElementContext getElement(int i) {
			return getRuleContext(GetElementContext.class,i);
		}
		public List<GetSizeContext> getSize() {
			return getRuleContexts(GetSizeContext.class);
		}
		public GetSizeContext getSize(int i) {
			return getRuleContext(GetSizeContext.class,i);
		}
		public OparrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oparray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterOparray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitOparray(this);
		}
	}

	public final OparrayContext oparray() throws RecognitionException {
		OparrayContext _localctx = new OparrayContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_oparray);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(591);
			match(OPARRAY);
			setState(592);
			((OparrayContext)_localctx).codomain = match(ID);
			setState(593);
			((OparrayContext)_localctx).opname = match(ID);
			setState(594);
			match(LPAREN);
			setState(595);
			((OparrayContext)_localctx).domain = match(ID);
			setState(596);
			match(STAR);
			setState(597);
			match(RPAREN);
			setState(598);
			match(LBRACE);
			setState(606);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IS_FSYM) | (1L << MAKE_EMPTY) | (1L << MAKE_APPEND) | (1L << GET_ELEMENT) | (1L << GET_SIZE))) != 0)) {
				{
				setState(604);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(599);
					isFsym();
					}
					break;
				case MAKE_EMPTY:
					{
					setState(600);
					makeEmptyArray();
					}
					break;
				case MAKE_APPEND:
					{
					setState(601);
					makeAppendArray();
					}
					break;
				case GET_ELEMENT:
					{
					setState(602);
					getElement();
					}
					break;
				case GET_SIZE:
					{
					setState(603);
					getSize();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(608);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(609);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImplementContext extends ParserRuleContext {
		public TerminalNode IMPLEMENT() { return getToken(TomIslandParser.IMPLEMENT, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ImplementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_implement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterImplement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitImplement(this);
		}
	}

	public final ImplementContext implement() throws RecognitionException {
		ImplementContext _localctx = new ImplementContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_implement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(611);
			match(IMPLEMENT);
			setState(612);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EqualsTermContext extends ParserRuleContext {
		public Token id1;
		public Token id2;
		public TerminalNode EQUALS() { return getToken(TomIslandParser.EQUALS, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(TomIslandParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public EqualsTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalsTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterEqualsTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitEqualsTerm(this);
		}
	}

	public final EqualsTermContext equalsTerm() throws RecognitionException {
		EqualsTermContext _localctx = new EqualsTermContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_equalsTerm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(614);
			match(EQUALS);
			setState(615);
			match(LPAREN);
			setState(616);
			((EqualsTermContext)_localctx).id1 = match(ID);
			setState(617);
			match(COMMA);
			setState(618);
			((EqualsTermContext)_localctx).id2 = match(ID);
			setState(619);
			match(RPAREN);
			setState(620);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IsSortContext extends ParserRuleContext {
		public TerminalNode IS_SORT() { return getToken(TomIslandParser.IS_SORT, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public IsSortContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isSort; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterIsSort(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitIsSort(this);
		}
	}

	public final IsSortContext isSort() throws RecognitionException {
		IsSortContext _localctx = new IsSortContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_isSort);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(622);
			match(IS_SORT);
			setState(623);
			match(LPAREN);
			setState(624);
			match(ID);
			setState(625);
			match(RPAREN);
			setState(626);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IsFsymContext extends ParserRuleContext {
		public TerminalNode IS_FSYM() { return getToken(TomIslandParser.IS_FSYM, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public IsFsymContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isFsym; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterIsFsym(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitIsFsym(this);
		}
	}

	public final IsFsymContext isFsym() throws RecognitionException {
		IsFsymContext _localctx = new IsFsymContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_isFsym);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(628);
			match(IS_FSYM);
			setState(629);
			match(LPAREN);
			setState(630);
			match(ID);
			setState(631);
			match(RPAREN);
			setState(632);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeContext extends ParserRuleContext {
		public TerminalNode MAKE() { return getToken(TomIslandParser.MAKE, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TomIslandParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TomIslandParser.COMMA, i);
		}
		public MakeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_make; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterMake(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitMake(this);
		}
	}

	public final MakeContext make() throws RecognitionException {
		MakeContext _localctx = new MakeContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_make);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(634);
			match(MAKE);
			setState(635);
			match(LPAREN);
			setState(644);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(636);
				match(ID);
				setState(641);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(637);
					match(COMMA);
					setState(638);
					match(ID);
					}
					}
					setState(643);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(646);
			match(RPAREN);
			setState(647);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeEmptyListContext extends ParserRuleContext {
		public TerminalNode MAKE_EMPTY() { return getToken(TomIslandParser.MAKE_EMPTY, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public MakeEmptyListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeEmptyList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterMakeEmptyList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitMakeEmptyList(this);
		}
	}

	public final MakeEmptyListContext makeEmptyList() throws RecognitionException {
		MakeEmptyListContext _localctx = new MakeEmptyListContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_makeEmptyList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(649);
			match(MAKE_EMPTY);
			setState(650);
			match(LPAREN);
			setState(651);
			match(RPAREN);
			setState(652);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeEmptyArrayContext extends ParserRuleContext {
		public TerminalNode MAKE_EMPTY() { return getToken(TomIslandParser.MAKE_EMPTY, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public MakeEmptyArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeEmptyArray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterMakeEmptyArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitMakeEmptyArray(this);
		}
	}

	public final MakeEmptyArrayContext makeEmptyArray() throws RecognitionException {
		MakeEmptyArrayContext _localctx = new MakeEmptyArrayContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_makeEmptyArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(654);
			match(MAKE_EMPTY);
			setState(655);
			match(LPAREN);
			setState(656);
			match(ID);
			setState(657);
			match(RPAREN);
			setState(658);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeAppendArrayContext extends ParserRuleContext {
		public Token id1;
		public Token id2;
		public TerminalNode MAKE_APPEND() { return getToken(TomIslandParser.MAKE_APPEND, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(TomIslandParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public MakeAppendArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeAppendArray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterMakeAppendArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitMakeAppendArray(this);
		}
	}

	public final MakeAppendArrayContext makeAppendArray() throws RecognitionException {
		MakeAppendArrayContext _localctx = new MakeAppendArrayContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_makeAppendArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(660);
			match(MAKE_APPEND);
			setState(661);
			match(LPAREN);
			setState(662);
			((MakeAppendArrayContext)_localctx).id1 = match(ID);
			setState(663);
			match(COMMA);
			setState(664);
			((MakeAppendArrayContext)_localctx).id2 = match(ID);
			setState(665);
			match(RPAREN);
			setState(666);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeInsertListContext extends ParserRuleContext {
		public Token id1;
		public Token id2;
		public TerminalNode MAKE_INSERT() { return getToken(TomIslandParser.MAKE_INSERT, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(TomIslandParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public MakeInsertListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeInsertList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterMakeInsertList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitMakeInsertList(this);
		}
	}

	public final MakeInsertListContext makeInsertList() throws RecognitionException {
		MakeInsertListContext _localctx = new MakeInsertListContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_makeInsertList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(668);
			match(MAKE_INSERT);
			setState(669);
			match(LPAREN);
			setState(670);
			((MakeInsertListContext)_localctx).id1 = match(ID);
			setState(671);
			match(COMMA);
			setState(672);
			((MakeInsertListContext)_localctx).id2 = match(ID);
			setState(673);
			match(RPAREN);
			setState(674);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GetSlotContext extends ParserRuleContext {
		public Token id1;
		public Token id2;
		public TerminalNode GET_SLOT() { return getToken(TomIslandParser.GET_SLOT, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(TomIslandParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public GetSlotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getSlot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterGetSlot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitGetSlot(this);
		}
	}

	public final GetSlotContext getSlot() throws RecognitionException {
		GetSlotContext _localctx = new GetSlotContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_getSlot);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(676);
			match(GET_SLOT);
			setState(677);
			match(LPAREN);
			setState(678);
			((GetSlotContext)_localctx).id1 = match(ID);
			setState(679);
			match(COMMA);
			setState(680);
			((GetSlotContext)_localctx).id2 = match(ID);
			setState(681);
			match(RPAREN);
			setState(682);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GetHeadContext extends ParserRuleContext {
		public TerminalNode GET_HEAD() { return getToken(TomIslandParser.GET_HEAD, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public GetHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterGetHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitGetHead(this);
		}
	}

	public final GetHeadContext getHead() throws RecognitionException {
		GetHeadContext _localctx = new GetHeadContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_getHead);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(684);
			match(GET_HEAD);
			setState(685);
			match(LPAREN);
			setState(686);
			match(ID);
			setState(687);
			match(RPAREN);
			setState(688);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GetTailContext extends ParserRuleContext {
		public TerminalNode GET_TAIL() { return getToken(TomIslandParser.GET_TAIL, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public GetTailContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getTail; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterGetTail(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitGetTail(this);
		}
	}

	public final GetTailContext getTail() throws RecognitionException {
		GetTailContext _localctx = new GetTailContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_getTail);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(690);
			match(GET_TAIL);
			setState(691);
			match(LPAREN);
			setState(692);
			match(ID);
			setState(693);
			match(RPAREN);
			setState(694);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GetElementContext extends ParserRuleContext {
		public Token id1;
		public Token id2;
		public TerminalNode GET_ELEMENT() { return getToken(TomIslandParser.GET_ELEMENT, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(TomIslandParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
		}
		public GetElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterGetElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitGetElement(this);
		}
	}

	public final GetElementContext getElement() throws RecognitionException {
		GetElementContext _localctx = new GetElementContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_getElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(696);
			match(GET_ELEMENT);
			setState(697);
			match(LPAREN);
			setState(698);
			((GetElementContext)_localctx).id1 = match(ID);
			setState(699);
			match(COMMA);
			setState(700);
			((GetElementContext)_localctx).id2 = match(ID);
			setState(701);
			match(RPAREN);
			setState(702);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IsEmptyListContext extends ParserRuleContext {
		public TerminalNode IS_EMPTY() { return getToken(TomIslandParser.IS_EMPTY, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public IsEmptyListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isEmptyList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterIsEmptyList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitIsEmptyList(this);
		}
	}

	public final IsEmptyListContext isEmptyList() throws RecognitionException {
		IsEmptyListContext _localctx = new IsEmptyListContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_isEmptyList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(704);
			match(IS_EMPTY);
			setState(705);
			match(LPAREN);
			setState(706);
			match(ID);
			setState(707);
			match(RPAREN);
			setState(708);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GetSizeContext extends ParserRuleContext {
		public TerminalNode GET_SIZE() { return getToken(TomIslandParser.GET_SIZE, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public GetSizeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getSize; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterGetSize(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitGetSize(this);
		}
	}

	public final GetSizeContext getSize() throws RecognitionException {
		GetSizeContext _localctx = new GetSizeContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_getSize);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(710);
			match(GET_SIZE);
			setState(711);
			match(LPAREN);
			setState(712);
			match(ID);
			setState(713);
			match(RPAREN);
			setState(714);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GetDefaultContext extends ParserRuleContext {
		public TerminalNode GET_DEFAULT() { return getToken(TomIslandParser.GET_DEFAULT, 0); }
		public TerminalNode LPAREN() { return getToken(TomIslandParser.LPAREN, 0); }
		public TerminalNode ID() { return getToken(TomIslandParser.ID, 0); }
		public TerminalNode RPAREN() { return getToken(TomIslandParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public GetDefaultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_getDefault; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).enterGetDefault(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TomIslandParserListener ) ((TomIslandParserListener)listener).exitGetDefault(this);
		}
	}

	public final GetDefaultContext getDefault() throws RecognitionException {
		GetDefaultContext _localctx = new GetDefaultContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_getDefault);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(716);
			match(GET_DEFAULT);
			setState(717);
			match(LPAREN);
			setState(718);
			match(ID);
			setState(719);
			match(RPAREN);
			setState(720);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 15:
			return constraint_sempred((ConstraintContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean constraint_sempred(ConstraintContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 10);
		case 1:
			return precpred(_ctx, 9);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3I\u02d5\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\3\2\3\2\7\2e\n\2\f\2\16\2h"+
		"\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3t\n\3\3\4\3\4\3\4\3\4"+
		"\5\4z\n\4\3\4\3\4\3\4\7\4\177\n\4\f\4\16\4\u0082\13\4\3\4\3\4\3\5\3\5"+
		"\3\5\3\5\3\5\7\5\u008b\n\5\f\5\16\5\u008e\13\5\5\5\u0090\n\5\3\5\5\5\u0093"+
		"\n\5\3\5\3\5\7\5\u0097\n\5\f\5\16\5\u009a\13\5\3\5\3\5\3\6\3\6\3\6\3\6"+
		"\5\6\u00a2\n\6\3\6\3\6\3\6\3\6\3\6\7\6\u00a9\n\6\f\6\16\6\u00ac\13\6\3"+
		"\6\3\6\3\7\3\7\3\7\3\7\3\7\7\7\u00b5\n\7\f\7\16\7\u00b8\13\7\3\7\3\7\5"+
		"\7\u00bc\n\7\3\7\3\7\3\b\3\b\5\b\u00c2\n\b\3\b\3\b\3\t\3\t\3\t\3\t\7\t"+
		"\u00ca\n\t\f\t\16\t\u00cd\13\t\3\t\3\t\3\n\3\n\3\n\3\n\7\n\u00d5\n\n\f"+
		"\n\16\n\u00d8\13\n\3\n\3\n\3\13\3\13\3\13\5\13\u00df\n\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\5\13\u00e7\n\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\5\13\u00f4\n\13\3\f\3\f\3\f\3\f\7\f\u00fa\n\f\f\f"+
		"\16\f\u00fd\13\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16\7\16\u0106\n\16\f\16\16"+
		"\16\u0109\13\16\3\17\3\17\5\17\u010d\n\17\3\17\3\17\3\20\3\20\3\20\7\20"+
		"\u0114\n\20\f\20\16\20\u0117\13\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u013a"+
		"\n\21\3\21\3\21\3\21\3\21\3\21\3\21\7\21\u0142\n\21\f\21\16\21\u0145\13"+
		"\21\3\22\3\22\5\22\u0149\n\22\3\22\3\22\3\22\3\22\3\22\7\22\u0150\n\22"+
		"\f\22\16\22\u0153\13\22\5\22\u0155\n\22\3\22\3\22\5\22\u0159\n\22\3\23"+
		"\5\23\u015c\n\23\3\23\5\23\u015f\n\23\3\23\3\23\3\23\3\23\3\23\7\23\u0166"+
		"\n\23\f\23\16\23\u0169\13\23\5\23\u016b\n\23\3\23\3\23\5\23\u016f\n\23"+
		"\3\23\5\23\u0172\n\23\3\23\3\23\3\23\3\23\3\23\7\23\u0179\n\23\f\23\16"+
		"\23\u017c\13\23\5\23\u017e\n\23\3\23\3\23\5\23\u0182\n\23\3\23\5\23\u0185"+
		"\n\23\3\23\3\23\5\23\u0189\n\23\3\23\5\23\u018c\n\23\3\23\3\23\5\23\u0190"+
		"\n\23\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\7\25\u019c\n\25"+
		"\f\25\16\25\u019f\13\25\5\25\u01a1\n\25\3\25\3\25\3\25\5\25\u01a6\n\25"+
		"\3\26\3\26\3\26\7\26\u01ab\n\26\f\26\16\26\u01ae\13\26\3\26\3\26\3\26"+
		"\7\26\u01b3\n\26\f\26\16\26\u01b6\13\26\3\26\3\26\3\26\5\26\u01bb\n\26"+
		"\3\26\3\26\3\26\5\26\u01c0\n\26\3\27\6\27\u01c3\n\27\r\27\16\27\u01c4"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\5\30"+
		"\u01d4\n\30\3\30\3\30\5\30\u01d8\n\30\3\30\3\30\5\30\u01dc\n\30\5\30\u01de"+
		"\n\30\3\31\3\31\3\31\3\31\3\31\7\31\u01e5\n\31\f\31\16\31\u01e8\13\31"+
		"\3\31\3\31\5\31\u01ec\n\31\3\32\3\32\5\32\u01f0\n\32\3\32\3\32\5\32\u01f4"+
		"\n\32\3\32\5\32\u01f7\n\32\3\33\3\33\3\34\3\34\3\34\3\34\7\34\u01ff\n"+
		"\34\f\34\16\34\u0202\13\34\5\34\u0204\n\34\3\34\3\34\3\35\3\35\3\35\3"+
		"\35\3\35\3\35\3\35\3\35\7\35\u0210\n\35\f\35\16\35\u0213\13\35\5\35\u0215"+
		"\n\35\3\35\3\35\3\36\3\36\3\36\3\36\5\36\u021d\n\36\3\36\3\36\3\36\5\36"+
		"\u0222\n\36\3\36\5\36\u0225\n\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\5"+
		"\37\u022e\n\37\3\37\3\37\3\37\3\37\3\37\3\37\7\37\u0236\n\37\f\37\16\37"+
		"\u0239\13\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \7 \u024b"+
		"\n \f \16 \u024e\13 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\7!\u025f"+
		"\n!\f!\16!\u0262\13!\3!\3!\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3"+
		"$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\7&\u0282\n&\f&\16&\u0285\13"+
		"&\5&\u0287\n&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3)\3)\3)"+
		"\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3+\3,\3,"+
		"\3,\3,\3,\3,\3-\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/"+
		"\3/\3\60\3\60\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\61\3\61\5"+
		"f\u00fb\u01c4\3 \62\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60"+
		"\62\64\668:<>@BDFHJLNPRTVXZ\\^`\2\6\3\29:\3\2\64\65\3\2 !\3\2?C\u031a"+
		"\2f\3\2\2\2\4s\3\2\2\2\6u\3\2\2\2\b\u0085\3\2\2\2\n\u009d\3\2\2\2\f\u00af"+
		"\3\2\2\2\16\u00bf\3\2\2\2\20\u00c5\3\2\2\2\22\u00d0\3\2\2\2\24\u00f3\3"+
		"\2\2\2\26\u00f5\3\2\2\2\30\u0100\3\2\2\2\32\u0102\3\2\2\2\34\u010a\3\2"+
		"\2\2\36\u0110\3\2\2\2 \u0139\3\2\2\2\"\u0158\3\2\2\2$\u018f\3\2\2\2&\u0191"+
		"\3\2\2\2(\u01a5\3\2\2\2*\u01bf\3\2\2\2,\u01c2\3\2\2\2.\u01dd\3\2\2\2\60"+
		"\u01eb\3\2\2\2\62\u01f6\3\2\2\2\64\u01f8\3\2\2\2\66\u01fa\3\2\2\28\u0207"+
		"\3\2\2\2:\u0218\3\2\2\2<\u0228\3\2\2\2>\u023c\3\2\2\2@\u0251\3\2\2\2B"+
		"\u0265\3\2\2\2D\u0268\3\2\2\2F\u0270\3\2\2\2H\u0276\3\2\2\2J\u027c\3\2"+
		"\2\2L\u028b\3\2\2\2N\u0290\3\2\2\2P\u0296\3\2\2\2R\u029e\3\2\2\2T\u02a6"+
		"\3\2\2\2V\u02ae\3\2\2\2X\u02b4\3\2\2\2Z\u02ba\3\2\2\2\\\u02c2\3\2\2\2"+
		"^\u02c8\3\2\2\2`\u02ce\3\2\2\2be\5\4\3\2ce\5\30\r\2db\3\2\2\2dc\3\2\2"+
		"\2eh\3\2\2\2fg\3\2\2\2fd\3\2\2\2g\3\3\2\2\2hf\3\2\2\2it\5\b\5\2jt\5\n"+
		"\6\2kt\5\f\7\2lt\5\16\b\2mt\5:\36\2nt\5<\37\2ot\5> \2pt\5@!\2qt\5(\25"+
		"\2rt\5\6\4\2si\3\2\2\2sj\3\2\2\2sk\3\2\2\2sl\3\2\2\2sm\3\2\2\2sn\3\2\2"+
		"\2so\3\2\2\2sp\3\2\2\2sq\3\2\2\2sr\3\2\2\2t\5\3\2\2\2u\u0080\7+\2\2vy"+
		"\7*\2\2wz\5*\26\2xz\5(\25\2yw\3\2\2\2yx\3\2\2\2z{\3\2\2\2{|\7*\2\2|\177"+
		"\3\2\2\2}\177\5\30\r\2~v\3\2\2\2~}\3\2\2\2\177\u0082\3\2\2\2\u0080~\3"+
		"\2\2\2\u0080\u0081\3\2\2\2\u0081\u0083\3\2\2\2\u0082\u0080\3\2\2\2\u0083"+
		"\u0084\7,\2\2\u0084\7\3\2\2\2\u0085\u0092\7\3\2\2\u0086\u008f\7 \2\2\u0087"+
		"\u008c\5$\23\2\u0088\u0089\7%\2\2\u0089\u008b\5$\23\2\u008a\u0088\3\2"+
		"\2\2\u008b\u008e\3\2\2\2\u008c\u008a\3\2\2\2\u008c\u008d\3\2\2\2\u008d"+
		"\u0090\3\2\2\2\u008e\u008c\3\2\2\2\u008f\u0087\3\2\2\2\u008f\u0090\3\2"+
		"\2\2\u0090\u0091\3\2\2\2\u0091\u0093\7!\2\2\u0092\u0086\3\2\2\2\u0092"+
		"\u0093\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0098\7\36\2\2\u0095\u0097\5"+
		"\24\13\2\u0096\u0095\3\2\2\2\u0097\u009a\3\2\2\2\u0098\u0096\3\2\2\2\u0098"+
		"\u0099\3\2\2\2\u0099\u009b\3\2\2\2\u009a\u0098\3\2\2\2\u009b\u009c\7\37"+
		"\2\2\u009c\t\3\2\2\2\u009d\u009e\7\4\2\2\u009e\u009f\7=\2\2\u009f\u00a1"+
		"\7 \2\2\u00a0\u00a2\5\32\16\2\u00a1\u00a0\3\2\2\2\u00a1\u00a2\3\2\2\2"+
		"\u00a2\u00a3\3\2\2\2\u00a3\u00a4\7!\2\2\u00a4\u00a5\7\5\2\2\u00a5\u00a6"+
		"\5$\23\2\u00a6\u00aa\7\36\2\2\u00a7\u00a9\5\22\n\2\u00a8\u00a7\3\2\2\2"+
		"\u00a9\u00ac\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00ad"+
		"\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ad\u00ae\7\37\2\2\u00ae\13\3\2\2\2\u00af"+
		"\u00b0\7\7\2\2\u00b0\u00b1\7\36\2\2\u00b1\u00b6\7=\2\2\u00b2\u00b3\t\2"+
		"\2\2\u00b3\u00b5\7=\2\2\u00b4\u00b2\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6"+
		"\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00bb\3\2\2\2\u00b8\u00b6\3\2"+
		"\2\2\u00b9\u00ba\7;\2\2\u00ba\u00bc\7=\2\2\u00bb\u00b9\3\2\2\2\u00bb\u00bc"+
		"\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00be\7\37\2\2\u00be\r\3\2\2\2\u00bf"+
		"\u00c1\7\b\2\2\u00c0\u00c2\5\20\t\2\u00c1\u00c0\3\2\2\2\u00c1\u00c2\3"+
		"\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c4\5\26\f\2\u00c4\17\3\2\2\2\u00c5"+
		"\u00c6\7 \2\2\u00c6\u00cb\7>\2\2\u00c7\u00c8\7%\2\2\u00c8\u00ca\7>\2\2"+
		"\u00c9\u00c7\3\2\2\2\u00ca\u00cd\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cb\u00cc"+
		"\3\2\2\2\u00cc\u00ce\3\2\2\2\u00cd\u00cb\3\2\2\2\u00ce\u00cf\7!\2\2\u00cf"+
		"\21\3\2\2\2\u00d0\u00d1\7\6\2\2\u00d1\u00d2\7=\2\2\u00d2\u00d6\7\36\2"+
		"\2\u00d3\u00d5\5\24\13\2\u00d4\u00d3\3\2\2\2\u00d5\u00d8\3\2\2\2\u00d6"+
		"\u00d4\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00d9\3\2\2\2\u00d8\u00d6\3\2"+
		"\2\2\u00d9\u00da\7\37\2\2\u00da\23\3\2\2\2\u00db\u00de\5\36\20\2\u00dc"+
		"\u00dd\t\3\2\2\u00dd\u00df\5 \21\2\u00de\u00dc\3\2\2\2\u00de\u00df\3\2"+
		"\2\2\u00df\u00e0\3\2\2\2\u00e0\u00e1\7$\2\2\u00e1\u00e2\5\26\f\2\u00e2"+
		"\u00f4\3\2\2\2\u00e3\u00e6\5\36\20\2\u00e4\u00e5\t\3\2\2\u00e5\u00e7\5"+
		" \21\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8"+
		"\u00e9\7$\2\2\u00e9\u00ea\5$\23\2\u00ea\u00f4\3\2\2\2\u00eb\u00ec\5 \21"+
		"\2\u00ec\u00ed\7$\2\2\u00ed\u00ee\5\26\f\2\u00ee\u00f4\3\2\2\2\u00ef\u00f0"+
		"\5 \21\2\u00f0\u00f1\7$\2\2\u00f1\u00f2\5$\23\2\u00f2\u00f4\3\2\2\2\u00f3"+
		"\u00db\3\2\2\2\u00f3\u00e3\3\2\2\2\u00f3\u00eb\3\2\2\2\u00f3\u00ef\3\2"+
		"\2\2\u00f4\25\3\2\2\2\u00f5\u00fb\7\36\2\2\u00f6\u00fa\5\4\3\2\u00f7\u00fa"+
		"\5\26\f\2\u00f8\u00fa\5\30\r\2\u00f9\u00f6\3\2\2\2\u00f9\u00f7\3\2\2\2"+
		"\u00f9\u00f8\3\2\2\2\u00fa\u00fd\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fb\u00f9"+
		"\3\2\2\2\u00fc\u00fe\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fe\u00ff\7\37\2\2"+
		"\u00ff\27\3\2\2\2\u0100\u0101\13\2\2\2\u0101\31\3\2\2\2\u0102\u0107\5"+
		"\34\17\2\u0103\u0104\7%\2\2\u0104\u0106\5\34\17\2\u0105\u0103\3\2\2\2"+
		"\u0106\u0109\3\2\2\2\u0107\u0105\3\2\2\2\u0107\u0108\3\2\2\2\u0108\33"+
		"\3\2\2\2\u0109\u0107\3\2\2\2\u010a\u010c\7=\2\2\u010b\u010d\7&\2\2\u010c"+
		"\u010b\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u010e\3\2\2\2\u010e\u010f\7="+
		"\2\2\u010f\35\3\2\2\2\u0110\u0115\5.\30\2\u0111\u0112\7%\2\2\u0112\u0114"+
		"\5.\30\2\u0113\u0111\3\2\2\2\u0114\u0117\3\2\2\2\u0115\u0113\3\2\2\2\u0115"+
		"\u0116\3\2\2\2\u0116\37\3\2\2\2\u0117\u0115\3\2\2\2\u0118\u0119\b\21\1"+
		"\2\u0119\u011a\5.\30\2\u011a\u011b\7\34\2\2\u011b\u011c\5$\23\2\u011c"+
		"\u013a\3\2\2\2\u011d\u011e\5\"\22\2\u011e\u011f\7\60\2\2\u011f\u0120\5"+
		"\"\22\2\u0120\u013a\3\2\2\2\u0121\u0122\5\"\22\2\u0122\u0123\7.\2\2\u0123"+
		"\u0124\5\"\22\2\u0124\u013a\3\2\2\2\u0125\u0126\5\"\22\2\u0126\u0127\7"+
		"\61\2\2\u0127\u0128\5\"\22\2\u0128\u013a\3\2\2\2\u0129\u012a\5\"\22\2"+
		"\u012a\u012b\7/\2\2\u012b\u012c\5\"\22\2\u012c\u013a\3\2\2\2\u012d\u012e"+
		"\5\"\22\2\u012e\u012f\7\62\2\2\u012f\u0130\5\"\22\2\u0130\u013a\3\2\2"+
		"\2\u0131\u0132\5\"\22\2\u0132\u0133\7\63\2\2\u0133\u0134\5\"\22\2\u0134"+
		"\u013a\3\2\2\2\u0135\u0136\7 \2\2\u0136\u0137\5 \21\2\u0137\u0138\7!\2"+
		"\2\u0138\u013a\3\2\2\2\u0139\u0118\3\2\2\2\u0139\u011d\3\2\2\2\u0139\u0121"+
		"\3\2\2\2\u0139\u0125\3\2\2\2\u0139\u0129\3\2\2\2\u0139\u012d\3\2\2\2\u0139"+
		"\u0131\3\2\2\2\u0139\u0135\3\2\2\2\u013a\u0143\3\2\2\2\u013b\u013c\f\f"+
		"\2\2\u013c\u013d\7\64\2\2\u013d\u0142\5 \21\r\u013e\u013f\f\13\2\2\u013f"+
		"\u0140\7\65\2\2\u0140\u0142\5 \21\f\u0141\u013b\3\2\2\2\u0141\u013e\3"+
		"\2\2\2\u0142\u0145\3\2\2\2\u0143\u0141\3\2\2\2\u0143\u0144\3\2\2\2\u0144"+
		"!\3\2\2\2\u0145\u0143\3\2\2\2\u0146\u0148\7=\2\2\u0147\u0149\7\'\2\2\u0148"+
		"\u0147\3\2\2\2\u0148\u0149\3\2\2\2\u0149\u0159\3\2\2\2\u014a\u014b\7="+
		"\2\2\u014b\u0154\7 \2\2\u014c\u0151\5\"\22\2\u014d\u014e\7%\2\2\u014e"+
		"\u0150\5\"\22\2\u014f\u014d\3\2\2\2\u0150\u0153\3\2\2\2\u0151\u014f\3"+
		"\2\2\2\u0151\u0152\3\2\2\2\u0152\u0155\3\2\2\2\u0153\u0151\3\2\2\2\u0154"+
		"\u014c\3\2\2\2\u0154\u0155\3\2\2\2\u0155\u0156\3\2\2\2\u0156\u0159\7!"+
		"\2\2\u0157\u0159\5\64\33\2\u0158\u0146\3\2\2\2\u0158\u014a\3\2\2\2\u0158"+
		"\u0157\3\2\2\2\u0159#\3\2\2\2\u015a\u015c\7=\2\2\u015b\u015a\3\2\2\2\u015b"+
		"\u015c\3\2\2\2\u015c\u015e\3\2\2\2\u015d\u015f\7<\2\2\u015e\u015d\3\2"+
		"\2\2\u015e\u015f\3\2\2\2\u015f\u0160\3\2\2\2\u0160\u0161\7=\2\2\u0161"+
		"\u016a\7 \2\2\u0162\u0167\5$\23\2\u0163\u0164\7%\2\2\u0164\u0166\5$\23"+
		"\2\u0165\u0163\3\2\2\2\u0166\u0169\3\2\2\2\u0167\u0165\3\2\2\2\u0167\u0168"+
		"\3\2\2\2\u0168\u016b\3\2\2\2\u0169\u0167\3\2\2\2\u016a\u0162\3\2\2\2\u016a"+
		"\u016b\3\2\2\2\u016b\u016c\3\2\2\2\u016c\u0190\7!\2\2\u016d\u016f\7=\2"+
		"\2\u016e\u016d\3\2\2\2\u016e\u016f\3\2\2\2\u016f\u0171\3\2\2\2\u0170\u0172"+
		"\7<\2\2\u0171\u0170\3\2\2\2\u0171\u0172\3\2\2\2\u0172\u0173\3\2\2\2\u0173"+
		"\u0174\7=\2\2\u0174\u017d\7\"\2\2\u0175\u017a\5&\24\2\u0176\u0177\7%\2"+
		"\2\u0177\u0179\5&\24\2\u0178\u0176\3\2\2\2\u0179\u017c\3\2\2\2\u017a\u0178"+
		"\3\2\2\2\u017a\u017b\3\2\2\2\u017b\u017e\3\2\2\2\u017c\u017a\3\2\2\2\u017d"+
		"\u0175\3\2\2\2\u017d\u017e\3\2\2\2\u017e\u017f\3\2\2\2\u017f\u0190\7#"+
		"\2\2\u0180\u0182\7=\2\2\u0181\u0180\3\2\2\2\u0181\u0182\3\2\2\2\u0182"+
		"\u0184\3\2\2\2\u0183\u0185\7<\2\2\u0184\u0183\3\2\2\2\u0184\u0185\3\2"+
		"\2\2\u0185\u0186\3\2\2\2\u0186\u0188\7=\2\2\u0187\u0189\7\'\2\2\u0188"+
		"\u0187\3\2\2\2\u0188\u0189\3\2\2\2\u0189\u0190\3\2\2\2\u018a\u018c\7="+
		"\2\2\u018b\u018a\3\2\2\2\u018b\u018c\3\2\2\2\u018c\u018d\3\2\2\2\u018d"+
		"\u0190\5\64\33\2\u018e\u0190\7(\2\2\u018f\u015b\3\2\2\2\u018f\u016e\3"+
		"\2\2\2\u018f\u0181\3\2\2\2\u018f\u018b\3\2\2\2\u018f\u018e\3\2\2\2\u0190"+
		"%\3\2\2\2\u0191\u0192\7=\2\2\u0192\u0193\7\35\2\2\u0193\u0194\5$\23\2"+
		"\u0194\'\3\2\2\2\u0195\u0196\7<\2\2\u0196\u0197\7=\2\2\u0197\u01a0\7\""+
		"\2\2\u0198\u019d\5&\24\2\u0199\u019a\7%\2\2\u019a\u019c\5&\24\2\u019b"+
		"\u0199\3\2\2\2\u019c\u019f\3\2\2\2\u019d\u019b\3\2\2\2\u019d\u019e\3\2"+
		"\2\2\u019e\u01a1\3\2\2\2\u019f\u019d\3\2\2\2\u01a0\u0198\3\2\2\2\u01a0"+
		"\u01a1\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2\u01a6\7#\2\2\u01a3\u01a4\7<\2"+
		"\2\u01a4\u01a6\5*\26\2\u01a5\u0195\3\2\2\2\u01a5\u01a3\3\2\2\2\u01a6)"+
		"\3\2\2\2\u01a7\u01a8\7=\2\2\u01a8\u01ac\7 \2\2\u01a9\u01ab\5*\26\2\u01aa"+
		"\u01a9\3\2\2\2\u01ab\u01ae\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ac\u01ad\3\2"+
		"\2\2\u01ad\u01af\3\2\2\2\u01ae\u01ac\3\2\2\2\u01af\u01c0\7!\2\2\u01b0"+
		"\u01b4\7 \2\2\u01b1\u01b3\5*\26\2\u01b2\u01b1\3\2\2\2\u01b3\u01b6\3\2"+
		"\2\2\u01b4\u01b2\3\2\2\2\u01b4\u01b5\3\2\2\2\u01b5\u01b7\3\2\2\2\u01b6"+
		"\u01b4\3\2\2\2\u01b7\u01c0\7!\2\2\u01b8\u01ba\7=\2\2\u01b9\u01bb\7\'\2"+
		"\2\u01ba\u01b9\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bb\u01c0\3\2\2\2\u01bc\u01c0"+
		"\5\64\33\2\u01bd\u01c0\7(\2\2\u01be\u01c0\5,\27\2\u01bf\u01a7\3\2\2\2"+
		"\u01bf\u01b0\3\2\2\2\u01bf\u01b8\3\2\2\2\u01bf\u01bc\3\2\2\2\u01bf\u01bd"+
		"\3\2\2\2\u01bf\u01be\3\2\2\2\u01c0+\3\2\2\2\u01c1\u01c3\n\4\2\2\u01c2"+
		"\u01c1\3\2\2\2\u01c3\u01c4\3\2\2\2\u01c4\u01c5\3\2\2\2\u01c4\u01c2\3\2"+
		"\2\2\u01c5-\3\2\2\2\u01c6\u01c7\7=\2\2\u01c7\u01c8\7*\2\2\u01c8\u01de"+
		"\5.\30\2\u01c9\u01ca\7)\2\2\u01ca\u01de\5.\30\2\u01cb\u01cc\5\60\31\2"+
		"\u01cc\u01cd\5\66\34\2\u01cd\u01de\3\2\2\2\u01ce\u01cf\5\60\31\2\u01cf"+
		"\u01d0\58\35\2\u01d0\u01de\3\2\2\2\u01d1\u01d3\7=\2\2\u01d2\u01d4\7\'"+
		"\2\2\u01d3\u01d2\3\2\2\2\u01d3\u01d4\3\2\2\2\u01d4\u01de\3\2\2\2\u01d5"+
		"\u01d7\7(\2\2\u01d6\u01d8\7\'\2\2\u01d7\u01d6\3\2\2\2\u01d7\u01d8\3\2"+
		"\2\2\u01d8\u01de\3\2\2\2\u01d9\u01db\5\64\33\2\u01da\u01dc\7\'\2\2\u01db"+
		"\u01da\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc\u01de\3\2\2\2\u01dd\u01c6\3\2"+
		"\2\2\u01dd\u01c9\3\2\2\2\u01dd\u01cb\3\2\2\2\u01dd\u01ce\3\2\2\2\u01dd"+
		"\u01d1\3\2\2\2\u01dd\u01d5\3\2\2\2\u01dd\u01d9\3\2\2\2\u01de/\3\2\2\2"+
		"\u01df\u01ec\5\62\32\2\u01e0\u01e1\7 \2\2\u01e1\u01e6\5\62\32\2\u01e2"+
		"\u01e3\7\66\2\2\u01e3\u01e5\5\62\32\2\u01e4\u01e2\3\2\2\2\u01e5\u01e8"+
		"\3\2\2\2\u01e6\u01e4\3\2\2\2\u01e6\u01e7\3\2\2\2\u01e7\u01e9\3\2\2\2\u01e8"+
		"\u01e6\3\2\2\2\u01e9\u01ea\7!\2\2\u01ea\u01ec\3\2\2\2\u01eb\u01df\3\2"+
		"\2\2\u01eb\u01e0\3\2\2\2\u01ec\61\3\2\2\2\u01ed\u01ef\7=\2\2\u01ee\u01f0"+
		"\7\67\2\2\u01ef\u01ee\3\2\2\2\u01ef\u01f0\3\2\2\2\u01f0\u01f7\3\2\2\2"+
		"\u01f1\u01f3\7=\2\2\u01f2\u01f4\78\2\2\u01f3\u01f2\3\2\2\2\u01f3\u01f4"+
		"\3\2\2\2\u01f4\u01f7\3\2\2\2\u01f5\u01f7\5\64\33\2\u01f6\u01ed\3\2\2\2"+
		"\u01f6\u01f1\3\2\2\2\u01f6\u01f5\3\2\2\2\u01f7\63\3\2\2\2\u01f8\u01f9"+
		"\t\5\2\2\u01f9\65\3\2\2\2\u01fa\u0203\7 \2\2\u01fb\u0200\5.\30\2\u01fc"+
		"\u01fd\7%\2\2\u01fd\u01ff\5.\30\2\u01fe\u01fc\3\2\2\2\u01ff\u0202\3\2"+
		"\2\2\u0200\u01fe\3\2\2\2\u0200\u0201\3\2\2\2\u0201\u0204\3\2\2\2\u0202"+
		"\u0200\3\2\2\2\u0203\u01fb\3\2\2\2\u0203\u0204\3\2\2\2\u0204\u0205\3\2"+
		"\2\2\u0205\u0206\7!\2\2\u0206\67\3\2\2\2\u0207\u0214\7\"\2\2\u0208\u0209"+
		"\7=\2\2\u0209\u020a\7\35\2\2\u020a\u0211\5.\30\2\u020b\u020c\7%\2\2\u020c"+
		"\u020d\7=\2\2\u020d\u020e\7\35\2\2\u020e\u0210\5.\30\2\u020f\u020b\3\2"+
		"\2\2\u0210\u0213\3\2\2\2\u0211\u020f\3\2\2\2\u0211\u0212\3\2\2\2\u0212"+
		"\u0215\3\2\2\2\u0213\u0211\3\2\2\2\u0214\u0208\3\2\2\2\u0214\u0215\3\2"+
		"\2\2\u0215\u0216\3\2\2\2\u0216\u0217\7#\2\2\u02179\3\2\2\2\u0218\u0219"+
		"\7\f\2\2\u0219\u021c\7=\2\2\u021a\u021b\7\5\2\2\u021b\u021d\7=\2\2\u021c"+
		"\u021a\3\2\2\2\u021c\u021d\3\2\2\2\u021d\u021e\3\2\2\2\u021e\u021f\7\36"+
		"\2\2\u021f\u0221\5B\"\2\u0220\u0222\5F$\2\u0221\u0220\3\2\2\2\u0221\u0222"+
		"\3\2\2\2\u0222\u0224\3\2\2\2\u0223\u0225\5D#\2\u0224\u0223\3\2\2\2\u0224"+
		"\u0225\3\2\2\2\u0225\u0226\3\2\2\2\u0226\u0227\7\37\2\2\u0227;\3\2\2\2"+
		"\u0228\u0229\7\t\2\2\u0229\u022a\7=\2\2\u022a\u022b\7=\2\2\u022b\u022d"+
		"\7 \2\2\u022c\u022e\5\32\16\2\u022d\u022c\3\2\2\2\u022d\u022e\3\2\2\2"+
		"\u022e\u022f\3\2\2\2\u022f\u0230\7!\2\2\u0230\u0237\7\36\2\2\u0231\u0236"+
		"\5H%\2\u0232\u0236\5J&\2\u0233\u0236\5T+\2\u0234\u0236\5`\61\2\u0235\u0231"+
		"\3\2\2\2\u0235\u0232\3\2\2\2\u0235\u0233\3\2\2\2\u0235\u0234\3\2\2\2\u0236"+
		"\u0239\3\2\2\2\u0237\u0235\3\2\2\2\u0237\u0238\3\2\2\2\u0238\u023a\3\2"+
		"\2\2\u0239\u0237\3\2\2\2\u023a\u023b\7\37\2\2\u023b=\3\2\2\2\u023c\u023d"+
		"\7\13\2\2\u023d\u023e\7=\2\2\u023e\u023f\7=\2\2\u023f\u0240\7 \2\2\u0240"+
		"\u0241\7=\2\2\u0241\u0242\7\'\2\2\u0242\u0243\7!\2\2\u0243\u024c\7\36"+
		"\2\2\u0244\u024b\5H%\2\u0245\u024b\5L\'\2\u0246\u024b\5R*\2\u0247\u024b"+
		"\5V,\2\u0248\u024b\5X-\2\u0249\u024b\5\\/\2\u024a\u0244\3\2\2\2\u024a"+
		"\u0245\3\2\2\2\u024a\u0246\3\2\2\2\u024a\u0247\3\2\2\2\u024a\u0248\3\2"+
		"\2\2\u024a\u0249\3\2\2\2\u024b\u024e\3\2\2\2\u024c\u024a\3\2\2\2\u024c"+
		"\u024d\3\2\2\2\u024d\u024f\3\2\2\2\u024e\u024c\3\2\2\2\u024f\u0250\7\37"+
		"\2\2\u0250?\3\2\2\2\u0251\u0252\7\n\2\2\u0252\u0253\7=\2\2\u0253\u0254"+
		"\7=\2\2\u0254\u0255\7 \2\2\u0255\u0256\7=\2\2\u0256\u0257\7\'\2\2\u0257"+
		"\u0258\7!\2\2\u0258\u0260\7\36\2\2\u0259\u025f\5H%\2\u025a\u025f\5N(\2"+
		"\u025b\u025f\5P)\2\u025c\u025f\5Z.\2\u025d\u025f\5^\60\2\u025e\u0259\3"+
		"\2\2\2\u025e\u025a\3\2\2\2\u025e\u025b\3\2\2\2\u025e\u025c\3\2\2\2\u025e"+
		"\u025d\3\2\2\2\u025f\u0262\3\2\2\2\u0260\u025e\3\2\2\2\u0260\u0261\3\2"+
		"\2\2\u0261\u0263\3\2\2\2\u0262\u0260\3\2\2\2\u0263\u0264\7\37\2\2\u0264"+
		"A\3\2\2\2\u0265\u0266\7\32\2\2\u0266\u0267\5\26\f\2\u0267C\3\2\2\2\u0268"+
		"\u0269\7\33\2\2\u0269\u026a\7 \2\2\u026a\u026b\7=\2\2\u026b\u026c\7%\2"+
		"\2\u026c\u026d\7=\2\2\u026d\u026e\7!\2\2\u026e\u026f\5\26\f\2\u026fE\3"+
		"\2\2\2\u0270\u0271\7\16\2\2\u0271\u0272\7 \2\2\u0272\u0273\7=\2\2\u0273"+
		"\u0274\7!\2\2\u0274\u0275\5\26\f\2\u0275G\3\2\2\2\u0276\u0277\7\r\2\2"+
		"\u0277\u0278\7 \2\2\u0278\u0279\7=\2\2\u0279\u027a\7!\2\2\u027a\u027b"+
		"\5\26\f\2\u027bI\3\2\2\2\u027c\u027d\7\17\2\2\u027d\u0286\7 \2\2\u027e"+
		"\u0283\7=\2\2\u027f\u0280\7%\2\2\u0280\u0282\7=\2\2\u0281\u027f\3\2\2"+
		"\2\u0282\u0285\3\2\2\2\u0283\u0281\3\2\2\2\u0283\u0284\3\2\2\2\u0284\u0287"+
		"\3\2\2\2\u0285\u0283\3\2\2\2\u0286\u027e\3\2\2\2\u0286\u0287\3\2\2\2\u0287"+
		"\u0288\3\2\2\2\u0288\u0289\7!\2\2\u0289\u028a\5\26\f\2\u028aK\3\2\2\2"+
		"\u028b\u028c\7\20\2\2\u028c\u028d\7 \2\2\u028d\u028e\7!\2\2\u028e\u028f"+
		"\5\26\f\2\u028fM\3\2\2\2\u0290\u0291\7\20\2\2\u0291\u0292\7 \2\2\u0292"+
		"\u0293\7=\2\2\u0293\u0294\7!\2\2\u0294\u0295\5\26\f\2\u0295O\3\2\2\2\u0296"+
		"\u0297\7\21\2\2\u0297\u0298\7 \2\2\u0298\u0299\7=\2\2\u0299\u029a\7%\2"+
		"\2\u029a\u029b\7=\2\2\u029b\u029c\7!\2\2\u029c\u029d\5\26\f\2\u029dQ\3"+
		"\2\2\2\u029e\u029f\7\22\2\2\u029f\u02a0\7 \2\2\u02a0\u02a1\7=\2\2\u02a1"+
		"\u02a2\7%\2\2\u02a2\u02a3\7=\2\2\u02a3\u02a4\7!\2\2\u02a4\u02a5\5\26\f"+
		"\2\u02a5S\3\2\2\2\u02a6\u02a7\7\23\2\2\u02a7\u02a8\7 \2\2\u02a8\u02a9"+
		"\7=\2\2\u02a9\u02aa\7%\2\2\u02aa\u02ab\7=\2\2\u02ab\u02ac\7!\2\2\u02ac"+
		"\u02ad\5\26\f\2\u02adU\3\2\2\2\u02ae\u02af\7\26\2\2\u02af\u02b0\7 \2\2"+
		"\u02b0\u02b1\7=\2\2\u02b1\u02b2\7!\2\2\u02b2\u02b3\5\26\f\2\u02b3W\3\2"+
		"\2\2\u02b4\u02b5\7\27\2\2\u02b5\u02b6\7 \2\2\u02b6\u02b7\7=\2\2\u02b7"+
		"\u02b8\7!\2\2\u02b8\u02b9\5\26\f\2\u02b9Y\3\2\2\2\u02ba\u02bb\7\25\2\2"+
		"\u02bb\u02bc\7 \2\2\u02bc\u02bd\7=\2\2\u02bd\u02be\7%\2\2\u02be\u02bf"+
		"\7=\2\2\u02bf\u02c0\7!\2\2\u02c0\u02c1\5\26\f\2\u02c1[\3\2\2\2\u02c2\u02c3"+
		"\7\31\2\2\u02c3\u02c4\7 \2\2\u02c4\u02c5\7=\2\2\u02c5\u02c6\7!\2\2\u02c6"+
		"\u02c7\5\26\f\2\u02c7]\3\2\2\2\u02c8\u02c9\7\30\2\2\u02c9\u02ca\7 \2\2"+
		"\u02ca\u02cb\7=\2\2\u02cb\u02cc\7!\2\2\u02cc\u02cd\5\26\f\2\u02cd_\3\2"+
		"\2\2\u02ce\u02cf\7\24\2\2\u02cf\u02d0\7 \2\2\u02d0\u02d1\7=\2\2\u02d1"+
		"\u02d2\7!\2\2\u02d2\u02d3\5\26\f\2\u02d3a\3\2\2\2Pdfsy~\u0080\u008c\u008f"+
		"\u0092\u0098\u00a1\u00aa\u00b6\u00bb\u00c1\u00cb\u00d6\u00de\u00e6\u00f3"+
		"\u00f9\u00fb\u0107\u010c\u0115\u0139\u0141\u0143\u0148\u0151\u0154\u0158"+
		"\u015b\u015e\u0167\u016a\u016e\u0171\u017a\u017d\u0181\u0184\u0188\u018b"+
		"\u018f\u019d\u01a0\u01a5\u01ac\u01b4\u01ba\u01bf\u01c4\u01d3\u01d7\u01db"+
		"\u01dd\u01e6\u01eb\u01ef\u01f3\u01f6\u0200\u0203\u0211\u0214\u021c\u0221"+
		"\u0224\u022d\u0235\u0237\u024a\u024c\u025e\u0260\u0283\u0286";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}