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
      //System.out.println("Result = " + expl);
      //System.out.println( Pretty.toString(expl) );

      ExpressionList expandl = Compiler.expand(expl);
      //System.out.println( Pretty.toString(expandl) );

      Map<String,Integer> sig = new HashMap<String,Integer>();
      Map<String,Integer> origsig = new HashMap<String,Integer>();
      List<Rule> bagOfRule = new ArrayList<Rule>();
      Compiler.compile(bagOfRule,origsig,sig,expandl);

      String classname = "Test";
      Collections.sort(bagOfRule, new MyRuleComparator());
//       Collections.sort(bagOfRule, new BottomRuleComparator());
 
      List<Rule> ruleList = new ArrayList<Rule>(Pretty.generateRulesWithoutAntiPatterns(bagOfRule,origsig));
      Collections.sort(ruleList, new MyRuleComparator());
//       Collections.sort(ruleList, new BottomRuleComparator());

//       System.out.println(Pretty.generate(bagOfRule,sig,classname) );
//       System.out.println(Pretty.generate(ruleList,sig,classname) );

//       System.out.println( Pretty.generateAprove(bagOfRule,origsig,false) );
      System.out.println( Pretty.generateAprove(ruleList,origsig,true) );
    } catch (Exception e) {
      System.err.println("exception: " + e);
      return;
    }
  }
}
