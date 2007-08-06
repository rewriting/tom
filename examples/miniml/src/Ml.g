
grammar Ml;

options {
    output=AST;
    ASTLabelType=CommonTree; // type of $stat.tree ref etc...
    //backtrack=true;
}

tokens { CONSTR; FAPPL; VALUELIST; RULELIST; PROG; AFFECTLIST; ABS; PATTERNLIST; IDLIST; RARROW;}

prog : LET ID COLUMN type AFFECT value END -> ^(PROG ID type value);

value : 
      | ID
      | ID LPAREN value_list RPAREN -> ^(CONSTR ID value_list)
      | ID LPAREN RPAREN -> ^(CONSTR ID)
      | matchexpr
      | funexpr
      | LET^ affect IN! value
      | LPAREN! value RPAREN!
      | LBRACKET v1=value COMMA v2=value RBRACKET -> ^(FAPPL $v1 $v2)
      ;

affect : ID COLUMN! type AFFECT^ value;

type : type2 (ARROW type)* -> ^(ABS type2) ^(ABS type)* ; 
type2 : ID 
      | LPAREN! type RPAREN!
      ; 

value_list : value (COMMA value)* -> ^(VALUELIST value)+; 

matchexpr : MATCH^ ID WITH! conj;

pattern : ID LPAREN pattern_list RPAREN -> ^(CONSTR ID pattern_list)
        | ID LPAREN RPAREN -> ^(CONSTR ID)
        | ID
        ;

pattern_list : pattern (COMMA pattern)* -> ^(PATTERNLIST pattern)+; 

conj : (PIPE rewrite)+ -> ^(RULELIST rewrite)+
     | rewrite (PIPE rewrite)* -> ^(RULELIST rewrite)+
     ;

rewrite : pattern ARROW value -> ^(RARROW pattern value);

funexpr : FUNCTION^ ID ARROW! value;


MATCH : 'match';
WITH: 'with';
LET : 'let';
IN : 'in';
PIPE : '|';
END : ';;';
ARROW : '->';
AFFECT : '=';
FUNCTION : 'function';
LPAREN : '(' ;
RPAREN : ')' ;
LBRACKET : '[';
RBRACKET : ']';
COMMA : ',';
COLUMN : ':';


ID : ('_'|'A'..'Z'|'a'..'z')('_'|'A'..'Z'|'a'..'z'|'0'..'9')*;
NUMBER: ('0'..'9')+;

WS  :  (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
;

COMMENT
:   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
;

LINE_COMMENT
: '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
;



