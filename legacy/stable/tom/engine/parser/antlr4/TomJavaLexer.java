// Generated from /Users/pem/github/tom/src/tom/engine/parser/antlr4/TomJava.g4 by ANTLR 4.5.3
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
public class TomJavaLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, PACKAGE=2, QID=3, MLCOMMENT=4, SLCOMMENT=5, WS=6, ANY=7;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "PACKAGE", "QID", "ID", "MLCOMMENT", "SLCOMMENT", "WS", "ANY"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'", "'package'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, "PACKAGE", "QID", "MLCOMMENT", "SLCOMMENT", "WS", "ANY"
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


	public TomJavaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "TomJava.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\tP\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\7\4!\n\4\f\4\16\4$\13\4\3\5\3\5\7"+
		"\5(\n\5\f\5\16\5+\13\5\3\6\3\6\3\6\3\6\7\6\61\n\6\f\6\16\6\64\13\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\7\7?\n\7\f\7\16\7B\13\7\3\7\3\7\3\b\6"+
		"\bG\n\b\r\b\16\bH\3\b\3\b\3\t\3\t\3\t\3\t\3\62\2\n\3\3\5\4\7\5\t\2\13"+
		"\6\r\7\17\b\21\t\3\2\6\4\2C\\c|\5\2\62;C\\c|\4\2\f\f\17\17\5\2\13\f\17"+
		"\17\"\"S\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2"+
		"\2\17\3\2\2\2\2\21\3\2\2\2\3\23\3\2\2\2\5\25\3\2\2\2\7\35\3\2\2\2\t%\3"+
		"\2\2\2\13,\3\2\2\2\r:\3\2\2\2\17F\3\2\2\2\21L\3\2\2\2\23\24\7=\2\2\24"+
		"\4\3\2\2\2\25\26\7r\2\2\26\27\7c\2\2\27\30\7e\2\2\30\31\7m\2\2\31\32\7"+
		"c\2\2\32\33\7i\2\2\33\34\7g\2\2\34\6\3\2\2\2\35\"\5\t\5\2\36\37\7\60\2"+
		"\2\37!\5\t\5\2 \36\3\2\2\2!$\3\2\2\2\" \3\2\2\2\"#\3\2\2\2#\b\3\2\2\2"+
		"$\"\3\2\2\2%)\t\2\2\2&(\t\3\2\2\'&\3\2\2\2(+\3\2\2\2)\'\3\2\2\2)*\3\2"+
		"\2\2*\n\3\2\2\2+)\3\2\2\2,-\7\61\2\2-.\7,\2\2.\62\3\2\2\2/\61\13\2\2\2"+
		"\60/\3\2\2\2\61\64\3\2\2\2\62\63\3\2\2\2\62\60\3\2\2\2\63\65\3\2\2\2\64"+
		"\62\3\2\2\2\65\66\7,\2\2\66\67\7\61\2\2\678\3\2\2\289\b\6\2\29\f\3\2\2"+
		"\2:;\7\61\2\2;<\7\61\2\2<@\3\2\2\2=?\n\4\2\2>=\3\2\2\2?B\3\2\2\2@>\3\2"+
		"\2\2@A\3\2\2\2AC\3\2\2\2B@\3\2\2\2CD\b\7\2\2D\16\3\2\2\2EG\t\5\2\2FE\3"+
		"\2\2\2GH\3\2\2\2HF\3\2\2\2HI\3\2\2\2IJ\3\2\2\2JK\b\b\2\2K\20\3\2\2\2L"+
		"M\13\2\2\2MN\3\2\2\2NO\b\t\2\2O\22\3\2\2\2\b\2\")\62@H\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}