grammar miniTom;

options {
	output=AST;
  ASTLabelType=Tree;
  backtrack=true;
}

tokens {
  HOSTBLOCK;
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
public static boolean switchUponBrace = false;
}

/* Parser rules */


typetermconstruct : STRING LEFTBR implementKword (issortKword)? (equalsKword)? -> ^(TYPETERM);

implementKword : IMPLEMENT  s1=innerBlock -> ^(HOSTBLOCK $s1) ;
issortKword : ISSORT LEFTPAR s1=STRING RIGHTPAR LEFTBR s2=innerBlock -> ^(HOSTBLOCK $s2) ;
equalsKword : EQUALS LEFTPAR s1=STRING COMMA s2=STRING LEFTBR s3=innerBlock -> ^(HOSTBLOCK $s3) ;

matchconstruct :	LEFTPAR RIGHTPAR LEFTBR patternaction* -> ^(MATCH patternaction*) ;

patternaction : (s1=STRING)  s2=innerBlock  -> ^(PATTERNACTION $s1 $s2);

innerBlock : VOUCHER {miniTomLexer.switchUponBrace = false;} -> { miniTomLexer.SubTrees.poll()};

/* Lexer rules */
VOUCHER  : ;
LEFTPAR    : '(' ;
RIGHTPAR   : ')' ;
LEFTBR     : '{' {
  if(! miniTomLexer.switchUponBrace) {
    levelcounter++;
  } else {
    HostParser switcher = new HostParser(input,"{","}");
    if(!SubTrees.offer((Tree) switcher.getTree())) {
      System.out.println("Achtung ! Could not queue '{' tree");
    }
    emit(new ClassicToken(miniTomLexer.VOUCHER));
  }
};
RIGHTBR    : '}' {if(levelcounter==0){emit(Token.EOF_TOKEN);} else{levelcounter-=1;}  } ;
SEMICOLUMN : ';' ;
COMMA      : ',' ;
IMPLEMENT  : 'implement' {miniTomLexer.switchUponBrace = true; } ;
ISSORT     : 'is_sort' {miniTomLexer.switchUponBrace = true; };
EQUALS     : 'equals' {miniTomLexer.switchUponBrace = true; };
ARROWBR     : '-> {' {
//  levelcounter+=0;
//  input.rewind();
  HostParser switcher = new HostParser(input,"{","}");
  if(!SubTrees.offer((Tree) switcher.getTree())) {
    System.out.println("Achtung ! Could not queue '{' tree");
  }
  emit(new ClassicToken(miniTomLexer.VOUCHER));
} ;
OPENCOM    : '/*' {
  input.rewind();
  HostParser switcher = new HostParser(input,"*/");
  if (!SubTrees.offer((Tree) switcher.getTree())) {
    System.out.println("Achtung ! Could not queue tree");
  };
  ClassicToken Voucher = new ClassicToken(miniTomLexer.VOUCHER);
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

