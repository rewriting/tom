grammar BQTerm;

options {
  output=AST;
  ASTLabelType=Tree;
  backtrack=true;
  tokenVocab=CSTTokens;
}

@parser::header {
package tom.engine.newparser.parser;
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

/* actual backquote already consumed */
csBQTerm
returns [int marker] :
  IDENTIFIER (WS)? LPAR
    (WS)? (csNonHeadBQTerm ((WS)? COMMA (WS)? csNonHeadBQTerm)*)? (WS)? RPAR
  {$marker = ((CustomToken)$RPAR).getPayload(Integer.class);}

  -> ^(cst_BQAppl ^(cst_Name IDENTIFIER)
                ^(cst_concBQTerm csNonHeadBQTerm*))
 
 |IDENTIFIER (WS)? LBR
    (WS)? (csPairSlotBQTerm ((WS)? COMMA (WS)? csPairSlotBQTerm)*)? (WS)? RBR
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}
  
  -> ^(cst_BQRecordAppl
     	^(cst_Name IDENTIFIER)
        ^(cst_concCstPairSlotBQTerm csPairSlotBQTerm*)) 
 
 |IDENTIFIER STAR
  {$marker = ((CustomToken)$STAR).getPayload(Integer.class);}

  -> ^(cst_BQVarStar ^(cst_Name IDENTIFIER))
 
 |IDENTIFIER
  {$marker = ((CustomToken)$IDENTIFIER).getPayload(Integer.class);}

  -> ^(cst_BQVar ^(cst_Name IDENTIFIER))
; 

csNonHeadBQTerm :
  csBQTerm
  -> csBQTerm

 |UNDERSCORE
  -> ^(cst_BQDefault)
; 

csPairSlotBQTerm :
  IDENTIFIER (WS)? EQUAL (WS)? (csNonHeadBQTerm)
  -> ^(CstPairSlotBQTerm  ^(cst_Name IDENTIFIER) csNonHeadBQTerm)
;

// TOKENS
// XXX dummy mark system
IDENTIFIER : LETTER(LETTER | DIGIT | '_' | '-')*
  {tokenCustomizer.prepareNextToken(input.mark());};

STAR   : '*' {tokenCustomizer.prepareNextToken(input.mark());};
COMMA  : ',' ;
LPAR   : '(' ;
RPAR   : ')' {tokenCustomizer.prepareNextToken(input.mark());};
LBR    : '[' ;
RBR    : ']' {tokenCustomizer.prepareNextToken(input.mark());};
EQUAL  : '=' ;
UNDERSCORE : '_';



fragment
LETTER	: 'A'..'Z' | 'a'..'z';
fragment
DIGIT	: '0'..'9';

WS	: ('\r' | '\n' | '\t' | ' ' )*; // needs greedyness ?

SL_COMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
ML_COMMENT : '/*' ( options {greedy=false;} : . )* '*/'{ $channel=HIDDEN; } ;

// lexer need a rule for every input
// even for chars we don't use
DEFAULT : . { $channel=HIDDEN;};
