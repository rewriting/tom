grammar Island3;

start : (water | island)* ;
water : SW .*? EW ;
island : '%match()' '{' (trule)* '}' ;

trule : ID '->' '{' start '}' ;

SW : '%start_water' ;
EW : '%end_water' ;

//STRING : '"' ( '\\"' | . )*? '"' ;
ID : [a-zA-Z] [a-zA-Z0-9]* ;
WS : [ \r\t\n]+ -> skip ;
ANY : . ; 


