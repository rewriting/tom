package lemu;

import lemu.sequents.*;
import lemu.sequents.types.*;

import lemu.urban.*;
import lemu.urban.types.*;

import tom.library.sl.*;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;

import java.io.*;

class PrettyPrinter {

//  %include { sequents/sequents.tom }
  %include { urban/urban.tom }
  %include { sl.tom }
  %typeterm Set {implement {Set} }
  %typeterm Collection { implement {Collection} }

  private static String translate(RuleType rt) {
    %match(rt) {
      axiomInfo[] ->  { return "axiom"; }
      impliesLeftInfo[] -> { return "\\Rightarrow_\\mathcal{L}"; }
      impliesRightInfo[]-> { return "\\Rightarrow_\\mathcal{R}"; }
      andLeftInfo[] -> { return "\\land_\\mathcal{L}"; }
      andRightInfo[] -> { return "\\land_\\mathcal{R}"; }
      orLeftInfo[] -> { return "\\lor_\\mathcal{L}"; }
      orRightInfo[] -> { return "\\lor_\\mathcal{R}"; }
      forallRightInfo[] -> { return "\\forall_\\mathcal{R}"; }
      forallLeftInfo[] -> { return "\\forall_\\mathcal{L}"; }
      existsRightInfo[] -> { return "\\exists_\\mathcal{R}"; }
      existsLeftInfo[] -> { return "\\exists_\\mathcal{L}"; }
      bottomInfo[] -> { return "\\bot"; }
      topInfo[] -> { return "\\top"; }
      cutInfo(p) -> { return "cut (" + `toLatex(p) + ")"; }
      openInfo[] -> { return "open"; }
      reductionInfo[] -> { return "reduction"; }
      contractionLeftInfo[] -> { return "contr_\\mathcal{L}"; }
      contractionRightInfo[] -> { return "contr_\\mathcal{R}"; }
      weakLeftInfo() -> { return "weak_\\mathcal{L}"; }
      weakRightInfo() -> { return "weak_\\mathcal{R}"; }
      customRuleInfo[name=n] -> { return `n; }
    }
    return rt.toString();
  }

  public static int peanoToInt(Term term) throws Exception {
    return peanoToInt(term,0);
  }

