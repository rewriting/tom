header {
  /*
   * Tom
   *
   * Copyright (c) 2010-2016, Universite de Lorraine, Inria
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
}

{
  import java.util.LinkedList;
  import java.util.Iterator;
  import java.util.logging.Logger;
  import java.util.logging.Level;

  import antlr.LexerSharedInputState;
}
class BlockParser extends Parser;
options {
  //k=2; // because of field definition
  defaultErrorHandler = false;
}
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
}

block returns [String block]
{ block = "?"; }
:
 LBRACE { /* Verify there was nothing more than only a LBRACE in the input */
   if (!lexer.target.toString().trim().equals("{"))
    throw new RecognitionException("Expecting \"{\", found \""+lexer.target.toString()+"\"");
}
  rawblocklist
  RBRACE
{
  block = lexer.target.toString();
  lexer.clearTarget();
}
;

rawblocklist
: (
    STRING
    | LBRACE rawblocklist RBRACE
  )*
;

// here begins the lexer
{
  import antlr.*;
}
class BlockLexer extends Lexer;
options {
  k=6; // the default lookahead

    // a filter for the target language
    // permit to read every characters without defining them
    filter=TARGET;

    // fix the vocabulary to all characters
    charVocabulary='\u0000'..'\uffff';
}

{
    // this buffer contains the target code
    // we append each read character by lexer
    public StringBuffer target = new StringBuffer("");

    // clear the buffer
    public void clearTarget(){
        target.delete(0,target.length());
    }
}


// basic tokens
LBRACE
: '{'
{
  target.append($getText);
}
;
RBRACE
: '}'
{
  target.append($getText);
}
;

STRING
  : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"'
        {
            target.append($getText);
        }
  ;

protected
ESC
  : '\\'
    ( 'n'
    | 'r'
    | 't'
    | 'b'
    | 'f'
    | '"'
    | '\''
    | '\\'
    | ('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    | '0'..'3'
      (
        options {
          warnWhenFollowAmbig = false;
        }
      : '0'..'7'
        (
          options {
            warnWhenFollowAmbig = false;
          }
        : '0'..'7'
        )?
      )?
    | '4'..'7'
      (
        options {
          warnWhenFollowAmbig = false;
        }
      : '0'..'7'
      )?
    )
  ;

protected
HEX_DIGIT
  : ('0'..'9'|'A'..'F'|'a'..'f')
  ;

// tokens to skip : white spaces
WS  : ( ' '
    | '\t'
    | '\f'
    // handle newlines
    | ( "\r\n"  // Evil DOS
      | '\r'    // Macintosh
      | '\n'    // Unix (the right way)
      )
      { newline(); }
    )
        {
            target.append($getText);
            $setType(Token.SKIP);
        }
    ;

// comments
//COMMENT
//:
//( SL_COMMENT | ML_COMMENT )
//{ $setType(Token.SKIP);}
//;

protected
SL_COMMENT
:
"//"
( ~('\n'|'\r') )*
(
 options {
 generateAmbigWarnings=false;
 }
 : '\r' '\n'
 | '\r'
 | '\n'
 )
{
  target.append($getText);
  newline();
  $setType(Token.SKIP);
}
;

protected
ML_COMMENT
:
"/*"
( { LA(2)!='/' }? '*'
  |
  )
(
 options {
 greedy=false;  // make it exit upon "*/"
 generateAmbigWarnings=false; // shut off newline errors
 }
 : '\r' '\n' {newline();}
 | '\r'    {newline();}
 | '\n'    {newline();}
 | ~('\n'|'\r')
 )*
"*/"
{target.append($getText);
 //$setType(Token.SKIP);
}
;

// the rule for the filter: just append the text to the buffer
protected
TARGET
:
( . )
{target.append($getText);}
;
