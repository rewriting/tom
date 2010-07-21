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
  tomTerm a=ARROWLBRACE -> ^(PatternAction tomTerm ^({((TomToken)$a).getTree()}))
  ;

ARROWLBRACE : '-> {' /*(options {greedy=false;} : WS )* LBRACE*/
  {
//    System.out.println("\nbefore new Host*");
//    System.out.println("in arrowlbrace / tom nesting = " + nesting);
    HostLanguageLexer lexer = new HostLanguageLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
//    System.out.println("tom, tokens = " + tokens.toString() + " /fin");
//    System.out.println("tom, tokens list = " + tokens.getTokens().toString());
    HostLanguageParser parser = new HostLanguageParser(tokens);
//    System.out.println("before parser.block()");
    HostLanguageParser.block_return res = parser.block();
//    System.out.println("(tom - host) res.getTree() = " + ((org.antlr.runtime.tree.Tree)res.getTree()).toStringTree() + " ( <-  should be '(*Block )')");
    result = (Tree)res.getTree();
  }
  ;

LBRACE : '{' //{ nesting++; System.out.println("tom nesting++ = " + nesting);}
         ;

RBRACE : '}'
  {
    if ( nesting<=0 ) {
      emit(Token.EOF_TOKEN);
      //System.out.println("exit tom language\n");
    }
    else {
      nesting--;
      //System.out.println("tom nesting-- = " + nesting);
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
