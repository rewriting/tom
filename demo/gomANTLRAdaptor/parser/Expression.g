/* A grammar for expressions */
grammar Expression;
options {
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=ExpressionExpressionTokens;
}

@header {
  package parser;
}
@lexer::header {
  package parser;
}

ruleBase  : (statement SEMI)* EOF
    -> ^(StatementList (statement)*)
;

statement : lhs EQUALS rhs
    -> ^(Equal lhs rhs)
;

lhs : idP=ID
    -> ^(Id $idP)
;

rhs : (
       num=NUM
       | ex=expr
      )
     -> {num!=null}? ^(Num $num)
     -> $ex
;

expr  : op1=expr2 mo=MATHOP op2=expr
        -> {$mo.text.equals("/")}? ^(Div $op1 $op2)
        -> {$mo.text.equals("*")}? ^(Mult $op1 $op2)
        -> {$mo.text.equals("+")}? ^(Plus $op1 $op2)
        -> ^(Minus $op1 $op2)
        | expr2 -> expr2
;

expr2 :
  op1=ID -> ^(Id $op1)
  | op1=NUM -> ^(Num $op1)
;


ID : ('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;

NUM : ('0'..'9')+ ;

SEMI  : ';' ;

EQUALS  : '=' ;

MATHOP  : '/'
        | '*'
        | '+'
        | '-'
;

WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; }
;
