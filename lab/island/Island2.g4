grammar Island2;
start : clazz* ;

clazz : 'class' ID classBody 'endclass' ; 

classBody : (method|clazz|.)+?; 

method : 'method' ID block ;

block : '{' (block | .)*? '}' ;

//STRING : '"' ( '\\"' | . )*? '"' ;

ID : [a-zA-Z] [a-zA-Z0-9]* ;
WS : [ \r\t\n]+ -> skip ;
ANY : . ; 


