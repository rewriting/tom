package sa;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;                                                                                                    
import sa.rule.types.*;
import java.util.*;
import java.io.*;
import org.kohsuke.args4j.*;

public class CTRS {
  %include { rule/Rule.tom }
  protected static Options options = new Options();

  public static void main(String[] args) {
    CmdLineParser optionParser = new CmdLineParser(Main.options);
    optionParser.setUsageWidth(80);
    try {
      // parse the arguments.
      optionParser.parseArgument(args);
      //if( Main.options.arguments.isEmpty() ) {
      if( Main.options.help || Main.options.h ) {
        throw new CmdLineException("Help");
      }
    } catch( CmdLineException e ) {
      // if there's a problem in the command line,
      // you'll get this exception. this will report
      // an error message.
      System.err.println(e.getMessage());
      System.err.println("java CTRS [options...] arguments ...");
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
      if(Main.options.in != null) {
        fileinput = new FileInputStream(Main.options.in);
      }

      // ANTLR4
      ANTLRInputStream input = new ANTLRInputStream(fileinput);
      ProgramSyntaxLexer lexer = new ProgramSyntaxLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      ProgramSyntaxParser parser = new ProgramSyntaxParser(tokens);
      parser.setBuildParseTree(true); // tell ANTLR to build a parse tree
      ParseTree tree = parser.program(); // parse

      ParseTreeWalker walker = new ParseTreeWalker();
      AstBuilder astBuilder = new AstBuilder(); 
      walker.walk(astBuilder, tree);
      Program program = (Program) astBuilder.getValue(tree);

      //System.out.println("program: " + program);

      /*
       * Compilation of the strategy section
       */
      Compiler compiler = Compiler.getInstance();
      compiler.setProgram(program);

      // Transforms the strategy into a rewrite system
      //   get the TRS for the strategy named strategyName
      Set<String> strategyNames = compiler.collectConstantStrategyName(program);
      strategyNames.add("mainStrat");

      //       RuleList generatedRules = compiler.compileStrategy(strategyNames);
      RuleList generatedRules = `ConcRule();
      Signature extractedSignature = compiler.getExtractedSignature();
      Signature generatedSignature = compiler.getGeneratedSignature();
      if(Main.options.debug) {
        System.out.println("Extracted SIG = " + extractedSignature);
        System.out.println("Generated SIG = " + generatedSignature);
      }

      /////  STRATEGIES could be handeled here ///////

      if(Main.options.withType) {
        TypeCompiler typeCompiler = new TypeCompiler(extractedSignature);
        generatedSignature = typeCompiler.getTypedSignature();
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
      if(Main.options.out != null) {
        if(Main.options.directory != null) {
          outputfile = new PrintStream(Main.options.directory + "/" + Main.options.out);
        } else {
          outputfile = new PrintStream(Main.options.out);
        }
      }
      PrintStream tomoutputfile = System.out;
      if(Main.options.classname != null) {
        if(Main.options.directory != null) {
          tomoutputfile = new PrintStream(Main.options.directory + "/" + Main.options.classname + ".t");
        } else {
          tomoutputfile = new PrintStream(Main.options.classname + ".t");
        }
      }

      if(Main.options.classname != null) {
        tomoutputfile.println( Pretty.generateTom(strategyNames, generatedRules, extractedSignature, generatedSignature) );
      } 

      if(Main.options.aprove) {
        boolean innermost = false;
        outputfile.println( Pretty.generateAprove(generatedRules,innermost) );
      }
      if(Main.options.timbuk) {
        outputfile.println( Pretty.generateTimbuk(generatedRules,generatedSignature) );
      }
    } catch (Exception e) {
      System.err.println("exception: " + e);
      e.printStackTrace();
      return;
    }
  }


}
