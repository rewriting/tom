/* Parse conditions */
grammar Cond;
options {
  output=AST;
}

tokens {
  WfgNode;
  Activity;
  ConcWfg;
  EmptyWfg;
  And;
  Or;
  Xor;
  Cond;
  Label;
  NoCond;

}

@header {
  package bpel;
  import bpel.wfg.WfgTree;
  import bpel.wfg.WfgAdaptor;
  import bpel.wfg.types.*;
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
      parser.setTreeAdaptor(new WfgAdaptor());

      // Parse the input expression
      WfgTree tree = (WfgTree) parser.cond().getTree();

      return (Condition) tree.getTerm();
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

