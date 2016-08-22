parser grammar TomIslandParser;
options { tokenVocab=TomIslandLexer; }

start : (island | water)*? ;

island 
  : matchStatement
  | strategyStatement
  | includeStatement
  | typeterm
  | operator
  | oplist
  | oparray
  | bqcomposite
  ;

matchStatement
  : MATCH LPAREN (bqterm (COMMA bqterm)*)? RPAREN LBRACE actionRule* RBRACE 
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
  : patternlist ((AND | OR) constraint)? ARROW block
  | patternlist ((AND | OR) constraint)? ARROW bqterm
  | c=constraint ARROW block
  | c=constraint ARROW bqterm
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
  : pattern (COMMA pattern)* 
  ;

constraint
  : constraint AND constraint
  | constraint OR constraint
  | pattern MATCH_SYMBOL bqterm
  | term GREATERTHAN term
  | term GREATEROREQ term
  | term LOWERTHAN term
  | term LOWEROREQ term
  | term DOUBLEEQ term
  | term DIFFERENT term
  | LPAREN c=constraint RPAREN
  ;

term
  : var=ID STAR?
  | fsym=ID LPAREN (term (COMMA term)*)? RPAREN 
  ;

// may be change this syntax: `term:sort
bqterm
  : codomain=ID? BQUOTE? fsym=ID LPAREN (bqterm (COMMA bqterm)*)? RPAREN 
  | codomain=ID? BQUOTE? var=ID STAR?
  | constant
  ;

bqcomposite
  : BQUOTE composite
  ;

composite
  : fsym=ID LPAREN composite* RPAREN
  | LPAREN composite* RPAREN
  | var=ID STAR?
  | constant
  | waterexceptparen
//  | .*?
  ;

waterexceptparen 
  :
  ~(LPAREN|RPAREN)+? 
  ;

compositeplus
  : composite+
  ;

pattern
  : ID AT pattern 
  | ANTI pattern
  | fsymbol explicitArgs
  | fsymbol implicitArgs
  | var=ID STAR?
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
  : TYPETERM type=ID (EXTENDS supertype=ID)? LBRACE 
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

