// $ANTLR 2.7.2: "TomJavaParser.g" -> "TomJavaParser.java"$

package jtom.parser;
import antlr.*;
import java.io.*;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

public class TomJavaParser extends antlr.LLkParser       implements TomJavaParserTokenTypes
 {

  public static TomJavaParser createParser(String fileName) throws FileNotFoundException,IOException {
    File file = new File(fileName);
    TomJavaLexer lexer = new TomJavaLexer(new BufferedReader(new FileReader(file)));
    return new TomJavaParser(lexer);
  }

protected TomJavaParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public TomJavaParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected TomJavaParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public TomJavaParser(TokenStream lexer) {
  this(lexer,1);
}

public TomJavaParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final String  javaPackageDeclaration() throws RecognitionException, TokenStreamException {
		String result;
		
		
		result = "";
		
		
		if ((LA(1)==PACKAGE)) {
			match(PACKAGE);
			result=javaName();
			match(SEMICOLON);
		}
		else if (((LA(1) >= PACKAGE && LA(1) <= ML_COMMENT))) {
			matchNot(EOF);
		}
		else if ((LA(1)==EOF)) {
			match(Token.EOF_TYPE);
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		return result;
	}
	
	public final String  javaName() throws RecognitionException, TokenStreamException {
		String result;
		
		Token  name = null;
		Token  sname = null;
		
		result = "";
		
		
		name = LT(1);
		match(ID);
		result += name.getText();
		{
		_loop4:
		do {
			if ((LA(1)==DOT)) {
				match(DOT);
				sname = LT(1);
				match(ID);
				result += "."+sname.getText();
			}
			else {
				break _loop4;
			}
			
		} while (true);
		}
		return result;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"package\"",
		"SEMICOLON",
		"ID",
		"DOT",
		"LETTER",
		"DIGIT",
		"WS",
		"COMMENT",
		"SL_COMMENT",
		"ML_COMMENT"
	};
	
	
	}
