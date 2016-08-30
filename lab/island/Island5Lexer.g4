lexer grammar Island5Lexer;

MATCH : '%match' ;
STRATEGY : '%strategy' ;
EXTENDS : 'extends' ;
VISIT : 'visit' ;
INCLUDE : '%include' ;
GOM : '%gom' ;
OP : '%op' ;
OPARRAY : '%oparray' ;
OPLIST : '%oplist' ;
TYPETERM : '%typeterm' ;
IS_FSYM : 'is_fsym' ;
IS_SORT : 'is_sort' ;
MAKE : 'make' ;
MAKE_EMPTY : 'make_empty' ;
MAKE_APPEND : 'make_append' ;
MAKE_INSERT : 'make_insert' ;
GET_SLOT : 'get_slot' ;
GET_DEFAULT : 'get_default' ;
GET_ELEMENT : 'get_element' ;
GET_HEAD : 'get_head' ;
GET_TAIL : 'get_tail' ;
GET_SIZE : 'get_size' ;
IS_EMPTY : 'is_empty' ;
IMPLEMENT : 'implement' ;
EQUALS : 'equals' ;


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
SLASH : '/';
BACKSLASH : '\\';

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

