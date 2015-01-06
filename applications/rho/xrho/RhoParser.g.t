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
	*
	* by Germain Faure
  */
package xrho;
}

{
  import aterm.*;
  import aterm.pure.SingletonFactory;
  import xrho.rhoterm.*;
  import xrho.rhoterm.types.*;
	import java.util.Hashtable;
}

class RhoParser extends Parser;
options {
  k = 1;
}
{
	Hashtable table = new	Hashtable();
  %include { rhoterm/Rhoterm.tom }
//IL FAUT LA TRANSFORMER POUR QU'ELLE NE PRENNE QU'UN SEUL ARG
 public  RTerm fromAlgebraicApplToFunctionalAppl(ListRTerm listArg, RTerm head){
  	%match(ListRTerm listArg){
 		and(x,XS*) -> {
			return `fromAlgebraicApplToFunctionalAppl(XS,app(head,x));
 		}
 		_ -> {return head;}
		}
    return head;
 }
//TO BE COMPLETE
//Le nom que je donne commence par un "!"
 protected RTerm addName(String name,RTerm term){
	 if (!table.containsKey(name)){
		 table.put(name,term);
		 return term;
	 }
	 else {
		 System.out.println("Warning, the alias " + name + " was used before. Redefinition is impossible.");
		 return (RTerm)table.get(name);

	 }
 }
 protected RTerm getName(String name){
	 if (!table.containsKey(name)){
		 System.out.println("Error: alias undefined");
	 }
	 return (RTerm)table.get(name);
 }

}



program returns [RTerm term]
{
  term = null;
}
:  term=assign END | term=rhoterm END
;


rhoterm returns [RTerm term]
{
  term = `var("rhoterm");
}
: term=substappl
;

assign  returns [RTerm term]
{
	term=null;
	RTerm rhs = null;
}
: name:DEF (ASSIGN rhs=substappl)?
{
	if (rhs != null){
		term=addName(name.getText(),rhs);
	}
	else {
		term=getName(name.getText());
	}
};


substappl returns [RTerm appl]
{
  appl=`var("substappl");
  ListSubst substitutions=`andS();
  RTerm term=null;
}
: (LBRACK substitutions=substitutionlist RBRACK)? term=constappl
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
	RTerm x=null;
}
: lhs=arrow {rhs=lhs;} (APPL x=arrow {rhs=`app(rhs,x);})*
{
  if(x!=null) {
		appl=rhs;
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
  appl=`var("functionalapp");
  RTerm lhs=null;
  RTerm rhs=null;
	RTerm x=null;
}
: lhs=atom {rhs=lhs;} (STRUCT x=atom {rhs=`struct(rhs,x);})*
{
  if(x!=null) {
		appl=rhs;
  } else {
    appl=lhs;
  }
}
;
atom returns [RTerm atom]
{
  atom=null;
}
: atom=constant | atom=variable | atom=def | atom=chgeMode | LPAREN atom=rhoterm RPAREN
;

constant returns [RTerm constant]
{
  constant=null;
}
: constname:CONST 
{
  if ("stk".equals(constname.getText())) {
    constant = `stk();
  }
  else {
    constant=`Const(constname.getText());
  }
}
;

variable returns [RTerm var]
{
  var=null;
}
: varname:VAR {var=`var(varname.getText());}
;
chgeMode returns [RTerm mode]
{
  mode=null;
}
: RESULT {mode=`result();} | STEPS {mode=`steps();} | STRONG {mode=`strong();} | WEAK {mode=`weak();} | EXPLICIT {mode=`explicit();} |  PLAIN {mode=`plain();}

;

def returns [RTerm def]
{
  def=null;
}
: varname:DEF {def=getName(varname.getText());}
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
LALG : '{' ;
RALG : '}' ;
STRUCT : "|" ;
MATCH : "<<" ;
AND : '^' ;
EQ : '=' ;
AT : '@' ;
COMMA : ',' ;
RESULT : "NF" ;
STEPS : "DETAILS" ;
STRONG : "STRONG" ;
WEAK : "WEAK" ;
EXPLICIT : "EXPLICIT" ;
PLAIN : "PLAIN" ;
END : ';' ;
DEF options{ testLiterals = true; }: '!' 
('A'..'Z'
 |'a'..'z'
 |'0'..'9'
 )+
;
ASSIGN : ":=" ;

CONST options{ testLiterals = true; }
: 'a'..'z'
('A'..'Z'
 |'a'..'z'
 |'0'..'9'
 )* 
| ('0'..'9')+
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
