grammar BQParser;

start : (island | water)*? ;

island
  : BQUOTE composite
  ;

water
  : .
  ;

bqterm
  : codomain=ID? BQUOTE? fsym=ID LPAREN (bqterm (COMMA bqterm)*)? RPAREN 
  | codomain=ID? BQUOTE? var=ID STAR?
  | constant
  ;

bqcomposite
  : BQUOTE composite
  ;
/*
composite
  : compositeappl
  | compositeparen
  | compositevar
  | constant
  | waterwithoutparen
  ;
*/

composite
  : fsym=ID LPAREN composite* RPAREN
  | LPAREN composite* RPAREN
  | var=ID STAR?
  | constant
  | waterwithoutparen
  ;
waterwithoutparen : ~(LPAREN|RPAREN)+? ;
compositeparen : LPAREN composite* RPAREN ;
compositeappl : fsym=ID LPAREN composite* RPAREN ;
compositevar : var=ID STAR? ;

compositeplus
  : composite+
  ;

constant
  : INTEGER
  | LONG
  | CHAR
  | DOUBLE
  | STRING
  ;

MATCH_SYMBOL : '<<' ;
EQUAL : '=' ;
LBRACE : '{' ;
RBRACE : '}' ;
LPAREN : '(' ;
RPAREN : ')' ;
LSQUAREBR : '[' ;
RSQUAREBR : ']' ;
ARROW : '->' ;
COMMA : ',' ;
COLON : ':' ;
STAR : '*' ;
UNDERSCORE : '_' ;
ANTI : '!' ;
AT : '@' ;
ID : ('_')? LETTER (LETTER | DIGIT | '_' | '.' )*;
INTEGER : ('-')? (DIGIT)+;
DOUBLE  : ('-')? UNSIGNED_DOUBLE;
LONG    : ('-')? (DIGIT)+ LONG_SUFFIX;
STRING  : '"'  ( ESC | ~('"' |'\\'|'\n'|'\r') )* '"';
CHAR    : '\'' ( ESC | ~('\''|'\\'|'\n'|'\r') )+ '\'';

fragment UNSIGNED_DOUBLE : (DIGIT)+'.'(DIGIT)* | '.' (DIGIT)+;
fragment LONG_SUFFIX : 'l'|'L' ; 

fragment LETTER	: 'A'..'Z' | 'a'..'z';
fragment DIGIT	: '0'..'9';
fragment HEX_DIGIT : ('0'..'9'|'A'..'F'|'a'..'f');

fragment ESC :
  '\\'
    ( 'n' | 'r' | 't' | 'b' | 'f' | '"' | '\'' | '\\'
    | ('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    | '0'..'3' ( '0'..'7' ( '0'..'7')?)?
    | '4'..'7' ( '0'..'7')?
    )
  ;

BQUOTE : '`' ;
/*
BQTERM : '`' TERM ;
fragment TERM : 
      LPAREN TERM RPAREN
    | ID (LPAREN TERM (COMMA TERM)* RPAREN)? 
    | .*?
    ;
*/
ACTION_ESCAPE :   '\\' .  ;
ACTION_STRING_LITERAL :	'"' (ACTION_ESCAPE | ~["\\])* '"' ;
MLCOMMENT : '/*' .*? '*/' ;
SLCOMMENT : '//' ~[\r\n]* ;

WS : [ \r\t\n]+ -> skip ;
ANY : . ; 
