package sa;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;
import sa.rule.RuleAdaptor;
import sa.rule.types.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
import org.kohsuke.args4j.*;

public class Main {

    public static void main(String[] args) {
      Options options = new Options();

      CmdLineParser optionParser = new CmdLineParser(options);
      optionParser.setUsageWidth(80);
      try {
        // parse the arguments.
        optionParser.parseArgument(args);
        if( options.arguments.isEmpty() ) {
          throw new CmdLineException("No argument is given");
        }
      } catch( CmdLineException e ) {
        // if there's a problem in the command line,
        // you'll get this exception. this will report
        // an error message.
        System.err.println(e.getMessage());
        System.err.println("java Main [options...] arguments...");
        // print the list of available options
        optionParser.printUsage(System.err);
        System.err.println();
        return;
      }

      // this will redirect the output to the specified output
      System.out.println(options.out);

      System.out.println("withAP: " + options.withAP);
      System.out.println("aprove: " + options.aprove);
      System.out.println("out: " + options.out);
      System.out.println("in: " + options.in);

      System.out.println("other arguments are:");
      for( String s : options.arguments ) {
        System.out.println(s);
      }

      try {
        RuleLexer lexer = new RuleLexer(new ANTLRInputStream(System.in));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RuleParser ruleParser = new RuleParser(tokens);
        // Parse the input expression
        Tree b = (Tree) ruleParser.expressionlist().getTree();
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
