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
      if(options.in != null) {
        fileinput = new FileInputStream(options.in);
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

      System.out.println("Extracted signature: " + compiler.getExtractedSignature());
      System.out.println("Generated signature: " + compiler.getGeneratedSignature());

      // Transforms the strategy into a rewrite system
      //   get the TRS for the strategy named strategyName
      Set<String> strategyNames = compiler.collectConstantStrategyName(program);
      strategyNames.add("mainStrat");

      //       RuleList generatedRules = compiler.compileStrategy(strategyNames);
      RuleList generatedRules = `ConcRule();
      Signature extractedSignature = compiler.getExtractedSignature();
      Signature generatedSignature = compiler.getGeneratedSignature();
      // System.out.println("Extracted SIG = " + extractedSignature);
        

      /////  STRATEGIES should be handeled here ///////


//       assert Property.isLhsLinear(generatedRules);
//       // transform the LINEAR TRS: compile Aps and remove ATs
//       RuleCompiler ruleCompiler = new RuleCompiler(extractedSignature,generatedSignature);
//       if(options.withAP == false) {
//         generatedRules = ruleCompiler.expandAntiPatterns(generatedRules);
//         //System.out.println("expandAntiPatterns: generatedRules = " + Pretty.toString(generatedRules));
//       }
//       // if we don't expand the anti-patterns then we should keep the at-annotations as well
//       // otherwise output is strange
//       if(options.withAT == false && options.withAP == false) {
//         generatedRules = ruleCompiler.expandAt(generatedRules);
//         //System.out.println("expandAt: generatedRules = " + Pretty.toString(generatedRules));
//       }
//       // refresh the signatures (presently no modifications)
//       extractedSignature = ruleCompiler.getExtractedSignature();
//       generatedSignature = ruleCompiler.getGeneratedSignature();

//       if(options.withType) {
//         //System.out.println("before typing: generatedRules = " + Pretty.toString(generatedRules));
//         TypeCompiler typeCompiler = new TypeCompiler(extractedSignature);
//         typeCompiler.typeRules(generatedRules);
//         generatedRules = typeCompiler.getGeneratedRules();
//         generatedSignature = typeCompiler.getTypedSignature();
//       }

//       /*
//        * Post treatment
//        */
//       if(Main.options.pattern && Main.options.ordered) {
//         System.out.println("after compilation");
//         System.out.println("generatedRules = " + Pretty.toString(generatedRules));
//         // run the Pattern transformation here
//         //for(String name:generatedSignature.getSymbols()) {
//           //System.out.println("symbol: " + name + " function: " + generatedSignature.isFunction(name) + " internal: " + generatedSignature.isInternal(name));
//         //}
      
//         Trs otrs = RewriteSystem.trsRule(`Otrs(generatedRules),generatedSignature);
//         generatedRules = otrs.getlist();
//       }

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
