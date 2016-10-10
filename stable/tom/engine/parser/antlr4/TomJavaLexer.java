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
		JAVA_PACKAGE=1, MLCOMMENT=2, SLCOMMENT=3, WS=4, ANY=5;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"JAVA_PACKAGE", "MLCOMMENT", "SLCOMMENT", "WS", "ANY"
	};

	private static final String[] _LITERAL_NAMES = {
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "JAVA_PACKAGE", "MLCOMMENT", "SLCOMMENT", "WS", "ANY"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\7A\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7\2"+
		"\27\n\2\f\2\16\2\32\13\2\3\2\3\2\3\3\3\3\3\3\3\3\7\3\"\n\3\f\3\16\3%\13"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\7\4\60\n\4\f\4\16\4\63\13\4\3\4"+
		"\3\4\3\5\6\58\n\5\r\5\16\59\3\5\3\5\3\6\3\6\3\6\3\6\3#\2\7\3\3\5\4\7\5"+
		"\t\6\13\7\3\2\5\3\2==\4\2\f\f\17\17\5\2\13\f\17\17\"\"D\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\3\r\3\2\2\2\5\35\3\2\2"+
		"\2\7+\3\2\2\2\t\67\3\2\2\2\13=\3\2\2\2\r\16\7r\2\2\16\17\7c\2\2\17\20"+
		"\7e\2\2\20\21\7m\2\2\21\22\7c\2\2\22\23\7i\2\2\23\24\7g\2\2\24\30\3\2"+
		"\2\2\25\27\n\2\2\2\26\25\3\2\2\2\27\32\3\2\2\2\30\26\3\2\2\2\30\31\3\2"+
		"\2\2\31\33\3\2\2\2\32\30\3\2\2\2\33\34\7=\2\2\34\4\3\2\2\2\35\36\7\61"+
		"\2\2\36\37\7,\2\2\37#\3\2\2\2 \"\13\2\2\2! \3\2\2\2\"%\3\2\2\2#$\3\2\2"+
		"\2#!\3\2\2\2$&\3\2\2\2%#\3\2\2\2&\'\7,\2\2\'(\7\61\2\2()\3\2\2\2)*\b\3"+
		"\2\2*\6\3\2\2\2+,\7\61\2\2,-\7\61\2\2-\61\3\2\2\2.\60\n\3\2\2/.\3\2\2"+
		"\2\60\63\3\2\2\2\61/\3\2\2\2\61\62\3\2\2\2\62\64\3\2\2\2\63\61\3\2\2\2"+
		"\64\65\b\4\2\2\65\b\3\2\2\2\668\t\4\2\2\67\66\3\2\2\289\3\2\2\29\67\3"+
		"\2\2\29:\3\2\2\2:;\3\2\2\2;<\b\5\2\2<\n\3\2\2\2=>\13\2\2\2>?\3\2\2\2?"+
		"@\b\6\2\2@\f\3\2\2\2\7\2\30#\619\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}