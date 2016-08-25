grammar Simplang;

start : statements EOF;
statements      : statement+;
statement       : block #BlockStatement 
                | call  #CallStatement
                | decl  #DeclStatement
                ;
block           : LCUR statements RCUR;    
call            : methodName LPAR args=arglist? RPAR SEMI;
methodName      : ID;
arglist         : arg (COMMA arg)*;
arg             : expr;    
decl            : VAR variableName EQ expr SEMI;
variableName    : ID;
expr            : add_expr;     

add_expr        : lhs=primary_expr (add_op rhs=primary_expr)*;
add_op          : PLUS | MINUS;    
primary_expr    : string=STRING
                | id=ID
                | integer=INT
                ;    

VAR: 'var';   
ID: ('a'..'z'|'A'..'Z')+;
INT: ('0'..'9')+;
STRING: '\'' ~('\r'|'\n'|'\'')* '\'';
SEMI: ';';
LPAR: '(';
RPAR: ')';
LCUR: '{';
RCUR: '}';
PLUS: '+';
MINUS: '-';    
COMMA: ',';
EQ: '=';
WS: (' '|'\t'|'\f'|'\r'|'\n') -> skip;

