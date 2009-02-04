package sa;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;
import sa.rule.RuleAdaptor;
import sa.rule.types.*;
import java.util.*;
import java.io.*;
import org.kohsuke.args4j.*;

public class Main {
  protected static Options options = new Options();

  public static void main(String[] args) {
    Pretty pretty = new Pretty();

    CmdLineParser optionParser = new CmdLineParser(options);
    optionParser.setUsageWidth(80);
    try {
      // parse the arguments.
      optionParser.parseArgument(args);
      //if( options.arguments.isEmpty() ) {
      if( options.help || options.h ) {
        throw new CmdLineException("Help");
      }
    } catch( CmdLineException e ) {
      // if there's a problem in the command line,
      // you'll get this exception. this will report
      // an error message.
      System.err.println(e.getMessage());
      System.err.println("java Main [options...] arguments ...");
      // print the list of available options
      optionParser.printUsage(System.err);
      System.err.println();
      return;
    }

    // this will redirect the output to the specified output
    System.out.println(options.out);

    System.out.println("withAP: " + options.withAP);
    System.out.println("aprove: " + options.aprove);
    System.out.println("classname: " + options.classname);
    System.out.println("out: " + options.out);
    System.out.println("in: " + options.in);

    System.out.println("other arguments are:");
    for( String s : options.arguments ) {
      System.out.println(s);
    }

    try {
      InputStream fileinput = System.in;
      if(options.in != null) {
        fileinput = new FileInputStream(options.in);
      }

      // Parse the input expression and build an AST
      RuleLexer lexer = new RuleLexer(new ANTLRInputStream(fileinput));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      RuleParser ruleParser = new RuleParser(tokens);
      Tree b = (Tree) ruleParser.expressionlist().getTree();
      ExpressionList expl = (ExpressionList) RuleAdaptor.getTerm(b);

      ExpressionList expandl = Compiler.expand(expl);

      Map<String,Integer> generatedSignature = new HashMap<String,Integer>();
      Map<String,Integer> extractedSignature = new HashMap<String,Integer>();
      Collection<Rule> generatedRules = new HashSet<Rule>();

      //       System.out.println(generatedRules);

      // Transforms the strategy into a rewrite system
      Compiler.compile(generatedRules,extractedSignature,generatedSignature,expandl);

      if(options.withAP == false) {
//         Collection<Rule> tmp = new HashSet<Rule>();
//         for(Rule r:generatedRules) { 
//           // add new rules to generatedRules (for each anti-pattern)
//          Compiler.expandAntiPattern2(tmp,r,extractedSignature);
//         }
//         generatedRules = tmp;

        generatedRules = Compiler.expandAntiPatterns(generatedRules,extractedSignature);
      }
      
//       System.out.println("RULES without ANTI");
//       for(Rule r:generatedRules) { 
//         System.out.println(pretty.toString(r));
//       }
      
      if(options.withAT == false) {
        generatedRules = Compiler.expandAt(generatedRules);
      }

//       System.out.println("RULES without AT");
//       for(Rule r:generatedRules) { 
//         System.out.println(pretty.toString(r));
//       }
      
      List<Rule> orderedRules = new ArrayList<Rule>(generatedRules);
      Collections.sort(orderedRules, new MyRuleComparator());

      PrintStream outputfile = System.out;
      if(options.out != null) {
        outputfile = new PrintStream(options.out);
      }
      PrintStream tomoutputfile = System.out;
      if(options.classname != null) {
        tomoutputfile = new PrintStream(options.classname+".t");
      }

      if(options.classname != null) {
        tomoutputfile.println( pretty.generateTom(orderedRules,generatedSignature,options.classname) );
      } 
      if(options.aprove) {
        outputfile.println( pretty.generateAprove(orderedRules,extractedSignature,true) );
      }
    } catch (Exception e) {
      System.err.println("exception: " + e);
      e.printStackTrace();
      return;
    }
  }
}
