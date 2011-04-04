grammar Comment;

options {
  output=AST;
  ASTLabelType=Tree;
  backtrack=true;
}

tokens {
  ONELINE;
  REGULAR;
}

@header {
import org.antlr.runtime.tree.*;
}

@lexer::members {
public boolean oneline = false;
  
public CommentLexer(CharStream input, boolean b) {
  this(input,new RecognizerSharedState());
  oneline = b;
}
}

oneline : t=ANYTHING -> ^(ONELINE $t);

regular  : t=ANYTHING -> ^(REGULAR $t);

CLOSECOM    : '*/' {if(!oneline){emit(Token.EOF_TOKEN);}};
NEWLINE     : '\n' {if( oneline){emit(Token.EOF_TOKEN);}};
WS          : ('\r' | '\t' | ' ')* { $channel = HIDDEN; };
ANYTHING    : ('a'..'z'|' '|'/')*;
