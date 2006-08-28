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

  // export latex
  private static HashMap dict = new HashMap();

  // static bloc
  static {
    dict.put("axiom", "\\mathop{ax}");  
    dict.put("implies I", "\\Rightarrow_\\mathcal{I}");  
    dict.put("implies E", "\\Rightarrow_\\mathcal{E}");
    dict.put("implies R", "\\Rightarrow_\\mathcal{R}");
    dict.put("implies L", "\\Rightarrow_\\mathcal{L}");
    dict.put("and I", "\\land_\\mathcal{I}");  
    dict.put("and E", "\\land_\\mathcal{E}");
    dict.put("and R", "\\land_\\mathcal{R}");
    dict.put("and L", "\\land_\\mathcal{L}");
    dict.put("or I", "\\lor_\\mathcal{I}");
    dict.put("or E", "\\lor_\\mathcal{E}");
    dict.put("or R", "\\lor_\\mathcal{R}");
    dict.put("or L", "\\lor_\\mathcal{L}");
    dict.put("forAll R", "\\forall_\\mathcal{R}");
    dict.put("forAll L", "\\forall_\\mathcal{L}");
    dict.put("exists R", "\\exists_\\mathcal{R}");
    dict.put("exists L", "\\exists_\\mathcal{L}");
    dict.put("bottom", "\\bot");
    dict.put("top", "\\top");
    dict.put("cut", "\\mathop{cut}");
  }

  private static String translate(String name) {
    String res = (String) dict.get(name);
    if (res != null) return res;
    else  return name;
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

  %strategy IsActive(prop: Prop, tl: TermRuleList) extends `Fail() {
    visit Tree {
      // all propositions are virtually used in an open branch
      r@rule[name="open"] -> { return `r; }
      
      // in case of a reduce rule, we have to check if the reduced form of a prop is active
      r@rule("reduce",_,concl,_) -> {
        %match(Sequent `concl, Prop prop) {
          sequent((_*,p,_*),_), p -> {
            Prop after = (Prop) Unification.reduce(`p,tl);
            if (`p != after) 
                return `r;
            }
          sequent(_,(_*,p,_*)), p -> {
            Prop after = (Prop) Unification.reduce(`p,tl);
            if (`p != after) 
                return `r;
          }
        }
      }
      
      r@rule(name,_,_,p) -> { if (prop == `p) { return `r; } }
    }
  }

  %strategy Clean(tl: TermRuleList) extends `Identity() {
    visit Tree {
      r@rule(_,_,sequent(h,c),_) -> {
        Tree res = `r;
        %match(Context h) {
          (_*,x,_*) -> {
            res = (Tree) 
              ((MuStrategy) `Choice(OnceTopDown(IsActive(x,tl)),InnermostId(RemoveInHyp(x)))).apply(`res);
          }
        }
        %match(Context c) {
          (_*,x,_*) -> {
            res = (Tree) 
              ((MuStrategy) `Choice(OnceTopDown(IsActive(x,tl)),InnermostId(RemoveInConcl(x)))).apply(`res);
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
  public static Tree cleanTree(Tree tree, TermRuleList tl) {
    return (Tree) ((MuStrategy) `TopDown(Clean(tl))).apply(tree);
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
      ruledesc(hs,c,p) -> {
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

  public static void display(Tree tree, TermRuleList tl) throws java.io.IOException, java.lang.InterruptedException {
    tree = cleanTree(tree, tl);
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
