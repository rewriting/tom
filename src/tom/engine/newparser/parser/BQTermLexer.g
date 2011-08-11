lexer grammar BQTermLexer;
options {
  filter=true;
  tokenVocab=CSTTokens;
}

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
  {System.out.println("Lexed BQIDPAR : '"+$text+"'");}
;

BQIDBR   : BQ (WS)* FragID (WS)* '['
  {state.text = $FragID.text;}
  {System.out.println("Lexed BQIDBR : '"+$text+"'");}
;

BQIDSTAR : BQ FragID (WS)* '*'
  {state.text = $FragID.text;}
  {System.out.println("Lexed BQIDSTAR : '"+$text+"'");}
  {tokenCustomizer.prepareNextToken(input.mark());};

BQID     : BQ FragID
  {state.text = $FragID.text;}
  {System.out.println("Lexed BQID : '"+$text+"'");}
  {tokenCustomizer.prepareNextToken(input.mark());};



IDPAR    : FragID (WS)* '(' 
  {state.text = $FragID.text;}
  {System.out.println("Lexed IDPAR : '"+$text+"'");}
;

IDBR     : FragID (WS)* '['
  {state.text = $FragID.text;}
  {System.out.println("Lexed IDBR : '"+$text+"'");}
;

IDSTAR   : FragID '*'
  {state.text = $FragID.text;}
  {System.out.println("Lexed IDSTAR : '"+$text+"'");}
;

ID       : FragID
  {System.out.println("Lexed ID : '"+$text+"'");}
;


COMMA  : ',' ;
RPAR   : ')' {tokenCustomizer.prepareNextToken(input.mark());};
RBR    : ']' {tokenCustomizer.prepareNextToken(input.mark());};
EQUAL  : '=' ;
UNDERSCORE : '_';

fragment
BQ      : '`';
fragment
FragID      : ('A'..'Z' | 'a'..'z' |'_') ('A'..'Z' | 'a'..'z' |'_'|'0'..'9')*;
fragment
WS      : ('\r' | '\n' | '\t' | ' ' );


SL_COMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
ML_COMMENT : '/*' ( options {greedy=false;} : . )* '*/'{ $channel=HIDDEN; } ;

// lexer need a rule for every input
// even for chars we don't use
DEFAULT : . { $channel=HIDDEN;};
