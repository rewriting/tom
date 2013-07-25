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

top:
// top start rule has been replaced with input
//input :
	(property)*  EOF -> ^(PropList (property)*);

// property:
//   c=cond (  '=>' '(' p=property ')' -> ^(Implies $c $p)
//            |                        -> ^(Check $c) )
// ;

property:
  c=cond (IMPLIES '(' p=property ')')?
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
         | 'EQ' e2=exp -> ^(Eq $e1 $e2)
         | 'NEQ' e2=exp -> ^(Neq $e1 $e2)
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

// Deprecated method for declaring INT
//INT : ('0'..'9')('0'..'9')*;
INT : '0'..'9'+ ;	

// Deprecated method for declaring ID
ID : ('a'..'z'|'A'..'Z'|'\.')+;
//ID : ('a'..'z'|'A'..'Z'|'_'|'\.') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'\.')*;

// WS stands for White Space
// Deprecated method for declaring WS
//WS : (' ' | '\t' | ( '\r\n' | '\n' | '\r')) {$channel=HIDDEN;} ; 

WS : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ { $channel = HIDDEN; };

// Deprecated method for comments
//SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?  {$channel=HIDDEN;} ;
//MLCOMMENT : '/*' .* '*/' {$channel=HIDDEN;} ;

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
