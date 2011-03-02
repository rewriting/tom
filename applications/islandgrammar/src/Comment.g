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

oneline : t=text NEWLINE -> ^(ONELINE $t);

regular : t=text CLOSECOM -> ^(REGULAR $t);

text : ANYTHING* ;

CLOSECOM    : '*/'  ;
NEWLINE     : '\n'  ;
WS          : ('\r' | '\t' | ' ')* { $channel = HIDDEN; };
ANYTHING    : ( 'a'..'z'
              | 'A'..'Z'
              | '0'..'9'
              | '_'
              | '*'
              | '/'
              );

