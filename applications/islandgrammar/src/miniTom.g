grammar miniTom;

options {
	output=AST;
  ASTLabelType=Tree;
  backtrack=true;
}

tokens {
  PROGRAM;
  CODE;
  SUBCODE;
  HOST;
}

@parser::header {
import org.antlr.runtime.tree.*;
}

@lexer::header {
import org.antlr.runtime.tree.*;
}
@lexer::members{
  int levelcounter=-1;
  Tree subTree;
}

/* Parser rules */

program :	LEFTPAR RIGHTPAR LEFTBR code* -> ^(PROGRAM code*) ;

code :
        s1=statement SEMICOLUMN -> ^(CODE $s1)
      | s2=subcode -> ^(CODE $s2);
//      | OBRA s3=name RARROW LEFTBR CBRA -> ^(HOST $s3);

subcode : LEFTBR inside+ RIGHTBR -> ^(SUBCODE inside+);

inside    : B  ;
statement	:	A+ ;

/* Lexer rules */

LEFTPAR    : '(' ;
RIGHTPAR   : ')' ;
LEFTBR     : '{' {levelcounter+=1;} ;
RIGHTBR    : '}' {if(levelcounter==0){emit(Token.EOF_TOKEN);} else{levelcounter-=1;}  } ;
SEMICOLUMN : ';' ;
OPENCOM    : '/*' {
  CommentLexer lexer = new CommentLexer(input);
  CommonTokenStream tokens = new CommonTokenStream(lexer);
  CommentParser parser = new CommentParser(tokens);
  subTree = (Tree) parser.regular().getTree();
  };
A          : 'alice' ;
B          : 'bob' ;
OBRA       : '[' ;
CBRA       : ']' ;
RARROW     : '->';
LETTER     : 'A'..'Z' ;
WS	: ('\r' | '\n' | '\t' | ' ' )* { $channel = HIDDEN; };

