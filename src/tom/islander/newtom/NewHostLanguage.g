/*
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2010, INPL, INRIA
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
 */

grammar NewHostLanguage;

options {
  backtrack=true;
  output=AST;
  ASTLabelType=Tree;
  //tokenVocab=HostTokens;
  tokenVocab=TomTokens;

//  k=6; // the default lookahead
  // a filter for the target language
  // permit to read every characters without defining them
//?  filter=TARGET;
  // fix the vocabulary to all characters
//?  charVocabulary='\u0000'..'\uffff';
}

@header{
  package newtom;
  //package tom.engine.parser;
  import org.antlr.runtime.tree.Tree;
}

//@rulecatch { }
// equivalent to
/*options{
  // antlr does not catch exceptions automaticaly
  defaultErrorHandler = false;
}*/

@lexer::header{
  package newtom;
  //package tom.engine.parser;
  import org.antlr.runtime.tree.Tree;
  import org.antlr.runtime.Token;
  import org.antlr.runtime.CommonTokenStream;
  import org.antlr.runtime.ANTLRInputStream;

  import java.lang.StringBuilder;
}

@parser::members{
  NewHostLanguageLexer targetlexer = new NewHostLanguageLexer();

  // returns the current goal language code
  private String getCode() {
    String result = targetlexer.target.toString();
    targetlexer.clearTarget();
    return result;
  }

}

@lexer::members{
  public static int nesting = 0;
  public Tree result;

  // override standard token emission
  public Token emit() {
    TomToken t = new TomToken(input, state.type, state.channel,
        state.tokenStartCharIndex, getCharIndex()-1,result);
    t.setLine(state.tokenStartLine);
    t.setText(state.text);
    t.setCharPositionInLine(state.tokenStartCharPositionInLine);
    t.setTree(result);
    emit(t);
    result = null;
    return t;
  }

  public static final int TVALUE_MATCH         = 1;
  public static final int TVALUE_STRATEGY      = 2;
  public static final int TVALUE_OPERATOR      = 3;
  public static final int TVALUE_TYPETERM      = 4;
  public static final int TVALUE_OPERATORLIST  = 5;
  public static final int TVALUE_OPERATORARRAY = 6;
 
  public ParserRuleReturnScope parser(CharStream input, int tvalue) {
    NewTomLanguageLexer lexer = new NewTomLanguageLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    NewTomLanguageParser parser = new NewTomLanguageParser(tokens);
    ParserRuleReturnScope res = null;
    try{
    switch(tvalue) {
      case TVALUE_MATCH:
        res = parser.matchConstruct();
        break;
      case TVALUE_STRATEGY:
        res = parser.strategyConstruct();
        break;
      case TVALUE_OPERATOR:
        res = parser.operator();
        break;
      case TVALUE_TYPETERM:
        res = parser.typeTerm();
        break;
      case TVALUE_OPERATORLIST:
        res = parser.operatorList();
        break;
      case TVALUE_OPERATORARRAY:
        res = parser.operatorArray();
        break;
      default: System.out.println("Problem"); break;
    }
    }
    catch(Exception e) { 
      System.err.println(e);
    }
    return res;
  }
//////
  public StringBuilder target = new StringBuilder("");
    // clear the buffer
    public void clearTarget(){
        target.delete(0,target.length());
    }

}

// The grammar starts here
program : blockList -> ^(Program blockList);

blockList : (block)* -> ^(BlockList (block)* ) ;

block :
  backquoteConstruct -> ^(BackQuoteConstruct backquoteConstruct)
    // either a tom construct or everything else
  | matchConstruct
  | strategyConstruct
//  | gomsignature
  | operator
  | operatorList
  | operatorArray
  | includeConstruct
  | typeTerm
  | LBRACE blockList RBRACE -> ^(BracedBlockList blockList)
//  | s=Identifier /*STRING*/ -> ^(TLCodeBlock ^(ITL $s))
  | tlCodeBlock
  ;

tlCodeBlock :
//  s=ID /*STRING*/ -> ^(TLCodeBlock ^(ITL {getCode()}))
  s=Identifier /*s=IDCODE+*/ /*STRING*/ -> ^(TLCodeBlock ^(ITL $s))
//  (s+=Identifier|s+=WS|s+=ESC|s+=SpecialCharacters) /*s=IDCODE+*/ /*STRING*/ -> ^(TLCodeBlock ^(ITL ($s)+))// {getCode()}
  //s=POMP -> ^(TLCodeBlock ^(ITL $s))// {getCode()}
  ;

