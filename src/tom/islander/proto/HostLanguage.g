grammar HostLanguage;

options {
  backtrack=true;
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=HostTokens;
}

@header{
  //package proto;
  import org.antlr.runtime.tree.*;
  import org.antlr.runtime.*;
}

@lexer::header{
  //package proto;
  import org.antlr.runtime.tree.*;
}

@lexer::members{
  public static int hnesting = 0;
  public static final int TOM_CHANNEL = 42;
  public static final int BACKQUOTE_CHANNEL = 43;
  /*
  private Tree getBQAST(CharStream input) throws org.antlr.runtime.RecognitionException {
System.out.println("\nbefore new BackQuote* / type = " + input.getClass());
    BackQuoteLanguageLexer lexer = new BackQuoteLanguageLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
System.out.println("host, tokens = " + tokens.toString() + " /fin");
System.out.println("host, tokens list = " + tokens.getTokens().toString());
    BackQuoteLanguageParser parser = new BackQuoteLanguageParser(tokens);
    BackQuoteLanguageParser.backQuoteConstruct_return res = parser.backQuoteConstruct();
System.out.println("before parser.backQuoteConstruct() = " + ((Tree)res.getTree()).toStringTree());
    return (Tree)res.getTree();
  }
  */
}

// start
program :
  block* -> ^(Program block*)
  ;

block :
   hostConstruct -> ^(HostBlock hostConstruct)
  | tomConstruct -> ^(TomBlock tomConstruct)
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
tomConstruct :
  MATCH
  /* and other keywords  */
  ;

//BackQuote
backquoteConstruct :
  BACKQUOTE
//  '`(' -> { getBQAST(input) }
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

MATCH : '%' //match'
  {
System.out.println("\nbefore new Tom*");
    TomLanguageLexer lexer = new TomLanguageLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
System.out.println("host, tokenstream = " + tokens.toString() + " /fin");
System.out.println("host, tokens list = " + tokens.getTokens().toString());
    TomLanguageParser parser = new TomLanguageParser(tokens);
System.out.println("before parser.matchConstruct()");
//    parser.matchConstruct();

    TomLanguageParser.matchConstruct_return res = parser.matchConstruct();
    System.out.println("(host - tom) res.getTree() = " + ((org.antlr.runtime.tree.Tree)res.getTree()).toStringTree() + " (<- should be '(MatchConstruct )')");

//System.out.println("HOST before match channel change, channel = " + $channel);
//    $channel=TOM_CHANNEL;
//System.out.println("HOST after match channel change, channel = " + $channel);
  }
;

BACKQUOTE : '`(' //{getBQAST(input);} ;
  {
System.out.println("\nbefore new BackQuote*");
    BackQuoteLanguageLexer lexer = new BackQuoteLanguageLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
System.out.println("host, tokens = " + tokens.toString() + " /fin");
System.out.println("host, tokens list = " + tokens.getTokens().toString());
    BackQuoteLanguageParser parser = new BackQuoteLanguageParser(tokens);
System.out.println("before parser.backQuoteConstruct()");
//    parser.backQuoteConstruct();

    BackQuoteLanguageParser.backQuoteConstruct_return res = parser.backQuoteConstruct();
System.out.println("(host - bq) res.getTree() = " + ((Tree)res.getTree()).toStringTree() + " (<- should be BQVariable, BQVariableStar, BQUnamedVariable or BQUnamedVariableStar)");

//System.out.println("HOST before backquote channel change, channel = " + $channel);
//    $channel=BACKQUOTE_CHANNEL;
//System.out.println("HOST after backquote channel change, channel = " + $channel);
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

