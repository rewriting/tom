header {
package propp;
}

//{{{ class SeqParser extends Parser;
class SeqParser extends Parser;
options {
  buildAST = true;  // uses CommonAST by default
  k = 1;
}

seq: list_pred SEQ^ list_pred END!
  ;

list_pred: pred (LIST^ pred)*
  ;

pred: andpred (IMPL^ andpred)?
  ;

andpred: orpred (AND^ orpred)*
  ;

orpred: atom (OR^ atom)*
  ;

atom: LPAREN! pred RPAREN!
  | NOT^ atom
  | ID
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

LPAREN  
  : '(' ;
RPAREN
  : ')' ;
SEQ     
  : "|-";
IMPL    
  : "=>"
  | "|=>"
  ;
OR
  : "or"
  | "\\/";
AND
  : "&&"
  | "/\\";
LIST
  : ':' ;
NOT 
  : '!' 
  | '^'
  | '~'
  ;
END
  : ';'
  ;

ID
  : ('A'..'Z'
    |'a'..'n'
    |'p'..'z'
    )+
  ;
//}}}   

//{{{ class SeqTreeWalker extends TreeParser;
class SeqTreeWalker extends TreeParser;

seq returns [String r]
{
  String a,b;
  r="";
}
  : #(SEQ a=list_pred b=list_pred) 
    {
      r = "seq("+a+","+b+")";
    }
  ;

list_pred returns [String r]
{
  String a,b;
  r="";
}
  : #(LIST a=pred b=pred) 
    {
      r = "["+a+","+b+"]";
    }
  | #(IMPL a=pred b=pred) 
    {
      r = "[impl("+a+","+b+")]";
    }
  | #(OR a=pred b=pred) 
    {
      r = "[vee("+a+","+b+")]";
    }
  | #(AND a=pred b=pred) 
    {
      r = "[wedge("+a+","+b+")]";
    }
  | #(NOT a=pred) 
    {
      r = "[neg("+a+")]";
    }
  | i:ID     
    {
      r = "["+(String)String.valueOf(i.getText())+"]";
    }
  ;

pred returns [String r]
{
  String a,b;
  r="";
}
  : #(LIST a=pred b=pred) {r = a+","+b;}
  | #(IMPL a=pred b=pred) {r = "impl("+a+","+b+")";}
  | #(OR a=pred b=pred) {r = "vee("+a+","+b+")";}
  | #(AND a=pred b=pred) {r = "wedge("+a+","+b+")";}
  | #(NOT a=pred) {r = "neg("+a+")";}
  | i:ID     {r = (String)String.valueOf(i.getText());}
  ;
//}}}
