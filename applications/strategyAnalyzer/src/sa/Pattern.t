package sa;

import sa.rule.types.*;
import tom.library.sl.*;

public class Pattern {
  %include { rule/Rule.tom }
  %include { sl.tom }

  private static Signature eSig = new Signature();
  private static Signature gSig = new Signature();

  public static void main(String args[]) {

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("g", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T"),GomType("T")), `GomType("T") );


    Term V = `Var("_");

    Term a =`Appl("a", TermList());
    Term ga =`Appl("g", TermList(a));
    Term gv =`Appl("g", TermList(V));

    Term t = `Appl("f", TermList(Sub(V,Add(TermList(a,ga,gv)))));

    System.out.println("t = " + t);
    System.out.println("pretty t = " + Pretty.toString(t));

    try {
      //t = `Repeat(OnceTopDown(Choice(SimplifyAdd(),SimplifySub()))).visitLight(t);
      t = `Repeat(OnceTopDown(SimplifyAdd())).visitLight(t);
      System.out.println("t1 = " + Pretty.toString(t));
      t = `Repeat(OnceTopDown(SimplifySub())).visitLight(t);
      System.out.println("t2 = " + Pretty.toString(t));
      t = `Repeat(OnceTopDown(ExpandSub())).visitLight(t);
      System.out.println("t3 = " + Pretty.toString(t));

      t = `Repeat(OnceTopDown(SimplifyAdd())).visitLight(t);
      System.out.println("t1 = " + Pretty.toString(t));
      t = `Repeat(OnceTopDown(SimplifySub())).visitLight(t);
      System.out.println("t2 = " + Pretty.toString(t));
      t = `Repeat(OnceTopDown(ExpandSub())).visitLight(t);
      System.out.println("t3 = " + Pretty.toString(t));


    } catch(VisitFailure e) {
      System.out.println("failure on: " + t);
    }
    System.out.println("res = " + Pretty.toString(t));
  }

  %strategy SimplifyAdd() extends Fail() {
    visit Term {

      // x + t -> x
      Add(TermList(_*, Var("_"), _*)) -> {
        return `Var("_");
      }

      // g(t) + g(t') -> g(t+t')
      Add(TermList(C1*, Appl(f,tl1), C2*, Appl(f, tl2), C3*)) -> {
        return `Add(TermList(C1*, Appl(f,TermList(Add(TermList(tl1*,tl2*)))), C2*, C3*));
      }

      // (a+b) + (c+d) -> (a+b+c+d)
      Add(TermList(C1*, Add(tl1), C2*, Add(tl2), C3*)) -> {
        return `Add(TermList(C1*,tl1*,C2*,tl2*,C3*));
      }

    }
  }

  %strategy SimplifySub() extends Fail() {
    visit Term {

      // x - (a+b) -> x - a - b
      Sub(t1, Add(TermList(head,tail*))) -> {
        return `Sub(Sub(t1,head), Add(TermList(tail*)));
      }

      // t - () -> t
      Sub(t, Add(TermList())) -> {
        return `t;
      }

      // t - t -> ()
      Sub(t, t) -> {
        return `Add(TermList());
      }

      // (a+t+b) - t -> (a+b)
      Sub(Add(TermList(C1*,t,C2*)),t) -> {
        return `Add(TermList(C1*,C2*));
      }

    }
  }  
  
  %strategy ExpandSub() extends Fail() {
    visit Term {
      // x - a -> expand AP
      Sub(Var("_"),t) -> {
        RuleCompiler ruleCompiler = new RuleCompiler(Pattern.eSig, Pattern.gSig);
        RuleList rl = ruleCompiler.expandAntiPatterns(`ConcRule(Rule(Anti(t),Var("_"))));
        //System.out.println("rl = " + Pretty.toString(rl));
        TermList tl = `TermList();
        %match(rl) {
          ConcRule(_*,Rule(lhs,rhs),_*) -> {
            Term newLhs = `TopDown(RemoveVar()).visitLight(`lhs);
            tl = `TermList(tl*,newLhs);
          }
        }
        return `Add(tl);
      }
    }
  }

  %strategy RemoveVar() extends Identity() {
    visit Term {
      Var(_) -> {
        return `Var("_");
      }
    }
  }

}
