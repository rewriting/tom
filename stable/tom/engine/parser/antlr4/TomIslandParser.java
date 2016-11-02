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
		WS=70, NL=71, ANY=72;
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
		"MLCOMMENT", "SLCOMMENT", "WS", "NL", "ANY"
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
			case LMETAQUOTE:
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
		public List<BqcompositeContext> bqcomposite() {
			return getRuleContexts(BqcompositeContext.class);
		}
		public BqcompositeContext bqcomposite(int i) {
			return getRuleContext(BqcompositeContext.class,i);
		}
		public List<CompositeContext> composite() {
			return getRuleContexts(CompositeContext.class);
		}
		public CompositeContext composite(int i) {
			return getRuleContext(CompositeContext.class,i);
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
			setState(113);
			match(LMETAQUOTE);
			setState(124);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					setState(122);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						setState(114);
						match(AT);
						setState(117);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
						case 1:
							{
							setState(115);
							bqcomposite();
							}
							break;
						case 2:
							{
							setState(116);
							composite();
							}
							break;
						}
						setState(119);
						match(AT);
						}
						break;
					case 2:
						{
						setState(121);
						water();
						}
						break;
					}
					} 
				}
				setState(126);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(127);
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
			setState(129);
			match(MATCH);
			setState(142);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(130);
				match(LPAREN);
				setState(139);
				_la = _input.LA(1);
				if (((((_la - 38)) & ~0x3f) == 0 && ((1L << (_la - 38)) & ((1L << (UNDERSCORE - 38)) | (1L << (BQUOTE - 38)) | (1L << (ID - 38)) | (1L << (INTEGER - 38)) | (1L << (DOUBLE - 38)) | (1L << (LONG - 38)) | (1L << (STRING - 38)) | (1L << (CHAR - 38)))) != 0)) {
					{
					setState(131);
					bqterm();
					setState(136);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(132);
						match(COMMA);
						setState(133);
						bqterm();
						}
						}
						setState(138);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(141);
				match(RPAREN);
				}
			}

			setState(144);
			match(LBRACE);
			setState(148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (LPAREN - 30)) | (1L << (UNDERSCORE - 30)) | (1L << (ANTI - 30)) | (1L << (ID - 30)) | (1L << (INTEGER - 30)) | (1L << (DOUBLE - 30)) | (1L << (LONG - 30)) | (1L << (STRING - 30)) | (1L << (CHAR - 30)))) != 0)) {
				{
				{
				setState(145);
				actionRule();
				}
				}
				setState(150);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(151);
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
			setState(153);
			match(STRATEGY);
			setState(154);
			match(ID);
			setState(155);
			match(LPAREN);
			setState(157);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(156);
				slotList();
				}
			}

			setState(159);
			match(RPAREN);
			setState(160);
			match(EXTENDS);
			setState(161);
			bqterm();
			setState(162);
			match(LBRACE);
			setState(166);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VISIT) {
				{
				{
				setState(163);
				visit();
				}
				}
				setState(168);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(169);
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
			setState(171);
			match(INCLUDE);
			setState(172);
			match(LBRACE);
			setState(176);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SLASH) | (1L << BACKSLASH) | (1L << DOT) | (1L << ID))) != 0)) {
				{
				{
				setState(173);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SLASH) | (1L << BACKSLASH) | (1L << DOT) | (1L << ID))) != 0)) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				}
				setState(178);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(179);
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
			setState(181);
			match(GOM);
			setState(183);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(182);
				gomOptions();
				}
			}

			setState(185);
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
			setState(187);
			match(LPAREN);
			setState(188);
			match(DMINUSID);
			setState(193);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(189);
				match(COMMA);
				setState(190);
				match(DMINUSID);
				}
				}
				setState(195);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(196);
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
			setState(198);
			match(VISIT);
			setState(199);
			match(ID);
			setState(200);
			match(LBRACE);
			setState(204);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (LPAREN - 30)) | (1L << (UNDERSCORE - 30)) | (1L << (ANTI - 30)) | (1L << (ID - 30)) | (1L << (INTEGER - 30)) | (1L << (DOUBLE - 30)) | (1L << (LONG - 30)) | (1L << (STRING - 30)) | (1L << (CHAR - 30)))) != 0)) {
				{
				{
				setState(201);
				actionRule();
				}
				}
				setState(206);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(207);
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
			setState(233);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(209);
				patternlist();
				setState(212);
				_la = _input.LA(1);
				if (_la==AND || _la==OR) {
					{
					setState(210);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(211);
					constraint(0);
					}
				}

				setState(214);
				match(ARROW);
				setState(215);
				block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
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
				bqterm();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(225);
				((ActionRuleContext)_localctx).c = constraint(0);
				setState(226);
				match(ARROW);
				setState(227);
				block();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(229);
				((ActionRuleContext)_localctx).c = constraint(0);
				setState(230);
				match(ARROW);
				setState(231);
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
			setState(235);
			match(LBRACE);
			setState(241);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					setState(239);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
					case 1:
						{
						setState(236);
						island();
						}
						break;
					case 2:
						{
						setState(237);
						block();
						}
						break;
					case 3:
						{
						setState(238);
						water();
						}
						break;
					}
					} 
				}
				setState(243);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			}
			setState(244);
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
			setState(246);
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
			setState(248);
			slot();
			setState(253);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(249);
				match(COMMA);
				setState(250);
				slot();
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
			setState(256);
			((SlotContext)_localctx).id1 = match(ID);
			setState(258);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(257);
				match(COLON);
				}
			}

			setState(260);
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
			setState(262);
			pattern();
			setState(267);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(263);
				match(COMMA);
				setState(264);
				pattern();
				}
				}
				setState(269);
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
			setState(303);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(271);
				pattern();
				setState(272);
				match(MATCH_SYMBOL);
				setState(273);
				bqterm();
				}
				break;
			case 2:
				{
				setState(275);
				term();
				setState(276);
				match(GREATERTHAN);
				setState(277);
				term();
				}
				break;
			case 3:
				{
				setState(279);
				term();
				setState(280);
				match(GREATEROREQ);
				setState(281);
				term();
				}
				break;
			case 4:
				{
				setState(283);
				term();
				setState(284);
				match(LOWERTHAN);
				setState(285);
				term();
				}
				break;
			case 5:
				{
				setState(287);
				term();
				setState(288);
				match(LOWEROREQ);
				setState(289);
				term();
				}
				break;
			case 6:
				{
				setState(291);
				term();
				setState(292);
				match(DOUBLEEQ);
				setState(293);
				term();
				}
				break;
			case 7:
				{
				setState(295);
				term();
				setState(296);
				match(DIFFERENT);
				setState(297);
				term();
				}
				break;
			case 8:
				{
				setState(299);
				match(LPAREN);
				setState(300);
				((ConstraintContext)_localctx).c = constraint(0);
				setState(301);
				match(RPAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(313);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(311);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
					case 1:
						{
						_localctx = new ConstraintContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_constraint);
						setState(305);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(306);
						match(AND);
						setState(307);
						constraint(11);
						}
						break;
					case 2:
						{
						_localctx = new ConstraintContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_constraint);
						setState(308);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(309);
						match(OR);
						setState(310);
						constraint(10);
						}
						break;
					}
					} 
				}
				setState(315);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
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
			setState(334);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(316);
				((TermContext)_localctx).var = match(ID);
				setState(318);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
				case 1:
					{
					setState(317);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(320);
				((TermContext)_localctx).fsym = match(ID);
				setState(321);
				match(LPAREN);
				setState(330);
				_la = _input.LA(1);
				if (((((_la - 59)) & ~0x3f) == 0 && ((1L << (_la - 59)) & ((1L << (ID - 59)) | (1L << (INTEGER - 59)) | (1L << (DOUBLE - 59)) | (1L << (LONG - 59)) | (1L << (STRING - 59)) | (1L << (CHAR - 59)))) != 0)) {
					{
					setState(322);
					term();
					setState(327);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(323);
						match(COMMA);
						setState(324);
						term();
						}
						}
						setState(329);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(332);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(333);
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
			setState(389);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(337);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
				case 1:
					{
					setState(336);
					((BqtermContext)_localctx).codomain = match(ID);
					}
					break;
				}
				setState(340);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(339);
					match(BQUOTE);
					}
				}

				setState(342);
				((BqtermContext)_localctx).fsym = match(ID);
				setState(343);
				match(LPAREN);
				setState(352);
				_la = _input.LA(1);
				if (((((_la - 38)) & ~0x3f) == 0 && ((1L << (_la - 38)) & ((1L << (UNDERSCORE - 38)) | (1L << (BQUOTE - 38)) | (1L << (ID - 38)) | (1L << (INTEGER - 38)) | (1L << (DOUBLE - 38)) | (1L << (LONG - 38)) | (1L << (STRING - 38)) | (1L << (CHAR - 38)))) != 0)) {
					{
					setState(344);
					bqterm();
					setState(349);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(345);
						match(COMMA);
						setState(346);
						bqterm();
						}
						}
						setState(351);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(354);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(356);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
				case 1:
					{
					setState(355);
					((BqtermContext)_localctx).codomain = match(ID);
					}
					break;
				}
				setState(359);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(358);
					match(BQUOTE);
					}
				}

				setState(361);
				((BqtermContext)_localctx).fsym = match(ID);
				setState(362);
				match(LSQUAREBR);
				setState(371);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(363);
					pairSlotBqterm();
					setState(368);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(364);
						match(COMMA);
						setState(365);
						pairSlotBqterm();
						}
						}
						setState(370);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(373);
				match(RSQUAREBR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(375);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
				case 1:
					{
					setState(374);
					((BqtermContext)_localctx).codomain = match(ID);
					}
					break;
				}
				setState(378);
				_la = _input.LA(1);
				if (_la==BQUOTE) {
					{
					setState(377);
					match(BQUOTE);
					}
				}

				setState(380);
				((BqtermContext)_localctx).var = match(ID);
				setState(382);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
				case 1:
					{
					setState(381);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(385);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(384);
					((BqtermContext)_localctx).codomain = match(ID);
					}
				}

				setState(387);
				constant();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(388);
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
			setState(391);
			match(ID);
			setState(392);
			match(EQUAL);
			setState(393);
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
			setState(411);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(395);
				match(BQUOTE);
				setState(396);
				((BqcompositeContext)_localctx).fsym = match(ID);
				setState(397);
				match(LSQUAREBR);
				setState(406);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(398);
					pairSlotBqterm();
					setState(403);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(399);
						match(COMMA);
						setState(400);
						pairSlotBqterm();
						}
						}
						setState(405);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(408);
				match(RSQUAREBR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(409);
				match(BQUOTE);
				setState(410);
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
			setState(437);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(413);
				((CompositeContext)_localctx).fsym = match(ID);
				setState(414);
				match(LPAREN);
				setState(418);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						{
						setState(415);
						composite();
						}
						} 
					}
					setState(420);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
				}
				setState(421);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(422);
				match(LPAREN);
				setState(426);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						{
						setState(423);
						composite();
						}
						} 
					}
					setState(428);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
				}
				setState(429);
				match(RPAREN);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(430);
				((CompositeContext)_localctx).var = match(ID);
				setState(432);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
				case 1:
					{
					setState(431);
					match(STAR);
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(434);
				constant();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(435);
				match(UNDERSCORE);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(436);
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
		enterRule(_localctx, 42, RULE_pattern);
		int _la;
		try {
			setState(462);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(439);
				match(ID);
				setState(440);
				match(AT);
				setState(441);
				pattern();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(442);
				match(ANTI);
				setState(443);
				pattern();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(444);
				fsymbol();
				setState(445);
				explicitArgs();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(447);
				fsymbol();
				setState(448);
				implicitArgs();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(450);
				((PatternContext)_localctx).var = match(ID);
				setState(452);
				_la = _input.LA(1);
				if (_la==STAR) {
					{
					setState(451);
					match(STAR);
					}
				}

				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(454);
				match(UNDERSCORE);
				setState(456);
				_la = _input.LA(1);
				if (_la==STAR) {
					{
					setState(455);
					match(STAR);
					}
				}

				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(458);
				constant();
				setState(460);
				_la = _input.LA(1);
				if (_la==STAR) {
					{
					setState(459);
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
		enterRule(_localctx, 44, RULE_fsymbol);
		int _la;
		try {
			setState(476);
			switch (_input.LA(1)) {
			case ID:
			case INTEGER:
			case DOUBLE:
			case LONG:
			case STRING:
			case CHAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(464);
				headSymbol();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(465);
				match(LPAREN);
				setState(466);
				headSymbol();
				setState(471);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PIPE) {
					{
					{
					setState(467);
					match(PIPE);
					setState(468);
					headSymbol();
					}
					}
					setState(473);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(474);
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
			setState(487);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(478);
				match(ID);
				setState(480);
				_la = _input.LA(1);
				if (_la==QMARK) {
					{
					setState(479);
					match(QMARK);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(482);
				match(ID);
				setState(484);
				_la = _input.LA(1);
				if (_la==DQMARK) {
					{
					setState(483);
					match(DQMARK);
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(486);
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
			setState(489);
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
		enterRule(_localctx, 50, RULE_explicitArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(491);
			match(LPAREN);
			setState(500);
			_la = _input.LA(1);
			if (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (LPAREN - 30)) | (1L << (UNDERSCORE - 30)) | (1L << (ANTI - 30)) | (1L << (ID - 30)) | (1L << (INTEGER - 30)) | (1L << (DOUBLE - 30)) | (1L << (LONG - 30)) | (1L << (STRING - 30)) | (1L << (CHAR - 30)))) != 0)) {
				{
				setState(492);
				pattern();
				setState(497);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(493);
					match(COMMA);
					setState(494);
					pattern();
					}
					}
					setState(499);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(502);
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
			setState(504);
			match(LSQUAREBR);
			setState(517);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(505);
				match(ID);
				setState(506);
				match(EQUAL);
				setState(507);
				pattern();
				setState(514);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(508);
					match(COMMA);
					setState(509);
					match(ID);
					setState(510);
					match(EQUAL);
					setState(511);
					pattern();
					}
					}
					setState(516);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(519);
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
			setState(521);
			match(TYPETERM);
			setState(522);
			((TypetermContext)_localctx).type = match(ID);
			setState(525);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(523);
				match(EXTENDS);
				setState(524);
				((TypetermContext)_localctx).supertype = match(ID);
				}
			}

			setState(527);
			match(LBRACE);
			setState(528);
			implement();
			setState(530);
			_la = _input.LA(1);
			if (_la==IS_SORT) {
				{
				setState(529);
				isSort();
				}
			}

			setState(533);
			_la = _input.LA(1);
			if (_la==EQUALS) {
				{
				setState(532);
				equalsTerm();
				}
			}

			setState(535);
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
			setState(537);
			match(OP);
			setState(538);
			((OperatorContext)_localctx).codomain = match(ID);
			setState(539);
			((OperatorContext)_localctx).opname = match(ID);
			setState(540);
			match(LPAREN);
			setState(542);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(541);
				slotList();
				}
			}

			setState(544);
			match(RPAREN);
			setState(545);
			match(LBRACE);
			setState(552);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IS_FSYM) | (1L << MAKE) | (1L << GET_SLOT) | (1L << GET_DEFAULT))) != 0)) {
				{
				setState(550);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(546);
					isFsym();
					}
					break;
				case MAKE:
					{
					setState(547);
					make();
					}
					break;
				case GET_SLOT:
					{
					setState(548);
					getSlot();
					}
					break;
				case GET_DEFAULT:
					{
					setState(549);
					getDefault();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(554);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(555);
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
			setState(557);
			match(OPLIST);
			setState(558);
			((OplistContext)_localctx).codomain = match(ID);
			setState(559);
			((OplistContext)_localctx).opname = match(ID);
			setState(560);
			match(LPAREN);
			setState(561);
			((OplistContext)_localctx).domain = match(ID);
			setState(562);
			match(STAR);
			setState(563);
			match(RPAREN);
			setState(564);
			match(LBRACE);
			setState(573);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IS_FSYM) | (1L << MAKE_EMPTY) | (1L << MAKE_INSERT) | (1L << GET_HEAD) | (1L << GET_TAIL) | (1L << IS_EMPTY))) != 0)) {
				{
				setState(571);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(565);
					isFsym();
					}
					break;
				case MAKE_EMPTY:
					{
					setState(566);
					makeEmptyList();
					}
					break;
				case MAKE_INSERT:
					{
					setState(567);
					makeInsertList();
					}
					break;
				case GET_HEAD:
					{
					setState(568);
					getHead();
					}
					break;
				case GET_TAIL:
					{
					setState(569);
					getTail();
					}
					break;
				case IS_EMPTY:
					{
					setState(570);
					isEmptyList();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(575);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(576);
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
			setState(578);
			match(OPARRAY);
			setState(579);
			((OparrayContext)_localctx).codomain = match(ID);
			setState(580);
			((OparrayContext)_localctx).opname = match(ID);
			setState(581);
			match(LPAREN);
			setState(582);
			((OparrayContext)_localctx).domain = match(ID);
			setState(583);
			match(STAR);
			setState(584);
			match(RPAREN);
			setState(585);
			match(LBRACE);
			setState(593);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IS_FSYM) | (1L << MAKE_EMPTY) | (1L << MAKE_APPEND) | (1L << GET_ELEMENT) | (1L << GET_SIZE))) != 0)) {
				{
				setState(591);
				switch (_input.LA(1)) {
				case IS_FSYM:
					{
					setState(586);
					isFsym();
					}
					break;
				case MAKE_EMPTY:
					{
					setState(587);
					makeEmptyArray();
					}
					break;
				case MAKE_APPEND:
					{
					setState(588);
					makeAppendArray();
					}
					break;
				case GET_ELEMENT:
					{
					setState(589);
					getElement();
					}
					break;
				case GET_SIZE:
					{
					setState(590);
					getSize();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(595);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(596);
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
			setState(598);
			match(IMPLEMENT);
			setState(599);
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
			setState(601);
			match(EQUALS);
			setState(602);
			match(LPAREN);
			setState(603);
			((EqualsTermContext)_localctx).id1 = match(ID);
			setState(604);
			match(COMMA);
			setState(605);
			((EqualsTermContext)_localctx).id2 = match(ID);
			setState(606);
			match(RPAREN);
			setState(607);
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
			setState(609);
			match(IS_SORT);
			setState(610);
			match(LPAREN);
			setState(611);
			match(ID);
			setState(612);
			match(RPAREN);
			setState(613);
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
			setState(615);
			match(IS_FSYM);
			setState(616);
			match(LPAREN);
			setState(617);
			match(ID);
			setState(618);
			match(RPAREN);
			setState(619);
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
			setState(621);
			match(MAKE);
			setState(622);
			match(LPAREN);
			setState(631);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(623);
				match(ID);
				setState(628);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(624);
					match(COMMA);
					setState(625);
					match(ID);
					}
					}
					setState(630);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

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
			setState(636);
			match(MAKE_EMPTY);
			setState(637);
			match(LPAREN);
			setState(638);
			match(RPAREN);
			setState(639);
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
			setState(641);
			match(MAKE_EMPTY);
			setState(642);
			match(LPAREN);
			setState(643);
			match(ID);
			setState(644);
			match(RPAREN);
			setState(645);
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
			setState(647);
			match(MAKE_APPEND);
			setState(648);
			match(LPAREN);
			setState(649);
			((MakeAppendArrayContext)_localctx).id1 = match(ID);
			setState(650);
			match(COMMA);
			setState(651);
			((MakeAppendArrayContext)_localctx).id2 = match(ID);
			setState(652);
			match(RPAREN);
			setState(653);
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
			setState(655);
			match(MAKE_INSERT);
			setState(656);
			match(LPAREN);
			setState(657);
			((MakeInsertListContext)_localctx).id1 = match(ID);
			setState(658);
			match(COMMA);
			setState(659);
			((MakeInsertListContext)_localctx).id2 = match(ID);
			setState(660);
			match(RPAREN);
			setState(661);
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
			setState(663);
			match(GET_SLOT);
			setState(664);
			match(LPAREN);
			setState(665);
			((GetSlotContext)_localctx).id1 = match(ID);
			setState(666);
			match(COMMA);
			setState(667);
			((GetSlotContext)_localctx).id2 = match(ID);
			setState(668);
			match(RPAREN);
			setState(669);
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
			setState(671);
			match(GET_HEAD);
			setState(672);
			match(LPAREN);
			setState(673);
			match(ID);
			setState(674);
			match(RPAREN);
			setState(675);
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
			setState(677);
			match(GET_TAIL);
			setState(678);
			match(LPAREN);
			setState(679);
			match(ID);
			setState(680);
			match(RPAREN);
			setState(681);
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
			setState(683);
			match(GET_ELEMENT);
			setState(684);
			match(LPAREN);
			setState(685);
			((GetElementContext)_localctx).id1 = match(ID);
			setState(686);
			match(COMMA);
			setState(687);
			((GetElementContext)_localctx).id2 = match(ID);
			setState(688);
			match(RPAREN);
			setState(689);
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
			setState(691);
			match(IS_EMPTY);
			setState(692);
			match(LPAREN);
			setState(693);
			match(ID);
			setState(694);
			match(RPAREN);
			setState(695);
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
			setState(697);
			match(GET_SIZE);
			setState(698);
			match(LPAREN);
			setState(699);
			match(ID);
			setState(700);
			match(RPAREN);
			setState(701);
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
			setState(703);
			match(GET_DEFAULT);
			setState(704);
			match(LPAREN);
			setState(705);
			match(ID);
			setState(706);
			match(RPAREN);
			setState(707);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3J\u02c8\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\3\2\3\2\7\2c\n\2\f\2\16\2f\13\2\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3r\n\3\3\4\3\4\3\4\3\4\5\4x\n\4\3"+
		"\4\3\4\3\4\7\4}\n\4\f\4\16\4\u0080\13\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\7"+
		"\5\u0089\n\5\f\5\16\5\u008c\13\5\5\5\u008e\n\5\3\5\5\5\u0091\n\5\3\5\3"+
		"\5\7\5\u0095\n\5\f\5\16\5\u0098\13\5\3\5\3\5\3\6\3\6\3\6\3\6\5\6\u00a0"+
		"\n\6\3\6\3\6\3\6\3\6\3\6\7\6\u00a7\n\6\f\6\16\6\u00aa\13\6\3\6\3\6\3\7"+
		"\3\7\3\7\7\7\u00b1\n\7\f\7\16\7\u00b4\13\7\3\7\3\7\3\b\3\b\5\b\u00ba\n"+
		"\b\3\b\3\b\3\t\3\t\3\t\3\t\7\t\u00c2\n\t\f\t\16\t\u00c5\13\t\3\t\3\t\3"+
		"\n\3\n\3\n\3\n\7\n\u00cd\n\n\f\n\16\n\u00d0\13\n\3\n\3\n\3\13\3\13\3\13"+
		"\5\13\u00d7\n\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00df\n\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00ec\n\13\3\f\3"+
		"\f\3\f\3\f\7\f\u00f2\n\f\f\f\16\f\u00f5\13\f\3\f\3\f\3\r\3\r\3\16\3\16"+
		"\3\16\7\16\u00fe\n\16\f\16\16\16\u0101\13\16\3\17\3\17\5\17\u0105\n\17"+
		"\3\17\3\17\3\20\3\20\3\20\7\20\u010c\n\20\f\20\16\20\u010f\13\20\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\5\21\u0132\n\21\3\21\3\21\3\21\3\21\3\21\3\21\7\21"+
		"\u013a\n\21\f\21\16\21\u013d\13\21\3\22\3\22\5\22\u0141\n\22\3\22\3\22"+
		"\3\22\3\22\3\22\7\22\u0148\n\22\f\22\16\22\u014b\13\22\5\22\u014d\n\22"+
		"\3\22\3\22\5\22\u0151\n\22\3\23\5\23\u0154\n\23\3\23\5\23\u0157\n\23\3"+
		"\23\3\23\3\23\3\23\3\23\7\23\u015e\n\23\f\23\16\23\u0161\13\23\5\23\u0163"+
		"\n\23\3\23\3\23\5\23\u0167\n\23\3\23\5\23\u016a\n\23\3\23\3\23\3\23\3"+
		"\23\3\23\7\23\u0171\n\23\f\23\16\23\u0174\13\23\5\23\u0176\n\23\3\23\3"+
		"\23\5\23\u017a\n\23\3\23\5\23\u017d\n\23\3\23\3\23\5\23\u0181\n\23\3\23"+
		"\5\23\u0184\n\23\3\23\3\23\5\23\u0188\n\23\3\24\3\24\3\24\3\24\3\25\3"+
		"\25\3\25\3\25\3\25\3\25\7\25\u0194\n\25\f\25\16\25\u0197\13\25\5\25\u0199"+
		"\n\25\3\25\3\25\3\25\5\25\u019e\n\25\3\26\3\26\3\26\7\26\u01a3\n\26\f"+
		"\26\16\26\u01a6\13\26\3\26\3\26\3\26\7\26\u01ab\n\26\f\26\16\26\u01ae"+
		"\13\26\3\26\3\26\3\26\5\26\u01b3\n\26\3\26\3\26\3\26\5\26\u01b8\n\26\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u01c7"+
		"\n\27\3\27\3\27\5\27\u01cb\n\27\3\27\3\27\5\27\u01cf\n\27\5\27\u01d1\n"+
		"\27\3\30\3\30\3\30\3\30\3\30\7\30\u01d8\n\30\f\30\16\30\u01db\13\30\3"+
		"\30\3\30\5\30\u01df\n\30\3\31\3\31\5\31\u01e3\n\31\3\31\3\31\5\31\u01e7"+
		"\n\31\3\31\5\31\u01ea\n\31\3\32\3\32\3\33\3\33\3\33\3\33\7\33\u01f2\n"+
		"\33\f\33\16\33\u01f5\13\33\5\33\u01f7\n\33\3\33\3\33\3\34\3\34\3\34\3"+
		"\34\3\34\3\34\3\34\3\34\7\34\u0203\n\34\f\34\16\34\u0206\13\34\5\34\u0208"+
		"\n\34\3\34\3\34\3\35\3\35\3\35\3\35\5\35\u0210\n\35\3\35\3\35\3\35\5\35"+
		"\u0215\n\35\3\35\5\35\u0218\n\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\5"+
		"\36\u0221\n\36\3\36\3\36\3\36\3\36\3\36\3\36\7\36\u0229\n\36\f\36\16\36"+
		"\u022c\13\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3"+
		"\37\3\37\3\37\3\37\3\37\7\37\u023e\n\37\f\37\16\37\u0241\13\37\3\37\3"+
		"\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \7 \u0252\n \f \16 \u0255\13"+
		" \3 \3 \3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3$"+
		"\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\7%\u0275\n%\f%\16%\u0278\13%\5%\u027a\n"+
		"%\3%\3%\3%\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3"+
		"(\3(\3)\3)\3)\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3"+
		"+\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3/\3/\3"+
		"/\3/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60\3\60\7d~\u00f3\u01a4\u01ac\3"+
		" \61\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@"+
		"BDFHJLNPRTVXZ\\^\2\5\4\29;==\3\2\64\65\3\2?C\u030c\2d\3\2\2\2\4q\3\2\2"+
		"\2\6s\3\2\2\2\b\u0083\3\2\2\2\n\u009b\3\2\2\2\f\u00ad\3\2\2\2\16\u00b7"+
		"\3\2\2\2\20\u00bd\3\2\2\2\22\u00c8\3\2\2\2\24\u00eb\3\2\2\2\26\u00ed\3"+
		"\2\2\2\30\u00f8\3\2\2\2\32\u00fa\3\2\2\2\34\u0102\3\2\2\2\36\u0108\3\2"+
		"\2\2 \u0131\3\2\2\2\"\u0150\3\2\2\2$\u0187\3\2\2\2&\u0189\3\2\2\2(\u019d"+
		"\3\2\2\2*\u01b7\3\2\2\2,\u01d0\3\2\2\2.\u01de\3\2\2\2\60\u01e9\3\2\2\2"+
		"\62\u01eb\3\2\2\2\64\u01ed\3\2\2\2\66\u01fa\3\2\2\28\u020b\3\2\2\2:\u021b"+
		"\3\2\2\2<\u022f\3\2\2\2>\u0244\3\2\2\2@\u0258\3\2\2\2B\u025b\3\2\2\2D"+
		"\u0263\3\2\2\2F\u0269\3\2\2\2H\u026f\3\2\2\2J\u027e\3\2\2\2L\u0283\3\2"+
		"\2\2N\u0289\3\2\2\2P\u0291\3\2\2\2R\u0299\3\2\2\2T\u02a1\3\2\2\2V\u02a7"+
		"\3\2\2\2X\u02ad\3\2\2\2Z\u02b5\3\2\2\2\\\u02bb\3\2\2\2^\u02c1\3\2\2\2"+
		"`c\5\4\3\2ac\5\30\r\2b`\3\2\2\2ba\3\2\2\2cf\3\2\2\2de\3\2\2\2db\3\2\2"+
		"\2e\3\3\2\2\2fd\3\2\2\2gr\5\b\5\2hr\5\n\6\2ir\5\f\7\2jr\5\16\b\2kr\58"+
		"\35\2lr\5:\36\2mr\5<\37\2nr\5> \2or\5(\25\2pr\5\6\4\2qg\3\2\2\2qh\3\2"+
		"\2\2qi\3\2\2\2qj\3\2\2\2qk\3\2\2\2ql\3\2\2\2qm\3\2\2\2qn\3\2\2\2qo\3\2"+
		"\2\2qp\3\2\2\2r\5\3\2\2\2s~\7+\2\2tw\7*\2\2ux\5(\25\2vx\5*\26\2wu\3\2"+
		"\2\2wv\3\2\2\2xy\3\2\2\2yz\7*\2\2z}\3\2\2\2{}\5\30\r\2|t\3\2\2\2|{\3\2"+
		"\2\2}\u0080\3\2\2\2~\177\3\2\2\2~|\3\2\2\2\177\u0081\3\2\2\2\u0080~\3"+
		"\2\2\2\u0081\u0082\7,\2\2\u0082\7\3\2\2\2\u0083\u0090\7\3\2\2\u0084\u008d"+
		"\7 \2\2\u0085\u008a\5$\23\2\u0086\u0087\7%\2\2\u0087\u0089\5$\23\2\u0088"+
		"\u0086\3\2\2\2\u0089\u008c\3\2\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2"+
		"\2\2\u008b\u008e\3\2\2\2\u008c\u008a\3\2\2\2\u008d\u0085\3\2\2\2\u008d"+
		"\u008e\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u0091\7!\2\2\u0090\u0084\3\2"+
		"\2\2\u0090\u0091\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0096\7\36\2\2\u0093"+
		"\u0095\5\24\13\2\u0094\u0093\3\2\2\2\u0095\u0098\3\2\2\2\u0096\u0094\3"+
		"\2\2\2\u0096\u0097\3\2\2\2\u0097\u0099\3\2\2\2\u0098\u0096\3\2\2\2\u0099"+
		"\u009a\7\37\2\2\u009a\t\3\2\2\2\u009b\u009c\7\4\2\2\u009c\u009d\7=\2\2"+
		"\u009d\u009f\7 \2\2\u009e\u00a0\5\32\16\2\u009f\u009e\3\2\2\2\u009f\u00a0"+
		"\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a2\7!\2\2\u00a2\u00a3\7\5\2\2\u00a3"+
		"\u00a4\5$\23\2\u00a4\u00a8\7\36\2\2\u00a5\u00a7\5\22\n\2\u00a6\u00a5\3"+
		"\2\2\2\u00a7\u00aa\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9"+
		"\u00ab\3\2\2\2\u00aa\u00a8\3\2\2\2\u00ab\u00ac\7\37\2\2\u00ac\13\3\2\2"+
		"\2\u00ad\u00ae\7\7\2\2\u00ae\u00b2\7\36\2\2\u00af\u00b1\t\2\2\2\u00b0"+
		"\u00af\3\2\2\2\u00b1\u00b4\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b2\u00b3\3\2"+
		"\2\2\u00b3\u00b5\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b5\u00b6\7\37\2\2\u00b6"+
		"\r\3\2\2\2\u00b7\u00b9\7\b\2\2\u00b8\u00ba\5\20\t\2\u00b9\u00b8\3\2\2"+
		"\2\u00b9\u00ba\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc\5\26\f\2\u00bc"+
		"\17\3\2\2\2\u00bd\u00be\7 \2\2\u00be\u00c3\7>\2\2\u00bf\u00c0\7%\2\2\u00c0"+
		"\u00c2\7>\2\2\u00c1\u00bf\3\2\2\2\u00c2\u00c5\3\2\2\2\u00c3\u00c1\3\2"+
		"\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c6\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c6"+
		"\u00c7\7!\2\2\u00c7\21\3\2\2\2\u00c8\u00c9\7\6\2\2\u00c9\u00ca\7=\2\2"+
		"\u00ca\u00ce\7\36\2\2\u00cb\u00cd\5\24\13\2\u00cc\u00cb\3\2\2\2\u00cd"+
		"\u00d0\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d1\3\2"+
		"\2\2\u00d0\u00ce\3\2\2\2\u00d1\u00d2\7\37\2\2\u00d2\23\3\2\2\2\u00d3\u00d6"+
		"\5\36\20\2\u00d4\u00d5\t\3\2\2\u00d5\u00d7\5 \21\2\u00d6\u00d4\3\2\2\2"+
		"\u00d6\u00d7\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\u00d9\7$\2\2\u00d9\u00da"+
		"\5\26\f\2\u00da\u00ec\3\2\2\2\u00db\u00de\5\36\20\2\u00dc\u00dd\t\3\2"+
		"\2\u00dd\u00df\5 \21\2\u00de\u00dc\3\2\2\2\u00de\u00df\3\2\2\2\u00df\u00e0"+
		"\3\2\2\2\u00e0\u00e1\7$\2\2\u00e1\u00e2\5$\23\2\u00e2\u00ec\3\2\2\2\u00e3"+
		"\u00e4\5 \21\2\u00e4\u00e5\7$\2\2\u00e5\u00e6\5\26\f\2\u00e6\u00ec\3\2"+
		"\2\2\u00e7\u00e8\5 \21\2\u00e8\u00e9\7$\2\2\u00e9\u00ea\5$\23\2\u00ea"+
		"\u00ec\3\2\2\2\u00eb\u00d3\3\2\2\2\u00eb\u00db\3\2\2\2\u00eb\u00e3\3\2"+
		"\2\2\u00eb\u00e7\3\2\2\2\u00ec\25\3\2\2\2\u00ed\u00f3\7\36\2\2\u00ee\u00f2"+
		"\5\4\3\2\u00ef\u00f2\5\26\f\2\u00f0\u00f2\5\30\r\2\u00f1\u00ee\3\2\2\2"+
		"\u00f1\u00ef\3\2\2\2\u00f1\u00f0\3\2\2\2\u00f2\u00f5\3\2\2\2\u00f3\u00f4"+
		"\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f4\u00f6\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f6"+
		"\u00f7\7\37\2\2\u00f7\27\3\2\2\2\u00f8\u00f9\13\2\2\2\u00f9\31\3\2\2\2"+
		"\u00fa\u00ff\5\34\17\2\u00fb\u00fc\7%\2\2\u00fc\u00fe\5\34\17\2\u00fd"+
		"\u00fb\3\2\2\2\u00fe\u0101\3\2\2\2\u00ff\u00fd\3\2\2\2\u00ff\u0100\3\2"+
		"\2\2\u0100\33\3\2\2\2\u0101\u00ff\3\2\2\2\u0102\u0104\7=\2\2\u0103\u0105"+
		"\7&\2\2\u0104\u0103\3\2\2\2\u0104\u0105\3\2\2\2\u0105\u0106\3\2\2\2\u0106"+
		"\u0107\7=\2\2\u0107\35\3\2\2\2\u0108\u010d\5,\27\2\u0109\u010a\7%\2\2"+
		"\u010a\u010c\5,\27\2\u010b\u0109\3\2\2\2\u010c\u010f\3\2\2\2\u010d\u010b"+
		"\3\2\2\2\u010d\u010e\3\2\2\2\u010e\37\3\2\2\2\u010f\u010d\3\2\2\2\u0110"+
		"\u0111\b\21\1\2\u0111\u0112\5,\27\2\u0112\u0113\7\34\2\2\u0113\u0114\5"+
		"$\23\2\u0114\u0132\3\2\2\2\u0115\u0116\5\"\22\2\u0116\u0117\7\60\2\2\u0117"+
		"\u0118\5\"\22\2\u0118\u0132\3\2\2\2\u0119\u011a\5\"\22\2\u011a\u011b\7"+
		".\2\2\u011b\u011c\5\"\22\2\u011c\u0132\3\2\2\2\u011d\u011e\5\"\22\2\u011e"+
		"\u011f\7\61\2\2\u011f\u0120\5\"\22\2\u0120\u0132\3\2\2\2\u0121\u0122\5"+
		"\"\22\2\u0122\u0123\7/\2\2\u0123\u0124\5\"\22\2\u0124\u0132\3\2\2\2\u0125"+
		"\u0126\5\"\22\2\u0126\u0127\7\62\2\2\u0127\u0128\5\"\22\2\u0128\u0132"+
		"\3\2\2\2\u0129\u012a\5\"\22\2\u012a\u012b\7\63\2\2\u012b\u012c\5\"\22"+
		"\2\u012c\u0132\3\2\2\2\u012d\u012e\7 \2\2\u012e\u012f\5 \21\2\u012f\u0130"+
		"\7!\2\2\u0130\u0132\3\2\2\2\u0131\u0110\3\2\2\2\u0131\u0115\3\2\2\2\u0131"+
		"\u0119\3\2\2\2\u0131\u011d\3\2\2\2\u0131\u0121\3\2\2\2\u0131\u0125\3\2"+
		"\2\2\u0131\u0129\3\2\2\2\u0131\u012d\3\2\2\2\u0132\u013b\3\2\2\2\u0133"+
		"\u0134\f\f\2\2\u0134\u0135\7\64\2\2\u0135\u013a\5 \21\r\u0136\u0137\f"+
		"\13\2\2\u0137\u0138\7\65\2\2\u0138\u013a\5 \21\f\u0139\u0133\3\2\2\2\u0139"+
		"\u0136\3\2\2\2\u013a\u013d\3\2\2\2\u013b\u0139\3\2\2\2\u013b\u013c\3\2"+
		"\2\2\u013c!\3\2\2\2\u013d\u013b\3\2\2\2\u013e\u0140\7=\2\2\u013f\u0141"+
		"\7\'\2\2\u0140\u013f\3\2\2\2\u0140\u0141\3\2\2\2\u0141\u0151\3\2\2\2\u0142"+
		"\u0143\7=\2\2\u0143\u014c\7 \2\2\u0144\u0149\5\"\22\2\u0145\u0146\7%\2"+
		"\2\u0146\u0148\5\"\22\2\u0147\u0145\3\2\2\2\u0148\u014b\3\2\2\2\u0149"+
		"\u0147\3\2\2\2\u0149\u014a\3\2\2\2\u014a\u014d\3\2\2\2\u014b\u0149\3\2"+
		"\2\2\u014c\u0144\3\2\2\2\u014c\u014d\3\2\2\2\u014d\u014e\3\2\2\2\u014e"+
		"\u0151\7!\2\2\u014f\u0151\5\62\32\2\u0150\u013e\3\2\2\2\u0150\u0142\3"+
		"\2\2\2\u0150\u014f\3\2\2\2\u0151#\3\2\2\2\u0152\u0154\7=\2\2\u0153\u0152"+
		"\3\2\2\2\u0153\u0154\3\2\2\2\u0154\u0156\3\2\2\2\u0155\u0157\7<\2\2\u0156"+
		"\u0155\3\2\2\2\u0156\u0157\3\2\2\2\u0157\u0158\3\2\2\2\u0158\u0159\7="+
		"\2\2\u0159\u0162\7 \2\2\u015a\u015f\5$\23\2\u015b\u015c\7%\2\2\u015c\u015e"+
		"\5$\23\2\u015d\u015b\3\2\2\2\u015e\u0161\3\2\2\2\u015f\u015d\3\2\2\2\u015f"+
		"\u0160\3\2\2\2\u0160\u0163\3\2\2\2\u0161\u015f\3\2\2\2\u0162\u015a\3\2"+
		"\2\2\u0162\u0163\3\2\2\2\u0163\u0164\3\2\2\2\u0164\u0188\7!\2\2\u0165"+
		"\u0167\7=\2\2\u0166\u0165\3\2\2\2\u0166\u0167\3\2\2\2\u0167\u0169\3\2"+
		"\2\2\u0168\u016a\7<\2\2\u0169\u0168\3\2\2\2\u0169\u016a\3\2\2\2\u016a"+
		"\u016b\3\2\2\2\u016b\u016c\7=\2\2\u016c\u0175\7\"\2\2\u016d\u0172\5&\24"+
		"\2\u016e\u016f\7%\2\2\u016f\u0171\5&\24\2\u0170\u016e\3\2\2\2\u0171\u0174"+
		"\3\2\2\2\u0172\u0170\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0176\3\2\2\2\u0174"+
		"\u0172\3\2\2\2\u0175\u016d\3\2\2\2\u0175\u0176\3\2\2\2\u0176\u0177\3\2"+
		"\2\2\u0177\u0188\7#\2\2\u0178\u017a\7=\2\2\u0179\u0178\3\2\2\2\u0179\u017a"+
		"\3\2\2\2\u017a\u017c\3\2\2\2\u017b\u017d\7<\2\2\u017c\u017b\3\2\2\2\u017c"+
		"\u017d\3\2\2\2\u017d\u017e\3\2\2\2\u017e\u0180\7=\2\2\u017f\u0181\7\'"+
		"\2\2\u0180\u017f\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u0188\3\2\2\2\u0182"+
		"\u0184\7=\2\2\u0183\u0182\3\2\2\2\u0183\u0184\3\2\2\2\u0184\u0185\3\2"+
		"\2\2\u0185\u0188\5\62\32\2\u0186\u0188\7(\2\2\u0187\u0153\3\2\2\2\u0187"+
		"\u0166\3\2\2\2\u0187\u0179\3\2\2\2\u0187\u0183\3\2\2\2\u0187\u0186\3\2"+
		"\2\2\u0188%\3\2\2\2\u0189\u018a\7=\2\2\u018a\u018b\7\35\2\2\u018b\u018c"+
		"\5$\23\2\u018c\'\3\2\2\2\u018d\u018e\7<\2\2\u018e\u018f\7=\2\2\u018f\u0198"+
		"\7\"\2\2\u0190\u0195\5&\24\2\u0191\u0192\7%\2\2\u0192\u0194\5&\24\2\u0193"+
		"\u0191\3\2\2\2\u0194\u0197\3\2\2\2\u0195\u0193\3\2\2\2\u0195\u0196\3\2"+
		"\2\2\u0196\u0199\3\2\2\2\u0197\u0195\3\2\2\2\u0198\u0190\3\2\2\2\u0198"+
		"\u0199\3\2\2\2\u0199\u019a\3\2\2\2\u019a\u019e\7#\2\2\u019b\u019c\7<\2"+
		"\2\u019c\u019e\5*\26\2\u019d\u018d\3\2\2\2\u019d\u019b\3\2\2\2\u019e)"+
		"\3\2\2\2\u019f\u01a0\7=\2\2\u01a0\u01a4\7 \2\2\u01a1\u01a3\5*\26\2\u01a2"+
		"\u01a1\3\2\2\2\u01a3\u01a6\3\2\2\2\u01a4\u01a5\3\2\2\2\u01a4\u01a2\3\2"+
		"\2\2\u01a5\u01a7\3\2\2\2\u01a6\u01a4\3\2\2\2\u01a7\u01b8\7!\2\2\u01a8"+
		"\u01ac\7 \2\2\u01a9\u01ab\5*\26\2\u01aa\u01a9\3\2\2\2\u01ab\u01ae\3\2"+
		"\2\2\u01ac\u01ad\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ad\u01af\3\2\2\2\u01ae"+
		"\u01ac\3\2\2\2\u01af\u01b8\7!\2\2\u01b0\u01b2\7=\2\2\u01b1\u01b3\7\'\2"+
		"\2\u01b2\u01b1\3\2\2\2\u01b2\u01b3\3\2\2\2\u01b3\u01b8\3\2\2\2\u01b4\u01b8"+
		"\5\62\32\2\u01b5\u01b8\7(\2\2\u01b6\u01b8\5\30\r\2\u01b7\u019f\3\2\2\2"+
		"\u01b7\u01a8\3\2\2\2\u01b7\u01b0\3\2\2\2\u01b7\u01b4\3\2\2\2\u01b7\u01b5"+
		"\3\2\2\2\u01b7\u01b6\3\2\2\2\u01b8+\3\2\2\2\u01b9\u01ba\7=\2\2\u01ba\u01bb"+
		"\7*\2\2\u01bb\u01d1\5,\27\2\u01bc\u01bd\7)\2\2\u01bd\u01d1\5,\27\2\u01be"+
		"\u01bf\5.\30\2\u01bf\u01c0\5\64\33\2\u01c0\u01d1\3\2\2\2\u01c1\u01c2\5"+
		".\30\2\u01c2\u01c3\5\66\34\2\u01c3\u01d1\3\2\2\2\u01c4\u01c6\7=\2\2\u01c5"+
		"\u01c7\7\'\2\2\u01c6\u01c5\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7\u01d1\3\2"+
		"\2\2\u01c8\u01ca\7(\2\2\u01c9\u01cb\7\'\2\2\u01ca\u01c9\3\2\2\2\u01ca"+
		"\u01cb\3\2\2\2\u01cb\u01d1\3\2\2\2\u01cc\u01ce\5\62\32\2\u01cd\u01cf\7"+
		"\'\2\2\u01ce\u01cd\3\2\2\2\u01ce\u01cf\3\2\2\2\u01cf\u01d1\3\2\2\2\u01d0"+
		"\u01b9\3\2\2\2\u01d0\u01bc\3\2\2\2\u01d0\u01be\3\2\2\2\u01d0\u01c1\3\2"+
		"\2\2\u01d0\u01c4\3\2\2\2\u01d0\u01c8\3\2\2\2\u01d0\u01cc\3\2\2\2\u01d1"+
		"-\3\2\2\2\u01d2\u01df\5\60\31\2\u01d3\u01d4\7 \2\2\u01d4\u01d9\5\60\31"+
		"\2\u01d5\u01d6\7\66\2\2\u01d6\u01d8\5\60\31\2\u01d7\u01d5\3\2\2\2\u01d8"+
		"\u01db\3\2\2\2\u01d9\u01d7\3\2\2\2\u01d9\u01da\3\2\2\2\u01da\u01dc\3\2"+
		"\2\2\u01db\u01d9\3\2\2\2\u01dc\u01dd\7!\2\2\u01dd\u01df\3\2\2\2\u01de"+
		"\u01d2\3\2\2\2\u01de\u01d3\3\2\2\2\u01df/\3\2\2\2\u01e0\u01e2\7=\2\2\u01e1"+
		"\u01e3\7\67\2\2\u01e2\u01e1\3\2\2\2\u01e2\u01e3\3\2\2\2\u01e3\u01ea\3"+
		"\2\2\2\u01e4\u01e6\7=\2\2\u01e5\u01e7\78\2\2\u01e6\u01e5\3\2\2\2\u01e6"+
		"\u01e7\3\2\2\2\u01e7\u01ea\3\2\2\2\u01e8\u01ea\5\62\32\2\u01e9\u01e0\3"+
		"\2\2\2\u01e9\u01e4\3\2\2\2\u01e9\u01e8\3\2\2\2\u01ea\61\3\2\2\2\u01eb"+
		"\u01ec\t\4\2\2\u01ec\63\3\2\2\2\u01ed\u01f6\7 \2\2\u01ee\u01f3\5,\27\2"+
		"\u01ef\u01f0\7%\2\2\u01f0\u01f2\5,\27\2\u01f1\u01ef\3\2\2\2\u01f2\u01f5"+
		"\3\2\2\2\u01f3\u01f1\3\2\2\2\u01f3\u01f4\3\2\2\2\u01f4\u01f7\3\2\2\2\u01f5"+
		"\u01f3\3\2\2\2\u01f6\u01ee\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7\u01f8\3\2"+
		"\2\2\u01f8\u01f9\7!\2\2\u01f9\65\3\2\2\2\u01fa\u0207\7\"\2\2\u01fb\u01fc"+
		"\7=\2\2\u01fc\u01fd\7\35\2\2\u01fd\u0204\5,\27\2\u01fe\u01ff\7%\2\2\u01ff"+
		"\u0200\7=\2\2\u0200\u0201\7\35\2\2\u0201\u0203\5,\27\2\u0202\u01fe\3\2"+
		"\2\2\u0203\u0206\3\2\2\2\u0204\u0202\3\2\2\2\u0204\u0205\3\2\2\2\u0205"+
		"\u0208\3\2\2\2\u0206\u0204\3\2\2\2\u0207\u01fb\3\2\2\2\u0207\u0208\3\2"+
		"\2\2\u0208\u0209\3\2\2\2\u0209\u020a\7#\2\2\u020a\67\3\2\2\2\u020b\u020c"+
		"\7\f\2\2\u020c\u020f\7=\2\2\u020d\u020e\7\5\2\2\u020e\u0210\7=\2\2\u020f"+
		"\u020d\3\2\2\2\u020f\u0210\3\2\2\2\u0210\u0211\3\2\2\2\u0211\u0212\7\36"+
		"\2\2\u0212\u0214\5@!\2\u0213\u0215\5D#\2\u0214\u0213\3\2\2\2\u0214\u0215"+
		"\3\2\2\2\u0215\u0217\3\2\2\2\u0216\u0218\5B\"\2\u0217\u0216\3\2\2\2\u0217"+
		"\u0218\3\2\2\2\u0218\u0219\3\2\2\2\u0219\u021a\7\37\2\2\u021a9\3\2\2\2"+
		"\u021b\u021c\7\t\2\2\u021c\u021d\7=\2\2\u021d\u021e\7=\2\2\u021e\u0220"+
		"\7 \2\2\u021f\u0221\5\32\16\2\u0220\u021f\3\2\2\2\u0220\u0221\3\2\2\2"+
		"\u0221\u0222\3\2\2\2\u0222\u0223\7!\2\2\u0223\u022a\7\36\2\2\u0224\u0229"+
		"\5F$\2\u0225\u0229\5H%\2\u0226\u0229\5R*\2\u0227\u0229\5^\60\2\u0228\u0224"+
		"\3\2\2\2\u0228\u0225\3\2\2\2\u0228\u0226\3\2\2\2\u0228\u0227\3\2\2\2\u0229"+
		"\u022c\3\2\2\2\u022a\u0228\3\2\2\2\u022a\u022b\3\2\2\2\u022b\u022d\3\2"+
		"\2\2\u022c\u022a\3\2\2\2\u022d\u022e\7\37\2\2\u022e;\3\2\2\2\u022f\u0230"+
		"\7\13\2\2\u0230\u0231\7=\2\2\u0231\u0232\7=\2\2\u0232\u0233\7 \2\2\u0233"+
		"\u0234\7=\2\2\u0234\u0235\7\'\2\2\u0235\u0236\7!\2\2\u0236\u023f\7\36"+
		"\2\2\u0237\u023e\5F$\2\u0238\u023e\5J&\2\u0239\u023e\5P)\2\u023a\u023e"+
		"\5T+\2\u023b\u023e\5V,\2\u023c\u023e\5Z.\2\u023d\u0237\3\2\2\2\u023d\u0238"+
		"\3\2\2\2\u023d\u0239\3\2\2\2\u023d\u023a\3\2\2\2\u023d\u023b\3\2\2\2\u023d"+
		"\u023c\3\2\2\2\u023e\u0241\3\2\2\2\u023f\u023d\3\2\2\2\u023f\u0240\3\2"+
		"\2\2\u0240\u0242\3\2\2\2\u0241\u023f\3\2\2\2\u0242\u0243\7\37\2\2\u0243"+
		"=\3\2\2\2\u0244\u0245\7\n\2\2\u0245\u0246\7=\2\2\u0246\u0247\7=\2\2\u0247"+
		"\u0248\7 \2\2\u0248\u0249\7=\2\2\u0249\u024a\7\'\2\2\u024a\u024b\7!\2"+
		"\2\u024b\u0253\7\36\2\2\u024c\u0252\5F$\2\u024d\u0252\5L\'\2\u024e\u0252"+
		"\5N(\2\u024f\u0252\5X-\2\u0250\u0252\5\\/\2\u0251\u024c\3\2\2\2\u0251"+
		"\u024d\3\2\2\2\u0251\u024e\3\2\2\2\u0251\u024f\3\2\2\2\u0251\u0250\3\2"+
		"\2\2\u0252\u0255\3\2\2\2\u0253\u0251\3\2\2\2\u0253\u0254\3\2\2\2\u0254"+
		"\u0256\3\2\2\2\u0255\u0253\3\2\2\2\u0256\u0257\7\37\2\2\u0257?\3\2\2\2"+
		"\u0258\u0259\7\32\2\2\u0259\u025a\5\26\f\2\u025aA\3\2\2\2\u025b\u025c"+
		"\7\33\2\2\u025c\u025d\7 \2\2\u025d\u025e\7=\2\2\u025e\u025f\7%\2\2\u025f"+
		"\u0260\7=\2\2\u0260\u0261\7!\2\2\u0261\u0262\5\26\f\2\u0262C\3\2\2\2\u0263"+
		"\u0264\7\16\2\2\u0264\u0265\7 \2\2\u0265\u0266\7=\2\2\u0266\u0267\7!\2"+
		"\2\u0267\u0268\5\26\f\2\u0268E\3\2\2\2\u0269\u026a\7\r\2\2\u026a\u026b"+
		"\7 \2\2\u026b\u026c\7=\2\2\u026c\u026d\7!\2\2\u026d\u026e\5\26\f\2\u026e"+
		"G\3\2\2\2\u026f\u0270\7\17\2\2\u0270\u0279\7 \2\2\u0271\u0276\7=\2\2\u0272"+
		"\u0273\7%\2\2\u0273\u0275\7=\2\2\u0274\u0272\3\2\2\2\u0275\u0278\3\2\2"+
		"\2\u0276\u0274\3\2\2\2\u0276\u0277\3\2\2\2\u0277\u027a\3\2\2\2\u0278\u0276"+
		"\3\2\2\2\u0279\u0271\3\2\2\2\u0279\u027a\3\2\2\2\u027a\u027b\3\2\2\2\u027b"+
		"\u027c\7!\2\2\u027c\u027d\5\26\f\2\u027dI\3\2\2\2\u027e\u027f\7\20\2\2"+
		"\u027f\u0280\7 \2\2\u0280\u0281\7!\2\2\u0281\u0282\5\26\f\2\u0282K\3\2"+
		"\2\2\u0283\u0284\7\20\2\2\u0284\u0285\7 \2\2\u0285\u0286\7=\2\2\u0286"+
		"\u0287\7!\2\2\u0287\u0288\5\26\f\2\u0288M\3\2\2\2\u0289\u028a\7\21\2\2"+
		"\u028a\u028b\7 \2\2\u028b\u028c\7=\2\2\u028c\u028d\7%\2\2\u028d\u028e"+
		"\7=\2\2\u028e\u028f\7!\2\2\u028f\u0290\5\26\f\2\u0290O\3\2\2\2\u0291\u0292"+
		"\7\22\2\2\u0292\u0293\7 \2\2\u0293\u0294\7=\2\2\u0294\u0295\7%\2\2\u0295"+
		"\u0296\7=\2\2\u0296\u0297\7!\2\2\u0297\u0298\5\26\f\2\u0298Q\3\2\2\2\u0299"+
		"\u029a\7\23\2\2\u029a\u029b\7 \2\2\u029b\u029c\7=\2\2\u029c\u029d\7%\2"+
		"\2\u029d\u029e\7=\2\2\u029e\u029f\7!\2\2\u029f\u02a0\5\26\f\2\u02a0S\3"+
		"\2\2\2\u02a1\u02a2\7\26\2\2\u02a2\u02a3\7 \2\2\u02a3\u02a4\7=\2\2\u02a4"+
		"\u02a5\7!\2\2\u02a5\u02a6\5\26\f\2\u02a6U\3\2\2\2\u02a7\u02a8\7\27\2\2"+
		"\u02a8\u02a9\7 \2\2\u02a9\u02aa\7=\2\2\u02aa\u02ab\7!\2\2\u02ab\u02ac"+
		"\5\26\f\2\u02acW\3\2\2\2\u02ad\u02ae\7\25\2\2\u02ae\u02af\7 \2\2\u02af"+
		"\u02b0\7=\2\2\u02b0\u02b1\7%\2\2\u02b1\u02b2\7=\2\2\u02b2\u02b3\7!\2\2"+
		"\u02b3\u02b4\5\26\f\2\u02b4Y\3\2\2\2\u02b5\u02b6\7\31\2\2\u02b6\u02b7"+
		"\7 \2\2\u02b7\u02b8\7=\2\2\u02b8\u02b9\7!\2\2\u02b9\u02ba\5\26\f\2\u02ba"+
		"[\3\2\2\2\u02bb\u02bc\7\30\2\2\u02bc\u02bd\7 \2\2\u02bd\u02be\7=\2\2\u02be"+
		"\u02bf\7!\2\2\u02bf\u02c0\5\26\f\2\u02c0]\3\2\2\2\u02c1\u02c2\7\24\2\2"+
		"\u02c2\u02c3\7 \2\2\u02c3\u02c4\7=\2\2\u02c4\u02c5\7!\2\2\u02c5\u02c6"+
		"\5\26\f\2\u02c6_\3\2\2\2Nbdqw|~\u008a\u008d\u0090\u0096\u009f\u00a8\u00b2"+
		"\u00b9\u00c3\u00ce\u00d6\u00de\u00eb\u00f1\u00f3\u00ff\u0104\u010d\u0131"+
		"\u0139\u013b\u0140\u0149\u014c\u0150\u0153\u0156\u015f\u0162\u0166\u0169"+
		"\u0172\u0175\u0179\u017c\u0180\u0183\u0187\u0195\u0198\u019d\u01a4\u01ac"+
		"\u01b2\u01b7\u01c6\u01ca\u01ce\u01d0\u01d9\u01de\u01e2\u01e6\u01e9\u01f3"+
		"\u01f6\u0204\u0207\u020f\u0214\u0217\u0220\u0228\u022a\u023d\u023f\u0251"+
		"\u0253\u0276\u0279";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}