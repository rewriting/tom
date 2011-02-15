header {
 /*
  * Copyright (c) 2005-2011, INPL, INRIA
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
  * 	- Neither the name of the INRIA nor the names of its
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
	package termgraph;
}

{
  import aterm.*;
  import aterm.pure.SingletonFactory;
  import termgraph.lambdaterm.*;
  import termgraph.lambdaterm.types.*;
	import java.util.Hashtable;
}

class LambdaTermParser extends Parser;
options {
  k = 1;
}
{
  %include { lambdaterm/lambdaterm.tom }
}

lambdaterm returns [LambdaTerm term]
{
  term = `var("lambdaterm");
}
: term=functionalapp END
;


functionalapp returns [LambdaTerm appl]
{
  appl=`var("functionalapp");
  LambdaTerm lhs=null;
  LambdaTerm rhs=null;
	LambdaTerm x=null;
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

abs returns [LambdaTerm appl]
{
  appl=null;
  LambdaTerm lhs=null;
  LambdaTerm rhs=null;
}
: (ABS lhs=variable)? rhs=atom
{
  if(lhs!=null) {
    appl=`abs(lhs,rhs);
  } else {
    appl=rhs;
  }
}
;


atom returns [LambdaTerm atom]
{
  atom=null;
}
: atom=variable |LPAREN atom=functionalapp RPAREN
;

variable returns [LambdaTerm var]
{
  var=null;
}
: varname:VAR {var=`var(varname.getText());}
;

class LambdaTermLexer extends Lexer;

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

ABS : "\\" ;
APPL : "." ;
LPAREN : "(" ;
RPAREN : ")" ;
LBRACK : "[" ;
RBRACK : "]" ;
MATCH : "<<" ;
END : ";" ;

VAR options{ testLiterals = true; }
: ('A'..'Z'
 |'a'..'z'
 |'0'..'9'
 )+
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
