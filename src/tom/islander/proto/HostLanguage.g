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
  public static int nesting = 0;
  public Tree result;

  // override standard token emission
  public Token emit() {
    TomToken t = new TomToken(input, state.type, state.channel,
        state.tokenStartCharIndex, getCharIndex()-1,result);
    t.setLine(state.tokenStartLine);
    t.setText(state.text);
    t.setCharPositionInLine(state.tokenStartCharPositionInLine);
    t.setTree(result);
    emit(t);
    result = null;
    return t;
  }
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
  | '{' block '}' -> block

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
  m=MATCH -> ^({((TomToken)$m).getTree()})
  /* and other keywords  */
  ;

//BackQuote
backquoteConstruct :
  b=BACKQUOTE -> ^({((TomToken)$b).getTree()})
  ;

//Lexer
ID  : ('a'..'z'|'A'..'Z')+ ;

INT : ('0'..'9')+ ;

LBRACE : '{' { nesting++; } //{System.out.println("host nesting++ = " + nesting);}
 ;

RBRACE : '}'
  {
    if ( nesting<=0 ) {
      emit(Token.EOF_TOKEN);
      //System.out.println("exit embedded hostlanguage\n");
    }
    else {
      nesting--;
      //System.out.println("host nesting-- = " + nesting);
    }
  }
  ;

MATCH : '%match'
  {
//    System.out.println("\nbefore new Tom* / type de input = " + input.getClass().toString());
//System.out.println("\nbefore new Tom*");
    TomLanguageLexer lexer = new TomLanguageLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
//System.out.println("host, tokenstream = " + tokens.toString() + " /fin");
//System.out.println("host, tokens list = " + tokens.getTokens().toString());
    TomLanguageParser parser = new TomLanguageParser(tokens);
//System.out.println("before parser.matchConstruct()");
    TomLanguageParser.matchConstruct_return res = parser.matchConstruct();
    result = (Tree)res.getTree();
  }
  ;

BACKQUOTE : '`('
  {
//System.out.println("\nbefore new BackQuote*");
    BackQuoteLanguageLexer lexer = new BackQuoteLanguageLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
//System.out.println("host, tokens = " + tokens.toString() + " /fin");
//System.out.println("host, tokens list = " + tokens.getTokens().toString());
    BackQuoteLanguageParser parser = new BackQuoteLanguageParser(tokens);
//System.out.println("before parser.backQuoteConstruct()");
    BackQuoteLanguageParser.backQuoteConstruct_return res = parser.backQuoteConstruct();
//    System.out.println("(host - bq) res.getTree() = " + ((Tree)res.getTree()).toStringTree() + " (<- should be BQVariable, BQVariableStar, BQUnamedVariable or BQUnamedVariableStar)");
    result = (Tree)res.getTree();
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

