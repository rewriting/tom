grammar miniTom;

options {
	output=AST;
  ASTLabelType=Tree;
  backtrack=true;
}

tokens {
  HOSTBLOCK;
  MATCH;
  MATCHARG;
  TYPED;
  UNTYPED;
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
int currentToken = 0;
public static Queue<Tree> SubTrees = new LinkedList<Tree>();
}

/* Parser rules */


typetermconstruct : STRING LEFTBR implementKword (issortKword)? (equalsKword)? -> ^(TYPETERM implementKword (issortKword)? (equalsKword)?);

implementKword : IMPLEMENT s1=innerBlock -> ^(IMPLEMENT $s1) ;
issortKword : ISSORT LEFTPAR s1=STRING RIGHTPAR s2=innerBlock -> ^(ISSORT $s2) ;
equalsKword : EQUALS LEFTPAR s1=STRING COMMA s2=STRING LEFTBR s3=innerBlock -> ^(EQUALS $s3) ;

matchconstruct : LEFTPAR RIGHTPAR LEFTBR patternaction*  -> ^(MATCH patternaction*) ;

patternaction : (s1=STRING) RARROW s2=innerBlock  -> ^(PATTERNACTION $s1 $s2);

innerBlock : VOUCHER -> { miniTomLexer.SubTrees.poll()};

/* Lexer rules */
VOUCHER  : ;
LEFTPAR    : '(' ;
RIGHTPAR   : ')' ;
LEFTBR     : '{' {
  if(currentToken == 0) {
    levelcounter++;
  } else {
    HostParser switcher = new HostParser(input,"{","}");
    if(!SubTrees.offer((Tree) switcher.getTree())) {
      System.out.println("Achtung ! Could not queue '{' tree");
    }
    emit(new ClassicToken(miniTomLexer.VOUCHER));
  }
} ;
RIGHTBR    : '}' {
  if(levelcounter<=0){emit(Token.EOF_TOKEN);} else{levelcounter-=1;}
} ;
SEMICOLUMN : ';' ;
COMMA      : ',' ;
RARROW     : '->' {
  currentToken = miniTomLexer.RARROW ;
} ;
IMPLEMENT  : 'implement' {
  currentToken = miniTomLexer.IMPLEMENT ;
} ;
ISSORT     : 'is_sort' {
  currentToken = miniTomLexer.ISSORT ;
} ;
EQUALS     : 'equals' {
  currentToken = miniTomLexer.EQUALS ;
};
LETTER     : 'A'..'Z' | 'a'..'z';
DIGIT      : '0'..'9';
STRING     : LETTER (LETTER | DIGIT)*;
WS	: ('\r' | '\n' | '\t' | ' ' )* { $channel = HIDDEN; };
SL_COMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
  { $channel=HIDDEN; } ;
ML_COMMENT : '/*' ( options {greedy=false;} : . )* '*/'
  { $channel=HIDDEN; } ;

