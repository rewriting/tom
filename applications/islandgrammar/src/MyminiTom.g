grammar MyminiTom;

options {
	output=AST;
  ASTLabelType=Tree;
  //tokenVocab=IGTokens;
}

tokens {
  PROGRAM;
  CODE;
}

/* Parser rules */

program :	LEFTPAR RIGHTPAR LEFTBR code RIGHTBR -> ^(PROGRAM code) ;

code :	(s=statement {System.out.println($s.text);} SEMICOLUMN) -> ^(CODE $s) ;

statement	:	'a'+ ;


/* Lexer rules */

LEFTPAR    : '(' ;
RIGHTPAR   : ')' ;
LEFTBR     : '{' ;
RIGHTBR    : '}' ;
SEMICOLUMN : ';' ;
A          : 'alice' ;
B          : 'bob' ;

