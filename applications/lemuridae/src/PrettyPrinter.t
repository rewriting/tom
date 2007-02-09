import sequents.*;
import sequents.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.MuStrategy;
import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;

import java.io.*;

class PrettyPrinter {

  %include { sequents/sequents.tom }
  %include { mutraveler.tom }
  %typeterm Set {implement {Set}}
  %typeterm Collection { implement {Collection}}

  private static String translate(RuleType rt) {
    %match(rt) {
      axiomInfo[] ->  { return "axiom"; }
      impliesLeftInfo[] -> { return "\\Rightarrow_\\mathcal{L}"; }
      impliesRightInfo[]-> { return "\\Rightarrow_\\mathcal{R}"; }
      andLeftInfo[] -> { return "\\land_\\mathcal{L}"; }
      andRightInfo[] -> { return "\\land_\\mathcal{R}"; }
      orLeftInfo[] -> { return "\\lor_\\mathcal{L}"; }
      orRightInfo[] -> { return "\\lor_\\mathcal{R}"; }
      forAllRightInfo[] -> { return "\\forall_\\mathcal{R}"; }
      forAllLeftInfo[] -> { return "\\forall_\\mathcal{L}"; }
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
      funAppl(fun("z"),()) -> { return i; }
      funAppl(fun("succ"),(t)) -> { return peanoToInt(`t,i+1); }
    }
    throw new Exception("term is not an integer");
  }

  public static Term intToPeano(int n) {
    Term res = `funAppl(fun("z"),concTerm());
    for (int i=0; i<n; i++) {
      res = `funAppl(fun("succ"),concTerm(res));
    }
    return res;
  }

  // --- auxiliary strats for cleanTree ----

  %strategy RemoveInHyp(prop: Prop) extends `Identity() {
    visit Sequent {
      sequent((X*,p,Y*), c) -> { if (prop == `p) return `sequent(context(X*,Y*),c); }
    }
  }

  %strategy RemoveInConcl(prop: Prop) extends `Identity() {
    visit Sequent {
      sequent(h, (X*,p,Y*)) -> { if (prop == `p) return `sequent(h,context(X*,Y*)); }
    }
  }

  %strategy IsActive(prop: Prop, tl: TermRuleList, pl: PropRuleList) extends `Fail() {
    visit Tree {
      // all propositions are virtually used in an open branch
      r@rule[type=openInfo[]] -> { return `r; }

      // in case of a reduce rule, we have to check if the reduced form of a prop is active
      r@rule(reductionInfo[],_,concl,_) -> {
        %match(Sequent `concl, Prop prop) {
          sequent((_*,p,_*),_), p -> {
            Prop after = (Prop) Unification.reduce(`p,tl,pl);
            if (`p != after) 
              return `r;
          }
          sequent(_,(_*,p,_*)), p -> {
            Prop after = (Prop) Unification.reduce(`p,tl,pl);
            if (`p != after) 
              return `r;
          }
        }
      }

      r@rule(name,_,_,p) -> { if (prop == `p) { return `r; } }
    }
  }

  %strategy Clean(tl: TermRuleList, pl: PropRuleList) extends `Identity() {
    visit Tree {
      r@rule(_,_,sequent(h,c),_) -> {
        Tree res = `r;
        %match(Context h) {
          (_*,x,_*) -> {
            res = (Tree) 
              ((MuStrategy) `Choice(OnceTopDown(IsActive(x,tl,pl)),InnermostId(RemoveInHyp(x)))).apply(`res);
          }
        }
        %match(Context c) {
          (_*,x,_*) -> {
            res = (Tree) 
              ((MuStrategy) `Choice(OnceTopDown(IsActive(x,tl,pl)),InnermostId(RemoveInConcl(x)))).apply(`res);
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
    return (Tree) ((MuStrategy) `TopDown(Clean(tl,pl))).apply(tree);
  }

  public static String toLatex(sequentsAbstractType term) {
    %match(Tree term) {
      rule(n,(),c,_) -> {return "\\infer["+ translate(`n) +"]{" + toLatex(`c) + "}{}";}
      rule(n,p,c,_) -> {return "\\infer["+ translate(`n) +"]{" + toLatex(`c) + "}{"+ toLatex(`p) +"}";}
    }

    %match(Premisses term) {
      () -> { return ""; }
      (x) -> { return toLatex(`x); }
      (h,t*) -> { return toLatex(`h) + " & " + toLatex(`t*); }
    }

    %match(Context term) {
      () -> { return ""; }
      (x) -> { return toLatex(`x); }
      (h,t*) -> {return toLatex(`h) + ", " + toLatex(`t);}
    }

    %match(Sequent term) {
      sequent((), c) -> { return "\\vdash " + toLatex(`c); }
      sequent(ctx, c) -> { return toLatex(`ctx) + " \\vdash " + toLatex(`c); }
    }

    %match(Prop term) {

      // arithmetic pretty print
      relationAppl(relation("eq"),(x,y)) -> {
        return toLatex(`x) + " = " + toLatex(`y);
      }
      relationAppl(relation("gt"),(x,y)) -> {
        return toLatex(`x) + " > " + toLatex(`y);
      }
      relationAppl(relation("lt"),(x,y)) -> {
        return toLatex(`x) + " < " + toLatex(`y);
      }
      relationAppl(relation("le"),(x,y)) -> {
        return toLatex(`x) + " \\le " + toLatex(`y);
      }

      // set theory pretty print
      relationAppl(relation("in"),(x,y)) -> {
        return toLatex(`x) + " \\in " + toLatex(`y);
      }
      relationAppl(relation("subset"),(x,y)) -> {
        return toLatex(`x) + " \\subset " + toLatex(`y);
      }
      relationAppl(relation("supset"),(x,y)) -> {
        return toLatex(`x) + " \\supset " + toLatex(`y);
      }

      relationAppl(relation[name=n], ()) -> { return `n;}
      relationAppl(relation[name=n], tlist) -> { return `n + "(" + toLatex(`tlist) + ")";}
      and(p1, p2) -> { return "(" + toLatex(`p1) + " \\land " + toLatex(`p2) + ")";}
      or(p1, p2) -> { return "(" + toLatex(`p1) + " \\lor " + toLatex(`p2) + ")";}
      //negation
      implies(p, bottom()) -> { return "\\lnot (" + toLatex(`p) + ")"; }
      implies(p1, p2) -> { return "(" + toLatex(`p1) + " \\Rightarrow " + toLatex(`p2) + ")";}
      forAll(n, p) -> { return "\\forall " + `n + " . (" + toLatex(`p) + ")";}
      exists(n, p) -> { return "\\exists " + `n + " . (" + toLatex(`p) + ")";}
      bottom() -> { return "\\perp";  }
      top() -> { return "\\top";  }
    }	

    %match(TermList term) {
      (x) -> { return toLatex(`x); }
      (h,t*) -> { return toLatex(`h) + ", " + toLatex(`t); }
    }

    %match(Term term) {
      Var(n) -> { return `n; }

      // arithmetic
      funAppl(fun("z"),()) -> { return "0"; }
      i@funAppl(fun("succ"),x) -> {
        try { return Integer.toString(peanoToInt(`i));}
        catch (Exception e) {}
      }
      funAppl(fun("plus"),(t1,t2)) -> { 
        return "(" + toLatex(`t1) + "+" + toLatex(`t2) + ")";
      }
      funAppl(fun("mult"),(t1,t2)) -> { 
        return "(" + toLatex(`t1) + " \\times " + toLatex(`t2) + ")";
      }
      funAppl(fun("minus"),(t1,t2)) -> { 
        return "(" + toLatex(`t1) + " - " + toLatex(`t2) + ")";
      }
      funAppl(fun("div"),(t1,t2)) -> { 
        return "(" + toLatex(`t1) + " / " + toLatex(`t2) + ")";
      }

      // set theory
      funAppl(fun("union"),(t1,t2)) -> { 
        return "(" + toLatex(`t1) + ") \\cup (" + toLatex(`t2) + ")";
      }
      funAppl(fun("inter"),(t1,t2)) -> { 
        return "(" + toLatex(`t1) + ") \\cap (" + toLatex(`t2) + ")";
      }
      funAppl(fun("emptyset"),()) -> { 
        return "\\emptyset";
      }

      // finite 1st order theory of classes pretty print
      funAppl(fun("appl"),(p,x*)) -> {
        return `toLatex(p) + "["+ toLatex(`x*) + "]";
      }
      funAppl(fun("nil"),()) -> {
        return ("nil");
      }
      funAppl(fun("fEq"),(x,y)) -> {
        return toLatex(`x) + "\\dot{=}" + `toLatex(y);
      }
      l@funAppl(fun("cons"),(x,y)) -> {
        if (endedByNil(`l)) return "\\langle " + listToLatex(`l) + "\\rangle "; 
        else return toLatex(`x) + "::" + toLatex(`y);
      }

      funAppl(fun[name=n], ()) -> { return `n + "()";}
      funAppl(fun[name=n], tlist) -> { return `n + "(" + toLatex(`tlist) + ")";}
    }

    return null;
  }

  // finite 1st order theory of classes list pretty print
  private static boolean endedByNil(Term l) {
    %match(Term l) {
      funAppl(fun("cons"),(x,funAppl(fun("nil"),()))) -> { return true; }
      funAppl(fun("cons"),(_,y)) -> { return endedByNil(`y); }
    }
    return false;
  }

  public static String listToLatex(Term t) {
    %match(Term t) {
      funAppl(fun("cons"),(x,funAppl(fun("nil"),()))) -> { return toLatex(`x); }
      funAppl(fun("cons"),(x,y)) -> {
        return toLatex(`x) + "," + listToLatex(`y); 
      }
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
      () -> { return ""; }
      (f) -> { return prettyPrint(`f); }
      (h,t*) -> { return prettyPrint(`h) + ", " +  prettyPrint(`t); }
    }

    %match(Prop term) {
      and(p1,p2) -> { 
        return "(" + prettyPrint(`p1) + " /\\ " + prettyPrint(`p2) + ")";
      }
      or(p1,p2) -> { 
        return "(" + prettyPrint(`p1) + " \\/ " + prettyPrint(`p2) + ")";
      }
      implies(p1,p2) -> { 
        return "(" + prettyPrint(`p1) + " => " + prettyPrint(`p2) + ")";
      }
      forAll(x,p1) -> {
        return "forall " + `x + ".(" + prettyPrint(`p1) + ")";
      }
      exists(x,p1) -> { 
        return "exists " + `x + ".(" + prettyPrint(`p1) + ")";
      }
      relationAppl(relation(r),()) -> {
        return `r;
      }

      // arithmetic pretty print
      relationAppl(relation("eq"),(x,y)) -> {
        return prettyPrint(`x) + " = " + prettyPrint(`y);
      }
      relationAppl(relation("gt"),(x,y)) -> {
        return prettyPrint(`x) + " > " + prettyPrint(`y);
      }
      relationAppl(relation("lt"),(x,y)) -> {
        return prettyPrint(`x) + " < " + prettyPrint(`y);
      }
      relationAppl(relation("le"),(x,y)) -> {
        return prettyPrint(`x) + " <= " + prettyPrint(`y);
      }

      // set theory prettyprint
      relationAppl(relation("in"),(x,y)) -> {
        return prettyPrint(`x) + " in " + prettyPrint(`y);
      }

      relationAppl(relation(r),x) -> {
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
      Var(x) -> { return `x;}


      // arithmetic pretty print
      funAppl(fun("z"),()) -> { return "0"; }
      i@funAppl(fun("succ"),x) -> {
        try { return Integer.toString(peanoToInt(`i));}
        catch (Exception e) {}
      }
      funAppl(fun("plus"),(t1,t2)) -> { 
        return "(" + prettyPrint(`t1) + "+" + prettyPrint(`t2) + ")";
      }
      funAppl(fun("mult"),(t1,t2)) -> { 
        return "(" + prettyPrint(`t1) + "*" + prettyPrint(`t2) + ")";
      }
      funAppl(fun("minus"),(t1,t2)) -> { 
        return "(" + prettyPrint(`t1) + "-" + prettyPrint(`t2) + ")";
      }
      funAppl(fun("div"),(t1,t2)) -> { 
        return "(" + prettyPrint(`t1) + "/" + prettyPrint(`t2) + ")";
      }

      // finite 1st order theory of classes pretty print
      funAppl(fun("appl"),(p,x*)) -> {
        return `prettyPrint(p) + "["+ prettyPrint(`x*) + "]";
      }
      funAppl(fun("nil"),()) -> {
        return ("nil");
      }
      l@funAppl(fun("cons"),(x,y)) -> {
        if(endedByNil(`l)) return "<" + prettyList(`l) + ">"; 
        else return prettyPrint(`x) + "::" + prettyPrint(`y);
      }


      funAppl(fun(name),x) -> {
        return `name + "(" + prettyPrint(`x) + ")";
      }

      FreshVar(n,_) -> { return `n; }
      NewVar(n,_) -> { return `n; }
    }

    %match(TermList term) {
      () -> {return ""; }
      (h) -> { return prettyPrint(`h);}
      (h,t*) -> { return prettyPrint(`h) + ", " + prettyPrint(`t); }
    }

    return term.toString();
  }

  // finite 1st order theory of classes list pretty print
  public static String prettyList(Term t) {
    %match(Term t) {
      funAppl(fun("cons"),(x,funAppl(fun("nil"),()))) -> { return prettyPrint(`x); }
      funAppl(fun("cons"),(x,y)) -> {
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
    return res + " not in FV";
  }

  public static String prettyRule(sequentsAbstractType term) {

    %match(RuleList term) {
      (x) -> { return prettyRule(`x); }
      (t,q*) -> {	return prettyRule(`t) + "\n\n" + prettyRule(`q); }
    }

    %match(SeqList term) {
      () -> { return ""; }
      (x) -> { return prettyPrint(`x); }
      (t,q*) -> {	return prettyPrint(`t) + "    " + prettyRule(`q); }
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
    display(tree);
  }

  // displays a latex output in xdvi
  public static void display(sequentsAbstractType term) throws java.io.IOException, java.lang.InterruptedException {
    File tmp = File.createTempFile("output",".tex");
    FileWriter writer = new FileWriter(tmp);
    String path = tmp.getAbsolutePath();

    writer.write("\\documentclass{article}\n\\usepa"+"ckage{proof}\n\\begin{document}\n\\[\n");
    writer.write(toLatex(term));
    writer.write("\n\\]\n");
    writer.write("\\end{document}\n");
    writer.flush();

    System.out.println(path);
    Runtime rt = Runtime.getRuntime();
    Process pr = rt.exec("latex -output-directory=/tmp " + path );
    pr.waitFor(); 
    pr = rt.exec("xdvi " + path.substring(0,path.length()-4) +".dvi");
    pr.waitFor();
  }
}
