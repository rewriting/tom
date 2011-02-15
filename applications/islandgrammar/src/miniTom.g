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
  SUBCODE;
}
@lexer::members{int levelcounter=-1;}
/* Parser rules */

program :	LEFTPAR RIGHTPAR LEFTBR code* -> ^(PROGRAM code*) ;

code :
        s1=statement SEMICOLUMN -> ^(CODE $s1)
      | s2=subcode -> ^(CODE $s2);

subcode : LEFTBR inside+ RIGHTBR -> ^(SUBCODE inside+);

inside    : B  ;
statement	:	A+ ;


/* Lexer rules */

LEFTPAR    : '(' ;
RIGHTPAR   : ')' ;
LEFTBR     : '{' {levelcounter+=1;System.out.println(+levelcounter);} ;
RIGHTBR    : '}' {if(levelcounter==0){emit(Token.EOF_TOKEN);/*emit(new CommonToken(1,"}"));*/} else{levelcounter-=1;}  } ;
SEMICOLUMN : ';' ;
A          : 'alice' ;
B          : 'bob' ;

WS	: ('\r' | '\n' | '\t' | ' ' )* { $channel = HIDDEN; };

