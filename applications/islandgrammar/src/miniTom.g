grammar miniTom;

options {
	output=AST;
  ASTLabelType=Tree;
  backtrack=true;
}

tokens {
  MATCH;
  TYPETERM;
  PATTERNACTION;
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
public static Queue<Tree> SubTrees = new LinkedList<Tree>();
}

/* Parser rules */


typetermconstruct : STRING LEFTBR implementKword (issortKword)? (equalsKword)? -> ^(TYPETERM);

implementKword : IMPLEMENT LEFTBR s1=innerBlock RIGHTBR -> ^(HOSTBLOCK $s1) ;
issortKword : ISSORT LEFTPAR s1=STRING RIGHTPAR LEFTBR s2=innerBlock RIGHTBR -> ^(HOSTBLOCK $s2) ;
equalsKword : EQUALS LEFTPAR s1=STRING COMMA s2=STRING LEFTBR s3=innerBlock RIGHTBR -> ^(HOSTBLOCK $s3) ;

matchconstruct :	LEFTPAR RIGHTPAR LEFTBR patternaction* -> ^(MATCH patternaction*) ;

patternaction : (s1=STRING)  s2=innerBlock  -> ^(PATTERNACTION $s1 $s2);

innerBlock : ARROWBR -> {miniTomLexer.SubTrees.poll()};

/* Lexer rules */
HOSTBLOCK  : ;
LEFTPAR    : '(' ;
RIGHTPAR   : ')' ;
LEFTBR     : '{' {levelcounter++;};
RIGHTBR    : '}' {if(levelcounter==0){emit(Token.EOF_TOKEN);} else{levelcounter-=1;}  } ;
SEMICOLUMN : ';' ;
COMMA      : ',' ;
IMPLEMENT  : 'implement' ;
ISSORT     : 'is_sort' ;
EQUALS     : 'equals' ;
ARROWBR     : '-> {' {
  levelcounter+=0;
//  input.rewind();
  HostParser switcher = new HostParser(input,"}");
  if(!SubTrees.offer((Tree) switcher.getTree())) {
    System.out.println("Achtung ! Could not queue '{' tree");
  }
  emit(new ClassicToken(miniTomLexer.ARROWBR));
//  emit(new ClassicToken(miniTomLexer.HOSTBLOCK));
} ;
OPENCOM    : '/*' {
  input.rewind();
  HostParser switcher = new HostParser(input,"*/");
  if (!SubTrees.offer((Tree) switcher.getTree())) {
    System.out.println("Achtung ! Could not queue tree");
  };
  ClassicToken Voucher = new ClassicToken(miniTomLexer.HOSTBLOCK);
  emit(Voucher);
};
A          : 'alice' ;
B          : 'bob' ;
OBRA       : '[' ;
CBRA       : ']' ;
RARROW     : '->';
LETTER     : 'A'..'Z' | 'a'..'z';
DIGIT      : '0'..'9';
STRING     : LETTER (LETTER | DIGIT)*;
WS	: ('\r' | '\n' | '\t' | ' ' )* { $channel = HIDDEN; };

