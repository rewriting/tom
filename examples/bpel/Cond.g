/* Parse conditions */
grammar Cond;
options {
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=WfgTokens;
}

@header {
  package bpel;
  import bpel.wfg.types.*;
  import bpel.wfg.WfgAdaptor;
  import org.antlr.runtime.tree.Tree;
}
@lexer::header {
  package bpel;
}

@members {
  public static Condition parse(String cond) {
    try {
      CondLexer lexer = new CondLexer(new ANTLRStringStream(cond));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      CondParser parser = new CondParser(tokens);

      // Parse the input expression
      Tree tree = (Tree) parser.cond().getTree();
      return (Condition) WfgAdaptor.getTerm(tree);
    } catch(Exception e ) {
      throw new RuntimeException(e);
    }
  }
}

cond :
  andcond (OR andcond)* -> ^(Or (andcond)*);

andcond :
  atom (AND atom)* -> ^(And (atom)*);

atom :
  ID -> ^(Label ID)
  | LPAR cond RPAR -> ^(cond)
  ;

WS : (' '
    | '\t'
    | '\n'
    | '\r')
    { $channel=HIDDEN; }
  ;

LPAR : '(' ;
RPAR : ')' ;
OR : 'OR' ;
AND : 'AND' ;
ID : ('A'..'Z' |'a'..'z' |'1'..'9')+ ;

