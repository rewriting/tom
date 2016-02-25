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
  %include { rule/Rule.tom }
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

    // check options
    if(Main.options.metalevel && Main.options.withType) {
      System.err.println("options metalevel and withType are incompatible");
      return;
    }

    try {
      InputStream fileinput = System.in;
      if(options.in != null) {
        fileinput = new FileInputStream(options.in);
      }

      RuleLexer lexer = new RuleLexer(new ANTLRInputStream(fileinput));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      RuleParser ruleParser = new RuleParser(tokens);
      Tree tree = (Tree) ruleParser.program().getTree();
      Program program = (Program) RuleAdaptor.getTerm(tree);
      System.out.println(program);

      /*
       * Compilation of the strategy section
       */
      Compiler compiler = Compiler.getInstance();
      compiler.setProgram(program);

      // Transforms the strategy into a rewrite system
      //   get the TRS for the strategy named strategyName
      Set<String> strategyNames = compiler.collectConstantStrategyName(program);
      strategyNames.add("mainStrat");

      RuleList generatedRules = compiler.compileStrategy(strategyNames);
      Signature extractedSignature = compiler.getExtractedSignature();
      Signature generatedSignature = compiler.getGeneratedSignature();
      System.out.println(extractedSignature);
      System.out.println(generatedSignature);
      System.out.println(generatedRules);
      System.out.println(strategyNames.toString());

      //System.out.println("compileStrategy: generatedRules = " + Pretty.toString(generatedRules));

      assert Tools.isLhsLinear(generatedRules);
      // transform the LINEAR TRS: compile Aps and remove ATs
      RuleCompiler ruleCompiler = new RuleCompiler(extractedSignature,generatedSignature);
      if(options.withAP == false) {
        generatedRules = ruleCompiler.expandAntiPatterns(generatedRules);
        //System.out.println("expandAntiPatterns: generatedRules = " + Pretty.toString(generatedRules));
      }
      // if we don't expand the anti-patterns then we should keep the at-annotations as well
      // otherwise output is strange
      if(options.withAT == false && options.withAP == false) {
        generatedRules = ruleCompiler.expandAt(generatedRules);
        //System.out.println("expandAt: generatedRules = " + Pretty.toString(generatedRules));
      }
      // refresh the signatures (presently no modifications)
      extractedSignature = ruleCompiler.getExtractedSignature();
      generatedSignature = ruleCompiler.getGeneratedSignature();

      if(options.withType) {
        //System.out.println("before typing: generatedRules = " + Pretty.toString(generatedRules));
        TypeCompiler typeCompiler = new TypeCompiler(extractedSignature);
        typeCompiler.typeRules(generatedRules);
        generatedRules = typeCompiler.getGeneratedRules();
        generatedSignature = typeCompiler.getTypedSignature();
      }

      /*
       * Post treatment
       */
      if(Main.options.pattern && Main.options.ordered) { // && Main.options.withType) {
        System.out.println("after compilation");
        System.out.println("generatedRules = " + Pretty.toString(generatedRules));
        // run the Pattern transformation here
        //for(String name:generatedSignature.getSymbols()) {
          //System.out.println("symbol: " + name + " function: " + generatedSignature.isFunction(name) + " internal: " + generatedSignature.isInternal(name));
        //}

        Trs otrs = RewriteSystem.trsRule(`Otrs(generatedRules),generatedSignature);
        generatedRules = otrs.getlist();
      }

      /*
       * Handle the TRS part of a specification
       */
      Trs trs = program.gettrs();
      trs = RewriteSystem.transformNLOTRSintoLOTRS(trs,generatedSignature);
      trs = RewriteSystem.trsRule(trs,generatedSignature);
      for(Rule r:trs.getlist().getCollectionConcRule()) {
        // System.out.println(Pretty.toString(r));
        generatedRules = ((sa.rule.types.rulelist.ConcRule)generatedRules).append(r);
      }


      /*
       * Generate output
       */
      PrintStream outputfile = System.out;
      if(options.out != null) {
        if(options.directory != null) {
          outputfile = new PrintStream(options.directory + "/" + options.out);
        } else {
          outputfile = new PrintStream(options.out);
        }
      }
      PrintStream tomoutputfile = System.out;
      if(options.classname != null) {
        if(options.directory != null) {
          tomoutputfile = new PrintStream(options.directory + "/" + options.classname + ".t");
        } else {
          tomoutputfile = new PrintStream(options.classname + ".t");
        }
      }

      if(options.classname != null) {
        tomoutputfile.println( Pretty.generateTom(strategyNames, generatedRules, extractedSignature, generatedSignature) );
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
