parser grammar PPIslandParser;
options { tokenVocab=PPIslandLexer; }

start : (island|.)*? ;

island :
  MATCH LPAREN (ID (COMMA ID)?)? RPAREN LBRACE (trule)* RBRACE 
| INCLUDE LBRACE ID LBRACE
;
trule : pattern ARROW block ;

block : LBRACE (island | block | .)*? RBRACE ;

pattern : ID (LPAREN ( pattern (COMMA pattern)*)? RPAREN)? ;

/**
- match constraints :   pattern << term
- logical operations :  constraint ('&&'|'||') constraint
- numeric constraints : term ('>'|'<'|'>='|'<='|'=='|'!=') term 

priority in operators is :
(from higher to lower)
 '<<'
 '>'|'<'|'>='|'<='|'=='|'!='
 '&&'
 '||'
*/
constraint : 
 constraint1
;

constraint1 :
  constraint2 (or=OR constraint2)*
;

constraint2 :
  constraint3 (and=AND constraint3)*
;

constraint3 :
    pattern MATCH_SYMBOL BQ
  | term (GREATERTHAN|GREATEROREQ|LOWERTHAN|LOWEROREQ|DOUBLEEQ|DIFFERENT) term
  | LPAREN constraint RPAREN
;


