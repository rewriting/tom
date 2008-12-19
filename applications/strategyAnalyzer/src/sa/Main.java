package sa;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;
import sa.rule.RuleAdaptor;
import sa.rule.types.*;
import java.util.*;

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

      Map<String,Integer> sig = new HashMap<String,Integer>();
      Map<String,Integer> origsig = new HashMap<String,Integer>();
      Collection<Rule> bag = new ArrayList<Rule>();
      Compiler.compile(bag,origsig,sig,expandl);

      String classname = "Test";
      System.out.println( Pretty.generate(bag,sig,classname) );
    } catch (Exception e) {
      System.err.println("exception: " + e);
      return;
    }
  }
}
