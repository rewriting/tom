/* This grammar parses simple rules */
grammar Rule;
options {
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=RuleTokens;
}

@header {
  package parser;
}
@lexer::header {
  package parser;
}
ruleset :
  (rule)* EOF -> ^(RuleList (rule)*)
  ;
rule :
  pattern ARROW term (IF cond=condition)?
    -> { cond == null }? ^(Rule pattern term)
    -> ^(ConditionalRule pattern term $cond)
  ;
condition :
  p1=term (EQUALS p2=term
		  | NOTEQUALS p3=term
		  | LEQ p4=term
		  | LT p5=term
		  | GEQ p6=term
		  | GT p7=term
		  | DOT ID LPAR p8=term RPAR
	  )?
    -> {p2!=null}? ^(CondEquals $p1 $p2)
    -> {p3!=null}? ^(CondNotEquals $p1 $p3)
    -> {p4!=null}? ^(CondLessEquals $p1 $p4)
    -> {p5!=null}? ^(CondLessThan $p1 $p5)
    -> {p6!=null}? ^(CondGreaterEquals $p1 $p6)
    -> {p7!=null}? ^(CondGreaterThan $p1 $p7)
    -> {p8!=null}? ^(CondMethod $p1 ID $p8)
    -> ^(CondTerm $p1)
  ;
pattern :
  ID LPAR (term (COMA term)*)? RPAR -> ^(Appl ID ^(TermList term*))
;
term :
  pattern
  | ID -> ^(Var ID)
  | builtin
;
builtin :
  INT -> ^(BuiltinInt INT)
| STRING -> ^(BuiltinString STRING)
;

ARROW : '->' ;
AMPERCENT : '&' ;
COLON : ':' ;
LPAR : '(' ;
RPAR : ')' ;
COMA : ',' ;
EQUALS : '==';
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
