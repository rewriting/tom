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
@lexer::members{int levelcounter=0;}
/* Parser rules */

program :	LEFTPAR RIGHTPAR LEFTBR code* RIGHTBR -> ^(PROGRAM code*) ;

code :	(s=statement {System.out.println($s.text);} SEMICOLUMN) -> ^(CODE $s) ;

statement	:	A+ ;


/* Lexer rules */

LEFTPAR    : '(' ;
RIGHTPAR   : ')' ;
LEFTBR     : '{' {
		     levelcounter++;
	     };
RIGHTBR    : '}'{ if(levelcounter==0)
{
	emit(Token.EOF_TOKEN);} else {
		levelcounter-=1;}
		} ;
SEMICOLUMN : ';' ;
A          : 'alice' ;
B          : 'bob' ;

WS	: ('\r' | '\n' | '\t' | ' ' )* { $channel = HIDDEN; };

