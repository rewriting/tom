header {
package propp;

import propp.seq.*;
import propp.seq.types.*;
}

//{{{ class SeqParser extends Parser;
class SeqParser extends Parser;
options {
  buildAST = true;  // uses CommonAST by default
  k = 1;
}
{
  %include { seq/Seq.tom }
}

seq returns [Sequent s]
{ 
  s = null;
  ListPred left = null;
  ListPred right = null;
}
: left=list_pred SEQ^ right=list_pred END!
{ s = `seq(left,right); }
;

list_pred returns [ListPred l]
{ 
  l = `concPred();
  Pred head = null;
  Pred tl = null;
}
: head=pred {l = `concPred(head); } (LIST^ tl=pred { l=`concPred(l*,tl); })*
;

pred returns [Pred p]
{ 
  p = null;
  Pred left = null;
  Pred right = null;
}
: left=andpred (IMPL^ right=andpred)?
{ 
  if (right!=null) {
    p = `impl(left,right);
  } else {
    p = left;
  }
}
;

andpred returns [Pred p]
{ 
  p = null;
  Pred left = null;
  Pred right = null;
}
: left=orpred { p = left; } 
(AND^ right=orpred { p = `wedge(p,right); })*
;

orpred returns [Pred p]
{ 
  p = null;
  Pred left = null;
  Pred right = null;
}
: left=atom { p = left; }
(OR^ right=atom { p = `wedge(p,right); })*
;

atom returns [Pred p]
{ 
  p = null;
  Pred pred = null;
}
: LPAREN! pred=pred RPAREN! { p = pred; }
  | NOT^ pred=atom { p = `neg(pred); }
  | i:ID { p = Pred.fromTerm(
      aterm.pure.SingletonFactory.getInstance().parse(
        (String)String.valueOf(i.getText()))); }
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
