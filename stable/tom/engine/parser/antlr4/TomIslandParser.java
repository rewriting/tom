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
		ANTI=39, AT=40, METAQUOTE=41, ATAT=42, GREATEROREQ=43, LOWEROREQ=44, GREATERTHAN=45, 
		LOWERTHAN=46, DOUBLEEQ=47, DIFFERENT=48, AND=49, OR=50, PIPE=51, QMARK=52, 
		DQMARK=53, SLASH=54, BACKSLASH=55, DOT=56, BQUOTE=57, ID=58, DMINUSID=59, 
		INTEGER=60, DOUBLE=61, LONG=62, STRING=63, CHAR=64, ACTION_ESCAPE=65, 
		ACTION_STRING_LITERAL=66, MLCOMMENT=67, SLCOMMENT=68, WS=69, NL=70, ANY=71;
	public static final int
		RULE_start = 0, RULE_island = 1, RULE_metaquote = 2, RULE_matchStatement = 3, 
		RULE_strategyStatement = 4, RULE_includeStatement = 5, RULE_gomStatement = 6, 
		RULE_gomOptions = 7, RULE_visit = 8, RULE_actionRule = 9, RULE_block = 10, 
		RULE_water = 11, RULE_slotList = 12, RULE_slot = 13, RULE_patternlist = 14, 
		RULE_constraint = 15, RULE_term = 16, RULE_bqterm = 17, RULE_pairSlotBqterm = 18, 
		RULE_bqcomposite = 19, RULE_composite = 20, RULE_pattern = 21, RULE_fsymbol = 22, 
		RULE_headSymbol = 23, RULE_constant = 24, RULE_explicitArgs = 25, RULE_implicitArgs = 26, 
		RULE_typeterm = 27, RULE_operator = 28, RULE_oplist = 29, RULE_oparray = 30, 
		RULE_implement = 31, RULE_equalsTerm = 32, RULE_isSort = 33, RULE_isFsym = 34, 
		RULE_make = 35, RULE_makeEmptyList = 36, RULE_makeEmptyArray = 37, RULE_makeAppendArray = 38, 
		RULE_makeInsertList = 39, RULE_getSlot = 40, RULE_getHead = 41, RULE_getTail = 42, 
		RULE_getElement = 43, RULE_isEmptyList = 44, RULE_getSize = 45, RULE_getDefault = 46;
	public static final String[] ruleNames = {
		"start", "island", "metaquote", "matchStatement", "strategyStatement", 
		"includeStatement", "gomStatement", "gomOptions", "visit", "actionRule", 
		"block", "water", "slotList", "slot", "patternlist", "constraint", "term", 
		"bqterm", "pairSlotBqterm", "bqcomposite", "composite", "pattern", "fsymbol", 
		"headSymbol", "constant", "explicitArgs", "implicitArgs", "typeterm", 
		"operator", "oplist", "oparray", "implement", "equalsTerm", "isSort", 
		"isFsym", "make", "makeEmptyList", "makeEmptyArray", "makeAppendArray", 
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
		"'_'", "'!'", "'@'", null, "'@@'", "'>='", "'<='", "'>'", "'<'", "'=='", 
		"'!='", "'&&'", "'||'", "'|'", "'?'", "'??'", "'/'", "'\\'", "'.'", "'`'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "MATCH", "STRATEGY", "EXTENDS", "VISIT", "INCLUDE", "GOM", "OP", 
		"OPARRAY", "OPLIST", "TYPETERM", "IS_FSYM", "IS_SORT", "MAKE", "MAKE_EMPTY", 
		"MAKE_APPEND", "MAKE_INSERT", "GET_SLOT", "GET_DEFAULT", "GET_ELEMENT", 
		"GET_HEAD", "GET_TAIL", "GET_SIZE", "IS_EMPTY", "IMPLEMENT", "EQUALS", 
		"MATCH_SYMBOL", "EQUAL", "LBRACE", "RBRACE", "LPAREN", "RPAREN", "LSQUAREBR", 
		"RSQUAREBR", "ARROW", "COMMA", "COLON", "STAR", "UNDERSCORE", "ANTI", 
		"AT", "METAQUOTE", "ATAT", "GREATEROREQ", "LOWEROREQ", "GREATERTHAN", 
		"LOWERTHAN", "DOUBLEEQ", "DIFFERENT", "AND", "OR", "PIPE", "QMARK", "DQMARK", 
		"SLASH", "BACKSLASH", "DOT", "BQUOTE", "ID", "DMINUSID", "INTEGER", "DOUBLE", 
		"LONG", "STRING", "CHAR", "ACTION_ESCAPE", "ACTION_STRING_LITERAL", "MLCOMMENT", 
		"SLCOMMENT", "WS", "NL", "ANY"
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
			setState(98);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					setState(96);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
					case 1:
						{
						setState(94);
						island();
						}
						break;
					case 2:
						{
						setState(95);
						water();
						}
						break;
					}
					} 
				}
				setState(100);
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
			setState(111);
			switch (_input.LA(1)) {
			case MATCH:
				enterOuterAlt(_localctx, 1);
				{
				setState(101);
				matchStatement();
				}
				break;
			case STRATEGY:
				enterOuterAlt(_localctx, 2);
				{
				setState(102);
				strategyStatement();
				}
				break;
			case INCLUDE:
				enterOuterAlt(_localctx, 3);
				{
				setState(103);
				includeStatement();
				}
				break;
			case GOM:
				enterOuterAlt(_localctx, 4);
				{
				setState(104);
				gomStatement();
				}
				break;
			case TYPETERM:
				enterOuterAlt(_localctx, 5);
				{
				setState(105);
				typeterm();
				}
				break;
			case OP:
				enterOuterAlt(_localctx, 6);
				{
				setState(106);
				operator();
				}
				break;
			case OPLIST:
				enterOuterAlt(_localctx, 7);
				{
				setState(107);
				oplist();
				}
				break;
			case OPARRAY:
				enterOuterAlt(_localctx, 8);
				{
				setState(108);
				oparray();
				}
				break;
			case BQUOTE:
				enterOuterAlt(_localctx, 9);
				{
				setState(109);
				bqcomposite();
				}
				break;
			case METAQUOTE:
				enterOuterAlt(_localctx, 10);
				{
				setState(110);
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
		public TerminalNode METAQUOTE() { return getToken(TomIslandParser.METAQUOTE, 0); }
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
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			match(METAQUOTE);
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
			setState(115);
			match(MATCH);
			setState(128);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(116);
				match(LPAREN);
				setState(125);
				_la = _input.LA(1);
				if (((((_la - 38)) & ~0x3f) == 0 && ((1L << (_la - 38)) & ((1L << (UNDERSCORE - 38)) | (1L << (BQUOTE - 38)) | (1L << (ID - 38)) | (1L << (INTEGER - 38)) | (1L << (DOUBLE - 38)) | (1L << (LONG - 38)) | (1L << (STRING - 38)) | (1L << (CHAR - 38)))) != 0)) {
					{
					setState(117);
					bqterm();
					setState(122);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(118);
						match(COMMA);
						setState(119);
						bqterm();
						}
						}
						setState(124);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(127);
				match(RPAREN);
				}
			}

			setState(130);
			match(LBRACE);
			setState(134);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (LPAREN - 30)) | (1L << (UNDERSCORE - 30)) | (1L << (ANTI - 30)) | (1L << (ID - 30)) | (1L << (INTEGER - 30)) | (1L << (DOUBLE - 30)) | (1L << (LONG - 30)) | (1L << (STRING - 30)) | (1L << (CHAR - 30)))) != 0)) {
				{
				{
				setState(131);
				actionRule();
				}
				}
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(137);
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
			setState(139);
			match(STRATEGY);
			setState(140);
			match(ID);
			setState(141);
			match(LPAREN);
			setState(143);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(142);
				slotList();
				}
			}

			setState(145);
			match(RPAREN);
			setState(146);
			match(EXTENDS);
			setState(147);
			bqterm();
			setState(148);
			match(LBRACE);
			setState(152);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VISIT) {
				{
				{
				setState(149);
				visit();
				}
				}
				setState(154);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(155);
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
		public TerminalNode RBRACE() { return getToken(TomIslandParser.RBRACE, 0); }
		public List<TerminalNode> DOT() { return getTokens(TomIslandParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(TomIslandParser.DOT, i);
		}
		public List<TerminalNode> SLASH() { return getTokens(TomIslandParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(TomIslandParser.SLASH, i);
		}
		public List<TerminalNode> BACKSLASH() { return getTokens(TomIslandParser.BACKSLASH); }
		public TerminalNode BACKSLASH(int i) {
			return getToken(TomIslandParser.BACKSLASH, i);
		}
		public List<TerminalNode> ID() { return getTokens(TomIslandParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TomIslandParser.ID, i);
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
			setState(157);
			match(INCLUDE);
			setState(158);
			match(LBRACE);
			setState(162);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SLASH) | (1L << BACKSLASH) | (1L << DOT) | (1L << ID))) != 0)) {
				{
				{
				setState(159);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SLASH) | (1L << BACKSLASH) | (1L << DOT) | (1L << ID))) != 0)) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				}
				setState(164);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(165);
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
			setState(167);
			match(GOM);
			setState(169);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(168);
				gomOptions();
				}
			}

			setState(171);
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
			setState(173);
			match(LPAREN);
			setState(174);
			match(DMINUSID);
			setState(179);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(175);
				match(COMMA);
				setState(176);
				match(DMINUSID);
				}
				}
				setState(181);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(182);
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
			setState(184);
			match(VISIT);
			setState(185);
			match(ID);
			setState(186);
			match(LBRACE);
			setState(190);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (LPAREN - 30)) | (1L << (UNDERSCORE - 30)) | (1L << (ANTI - 30)) | (1L << (ID - 30)) | (1L << (INTEGER - 30)) | (1L << (DOUBLE - 30)) | (1L << (LONG - 30)) | (1L << (STRING - 30)) | (1L << (CHAR - 30)))) != 0)) {
				{
				{
				setState(187);
				actionRule();
				}
				}
				setState(192);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(193);
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
			setState(219);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(195);
				patternlist();
				setState(198);
				_la = _input.LA(1);
				if (_la==AND || _la==OR) {
					{
					setState(196);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(197);
					constraint(0);
					}
				}

				setState(200);
				match(ARROW);
				setState(201);
				block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(203);
				patternlist();
				setState(206);
				_la = _input.LA(1);
				if (_la==AND || _la==OR) {
					{
					setState(204);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(205);
					constraint(0);
					}
				}

				setState(208);
				match(ARROW);
				setState(209);
				bqterm();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(211);
				((ActionRuleContext)_localctx).c = constraint(0);
				setState(212);
				match(ARROW);
				setState(213);
				block();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(215);
				((ActionRuleContext)_localctx).c = constraint(0);
				setState(216);
				match(ARROW);
				setState(217);
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
			setState(221);
			match(LBRACE);
			setState(227);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					setState(225);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
					case 1:
						{
						setState(222);
						island();
						}
						break;
					case 2:
						{
						setState(223);
						block();
						}
						break;
					case 3:
						{
						setState(224);
						water();
						}
						break;
					}
					} 
				}
				setState(229);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			setState(230);
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
			setState(232);
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
			setState(234);
			slot();
			setState(239);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(235);
				match(COMMA);
				setState(236);
				slot();
				}
				}
				setState(241);
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
			setState(242);
			((SlotContext)_localctx).id1 = match(ID);
			setState(244);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(243);
				match(COLON);
				}
			}

			setState(246);
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
			setState(248);
			pattern();
			setState(253);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(249);
				match(COMMA);
				setState(250);
				pattern();
				}
				}
				setState(255);
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
			setState(289);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(257);
				pattern();
				setState(258);
				match(MATCH_SYMBOL);
				setState(259);
				bqterm();
				}
				break;
			case 2:
				{
				setState(261);
				term();
				setState(262);
				match(GREATERTHAN);
				setState(263);
				term();
				}
				break;
			case 3:
				{
				setState(265);
				term();
				setState(266);
				match(GREATEROREQ);
				setState(267);
				term();
				}
				break;
			case 4:
				{
				setState(269);
				term();
				setState(270);
				match(LOWERTHAN);
				setState(271);
				term();
				}
				break;
			case 5:
				{
				setState(273);
				term();
				setState(274);
				match(LOWEROREQ);
				setState(275);
				term();
				}
				break;
			case 6:
				{
				setState(277);
				term();
				setState(278);
				match(DOUBLEEQ);
				setState(279);
				term();
				}
				break;
			case 7:
				{
				setState(281);
				term();
				setState(282);
				match(DIFFERENT);
				setState(283);
				term();
				}
				break;
			case 8:
				{
				setState(285);
				match(LPAREN);
				setState(286);
				((ConstraintContext)_localctx).c = constraint(0);
				setState(287);
				match(RPAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(299);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(297);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
					case 1:
						{
						_localctx = new ConstraintContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_constraint);
						setState(291);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(292);
						match(AND);
						setState(293);
						constraint(11);
						}
						break;
					case 2:
						{
						_localctx = new ConstraintContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_constraint);
						setState(294);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(295);
						match(OR);
						setState(296);
						constraint(10);
						}
						break;
					}
					} 
				}
				setState(301);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
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
			setState(320);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(302);
				((TermContext)_localctx).var = match(ID);
				setState(304);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
				case 1:
					{
					setState(303);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(306);
				((TermContext)_localctx).fsym = match(ID);
				setState(307);
				match(LPAREN);
				setState(316);
				_la = _input.LA(1);
				if (((((_la - 58)) & ~0x3f) == 0 && ((1L << (_la - 58)) & ((1L << (ID - 58)) | (1L << (INTEGER - 58)) | (1L << (DOUBLE - 58)) | (1L << (LONG - 58)) | (1L << (STRING - 58)) | (1L << (CHAR - 58)))) != 0)) {
					{
					setState(308);
					term();
					setState(313);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(309);
						match(COMMA);
						setState(310);
						term();
						}
						}
						setState(315);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(318);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(319);
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
			setState(375);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(323);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
				case 1:
					{
					setState(322);
					((BqtermContext)_localctx).codomain = match(ID);
					}
					break;
				}
				setState(326);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(325);
					match(BQUOTE);
					}
				}

				setState(328);
				((BqtermContext)_localctx).fsym = match(ID);
				setState(329);
				match(LPAREN);
				setState(338);
				_la = _input.LA(1);
				if (((((_la - 38)) & ~0x3f) == 0 && ((1L << (_la - 38)) & ((1L << (UNDERSCORE - 38)) | (1L << (BQUOTE - 38)) | (1L << (ID - 38)) | (1L << (INTEGER - 38)) | (1L << (DOUBLE - 38)) | (1L << (LONG - 38)) | (1L << (STRING - 38)) | (1L << (CHAR - 38)))) != 0)) {
					{
					setState(330);
					bqterm();
					setState(335);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(331);
						match(COMMA);
						setState(332);
						bqterm();
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
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(342);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
				case 1:
					{
					setState(341);
					((BqtermContext)_localctx).codomain = match(ID);
					}
					break;
				}
				setState(345);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(344);
					match(BQUOTE);
					}
				}

				setState(347);
				((BqtermContext)_localctx).fsym = match(ID);
				setState(348);
				match(LSQUAREBR);
				setState(357);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(349);
					pairSlotBqterm();
					setState(354);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(350);
						match(COMMA);
						setState(351);
						pairSlotBqterm();
						}
						}
						setState(356);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(359);
				match(RSQUAREBR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(361);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
				case 1:
					{
					setState(360);
					((BqtermContext)_localctx).codomain = match(ID);
					}
					break;
				}
				setState(364);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(363);
					match(BQUOTE);
					}
				}

				setState(366);
				((BqtermContext)_localctx).var = match(ID);
				setState(368);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
				case 1:
					{
					setState(367);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(371);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(370);
					((BqtermContext)_localctx).codomain = match(ID);
					}
				}

				setState(373);
				constant();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(374);
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
			setState(377);
			match(ID);
			setState(378);
			match(EQUAL);
			setState(379);
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
			setState(397);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(381);
				match(BQUOTE);
				setState(382);
				((BqcompositeContext)_localctx).fsym = match(ID);
				setState(383);
				match(LSQUAREBR);
				setState(392);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(384);
					pairSlotBqterm();
					setState(389);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(385);
						match(COMMA);
						setState(386);
						pairSlotBqterm();
						}
						}
						setState(391);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(394);
				match(RSQUAREBR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(395);
				match(BQUOTE);
				setState(396);
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
		public WaterContext water() {
			return getRuleContext(WaterContext.class,0);
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
		try {
			int _alt;
			setState(423);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(399);
				((CompositeContext)_localctx).fsym = match(ID);
				setState(400);
				match(LPAREN);
				setState(404);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						{
						setState(401);
						composite();
						}
						} 
					}
					setState(406);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
				}
				setState(407);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(408);
				match(LPAREN);
				setState(412);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						{
						setState(409);
						composite();
						}
						} 
					}
					setState(414);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
				}
				setState(415);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(416);
				((CompositeContext)_localctx).var = match(ID);
				setState(418);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
				case 1:
					{
					setState(417);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(420);
				constant();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(421);
				match(UNDERSCORE);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(422);
				water();
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
		public List<ConstantContext> constant() {
			return getRuleContexts(ConstantContext.class);
		}
		public ConstantContext constant(int i) {
			return getRuleContext(ConstantContext.class,i);
		}
		public List<TerminalNode> PIPE() { return getTokens(TomIslandParser.PIPE); }
		public TerminalNode PIPE(int i) {
			return getToken(TomIslandParser.PIPE, i);
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
		enterRule(_localctx, 42, RULE_pattern);
		int _la;
		try {
			setState(452);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(425);
				match(ID);
				setState(426);
				match(AT);
				setState(427);
				pattern();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(428);
				match(ANTI);
				setState(429);
				pattern();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(430);
				fsymbol();
				setState(431);
				explicitArgs();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(433);
				fsymbol();
				setState(434);
				implicitArgs();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(436);
				((PatternContext)_localctx).var = match(ID);
				setState(438);
				_la = _input.LA(1);
				if (_la==STAR) {
					{
					setState(437);
					match(STAR);
					}
				}

				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(440);
				match(UNDERSCORE);
				setState(442);
				_la = _input.LA(1);
				if (_la==STAR) {
					{
					setState(441);
					match(STAR);
					}
				}

				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(444);
				constant();
				setState(449);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PIPE) {
					{
					{
					setState(445);
					match(PIPE);
					setState(446);
					constant();
					}
					}
					setState(451);
					_errHandler.sync(this);
					_la = _input.LA(1);
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
		enterRule(_localctx, 44, RULE_fsymbol);
		int _la;
		try {
			setState(465);
			switch (_input.LA(1)) {
			case ID:
			case INTEGER:
			case DOUBLE:
			case LONG:
			case STRING:
			case CHAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(454);
				headSymbol();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(455);
				match(LPAREN);
				setState(456);
				headSymbol();
				setState(459); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(457);
					match(PIPE);
					setState(458);
					headSymbol();
					}
					}
					setState(461); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==PIPE );
				setState(463);
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
		enterRule(_localctx, 46, RULE_headSymbol);
		int _la;
		try {
			setState(476);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(467);
				match(ID);
				setState(469);
				_la = _input.LA(1);
				if (_la==QMARK) {
					{
					setState(468);
					match(QMARK);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(471);
				match(ID);
				setState(473);
				_la = _input.LA(1);
				if (_la==DQMARK) {
					{
					setState(472);
					match(DQMARK);
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(475);
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
		enterRule(_localctx, 48, RULE_constant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(478);
			_la = _input.LA(1);
			if ( !(((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & ((1L << (INTEGER - 60)) | (1L << (DOUBLE - 60)) | (1L << (LONG - 60)) | (1L << (STRING - 60)) | (1L << (CHAR - 60)))) != 0)) ) {
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
		enterRule(_localctx, 50, RULE_explicitArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(480);
			match(LPAREN);
			setState(489);
			_la = _input.LA(1);
			if (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (LPAREN - 30)) | (1L << (UNDERSCORE - 30)) | (1L << (ANTI - 30)) | (1L << (ID - 30)) | (1L << (INTEGER - 30)) | (1L << (DOUBLE - 30)) | (1L << (LONG - 30)) | (1L << (STRING - 30)) | (1L << (CHAR - 30)))) != 0)) {
				{
				setState(481);
				pattern();
				setState(486);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(482);
					match(COMMA);
					setState(483);
					pattern();
					}
					}
					setState(488);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(491);
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
		enterRule(_localctx, 52, RULE_implicitArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(493);
			match(LSQUAREBR);
			setState(506);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(494);
				match(ID);
				setState(495);
				match(EQUAL);
				setState(496);
				pattern();
				setState(503);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(497);
					match(COMMA);
					setState(498);
					match(ID);
					setState(499);
					match(EQUAL);
					setState(500);
					pattern();
					}
					}
					setState(505);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(508);
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
		enterRule(_localctx, 54, RULE_typeterm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(510);
			match(TYPETERM);
			setState(511);
			((TypetermContext)_localctx).type = match(ID);
			setState(514);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(512);
				match(EXTENDS);
				setState(513);
				((TypetermContext)_localctx).supertype = match(ID);
				}
			}

			setState(516);
			match(LBRACE);
			setState(517);
			implement();
			setState(519);
			_la = _input.LA(1);
			if (_la==IS_SORT) {
				{
				setState(518);
				isSort();
				}
			}

			setState(522);
			_la = _input.LA(1);
			if (_la==EQUALS) {
				{
				setState(521);
				equalsTerm();
				}
			}

			setState(524);
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
		enterRule(_localctx, 56, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(526);
			match(OP);
			setState(527);
			((OperatorContext)_localctx).codomain = match(ID);
			setState(528);
			((OperatorContext)_localctx).opname = match(ID);
			setState(529);
			match(LPAREN);
			setState(531);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(530);
				slotList();
				}
			}

			setState(533);
			match(RPAREN);
			setState(534);
			match(LBRACE);
			setState(541);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IS_FSYM) | (1L << MAKE) | (1L << GET_SLOT) | (1L << GET_DEFAULT))) != 0)) {
				{
				setState(539);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(535);
					isFsym();
					}
					break;
				case MAKE:
					{
					setState(536);
					make();
					}
					break;
				case GET_SLOT:
					{
					setState(537);
					getSlot();
					}
					break;
				case GET_DEFAULT:
					{
					setState(538);
					getDefault();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(543);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(544);
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
		enterRule(_localctx, 58, RULE_oplist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(546);
			match(OPLIST);
			setState(547);
			((OplistContext)_localctx).codomain = match(ID);
			setState(548);
			((OplistContext)_localctx).opname = match(ID);
			setState(549);
			match(LPAREN);
			setState(550);
			((OplistContext)_localctx).domain = match(ID);
			setState(551);
			match(STAR);
			setState(552);
			match(RPAREN);
			setState(553);
			match(LBRACE);
			setState(562);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IS_FSYM) | (1L << MAKE_EMPTY) | (1L << MAKE_INSERT) | (1L << GET_HEAD) | (1L << GET_TAIL) | (1L << IS_EMPTY))) != 0)) {
				{
				setState(560);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(554);
					isFsym();
					}
					break;
				case MAKE_EMPTY:
					{
					setState(555);
					makeEmptyList();
					}
					break;
				case MAKE_INSERT:
					{
					setState(556);
					makeInsertList();
					}
					break;
				case GET_HEAD:
					{
					setState(557);
					getHead();
					}
					break;
				case GET_TAIL:
					{
					setState(558);
					getTail();
					}
					break;
				case IS_EMPTY:
					{
					setState(559);
					isEmptyList();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(564);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(565);
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
		enterRule(_localctx, 60, RULE_oparray);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(567);
			match(OPARRAY);
			setState(568);
			((OparrayContext)_localctx).codomain = match(ID);
			setState(569);
			((OparrayContext)_localctx).opname = match(ID);
			setState(570);
			match(LPAREN);
			setState(571);
			((OparrayContext)_localctx).domain = match(ID);
			setState(572);
			match(STAR);
			setState(573);
			match(RPAREN);
			setState(574);
			match(LBRACE);
			setState(582);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IS_FSYM) | (1L << MAKE_EMPTY) | (1L << MAKE_APPEND) | (1L << GET_ELEMENT) | (1L << GET_SIZE))) != 0)) {
				{
				setState(580);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(575);
					isFsym();
					}
					break;
				case MAKE_EMPTY:
					{
					setState(576);
					makeEmptyArray();
					}
					break;
				case MAKE_APPEND:
					{
					setState(577);
					makeAppendArray();
					}
					break;
				case GET_ELEMENT:
					{
					setState(578);
					getElement();
					}
					break;
				case GET_SIZE:
					{
					setState(579);
					getSize();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(584);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(585);
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
		enterRule(_localctx, 62, RULE_implement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(587);
			match(IMPLEMENT);
			setState(588);
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
		enterRule(_localctx, 64, RULE_equalsTerm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(590);
			match(EQUALS);
			setState(591);
			match(LPAREN);
			setState(592);
			((EqualsTermContext)_localctx).id1 = match(ID);
			setState(593);
			match(COMMA);
			setState(594);
			((EqualsTermContext)_localctx).id2 = match(ID);
			setState(595);
			match(RPAREN);
			setState(596);
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
		enterRule(_localctx, 66, RULE_isSort);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(598);
			match(IS_SORT);
			setState(599);
			match(LPAREN);
			setState(600);
			match(ID);
			setState(601);
			match(RPAREN);
			setState(602);
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
		enterRule(_localctx, 68, RULE_isFsym);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(604);
			match(IS_FSYM);
			setState(605);
			match(LPAREN);
			setState(606);
			match(ID);
			setState(607);
			match(RPAREN);
			setState(608);
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
		enterRule(_localctx, 70, RULE_make);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(610);
			match(MAKE);
			setState(611);
			match(LPAREN);
			setState(620);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(612);
				match(ID);
				setState(617);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(613);
					match(COMMA);
					setState(614);
					match(ID);
					}
					}
					setState(619);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(622);
			match(RPAREN);
			setState(623);
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
		enterRule(_localctx, 72, RULE_makeEmptyList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(625);
			match(MAKE_EMPTY);
			setState(626);
			match(LPAREN);
			setState(627);
			match(RPAREN);
			setState(628);
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
		enterRule(_localctx, 74, RULE_makeEmptyArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(630);
			match(MAKE_EMPTY);
			setState(631);
			match(LPAREN);
			setState(632);
			match(ID);
			setState(633);
			match(RPAREN);
			setState(634);
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
		enterRule(_localctx, 76, RULE_makeAppendArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(636);
			match(MAKE_APPEND);
			setState(637);
			match(LPAREN);
			setState(638);
			((MakeAppendArrayContext)_localctx).id1 = match(ID);
			setState(639);
			match(COMMA);
			setState(640);
			((MakeAppendArrayContext)_localctx).id2 = match(ID);
			setState(641);
			match(RPAREN);
			setState(642);
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
		enterRule(_localctx, 78, RULE_makeInsertList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(644);
			match(MAKE_INSERT);
			setState(645);
			match(LPAREN);
			setState(646);
			((MakeInsertListContext)_localctx).id1 = match(ID);
			setState(647);
			match(COMMA);
			setState(648);
			((MakeInsertListContext)_localctx).id2 = match(ID);
			setState(649);
			match(RPAREN);
			setState(650);
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
		enterRule(_localctx, 80, RULE_getSlot);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(652);
			match(GET_SLOT);
			setState(653);
			match(LPAREN);
			setState(654);
			((GetSlotContext)_localctx).id1 = match(ID);
			setState(655);
			match(COMMA);
			setState(656);
			((GetSlotContext)_localctx).id2 = match(ID);
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
		enterRule(_localctx, 82, RULE_getHead);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(660);
			match(GET_HEAD);
			setState(661);
			match(LPAREN);
			setState(662);
			match(ID);
			setState(663);
			match(RPAREN);
			setState(664);
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
		enterRule(_localctx, 84, RULE_getTail);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(666);
			match(GET_TAIL);
			setState(667);
			match(LPAREN);
			setState(668);
			match(ID);
			setState(669);
			match(RPAREN);
			setState(670);
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
		enterRule(_localctx, 86, RULE_getElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(672);
			match(GET_ELEMENT);
			setState(673);
			match(LPAREN);
			setState(674);
			((GetElementContext)_localctx).id1 = match(ID);
			setState(675);
			match(COMMA);
			setState(676);
			((GetElementContext)_localctx).id2 = match(ID);
			setState(677);
			match(RPAREN);
			setState(678);
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
		enterRule(_localctx, 88, RULE_isEmptyList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(680);
			match(IS_EMPTY);
			setState(681);
			match(LPAREN);
			setState(682);
			match(ID);
			setState(683);
			match(RPAREN);
			setState(684);
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
		enterRule(_localctx, 90, RULE_getSize);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(686);
			match(GET_SIZE);
			setState(687);
			match(LPAREN);
			setState(688);
			match(ID);
			setState(689);
			match(RPAREN);
			setState(690);
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
		enterRule(_localctx, 92, RULE_getDefault);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(692);
			match(GET_DEFAULT);
			setState(693);
			match(LPAREN);
			setState(694);
			match(ID);
			setState(695);
			match(RPAREN);
			setState(696);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3I\u02bd\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\3\2\3\2\7\2c\n\2\f\2\16\2f\13\2\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3r\n\3\3\4\3\4\3\5\3\5\3\5\3\5\3"+
		"\5\7\5{\n\5\f\5\16\5~\13\5\5\5\u0080\n\5\3\5\5\5\u0083\n\5\3\5\3\5\7\5"+
		"\u0087\n\5\f\5\16\5\u008a\13\5\3\5\3\5\3\6\3\6\3\6\3\6\5\6\u0092\n\6\3"+
		"\6\3\6\3\6\3\6\3\6\7\6\u0099\n\6\f\6\16\6\u009c\13\6\3\6\3\6\3\7\3\7\3"+
		"\7\7\7\u00a3\n\7\f\7\16\7\u00a6\13\7\3\7\3\7\3\b\3\b\5\b\u00ac\n\b\3\b"+
		"\3\b\3\t\3\t\3\t\3\t\7\t\u00b4\n\t\f\t\16\t\u00b7\13\t\3\t\3\t\3\n\3\n"+
		"\3\n\3\n\7\n\u00bf\n\n\f\n\16\n\u00c2\13\n\3\n\3\n\3\13\3\13\3\13\5\13"+
		"\u00c9\n\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00d1\n\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00de\n\13\3\f\3\f\3"+
		"\f\3\f\7\f\u00e4\n\f\f\f\16\f\u00e7\13\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16"+
		"\7\16\u00f0\n\16\f\16\16\16\u00f3\13\16\3\17\3\17\5\17\u00f7\n\17\3\17"+
		"\3\17\3\20\3\20\3\20\7\20\u00fe\n\20\f\20\16\20\u0101\13\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\5\21\u0124\n\21\3\21\3\21\3\21\3\21\3\21\3\21\7\21\u012c"+
		"\n\21\f\21\16\21\u012f\13\21\3\22\3\22\5\22\u0133\n\22\3\22\3\22\3\22"+
		"\3\22\3\22\7\22\u013a\n\22\f\22\16\22\u013d\13\22\5\22\u013f\n\22\3\22"+
		"\3\22\5\22\u0143\n\22\3\23\5\23\u0146\n\23\3\23\5\23\u0149\n\23\3\23\3"+
		"\23\3\23\3\23\3\23\7\23\u0150\n\23\f\23\16\23\u0153\13\23\5\23\u0155\n"+
		"\23\3\23\3\23\5\23\u0159\n\23\3\23\5\23\u015c\n\23\3\23\3\23\3\23\3\23"+
		"\3\23\7\23\u0163\n\23\f\23\16\23\u0166\13\23\5\23\u0168\n\23\3\23\3\23"+
		"\5\23\u016c\n\23\3\23\5\23\u016f\n\23\3\23\3\23\5\23\u0173\n\23\3\23\5"+
		"\23\u0176\n\23\3\23\3\23\5\23\u017a\n\23\3\24\3\24\3\24\3\24\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\7\25\u0186\n\25\f\25\16\25\u0189\13\25\5\25\u018b"+
		"\n\25\3\25\3\25\3\25\5\25\u0190\n\25\3\26\3\26\3\26\7\26\u0195\n\26\f"+
		"\26\16\26\u0198\13\26\3\26\3\26\3\26\7\26\u019d\n\26\f\26\16\26\u01a0"+
		"\13\26\3\26\3\26\3\26\5\26\u01a5\n\26\3\26\3\26\3\26\5\26\u01aa\n\26\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u01b9"+
		"\n\27\3\27\3\27\5\27\u01bd\n\27\3\27\3\27\3\27\7\27\u01c2\n\27\f\27\16"+
		"\27\u01c5\13\27\5\27\u01c7\n\27\3\30\3\30\3\30\3\30\3\30\6\30\u01ce\n"+
		"\30\r\30\16\30\u01cf\3\30\3\30\5\30\u01d4\n\30\3\31\3\31\5\31\u01d8\n"+
		"\31\3\31\3\31\5\31\u01dc\n\31\3\31\5\31\u01df\n\31\3\32\3\32\3\33\3\33"+
		"\3\33\3\33\7\33\u01e7\n\33\f\33\16\33\u01ea\13\33\5\33\u01ec\n\33\3\33"+
		"\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\7\34\u01f8\n\34\f\34\16"+
		"\34\u01fb\13\34\5\34\u01fd\n\34\3\34\3\34\3\35\3\35\3\35\3\35\5\35\u0205"+
		"\n\35\3\35\3\35\3\35\5\35\u020a\n\35\3\35\5\35\u020d\n\35\3\35\3\35\3"+
		"\36\3\36\3\36\3\36\3\36\5\36\u0216\n\36\3\36\3\36\3\36\3\36\3\36\3\36"+
		"\7\36\u021e\n\36\f\36\16\36\u0221\13\36\3\36\3\36\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\7\37\u0233\n\37\f\37"+
		"\16\37\u0236\13\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \7"+
		" \u0247\n \f \16 \u024a\13 \3 \3 \3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\""+
		"\3\"\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\7%\u026a\n%\f"+
		"%\16%\u026d\13%\5%\u026f\n%\3%\3%\3%\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3"+
		"\'\3\'\3(\3(\3(\3(\3(\3(\3(\3(\3)\3)\3)\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*"+
		"\3*\3*\3*\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3-\3-\3-"+
		"\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60\3\60"+
		"\6d\u00e5\u0196\u019e\3 \61\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \""+
		"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^\2\5\4\28:<<\3\2\63\64\3\2>B\u02fe"+
		"\2d\3\2\2\2\4q\3\2\2\2\6s\3\2\2\2\bu\3\2\2\2\n\u008d\3\2\2\2\f\u009f\3"+
		"\2\2\2\16\u00a9\3\2\2\2\20\u00af\3\2\2\2\22\u00ba\3\2\2\2\24\u00dd\3\2"+
		"\2\2\26\u00df\3\2\2\2\30\u00ea\3\2\2\2\32\u00ec\3\2\2\2\34\u00f4\3\2\2"+
		"\2\36\u00fa\3\2\2\2 \u0123\3\2\2\2\"\u0142\3\2\2\2$\u0179\3\2\2\2&\u017b"+
		"\3\2\2\2(\u018f\3\2\2\2*\u01a9\3\2\2\2,\u01c6\3\2\2\2.\u01d3\3\2\2\2\60"+
		"\u01de\3\2\2\2\62\u01e0\3\2\2\2\64\u01e2\3\2\2\2\66\u01ef\3\2\2\28\u0200"+
		"\3\2\2\2:\u0210\3\2\2\2<\u0224\3\2\2\2>\u0239\3\2\2\2@\u024d\3\2\2\2B"+
		"\u0250\3\2\2\2D\u0258\3\2\2\2F\u025e\3\2\2\2H\u0264\3\2\2\2J\u0273\3\2"+
		"\2\2L\u0278\3\2\2\2N\u027e\3\2\2\2P\u0286\3\2\2\2R\u028e\3\2\2\2T\u0296"+
		"\3\2\2\2V\u029c\3\2\2\2X\u02a2\3\2\2\2Z\u02aa\3\2\2\2\\\u02b0\3\2\2\2"+
		"^\u02b6\3\2\2\2`c\5\4\3\2ac\5\30\r\2b`\3\2\2\2ba\3\2\2\2cf\3\2\2\2de\3"+
		"\2\2\2db\3\2\2\2e\3\3\2\2\2fd\3\2\2\2gr\5\b\5\2hr\5\n\6\2ir\5\f\7\2jr"+
		"\5\16\b\2kr\58\35\2lr\5:\36\2mr\5<\37\2nr\5> \2or\5(\25\2pr\5\6\4\2qg"+
		"\3\2\2\2qh\3\2\2\2qi\3\2\2\2qj\3\2\2\2qk\3\2\2\2ql\3\2\2\2qm\3\2\2\2q"+
		"n\3\2\2\2qo\3\2\2\2qp\3\2\2\2r\5\3\2\2\2st\7+\2\2t\7\3\2\2\2u\u0082\7"+
		"\3\2\2v\177\7 \2\2w|\5$\23\2xy\7%\2\2y{\5$\23\2zx\3\2\2\2{~\3\2\2\2|z"+
		"\3\2\2\2|}\3\2\2\2}\u0080\3\2\2\2~|\3\2\2\2\177w\3\2\2\2\177\u0080\3\2"+
		"\2\2\u0080\u0081\3\2\2\2\u0081\u0083\7!\2\2\u0082v\3\2\2\2\u0082\u0083"+
		"\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0088\7\36\2\2\u0085\u0087\5\24\13"+
		"\2\u0086\u0085\3\2\2\2\u0087\u008a\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089"+
		"\3\2\2\2\u0089\u008b\3\2\2\2\u008a\u0088\3\2\2\2\u008b\u008c\7\37\2\2"+
		"\u008c\t\3\2\2\2\u008d\u008e\7\4\2\2\u008e\u008f\7<\2\2\u008f\u0091\7"+
		" \2\2\u0090\u0092\5\32\16\2\u0091\u0090\3\2\2\2\u0091\u0092\3\2\2\2\u0092"+
		"\u0093\3\2\2\2\u0093\u0094\7!\2\2\u0094\u0095\7\5\2\2\u0095\u0096\5$\23"+
		"\2\u0096\u009a\7\36\2\2\u0097\u0099\5\22\n\2\u0098\u0097\3\2\2\2\u0099"+
		"\u009c\3\2\2\2\u009a\u0098\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009d\3\2"+
		"\2\2\u009c\u009a\3\2\2\2\u009d\u009e\7\37\2\2\u009e\13\3\2\2\2\u009f\u00a0"+
		"\7\7\2\2\u00a0\u00a4\7\36\2\2\u00a1\u00a3\t\2\2\2\u00a2\u00a1\3\2\2\2"+
		"\u00a3\u00a6\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a7"+
		"\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a7\u00a8\7\37\2\2\u00a8\r\3\2\2\2\u00a9"+
		"\u00ab\7\b\2\2\u00aa\u00ac\5\20\t\2\u00ab\u00aa\3\2\2\2\u00ab\u00ac\3"+
		"\2\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00ae\5\26\f\2\u00ae\17\3\2\2\2\u00af"+
		"\u00b0\7 \2\2\u00b0\u00b5\7=\2\2\u00b1\u00b2\7%\2\2\u00b2\u00b4\7=\2\2"+
		"\u00b3\u00b1\3\2\2\2\u00b4\u00b7\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b6"+
		"\3\2\2\2\u00b6\u00b8\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b8\u00b9\7!\2\2\u00b9"+
		"\21\3\2\2\2\u00ba\u00bb\7\6\2\2\u00bb\u00bc\7<\2\2\u00bc\u00c0\7\36\2"+
		"\2\u00bd\u00bf\5\24\13\2\u00be\u00bd\3\2\2\2\u00bf\u00c2\3\2\2\2\u00c0"+
		"\u00be\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c3\3\2\2\2\u00c2\u00c0\3\2"+
		"\2\2\u00c3\u00c4\7\37\2\2\u00c4\23\3\2\2\2\u00c5\u00c8\5\36\20\2\u00c6"+
		"\u00c7\t\3\2\2\u00c7\u00c9\5 \21\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9\3\2"+
		"\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cb\7$\2\2\u00cb\u00cc\5\26\f\2\u00cc"+
		"\u00de\3\2\2\2\u00cd\u00d0\5\36\20\2\u00ce\u00cf\t\3\2\2\u00cf\u00d1\5"+
		" \21\2\u00d0\u00ce\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2"+
		"\u00d3\7$\2\2\u00d3\u00d4\5$\23\2\u00d4\u00de\3\2\2\2\u00d5\u00d6\5 \21"+
		"\2\u00d6\u00d7\7$\2\2\u00d7\u00d8\5\26\f\2\u00d8\u00de\3\2\2\2\u00d9\u00da"+
		"\5 \21\2\u00da\u00db\7$\2\2\u00db\u00dc\5$\23\2\u00dc\u00de\3\2\2\2\u00dd"+
		"\u00c5\3\2\2\2\u00dd\u00cd\3\2\2\2\u00dd\u00d5\3\2\2\2\u00dd\u00d9\3\2"+
		"\2\2\u00de\25\3\2\2\2\u00df\u00e5\7\36\2\2\u00e0\u00e4\5\4\3\2\u00e1\u00e4"+
		"\5\26\f\2\u00e2\u00e4\5\30\r\2\u00e3\u00e0\3\2\2\2\u00e3\u00e1\3\2\2\2"+
		"\u00e3\u00e2\3\2\2\2\u00e4\u00e7\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e5\u00e3"+
		"\3\2\2\2\u00e6\u00e8\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e8\u00e9\7\37\2\2"+
		"\u00e9\27\3\2\2\2\u00ea\u00eb\13\2\2\2\u00eb\31\3\2\2\2\u00ec\u00f1\5"+
		"\34\17\2\u00ed\u00ee\7%\2\2\u00ee\u00f0\5\34\17\2\u00ef\u00ed\3\2\2\2"+
		"\u00f0\u00f3\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\33"+
		"\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f4\u00f6\7<\2\2\u00f5\u00f7\7&\2\2\u00f6"+
		"\u00f5\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00f9\7<"+
		"\2\2\u00f9\35\3\2\2\2\u00fa\u00ff\5,\27\2\u00fb\u00fc\7%\2\2\u00fc\u00fe"+
		"\5,\27\2\u00fd\u00fb\3\2\2\2\u00fe\u0101\3\2\2\2\u00ff\u00fd\3\2\2\2\u00ff"+
		"\u0100\3\2\2\2\u0100\37\3\2\2\2\u0101\u00ff\3\2\2\2\u0102\u0103\b\21\1"+
		"\2\u0103\u0104\5,\27\2\u0104\u0105\7\34\2\2\u0105\u0106\5$\23\2\u0106"+
		"\u0124\3\2\2\2\u0107\u0108\5\"\22\2\u0108\u0109\7/\2\2\u0109\u010a\5\""+
		"\22\2\u010a\u0124\3\2\2\2\u010b\u010c\5\"\22\2\u010c\u010d\7-\2\2\u010d"+
		"\u010e\5\"\22\2\u010e\u0124\3\2\2\2\u010f\u0110\5\"\22\2\u0110\u0111\7"+
		"\60\2\2\u0111\u0112\5\"\22\2\u0112\u0124\3\2\2\2\u0113\u0114\5\"\22\2"+
		"\u0114\u0115\7.\2\2\u0115\u0116\5\"\22\2\u0116\u0124\3\2\2\2\u0117\u0118"+
		"\5\"\22\2\u0118\u0119\7\61\2\2\u0119\u011a\5\"\22\2\u011a\u0124\3\2\2"+
		"\2\u011b\u011c\5\"\22\2\u011c\u011d\7\62\2\2\u011d\u011e\5\"\22\2\u011e"+
		"\u0124\3\2\2\2\u011f\u0120\7 \2\2\u0120\u0121\5 \21\2\u0121\u0122\7!\2"+
		"\2\u0122\u0124\3\2\2\2\u0123\u0102\3\2\2\2\u0123\u0107\3\2\2\2\u0123\u010b"+
		"\3\2\2\2\u0123\u010f\3\2\2\2\u0123\u0113\3\2\2\2\u0123\u0117\3\2\2\2\u0123"+
		"\u011b\3\2\2\2\u0123\u011f\3\2\2\2\u0124\u012d\3\2\2\2\u0125\u0126\f\f"+
		"\2\2\u0126\u0127\7\63\2\2\u0127\u012c\5 \21\r\u0128\u0129\f\13\2\2\u0129"+
		"\u012a\7\64\2\2\u012a\u012c\5 \21\f\u012b\u0125\3\2\2\2\u012b\u0128\3"+
		"\2\2\2\u012c\u012f\3\2\2\2\u012d\u012b\3\2\2\2\u012d\u012e\3\2\2\2\u012e"+
		"!\3\2\2\2\u012f\u012d\3\2\2\2\u0130\u0132\7<\2\2\u0131\u0133\7\'\2\2\u0132"+
		"\u0131\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u0143\3\2\2\2\u0134\u0135\7<"+
		"\2\2\u0135\u013e\7 \2\2\u0136\u013b\5\"\22\2\u0137\u0138\7%\2\2\u0138"+
		"\u013a\5\"\22\2\u0139\u0137\3\2\2\2\u013a\u013d\3\2\2\2\u013b\u0139\3"+
		"\2\2\2\u013b\u013c\3\2\2\2\u013c\u013f\3\2\2\2\u013d\u013b\3\2\2\2\u013e"+
		"\u0136\3\2\2\2\u013e\u013f\3\2\2\2\u013f\u0140\3\2\2\2\u0140\u0143\7!"+
		"\2\2\u0141\u0143\5\62\32\2\u0142\u0130\3\2\2\2\u0142\u0134\3\2\2\2\u0142"+
		"\u0141\3\2\2\2\u0143#\3\2\2\2\u0144\u0146\7<\2\2\u0145\u0144\3\2\2\2\u0145"+
		"\u0146\3\2\2\2\u0146\u0148\3\2\2\2\u0147\u0149\7;\2\2\u0148\u0147\3\2"+
		"\2\2\u0148\u0149\3\2\2\2\u0149\u014a\3\2\2\2\u014a\u014b\7<\2\2\u014b"+
		"\u0154\7 \2\2\u014c\u0151\5$\23\2\u014d\u014e\7%\2\2\u014e\u0150\5$\23"+
		"\2\u014f\u014d\3\2\2\2\u0150\u0153\3\2\2\2\u0151\u014f\3\2\2\2\u0151\u0152"+
		"\3\2\2\2\u0152\u0155\3\2\2\2\u0153\u0151\3\2\2\2\u0154\u014c\3\2\2\2\u0154"+
		"\u0155\3\2\2\2\u0155\u0156\3\2\2\2\u0156\u017a\7!\2\2\u0157\u0159\7<\2"+
		"\2\u0158\u0157\3\2\2\2\u0158\u0159\3\2\2\2\u0159\u015b\3\2\2\2\u015a\u015c"+
		"\7;\2\2\u015b\u015a\3\2\2\2\u015b\u015c\3\2\2\2\u015c\u015d\3\2\2\2\u015d"+
		"\u015e\7<\2\2\u015e\u0167\7\"\2\2\u015f\u0164\5&\24\2\u0160\u0161\7%\2"+
		"\2\u0161\u0163\5&\24\2\u0162\u0160\3\2\2\2\u0163\u0166\3\2\2\2\u0164\u0162"+
		"\3\2\2\2\u0164\u0165\3\2\2\2\u0165\u0168\3\2\2\2\u0166\u0164\3\2\2\2\u0167"+
		"\u015f\3\2\2\2\u0167\u0168\3\2\2\2\u0168\u0169\3\2\2\2\u0169\u017a\7#"+
		"\2\2\u016a\u016c\7<\2\2\u016b\u016a\3\2\2\2\u016b\u016c\3\2\2\2\u016c"+
		"\u016e\3\2\2\2\u016d\u016f\7;\2\2\u016e\u016d\3\2\2\2\u016e\u016f\3\2"+
		"\2\2\u016f\u0170\3\2\2\2\u0170\u0172\7<\2\2\u0171\u0173\7\'\2\2\u0172"+
		"\u0171\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u017a\3\2\2\2\u0174\u0176\7<"+
		"\2\2\u0175\u0174\3\2\2\2\u0175\u0176\3\2\2\2\u0176\u0177\3\2\2\2\u0177"+
		"\u017a\5\62\32\2\u0178\u017a\7(\2\2\u0179\u0145\3\2\2\2\u0179\u0158\3"+
		"\2\2\2\u0179\u016b\3\2\2\2\u0179\u0175\3\2\2\2\u0179\u0178\3\2\2\2\u017a"+
		"%\3\2\2\2\u017b\u017c\7<\2\2\u017c\u017d\7\35\2\2\u017d\u017e\5$\23\2"+
		"\u017e\'\3\2\2\2\u017f\u0180\7;\2\2\u0180\u0181\7<\2\2\u0181\u018a\7\""+
		"\2\2\u0182\u0187\5&\24\2\u0183\u0184\7%\2\2\u0184\u0186\5&\24\2\u0185"+
		"\u0183\3\2\2\2\u0186\u0189\3\2\2\2\u0187\u0185\3\2\2\2\u0187\u0188\3\2"+
		"\2\2\u0188\u018b\3\2\2\2\u0189\u0187\3\2\2\2\u018a\u0182\3\2\2\2\u018a"+
		"\u018b\3\2\2\2\u018b\u018c\3\2\2\2\u018c\u0190\7#\2\2\u018d\u018e\7;\2"+
		"\2\u018e\u0190\5*\26\2\u018f\u017f\3\2\2\2\u018f\u018d\3\2\2\2\u0190)"+
		"\3\2\2\2\u0191\u0192\7<\2\2\u0192\u0196\7 \2\2\u0193\u0195\5*\26\2\u0194"+
		"\u0193\3\2\2\2\u0195\u0198\3\2\2\2\u0196\u0197\3\2\2\2\u0196\u0194\3\2"+
		"\2\2\u0197\u0199\3\2\2\2\u0198\u0196\3\2\2\2\u0199\u01aa\7!\2\2\u019a"+
		"\u019e\7 \2\2\u019b\u019d\5*\26\2\u019c\u019b\3\2\2\2\u019d\u01a0\3\2"+
		"\2\2\u019e\u019f\3\2\2\2\u019e\u019c\3\2\2\2\u019f\u01a1\3\2\2\2\u01a0"+
		"\u019e\3\2\2\2\u01a1\u01aa\7!\2\2\u01a2\u01a4\7<\2\2\u01a3\u01a5\7\'\2"+
		"\2\u01a4\u01a3\3\2\2\2\u01a4\u01a5\3\2\2\2\u01a5\u01aa\3\2\2\2\u01a6\u01aa"+
		"\5\62\32\2\u01a7\u01aa\7(\2\2\u01a8\u01aa\5\30\r\2\u01a9\u0191\3\2\2\2"+
		"\u01a9\u019a\3\2\2\2\u01a9\u01a2\3\2\2\2\u01a9\u01a6\3\2\2\2\u01a9\u01a7"+
		"\3\2\2\2\u01a9\u01a8\3\2\2\2\u01aa+\3\2\2\2\u01ab\u01ac\7<\2\2\u01ac\u01ad"+
		"\7*\2\2\u01ad\u01c7\5,\27\2\u01ae\u01af\7)\2\2\u01af\u01c7\5,\27\2\u01b0"+
		"\u01b1\5.\30\2\u01b1\u01b2\5\64\33\2\u01b2\u01c7\3\2\2\2\u01b3\u01b4\5"+
		".\30\2\u01b4\u01b5\5\66\34\2\u01b5\u01c7\3\2\2\2\u01b6\u01b8\7<\2\2\u01b7"+
		"\u01b9\7\'\2\2\u01b8\u01b7\3\2\2\2\u01b8\u01b9\3\2\2\2\u01b9\u01c7\3\2"+
		"\2\2\u01ba\u01bc\7(\2\2\u01bb\u01bd\7\'\2\2\u01bc\u01bb\3\2\2\2\u01bc"+
		"\u01bd\3\2\2\2\u01bd\u01c7\3\2\2\2\u01be\u01c3\5\62\32\2\u01bf\u01c0\7"+
		"\65\2\2\u01c0\u01c2\5\62\32\2\u01c1\u01bf\3\2\2\2\u01c2\u01c5\3\2\2\2"+
		"\u01c3\u01c1\3\2\2\2\u01c3\u01c4\3\2\2\2\u01c4\u01c7\3\2\2\2\u01c5\u01c3"+
		"\3\2\2\2\u01c6\u01ab\3\2\2\2\u01c6\u01ae\3\2\2\2\u01c6\u01b0\3\2\2\2\u01c6"+
		"\u01b3\3\2\2\2\u01c6\u01b6\3\2\2\2\u01c6\u01ba\3\2\2\2\u01c6\u01be\3\2"+
		"\2\2\u01c7-\3\2\2\2\u01c8\u01d4\5\60\31\2\u01c9\u01ca\7 \2\2\u01ca\u01cd"+
		"\5\60\31\2\u01cb\u01cc\7\65\2\2\u01cc\u01ce\5\60\31\2\u01cd\u01cb\3\2"+
		"\2\2\u01ce\u01cf\3\2\2\2\u01cf\u01cd\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0"+
		"\u01d1\3\2\2\2\u01d1\u01d2\7!\2\2\u01d2\u01d4\3\2\2\2\u01d3\u01c8\3\2"+
		"\2\2\u01d3\u01c9\3\2\2\2\u01d4/\3\2\2\2\u01d5\u01d7\7<\2\2\u01d6\u01d8"+
		"\7\66\2\2\u01d7\u01d6\3\2\2\2\u01d7\u01d8\3\2\2\2\u01d8\u01df\3\2\2\2"+
		"\u01d9\u01db\7<\2\2\u01da\u01dc\7\67\2\2\u01db\u01da\3\2\2\2\u01db\u01dc"+
		"\3\2\2\2\u01dc\u01df\3\2\2\2\u01dd\u01df\5\62\32\2\u01de\u01d5\3\2\2\2"+
		"\u01de\u01d9\3\2\2\2\u01de\u01dd\3\2\2\2\u01df\61\3\2\2\2\u01e0\u01e1"+
		"\t\4\2\2\u01e1\63\3\2\2\2\u01e2\u01eb\7 \2\2\u01e3\u01e8\5,\27\2\u01e4"+
		"\u01e5\7%\2\2\u01e5\u01e7\5,\27\2\u01e6\u01e4\3\2\2\2\u01e7\u01ea\3\2"+
		"\2\2\u01e8\u01e6\3\2\2\2\u01e8\u01e9\3\2\2\2\u01e9\u01ec\3\2\2\2\u01ea"+
		"\u01e8\3\2\2\2\u01eb\u01e3\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec\u01ed\3\2"+
		"\2\2\u01ed\u01ee\7!\2\2\u01ee\65\3\2\2\2\u01ef\u01fc\7\"\2\2\u01f0\u01f1"+
		"\7<\2\2\u01f1\u01f2\7\35\2\2\u01f2\u01f9\5,\27\2\u01f3\u01f4\7%\2\2\u01f4"+
		"\u01f5\7<\2\2\u01f5\u01f6\7\35\2\2\u01f6\u01f8\5,\27\2\u01f7\u01f3\3\2"+
		"\2\2\u01f8\u01fb\3\2\2\2\u01f9\u01f7\3\2\2\2\u01f9\u01fa\3\2\2\2\u01fa"+
		"\u01fd\3\2\2\2\u01fb\u01f9\3\2\2\2\u01fc\u01f0\3\2\2\2\u01fc\u01fd\3\2"+
		"\2\2\u01fd\u01fe\3\2\2\2\u01fe\u01ff\7#\2\2\u01ff\67\3\2\2\2\u0200\u0201"+
		"\7\f\2\2\u0201\u0204\7<\2\2\u0202\u0203\7\5\2\2\u0203\u0205\7<\2\2\u0204"+
		"\u0202\3\2\2\2\u0204\u0205\3\2\2\2\u0205\u0206\3\2\2\2\u0206\u0207\7\36"+
		"\2\2\u0207\u0209\5@!\2\u0208\u020a\5D#\2\u0209\u0208\3\2\2\2\u0209\u020a"+
		"\3\2\2\2\u020a\u020c\3\2\2\2\u020b\u020d\5B\"\2\u020c\u020b\3\2\2\2\u020c"+
		"\u020d\3\2\2\2\u020d\u020e\3\2\2\2\u020e\u020f\7\37\2\2\u020f9\3\2\2\2"+
		"\u0210\u0211\7\t\2\2\u0211\u0212\7<\2\2\u0212\u0213\7<\2\2\u0213\u0215"+
		"\7 \2\2\u0214\u0216\5\32\16\2\u0215\u0214\3\2\2\2\u0215\u0216\3\2\2\2"+
		"\u0216\u0217\3\2\2\2\u0217\u0218\7!\2\2\u0218\u021f\7\36\2\2\u0219\u021e"+
		"\5F$\2\u021a\u021e\5H%\2\u021b\u021e\5R*\2\u021c\u021e\5^\60\2\u021d\u0219"+
		"\3\2\2\2\u021d\u021a\3\2\2\2\u021d\u021b\3\2\2\2\u021d\u021c\3\2\2\2\u021e"+
		"\u0221\3\2\2\2\u021f\u021d\3\2\2\2\u021f\u0220\3\2\2\2\u0220\u0222\3\2"+
		"\2\2\u0221\u021f\3\2\2\2\u0222\u0223\7\37\2\2\u0223;\3\2\2\2\u0224\u0225"+
		"\7\13\2\2\u0225\u0226\7<\2\2\u0226\u0227\7<\2\2\u0227\u0228\7 \2\2\u0228"+
		"\u0229\7<\2\2\u0229\u022a\7\'\2\2\u022a\u022b\7!\2\2\u022b\u0234\7\36"+
		"\2\2\u022c\u0233\5F$\2\u022d\u0233\5J&\2\u022e\u0233\5P)\2\u022f\u0233"+
		"\5T+\2\u0230\u0233\5V,\2\u0231\u0233\5Z.\2\u0232\u022c\3\2\2\2\u0232\u022d"+
		"\3\2\2\2\u0232\u022e\3\2\2\2\u0232\u022f\3\2\2\2\u0232\u0230\3\2\2\2\u0232"+
		"\u0231\3\2\2\2\u0233\u0236\3\2\2\2\u0234\u0232\3\2\2\2\u0234\u0235\3\2"+
		"\2\2\u0235\u0237\3\2\2\2\u0236\u0234\3\2\2\2\u0237\u0238\7\37\2\2\u0238"+
		"=\3\2\2\2\u0239\u023a\7\n\2\2\u023a\u023b\7<\2\2\u023b\u023c\7<\2\2\u023c"+
		"\u023d\7 \2\2\u023d\u023e\7<\2\2\u023e\u023f\7\'\2\2\u023f\u0240\7!\2"+
		"\2\u0240\u0248\7\36\2\2\u0241\u0247\5F$\2\u0242\u0247\5L\'\2\u0243\u0247"+
		"\5N(\2\u0244\u0247\5X-\2\u0245\u0247\5\\/\2\u0246\u0241\3\2\2\2\u0246"+
		"\u0242\3\2\2\2\u0246\u0243\3\2\2\2\u0246\u0244\3\2\2\2\u0246\u0245\3\2"+
		"\2\2\u0247\u024a\3\2\2\2\u0248\u0246\3\2\2\2\u0248\u0249\3\2\2\2\u0249"+
		"\u024b\3\2\2\2\u024a\u0248\3\2\2\2\u024b\u024c\7\37\2\2\u024c?\3\2\2\2"+
		"\u024d\u024e\7\32\2\2\u024e\u024f\5\26\f\2\u024fA\3\2\2\2\u0250\u0251"+
		"\7\33\2\2\u0251\u0252\7 \2\2\u0252\u0253\7<\2\2\u0253\u0254\7%\2\2\u0254"+
		"\u0255\7<\2\2\u0255\u0256\7!\2\2\u0256\u0257\5\26\f\2\u0257C\3\2\2\2\u0258"+
		"\u0259\7\16\2\2\u0259\u025a\7 \2\2\u025a\u025b\7<\2\2\u025b\u025c\7!\2"+
		"\2\u025c\u025d\5\26\f\2\u025dE\3\2\2\2\u025e\u025f\7\r\2\2\u025f\u0260"+
		"\7 \2\2\u0260\u0261\7<\2\2\u0261\u0262\7!\2\2\u0262\u0263\5\26\f\2\u0263"+
		"G\3\2\2\2\u0264\u0265\7\17\2\2\u0265\u026e\7 \2\2\u0266\u026b\7<\2\2\u0267"+
		"\u0268\7%\2\2\u0268\u026a\7<\2\2\u0269\u0267\3\2\2\2\u026a\u026d\3\2\2"+
		"\2\u026b\u0269\3\2\2\2\u026b\u026c\3\2\2\2\u026c\u026f\3\2\2\2\u026d\u026b"+
		"\3\2\2\2\u026e\u0266\3\2\2\2\u026e\u026f\3\2\2\2\u026f\u0270\3\2\2\2\u0270"+
		"\u0271\7!\2\2\u0271\u0272\5\26\f\2\u0272I\3\2\2\2\u0273\u0274\7\20\2\2"+
		"\u0274\u0275\7 \2\2\u0275\u0276\7!\2\2\u0276\u0277\5\26\f\2\u0277K\3\2"+
		"\2\2\u0278\u0279\7\20\2\2\u0279\u027a\7 \2\2\u027a\u027b\7<\2\2\u027b"+
		"\u027c\7!\2\2\u027c\u027d\5\26\f\2\u027dM\3\2\2\2\u027e\u027f\7\21\2\2"+
		"\u027f\u0280\7 \2\2\u0280\u0281\7<\2\2\u0281\u0282\7%\2\2\u0282\u0283"+
		"\7<\2\2\u0283\u0284\7!\2\2\u0284\u0285\5\26\f\2\u0285O\3\2\2\2\u0286\u0287"+
		"\7\22\2\2\u0287\u0288\7 \2\2\u0288\u0289\7<\2\2\u0289\u028a\7%\2\2\u028a"+
		"\u028b\7<\2\2\u028b\u028c\7!\2\2\u028c\u028d\5\26\f\2\u028dQ\3\2\2\2\u028e"+
		"\u028f\7\23\2\2\u028f\u0290\7 \2\2\u0290\u0291\7<\2\2\u0291\u0292\7%\2"+
		"\2\u0292\u0293\7<\2\2\u0293\u0294\7!\2\2\u0294\u0295\5\26\f\2\u0295S\3"+
		"\2\2\2\u0296\u0297\7\26\2\2\u0297\u0298\7 \2\2\u0298\u0299\7<\2\2\u0299"+
		"\u029a\7!\2\2\u029a\u029b\5\26\f\2\u029bU\3\2\2\2\u029c\u029d\7\27\2\2"+
		"\u029d\u029e\7 \2\2\u029e\u029f\7<\2\2\u029f\u02a0\7!\2\2\u02a0\u02a1"+
		"\5\26\f\2\u02a1W\3\2\2\2\u02a2\u02a3\7\25\2\2\u02a3\u02a4\7 \2\2\u02a4"+
		"\u02a5\7<\2\2\u02a5\u02a6\7%\2\2\u02a6\u02a7\7<\2\2\u02a7\u02a8\7!\2\2"+
		"\u02a8\u02a9\5\26\f\2\u02a9Y\3\2\2\2\u02aa\u02ab\7\31\2\2\u02ab\u02ac"+
		"\7 \2\2\u02ac\u02ad\7<\2\2\u02ad\u02ae\7!\2\2\u02ae\u02af\5\26\f\2\u02af"+
		"[\3\2\2\2\u02b0\u02b1\7\30\2\2\u02b1\u02b2\7 \2\2\u02b2\u02b3\7<\2\2\u02b3"+
		"\u02b4\7!\2\2\u02b4\u02b5\5\26\f\2\u02b5]\3\2\2\2\u02b6\u02b7\7\24\2\2"+
		"\u02b7\u02b8\7 \2\2\u02b8\u02b9\7<\2\2\u02b9\u02ba\7!\2\2\u02ba\u02bb"+
		"\5\26\f\2\u02bb_\3\2\2\2Kbdq|\177\u0082\u0088\u0091\u009a\u00a4\u00ab"+
		"\u00b5\u00c0\u00c8\u00d0\u00dd\u00e3\u00e5\u00f1\u00f6\u00ff\u0123\u012b"+
		"\u012d\u0132\u013b\u013e\u0142\u0145\u0148\u0151\u0154\u0158\u015b\u0164"+
		"\u0167\u016b\u016e\u0172\u0175\u0179\u0187\u018a\u018f\u0196\u019e\u01a4"+
		"\u01a9\u01b8\u01bc\u01c3\u01c6\u01cf\u01d3\u01d7\u01db\u01de\u01e8\u01eb"+
		"\u01f9\u01fc\u0204\u0209\u020c\u0215\u021d\u021f\u0232\u0234\u0246\u0248"+
		"\u026b\u026e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}