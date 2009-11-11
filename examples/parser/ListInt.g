grammar ListInt;
options {
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=ListIntTokens;
}

@header {
  package parser;
}
@lexer::header {
  package parser;
}

listint :
  LBRACK (INT (COMMA INT)*)? RBRACK -> ^(ConcInt INT*)
;

INT : ('0'..'9')+ ;
LBRACK : '[' ;
RBRACK : ']' ;
COMMA : ',' ;

WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;
SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
