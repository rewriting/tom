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
  CONDNOTEQUALS;
  CONDLEQ;
  CONDLT;
  CONDGEQ;
  CONDGT;
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
  pattern ARROW term (IF cond=condition)?
    -> { cond == null }? ^(RULE pattern term)
    -> ^(CONDRULE pattern term $cond)
  ;
condition :
  p1=term (EQUALS p2=term | NOTEQUALS p3=term |
           LEQ p4=term | LT p5=term | 
           GEQ p6=term | GT p7=term)?
    -> {p2!=null}? ^(CONDEQUALS $p1 $p2)
    -> {p3!=null}? ^(CONDNOTEQUALS $p1 $p3)
    -> {p4!=null}? ^(CONDLEQ $p1 $p4)
    -> {p5!=null}? ^(CONDLT $p1 $p5)
    -> {p6!=null}? ^(CONDGEQ $p1 $p6)
    -> {p7!=null}? ^(CONDGT $p1 $p7)
    -> ^(CONDTERM $p1)
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
NOTEQUALS : '!=';
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
