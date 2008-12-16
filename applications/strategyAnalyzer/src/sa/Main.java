package sa;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;
import sa.rule.RuleAdaptor;
import sa.rule.types.*;
public class Main {

  public static void main(String[] args) {
    try {
      RuleLexer lexer = new RuleLexer(new ANTLRInputStream(System.in));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      RuleParser parser = new RuleParser(tokens);
      // Parse the input expression
      Tree b = (Tree) parser.expressionlist().getTree();
      ExpressionList expl = (ExpressionList) RuleAdaptor.getTerm(b);
      System.out.println("Result = " + expl);
      System.out.println( Pretty.toString(expl) );

      ExpressionList expandl = Compiler.expand(expl);
      System.out.println( Pretty.toString(expandl) );

      Compiler.compile(expandl);
    } catch (Exception e) {
      System.err.println("exception: " + e);
      return;
    }
  }
}
