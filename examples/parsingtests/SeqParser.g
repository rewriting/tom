header {
package parsingtests;
}

//{{{ class SeqParser extends Parser;
class SeqParser extends Parser;
options {
  buildAST = true;  // uses CommonAST by default
  //k = 1;
}
//tokens {
//  IDENT;
//}
seq: list_pred SEQ^ list_pred END! ;

list_pred: pred (LIST^ pred)* ;

pred: andpred (IMPL^ andpred)? ;

andpred: orpred (AND^ orpred)* ;

orpred: atom (OR^ atom)* ;

atom: LPAREN! pred RPAREN!
  | NOT^ atom
  | i:ID //{#atom = #([IDENT,"IDENT"],#atom);}
  ;

///}}}

//{{{ class SeqLexer extends Lexer;
class SeqLexer extends Lexer;

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

LPAREN : '(' ;
RPAREN : ')' ;
SEQ : "|-";
IMPL : "=>"
     | "|=>" ;
OR : "or"
   | "\\/" ;
AND : "&&"
    | "/\\" ;
LIST : ':' ;
NOT : '!'
    | '^'
    | '~' ;
END : ';' ;

ID : ('A'..'Z'
   |'a'..'n'
   |'p'..'z'
   )+ ;
//}}}
