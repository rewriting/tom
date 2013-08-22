/* This grammar parses the simple prop language */
grammar PropLang;
options {
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=PropLangTokens;
}

//tokens {
//%include { proplang/PropLangTokens.tokens }
//}

@header {
  package tomchecker;
}
@lexer::header {
  package tomchecker;
}

/*----------------------------------------------------------------------------------
 * PARSER RULES
*----------------------------------------------------------------------------------*/

//top:
//	(existential)*  EOF -> ^(PropList (property)*);

top:
	(existential)*  EOF -> ^(PropList (existential)*);

existential:
	(eq='$')? prop=property ->{eq!=null}? ^(Exists $prop)
	-> $prop
;

property:
  c= cond (IMPLIES '(' p=property ')')?
	  -> {p!=null}?  ^(Implies $c $p)
    -> ^(Check $c)
;

cond:
  left=orCond (AND right=cond)?
    -> {right!=null}? ^(And $left $right?)
    -> $left
  ;

orCond:
  left=atom (OR right=orCond)?
    -> {right!=null}? ^(Or $left $right?)
    -> $left
  ;

atom:
     LPAREN cond RPAREN -> cond
   | NOT c=cond -> ^(Neg $c) 
   | comp
;

comp:
  e1=exp ( '==' e2=exp -> ^(Same $e1 $e2)
         | '!=' e2=exp -> ^(Diff $e1 $e2)
         | 'EQ' e2=exp -> ^(Eq $e1 $e2) // Existential Quantifier
         | 'NEQ' e2=exp -> ^(Neq $e1 $e2) // Not Existential Quantifier
         | '>' e2=exp -> ^(Gre $e1 $e2)
         | '>=' e2=exp -> ^(Geq $e1 $e2)
         | '<' e2=exp -> ^(Less $e1 $e2)
         | '<=' e2=exp -> ^(Leq $e1 $e2)
         )
;

exp:
    id=ID '(' args ')' -> ^(Func $id args)
  | id=ID '(' ')' -> ^(Func $id ^(Arg))
  | id1=ID ':' id2=ID -> ^(Var $id1 $id2)
  | 'True' -> ^(True)
  | 'False' -> ^(False)
  | 'Null' -> ^(Null)
  | id=INT
;


args:
  exp (',' exp)* -> ^(Arg exp*)
;

/*----------------------------------------------------------------------------------
 * LEXER RULES
*----------------------------------------------------------------------------------*/

INT : '0'..'9'+ ;	

ID : ('a'..'z'|'A'..'Z'|'\.')+;

WS : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ { $channel = HIDDEN; };

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

LPAREN : '(';
RPAREN : ')';
OR : '||' ;
AND : '&&' ;
NOT : '!' ;
IMPLIES : '=>' ;