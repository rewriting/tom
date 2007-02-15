/*
 * Copyright (c) 2004-2007, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