  private static int peanoToInt(Term term, int i) throws Exception {
    %match(Term term) {
      funAppl("z",concTerm()) -> { return i; }
      funAppl("succ",concTerm(t)) -> { return peanoToInt(`t,i+1); }
    }
    throw new Exception("term is not an integer");
  }

  public static Term intToPeano(int n) {
    Term res = `funAppl("z",concTerm());
    for (int i=0; i<n; i++) {
      res = `funAppl("succ",concTerm(res));
    }
    return res;
  }

  // --- auxiliary strats for cleanTree ----

  %strategy RemoveInHyp(prop: Prop) extends `Identity() {
    visit Sequent {
      sequent(context(X*,p,Y*), c) -> { if (prop == `p) return `sequent(context(X*,Y*),c); }
    }
  }

  %strategy RemoveInConcl(prop: Prop) extends `Identity() {
    visit Sequent {
      sequent(h, context(X*,p,Y*)) -> { if (prop == `p) return `sequent(h,context(X*,Y*)); }
    }
  }

  %strategy IsActive(prop: Prop, tl: TermRuleList, pl: PropRuleList) extends `Fail() {
    visit Tree {
      // all propositions are virtually used in an open branch
      r@rule[type=openInfo[]] -> { return `r; }

      // in case of a reduce rule, we have to check if the reduced form of a prop is active
      r@rule(reductionInfo[],_,concl,_) -> {
        %match(Sequent `concl, Prop prop) {
          sequent(context(_*,p,_*),_), p -> {
            Prop after = (Prop) Unification.reduce(`p,tl,pl);
            if (`p != after)
              return `r;
          }
          sequent(_,context(_*,p,_*)), p -> {
            Prop after = (Prop) Unification.reduce(`p,tl,pl);
            if (`p != after)
              return `r;
          }
        }
      }

      r@rule(_,_,_,p) -> { if (prop == `p) { return `r; } }
    }
  }

  %strategy Clean(tl: TermRuleList, pl: PropRuleList) extends `Identity() {
    visit Tree {
      r@rule(_,_,sequent(h,c),_) -> {
        Tree res = `r;
        %match(Context h) {
          context(_*,x,_*) -> {
            res = (Tree)
              `Choice(OnceTopDown(IsActive(x,tl,pl)),InnermostId(RemoveInHyp(x))).visit(`res);
          }
        }
        %match(Context c) {
          context(_*,x,_*) -> {
            res = (Tree)
              `Choice(OnceTopDown(IsActive(x,tl,pl)),InnermostId(RemoveInConcl(x))).visit(`res);
          }
        }
        return res;
      }
    }
  }

  // ---------------------------------------

  /**
   * remove unused hypothesis and conclusions in subtrees
   **/
  public static Tree cleanTree(Tree tree, TermRuleList tl, PropRuleList pl) {
    try { return (Tree) `TopDown(Clean(tl,pl)).visit(tree); }
    catch(VisitFailure e) { e.printStackTrace(); throw new RuntimeException(); }
  }

  public static String toLatex(sequentsAbstractType term) {
    %match(Tree term) {
      rule(n,premisses(),c,_) -> {return "\\infer["+ translate(`n) +"]\n{"+ toLatex(`c) +"}\n{}";}
      rule(n,p,c,_) -> {return "\\infer["+ translate(`n) +"]\n{" + toLatex(`c) + "}\n{"+ toLatex(`p) +"}";}
    }

    %match(Premisses term) {
      premisses() -> { return ""; }
      premisses(x) -> { return toLatex(`x); }
      premisses(h,t*) -> { return toLatex(`h) + " & " + toLatex(`t*); }
    }

    %match(Context term) {
      context() -> { return ""; }
      context(x) -> { return toLatex(`x); }
      context(h,t*) -> {return toLatex(`h) + ", " + toLatex(`t);}
    }

    %match(Sequent term) {
      sequent(context(), c) -> { return "\\vdash " + toLatex(`c); }
      sequent(ctx, c) -> { return toLatex(`ctx) + " \\vdash " + toLatex(`c); }
    }

    %match(Prop term) {

      // arithmetic pretty print
      relationAppl("eq",concTerm(x,y)) -> {
        return toLatex(`x) + " = " + toLatex(`y);
      }
      relationAppl("gt",concTerm(x,y)) -> {
        return toLatex(`x) + " > " + toLatex(`y);
      }
      relationAppl("lt",concTerm(x,y)) -> {
        return toLatex(`x) + " < " + toLatex(`y);
      }
      relationAppl("le",concTerm(x,y)) -> {
        return toLatex(`x) + " \\le " + toLatex(`y);
      }

      // set theory pretty print
      relationAppl("in",concTerm(x,y)) -> {
        return toLatex(`x) + " \\in " + toLatex(`y);
      }
      relationAppl("subset",concTerm(x,y)) -> {
        return toLatex(`x) + " \\subset " + toLatex(`y);
      }
      relationAppl("supset",concTerm(x,y)) -> {
        return toLatex(`x) + " \\supset " + toLatex(`y);
      }

      // lamda-Pi
      relationAppl("WF",concTerm(x)) -> {
        if (leftEndedByNil(`x))
          return ("\\mathsf{WF}\\left(") + contextListToLatex(`x) + "\\right)";
        else
          return ("\\mathsf{WF}\\left(") + toLatex(`x) + "\\right)";
      }

      relationAppl(n, concTerm()) -> { return `n;}
      relationAppl(n, tlist) -> { return `n + "(" + toLatex(`tlist) + ")";}

      implies(p, bottom()) -> { return "\\lnot (" + toLatex(`p) + ")"; }

      implies(p1@relationAppl[],p2) -> {
        return %[@toLatex(`p1)@ \Rightarrow @toLatex(`p2)@]% ;
      }
      implies(p1,p2) -> {
        return %[(@toLatex(`p1)@) \Rightarrow @toLatex(`p2)@]% ;
      }
      or(p1@relationAppl[],p2) -> {
        return %[@toLatex(`p1)@ \lor @toLatex(`p2)@]%;
      }
      or(p1,p2) -> {
        return %[(@toLatex(`p1)@) \lor @toLatex(`p2)@]%;
      }
      and(p1@relationAppl[],p2) -> {
        return %[@toLatex(`p1)@ \land @toLatex(`p2)@]%;
      }
      and(p1,p2) -> {
        return %[(@toLatex(`p1)@) \land @toLatex(`p2)@]%;
      }
      forall(n, p) -> { return "\\forall " + `n + ", " + toLatex(`p);}
      exists(n, p) -> { return "\\exists " + `n + ", " + toLatex(`p);}
      bottom() -> { return "\\bot";  }
      top() -> { return "\\top";  }
    }

    %match(TermList term) {
      concTerm(x) -> { return toLatex(`x); }
      concTerm(h,t*) -> { return toLatex(`h) + ", " + toLatex(`t); }
    }

    %match(Term term) {
      Var(concString('@',n*)) -> { return `n; }
      Var(n) -> { return `n; }

      // arithmetic
      funAppl("z",concTerm()) -> { return "0"; }
      i@funAppl("succ",_) -> {
        try { return Integer.toString(peanoToInt(`i));}
        catch (Exception e) {}
      }

      funAppl("plus",concTerm(t1,t2)) -> {
        return "(" + toLatex(`t1) + "+" + toLatex(`t2) + ")";
      }
      funAppl("mult",concTerm(t1,t2)) -> {
        return "(" + toLatex(`t1) + " \\times " + toLatex(`t2) + ")";
      }
      funAppl("minus",concTerm(t1,t2)) -> {
        return "(" + toLatex(`t1) + " - " + toLatex(`t2) + ")";
      }
      funAppl("div",concTerm(t1,t2)) -> {
        return "(" + toLatex(`t1) + " / " + toLatex(`t2) + ")";
      }

      // set theory
      funAppl("union",concTerm(t1,t2)) -> {
        return "(" + toLatex(`t1) + ") \\cup (" + toLatex(`t2) + ")";
      }
      funAppl("inter",concTerm(t1,t2)) -> {
        return "(" + toLatex(`t1) + ") \\cap (" + toLatex(`t2) + ")";
      }
      funAppl("emptyset",concTerm()) -> {
        return "\\emptyset";
      }

      // finite 1st order theory of classes pretty print
      funAppl("appl",concTerm(p,x*)) -> {
        return `toLatex(p) + "["+ toLatex(`x*) + "]";
      }
      funAppl("nil",concTerm()) -> {
        return ("nil");
      }
      funAppl("fEq",concTerm(x,y)) -> {
        return toLatex(`x) + "\\dot{=}" + `toLatex(y);
      }
      l@funAppl("cons",concTerm(x,y)) -> {
        if (endedByNil(`l)) return "\\langle " + listToLatex(`l) + "\\rangle ";
        else return toLatex(`x) + "::" + toLatex(`y);
      }

      // lambda-Pi
     funAppl("type",concTerm()) -> {
       return ("*");
     }
     funAppl("kind",concTerm()) -> {
       return ("\\square");
     }
     funAppl("pitype",concTerm(x,y)) -> {
       return ("\\dot\\pi_*") + toLatex(`x) + " .~" +  toLatex(`y);
     }
     funAppl("pikind",concTerm(x,y)) -> {
       return ("\\dot\\pi_\\square") + toLatex(`x) + " .~" +  toLatex(`y);
     }

     // lambda-sigma
     funAppl("subst",concTerm(x,y)) -> {
       return toLatex(`x) + "[" + toLatex(`y) + "]";
     }
     funAppl("rond",concTerm(x,y)) -> {
       return toLatex(`x) + " \\circ " + toLatex(`y);
     }
     funAppl("shift",concTerm()) -> {
       return "\\uparrow";
     }
     funAppl("one",concTerm()) -> {
       return "\\mathsf{1}";
     }
     funAppl("id",concTerm()) -> {
       return "id";
     }
     funAppl("lcons",concTerm(x,y)) -> {
       return  toLatex(`x) + " \\cdot " + toLatex(`y);
     }
     funAppl("lambda",concTerm(x)) -> {
       return "\\lambda " + toLatex(`x);
     }
     funAppl("lappl",concTerm(p,x*)) -> {
       return "(" + toLatex(`p) + "~" + toLatex(`x*) + ")";
     }

     funAppl(n, concTerm()) -> { return `n + "()";}
     funAppl(n, tlist) -> { return `n + "(" + toLatex(`tlist) + ")";}
    }

    return null;
  }

  public static String toLatex(urbanAbstractType term) {

    %match(NTree term) {
      nrule(n,npremisses(),c,pt) -> {
        return "\\infer["+ translate(`n) +"]\n{ "+toLatex(`pt)+" \\rhd "+ toLatex(`c) +"}\n{}";
      }
      nrule(n,p,c,pt) -> {return "\\infer["+ translate(`n) +"]\n{ "+toLatex(`pt)+" \\rhd " + toLatex(`c) + "}\n{"+ toLatex(`p) +"}";}
    }

    %match(NPremisses term) {
      npremisses() -> { return ""; }
      npremisses(x) -> { return toLatex(`x); }
      npremisses(h,t*) -> { return toLatex(`h) + " & " + toLatex(`t*); }
    }

    %match (NProp term) {
      nprop(x,p) -> { return toLatex(`x)+":"+toLatex(`p);}
    }

    %match (CNProp term) {
      cnprop(a,p) -> { return toLatex(`a)+":"+toLatex(`p);}
    }

    %match (NContext term) {
      ncontext() -> {return "";}
      ncontext(h) -> {return toLatex(`h); }
      ncontext(h,d*) -> {return toLatex(`h)+","+toLatex(`d);}
    }

    %match (CNContext term) {
      cncontext() -> {return "";}
      cncontext(h) -> {return toLatex(`h); }
      cncontext(h,d*) -> {return toLatex(`h)+","+toLatex(`d);}
    }

    %match (NSequent term) {
      nsequent(h,c) -> {return toLatex(`h)+"\\vdash "+toLatex(`c);}
    }

    %match (Name term) {
      name(n) -> {return "x_"+`n;}
    }

    %match (CoName term) {
      coname(n) -> {return "a_"+`n; }
    }

    %match (ProofTerm term) { // INCOMPLET manque le 1er ordre
      ax(n,cn) -> {return "{\\sf Ax}("+toLatex(`n)+","+toLatex(`cn)+")";}
      cut(a,m1,x,m2) -> {return "{\\sf Cut}(\\langle "+toLatex(`a)+"\\rangle ,"+toLatex(`m1)+", \\langle "+toLatex(`x)+" \\rangle ,"+toLatex(`m2)+")";}
      falseL(n) -> {return "{\\sf False}_L("+toLatex(`n)+")";}
      trueR(cn) -> {return "{\\sf True}_R("+toLatex(`cn)+")";}
      andR(a,m1,b,m2,nc) -> {return "{\\sf And}_R(\\langle "+toLatex(`a)+"\\rangle "+toLatex(`m1)+",\\langle "+toLatex(`b)+"\\rangle "+toLatex(`m2)+","+toLatex(`nc)+")" ;}
      orL(x,m1,y,m2,c) -> {return "{\\sf Or}_L(\\langle "+toLatex(`x)+"\\rangle "+toLatex(`m1)+",\\langle "+toLatex(`y)+"\\rangle "+toLatex(`m2)+","+toLatex(`c)+")" ;}
      andL(x,y,m,n) -> {return "{\\sf And}_L(\\langle "+toLatex(`x)+"\\rangle\\langle "+toLatex(`y)+"\\rangle "+toLatex(`m)+","+toLatex(`n)+")";}
      orR(a,b,m,cn) -> {return "{\\sf Or}_R(\\langle "+toLatex(`a)+"\\rangle\\langle "+toLatex(`b)+"\\rangle "+toLatex(`m)+","+toLatex(`cn)+")";}
      implyR(x,a,m1,cn) -> {return "{\\sf Imply}_R(\\langle "+toLatex(`x)+"\\rangle\\langle "+toLatex(`a)+"\\rangle "+toLatex(`m1)+","+toLatex(`cn)+")";}
      implyL(a,m1,x,m2,n) -> {return "{\\sf Imply}_L(\\langle "+toLatex(`a)+"\\rangle "+toLatex(`m1)+",\\langle "+toLatex(`x)+"\\rangle "+toLatex(`m2)+","+toLatex(`n)+")" ;}
      forallL(x,m,t,n) -> {return "{\\sf Forall}_L(\\langle "+toLatex(`x)+"\\rangle "+toLatex(`m)+", "+toLatex(`t)+", "+toLatex(`n)+")" ;}
      forallR(a,varx,m,cn) -> {return "{\\sf Forall}_R(\\langle "+toLatex(`a)+"\\rangle \\langle "+toLatex(`varx)+"\\rangle "+toLatex(`m)+", "+toLatex(`cn)+")" ;}
      existsL(x,varx,m,n) -> {return "{\\sf Exists}_L(\\langle "+toLatex(`x)+"\\rangle \\langle "+toLatex(`varx)+"\\rangle "+toLatex(`m)+", "+toLatex(`n)+")" ;}
      existsR(a,m,t,cn) -> {return "{\\sf Exists}_R(\\langle "+toLatex(`a)+"\\rangle "+toLatex(`m)+", "+toLatex(`t)+", "+toLatex(`cn)+")" ;}
    }

    return null;

  }


  // finite 1st order theory of classes list pretty print
  private static boolean endedByNil(Term l) {
    %match(Term l) {
      funAppl("cons",concTerm(_,funAppl("nil",concTerm()))) -> { return true; }
      funAppl("cons",concTerm(_,y)) -> { return endedByNil(`y); }
    }
    return false;
  }

  public static String listToLatex(Term t) {
    %match(Term t) {
      funAppl("cons",concTerm(x,funAppl("nil",concTerm()))) -> { return toLatex(`x); }
      funAppl("cons",concTerm(x,y)) -> {
        return toLatex(`x) + "," + listToLatex(`y);
      }
    }
    return null;
  }

  // lambda-Pi context list pretty print (inverted order)
  private static boolean leftEndedByNil(Term l) {
    %match(Term l) {
	  funAppl("cons",concTerm(funAppl("nil",concTerm()),_)) -> { return true; }
	  funAppl("cons",concTerm(x,_)) -> { return leftEndedByNil(`x); }
    }
    return false;
  }

  public static String contextListToLatex(Term t) {
    %match(Term t) {
	  funAppl("cons",concTerm(funAppl("nil",concTerm()), funAppl("e",concTerm(a,b))))
	      -> { return toLatex(`a) + " \\in " + toLatex(`b); }
	  funAppl("cons",concTerm(x,funAppl("e",concTerm(a,b)))) -> {
	      return contextListToLatex(`x) + "," + toLatex(`a) + " \\in " + toLatex(`b);      }
    }
    return null;
  }

  public static String prettyPrint(sequentsAbstractType term) {

    %match(Sequent term) {
      sequent(h,c) -> {
        return prettyPrint(`h) + " |- " + prettyPrint(`c);
      }
    }

    %match(Context term) {
      context() -> { return ""; }
      context(f) -> { return prettyPrint(`f); }
      context(h,t*) -> { return prettyPrint(`h) + ", " +  prettyPrint(`t); }
    }

    %match(Prop term) {
      implies(p1@relationAppl[],p2) -> {
        return %[@prettyPrint(`p1)@ => @prettyPrint(`p2)@]% ;
      }
      implies(p1,p2) -> {
        return %[(@prettyPrint(`p1)@) => @prettyPrint(`p2)@]% ;
      }
      or(p1@relationAppl[],p2) -> {
        return %[@prettyPrint(`p1)@ \/ @prettyPrint(`p2)@]%;
      }
      or(p1,p2) -> {
        return %[(@prettyPrint(`p1)@) \/ @prettyPrint(`p2)@]%;
      }
      and(p1@relationAppl[],p2) -> {
        return %[@prettyPrint(`p1)@ /\ @prettyPrint(`p2)@]%;
      }
      and(p1,p2) -> {
        return %[(@prettyPrint(`p1)@) /\ @prettyPrint(`p2)@]%;
      }
      forall(x,p1) -> {
        return "forall " + `x + ", " + prettyPrint(`p1);
      }
      exists(x,p1) -> {
        return "exists " + `x + ", " + prettyPrint(`p1);
      }
      relationAppl(r,concTerm()) -> {
        return `r;
      }

      // arithmetic pretty print
      relationAppl("eq",concTerm(x,y)) -> {
        return prettyPrint(`x) + " = " + prettyPrint(`y);
      }
      relationAppl("gt",concTerm(x,y)) -> {
        return prettyPrint(`x) + " > " + prettyPrint(`y);
      }
      relationAppl("lt",concTerm(x,y)) -> {
        return prettyPrint(`x) + " < " + prettyPrint(`y);
      }
      relationAppl("le",concTerm(x,y)) -> {
        return prettyPrint(`x) + " <= " + prettyPrint(`y);
      }

      // set theory prettyprint
      relationAppl("in",concTerm(x,y)) -> {
        return prettyPrint(`x) + " \u2208 " + prettyPrint(`y);
      }

      relationAppl(r,x) -> {
        return `r + "(" + prettyPrint(`x) + ")";
      }

      bottom() -> {
        return "False";
      }
      top() -> {
        return "True";
      }
    }

    %match(Term term) {
      Var(concString('@',n*)) -> { return `n; }
      Var(x) -> { return `x;}

      // arithmetic pretty print
      funAppl("z",concTerm()) -> { return "0"; }
      i@funAppl("succ",_) -> {
        try { return Integer.toString(peanoToInt(`i));}
        catch (Exception e) {}
      }
      funAppl("plus",concTerm(t1,t2)) -> {
        return "(" + prettyPrint(`t1) + "+" + prettyPrint(`t2) + ")";
      }
      funAppl("mult",concTerm(t1,t2)) -> {
        return "(" + prettyPrint(`t1) + "*" + prettyPrint(`t2) + ")";
      }
      funAppl("minus",concTerm(t1,t2)) -> {
        return "(" + prettyPrint(`t1) + "-" + prettyPrint(`t2) + ")";
      }
      funAppl("div",concTerm(t1,t2)) -> {
        return "(" + prettyPrint(`t1) + "/" + prettyPrint(`t2) + ")";
      }

      // finite 1st order theory of classes pretty print
      funAppl("appl",concTerm(p,x*)) -> {
        return `prettyPrint(p) + "["+ prettyPrint(`x*) + "]";
      }
      funAppl("nil",concTerm()) -> {
        return ("nil");
      }
      l@funAppl("cons",concTerm(x,y)) -> {
        if(endedByNil(`l)) return "<" + prettyList(`l) + ">";
        else return prettyPrint(`x) + "::" + prettyPrint(`y);
      }

      // lambda-sigma
      funAppl("lambda",concTerm(x)) -> {
        return "λ" + `prettyPrint(x);
      }
      funAppl("lappl",concTerm(p,x*)) -> {
        return "(" + `prettyPrint(p) + " "+ prettyPrint(`x*) + ")";
      }
      funAppl("subst",concTerm(p,x*)) -> {
        return  `prettyPrint(p) + "["+ prettyPrint(`x*) + "]";
      }
      funAppl("one",concTerm()) -> {
        return ("1");
      }
      funAppl("shift",concTerm()) -> {
        return ("↑");
      }
      funAppl("lcons",concTerm(x,y)) -> {
        return prettyPrint(`x) + "." + prettyPrint(`y);
      }
      funAppl("rond",concTerm(x,y)) -> {
        return prettyPrint(`x) + " o " + prettyPrint(`y);
      }

      // lambda-pi
      funAppl("type",concTerm()) -> {
        return ("*");
      }

      funAppl("kind",concTerm()) -> {
        return ("□");
      }

      funAppl("pitype",concTerm(x,y))  -> {
        return "π⁎" + prettyPrint(`x) + ". " + prettyPrint(`y);
      }

      funAppl("pikind",concTerm(x,y))  -> {
        return "π◽" + prettyPrint(`x) + ". " + prettyPrint(`y);
      }


      funAppl(name,x) -> {
        return `name + "(" + prettyPrint(`x) + ")";
      }

      FreshVar(n,_) -> { return `n; }
      NewVar(n,_) -> { return `n; }
    }

    %match(TermList term) {
      concTerm() -> {return ""; }
      concTerm(h) -> { return prettyPrint(`h);}
      concTerm(h,t*) -> { return prettyPrint(`h) + ", " + prettyPrint(`t); }
    }

    return term.toString();
  }

  public static String prettyPrint(urbanAbstractType term) {

    %match(Name term) {
      name(n) -> {return "x"+`n; }
    }

    %match(CoName term) {
      coname(cn) -> {return "a"+`cn; }
    }

    %match(NProp term) {
      nprop(n,a) -> { return "<"+prettyPrint(`n)+":"+prettyPrint(`a)+">"; }
    }

    %match(CNProp term) {
      cnprop(cn,a) -> { return "<"+prettyPrint(`cn)+":"+prettyPrint(`a)+">"; }
    }

    %match(NContext term) {
      ncontext() -> { return ""; }
      ncontext(h) -> { return  prettyPrint(`h); }
      ncontext(h,t*) -> { return prettyPrint(`h) + ", " + prettyPrint(`t); }
    }

    %match(CNContext term) {
      cncontext() -> { return ""; }
      cncontext(h) -> { return  prettyPrint(`h); }
      cncontext(h,t*) -> { return prettyPrint(`h) + ", " + prettyPrint(`t); }
    }

    %match(ProofTerm term) {
      ax(n,cn) -> {return "ax("+prettyPrint(`n)+", "+prettyPrint(`cn)+")"; }
      cut(a,m1,x,m2) -> {return "cut("+prettyPrint(`a)+" "+prettyPrint(`m1)+", "+prettyPrint(`x)+" "+prettyPrint(`m2)+")";}
      falseL(n) -> {return "falseL("+prettyPrint(`n)+")";}
      trueR(cn) -> {return "trueR("+prettyPrint(`cn)+")";}
      andR(a,m1,b,m2,nc) -> {return "andR("+prettyPrint(`a)+" "+prettyPrint(`m1)+", "+prettyPrint(`b)+" "+prettyPrint(`m2)+", "+prettyPrint(`nc)+")" ;}
      andL(x,y,m,n) -> {return "andL("+prettyPrint(`x)+" "+prettyPrint(`y)+" "+prettyPrint(`m)+", "+prettyPrint(`n)+")" ;}
      orR(a,b,m,cn) -> {return "orR("+prettyPrint(`a)+" "+prettyPrint(`b)+" "+prettyPrint(`m)+", "+prettyPrint(`cn)+")" ;}
      orL(x,m1,y,m2,n) -> {return "orL("+prettyPrint(`x)+" "+prettyPrint(`m1)+", "+prettyPrint(`y)+" "+prettyPrint(`m2)+", "+prettyPrint(`n)+")" ;}
      implyR(x,a,m,cn) -> {return "implyR("+prettyPrint(`x)+" "+prettyPrint(`a)+" "+prettyPrint(`m)+", "+prettyPrint(`cn)+")" ;}
      implyL(a,m1,x,m2,n) -> {return "implyL("+prettyPrint(`a)+" "+prettyPrint(`m1)+", "+prettyPrint(`x)+" "+prettyPrint(`m2)+", "+prettyPrint(`n)+")" ;}
      existsL(x,varx,m,n) -> {return "existsL("+prettyPrint(`x)+" <"+prettyPrint(`varx)+"> "+prettyPrint(`m)+", "+prettyPrint(`n)+")" ;}
      existsR(a,m,t,cn) -> {return "existsR("+prettyPrint(`a)+" "+prettyPrint(`m)+", "+prettyPrint(`t)+", "+prettyPrint(`cn)+")";}
      forallL(x,m,t,n) -> {return "forallL("+prettyPrint(`x)+" "+prettyPrint(`m)+", "+prettyPrint(`t)+", "+prettyPrint(`n)+")";}
      forallR(a,varx,m,cn) -> {return "forallR("+prettyPrint(`a)+" <"+prettyPrint(`varx)+"> "+prettyPrint(`m)+", "+prettyPrint(`cn)+")" ;}
    }

    return term.toString();
  }

  // finite 1st order theory of classes list pretty print
  public static String prettyList(Term t) {
    %match(Term t) {
      funAppl("cons",concTerm(x,funAppl("nil",concTerm()))) -> { return prettyPrint(`x); }
      funAppl("cons",concTerm(x,y)) -> {
        return prettyPrint(`x) + "," + prettyList(`y);
      }
    }
    return null;
  }

  private static String getPrettySideConstraints(SeqList list) {

    HashSet<Term> set = Utils.getSideConstraints(list);

    if (set.isEmpty())
      return null;

    Iterator<Term> it = set.iterator();
    Term var = it.next();
    String res = prettyPrint(var);
    while (it.hasNext()) {
      var =  it.next();
      res += ", " + prettyPrint(var);
    }
    return res + " fresh";
  }

  public static String prettyRule(sequentsAbstractType term) {

    %match(RuleList term) {
      rlist(x) -> { return prettyRule(`x); }
      rlist(t,q*) -> {	return prettyRule(`t) + "\n\n" + prettyRule(`q); }
    }

    %match(SeqList term) {
      concSeq() -> { return ""; }
      concSeq(x) -> { return prettyPrint(`x); }
      concSeq(t,q*) -> {	return prettyPrint(`t) + "    " + prettyRule(`q); }
    }

    // generated rules
    %match(Rule term) {
      ruledesc(hs,c,p,_) -> {
        String r1 = prettyRule(`p);
        String r2;
        if(`hs==0)
          r2 = prettyPrint(`sequent(context(c),context()));
        else
          r2 = prettyPrint(`sequent(context(),context(c)));

        StringBuffer sb = new StringBuffer();
        for (int i=0; i< Math.max(r1.length(), r2.length()); ++i)
          sb.append('-');


        String r3 = getPrettySideConstraints(`p);
        if (r3 != null)
          return r1 + "\n" + sb.toString() + " (" + r3 + ")\n" + r2;
        else
          return r1 + "\n" + sb.toString() + "\n" + r2;
      }
    }
    return term.toString();
  }

  public static void display(Tree tree, TermRuleList tl, PropRuleList pl) throws java.io.IOException, java.lang.InterruptedException {
    //tree = cleanTree(tree, tl, pl);
    display(tree);
  }

  // displays a latex output in xdvi
  public static void display(sequentsAbstractType term) throws java.io.IOException, java.lang.InterruptedException {
    File tmp = File.createTempFile("output",".tex");
    FileWriter writer = new FileWriter(tmp);
    String path = tmp.getAbsolutePath();
    String name = tmp.getName();
    //String basename = path.substring(0,path.length()-4);
    String basename = "/tmp/"+name;
    basename = basename.substring(0,basename.length()-4);

    writer.write("\\documentclass{article}\n\\usepa"+"ckage{proof}\n\\usepa"+"ckage{amssymb}\n\\begin{document}\n\\pagestyle{empty}\n\\[\n");
    writer.write(toLatex(term));
    writer.write("\n\\]\n");
    writer.write("\\end{document}\n");
    writer.flush();

    System.out.println(path);
    Runtime rt = Runtime.getRuntime();
    System.out.println(%[latex -output-directory=/tmp @path@]%);
    Process pr = rt.exec(%[latex -output-directory=/tmp @path@]%);
    //Process pr = rt.exec(%[latex -output-directory=/tmp \nonstopmode\input{@path@}]%);
    int ret = pr.waitFor();
    if (ret == 0) {
      System.out.println(%[dvips @basename@.dvi -X 300 -Y 300 -E -o @basename@.ps]%);
      pr = rt.exec(%[dvips @basename@.dvi -X 300 -Y 300 -E -o @basename@.ps]%);
      ret = pr.waitFor();
    }
    if (ret == 0) {
      System.out.println(%[gv --scale=3 @basename@.ps]%);
      pr = rt.exec(%[gv --scale=3 @basename@.ps]%);
      ret = pr.waitFor();
    }
/*    if (ret == 0) {
	pr = rt.exec("xdvi " + path.substring(0,path.length()-4) +".dvi");
	pr.waitFor();} */
    else
	System.err.println("An error occurred during the LaTeX compilation.");
  }

  // displays a latex output in xdvi
  public static void display(urbanAbstractType term) throws java.io.IOException, java.lang.InterruptedException {
    File tmp = File.createTempFile("output",".tex");
    FileWriter writer = new FileWriter(tmp);
    String path = tmp.getAbsolutePath();
    String name = tmp.getName();
    //String basename = path.substring(0,path.length()-4);
    String basename = "/tmp/"+name;
    basename = basename.substring(0,basename.length()-4);

    writer.write("\\documentclass{article}\n\\usepa"+"ckage{proof}\n\\usepa"+"ckage{amssymb}\n\\begin{document}\n\\pagestyle{empty}\n\\[\n");
    writer.write(toLatex(term));
    writer.write("\n\\]\n");
    writer.write("\\end{document}\n");
    writer.flush();

    System.out.println(path);
    Runtime rt = Runtime.getRuntime();
    Process pr = rt.exec("latex -output-directory=/tmp " + path);
    //Process pr = rt.exec("latex -output-directory=/tmp \\nonstopmode\\input{" + path + "}");

    int ret = pr.waitFor();
    if (ret == 0) {
      pr = rt.exec("dvips " + basename +".dvi -X 300 -Y 300 -E -o "+ basename +".ps");
      ret = pr.waitFor();
    }
    if (ret ==0) {
      pr = rt.exec("gv --scale=3 "+ basename +".ps");
      ret = pr.waitFor();
    }
    /*    if (ret == 0) {
          pr = rt.exec("xdvi " + path.substring(0,path.length()-4) +".dvi");
          pr.waitFor();} */
    else
      System.err.println("An error occurred during the LaTeX compilation.");
  }

}
