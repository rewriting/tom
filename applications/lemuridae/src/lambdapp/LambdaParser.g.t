//header {
//package parsingtests;
//}

{
  import lambda.*;
  import lambda.types.*;
}


class LambdaParser extends Parser;
options {
  buildAST = true;  // uses CommonAST by default
  k = 2;
  defaultErrorHandler=false;
}

{
  %include { lambda/lambda.tom }
}


start returns [LTerm res]
{
  res=null;
}
: res = lterm END
;

lterm returns [LTerm lt]
{
  lt = null;
}
: lt=abs|lt=appl|lt=fun|lt=var
;


appl returns [LTerm appl]
{
  appl=null;
  LTerm lhs=null;
  LTerm rhs=null;
}
: LPAREN lhs=lterm rhs=lterm RPAREN 
{
  appl = `Appl(lhs,rhs);
}
;

abs returns [LTerm abs]
{
  abs=null;
  LTerm x=null;
}
: LAMBDA v:VAR DOT x=lterm
{
  abs = `Abs(v.getText(),x);
}
;

fun returns [LTerm f] 
{
  f=null;
}
: v:VAR LPAREN RPAREN
{
  f = `Fun(v.getText());
}
;

var returns [LTerm res] 
{
  res=null;
}
: v:VAR
{
  res = `Var(v.getText());
}
;


class LambdaLexer extends Lexer;

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

LAMBDA : "\\" ;
DOT: '.';
LPAREN : '(' ;
RPAREN : ')' ;
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

