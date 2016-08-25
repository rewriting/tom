grammar Island4;

start : (island|.)*? ;

island : MATCH LBRACE (trule)* RBRACE ;
trule : ID ARROW block ;
block : LBRACE (island|.)*? RBRACE ;

MATCH : '%match()' ;
LBRACE : '{' ;
RBRACE : '}' ;
ARROW : '->' ;
ID : [a-zA-Z] [a-zA-Z0-9]* ;
WS : [ \r\t\n]+ -> skip ;
ANY : . ; 


