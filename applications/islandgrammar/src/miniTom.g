grammar miniTom;

options {
	output=AST;
  ASTLabelType=Tree;
  backtrack=true;
  //tokenVocab=IGTokens;
}

tokens {
  PROGRAM;
  CODE;
}

/* Parser rules */

program :	LEFTPAR RIGHTPAR LEFTBR code* RIGHTBR -> ^(PROGRAM code*) ;

code :	(s=statement {System.out.println($s.text);} SEMICOLUMN) -> ^(CODE $s) ;

statement	:	A+ ;


/* Lexer rules */

LEFTPAR    : '(' ;
RIGHTPAR   : ')' ;
LEFTBR     : '{' ;
RIGHTBR    : '}' {emit(Token.EOF_TOKEN);} ;
SEMICOLUMN : ';' ;
A          : 'alice' ;
B          : 'bob' ;

WS	: ('\r' | '\n' | '\t' | ' ' )* { $channel = HIDDEN; };