//goalLanguageBlock :
  // we are here because goalLanguageBlock has been called in
  // NewTomLanguageParser. cf. GOALLBRACE <-> '{' with some java code
  /*LBRACE*/ //blockList RBRACE -> blockList  //^(BlockList blockList)
//  ;

// the %strategy construct
strategyConstruct :
  t=STRATEGY -> ^({((TomToken)$t).getTree()})
  ;

//%match
matchConstruct :
  t=MATCH -> ^({((TomToken)$t).getTree()})
  ;

//%gom
/* // will be different
gomsignature :
  t=GOM -> ^({((TomToken)$t).getTree()})
  ;*/

// `(...)
backquoteConstruct :
  t=BACKQUOTE -> ^({((TomToken)$t).getTree()})
  ;

//%op
operator :
  t=OPERATOR -> ^({((TomToken)$t).getTree()})
  ;

//%oplist
operatorList :
  t=OPERATORLIST -> ^({((TomToken)$t).getTree()})
  ;

//%oparray
operatorArray :
  t=OPERATORARRAY -> ^({((TomToken)$t).getTree()})
  ;

//%include
includeConstruct :
  INCLUDE LBRACE filename=FILENAME RBRACE -> ^(Include $filename)
  ;

//code :
//  t=CODE -> ^({((TomToken)$t).getTree()})
//  ;

//%typeterm
typeTerm :
  t=TYPETERM -> ^({((TomToken)$t).getTree()})
  ;



// LEXER

//options { filter=true; }
// here begins tokens definition
// the following tokens are keywords for tom constructs
// when read, we switch lexers to tom

BACKQUOTE : '`('
  {
//    System.out.println("\nbefore new BackQuote*");
    NewBackQuoteLanguageLexer lexer = new NewBackQuoteLanguageLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
//    System.out.println("host, tokens = " + tokens.toString() + " /fin");
//    System.out.println("host, tokens list = " + tokens.getTokens().toString());
    NewBackQuoteLanguageParser parser = new NewBackQuoteLanguageParser(tokens);
//    System.out.println("before parser.backQuoteConstruct()");
//OJD    NewBackQuoteLanguageParser.backQuoteTerm_return res = parser.backQuoteTerm();
    NewBackQuoteLanguageParser.expression_return res = parser.expression();
//    System.out.println("(host - bq) res.getTree() =\n" + ((Tree)res.getTree()).toStringTree());
    result = (Tree)res.getTree();
//    System.out.println("(host - bq) end, result =\n" + result.toStringTree());
  }
  ;

MATCH : '%match'
  { result = (Tree)parser(input, TVALUE_MATCH).getTree(); }
  ;

TYPETERM : '%typeterm'
  { result = (Tree)parser(input, TVALUE_TYPETERM).getTree(); }
  ;

STRATEGY : '%strategy' 
  { result = (Tree)parser(input, TVALUE_STRATEGY).getTree(); }
  ;

OPERATOR : '%op' 
  { result = (Tree)parser(input, TVALUE_OPERATOR).getTree(); }
  ;

OPERATORLIST : '%oplist' 
  { result = (Tree)parser(input, TVALUE_OPERATORLIST).getTree(); }
  ;

OPERATORARRAY : '%oparray'
  { result = (Tree)parser(input, TVALUE_OPERATORARRAY).getTree(); }
  ;

// following tokens are keyword for tom constructs
// do not need to switch lexers
INCLUDE : '%include' ;

GOM : '%gom'
  (
    |
    (
     '('
     (
     options {
       greedy=false;
       //generateAmbigWarnings=false; // shut off newline errors
     }
     : '\r' '\n' //{newline();}
     | '\r'    //{newline();}
     | '\n'    //{newline();}
     | ~('\n'|'\r')
    )*
    ')')
    )
  ;

/* TO ADD after a first Tom integration
GOM : '%gom'
  {
    GomLanguageLexer lexer = new GomLanguageLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    GomLanguageParser parser = new GomLanguageParser(tokens);
    GomLanguageParser.module_return res = parser.module();
    result = (Tree)res.getTree();
  }
  ;
*/

// basic tokens
LBRACE : '{' { nesting++; } {System.out.println("host nesting++ = " + nesting);}
  ;

RBRACE : '}'
  {
    if ( nesting<=0 ) {
      System.out.println("exit embedded hostlanguage and emit(Token.EOF)\n");
      emit(Token.EOF_TOKEN);
    }
    else {
      nesting--;
      System.out.println("host nesting-- = " + nesting);
    }
  }
  ;

