package lemu2;

import tom.library.sl.*;
import lemu2.proofterms.types.*;
import lemu2.proofterms.types.namelist.nameList;
import lemu2.proofterms.types.conamelist.conameList;
import org.antlr.runtime.*;
import java.util.Collection;

public class Main {

  %include { proofterms/proofterms.tom } 
  %include { sl.tom } 

  private static String 
    prettyNormalForms(Collection<ProofTerm> c,TermRewriteRules tr, PropRewriteRules pr) {
    String res = "";
    for (ProofTerm pt: c) {
      res += "- " + Pretty.pretty(pt.export());
      res += "\n";
      res += "  typechecks : " + TypeChecker.typecheck(pt,tr,pr);
      res += "\n";
    }
    return res;
  }

  public static void main(String[] args) {
    try{
      LemuLexer lexer = new LemuLexer(new ANTLRInputStream(System.in));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      LemuParser parser = new LemuParser(tokens);
      TermRewriteRules rrules = parser.termmodulo().convert();
      System.out.println(Pretty.pretty(rrules.export()));
      PropRewriteRules prules = parser.propmodulo().convert();
      System.out.println(Pretty.pretty(prules.export()));
      ProofTerm pt = parser.proofterm().convert();
      System.out.println(Pretty.pretty(pt.export()));
      System.out.println();
      System.out.println("typechecks : " + TypeChecker.typecheck(pt,rrules,prules));
      System.out.println("normal forms : ");
      System.out.println(prettyNormalForms(Evaluation.reduce(pt),rrules,prules));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}

