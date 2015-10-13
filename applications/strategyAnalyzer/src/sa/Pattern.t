package sa;

import sa.rule.types.*;
import tom.library.sl.*;

public class Pattern {
  %include { rule/Rule.tom }
  %include { sl.tom }

  private static Signature eSig = new Signature();
  private static Signature gSig = new Signature();

  public static void main(String args[]) {
    Term V = `Var("_");
    Term t = null;

    // example 1
    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("g", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T"),GomType("T")), `GomType("T") );

    Term a =`Appl("a", TermList());
    Term ga =`Appl("g", TermList(a));
    Term gv =`Appl("g", TermList(V));

    t = `Appl("f", TermList(Sub(gv,Add(TermList(a,ga)))));
    //t = `Appl("f", TermList(Sub(V,Add(TermList(a,ga,gv)))));

    // example 2
    eSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    eSig.addSymbol("Cons", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );
    //eSig.addSymbol("sep", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );

    Term y_ys = `Appl("Cons", TermList(V,V));
    Term x_y_ys = `Appl("Cons", TermList(V,y_ys));
    Term sep1 = `Appl("sep", TermList(V,x_y_ys));
    Term sep2 = `Appl("sep", TermList(V,V));

    t = `Sub(sep2,sep1);

    System.out.println("pretty t = " + Pretty.toString(t));

    try {
      //t = `Repeat(OnceTopDown(Choice(SimplifyAdd(),SimplifySub()))).visitLight(t);
      Term oldT = null;
      while(oldT != t) {
        oldT = t;
        t = `Repeat(OnceTopDown(SimplifyAdd())).visitLight(t);
        System.out.println("t1 = " + Pretty.toString(t));
        t = `Repeat(OnceTopDown(SimplifySub())).visitLight(t);
        System.out.println("t2 = " + Pretty.toString(t));
        t = `Repeat(OnceTopDown(ExpandSub())).visitLight(t);
        System.out.println("t3 = " + Pretty.toString(t));
      }


    } catch(VisitFailure e) {
      System.out.println("failure on: " + t);
    }
    System.out.println("res = " + Pretty.toString(t));
  }

  %strategy SimplifyAdd() extends Fail() {
    visit Term {

      // (a+b) + (c+d) -> (a+b+c+d)
      Add(TermList(C1*, Add(tl1), C2*, Add(tl2), C3*)) -> {
        return `Add(TermList(C1*,tl1*,C2*,tl2*,C3*));
      }

      // a + x + b -> x
      Add(TermList(_*, Var("_"), _*)) -> {
        return `Var("_");
      }
      
      // t + t -> t
      Add(TermList(C1*, t, C2*, t, C3*)) -> {
        return `Add(TermList(C1*,t,C2*,C3*));
      }

      // g(t) + g(t') -> g(t + t')
      Add(TermList(C1*, Appl(f,tl1), C2*, Appl(f, tl2), C3*)) -> {
        TermList tl = `add(tl1,tl2);
        return `Add(TermList(C1*, Appl(f,tl), C2*, C3*));
      }

    }
  }

  %strategy SimplifySub() extends Fail() {
    visit Term {
      // x - x -> x
      Sub(Var("_"), Var("_")) -> {
        return `Var("_");
      }

      // t - t -> ()
      Sub(t, t) -> {
        return `t;
      }
      //Sub(Appl(f,tl), Appl(f,tl)) -> {
      //  return `Appl(f,tl);
      //}

      // t - () -> t
      Sub(t, Add(TermList())) -> {
        return `t;
      }

      // (a + t + b) - t -> (a + b)
      Sub(Add(TermList(C1*,t,C2*)),t) -> {
        return `Add(TermList(C1*,C2*));
      }

      // g(t) - g(t') -> g(t - t')
      Sub(Appl(f,tl1), Appl(f, tl2)) && tl1!=tl2-> {
        TermList tl = `sub(tl1,tl2);
        return `Appl(f,TermList(tl*));
      }

      // g(t) - (a + g(t') + b) -> g(t - t') - (a + b)
      Sub(Appl(f,tl1),Add(TermList(C1*, Appl(f, tl2), C2*))) -> {
        TermList tl = `sub(tl1,tl2);
        return `Sub(Appl(f,TermList(tl*)), Add(TermList(C1*,C2*)));
      }

      // TODO: t - t' -> t if t' not matched t
      // g(t) - f(t') -> g(t)
      Sub(Appl(f,tl1), Appl(g, tl2)) && f!=g -> {
        return `Appl(f,(tl1));
      }

      // g(t) - (a + f(t') + b) -> g(t) - (a + b)
      Sub(Appl(f,tl1),Add(TermList(C1*, Appl(g, tl2), C2*))) && f!=g -> {
        return `Sub(Appl(f,tl1), Add(TermList(C1*,C2*)));
      }

      // x - (a + b) -> x - a - b
      Sub(t1, Add(TermList(head,tail*))) -> {
        return `Sub(Sub(t1,head), Add(TermList(tail*)));
      }

    }
  }

  // (a,b,c) - (a',b',c') -> (a-a', b-b', c-c')
  private static TermList sub(TermList tl1,TermList tl2) {
    TermList tl = `TermList();
    while(!tl1.isEmptyTermList()) {
      Term h1 = tl1.getHeadTermList();
      Term h2 = tl2.getHeadTermList();
      tl = `TermList(tl*, Sub(h1,h2));

      tl1 = tl1.getTailTermList();
      tl2 = tl2.getTailTermList();
    }
    return tl;
  }

  private static TermList add(TermList tl1,TermList tl2) {
    TermList tl = `TermList();
    while(!tl1.isEmptyTermList()) {
      Term h1 = tl1.getHeadTermList();
      Term h2 = tl2.getHeadTermList();
      tl = `TermList(tl*, Add(TermList(h1,h2)));

      tl1 = tl1.getTailTermList();
      tl2 = tl2.getTailTermList();
    }
    return tl;
  }

  %strategy ExpandSub() extends Fail() {
    visit Term {
      // x - a -> expand AP
      Sub(Var("_"),t@Appl(f,args_f)) -> {
        GomType codomain = eSig.getCodomain(`f);
        RuleCompiler ruleCompiler = new RuleCompiler(Pattern.eSig, Pattern.gSig);
        RuleList rl = ruleCompiler.expandAntiPatterns(`ConcRule(Rule(Anti(t),Var("_"))));
        //System.out.println("rl = " + Pretty.toString(rl));
        TermList tl = `TermList();
        %match(rl) {
          ConcRule(_*,Rule(lhs@Appl(g,args_g),rhs),_*) -> {
            if(eSig.getCodomain(`g) == codomain) {
              Term newLhs = `TopDown(RemoveVar()).visitLight(`lhs);
              tl = `TermList(tl*,newLhs);
            }
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
