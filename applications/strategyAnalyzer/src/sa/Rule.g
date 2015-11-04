/* This grammar parses simple rules */
grammar Rule;
options {
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=RuleTokens;
}

@header {
  package sa;
}
@lexer::header {
  package sa;
}

// new syntax
program :
  abstractsyntax (f=functions)? strategies EOF
  -> {f!=null}? ^(Program abstractsyntax $f strategies)
  -> ^(Program abstractsyntax ^(ConcProduction) strategies)
;

abstractsyntax :
(ABSTRACT SYNTAX) (typedecl)*
  -> ^(ConcProduction (typedecl)*) 
;

functions :
(FUNCTIONS) (typedecl)*
  -> ^(ConcProduction (typedecl)*) 
;

strategies :
STRATEGIES (stratdecl)*
  -> ^(ConcStratDecl (stratdecl)*) 
;

stratdecl :
    stratname=ID paramlist  EQUALS stratbody
      -> ^(StratDecl $stratname paramlist stratbody)
  ;

paramlist :
  LPAR (param (COMMA param)* )? RPAR
 -> ^(ConcParam (param)*) 
;

param:
  ID
 -> ^(Param ID)
  ;

stratbody :
  | LBRACE (rule (COMMA rule)*)? RBRACE -> ^(Set ^(ConcRule (rule)*))
  | LBRACKET (rule (COMMA rule)*)? RBRACKET -> ^(List ^(ConcRule (rule)*))
  | strategy -> ^(Strat strategy)
  ;

//----------------------------
typedecl :
    typename=ID EQUALS alts=alternatives[typename]
      -> ^(SortType ^(GomType $typename) $alts)
  ;

alternatives[Token typename] :
  (ALT)? opdecl[typename] (ALT opdecl[typename])* 
  -> ^(ConcAlternative (opdecl)+)
  ;

opdecl[Token type] :
 ID fieldlist
  -> ^(Alternative ID fieldlist ^(GomType ID[type])
      )
  ;

fieldlist :
  LPAR (field (COMMA field)* )? RPAR -> ^(ConcField (field)*) 
;

field:
  type -> ^(UnamedField type)
  ;

type:
  ID -> ^(GomType ID)
  ;


strategy :
  s1=elementarystrategy (
       SEMICOLON s2=strategy
     | CHOICE s3=strategy
     )?
     -> {s2!=null}? ^(StratSequence $s1 $s2)
     -> {s3!=null}? ^(StratChoice $s1 $s3)
     -> $s1
  ;

elementarystrategy options { backtrack = true; }:
    IDENTITY -> ^(StratIdentity)
  | FAIL -> ^(StratFail)
  | LPAR strategy RPAR -> strategy
  | ALL LPAR strategy RPAR -> ^(StratAll strategy)
  | ONE LPAR strategy RPAR -> ^(StratOne strategy)
  | MU ID DOT LPAR strategy RPAR -> ^(StratMu ID strategy)
  | ID LPAR (strategy (COMMA strategy)*)? RPAR
       -> ^(StratAppl ID ^(ConcStrat (strategy)*))
  | ID -> ^(StratName ID)
  ;

rule :
  pattern ARROW term (IF cond=condition)?
    -> { cond == null }? ^(Rule pattern term)
    -> ^(ConditionalRule pattern term $cond)
  | ID ARROW term (IF cond=condition)?
    -> { cond == null }? ^(Rule ^(Var ID)term)
    -> ^(ConditionalRule ^(Var ID) term $cond)
  ;
condition :
  p1=term DOUBLEEQUALS p2=term
    -> ^(CondEquals $p1 $p2)
  ;
pattern :
    ID LPAR (term (COMMA term)*)? RPAR -> ^(Appl ID ^(TermList term*))
  | '!' term -> ^(Anti term)
;
term :
    pattern
  | ID -> ^(Var ID)
  | builtin
;

builtin :
  INT -> ^(BuiltinInt INT)
;

symbol :
  ID COLON INT -> ^(Symbol ID INT)
;

ABSTRACT : 'abstract';
SYNTAX   : 'syntax';
STRATEGIES   : 'strategies';
FUNCTIONS : 'functions';


ARROW : '->' ;
AMPERCENT : '&' ;
COLON : ':' ;
LPAR : '(' ;
RPAR : ')' ;
LBRACE : '{' ;
RBRACE : '}' ;
LBRACKET : '[' ;
RBRACKET : ']' ;
COMMA : ',' ;
SEMICOLON : ';' ;
CHOICE : '<+' ;
IDENTITY : 'identity';
FAIL : 'fail';
ALL : 'all';
ONE : 'one';
MU : 'mu';
LET : 'let';
IN : 'in';
SIGNATURE : 'signature';
EQUALS : '=';
ALT : '|';
DOUBLEEQUALS : '==';
NOTEQUALS : '!=';
DOT : '.';
LEQ : '<=';
LT : '<';
GEQ : '>=';
GT : '>';
IF : 'if' ;
INT : ('0'..'9')+ ;
ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ('*')?;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;

MLCOMMENT :
  '/*' ~'*'.* '*/'
  {$channel=HIDDEN;}
  ;