/*
ID : ('a'..'z' | 'A'..'Z')
     ('a'..'z' | 'A'..'Z' | '0'..'9' | '_' | '-')* ;
*/
/*
fragment LETTER    :   ('a'..'z' | 'A'..'Z')   ;
fragment DIGIT     :   ('0'..'9')  ;
ID
options{testLiterals = true;}
    :
        ('_')? LETTER
        (
            options{greedy = true;}:
            ( LETTER | DIGIT | '_' )
        )*
        {
            target.append($getText);
        }
    ;
*/

/*
fragment
POMP : POM+ ;

fragment
POM: (Identifier|WS|SpecialCharacters);
*/

Identifier:
  Letter (Letter|JavaIDDigit)*
  ;

fragment
Letter
    :  '\u0024' |
       '\u0041'..'\u005a' |
       '\u005f' |
       '\u0061'..'\u007a' |
       '\u00c0'..'\u00d6' |
       '\u00d8'..'\u00f6' |
       '\u00f8'..'\u00ff' |
       '\u0100'..'\u1fff' |
       '\u3040'..'\u318f' |
       '\u3300'..'\u337f' |
       '\u3400'..'\u3d2d' |
       '\u4e00'..'\u9fff' |
       '\uf900'..'\ufaff'
    ;

fragment
JavaIDDigit
    :  '\u0030'..'\u0039' |
       '\u0660'..'\u0669' |
       '\u06f0'..'\u06f9' |
       '\u0966'..'\u096f' |
       '\u09e6'..'\u09ef' |
       '\u0a66'..'\u0a6f' |
       '\u0ae6'..'\u0aef' |
       '\u0b66'..'\u0b6f' |
       '\u0be7'..'\u0bef' |
       '\u0c66'..'\u0c6f' |
       '\u0ce6'..'\u0cef' |
       '\u0d66'..'\u0d6f' |
       '\u0e50'..'\u0e59' |
       '\u0ed0'..'\u0ed9' |
       '\u1040'..'\u1049'
   ;

//fragment
//SpecialCharacters
//    : /*'@'*/ '\u0040' | '\u0021'..'\u002f' /*'\u002ea' | '(' | ')' | '/'*/ | '\\' | '<' | '>' //| '{' | '}'
    //'\u0021'..'\u002f'
//    ;

fragment
HEX_DIGIT
  : ('0'..'9'|'A'..'F'|'a'..'f')
  ;

// tokens to skip : white spaces
WS : ( ' '
 | '\t'
 | '\f'
 // handle newlines
 | ( '\r\n'  // Evil DOS
   | '\r'    // Macintosh
   | '\n'    // Unix (the right way)
   )
//   { newline(); }
  )
  { $channel=HIDDEN; }
  ;

// comments : HIDDEN for the moment, but should be kept
COMMENT :
  ( SL_COMMENT | ML_COMMENT )
  { $channel=HIDDEN; }
  ;

fragment
ML_COMMENT : 
  '/*' ( options {greedy=false;} : . )* '*/'
//  { $channel=HIDDEN; }
  ;

fragment
SL_COMMENT :
  '//' ~('\n'|'\r')* '\r'? '\n'
//  { $channel=HIDDEN; }
  ;

/*CODE
    :
        '%' '['
        ( { LA(2)!='%' }? ']'
        |
        )
        (
            options {
                greedy=false;
                generateAmbigWarnings=false; // shut off newline errors
            }
        : '\r' '\n' {newline();}
        | '\r'    {newline();}
        | '\n'    {newline();}
        | ~('\n'|'\r')
        )*
        ']' '%'
;*/

/*
file is :
  ./fileName | ./../../fileName | ../../fileName | fileName | path/to/fileName | ./path/to/fileName | ../../path/to/fileName

./////./../.././././a/b/b/b/.././sl.tom is valid ? => yes
a.b.c.sl.tom
*/
FILENAME : // to modify
  ( './' | '../' | '/' )* (Identifier ( './' | '../' | '/' )+)* Identifier ('.' Identifier)*
  ;

/*
fragment
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
      : '0'..'7'
        (
        : '0'..'7'
        )?
      )?
    | '4'..'7'
      (
      : '0'..'7'
      )?
    )
  ;
*/

/*
STRING
  : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"'
        {
          System.out.println("!!!!! " + $text  + " !!!!!!");
            target.append(getText());
        }
  ;
*/

// the rule for the filter: just append the text to the buffer
fragment
TARGET :
  (.)
  {System.out.println("#### " + $text  + " #####");}
  {target.append(getText());}
  ;

//IDCODE : Identifier| WS | ESC | SpecialCharacters;

/*fragment
ALL : ('\u0000'..'\uffff')
  //{target.append(getText());}
  ;*/
