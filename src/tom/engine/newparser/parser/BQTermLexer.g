lexer grammar BQTermLexer;
options {
  filter=true;
  tokenVocab=CSTTokens;
}
/*
 * Beware : Due to the use of option filter, rules order matters.
 */


@lexer::header {
package tom.engine.newparser.parser;
}

@lexer::members {
  private final TokenCustomizer tokenCustomizer = new TokenCustomizer();
  
  @Override 
  public void emit(Token t){
    super.emit(tokenCustomizer.customize(t));
  }
}


BQIDPAR  : BQ (FragWS)* FragID (FragWS)* '('
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

BQPAR    : BQ '('
  {state.text = "(";}
  //{System.out.println("Lexed BQPAR : '"+$text+"'");}
;

IDPAR    : FragID (FragWS)* '(' 
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
RPAR   : ')' {tokenCustomizer.prepareNextToken(input.mark());};
RBR    : ']' {tokenCustomizer.prepareNextToken(input.mark());};
EQUAL  : '=' ;


fragment
BQ      : '`';
fragment
FragID      :  ('_')?(LETTER|DIGIT|'_')+;
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
