/* This grammar parses simple rwrules */
grammar ProgramSyntax;

// new syntax
program :
  abstractsyntax (f=functions)? (s=strategies)? (t=trs)? EOF
;

abstractsyntax :
  (ABSTRACT SYNTAX) (typedecl)*
;

functions :
  (FUNCTIONS) (typedecl)*
;

strategies :
  STRATEGIES (stratdecl)*
;

trs :
  TRS LBRACKET (rwrule (COMMA? rwrule)*) RBRACKET
| TRS (rwrule (COMMA? rwrule)*) 
;

stratdecl :
  stratname=ID paramlist EQUALS strategy
;

paramlist :
  LPAR (param (COMMA param)* )? RPAR
;

param:
  ID
;

//----------------------------
typedecl :
  typename=ID EQUALS alts=alternatives
;

alternatives :
  (ALT)? opdecl (ALT opdecl)* 
;

opdecl :
  ID fieldlist
;

fieldlist :
  LPAR (field (COMMA field)* )? RPAR 
;

field:
  type 
;

type:
  ID 
;

strategy :
  s1=elementarystrategy (
       SEMICOLON s2=strategy
     | CHOICE s3=strategy
     )?
  | LBRACE (rwrule (COMMA? rwrule)*) RBRACE 
  | LBRACKET (rwrule (COMMA? rwrule)*) RBRACKET 
;

elementarystrategy :
    IDENTITY 
  | FAIL 
  | LPAR strategy RPAR 
  | ALL LPAR strategy RPAR 
  | ONE LPAR strategy RPAR 
  | MU ID DOT LPAR strategy RPAR 
  | ID LPAR (strategy (COMMA strategy)*)? RPAR
  | ID 
;

rwrule :
  pattern ARROW term (IF cond=condition)?
| ID ARROW term (IF cond=condition)?
;

condition :
  p1=term DOUBLEEQUALS p2=term
;

pattern :
  ID LPAR (term (COMMA term)*)? RPAR 
| '!' term 
;
term :
    pattern
  | ID 
  | builtin
;

builtin :
  INT 
;

symbol :
  ID COLON INT 
;

ABSTRACT : 'abstract';
SYNTAX   : 'syntax';
STRATEGIES   : 'strategies';
FUNCTIONS : 'functions';
TRS : 'trs';


ARROW : '->' ;
AMPERCENT : '&' ;
COLON : ':' ;
LPAR : '(' ;
RPAR : ')' ;
LBRACE : '{' ;
RBRACE : '}' ;
LBRACKET : '[' ;
RBRACKET : ']' ;
COMMA : ',' ;
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
ALT : '|';
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

WS : [ \t\r\n\u000C]+ -> channel(HIDDEN);
SLCOMMENT : '//' ~[\r\n]*    -> channel(HIDDEN);
MLCOMMENT : '/*' .*? '*/'    -> channel(HIDDEN);
