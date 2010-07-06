grammar TomLanguage;

options {
  backtrack=true;
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=TomTokens;
}

@header{
//  package islander.proto;
  import org.antlr.runtime.tree.*;
  import org.antlr.runtime.*;
}

@lexer::header{
//  package islander.proto;
  import org.antlr.runtime.tree.*;
}

@parser::members{
  public static Tree intermediateResult; //subTree

}

@lexer::members{
  public static int tnesting = 0;
}

matchConstruct :
  /*'match'*/ '(' matchArguments ')' LBRACE patternActionList RBRACE -> ^(MatchConstruct matchArguments patternActionList)
/*  | '{' constraintActionList '}' -> ^()*/
  ;

matchArguments :
  tomTerm (',' tomTerm)* -> ^(TomTermList tomTerm+)
  ;

tomTerm :
  type=ID name=ID -> ^(TomVariableType $name $type)
  | name=ID -> ^(TomVariable $name)
  ;

patternActionList :
  (patternAction)+ -> ^(PatternActionList patternAction+)
  ;

patternAction :
  tomTerm ARROWLBRACE /* here we call the host parser */ -> ^(PatternAction tomTerm ^({intermediateResult}))
  ;

//ARROW : '->' ;

ARROWLBRACE : '-> {' /*(options {greedy=false;} : WS )* LBRACE*/
  {
    System.out.println("\nbefore new Host*");
    System.out.println("in arrowlbrace / tom tnesting = " + tnesting);
    HostLanguageLexer lexer = new HostLanguageLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    System.out.println("tom, tokens = " + tokens.toString() + " /fin");
    System.out.println("tom, tokens list = " + tokens.getTokens().toString());
    HostLanguageParser parser = new HostLanguageParser(tokens);
    System.out.println("before parser.block()");
    HostLanguageParser.block_return res = parser.block();
    System.out.println("(tom - host) res.getTree() = " + ((org.antlr.runtime.tree.Tree)res.getTree()).toStringTree() + " ( <-  should be '(*Block )')");
    TomLanguageParser.intermediateResult = (Tree)res.getTree();
  }
  ;

LBRACE : '{' //{ tnesting++; System.out.println("tom tnesting++ = " + tnesting);}
         ;

RBRACE : '}'
  {
    if ( tnesting<=0 ) {
      emit(Token.EOF_TOKEN);
      //System.out.println("exit tom language\n");
    }
    else {
      tnesting--;
      //System.out.println("tom tnesting-- = " + tnesting);
    }
  }
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
