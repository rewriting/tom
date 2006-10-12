header {
package bpel;
}

class CondParser extends Parser;
options {
  buildAST = true;  
}

cond: andcond (OR^ andcond)* ;

andcond: atom (AND^ atom)* ;

atom: i:ID
    | LPAR cond RPAR
    ;

class CondLexer extends Lexer;

options {
  k=4;
}

WS
  : (' '
    | '\t'
    | '\n'
    | '\r')
    { _ttype = Token.SKIP; }
  ;

LPAR : '(' ;
RPAR : ')' ;
OR : "OR" ;
AND : "AND" ;
ID : ('A'..'Z' |'a'..'z' |'1'..'9')+ ;

