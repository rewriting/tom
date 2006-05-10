import sequents.*;
import sequents.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
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
    dict.put("axiom", "\\mathrel{Ax}");  
    dict.put("implies I", "\\Rightarrow I");  
    dict.put("implies E", "\\Rightarrow E");
    dict.put("implies R", "\\Rightarrow R");
    dict.put("implies L", "\\Rightarrow L");
    dict.put("and I", "\\wedge I");  
    dict.put("and E", "\\wedge E");
    dict.put("and R", "\\wedge R");
    dict.put("and L", "\\wedge L");
    dict.put("or I", "\\vee I");
    dict.put("or E", "\\vee E");
    dict.put("or R", "\\vee R");
    dict.put("or L", "\\vee L");
    dict.put("forAll R", "\\forall R");
    dict.put("forAll L", "\\forall L");
    dict.put("exists R", "\\exists R");
    dict.put("exists L", "\\exists L");
    dict.put("bottom", "\\perp");
  }

  private static String translate(String name) {
    String res = (String) dict.get(name);
    if (res != null) return res;
    else  return name;
  }

  // converts a context into a java set
  private static Set getContextSet(Context ctxt) {
    HashSet res = new HashSet();
    %match(Context ctxt) {
      (_*,x,_*) -> { res.add(`x); }
    }
    return res;
  }
 
  // Get set of sets
  %strategy GetContextsSet(result: Set) extends `Identity() {
    visit Sequent {
      sequent(ctxt,_) -> {
        result.add(getContextSet(`ctxt));
      }
    }
  }

 
  private static Context getGreaterCommonContext(Tree tree) {
    HashSet<Set<Prop>> tmp = new HashSet<Set<Prop>>();
    try {
      MuTraveler.init(`TopDown(GetContextsSet(tmp))).visit(tree);
    } catch (VisitFailure e) {
      e.printStackTrace();
    }

    Iterator<Set<Prop>> it = tmp.iterator();
    Set<Prop> res = it.next();
    while( it.hasNext() ) {
      res.retainAll((Set)it.next());
    }

    Context result = `context();
    for(Prop p : res) 
      result = `context(p,result*);
    

    return result;
  }

  %strategy ReplaceContext(ctxt: Context) extends `Identity() {
    visit Context {
      (X*, Y*, Z*) -> {
        if (`Y == ctxt) return `context(X*, Utils.prop("\\Gamma"), Z*);
      } 
    }
  }

  private static String prettyContext(Context ctxt) {
    %match(Context ctxt) {
      (h) -> { return toLatex(`h); }
      (h,t*) -> { return toLatex(`h) + ", " + prettyContext(`t); }
    }
    return null;
  }

  // finds greater comon context and replaces it by gamma
  public static String latexPreProcessing(Tree tree) {
    Context ctxt = getGreaterCommonContext(tree);

    try {
      tree = (Tree) MuTraveler.init(`TopDown(ReplaceContext(ctxt))).visit(tree);
    } catch (VisitFailure e) {
      e.printStackTrace();
    }

    return "$\\Gamma = " + prettyContext(ctxt) +"$\n\\[\n" + toLatex(tree) + "\n\\]";
  }

  public static String toLatex(sequentsAbstractType term) {
    %match(Tree term) {
      rule(n,(),c,_) -> {return "\\infer[("+ translate(`n) +")]{" + toLatex(`c) + "}{}";}
      rule(n,p,c,_) -> {return "\\infer[("+ translate(`n) +")]{" + toLatex(`c) + "}{"+ toLatex(`p) +"}";}
    }

    %match(Premisses term) {
      (x) -> { return toLatex(`x); }
      (h,t*) -> { return toLatex(`h) + " & " + toLatex(`t*); }
    }

    %match(Context term) {
      (x) -> { return toLatex(`x); }
      (h,t*) -> {return toLatex(`h) + ", " + toLatex(`t);}
    }

    %match(Sequent term) {
      sequent((), c) -> { return "\\vdash " + toLatex(`c); }
      sequent(ctx, c) -> { return toLatex(`ctx) + " \\vdash " + toLatex(`c); }
    }

    %match(Prop term) {
      relationAppl(relation[name=n], ()) -> { return `n;}
      relationAppl(relation[name=n], tlist) -> { return `n + "(" + toLatex(`tlist) + ")";}
      and(p1, p2) -> { return "(" + toLatex(`p1) + " \\wedge " + toLatex(`p2) + ")";}
      or(p1, p2) -> { return "(" + toLatex(`p1) + " \\vee " + toLatex(`p2) + ")";}
      implies(p1, p2) -> { return "(" + toLatex(`p1) + " \\Rightarrow " + toLatex(`p2) + ")";}
      forAll(n, p) -> { return "\\forall " + `n + " . (" + toLatex(`p) + ")";}
      exists(n, p) -> { return "\\exists " + `n + " . (" + toLatex(`p) + ")";}
      bottom() -> { return "\\perp";  }
      top() -> { return "\\mathrel(T)";  }
      nullProp() -> {
        return ""; 
      }

    }	

    %match(TermList term) {
      (x) -> { return toLatex(`x); }
      (h,t*) -> { return toLatex(`h) + ", " + toLatex(`t); }
    }

    %match(Term term) {
      Var(n) -> { return `n; }
      funAppl(fun[name=n], ()) -> { return `n + "()";}
      funAppl(fun[name=n], tlist) -> { return `n + "(" + toLatex(`tlist) + ")";}
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
      relationAppl(relation(r),x) -> {
        return `r + "(" + prettyPrint(`x) + ")";
      }
      bottom() -> {
        return "False";
      }
      top() -> {
        return "True";
      }
      nullProp() -> {
        return ""; 
      }
    }

    %match(Term term) {
      Var(x) -> { return `x;}
      funAppl(fun(f),x) -> { return `f + "(" + prettyPrint(`x) + ")";}
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
      (x) -> { return prettyPrint(`x); }
      (t,q*) -> {	return prettyPrint(`t) + "    " + prettyRule(`q); }
    }

    // generated rules
    %match(Rule term) {
      ruledesc(hs,c,p) -> {
        String r1 = prettyRule(`p);
        String r2;
        if(`hs==0)
          r2 = prettyPrint(`sequent(context(c),nullProp()));
        else
          r2 = prettyPrint(`sequent(context(),c));

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

    Runtime rt = Runtime.getRuntime();
    Process pr = rt.exec("latex -output-directory=/tmp "+ path );
    pr.waitFor(); 
    System.out.println(path);
    pr = rt.exec("xdvi " + path.substring(0,path.length()-4) +".dvi");
    pr.waitFor();
  }
}
