// Generated from /Users/pem/github/tom/src/tom/engine/parser/antlr4/TomIslandLexer.g4 by ANTLR 4.5.3
package tom.engine.parser.antlr4;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TomIslandLexer extends Lexer {
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
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"MATCH", "STRATEGY", "EXTENDS", "VISIT", "INCLUDE", "GOM", "OP", "OPARRAY", 
		"OPLIST", "TYPETERM", "IS_FSYM", "IS_SORT", "MAKE", "MAKE_EMPTY", "MAKE_APPEND", 
		"MAKE_INSERT", "GET_SLOT", "GET_DEFAULT", "GET_ELEMENT", "GET_HEAD", "GET_TAIL", 
		"GET_SIZE", "IS_EMPTY", "IMPLEMENT", "EQUALS", "MATCH_SYMBOL", "EQUAL", 
		"LBRACE", "RBRACE", "LPAREN", "RPAREN", "LSQUAREBR", "RSQUAREBR", "ARROW", 
		"COMMA", "COLON", "STAR", "UNDERSCORE", "ANTI", "AT", "LMETAQUOTE", "RMETAQUOTE", 
		"ATAT", "GREATEROREQ", "LOWEROREQ", "GREATERTHAN", "LOWERTHAN", "DOUBLEEQ", 
		"DIFFERENT", "AND", "OR", "PIPE", "QMARK", "DQMARK", "SLASH", "BACKSLASH", 
		"DOT", "BQUOTE", "ID", "DMINUSID", "INTEGER", "DOUBLE", "LONG", "STRING", 
		"CHAR", "UNSIGNED_DOUBLE", "LONG_SUFFIX", "LETTER", "DIGIT", "HEX_DIGIT", 
		"ESC", "ACTION_ESCAPE", "ACTION_STRING_LITERAL", "MLCOMMENT", "SLCOMMENT", 
		"WS", "NL", "ANY"
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


	public static final int WHITESPACE = 1; 
	public static final int COMMENTS = 2;


	public TomIslandLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "TomIslandLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2J\u0271\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3"+
		"\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\33\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3"+
		"\"\3\"\3#\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3*\3+\3+\3"+
		"+\3,\3,\3,\3-\3-\3-\3.\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\61\3\62\3\62"+
		"\3\62\3\63\3\63\3\63\3\64\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\3\67"+
		"\38\38\39\39\3:\3:\3;\3;\3<\5<\u01c9\n<\3<\3<\3<\3<\7<\u01cf\n<\f<\16"+
		"<\u01d2\13<\3=\3=\3=\3=\7=\u01d8\n=\f=\16=\u01db\13=\3>\5>\u01de\n>\3"+
		">\6>\u01e1\n>\r>\16>\u01e2\3?\5?\u01e6\n?\3?\3?\3@\5@\u01eb\n@\3@\6@\u01ee"+
		"\n@\r@\16@\u01ef\3@\3@\3A\3A\3A\7A\u01f7\nA\fA\16A\u01fa\13A\3A\3A\3B"+
		"\3B\3B\6B\u0201\nB\rB\16B\u0202\3B\3B\3C\6C\u0208\nC\rC\16C\u0209\3C\3"+
		"C\7C\u020e\nC\fC\16C\u0211\13C\3C\3C\6C\u0215\nC\rC\16C\u0216\5C\u0219"+
		"\nC\3D\3D\3E\3E\3F\3F\3G\3G\3H\3H\3H\6H\u0226\nH\rH\16H\u0227\3H\3H\3"+
		"H\3H\3H\3H\3H\3H\5H\u0232\nH\5H\u0234\nH\3H\3H\5H\u0238\nH\5H\u023a\n"+
		"H\3I\3I\3I\3J\3J\3J\7J\u0242\nJ\fJ\16J\u0245\13J\3J\3J\3K\3K\3K\3K\7K"+
		"\u024d\nK\fK\16K\u0250\13K\3K\3K\3K\3K\3K\3L\3L\3L\3L\7L\u025b\nL\fL\16"+
		"L\u025e\13L\3L\3L\3M\6M\u0263\nM\rM\16M\u0264\3M\3M\3N\6N\u026a\nN\rN"+
		"\16N\u026b\3N\3N\3O\3O\3\u024e\2P\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31"+
		"\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60"+
		"_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083C\u0085"+
		"\2\u0087\2\u0089\2\u008b\2\u008d\2\u008f\2\u0091D\u0093E\u0095F\u0097"+
		"G\u0099H\u009bI\u009dJ\3\2\f\6\2\f\f\17\17$$^^\6\2\f\f\17\17))^^\4\2N"+
		"Nnn\4\2C\\c|\5\2\62;CHch\n\2$$))^^ddhhppttvv\4\2$$^^\4\2\f\f\17\17\5\2"+
		"\13\13\17\17\"\"\3\2\f\f\u0289\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2"+
		"+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2"+
		"\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2"+
		"C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3"+
		"\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2"+
		"\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2"+
		"i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3"+
		"\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081"+
		"\3\2\2\2\2\u0083\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2"+
		"\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\3\u009f"+
		"\3\2\2\2\5\u00a6\3\2\2\2\7\u00b0\3\2\2\2\t\u00b8\3\2\2\2\13\u00be\3\2"+
		"\2\2\r\u00c7\3\2\2\2\17\u00cc\3\2\2\2\21\u00d0\3\2\2\2\23\u00d9\3\2\2"+
		"\2\25\u00e1\3\2\2\2\27\u00eb\3\2\2\2\31\u00f3\3\2\2\2\33\u00fb\3\2\2\2"+
		"\35\u0100\3\2\2\2\37\u010b\3\2\2\2!\u0117\3\2\2\2#\u0123\3\2\2\2%\u012c"+
		"\3\2\2\2\'\u0138\3\2\2\2)\u0144\3\2\2\2+\u014d\3\2\2\2-\u0156\3\2\2\2"+
		"/\u015f\3\2\2\2\61\u0168\3\2\2\2\63\u0172\3\2\2\2\65\u0179\3\2\2\2\67"+
		"\u017c\3\2\2\29\u017e\3\2\2\2;\u0180\3\2\2\2=\u0182\3\2\2\2?\u0184\3\2"+
		"\2\2A\u0186\3\2\2\2C\u0188\3\2\2\2E\u018a\3\2\2\2G\u018d\3\2\2\2I\u018f"+
		"\3\2\2\2K\u0191\3\2\2\2M\u0193\3\2\2\2O\u0195\3\2\2\2Q\u0197\3\2\2\2S"+
		"\u0199\3\2\2\2U\u019c\3\2\2\2W\u019f\3\2\2\2Y\u01a2\3\2\2\2[\u01a5\3\2"+
		"\2\2]\u01a8\3\2\2\2_\u01aa\3\2\2\2a\u01ac\3\2\2\2c\u01af\3\2\2\2e\u01b2"+
		"\3\2\2\2g\u01b5\3\2\2\2i\u01b8\3\2\2\2k\u01ba\3\2\2\2m\u01bc\3\2\2\2o"+
		"\u01bf\3\2\2\2q\u01c1\3\2\2\2s\u01c3\3\2\2\2u\u01c5\3\2\2\2w\u01c8\3\2"+
		"\2\2y\u01d3\3\2\2\2{\u01dd\3\2\2\2}\u01e5\3\2\2\2\177\u01ea\3\2\2\2\u0081"+
		"\u01f3\3\2\2\2\u0083\u01fd\3\2\2\2\u0085\u0218\3\2\2\2\u0087\u021a\3\2"+
		"\2\2\u0089\u021c\3\2\2\2\u008b\u021e\3\2\2\2\u008d\u0220\3\2\2\2\u008f"+
		"\u0222\3\2\2\2\u0091\u023b\3\2\2\2\u0093\u023e\3\2\2\2\u0095\u0248\3\2"+
		"\2\2\u0097\u0256\3\2\2\2\u0099\u0262\3\2\2\2\u009b\u0269\3\2\2\2\u009d"+
		"\u026f\3\2\2\2\u009f\u00a0\7\'\2\2\u00a0\u00a1\7o\2\2\u00a1\u00a2\7c\2"+
		"\2\u00a2\u00a3\7v\2\2\u00a3\u00a4\7e\2\2\u00a4\u00a5\7j\2\2\u00a5\4\3"+
		"\2\2\2\u00a6\u00a7\7\'\2\2\u00a7\u00a8\7u\2\2\u00a8\u00a9\7v\2\2\u00a9"+
		"\u00aa\7t\2\2\u00aa\u00ab\7c\2\2\u00ab\u00ac\7v\2\2\u00ac\u00ad\7g\2\2"+
		"\u00ad\u00ae\7i\2\2\u00ae\u00af\7{\2\2\u00af\6\3\2\2\2\u00b0\u00b1\7g"+
		"\2\2\u00b1\u00b2\7z\2\2\u00b2\u00b3\7v\2\2\u00b3\u00b4\7g\2\2\u00b4\u00b5"+
		"\7p\2\2\u00b5\u00b6\7f\2\2\u00b6\u00b7\7u\2\2\u00b7\b\3\2\2\2\u00b8\u00b9"+
		"\7x\2\2\u00b9\u00ba\7k\2\2\u00ba\u00bb\7u\2\2\u00bb\u00bc\7k\2\2\u00bc"+
		"\u00bd\7v\2\2\u00bd\n\3\2\2\2\u00be\u00bf\7\'\2\2\u00bf\u00c0\7k\2\2\u00c0"+
		"\u00c1\7p\2\2\u00c1\u00c2\7e\2\2\u00c2\u00c3\7n\2\2\u00c3\u00c4\7w\2\2"+
		"\u00c4\u00c5\7f\2\2\u00c5\u00c6\7g\2\2\u00c6\f\3\2\2\2\u00c7\u00c8\7\'"+
		"\2\2\u00c8\u00c9\7i\2\2\u00c9\u00ca\7q\2\2\u00ca\u00cb\7o\2\2\u00cb\16"+
		"\3\2\2\2\u00cc\u00cd\7\'\2\2\u00cd\u00ce\7q\2\2\u00ce\u00cf\7r\2\2\u00cf"+
		"\20\3\2\2\2\u00d0\u00d1\7\'\2\2\u00d1\u00d2\7q\2\2\u00d2\u00d3\7r\2\2"+
		"\u00d3\u00d4\7c\2\2\u00d4\u00d5\7t\2\2\u00d5\u00d6\7t\2\2\u00d6\u00d7"+
		"\7c\2\2\u00d7\u00d8\7{\2\2\u00d8\22\3\2\2\2\u00d9\u00da\7\'\2\2\u00da"+
		"\u00db\7q\2\2\u00db\u00dc\7r\2\2\u00dc\u00dd\7n\2\2\u00dd\u00de\7k\2\2"+
		"\u00de\u00df\7u\2\2\u00df\u00e0\7v\2\2\u00e0\24\3\2\2\2\u00e1\u00e2\7"+
		"\'\2\2\u00e2\u00e3\7v\2\2\u00e3\u00e4\7{\2\2\u00e4\u00e5\7r\2\2\u00e5"+
		"\u00e6\7g\2\2\u00e6\u00e7\7v\2\2\u00e7\u00e8\7g\2\2\u00e8\u00e9\7t\2\2"+
		"\u00e9\u00ea\7o\2\2\u00ea\26\3\2\2\2\u00eb\u00ec\7k\2\2\u00ec\u00ed\7"+
		"u\2\2\u00ed\u00ee\7a\2\2\u00ee\u00ef\7h\2\2\u00ef\u00f0\7u\2\2\u00f0\u00f1"+
		"\7{\2\2\u00f1\u00f2\7o\2\2\u00f2\30\3\2\2\2\u00f3\u00f4\7k\2\2\u00f4\u00f5"+
		"\7u\2\2\u00f5\u00f6\7a\2\2\u00f6\u00f7\7u\2\2\u00f7\u00f8\7q\2\2\u00f8"+
		"\u00f9\7t\2\2\u00f9\u00fa\7v\2\2\u00fa\32\3\2\2\2\u00fb\u00fc\7o\2\2\u00fc"+
		"\u00fd\7c\2\2\u00fd\u00fe\7m\2\2\u00fe\u00ff\7g\2\2\u00ff\34\3\2\2\2\u0100"+
		"\u0101\7o\2\2\u0101\u0102\7c\2\2\u0102\u0103\7m\2\2\u0103\u0104\7g\2\2"+
		"\u0104\u0105\7a\2\2\u0105\u0106\7g\2\2\u0106\u0107\7o\2\2\u0107\u0108"+
		"\7r\2\2\u0108\u0109\7v\2\2\u0109\u010a\7{\2\2\u010a\36\3\2\2\2\u010b\u010c"+
		"\7o\2\2\u010c\u010d\7c\2\2\u010d\u010e\7m\2\2\u010e\u010f\7g\2\2\u010f"+
		"\u0110\7a\2\2\u0110\u0111\7c\2\2\u0111\u0112\7r\2\2\u0112\u0113\7r\2\2"+
		"\u0113\u0114\7g\2\2\u0114\u0115\7p\2\2\u0115\u0116\7f\2\2\u0116 \3\2\2"+
		"\2\u0117\u0118\7o\2\2\u0118\u0119\7c\2\2\u0119\u011a\7m\2\2\u011a\u011b"+
		"\7g\2\2\u011b\u011c\7a\2\2\u011c\u011d\7k\2\2\u011d\u011e\7p\2\2\u011e"+
		"\u011f\7u\2\2\u011f\u0120\7g\2\2\u0120\u0121\7t\2\2\u0121\u0122\7v\2\2"+
		"\u0122\"\3\2\2\2\u0123\u0124\7i\2\2\u0124\u0125\7g\2\2\u0125\u0126\7v"+
		"\2\2\u0126\u0127\7a\2\2\u0127\u0128\7u\2\2\u0128\u0129\7n\2\2\u0129\u012a"+
		"\7q\2\2\u012a\u012b\7v\2\2\u012b$\3\2\2\2\u012c\u012d\7i\2\2\u012d\u012e"+
		"\7g\2\2\u012e\u012f\7v\2\2\u012f\u0130\7a\2\2\u0130\u0131\7f\2\2\u0131"+
		"\u0132\7g\2\2\u0132\u0133\7h\2\2\u0133\u0134\7c\2\2\u0134\u0135\7w\2\2"+
		"\u0135\u0136\7n\2\2\u0136\u0137\7v\2\2\u0137&\3\2\2\2\u0138\u0139\7i\2"+
		"\2\u0139\u013a\7g\2\2\u013a\u013b\7v\2\2\u013b\u013c\7a\2\2\u013c\u013d"+
		"\7g\2\2\u013d\u013e\7n\2\2\u013e\u013f\7g\2\2\u013f\u0140\7o\2\2\u0140"+
		"\u0141\7g\2\2\u0141\u0142\7p\2\2\u0142\u0143\7v\2\2\u0143(\3\2\2\2\u0144"+
		"\u0145\7i\2\2\u0145\u0146\7g\2\2\u0146\u0147\7v\2\2\u0147\u0148\7a\2\2"+
		"\u0148\u0149\7j\2\2\u0149\u014a\7g\2\2\u014a\u014b\7c\2\2\u014b\u014c"+
		"\7f\2\2\u014c*\3\2\2\2\u014d\u014e\7i\2\2\u014e\u014f\7g\2\2\u014f\u0150"+
		"\7v\2\2\u0150\u0151\7a\2\2\u0151\u0152\7v\2\2\u0152\u0153\7c\2\2\u0153"+
		"\u0154\7k\2\2\u0154\u0155\7n\2\2\u0155,\3\2\2\2\u0156\u0157\7i\2\2\u0157"+
		"\u0158\7g\2\2\u0158\u0159\7v\2\2\u0159\u015a\7a\2\2\u015a\u015b\7u\2\2"+
		"\u015b\u015c\7k\2\2\u015c\u015d\7|\2\2\u015d\u015e\7g\2\2\u015e.\3\2\2"+
		"\2\u015f\u0160\7k\2\2\u0160\u0161\7u\2\2\u0161\u0162\7a\2\2\u0162\u0163"+
		"\7g\2\2\u0163\u0164\7o\2\2\u0164\u0165\7r\2\2\u0165\u0166\7v\2\2\u0166"+
		"\u0167\7{\2\2\u0167\60\3\2\2\2\u0168\u0169\7k\2\2\u0169\u016a\7o\2\2\u016a"+
		"\u016b\7r\2\2\u016b\u016c\7n\2\2\u016c\u016d\7g\2\2\u016d\u016e\7o\2\2"+
		"\u016e\u016f\7g\2\2\u016f\u0170\7p\2\2\u0170\u0171\7v\2\2\u0171\62\3\2"+
		"\2\2\u0172\u0173\7g\2\2\u0173\u0174\7s\2\2\u0174\u0175\7w\2\2\u0175\u0176"+
		"\7c\2\2\u0176\u0177\7n\2\2\u0177\u0178\7u\2\2\u0178\64\3\2\2\2\u0179\u017a"+
		"\7>\2\2\u017a\u017b\7>\2\2\u017b\66\3\2\2\2\u017c\u017d\7?\2\2\u017d8"+
		"\3\2\2\2\u017e\u017f\7}\2\2\u017f:\3\2\2\2\u0180\u0181\7\177\2\2\u0181"+
		"<\3\2\2\2\u0182\u0183\7*\2\2\u0183>\3\2\2\2\u0184\u0185\7+\2\2\u0185@"+
		"\3\2\2\2\u0186\u0187\7]\2\2\u0187B\3\2\2\2\u0188\u0189\7_\2\2\u0189D\3"+
		"\2\2\2\u018a\u018b\7/\2\2\u018b\u018c\7@\2\2\u018cF\3\2\2\2\u018d\u018e"+
		"\7.\2\2\u018eH\3\2\2\2\u018f\u0190\7<\2\2\u0190J\3\2\2\2\u0191\u0192\7"+
		",\2\2\u0192L\3\2\2\2\u0193\u0194\7a\2\2\u0194N\3\2\2\2\u0195\u0196\7#"+
		"\2\2\u0196P\3\2\2\2\u0197\u0198\7B\2\2\u0198R\3\2\2\2\u0199\u019a\7\'"+
		"\2\2\u019a\u019b\7]\2\2\u019bT\3\2\2\2\u019c\u019d\7_\2\2\u019d\u019e"+
		"\7\'\2\2\u019eV\3\2\2\2\u019f\u01a0\7B\2\2\u01a0\u01a1\7B\2\2\u01a1X\3"+
		"\2\2\2\u01a2\u01a3\7@\2\2\u01a3\u01a4\7?\2\2\u01a4Z\3\2\2\2\u01a5\u01a6"+
		"\7>\2\2\u01a6\u01a7\7?\2\2\u01a7\\\3\2\2\2\u01a8\u01a9\7@\2\2\u01a9^\3"+
		"\2\2\2\u01aa\u01ab\7>\2\2\u01ab`\3\2\2\2\u01ac\u01ad\7?\2\2\u01ad\u01ae"+
		"\7?\2\2\u01aeb\3\2\2\2\u01af\u01b0\7#\2\2\u01b0\u01b1\7?\2\2\u01b1d\3"+
		"\2\2\2\u01b2\u01b3\7(\2\2\u01b3\u01b4\7(\2\2\u01b4f\3\2\2\2\u01b5\u01b6"+
		"\7~\2\2\u01b6\u01b7\7~\2\2\u01b7h\3\2\2\2\u01b8\u01b9\7~\2\2\u01b9j\3"+
		"\2\2\2\u01ba\u01bb\7A\2\2\u01bbl\3\2\2\2\u01bc\u01bd\7A\2\2\u01bd\u01be"+
		"\7A\2\2\u01ben\3\2\2\2\u01bf\u01c0\7\61\2\2\u01c0p\3\2\2\2\u01c1\u01c2"+
		"\7^\2\2\u01c2r\3\2\2\2\u01c3\u01c4\7\60\2\2\u01c4t\3\2\2\2\u01c5\u01c6"+
		"\7b\2\2\u01c6v\3\2\2\2\u01c7\u01c9\7a\2\2\u01c8\u01c7\3\2\2\2\u01c8\u01c9"+
		"\3\2\2\2\u01c9\u01ca\3\2\2\2\u01ca\u01d0\5\u0089E\2\u01cb\u01cf\5\u0089"+
		"E\2\u01cc\u01cf\5\u008bF\2\u01cd\u01cf\7a\2\2\u01ce\u01cb\3\2\2\2\u01ce"+
		"\u01cc\3\2\2\2\u01ce\u01cd\3\2\2\2\u01cf\u01d2\3\2\2\2\u01d0\u01ce\3\2"+
		"\2\2\u01d0\u01d1\3\2\2\2\u01d1x\3\2\2\2\u01d2\u01d0\3\2\2\2\u01d3\u01d4"+
		"\7/\2\2\u01d4\u01d5\7/\2\2\u01d5\u01d9\3\2\2\2\u01d6\u01d8\5\u0089E\2"+
		"\u01d7\u01d6\3\2\2\2\u01d8\u01db\3\2\2\2\u01d9\u01d7\3\2\2\2\u01d9\u01da"+
		"\3\2\2\2\u01daz\3\2\2\2\u01db\u01d9\3\2\2\2\u01dc\u01de\7/\2\2\u01dd\u01dc"+
		"\3\2\2\2\u01dd\u01de\3\2\2\2\u01de\u01e0\3\2\2\2\u01df\u01e1\5\u008bF"+
		"\2\u01e0\u01df\3\2\2\2\u01e1\u01e2\3\2\2\2\u01e2\u01e0\3\2\2\2\u01e2\u01e3"+
		"\3\2\2\2\u01e3|\3\2\2\2\u01e4\u01e6\7/\2\2\u01e5\u01e4\3\2\2\2\u01e5\u01e6"+
		"\3\2\2\2\u01e6\u01e7\3\2\2\2\u01e7\u01e8\5\u0085C\2\u01e8~\3\2\2\2\u01e9"+
		"\u01eb\7/\2\2\u01ea\u01e9\3\2\2\2\u01ea\u01eb\3\2\2\2\u01eb\u01ed\3\2"+
		"\2\2\u01ec\u01ee\5\u008bF\2\u01ed\u01ec\3\2\2\2\u01ee\u01ef\3\2\2\2\u01ef"+
		"\u01ed\3\2\2\2\u01ef\u01f0\3\2\2\2\u01f0\u01f1\3\2\2\2\u01f1\u01f2\5\u0087"+
		"D\2\u01f2\u0080\3\2\2\2\u01f3\u01f8\7$\2\2\u01f4\u01f7\5\u008fH\2\u01f5"+
		"\u01f7\n\2\2\2\u01f6\u01f4\3\2\2\2\u01f6\u01f5\3\2\2\2\u01f7\u01fa\3\2"+
		"\2\2\u01f8\u01f6\3\2\2\2\u01f8\u01f9\3\2\2\2\u01f9\u01fb\3\2\2\2\u01fa"+
		"\u01f8\3\2\2\2\u01fb\u01fc\7$\2\2\u01fc\u0082\3\2\2\2\u01fd\u0200\7)\2"+
		"\2\u01fe\u0201\5\u008fH\2\u01ff\u0201\n\3\2\2\u0200\u01fe\3\2\2\2\u0200"+
		"\u01ff\3\2\2\2\u0201\u0202\3\2\2\2\u0202\u0200\3\2\2\2\u0202\u0203\3\2"+
		"\2\2\u0203\u0204\3\2\2\2\u0204\u0205\7)\2\2\u0205\u0084\3\2\2\2\u0206"+
		"\u0208\5\u008bF\2\u0207\u0206\3\2\2\2\u0208\u0209\3\2\2\2\u0209\u0207"+
		"\3\2\2\2\u0209\u020a\3\2\2\2\u020a\u020b\3\2\2\2\u020b\u020f\7\60\2\2"+
		"\u020c\u020e\5\u008bF\2\u020d\u020c\3\2\2\2\u020e\u0211\3\2\2\2\u020f"+
		"\u020d\3\2\2\2\u020f\u0210\3\2\2\2\u0210\u0219\3\2\2\2\u0211\u020f\3\2"+
		"\2\2\u0212\u0214\7\60\2\2\u0213\u0215\5\u008bF\2\u0214\u0213\3\2\2\2\u0215"+
		"\u0216\3\2\2\2\u0216\u0214\3\2\2\2\u0216\u0217\3\2\2\2\u0217\u0219\3\2"+
		"\2\2\u0218\u0207\3\2\2\2\u0218\u0212\3\2\2\2\u0219\u0086\3\2\2\2\u021a"+
		"\u021b\t\4\2\2\u021b\u0088\3\2\2\2\u021c\u021d\t\5\2\2\u021d\u008a\3\2"+
		"\2\2\u021e\u021f\4\62;\2\u021f\u008c\3\2\2\2\u0220\u0221\t\6\2\2\u0221"+
		"\u008e\3\2\2\2\u0222\u0239\7^\2\2\u0223\u023a\t\7\2\2\u0224\u0226\7w\2"+
		"\2\u0225\u0224\3\2\2\2\u0226\u0227\3\2\2\2\u0227\u0225\3\2\2\2\u0227\u0228"+
		"\3\2\2\2\u0228\u0229\3\2\2\2\u0229\u022a\5\u008dG\2\u022a\u022b\5\u008d"+
		"G\2\u022b\u022c\5\u008dG\2\u022c\u022d\5\u008dG\2\u022d\u023a\3\2\2\2"+
		"\u022e\u0233\4\62\65\2\u022f\u0231\4\629\2\u0230\u0232\4\629\2\u0231\u0230"+
		"\3\2\2\2\u0231\u0232\3\2\2\2\u0232\u0234\3\2\2\2\u0233\u022f\3\2\2\2\u0233"+
		"\u0234\3\2\2\2\u0234\u023a\3\2\2\2\u0235\u0237\4\669\2\u0236\u0238\4\62"+
		"9\2\u0237\u0236\3\2\2\2\u0237\u0238\3\2\2\2\u0238\u023a\3\2\2\2\u0239"+
		"\u0223\3\2\2\2\u0239\u0225\3\2\2\2\u0239\u022e\3\2\2\2\u0239\u0235\3\2"+
		"\2\2\u023a\u0090\3\2\2\2\u023b\u023c\7^\2\2\u023c\u023d\13\2\2\2\u023d"+
		"\u0092\3\2\2\2\u023e\u0243\7$\2\2\u023f\u0242\5\u0091I\2\u0240\u0242\n"+
		"\b\2\2\u0241\u023f\3\2\2\2\u0241\u0240\3\2\2\2\u0242\u0245\3\2\2\2\u0243"+
		"\u0241\3\2\2\2\u0243\u0244\3\2\2\2\u0244\u0246\3\2\2\2\u0245\u0243\3\2"+
		"\2\2\u0246\u0247\7$\2\2\u0247\u0094\3\2\2\2\u0248\u0249\7\61\2\2\u0249"+
		"\u024a\7,\2\2\u024a\u024e\3\2\2\2\u024b\u024d\13\2\2\2\u024c\u024b\3\2"+
		"\2\2\u024d\u0250\3\2\2\2\u024e\u024f\3\2\2\2\u024e\u024c\3\2\2\2\u024f"+
		"\u0251\3\2\2\2\u0250\u024e\3\2\2\2\u0251\u0252\7,\2\2\u0252\u0253\7\61"+
		"\2\2\u0253\u0254\3\2\2\2\u0254\u0255\bK\2\2\u0255\u0096\3\2\2\2\u0256"+
		"\u0257\7\61\2\2\u0257\u0258\7\61\2\2\u0258\u025c\3\2\2\2\u0259\u025b\n"+
		"\t\2\2\u025a\u0259\3\2\2\2\u025b\u025e\3\2\2\2\u025c\u025a\3\2\2\2\u025c"+
		"\u025d\3\2\2\2\u025d\u025f\3\2\2\2\u025e\u025c\3\2\2\2\u025f\u0260\bL"+
		"\2\2\u0260\u0098\3\2\2\2\u0261\u0263\t\n\2\2\u0262\u0261\3\2\2\2\u0263"+
		"\u0264\3\2\2\2\u0264\u0262\3\2\2\2\u0264\u0265\3\2\2\2\u0265\u0266\3\2"+
		"\2\2\u0266\u0267\bM\3\2\u0267\u009a\3\2\2\2\u0268\u026a\t\13\2\2\u0269"+
		"\u0268\3\2\2\2\u026a\u026b\3\2\2\2\u026b\u0269\3\2\2\2\u026b\u026c\3\2"+
		"\2\2\u026c\u026d\3\2\2\2\u026d\u026e\bN\3\2\u026e\u009c\3\2\2\2\u026f"+
		"\u0270\13\2\2\2\u0270\u009e\3\2\2\2\37\2\u01c8\u01ce\u01d0\u01d9\u01dd"+
		"\u01e2\u01e5\u01ea\u01ef\u01f6\u01f8\u0200\u0202\u0209\u020f\u0216\u0218"+
		"\u0227\u0231\u0233\u0237\u0239\u0241\u0243\u024e\u025c\u0264\u026b\4\b"+
		"\2\2\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}