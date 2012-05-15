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

  private static String tirname(String s) {
    return %[\mbox{\small{\textsc{@s@}}}]%;
  }

  private static String translate(RuleType rt) {
    %match(rt) {
      axiomInfo[] ->  { return tirname("ax"); }
      impliesLeftInfo[] -> { return tirname("$\\Rightarrow$-l"); }
      impliesRightInfo[]-> { return tirname("$\\Rightarrow$-r"); }
      andLeftInfo[] -> { return tirname("$\\land$-l"); }
      andRightInfo[] -> { return tirname("$\\land$-r"); }
      orLeftInfo[] -> { return tirname("$\\lor$-l"); }
      orRightInfo[] -> { return tirname("$\\lor$-r"); }
      forallRightInfo[] -> { return tirname("$\\forall$-r"); }
      forallLeftInfo[] -> { return tirname("$\\forall$-l"); }
      existsRightInfo[] -> { return tirname("$\\exists$-r"); }
      existsLeftInfo[] -> { return tirname("$\\exists$-l"); }
      bottomInfo[] -> { return tirname("$\\bot$-l"); }
      topInfo[] -> { return tirname("$\\top$-r"); }
      cutInfo(_p) -> { return tirname("cut"); }
      openInfo[] -> { return tirname("open"); }
      reductionInfo[] -> { return tirname("$\\equiv$"); }
      contractionLeftInfo[] -> { return tirname("contr-l"); }
      contractionRightInfo[] -> { return tirname("contr-r"); }
      weakLeftInfo() -> { return tirname("weak-l"); }
      weakRightInfo() -> { return tirname("weak-r"); }
      customRuleInfo[name=n] -> { return tirname(`n); }
      foldRightInfo[num=n] -> { return tirname(%[fold-r[@`n@]]%); }
      foldLeftInfo[num=n] -> { return tirname(%[fold-l[@`n@]]%); }
      metaVariableInfo[] -> { return ""; }
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
      rule(metaVariableInfo(mv),premisses(),c,_) -> {return "\\infer{"+ toLatex(`c) +"}\n{"+`toLatex(mv)+"}";}
      rule(reductionInfo[],premisses(p),_c,_) -> { return toLatex(`p); }
      rule((weakRightInfo|weakLeftInfo)[],p,_c,_) -> { return toLatex(`p); }
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

    %match(TermList term) {
      concTerm() -> { return ""; }
      concTerm(x) -> { return toLatex(`x); }
      concTerm(h,t*) -> { return toLatex(`h) + ", " + toLatex(`t); }
    }

    %match(Term term) {
      Var(concString('@',n*)) -> { return `n; }
      Var(n) -> { return `n; }

      // arithmetic
      funAppl("rhd",concTerm(x,y)) -> {
        return toLatex(`x) + " \\rhd " + toLatex(`y);
      }

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
      funAppl("supset",concTerm(t1,t2)) -> {
	return "(" + toLatex(`t1) + ") \\supset (" + toLatex(`t2) + ")";
      }
      funAppl("emptyset",concTerm()) -> {
        return "\\emptyset";
      }

      // finite 1st order theory of classes pretty print
      funAppl("app",concTerm(p,x*)) -> {
        return `toLatex(p) + "\\ @\\ "+ toLatex(`x*);
      }
      funAppl("nil",concTerm()) -> {
        return ("\\nil");
      }
      funAppl("fEq",concTerm(x,y)) -> {
        return toLatex(`x) + "\\dot{=}" + `toLatex(y);
      }
      l@funAppl("cons",concTerm(x,y)) -> {
        if (endedByNil(`l)) return "[" + listToLatex(`l) + "]";
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
       return ("\\dot\\pi_* ") + toLatex(`x) + " .~" +  toLatex(`y);
     }
     funAppl("pikind",concTerm(x,y)) -> {
       return ("\\dot\\pi_\\square ") + toLatex(`x) + " .~" +  toLatex(`y);
     }
     funAppl("pitt",concTerm(x,y)) -> {
       return ("\\dot\\pi_{***} ") + toLatex(`x) + " .~" +  toLatex(`y);
     }
     funAppl("pitk",concTerm(x,y)) -> {
       return ("\\dot\\pi_{*\\square\\square} ") + toLatex(`x) + " .~" +  toLatex(`y);
     }
     funAppl("pikt",concTerm(x,y)) -> {
       return ("\\dot\\pi_{\\square**} ") + toLatex(`x) + " .~" +  toLatex(`y);
     }
     funAppl("pikk",concTerm(x,y)) -> {
       return ("\\dot\\pi_{\\square\\square\\square} ") + toLatex(`x) + " .~" +  toLatex(`y);
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
      // Emilie
      funAppl("int",concTerm()) -> { return %[{\bf int}]%; }
      funAppl("string",concTerm()) -> { return %[{\bf string}]%; }
      funAppl("unit",concTerm()) -> { return %[{\bf unit}]%; }
      funAppl("est_pair",concTerm()) -> { return %[\mbox{``est pair''}]%; }
      funAppl("est_impair",concTerm()) -> { return %[\mbox{``est impair''}]%; }
      funAppl("let_in",concTerm(funAppl("var",concTerm(x)),t,u)) -> { return %[{\bf let~var}~@`toLatex(x)@~{\bf :=}~(@`toLatex(t)@)~{\bf in}~(@`toLatex(u)@)~{\bf end}]%; }
      funAppl("let_in",concTerm(funAppl("defun",concTerm(f)),T,u,v)) -> { return %[{\bf let~defun}~@`toLatex(f)@() : @`toLatex(T)@ = (@`toLatex(u)@)~{\bf in}~(@`toLatex(v)@)~{\bf end}]%; }
      funAppl("let_in",concTerm(funAppl("defun",concTerm(f,x1,T1)),T,u,v)) -> { return %[{\bf let~defun}~@`toLatex(f)@(@`toLatex(x1)@ : @`toLatex(T1)@) : @`toLatex(T)@ = (@`toLatex(u)@)~{\bf in}~(@`toLatex(v)@)~{\bf end}]%; }
      funAppl("range",concTerm(dom,T)) -> { return %[@`toLatex(dom)@ \rightarrow @`toLatex(T)@]%; }
      funAppl("dom",concTerm()) -> { return %[\langle~\rangle]%; }
      funAppl("dom",concTerm(x)) -> { return %[\langle @`toLatex(x)@ \rangle]%; }
      funAppl("dom",concTerm(x,y)) -> { return %[\langle @`toLatex(x)@ \times @`toLatex(y)@ \rangle]%; }
      funAppl("while",concTerm(u,v)) -> { return %[{\bf while}~(@`toLatex(u)@)~{\bf do}~(@`toLatex(v)@)]%; }



      // Hoare triples
      funAppl("substitute",concTerm(P,x,e))  -> {
        return `toLatex(P) + "["
          + `toLatex(e) + "/" + `toLatex(x) + "]";
      }
      funAppl("var",concTerm(funAppl(x,concTerm())))  -> {
        return `x;
      }
      funAppl("seq",concTerm(x,y))  -> {
        return `toLatex(x) + "~;~" + `toLatex(y);
      }
      funAppl("and",concTerm(a,b)) -> {
        return `toLatex(a) + "\\dot{\\land}" + `toLatex(b);
      }
      funAppl("impl",concTerm(a,b)) -> {
        return `toLatex(a) + "\\dot{\\rightArrow}" + `toLatex(b);
      }
      funAppl("if_then_else",concTerm(a,b,c)) -> {
        return %[{\bf if}~(@`toLatex(a)@)~{\bf then}~(@`toLatex(b)@)~{\bf else}~(@`toLatex(c)@)]%;
      }
      l@funAppl("arg_cons",concTerm(x,y)) -> {
        if(endedByArgNil(`l)) return argListToLatex(`l) ;
        else return toLatex(`x) + ":" + toLatex(`y);
      }
      funAppl("arg_nil",concTerm()) -> {
        return "[]";
      }
      funAppl("predappl",concTerm(funAppl(p,concTerm()),l)) -> {
        return  "\\overline{" + `p + "}(" + `toLatex(l) + ")";
      }
      funAppl("affect",concTerm(x,e)) -> {
        return `toLatex(x) + ":=" + `toLatex(e);
      }
      funAppl("while",concTerm(b,e)) -> {
        return %[while\ @`toLatex(b)@\ do\ @`toLatex(e)@\ done]%;
      }
      funAppl("beq",concTerm(x,y)) -> {
        return "(" + `toLatex(x) + " = " + `toLatex(y) + ")";
      }
      funAppl("bnot",concTerm(x)) -> {
        return "!(" + `toLatex(x) + ")";
      }


     funAppl(n, concTerm()) -> { return `n + "()";}
     funAppl(n, tlist) -> { return `n + "(" + toLatex(`tlist) + ")";}
    }

    return null;
  }

  public static String toLatex(Prop p) { return toLatex(p,0); }
  public static String toLatex(Prop p, int prec) {
    %match(Prop p) {
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
      relationAppl("int",concTerm(x,y)) -> {
        return toLatex(`x) + " \\interm " + toLatex(`y);
      }
      relationAppl("inp",concTerm(x,y)) -> {
        return toLatex(`x) + " \\intermlist " + toLatex(`y);
      }
      relationAppl("intlist",concTerm(x,y)) -> {
        return toLatex(`x) + " \\inpattern " + toLatex(`y);
      }
      relationAppl("inplist",concTerm(x,y)) -> {
        return toLatex(`x) + " \\inpatternlist " + toLatex(`y);
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

      // Hoare Triples
      relationAppl("Hoare",concTerm(P,S,Q)) -> {
        return "\\{" + `toLatex(P) + "\\}" + `toLatex(S)  + "\\{" + `toLatex(Q) + "\\}";
      }
      relationAppl("eps",concTerm(x)) -> {
        return "\\varepsilon(" + `toLatex(x) + ")";
      }

/*
fa ex => \/ /\ ~ atom
0  0  1  2  3  4  5
*/

      relationAppl(n, concTerm()) -> { return `n;}
      relationAppl(n, tlist) -> { return `n + "(" + toLatex(`tlist) + ")";}

      implies(p1, bottom()) -> { if (prec <= 4) return "\\lnot " + toLatex(`p1,4); }

      implies(p1,p2) -> {
        if (prec <= 1) return %[@toLatex(`p1,2)@ \Rightarrow @toLatex(`p2,1)@]%;
      }
      or(p1,p2) -> {
        if (prec <= 2) return %[@toLatex(`p1,2)@ \lor @toLatex(`p2,3)@]%;
      }
      and(p1,p2) -> {
        if (prec <= 3) return %[@toLatex(`p1,3)@ \land @toLatex(`p2,4)@]%;
      }
      forall(n, p1) -> { if (prec <= 0) return "\\forall " + `n + ", " + toLatex(`p1,0);}
      exists(n, p1) -> { if (prec <= 0) return "\\exists " + `n + ", " + toLatex(`p1,0);}
      bottom() -> { return "\\bot";  }
      top() -> { return "\\top";  }
      p1 -> { return %[(@`toLatex(p1,0)@)]%; }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static String toLatex(urbanAbstractType term) {

    %match(Meta term) {
      mvar(name) -> { return `name; }
      rename(n1,n2,mv) -> {return %[@`toLatex(mv)@[@`toLatex(n1)@ \mapsto @`toLatex(n2)@]]%; }
      reconame(cn1,cn2,mv) -> {return %[@`toLatex(mv)@[@`toLatex(cn1)@ \mapsto @`toLatex(cn2)@]]%;}
      substconame(cn,n,pt,phi,mv) -> {
        return %[@`toLatex(mv)@[@`toLatex(cn)@ := \hat{@`toLatex(n)@}\langle @`toLatex(pt)@:@`toLatex(phi)@\rangle]]%;
      }
      substname(n,cn,pt,phi,mv) -> {
        return %[@`toLatex(mv)@[@`toLatex(n)@ := \hat{@`toLatex(cn)@}\langle @`toLatex(pt)@:@`toLatex(phi)@\rangle]]%;
      }
    }

    %match(NTree term) {
      nrule(metaVariableInfo(mv),npremisses(),c,_) -> {return "\\infer{"+ toLatex(`c) +"}\n{"+`toLatex(mv)+"}";}
      nrule(n,npremisses(),c,pt) -> {return "\\infer["+ translate(`n) +"]\n{ "+toLatex(`pt)+" \\rhd "+ toLatex(`c) +"}\n{}";}
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
      name(n) -> {return "x_{"+`n+"}";}
    }

    %match (CoName term) {
      coname(n) -> {return "a_{"+`n+"}"; }
    }

    %match (ProofTerm term) { // INCOMPLET manque le 1er ordre
      metaVar(mv) -> { return `toLatex(mv); }
      foldL(x,m,y,i) -> { return "{\\sf Fold}_L^"+`i +"(\\langle "+toLatex(`x)+"\\rangle "+toLatex(`m)+", "+toLatex(`y)+")"; }
      foldR(a,m,b,i) -> { return "{\\sf Fold}_R^"+`i +"(\\langle "+toLatex(`a)+"\\rangle "+toLatex(`m)+", "+toLatex(`b)+")"; }
      ax(n,cn) -> {return "{\\sf Ax}("+toLatex(`n)+","+toLatex(`cn)+")";}
      cut(a,m1,x,m2) -> {return "{\\sf Cut}(\\langle "+toLatex(`a)+"\\rangle "+toLatex(`m1)+", \\langle "+toLatex(`x)+" \\rangle "+toLatex(`m2)+")";}
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


  // Hoare pretty-print
  private static boolean endedByArgNil(Term l) {
    %match(Term l) {
      funAppl("arg_cons",concTerm(_,funAppl("arg_nil",concTerm()))) -> { return true; }
      funAppl("arg_cons",concTerm(_,y)) -> { return endedByArgNil(`y); }
    }
    return false;
  }

  public static String prettyArgList(Term t) {
    %match(Term t) {
      funAppl("arg_cons",concTerm(x,funAppl("arg_nil",concTerm()))) -> {
        return prettyPrint(`x);
      }
      funAppl("arg_cons",concTerm(x,y)) -> {
        return prettyPrint(`x) + "," + prettyArgList(`y);
      }
    }
    return null;
  }

  public static String argListToLatex(Term t) {
    %match(Term t) {
      funAppl("arg_cons",concTerm(x,funAppl("arg_nil",concTerm()))) -> {
        return toLatex(`x);
      }
      funAppl("arg_cons",concTerm(x,y)) -> {
        return toLatex(`x) + "," + argListToLatex(`y);
      }
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

  public static String prettyList(Term t) {
    %match(Term t) {
      funAppl("cons",concTerm(x,funAppl("nil",concTerm()))) -> { return prettyPrint(`x); }
      funAppl("cons",concTerm(x,y)) -> {
        return prettyPrint(`x) + "," + prettyList(`y);
      }
    }
    return null;
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
        return prettyPrint(`x) + " : " + prettyPrint(`y);
      }

      // Hoare triples
      relationAppl("Hoare",concTerm(P,S,Q)) -> {
        return "{" + `prettyPrint(P) + "} "
          + `prettyPrint(S)  + " {" + `prettyPrint(Q) + "}";
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

      // Emilie
      funAppl("est_pair",concTerm()) -> { return %["est pair"]%; }
      funAppl("est_impair",concTerm()) -> { return %["est impair"]%; }
      funAppl("let_in",concTerm(funAppl("var",concTerm(x)),t,u)) -> { return %[let var @`prettyPrint(x)@ := (@`prettyPrint(t)@) in (@`prettyPrint(u)@) end]%; }
      funAppl("let_in",concTerm(funAppl("defun",concTerm(f)),T,u,v)) -> { return %[let defun @`prettyPrint(f)@() : @`prettyPrint(T)@ = (@`prettyPrint(u)@) in (@`prettyPrint(v)@) end]%; }
      funAppl("let_in",concTerm(funAppl("defun",concTerm(f,x1,T1)),T,u,v)) -> { return %[let defun @`prettyPrint(f)@(@`prettyPrint(x1)@ : @`prettyPrint(T1)@) : @`prettyPrint(T)@ = (@`prettyPrint(u)@) in (@`prettyPrint(v)@) end]%; }
      funAppl("range",concTerm(dom,T)) -> { return %[@`prettyPrint(dom)@ -> @`prettyPrint(T)@]%; }
      funAppl("dom",concTerm()) -> { return "<>"; }
      funAppl("dom",concTerm(x)) -> { return "<"+ `prettyPrint(x) + ">"; }
      funAppl("dom",concTerm(x,y)) -> { return %[<@`prettyPrint(x)@ x @`prettyPrint(y)@>]%; }
      funAppl("while",concTerm(u,v)) -> { return %[while (@`prettyPrint(u)@) do (@`prettyPrint(v)@)]%; }


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
      funAppl("pitt",concTerm(x,y))  -> {
        return "π⁎⁎⁎" + prettyPrint(`x) + ". " + prettyPrint(`y);
      }
      funAppl("pitk",concTerm(x,y))  -> {
        return "π⁎◽◽" + prettyPrint(`x) + ". " + prettyPrint(`y);
      }
      funAppl("pikt",concTerm(x,y))  -> {
        return "π◽⁎⁎" + prettyPrint(`x) + ". " + prettyPrint(`y);
      }
      funAppl("pikk",concTerm(x,y))  -> {
        return "π◽◽◽" + prettyPrint(`x) + ". " + prettyPrint(`y);
      }

      // Hoare triples
      funAppl("substitute",concTerm(P,x,e))  -> {
        return `prettyPrint(P) + "["
          + `prettyPrint(e) + "/" + `prettyPrint(x) + "]";
      }
      funAppl("var",concTerm(funAppl(x,concTerm())))  -> {
        return "\"" + `x + "\"";
      }
      funAppl("seq",concTerm(x,y))  -> {
        return `prettyPrint(x) + " ; " + `prettyPrint(y);
      }
      funAppl("and",concTerm(a,b)) -> {
        return `prettyPrint(a) + " && " + `prettyPrint(b);
      }
      funAppl("not",concTerm(b)) -> {
        return "!(" + `prettyPrint(b) + ")";
      }
      funAppl("impl",concTerm(a,b)) -> {
        return `prettyPrint(a) + " ~> " + `prettyPrint(b);
      }
      funAppl("if_then_else",concTerm(a,b,c)) -> {
        return "if (" + `prettyPrint(a) + ") then (" + `prettyPrint(b) + ") else (" + `prettyPrint(c) + ")";
      }
      l@funAppl("arg_cons",concTerm(x,y)) -> {
        if(endedByArgNil(`l)) return "[" + prettyArgList(`l) + "]";
        else return prettyPrint(`x) + ":" + prettyPrint(`y);
      }
      funAppl("arg_nil",concTerm()) -> {
        return "[]";
      }
      funAppl("predappl",concTerm(funAppl(p,concTerm()),l)) -> {
        return "@(" +`p + "," + `prettyPrint(l) + ")";
      }
      funAppl("affect",concTerm(x,e)) -> {
        return `prettyPrint(x) + ":=" + `prettyPrint(e);
      }



      // normal case

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

    %match(TermRule term) {
       termrule(lhs, rhs) ->
       { return prettyPrint(`lhs) + " -> " + prettyPrint(`rhs); }
     }

    %match(PropRule term) {
       proprule(lhs, rhs) ->
       { return prettyPrint(`lhs) + " -> " + prettyPrint(`rhs); }
     }

    return term.toString();
  }

  public static String prettyPrint(urbanAbstractType term) {

    %match(Meta term) {
      mvar(name) -> { return `name; }
      rename(n1,n2,mv) -> {return %[@`prettyPrint(mv)@[@`prettyPrint(n1)@ -> @`prettyPrint(n2)@]]%; }
      reconame(cn1,cn2,mv) -> {return %[@`prettyPrint(mv)@[@`prettyPrint(cn1)@ -> @`prettyPrint(cn2)@]]%;}
      substconame(cn,n,pt,phi,mv) -> {
        return %[@`prettyPrint(mv)@[@`prettyPrint(cn)@ := <@`prettyPrint(n)@><@`prettyPrint(pt)@:@`prettyPrint(phi)@>]]%;
      }
      substname(n,cn,pt,phi,mv) -> {
        return %[@`prettyPrint(mv)@[@`prettyPrint(n)@ := <@`prettyPrint(cn)@}><@`prettyPrint(pt)@:@`prettyPrint(phi)@>]]%;
      }
    }

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

    %match(DBName term) {
      dbname(i) -> { return "n("+`i+")";}
    }

    %match(DBCoName term) {
      dbconame(i) -> { return "cn("+`i+")";}
    }

    %match(DBProofTerm term) {
      DBmetaVar(mv) -> { return `prettyPrint(mv); }
      DBfoldL(x,m,y,i) -> { return "foldL_"+`i +"(<n:"+prettyPrint(`x)+"> "+prettyPrint(`m)+", "+prettyPrint(`y)+")"; }
      DBfoldR(a,m,b,i) -> { return "foldR_"+`i +"(<cn:"+prettyPrint(`a)+"> "+prettyPrint(`m)+", "+prettyPrint(`b)+")"; }
      DBax(n,cn) -> {return "ax("+prettyPrint(`n)+", "+prettyPrint(`cn)+")"; }
      DBcut(a,m1,x,m2) -> {return "cut(<cn:"+prettyPrint(`a)+"> "+prettyPrint(`m1)+", <n:"+prettyPrint(`x)+"> "+prettyPrint(`m2)+")";}
      DBfalseL(n) -> {return "falseL("+prettyPrint(`n)+")";}
      DBtrueR(cn) -> {return "trueR("+prettyPrint(`cn)+")";}
      DBandR(a,m1,b,m2,nc) -> {return "andR(<cn:"+prettyPrint(`a)+"> "+prettyPrint(`m1)+", <cn:"+prettyPrint(`b)+"> "+prettyPrint(`m2)+","+prettyPrint(`nc)+")" ;}
      DBandL(x,y,m,n) -> {return "andL(<n:"+prettyPrint(`x)+"><n:"+prettyPrint(`y)+"> "+prettyPrint(`m)+", "+prettyPrint(`n)+")" ;}
      DBorR(a,b,m,cn) -> {return "orR(<cn:"+prettyPrint(`a)+"><cn:"+prettyPrint(`b)+"> "+prettyPrint(`m)+", "+prettyPrint(`cn)+")" ;}
      DBorL(x,m1,y,m2,n) -> {return "orL(<n:"+prettyPrint(`x)+"> "+prettyPrint(`m1)+", <n:"+prettyPrint(`y)+"> "+prettyPrint(`m2)+", "+prettyPrint(`n)+")" ;}
      DBimplyR(x,a,m,cn) -> {return "implyR(<n:"+prettyPrint(`x)+"><cn:"+prettyPrint(`a)+"> "+prettyPrint(`m)+", "+prettyPrint(`cn)+")" ;}
      DBimplyL(a,m1,x,m2,n) -> {return "implyL(<cn:"+prettyPrint(`a)+"> "+prettyPrint(`m1)+", <n:"+prettyPrint(`x)+"> "+prettyPrint(`m2)+","+prettyPrint(`n)+")" ;}
      DBexistsL(x,varx,m,n) -> {return "existsL(<n:"+prettyPrint(`x)+">{"+prettyPrint(`varx)+"} "+prettyPrint(`m)+", "+prettyPrint(`n)+")" ;}
      DBexistsR(a,m,t,cn) -> {return "existsR(<cn:"+prettyPrint(`a)+"> "+prettyPrint(`m)+", "+prettyPrint(`t)+", "+prettyPrint(`cn)+")";}
      DBforallL(x,m,t,n) -> {return "forallL(<n:"+prettyPrint(`x)+"> "+prettyPrint(`m)+", "+prettyPrint(`t)+", "+prettyPrint(`n)+")";}
      DBforallR(a,varx,m,cn) -> {return "forallR(<cn:"+prettyPrint(`a)+">{"+prettyPrint(`varx)+"} "+prettyPrint(`m)+", "+prettyPrint(`cn)+")" ;}
    }

    %match(ProofTerm term) {
      metaVar(mv) -> { return `prettyPrint(mv); }
      foldL(x,m,y,i) -> { return "foldL_"+`i +"("+prettyPrint(`x)+" "+prettyPrint(`m)+", "+prettyPrint(`y)+")"; }
      foldR(a,m,b,i) -> { return "foldR_"+`i +"("+prettyPrint(`a)+" "+prettyPrint(`m)+", "+prettyPrint(`b)+")"; }
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

    %match(NSequent term) {
      nsequent(nctxt,cnctxt) -> { return prettyPrint(`nctxt) + " |- " + prettyPrint(`cnctxt); }
    }

    return term.toString();
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
    tree = cleanTree(tree, tl, pl);
    tree = (Tree) Unification.reduce(tree,tl,pl);
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
