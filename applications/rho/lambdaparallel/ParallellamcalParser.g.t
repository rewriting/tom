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
	package lambdaparallel;
}

{
  import aterm.*;
  import aterm.pure.SingletonFactory;
  import lambdaparallel.parallellamterm.*;
  import lambdaparallel.parallellamterm.types.*;
	import java.util.Hashtable;
}

class ParallellamcalParser extends Parser;
options {
  k = 1;
}
{
  %include { parallellamterm/Parallellamterm.tom }
//   private ParallellamtermFactory factory = ParallellamtermFactory.getInstance(SingletonFactory.getInstance());
//   public ParallellamtermFactory getParallelllamtermFactory() {
//     return factory;
//   }
}

parallellamterm returns [Parallellamterm term]
{
  term = `var("parallellamterm");
}
: term=functionalapp END
;


functionalapp returns [Parallellamterm appl]
{
  appl=`var("functionalapp");
  Parallellamterm lhs=null;
  Parallellamterm rhs=null;
	Parallellamterm x=null;
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

abs returns [Parallellamterm appl]
{
  appl=null;
  Parallellamterm lhs=null;
  Parallellamterm rhs=null;
}
: lhs=structure (ABS rhs=structure)?
{
  if(rhs!=null) {
    appl=`abs(lhs,rhs);
  } else {
    appl=lhs;
  }
}
;
structure returns [Parallellamterm appl]
{
  appl=`var("structure");
  Parallellamterm lhs=null;
  Parallellamterm rhs=null;
	Parallellamterm x=null;
}
: lhs=atom {rhs=`parallel(lhs);} (STRUCT x=atom {%match(Parallellamterm rhs){ parallel(X*) -> {rhs=`parallel(X,x);}}})*
{
  if(x!=null) {
		appl=rhs;
  } else {
    appl=lhs;
  }
}
;

atom returns [Parallellamterm atom]
{
  atom=null;
}
: atom=variable |LPAREN atom=parallellamterm RPAREN
//: atom=constant | atom=variable |LPAREN atom=lamterm RPAREN
;




variable returns [Parallellamterm var]
{
  var=null;
}
: varname:VAR {var=`var(varname.getText());}
;

class ParallelllamcalLexer extends Lexer;

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
STRUCT : "|" ;
MATCH : "<<" ;
END : ';' ;

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
