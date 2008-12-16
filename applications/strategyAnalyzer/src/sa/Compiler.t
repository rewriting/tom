package sa;

import sa.rule.types.*;
import java.util.*;
import tom.library.sl.*;

public class Compiler {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Collection.tom }

  private final static Term BOTTOM = `Appl("Bottom",TermList());
  private static int phiNumber = 0;
  private static String getName() {
    return "phi" + (phiNumber++);
  }

  /*
   * Compile a strategy into a rewrite system
   */
  public static void compile(ExpressionList expl) {
    %match(expl) {
      ExpressionList(_*,Strat(s),_*) -> {
        Collection<Rule> bag = new ArrayList<Rule>();
        compileStrat(bag,`s);
        for(Rule r:bag) {
          System.out.println(Pretty.toString(r));
        }
      }
    }
  }

  /*
   * compile a strategy
   * return the name of the top symbol (phi) introduced
   */
  private static String compileStrat(Collection bag, Strat strat) {
    %match(strat) {
      StratRule(Rule(lhs,rhs)) -> {
        String phi = getName();
        bag.add(`Rule(Appl(phi,TermList(lhs)),rhs));
        return phi;
      }

      StratMu(name,s) -> {
        try {
          String phi_x = getName();
          Strat newStrat = `TopDown(ReplaceMuVar(name,phi_x)).visitLight(`s);
          String phi_s = compileStrat(bag,newStrat);
          bag.add(`Rule(Appl(phi_x,TermList(Var("x"))),Appl(phi_s,TermList(Var("x")))));
          bag.add(`Rule(Appl(phi_x,TermList(BOTTOM)),Appl(phi_s,TermList(BOTTOM))));
          return phi_s;
        } catch(VisitFailure e) {
          System.out.println("failure in StratMu on: " + `s);
        }
      }

      StratIdentity() -> {
        String phi = getName();
        bag.add(`Rule(Appl(phi,TermList(Var("x"))),Var("x")));
        return phi;
      }

      StratFail() -> {
        String phi = getName();
        bag.add(`Rule(Appl(phi,TermList(Var("x"))),BOTTOM));
        return phi;
      }

      StratSequence(s1,s2) -> {
        String phi_s1 = compileStrat(bag,`s1);
        String phi_s2 = compileStrat(bag,`s2);
        String phi = getName();
        bag.add(`Rule(Appl(phi,TermList(Var("x"))),
              Appl(phi_s2,TermList(Appl(phi_s1,TermList(Var("x")))))));
        return phi;
      }

      StratChoice(s1,s2) -> {
        String phi_s1 = compileStrat(bag,`s1);
        String phi_s2 = compileStrat(bag,`s2);
        String phi = getName();
        String phi2 = getName();
        bag.add(`Rule(Appl(phi,TermList(Var("x"))),
              Appl(phi2,TermList(Appl(phi_s1,TermList(Var("x"))), Var("x")))));
        bag.add(`Rule(Appl(phi2,TermList(BOTTOM,Var("x"))),
              Appl(phi_s2,TermList(Var("x")))));
        bag.add(`Rule(Appl(phi2,TermList(Anti(BOTTOM),Var("x"))),
              Appl(phi_s1,TermList(Var("x")))));
        return phi;
      }

      // mu fix point: transform the startame into a function call
      StratName(name) -> {
        return `name;
      }

    }
    return strat.toString();
  }

  %strategy ReplaceMuVar(name:String, appl:String) extends Identity() {
    visit Strat {
      StratName(n) && n==name -> {
        return `StratName(appl);
      }
    }
  }

  /*
   * Transforms Let(name,exp,body) into body[name/exp]
   */
  public static ExpressionList expand(ExpressionList expl) {
    try {
      return `RepeatId(TopDown(Expand())).visitLight(expl);
    } catch(VisitFailure e) {
      System.out.println("failure on: " + e);
    }
    return expl;
  }

  %strategy Expand() extends Identity() {
    visit Expression {
      Let(name,exp,body) -> {
        return `TopDown(Replace(name,exp)).visitLight(`body);
      }
    }
  }

  %strategy Replace(name:String, exp:Expression) extends Identity() {
    visit Strat {
      StratName(n) && n==name -> {
        return `StratExp(exp);
      }
    }
  }

}
