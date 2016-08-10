parser grammar Island5Parser;
options { tokenVocab=Island5Lexer; }

start : (island|.)*? ;

island 
  : MATCH LPAREN (bqterm (COMMA bqterm)?)? RPAREN LBRACE (trule)* RBRACE 
  | INCLUDE LBRACE ID LBRACE
  ;

trule
  : patternlist ARROW block
  | constraint ARROW block
  ;

block 
  : LBRACE (island | block | .)*? RBRACE
  ;


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

patternlist
  : pattern (COMMA pattern)* ((AND|OR) constraint)?
  ;

constraint
  : constraintAnd (or=OR constraintAnd)*
  ;

constraintAnd
  : constraintAtom (and=AND constraintAtom)*
  ;

constraintAtom
  : pattern MATCH_SYMBOL bqterm
  | bqterm ('>' | '>=' | '<' | '<=' | '==' | '!=') bqterm
  | LPAREN constraint RPAREN
  ;

term
  : ID STAR?
  | ID LPAREN (term (COMMA term)*)? RPAREN 
  | constant
  ;

bqterm
  : ID? BQUOTE? term
  ;

pattern
  : ID AT pattern 
  | ANTI pattern
  | fsymbol explicitArgs
  | fsymbol implicitArgs
  | ID STAR?
  | UNDERSCORE STAR?
  | constant STAR?
  ;

fsymbol 
  : headSymbol
  | LPAREN headSymbol (PIPE headSymbol)* RPAREN
  ;

headSymbol
  : ID (QMARK | DQMARK)?
  | constant
  ;

constant
  : INTEGER
  | LONG
  | CHAR
  | DOUBLE
  | STRING
  ;

explicitArgs
  : LPAREN (pattern (COMMA pattern)*)? RPAREN
  ;

implicitArgs
  : LSQUAREBR (ID EQUAL pattern (COMMA ID EQUAL pattern)*)? RSQUAREBR 
  ;


