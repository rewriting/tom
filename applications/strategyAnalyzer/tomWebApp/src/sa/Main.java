package sa;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.tree.Tree;
import sa.rule.RuleAdaptor;
import sa.rule.types.*;
import java.util.*;
import java.io.*;

public final class Main {
  private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_StratDecl(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDecl(Object t) {return  (t instanceof sa.rule.types.StratDecl) ;}private static boolean tom_equal_term_Field(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Field(Object t) {return  (t instanceof sa.rule.types.Field) ;}private static boolean tom_equal_term_ParamList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ParamList(Object t) {return  (t instanceof sa.rule.types.ParamList) ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomType(Object t) {return  (t instanceof sa.rule.types.GomType) ;}private static boolean tom_equal_term_Strat(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Strat(Object t) {return  (t instanceof sa.rule.types.Strat) ;}private static boolean tom_equal_term_StratDeclList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDeclList(Object t) {return  (t instanceof sa.rule.types.StratDeclList) ;}private static boolean tom_equal_term_TypeEnvironment(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeEnvironment(Object t) {return  (t instanceof sa.rule.types.TypeEnvironment) ;}private static boolean tom_equal_term_Param(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Param(Object t) {return  (t instanceof sa.rule.types.Param) ;}private static boolean tom_equal_term_AddList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AddList(Object t) {return  (t instanceof sa.rule.types.AddList) ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) {return  (t instanceof sa.rule.types.GomTypeList) ;}private static boolean tom_equal_term_RuleList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RuleList(Object t) {return  (t instanceof sa.rule.types.RuleList) ;}private static boolean tom_equal_term_Term(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Term(Object t) {return  (t instanceof sa.rule.types.Term) ;}private static boolean tom_equal_term_Condition(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Condition(Object t) {return  (t instanceof sa.rule.types.Condition) ;}private static boolean tom_equal_term_TermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TermList(Object t) {return  (t instanceof sa.rule.types.TermList) ;}private static boolean tom_equal_term_StratList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratList(Object t) {return  (t instanceof sa.rule.types.StratList) ;}private static boolean tom_equal_term_Trs(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Trs(Object t) {return  (t instanceof sa.rule.types.Trs) ;}private static boolean tom_equal_term_Rule(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Rule(Object t) {return  (t instanceof sa.rule.types.Rule) ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_FieldList(Object t) {return  (t instanceof sa.rule.types.FieldList) ;}private static boolean tom_equal_term_AlternativeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AlternativeList(Object t) {return  (t instanceof sa.rule.types.AlternativeList) ;}private static boolean tom_equal_term_Symbol(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Symbol(Object t) {return  (t instanceof sa.rule.types.Symbol) ;}private static boolean tom_equal_term_Alternative(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Alternative(Object t) {return  (t instanceof sa.rule.types.Alternative) ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ProductionList(Object t) {return  (t instanceof sa.rule.types.ProductionList) ;}private static boolean tom_equal_term_Production(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Production(Object t) {return  (t instanceof sa.rule.types.Production) ;}private static boolean tom_equal_term_Program(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Program(Object t) {return  (t instanceof sa.rule.types.Program) ;}private static  sa.rule.types.Trs  tom_make_Otrs( sa.rule.types.RuleList  t0) { return  sa.rule.types.trs.Otrs.make(t0) ;}
  public final static Options options = new Options();
  private String input;
  public static String output;

  public Main(String in) {
    this.input = in;
    output = "";
  }

  public String getOutput() {

    Pretty pretty = new Pretty();
    if(Main.options.metalevel && Main.options.withType) {
      //System.err.println("options metalevel and withType are incompatible");
      return "options metalevel and withType are incompatible";
    }

    try {

      RuleLexer lexer = new RuleLexer(new ANTLRStringStream(this.input));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      RuleParser ruleParser = new RuleParser(tokens);
      Tree tree = (Tree) ruleParser.program().getTree();
      Program program = (Program) RuleAdaptor.getTerm(tree);

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

      assert Tools.isLhsLinear(generatedRules);
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

       if(options.withType) {
        TypeCompiler typeCompiler = new TypeCompiler(extractedSignature);
        typeCompiler.typeRules(generatedRules);
        generatedRules = typeCompiler.getGeneratedRules();
        generatedSignature = typeCompiler.getTypedSignature();
      }

      /*
       * Post treatment
       */
       if(Main.options.pattern && Main.options.ordered && Main.options.withType) {
        System.out.println("after compilation");
        System.out.println("generatedRules = " + Pretty.toString(generatedRules));
        // run the Pattern transformation here
        for(String name:generatedSignature.getSymbols()) {
          System.out.println("symbol: " + name + " function: " + generatedSignature.isFunction(name) + " internal: " + generatedSignature.isInternal(name));
        }

        Trs otrs = RewriteSystem.trsRule(tom_make_Otrs(generatedRules),generatedSignature);
        generatedRules = otrs.getlist();
    /*    for(Rule r:res.getCollectionConcRule()) {
          System.out.println(Pretty.toString(r));
        }*/
        //System.out.println("size = " + res.length());

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

      /*PrintStream outputfile = System.out;
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
      }*/
      if(options.classname != null) {
        output +=  Pretty.generateTom(strategyNames, generatedRules, extractedSignature, generatedSignature);
      }

      if(options.aprove) {
        boolean innermost = false;
        output = "";
        output += Pretty.generateAprove(generatedRules,innermost);
      }
      if(options.timbuk) {
        output = "";
        output += Pretty.generateTimbuk(generatedRules,generatedSignature);
      }
      compiler.instance = null;
      return output;
    //  return "test = " + Main.options.withType;
    } catch (Exception e) {
      System.err.println("exception: " + e);
      e.printStackTrace();
      for(StackTraceElement s : e.getStackTrace()) {
        output += s+ "\n";
      }
      return output;
    }
  }
}
