grammar Island5;

start : (island|.)*? ;

island :
  MATCH LPAREN (ID (COMMA ID)?)? RPAREN LBRACE (trule)* RBRACE 
| INCLUDE LBRACE ID LBRACE
;
trule : pattern ARROW block ;

block : LBRACE (island | block | .)*? RBRACE ;

pattern : ID (LPAREN ( pattern (COMMA pattern)*)? RPAREN)? ;


MATCH : '%match' ;
INCLUDE : '%include' ;
LBRACE : '{' ;
RBRACE : '}' ;
LPAREN : '(' ;
RPAREN : ')' ;
ARROW : '->' ;
COMMA : ',' ;
COLON : ':' ;
ID : [a-zA-Z] [a-zA-Z0-9]* ;
INT : [0-9]+ ;
MLCOMMENT : '/*' .*? '*/' ;
SLCOMMENT : '//' ~[\r\n]* ;

WS : [ \r\t\n]+ -> skip ;
ANY : . ; 

