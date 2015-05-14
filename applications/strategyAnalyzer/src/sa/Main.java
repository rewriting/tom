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

    try {
      InputStream fileinput = System.in;
      if(options.in != null) {
        fileinput = new FileInputStream(options.in);
      }

      // TEMPORARY
      InputStream newsyntax = new FileInputStream("../examples/example1.ns");

      if(options.newparser) {
        System.out.println("NEW PARSER");
        // Parse the input expression and build an AST
        RuleLexer lexer = new RuleLexer(new ANTLRInputStream(fileinput));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RuleParser ruleParser = new RuleParser(tokens);
        Tree b = (Tree) ruleParser.program().getTree();
        Program t = (Program) RuleAdaptor.getTerm(b);


        System.out.println(t);
        //System.out.println(pretty.toString(expl));
        System.out.println("------------------------------------------   ");

        Compiler compiler = Compiler.getInstance();
        System.out.println("SIG = " +compiler.setProgram(t));

        
        //String strategyName="strat0";
        //this.generatedTRSs.put(strategyName,new ArrayList<Rule>());
        //topName = this.compileStrat(strategyName,this.strategies.get(name),this.generatedTRSs.get(name));

        //StratDecl sd = Tools.getStratDecl("obu",t);
        //System.out.println("sd = " + sd);
        //Expression si = compiler.instantiateStrategy(sd, compiler.getStratR());
        //System.out.println("si = " + si);

        System.out.println("main = " + Tools.getStratDecl("main", t));
        Expression expand = compiler.expandStrategy("main");
        System.out.println("expanded version = " + expand);




      } else {

        // TEMPORARY
        RuleLexer lexerNEW = new RuleLexer(new ANTLRInputStream(newsyntax));
        CommonTokenStream tokensNEW = new CommonTokenStream(lexerNEW);
        RuleParser ruleParserNEW = new RuleParser(tokensNEW);
        Tree bNEW = (Tree) ruleParserNEW.program().getTree();
        Program t = (Program) RuleAdaptor.getTerm(bNEW);

        Compiler compiler = Compiler.getInstance();
        System.out.println("SIG NEW = " +compiler.setProgram(t));
        // END TEMPORARY

        // Parse the input expression and build an AST
        RuleLexer lexer = new RuleLexer(new ANTLRInputStream(fileinput));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RuleParser ruleParser = new RuleParser(tokens);
        Tree b = (Tree) ruleParser.expressionlist().getTree();
        ExpressionList expl = (ExpressionList) RuleAdaptor.getTerm(b);
        //       System.out.println(pretty.toString(expl));
        //       System.out.println("------------------------------------------   ");

        //         Compiler compiler = Compiler.getInstance();
        compiler.setSignature(expl);

        // Transforms the strategy into a rewrite system
        //   get the TRS for the strategy named strategyName
        String strategyName="strat0";
        List<Rule> generatedRules = compiler.compile(strategyName);
        Signature extractedSignature = compiler.getExtractedSignature();
        Signature generatedSignature = compiler.getGeneratedSignature();

        // transform the LINEAR TRS: compile Aps and remove ATs
        RuleCompiler ruleCompiler = new RuleCompiler(extractedSignature,generatedSignature);
        if(options.withAP == false) {
          generatedRules = ruleCompiler.expandAntiPatterns(generatedRules);
        }      
        // if we don't expand the anti-patterns then we should keep the at-annotations as well
        // otherwise output is strange
        if(options.withAT == false && options.withAP == false) {
          generatedRules = ruleCompiler.expandAt(generatedRules);
        }
        // refresh the signatures (presently no modifications)
        extractedSignature = ruleCompiler.getExtractedSignature();
        generatedSignature = ruleCompiler.getGeneratedSignature();

        //         System.out.println("COMPILED STRATEGIES: "+Compiler.getInstance().getStrategyNames());
        
        PrintStream outputfile = System.out;
        if(options.out != null) {
          outputfile = new PrintStream(options.out);
        }
        PrintStream tomoutputfile = System.out;
        if(options.classname != null) {
          tomoutputfile = new PrintStream(options.classname+".t");
        }

        if(options.classname != null) {
          tomoutputfile.println( Pretty.generateTom(strategyName,generatedRules,generatedSignature,options.classname) );
        } 
        if(options.aprove) {
          boolean innermost = false;
          outputfile.println( Pretty.generateAprove(generatedRules,extractedSignature,innermost) );
        }
      }
    } catch (Exception e) {
      System.err.println("exception: " + e);
      e.printStackTrace();
      return;
    }
  }

}
