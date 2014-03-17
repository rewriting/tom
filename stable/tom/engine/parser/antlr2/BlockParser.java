// $ANTLR 2.7.7 (20060906): "BlockParser.g" -> "BlockParser.java"$

  /*
   * Tom
   *
   * Copyright (c) 2010-2014, Universite de Lorraine, Inria
   * Nancy, France.
   *
   * This program is free software; you can redistribute it and/or modify
   * it under the terms of the GNU General Public License as published by
   * the Free Software Foundation; either version 2 of the License, or
   * (at your option) any later version.
   *
   * This program is distributed in the hope that it will be useful,
   * but WITHOUT ANY WARRANTY; without even the implied warranty of
   * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   * GNU General Public License for more details.
   *
   * You should have received a copy of the GNU General Public License
   * along with this program; if not, write to the Free Software
   * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
   *
   * Antoine Reilles    e-mail: Antoine.Reilles@loria.fr
   *
   **/
  package tom.engine.parser.antlr2;

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

  import java.util.LinkedList;
  import java.util.Iterator;
  import java.util.logging.Logger;
  import java.util.logging.Level;

  import antlr.LexerSharedInputState;

public class BlockParser extends antlr.LLkParser       implements BlockParserTokenTypes
 {

  private static final String REAL ="real";
  private static final String DOUBLE ="double";

  private BlockLexer lexer = null;

  public static BlockParser makeBlockParser(LexerSharedInputState state) {
    return new BlockParser(new BlockLexer(state),"BlockParser");
  }

  public BlockParser(BlockLexer lexer, String message) {
    this(lexer);
    /* the message attribute is used for constructor disambiguation */
    this.lexer = lexer;
  }

protected BlockParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public BlockParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected BlockParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public BlockParser(TokenStream lexer) {
  this(lexer,1);
}

public BlockParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final String  block() throws RecognitionException, TokenStreamException {
		String block;
		
		block = "?";
		
		match(LBRACE);
		/* Verify there was nothing more than only a LBRACE in the input */
		if (!lexer.target.toString().trim().equals("{"))
		throw new RecognitionException("Expecting \"{\", found \""+lexer.target.toString()+"\"");
		
		rawblocklist();
		match(RBRACE);
		
		block = lexer.target.toString();
		lexer.clearTarget();
		
		return block;
	}
	
	public final void rawblocklist() throws RecognitionException, TokenStreamException {
		
		
		{
		_loop4:
		do {
			switch ( LA(1)) {
			case STRING:
			{
				match(STRING);
				break;
			}
			case LBRACE:
			{
				match(LBRACE);
				rawblocklist();
				match(RBRACE);
				break;
			}
			default:
			{
				break _loop4;
			}
			}
		} while (true);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"LBRACE",
		"RBRACE",
		"STRING",
		"ESC",
		"HEX_DIGIT",
		"WS",
		"SL_COMMENT",
		"ML_COMMENT",
		"TARGET"
	};
	
	
	}
