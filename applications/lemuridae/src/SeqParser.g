//header {
//package parsingtests;
//}

class SeqParser extends Parser;
options {
  buildAST = true;  // uses CommonAST by default
    k = 2;
}

tokens {
    VOIDLIST;
}


seq: SEQ pred DOT! 
   | list_pred SEQ pred DOT!
   ;

list_pred: pred (LIST^ pred)* ;

pred: andpred (IMPL^ andpred)* ;

andpred: orpred (AND^ orpred)* ;

orpred: quantif (OR^ quantif)* ;

quantif: FORALL^ VAR DOT! atom
       | EXISTS^ VAR DOT! atom
       | atom
       ;

atom: LPAREN! pred RPAREN!
    | appl
    | ID
//    | NOT^ atom
    ;

appl : ID LPAREN^ term_list  RPAREN! 
     ;

term_list: term (LIST^ term)* 
         |! { #term_list = #(#[VOIDLIST,"VOIDLIST"]); }
         ;

term : funappl
     | VAR
     ;

funappl : VAR LPAREN^ term_list RPAREN!;


// points d'entree pour les programmes
start1 : pred DOT! ;


class SeqLexer extends Lexer;
options {
  k=2;
}

WS
  : (' '
    | '\t'
    | '\n'
    | '\r')
    { _ttype = Token.SKIP; }
  ;

LPAREN : '(' ;

RPAREN : ')' ;

SEQ : "|-";

IMPL : "=>";

OR : "\\/" ;

AND :  "/\\" ;

LIST : ',' ;

NOT : '!'
    | '^'
    | '~' ;

DOT : '.' ;

ID : ('A'..'Z')('a'..'z')* ;

VAR: ('a'..'z')+ ;

FORALL  : "\\A";

EXISTS : "\\E";
