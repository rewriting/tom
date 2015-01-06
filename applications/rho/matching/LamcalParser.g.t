header {
 /*
  * Copyright (c) 2005-2015, Universite de Lorraine, Inria
  * All rights reserved.
  * 
  * Redistribution and use in source and binary forms, with or without
  * modification, are permitted provided that the following conditions are
  * met: 
  * 	- Redistributions of source code must retain the above copyright
  * 	notice, this list of conditions and the following disclaimer.  
  * 	- Redistributions in binary form must reproduce the above copyright
  * 	notice, this list of conditions and the following disclaimer in the
  * 	documentation and/or other materials provided with the distribution.
  * 	- Neither the name of the Inria nor the names of its
  * 	contributors may be used to endorse or promote products derived from
  * 	this software without specific prior written permission.
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
package matching;
}

{
  import matching.lamterm.*;
  import matching.lamterm.types.*;
	import java.util.Hashtable;
}

class LamcalParser extends Parser;
options {
  k = 1;
}
{
  %include { lamterm/Lamterm.tom }
}




matchingEquation returns [Equation eq]
{

  eq = null;
  LamTerm lhs = null;
	LamTerm rhs = null;
}
:   LBRACK lhs=lamterm MATCH rhs=lamterm RBRACK
{
	eq = `match(lhs,rhs);
}
;

lamterm returns [LamTerm term]
{
  term = `localVar("lamterm");
}
: term=functionalapp
;


functionalapp returns [LamTerm appl]
{
  appl=`localVar("functionalapp");
  LamTerm lhs=null;
  LamTerm rhs=null;
	LamTerm x=null;
}
: lhs=abs {rhs=lhs;} (APPL x=abs {rhs=`app(rhs,x);})*
{
  if(x!=null) {
		appl=rhs;
  } else {
    appl=lhs;
  }
}
;

abs returns [LamTerm appl]
{
  appl=null;
  LamTerm lhs=null;
  LamTerm rhs=null;
}
: lhs=atom (ABS rhs=atom)?
{
  if(rhs!=null) {
    appl=`abs(lhs,rhs);
  } else {
    appl=lhs;
  }
}
;

atom returns [LamTerm atom]
{
  atom=null;
}
: atom=constant | atom=matchVariable |atom=localVariable |LPAREN atom=lamterm RPAREN
//: atom=constant | atom=variable |LPAREN atom=lamterm RPAREN
;

constant returns [LamTerm constant]
{
  constant=null;
  //String constname="";
}
: constname:CONST 
{
    constant=`constant(constname.getText());
}
;

matchVariable returns [LamTerm var]
{
  var=null;
  //String varname="";
}
: varname:MATCHVAR {var=`matchVar(varname.getText());}
;

localVariable returns [LamTerm var]
{
  var=null;
  //String varname="";
}
: varname:LOCALVAR {var=`localVar(varname.getText());}
;

class LamcalLexer extends Lexer;

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

ABS : "->" ;
APPL : "." ;
LPAREN : '(' ;
RPAREN : ')' ;
LBRACK : "[" ;
RBRACK : "]" ;
MATCH : "<<" ;
END : ';' ;

CONST options{ testLiterals = true; }
: 'a'..'w'
('A'..'Z'
 |'a'..'z'
 |'0'..'9'
 )*
;

LOCALVAR options{ testLiterals = true; }
: 'x'..'z'
('A'..'Z'
 |'a'..'z'
 |'0'..'9'
 )*
;
MATCHVAR options{ testLiterals = true; }
: 'A'..'Z'
('A'..'Z'
 |'a'..'z'
 |'0'..'9'
 )*
;

SL_COMMENT
: "//" (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
{
  $setType(Token.SKIP);
  newline();
}
;

ML_COMMENT
: "/*"
(
 options {
 generateAmbigWarnings=false;
 }
 :
 { LA(2)!='/' }? '*'
 |       '\r' '\n' {newline();}
 |       '\r'      {newline();}
 |       '\n'      {newline();}
 |       ~('*'|'\n'|'\r')
 )*
"*/"
{$setType(Token.SKIP);}
;
