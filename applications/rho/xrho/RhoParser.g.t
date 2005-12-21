header {
 /*
  * Copyright (c) 2005-2006, INRIA
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
package xrho;
}

{
  import aterm.*;
  import aterm.pure.SingletonFactory;
  import xrho.rhoterm.*;
  import xrho.rhoterm.types.*;
}

class RhoParser extends Parser;
options {
  k = 1;
}
{
  %include { rhoterm/Rhoterm.tom }
  private RhotermFactory factory = RhotermFactory.getInstance(SingletonFactory.getInstance());
  public RhotermFactory getRhotermFactory() {
    return factory;
  }
}

program returns [RTerm term]
{
  term = null;
}
: term=rhoterm END
;

rhoterm returns [RTerm term]
{
  term = `var("rhoterm");
}
: term=substappl
;

substappl returns [RTerm appl]
{
  appl=`var("substappl");
  ListSubst substitutions=`andS();
  RTerm term=null;
}
: (LSUBST substitutions=substitutionlist RSUBST)? term=constappl
{
  if(substitutions!=`andS()) {
    appl = `appS(substitutions,term);
  } else {
    appl=term;
  }
}
;

substitutionlist returns [ListSubst list]
{
  list=`andS();
  Subst subst=null;
  ListSubst sublist=`andS();
}
: subst=substitution (AND sublist=substitutionlist)?
{ list=`andS(subst,sublist*); }
;

substitution returns [Subst subst]
{
  subst = null;
  RTerm thevar = null;
  RTerm term = null;
}
: thevar=variable EQ term=rhoterm
{ subst = `eq(thevar,term); }
;

constappl returns [RTerm appl]
{
  appl=null;
  ListConstraint constraints=`andC();
  RTerm term=null;
}
: (LBRACK constraints=constraintlist RBRACK)? term=functionalapp
{
  if(constraints!=`andC()) {
    appl = `appC(constraints,term);
  } else {
    appl=term;
  }
}
;

constraintlist returns [ListConstraint list]
{
  list = `andC();
  Constraint constraint = null;
  ListConstraint sublist = `andC();
}
: constraint=constraint (AND sublist=constraintlist)?
{ list=`andC(constraint,sublist*); }
;

constraint returns [Constraint constraint]
{
  constraint = null;
  RTerm lhs = null;
  RTerm rhs = null;
}
: lhs=rhoterm MATCH rhs=rhoterm
{ constraint = `match(lhs,rhs); }
;

functionalapp returns [RTerm appl]
{
  appl=`var("functionalapp");
  RTerm lhs=null;
  RTerm rhs=null;
}
: lhs=arrow (APPL rhs=arrow)?
{
  if(rhs!=null) {
    appl=`app(lhs,rhs);
  } else {
    appl=lhs;
  }
}
;

arrow returns [RTerm appl]
{
  appl=null;
  RTerm lhs=null;
  RTerm rhs=null;
}
: lhs=structure (ARROW rhs=structure)?
{
  if(rhs!=null) {
    appl=`abs(lhs,rhs);
  } else {
    appl=lhs;
  }
}
;

structure returns [RTerm appl]
{
  appl=null;
  RTerm lhs=null;
  RTerm rhs=null;
}
: lhs=atom (STRUCT rhs=atom)?
{ 
  if (rhs!=null) {
    appl=`struct(lhs,rhs);
  } else {
    appl=lhs;
  }
}
;

atom returns [RTerm atom]
{
  atom=null;
}
: atom=constant | atom=variable | LPAREN atom=rhoterm RPAREN
;


constant returns [RTerm constant]
{
  constant=null;
  //String constname="";
}
: constname:CONST 
{
  if ("stk".equals(constname.getText())) {
    constant = `stk();
  }
  else {
    constant=`const(constname.getText());
  }
}
;

variable returns [RTerm var]
{
  var=null;
  //String varname="";
}
: varname:VAR {var=`var(varname.getText());}
;

class RhoLexer extends Lexer;

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

ARROW : "->" ;
APPL : "." ;
LPAREN : '(' ;
RPAREN : ')' ;
LBRACK : '[' ;
RBRACK : ']' ;
LSUBST : '{' ;
RSUBST : '}' ;
STRUCT : "|" ;
MATCH : "<<" ;
AND : '^' ;
EQ : '=' ;
END : ';' ;

CONST options{ testLiterals = true; }
: 'a'..'z'
('A'..'Z'
 |'a'..'z'
 |'0'..'9'
 )*
;

VAR options{ testLiterals = true; }
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
