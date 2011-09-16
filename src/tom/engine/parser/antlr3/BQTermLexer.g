lexer grammar BQTermLexer;
options {
  filter=true;
  tokenVocab=CSTTokens;
}
/*
 * Beware : Due to the use of option filter, rules order matters.
 */


@lexer::header {
package tom.engine.parser.antlr3;
}

@lexer::members {
  private final TokenCustomizer tokenCustomizer = new TokenCustomizer();
  
  @Override 
  public void emit(Token t){
    super.emit(tokenCustomizer.customize(t));
  }
}


BQIDPAR  : BQ (FragWS)* FragID (FragWS)* LPAR //'('
  {state.text = $FragID.text;}
  //{System.out.println("Lexed BQIDPAR : '"+$text+"'");}
;

BQIDBR   : BQ (FragWS)* FragID (FragWS)* '['
  {state.text = $FragID.text;}
  //{System.out.println("Lexed BQIDBR : '"+$text+"'");}
;

BQIDSTAR : BQ FragID (FragWS)* '*'
  {state.text = $FragID.text;}
  //{System.out.println("Lexed BQIDSTAR : '"+$text+"'");}
  {tokenCustomizer.prepareNextToken(input.mark());};

BQID     : BQ FragID
  {state.text = $FragID.text;}
  //{System.out.println("Lexed BQID : '"+$text+"'");}
  {tokenCustomizer.prepareNextToken(input.mark());};

BQPAR    : BQ LPAR //'('
  {state.text = "(";}
  //{System.out.println("Lexed BQPAR : '"+$text+"'");}
;

IDPAR    : FragID (FragWS)* LPAR //'(' 
  {state.text = $FragID.text;}
  //{System.out.println("Lexed IDPAR : '"+$text+"'");}
;

IDBR     : FragID (FragWS)* '['
  {state.text = $FragID.text;}
  //{System.out.println("Lexed IDBR : '"+$text+"'");}
;

IDSTAR   : FragID '*'
  {state.text = $FragID.text;}
  //{System.out.println("Lexed IDSTAR : '"+$text+"'");}
;

ID       : FragID
  //{System.out.println("Lexed ID : '"+$text+"'");}
;

UNDERSCORE : '_';

COMMA  : ',' ;
LPAR   : '(' ;
RPAR   : ')' {tokenCustomizer.prepareNextToken(input.mark());};
RBR    : ']' {tokenCustomizer.prepareNextToken(input.mark());};
EQUAL  : '=' ;
fragment
MINUS  : '-' ;

//XML_START : '<';
//XML_CLOSE : '>' ;
//DOUBLE_QUOTE: '\"';

fragment
BQ      : '`';
fragment
FragID      :  ('_')? LETTER (LETTER|DIGIT|'_')*;

fragment
BQESC : '\\'
    ( 'n'
    | 'r'
    | 't'
    | 'b'
    | 'f'
    | '"'
    | '\''
    | '\\'
    //from older parser: need an adaptation?
   /* | ('u')+ BQ_HEX_DIGIT BQ_HEX_DIGIT BQ_HEX_DIGIT BQ_HEX_DIGIT
    | '0'..'3'
      (
      '0'..'7'
        (
        '0'..'7'
        )?
      )?
    | '4'..'7'
      (
      '0'..'7'
      )?*/
    )
  ;

BQSTRING : '"' (BQESC|~('"'|'\\'|'\n'|'\r'))* '"';
BQCHAR   : '\'' (~('\''|'\n'|'\r'|'\\') )+ '\'';
fragment
INTEGER  : ( MINUS )? ( DIGIT )+ ;
BQDOT    : '.' ;
NUM : INTEGER (BQDOT DIGIT*)? ;

fragment
LETTER  : ('A'..'Z' | 'a'..'z');
fragment
DIGIT   : '0'..'9';

fragment
FragWS      : ('\r' | '\n' | '\t' | ' ' );

WS :
FragWS {$channel=HIDDEN;} ;

SL_COMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
ML_COMMENT : '/*' ( options {greedy=false;} : . )* '*/'{ $channel=HIDDEN; } ;

ANY : . ;
