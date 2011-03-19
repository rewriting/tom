grammar miniTom;

options {
	output=AST;
//  filter=true;
  ASTLabelType=Tree;
  backtrack=true;
  //tokenVocab=IGTokens;
}

tokens {
  PROGRAM;
  CODE;
  SUBCODE;
  HOST;
}

@lexer::header {
import org.antlr.runtime.tree.*;
}
@parser::header {
import org.antlr.runtime.tree.*;
}
@lexer::members{int levelcounter=-1;}
/* Parser rules */

program :	LEFTPAR RIGHTPAR LEFTBR code* -> ^(PROGRAM code*) ;

code :
        s1=statement SEMICOLUMN -> ^(CODE $s1)
      | s2=subcode -> ^(CODE $s2);
//      | OBRA s3=name RARROW LEFTBR CBRA -> ^(HOST $s3);

subcode : LEFTBR inside+ RIGHTBR -> ^(SUBCODE inside+);

inside    : B  ;
statement	:	A+ ;

//name : ('A'..'Z')+('a'..'z' | 'A'..'Z' | '0'..'9')* ;
//name : LETTER+;

/* Lexer rules */

LEFTPAR    : '(' ;
RIGHTPAR   : ')' ;
LEFTBR     : '{' {levelcounter+=1;} ;
RIGHTBR    : '}' {if(levelcounter==0){emit(Token.EOF_TOKEN);} else{levelcounter-=1;}  } ;
SEMICOLUMN : ';' ;
OPENCOM    : '/*'  ;
A          : 'alice' ;
B          : 'bob' ;
OBRA       : '[' ;
CBRA       : ']' ;
RARROW     : '->';
LETTER     : 'A'..'Z' ;
WS	: ('\r' | '\n' | '\t' | ' ' )* { $channel = HIDDEN; };

