/* This grammar parses simple rules */
grammar Graph;
options {
  output=AST;
  ASTLabelType=TermTree;
}

tokens {
  %include { term/TermTokenList.txt }
}

@header {
  package parser;
  import parser.term.TermTree;
}
@lexer::header {
  package parser;
  import parser.term.TermTree;
}
uid :
  id1=ID COLON id2=ID -> ^(Uid $id1 $id2)
  ;
node :
  LT uid BAR (port)* GT -> ^(Node uid ^(PList (port)*))
  ;
port :
  ID ARROW (neighbour)* HAT state
    -> ^(Port ID ^(NeighList (neighbour)*) state)
  ;
portref :
  ID -> ^(RefPort ID)
  ;
noderef :
  ID -> ^(RefNode ID)
  ;
neighbour :
  LPAR noderef DASH (portref)* RPAR
    -> ^(Neighbour noderef ^(PList (portref)*))
  ;
state :
  'b' -> ^(B)
  | 'v' -> ^(V)
  | 'h' -> ^(H)
  ;

ARROW : '->' ;
COLON : ':' ;
LPAR : '(' ;
RPAR : ')' ;
BAR : '|';
DASH : '-';
HAT : '^';
LT : '<';
GT : '>';
INT : ('0'..'9')+ ;
ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ('*')?;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
