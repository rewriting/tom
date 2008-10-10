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

  private static String prettyNormalForms(Collection<ProofTerm> c) {
    String res = "";
    for (ProofTerm pt: c) {
      res += "- " + Pretty.pretty(pt.export());
      res += "\n";
      res += "  typechecks : " + TypeChecker.typecheck(pt);
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
      System.err.println(rrules);
      ProofTerm pt = parser.proofterm().convert();
      System.err.println(Pretty.pretty(pt.export()));
      System.out.println("typechecks : " + TypeChecker.typecheck(pt));
      System.out.println("normal forms : ");
      System.out.println(prettyNormalForms(Evaluation.reduce(pt)));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}

