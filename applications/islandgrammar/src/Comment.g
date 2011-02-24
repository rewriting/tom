grammar Comment;

options {
  output=AST;
  ASTLabelType=Tree;
  backtrack=true;
}

CLOSECOM    : '*/'  ;
/* En fait, il faudrait écrire le Comment Parser comme un parser ANTLR parce que de toute façon c'est pour utiliser des Lexer, et tout, qui sont disponible par ANTLR.

 On se mets donc sérieusement à écrire les choses dans ANTLR. Pour HostParser, ce serait bien de rajouter un Lexer avant, mais je sais pas si on a vraiment envie de le faire en ANTLR. Au contraire, plutôt garder celui-là en Java pur.*/
