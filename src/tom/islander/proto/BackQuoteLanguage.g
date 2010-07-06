grammar BackQuoteLanguage;

options {
  backtrack=true;
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=BackQuoteTokens;
}

@header{
//  package islander.proto;
}

@lexer::header{
//  package islander.proto;
}

@lexer::members{
  public static int bnesting = 0;
}

backQuoteConstruct :
  id=ID (
  (LPAREN RPAREN)? -> ^(BQVariable $id)
  | '*' -> ^(BQVariableStar $id)
  )
  | '_' -> ^(BQUnamedVariable )
  | '_*' -> ^(BQUnamedVariableStar )
//  | c=((bqVar '.')? method ('.' method)+) -> ^(BQComposite $c) // x.get() ; get() ; x.get().get()
  ;

ID  : ('a'..'z'|'A'..'Z')+ ;

INT : ('0'..'9')+ ;

WS  : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SL_COMMENT :
  '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
  { $channel=HIDDEN; }
  ;

ML_COMMENT :
  '/*' ( options {greedy=false;} : . )* '*/'
  { $channel=HIDDEN; }
  ;

LPAREN : '(' { bnesting++; System.out.println("backquote bnesting++ = " + bnesting);}
         ;

RPAREN : ')'
  {
    if ( bnesting<=0 ) {
      System.out.println("exit backquote language\n");
      emit(Token.EOF_TOKEN);
    }
    else {
      bnesting--;
      System.out.println("backquote bnesting-- = " + bnesting);
    }
  }
  ;
