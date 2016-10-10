// Generated from /Users/pem/github/tom/src/tom/engine/parser/antlr4/TomJava.g4 by ANTLR 4.5.3
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
public class TomJavaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		JAVA_PACKAGE=1, MLCOMMENT=2, SLCOMMENT=3, WS=4, ANY=5;
	public static final int
		RULE_start = 0;
	public static final String[] ruleNames = {
		"start"
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

	@Override
	public String getGrammarFileName() { return "TomJava.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TomJavaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class StartContext extends ParserRuleContext {
		public TerminalNode JAVA_PACKAGE() { return getToken(TomJavaParser.JAVA_PACKAGE, 0); }
		public List<TerminalNode> ANY() { return getTokens(TomJavaParser.ANY); }
		public TerminalNode ANY(int i) {
			return getToken(TomJavaParser.ANY, i);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(3);
			_la = _input.LA(1);
			if (_la==JAVA_PACKAGE) {
				{
				setState(2);
				match(JAVA_PACKAGE);
				}
			}

			setState(8);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ANY) {
				{
				{
				setState(5);
				match(ANY);
				}
				}
				setState(10);
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\7\16\4\2\t\2\3\2"+
		"\5\2\6\n\2\3\2\7\2\t\n\2\f\2\16\2\f\13\2\3\2\2\2\3\2\2\2\16\2\5\3\2\2"+
		"\2\4\6\7\3\2\2\5\4\3\2\2\2\5\6\3\2\2\2\6\n\3\2\2\2\7\t\7\7\2\2\b\7\3\2"+
		"\2\2\t\f\3\2\2\2\n\b\3\2\2\2\n\13\3\2\2\2\13\3\3\2\2\2\f\n\3\2\2\2\4\5"+
		"\n";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}