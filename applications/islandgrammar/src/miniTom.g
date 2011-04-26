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
import java.util.Queue;
import java.util.LinkedList;
import org.antlr.runtime.tree.*;
}

@lexer::members{
int levelcounter=-1;
//for more informations check : -http://www.antlr.org/wiki/pages/viewpage.action?pageId=5341230
			        -http://www.antlr.org/wiki/pages/viewpage.action?pageId=5341217
public Token nextToken() {
		while (true) {
			state.token = null;
			state.channel = Token.DEFAULT_CHANNEL;
			state.tokenStartCharIndex = input.index();
			state.tokenStartCharPositionInLine = input.getCharPositionInLine();
			state.tokenStartLine = input.getLine();
			state.text = null;
			if ( input.LA(1)==CharStream.EOF ) {
				return Token.EOF_TOKEN;
			}
			try {
				mTokens();
				if ( state.token==null ) {
					emit();
				}
				else if ( state.token==Token.SKIP_TOKEN ) {
					continue;
				}
				return state.token;
			}
			catch (RecognitionException re) {
				System.out.println("hmmm ... ça c'est du langage hote");
			}
		}
	}
}


/* Parser rules */

program :	LEFTPAR RIGHTPAR LEFTBR code* -> ^(PROGRAM code*) ;

code :
        s1=statement SEMICOLUMN -> ^(CODE $s1)
      | s2=subcode -> ^(CODE $s2)
      | s3=reccall -> ^($s3);
//      | OBRA s3=name RARROW LEFTBR CBRA -> ^(HOST $s3);

subcode : LEFTBR inside+ RIGHTBR -> ^(SUBCODE inside+);
reccall : SUBCODE -> {miniTomLexer.SubTrees.poll()};
inside    : B  ;
statement	:	A+ ;

/* Lexer rules */
SUBCODE    : ;
LEFTPAR    : '(' ;
RIGHTPAR   : ')' ;
LEFTBR     : '{' {levelcounter+=1;} ;
RIGHTBR    : '}' {if(levelcounter==0){emit(Token.EOF_TOKEN);} else{levelcounter-=1;}  } ;
SEMICOLUMN : ';' ;
A          : 'alice' ;
B          : 'bob' ;
OBRA       : '[' ;
CBRA       : ']' ;
RARROW     : '->';
LETTER     : 'A'..'Z' ;
WS	: ('\r' | '\n' | '\t' | ' ' )* { $channel = HIDDEN; };
ML_COMMENT : '/*' ( options {greedy=false;} : . )* '*/' ; // Matches multiline comments: Everything from "/*" to the next "*/".
                                                          // (greedy=false prevents that the dot matches everything between the
                                                          // first "/*" and the last "*/", not the next "*/" in case of several
                                                          // multiline comments in a file.)
