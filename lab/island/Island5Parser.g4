parser grammar Island5Parser;
options { tokenVocab=Island5Lexer; }

start : (island | water)*? ;

island 
  : matchStatement
  | strategyStatement
  | includeStatement
  | typeterm
  | operator
  | oplist
  | oparray
  ;

matchStatement
  : MATCH LPAREN (bqterm (COMMA bqterm)?)? RPAREN LBRACE actionRule* RBRACE 
  ;

strategyStatement
  : STRATEGY ID LPAREN slotList? RPAREN EXTENDS bqterm LBRACE visit* RBRACE
  ;

includeStatement
  : INCLUDE LBRACE ID RBRACE 
  ;

visit
  : VISIT ID LBRACE actionRule* RBRACE
  ;

actionRule
  : patternlist ARROW block
  | patternlist ARROW bqterm
  | constraint ARROW block
  | constraint ARROW bqterm
  ;

block 
  : LBRACE (island | block | water)*? RBRACE
  ;

water
  : .
  ;

slotList
  : slot (COMMA slot)*
  ;

slot
  : id1=ID COLON? id2=ID
  ;

patternlist
  : pattern (COMMA pattern)* ((AND | OR) constraint)?
  ;

constraint
  : constraint AND constraint
  | constraint OR constraint
  | pattern MATCH_SYMBOL bqterm
  | bqterm GREATERTHAN bqterm
  | bqterm GREATEROREQ bqterm
  | bqterm LOWERTHAN bqterm
  | bqterm LOWEROREQ bqterm
  | bqterm DOUBLEEQ bqterm
  | bqterm DIFFERENT bqterm
  | LPAREN constraint RPAREN
  ;

term
  : ID STAR?
  | ID LPAREN (term (COMMA term)*)? RPAREN 
  | constant
  ;

// may be change this syntax: `term:sort
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
  : ID QMARK?
  | ID DQMARK?
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

/*
 * signature
 */
typeterm
  : TYPETERM ID (EXTENDS ID)? LBRACE 
    implement isSort? equalsTerm?
    RBRACE
  ;

operator
  : OP codomain=ID opname=ID LPAREN slotList? RPAREN LBRACE 
    //(isFsym | make | getSlot | getDefault)*
    isFsym? make? getSlot* getDefault*
    RBRACE
  ;

oplist
  : OPARRAY codomain=ID opname=ID LPAREN domain=ID STAR RPAREN LBRACE 
    //(isFsym | makeEmptyList | makeInsertList | getHead | getTail | isEmptyList)*
    isFsym? makeEmptyList? makeInsertList? getHead? getTail? isEmptyList?
    RBRACE
  ;

oparray
  : OPARRAY codomain=ID opname=ID LPAREN domain=ID STAR RPAREN LBRACE 
    //(isFsym | makeEmptyArray | makeAppendArray | getElement | getSize)*
    isFsym? makeEmptyArray? makeAppendArray? getElement? getSize?
    RBRACE
  ;

implement
  : IMPLEMENT block
  ;

equalsTerm
  : EQUALS LPAREN id1=ID COMMA id2=ID RPAREN block
  ;

isSort
  : IS_SORT LPAREN ID RPAREN block
  ;

isFsym
  : IS_FSYM LPAREN ID RPAREN block
  ;

make
  : MAKE LPAREN (ID (COMMA ID)*)? RPAREN block
  ;

makeEmptyList
  : MAKE_EMPTY LPAREN RPAREN block
  ;

makeEmptyArray
  : MAKE_EMPTY LPAREN ID RPAREN block
  ;

makeAppendArray
  : MAKE_APPEND LPAREN id1=ID COMMA id2=ID RPAREN block
  ;
  
makeInsertList
  : MAKE_INSERT LPAREN id1=ID COMMA id2=ID RPAREN block
  ;
  
getSlot
  : GET_SLOT LPAREN id1=ID COMMA id2=ID RPAREN block
  ;

getHead
  : GET_HEAD LPAREN ID RPAREN block
  ;

getTail
  : GET_TAIL LPAREN ID RPAREN block
  ;

getElement
  : GET_ELEMENT LPAREN id1=ID COMMA id2=ID RPAREN block
  ;

isEmptyList
  : IS_EMPTY LPAREN ID RPAREN block
  ;

getSize
  : GET_SIZE LPAREN ID RPAREN block
  ;

getDefault
  : GET_DEFAULT LPAREN ID RPAREN block
  ;

