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

      // TODO: remove the if branch when option newparser removed
      RuleLexer lexerNEW = new RuleLexer(new ANTLRInputStream(fileinput));
      CommonTokenStream tokensNEW = new CommonTokenStream(lexerNEW);
      RuleParser ruleParserNEW = new RuleParser(tokensNEW);
      Tree bNEW = (Tree) ruleParserNEW.program().getTree();
      Program t = (Program) RuleAdaptor.getTerm(bNEW);

      Compiler compiler = Compiler.getInstance();
      compiler.setProgram(t);

      // Transforms the strategy into a rewrite system
      //   get the TRS for the strategy named strategyName
      String strategyName="mainStrat";
      List<Rule> generatedRules = compiler.compileStrategy(strategyName);
      Signature extractedSignature = compiler.getExtractedSignature();
      Signature generatedSignature = compiler.getGeneratedSignature();

      System.out.println("EXT SIG = " + extractedSignature);
      System.out.println("GEN SIG = " + generatedSignature);

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

      //         System.out.println("gSIG = " + generatedSignature);


      if(options.type != null) {
        // TEST (flatten types)
        TypeCompiler typeCompiler = new TypeCompiler(extractedSignature,generatedRules);
        //pem typeCompiler.flattenSignature();

        // refresh the signatures (presently no modifications)
        extractedSignature = typeCompiler.getExtractedSignature();
        Signature typedSignature = typeCompiler.getTypedSignature();

        typeCompiler.typeRules();


        generatedRules = typeCompiler.getGeneratedRules();
        generatedSignature = typedSignature;

        //         System.out.println("gSIG = " + generatedSignature);


        // TEST
        //         List<Rule> newRules = compiler.specialize("rule6", "T", generatedRules);  
      }


      PrintStream outputfile = System.out;
      if(options.out != null) {
        outputfile = new PrintStream(options.out);
      }
      PrintStream tomoutputfile = System.out;
      if(options.classname != null) {
        tomoutputfile = new PrintStream(options.classname+".t");
      }

      if(options.classname != null) {
        tomoutputfile.println( Pretty.generateTom(strategyName,options.type,generatedRules,generatedSignature,options.classname) );
      } 

      if(options.aprove) {
        boolean innermost = false;
        outputfile.println( Pretty.generateAprove(generatedRules,innermost) );
      }
      if(options.timbuk) {
        outputfile.println( Pretty.generateTimbuk(generatedRules,generatedSignature) );
      }
    } catch (Exception e) {
      System.err.println("exception: " + e);
      e.printStackTrace();
      return;
    }
  }


}
