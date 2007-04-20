/* This grammar parses simple rules */
grammar Rule;
options {
  output=AST;
  ASTLabelType=ASTTree;
}

tokens {
  RULELIST;
  RULE;
  CONDRULE;
  APPL;
  CONDTERM;
  CONDEQUALS;
}

@header {
  package tom.gom.expander.rule;
}
@lexer::header {
  package tom.gom.expander.rule;
}
ruleset :
  (rule)* EOF -> ^(RULELIST (rule)*)
  ;
rule :
  pattern ARROW rhs=term (IF cond=condition)?
    -> { cond == null }? ^(RULE pattern $rhs)
    -> ^(CONDRULE pattern $rhs $cond)
  ;
condition :
  p1=term (EQUALS p2=term)?
    -> {p2 == null}? ^(CONDTERM $p1)
    -> ^(CONDEQUALS $p1 $p2)
  ;

pattern :
  ID LPAR (term (COMA term)*)? RPAR -> ^(APPL ID term*)
  ;
term :
  pattern | ID | builtin
  ;
builtin :
  INT | STRING
  ;

ARROW : '->' ;
LPAR : '(' ;
RPAR : ')' ;
COMA : ',' ;
EQUALS : '==';
IF : 'if' ;
INT : ('0'..'9')+ ;
ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;
