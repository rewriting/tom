lexer grammar PPIslandLexer;

MATCH : '%match' ;
MATCH_SYMBOL : '<<' ;
INCLUDE : '%include' ;
LBRACE : '{' ;
RBRACE : '}' ;
LPAREN : '(' ;
RPAREN : ')' ;
ARROW : '->' ;
COMMA : ',' ;
COLON : ':' ;

GREATEROREQ : '>=';
LOWEROREQ : '<=';
GREATERTHAN : '>';
LOWERTHAN : '<';
DOUBLEEQ : '==';
DIFFERENT : '!=';
AND : '&&';
OR : '||';

ID : [a-zA-Z] [a-zA-Z0-9]* ;
INT	: [0-9]+ ;

BQ : '`' TERM ;
fragment TERM : 
      LPAREN TERM RPAREN
    | ID (LPAREN TERM (COMMA TERM)* RPAREN)? 
    | .*?
    ;


ACTION_ESCAPE :   '\\' .  ;
ACTION_STRING_LITERAL :	'"' (ACTION_ESCAPE | ~["\\])* '"' ;
MLCOMMENT : '/*' .*? '*/' ;
SLCOMMENT : '//' ~[\r\n]* ;

WS : [ \r\t\n]+ -> skip ;
ANY : . ; 
