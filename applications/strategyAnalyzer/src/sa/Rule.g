/* This grammar parses simple rules */
grammar Rule;
options {
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=RuleTokens;
}

@header {
  package sa;
}
@lexer::header {
  package sa;
}

expressionlist :
  (expression)* EOF -> ^(ExpressionList (expression)*)
  ;

expression :
    LET ID EQUALS v=expression IN t=expression -> ^(Let ID $v $t)
  | LBRACE (rule (COMA rule)*)? RBRACE -> ^(Set ^(RuleList (rule)*))
  | LBRACKET (rule (COMA rule)*)? RBRACKET -> ^(List ^(RuleList (rule)*))
  | strategy -> ^(Strat strategy)
  | SIGNATURE LBRACE (symbol (COMA symbol)*)? RBRACE -> ^(Signature ^(SymbolList (symbol)*))

  ;

strategy :
  s1=elementarystrategy (
       SEMICOLON s2=strategy
     | CHOICE s3=strategy
     )?
     -> {s2!=null}? ^(StratSequence $s1 $s2)
     -> {s3!=null}? ^(StratChoice $s1 $s3)
     -> $s1
  ;

elementarystrategy :
    IDENTITY -> ^(StratIdentity)
  | FAIL -> ^(StratFail)
  | LPAR strategy RPAR -> strategy
  | ALL LPAR strategy RPAR -> ^(StratAll strategy)
  | ONE LPAR strategy RPAR -> ^(StratOne strategy)
  | MU ID DOT LPAR strategy RPAR -> ^(StratMu ID strategy)
  | ID -> ^(StratName ID)
  ;

rule :
  pattern ARROW term (IF cond=condition)?
    -> { cond == null }? ^(Rule pattern term)
    -> ^(ConditionalRule pattern term $cond)
  | ID ARROW term (IF cond=condition)?
    -> { cond == null }? ^(Rule ^(Var ID)term)
    -> ^(ConditionalRule ^(Var ID) term $cond)
  ;
condition :
  p1=term DOUBLEEQUALS p2=term
    -> ^(CondEquals $p1 $p2)
  ;
pattern :
    ID LPAR (term (COMA term)*)? RPAR -> ^(Appl ID ^(TermList term*))
  | '!' term -> ^(Anti term)
;
term :
    pattern
  | ID -> ^(Var ID)
  | builtin
;

builtin :
  INT -> ^(BuiltinInt INT)
;

symbol :
  ID COLON INT -> ^(Symbol ID INT)
;

ARROW : '->' ;
AMPERCENT : '&' ;
COLON : ':' ;
LPAR : '(' ;
RPAR : ')' ;
LBRACE : '{' ;
RBRACE : '}' ;
LBRACKET : '[' ;
RBRACKET : ']' ;
COMA : ',' ;
SEMICOLON : ';' ;
CHOICE : '<+' ;
IDENTITY : 'identity';
FAIL : 'fail';
ALL : 'all';
ONE : 'one';
MU : 'mu';
LET : 'let';
IN : 'in';
SIGNATURE : 'signature';
EQUALS : '=';
DOUBLEEQUALS : '==';
NOTEQUALS : '!=';
DOT : '.';
LEQ : '<=';
LT : '<';
GEQ : '>=';
GT : '>';
IF : 'if' ;
INT : ('0'..'9')+ ;
ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ('*')?;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
