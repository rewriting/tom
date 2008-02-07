/* This grammar parses simple rules */
grammar Rule;
options {
  output=AST;
  ASTLabelType=RuleTree;
}

tokens {
%include { rule/RuleTokenList.txt }
}

@header {
package tom.gom.expander.rule;
import tom.gom.adt.rule.RuleTree;
}
@lexer::header {
package tom.gom.expander.rule;
import tom.gom.adt.rule.RuleTree;
}
ruleset:
  (rule)* EOF -> ^(RuleList (rule)*)
;
rule:
  pattern ARROW term (IF cond=condition)?
    -> { cond == null }? ^(Rule pattern term)
    -> ^(ConditionalRule pattern term $cond)
;
graphruleset:
  (graphrule)* EOF -> ^(RuleList (graphrule)*)
;
graphrule:
  lhs=labelledpattern ARROW rhs=labelledpattern (IF cond=condition)?
    -> { cond == null }? ^(Rule $lhs $rhs)
    -> ^(ConditionalRule $lhs $rhs $cond)
;
condition:
  cond=andcondition (or=OR andcondition)*
  -> {or!=null}? ^(CondOr andcondition*)
  -> $cond
;
andcondition:
  cond=notcondition (and=AND notcondition)*
  -> {and!=null}? ^(CondAnd notcondition*)
  -> $cond
;
notcondition:
  not=NOT? cond=simplecondition
  -> {not!=null}? ^(CondNot $cond)
  -> $cond
;
simplecondition:
  p1=term (EQUALS p2=term
          | NOTEQUALS p3=term
          | LEQ p4=term
          | LT p5=term
          | GEQ p6=term
          | GT p7=term
          | DOT ID LPAR p8=term RPAR
          )?
    -> {p2!=null}? ^(CondEquals $p1 $p2)
    -> {p3!=null}? ^(CondNotEquals $p1 $p3)
    -> {p4!=null}? ^(CondLessEquals $p1 $p4)
    -> {p5!=null}? ^(CondLessThan $p1 $p5)
    -> {p6!=null}? ^(CondGreaterEquals $p1 $p6)
    -> {p7!=null}? ^(CondGreaterThan $p1 $p7)
    -> {p8!=null}? ^(CondMethod $p1 ID $p8)
    -> ^(CondTerm $p1)
  | LPAR cond=condition RPAR
  -> $cond
;
pattern:
  ID LPAR (term (COMA term)*)? RPAR -> ^(Appl ID ^(TermList term*))
  | (varname=ID) AT (funname=ID) LPAR (term (COMA term)*)? RPAR -> ^(At $varname ^(Appl $funname ^(TermList term*)))
  | UNDERSCORE -> ^(UnnamedVar)
  | UNDERSCORE STAR -> ^(UnnamedVarStar)
;
term:
  pattern
/*  | ID -> ^(Var ID)*/
  | ID (s=STAR)? -> {null==s}? ^(Var ID) ->^(VarStar ID)
  | builtin
;
builtin:
  INT -> ^(BuiltinInt INT)
  | STRING -> ^(BuiltinString STRING)
;

labelledpattern:
  (namelabel=ID COLON)? p=graphpattern
  -> {$namelabel!=null}? ^(LabTerm $namelabel $p)
  -> $p
;
graphpattern:
  constructor
/*  | ID -> ^(Var ID)*/
  | ID (s=STAR)? -> {null==s}? ^(Var ID) ->^(VarStar ID)
  | builtin
  | ref
;
ref:
  AMPERCENT ID -> ^(RefTerm ID)
;
constructor:
  ID LPAR (labelledpattern (COMA labelledpattern)*)? RPAR
  -> ^(Appl ID ^(TermList labelledpattern*))
;

ARROW : '->';
AMPERCENT : '&';
UNDERSCORE : '_';
STAR : '*';
AT : '@';
COLON : ':';
LPAR : '(';
RPAR : ')';
COMA : ',';
AND : '&&';
OR : '||';
NOT : '!';
EQUALS : '==';
NOTEQUALS : '!=';
DOT : '.';
LEQ : '<=';
LT : '<';
GEQ : '>=';
GT : '>';
IF : 'if' ;
INT : ('0'..'9')+;
ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\');
STRING : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"';
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; };

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; };
