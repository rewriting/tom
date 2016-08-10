parser grammar Island5Parser;
options { tokenVocab=Island5Lexer; }

start : (island|.)*? ;

island 
: MATCH LPAREN (bqterm (COMMA bqterm)?)? RPAREN LBRACE (trule)* RBRACE 
| INCLUDE LBRACE ID LBRACE
;

trule : 
    patternlist ARROW block
  | constraint ARROW block
;

block : LBRACE (island | block | .)*? RBRACE ;


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

patternlist:
  pattern (COMMA pattern)* ((AND|OR) constraint)?
;

constraint :
  constraint_and (or=OR constraint_and)*
;

constraint_and :
  constraint_atom (and=AND constraint_atom)*
;

constraint_atom :
    pattern MATCH_SYMBOL bqterm
  | bqterm (GREATERTHAN|GREATEROREQ|LOWERTHAN|LOWEROREQ|DOUBLEEQ|DIFFERENT) bqterm
  | LPAREN constraint RPAREN
;

term : 
    ID STAR?
  | ID LPAREN (term (COMMA term)*)? RPAREN 
  | constant
;

bqterm :
    ID? BQUOTE? term
;

pattern :
    ID AT pattern 
  | ANTI pattern
  | fsymbol explicit_args
  | fsymbol implicit_args
  | ID STAR?
  | UNDERSCORE STAR?
  | constant STAR?
;

fsymbol :
    head_symbol
  | LPAREN head_symbol (PIPE head_symbol)* RPAREN
;

head_symbol :
    ID (QMARK | DQMARK)?
  | constant
;

constant : INTEGER|LONG|CHAR|DOUBLE|STRING;

explicit_args : LPAREN (pattern (COMMA pattern)*)? RPAREN ;

implicit_args : LSQUAREBR (ID EQUAL pattern (COMMA ID EQUAL pattern)*)? RSQUAREBR ;


