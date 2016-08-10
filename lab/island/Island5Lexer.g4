lexer grammar Island5Lexer;

MATCH : '%match' ;
MATCH_SYMBOL : '<<' ;
EQUAL : '=' ;
INCLUDE : '%include' ;
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

GREATEROREQ : '>=';
LOWEROREQ : '<=';
GREATERTHAN : '>';
LOWERTHAN : '<';
DOUBLEEQ : '==';
DIFFERENT : '!=';
AND : '&&';
OR : '||';
PIPE : '|';
QMARK : '?';
DQMARK : '??';

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
BQTERM : '`' TERM ;
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
