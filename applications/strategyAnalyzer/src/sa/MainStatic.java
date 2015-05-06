package sa;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;
import sa.rule.RuleAdaptor;
import sa.rule.types.*;
import java.util.*;
import java.io.*;
import org.kohsuke.args4j.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

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

    // print current options
    /*
    try {
      Class c = options.getClass();
      Field[] fields = c.getDeclaredFields();
      for(Field f : fields){
        if( !f.getName().startsWith("h") ){
          System.out.print(f.getName() + ":  " );
          if( f.getType() != Class.forName("java.util.List") ){
            System.out.println(f.get(options));
          } else {
            for( Object s : (List)f.get(options) ) {
              System.out.println("\n   "+ s);
            }
          }
        }
      }
    }
    catch (java.lang.ClassNotFoundException ec) {
      System.err.println("No class: " + ec);
    }    catch (java.lang.IllegalAccessException ef) {
      System.err.println("No field: " + ef);
    }
    System.out.println("\n------------------------------------------   ");
      */

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
      //       System.out.println(pretty.toString(expl));
      //       System.out.println("------------------------------------------   ");

      // Transforms Let(name,exp,body) into body[name/exp]
      ExpressionList expandl = Compiler.expand(expl);
      //       System.out.println(expandl);
      //       System.out.println(pretty.toString(expandl));
      //       System.out.println("------------------------------------------   ");

      // Get the list of defined signatures, each of them with a corresponding strategy
      List<Expression> signatures=new ArrayList<Expression>(); 
      List<Strat> strategies=new ArrayList<Strat>(); 
      Map<String,Integer> extractedSignature = Compiler.extractSignaturesAndStrategies(signatures,strategies,expandl);
      Map<String,Integer> generatedSignature = Compiler.generateSignature(extractedSignature);
      //       System.out.println(signatures);
      //       System.out.println(strategies);
      //       System.out.println(extractedSignature);
      //       System.out.println(generatedSignature);
      //       System.out.println("------------------------------------------   ");

      Collection<Rule> generatedRules = new HashSet<Rule>();

      // Uncomment to test previous version (against the new one)
      //       generatedSignature = new HashMap<String,Integer>();
      //       extractedSignature = new HashMap<String,Integer>();
      //       Compiler.compileOLD(generatedRules,extractedSignature,generatedSignature,expandl);

      // Transforms the strategy into a rewrite system
      Compiler.compile(generatedRules,extractedSignature,generatedSignature,strategies);
      //System.out.println(generatedRules );

      if(options.withAP == false) {
         for(Rule r:new HashSet<Rule>(generatedRules)) { // copy of generatedRules
           // add new rules to generatedRules (for each anti-pattern)
           Compiler.expandAntiPattern(generatedRules,r,extractedSignature,generatedSignature);
         }
      }
      
      // if we don't expand the anti-patterns then we should keep the at-annotations as well
      // otherwise output is strange
      if(options.withAT == false && options.withAP == false) {
        generatedRules = Compiler.expandAt(generatedRules);
      }
      
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
        boolean innermost = false;
        outputfile.println( pretty.generateAprove(orderedRules,extractedSignature,innermost) );
      }
    } catch (Exception e) {
      System.err.println("exception: " + e);
      e.printStackTrace();
      return;
    }
  }


  private static  class MyRuleComparator implements Comparator<Rule> {
    public int compare(Rule r1, Rule r2) {
      return r2.getlhs().toString().compareTo(r1.getlhs().toString());
    }
  }
}
