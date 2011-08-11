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


BQIDPAR  : BQ (WS)* FragID (WS)* '('
  {state.text = $FragID.text;}
  //{System.out.println("Lexed BQIDPAR : '"+$text+"'");}
;

BQIDBR   : BQ (WS)* FragID (WS)* '['
  {state.text = $FragID.text;}
  //{System.out.println("Lexed BQIDBR : '"+$text+"'");}
;

BQIDSTAR : BQ FragID (WS)* '*'
  {state.text = $FragID.text;}
  //{System.out.println("Lexed BQIDSTAR : '"+$text+"'");}
  {tokenCustomizer.prepareNextToken(input.mark());};

BQID     : BQ FragID
  {state.text = $FragID.text;}
  //{System.out.println("Lexed BQID : '"+$text+"'");}
  {tokenCustomizer.prepareNextToken(input.mark());};

BQPAR    : BQ '('
  //{System.out.println("Lexed BQPAR : '"+$text+"'");}
;

IDPAR    : FragID (WS)* '(' 
  {state.text = $FragID.text;}
  //{System.out.println("Lexed IDPAR : '"+$text+"'");}
;

IDBR     : FragID (WS)* '['
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
WS      : ('\r' | '\n' | '\t' | ' ' );


SL_COMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
ML_COMMENT : '/*' ( options {greedy=false;} : . )* '*/'{ $channel=HIDDEN; } ;

// lexer need a rule for every input
// even for chars we don't use
DEFAULT : . { $channel=HIDDEN;};
