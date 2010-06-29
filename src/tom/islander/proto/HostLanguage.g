grammar HostLanguage;

options {
  backtrack=true;
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=HostTokens;
}

@header{
//package proto;
}

@lexer::header{
  //package proto;
}

@lexer::members{
  public static int hnesting = 0;
  public static final int TOM_CHANNEL = 42;
  public static final int BACKQUOTE_CHANNEL = 43;
}

// start
program :
  block* -> ^(Program block*)
  ;

block :
   hostConstruct -> ^(HostBlock hostConstruct)
  | matchConstruct -> ^(TomBlock matchConstruct)
  /* and few other : all '%something' */
  | backquoteConstruct -> ^(BackQuoteBlock backquoteConstruct)
//  | '{' block '}' -> block

;

//host
hostConstruct :
  (variable)* (method)+ -> ^(HostConstruct ^(VariableList (variable)*) ^(MethodList (method)+))
  | statement -> ^(StatementConstruct statement)
  ;

variable :
  'int' id=ID e=('=' expr)? ';' -> ^(Variable $id)
/*  -> {e!=null} ^(VarExp $id $e)*/
  
  ;

method  :
  'method' id=ID '(' ')' '{' (variable)* (statement)+ '}' -> ^(Method $id  ^(VariableList (variable)*) ^(StatementList (statement)+))
  ;

statement :
  id=ID '=' expr ';' -> ^(Equal $id expr)
  | 'return' expr ';' -> ^(Return expr)
  ;

expr :
  ID
  | INT
  ;


//Tom
matchConstruct :
  MATCH^ //{System.out.println("before \%match");}
  ;

//BackQuote
backquoteConstruct :
  BACKQUOTE^
  ;

//Lexer
ID  : ('a'..'z'|'A'..'Z')+ ;

INT : ('0'..'9')+ ;

LBRACE : '{' { hnesting++; System.out.println("host hnesting++ = " + hnesting);} ;

RBRACE : '}'
  {
    if ( hnesting<=0 ) {
      emit(Token.EOF_TOKEN);
      System.out.println("exit embedded hostlanguage\n");
    }
    else {
      hnesting--;System.out.println("host hnesting-- = " + hnesting);
    }
  }
  ;

MATCH : '%match'
  {
System.out.println("\nbefore new Tom*");
    TomLanguageLexer lexer = new TomLanguageLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
System.out.println("host, tokenstream = " + tokens.toString() + " /fin");
System.out.println("host, tokens list = " + tokens.getTokens().toString());
    TomLanguageParser parser = new TomLanguageParser(tokens);
System.out.println("before parser.matchconstruct()");
    parser.matchConstruct();
/*
System.out.println("HOST before match channel change, channel = " + $channel);
    $channel=TOM_CHANNEL;
System.out.println("HOST after match channel change, channel = " + $channel);
*/
  }
;

BACKQUOTE : '`('
  {
System.out.println("\nbefore new BackQuote*");
    BackQuoteLanguageLexer lexer = new BackQuoteLanguageLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
System.out.println("host, tokens = " + tokens.toString() + " /fin");
System.out.println("host, tokens list = " + tokens.getTokens().toString());
    BackQuoteLanguageParser parser = new BackQuoteLanguageParser(tokens);
System.out.println("before parser.backquoteconstruct()");
   parser.backQuoteConstruct();
/*
System.out.println("HOST before backquote channel change, channel = " + $channel);
    $channel=BACKQUOTE_CHANNEL;
System.out.println("HOST after backquote channel change, channel = " + $channel);
*/
  }
  ;

WS  : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SL_COMMENT :
  '//'
  (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
  { $channel=HIDDEN; }
  ;

ML_COMMENT :
  '/*' ( options {greedy=false;} : . )* '*/'
  { $channel=HIDDEN; }
  ;

