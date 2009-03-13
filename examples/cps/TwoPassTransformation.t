package cps;

import tom.library.sl.*;
import cps.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

/* Encoding of the first-order two-pass CPS transformation */

public class TwoPassTransformation {

  %include { sl.tom }
  %include { lambda/lambda.tom }

  // returns t[u/x]
  public static LTerm substitute(LTerm t, LVar x, LTerm u) {
    %match(t) {
      App(t1,t2) -> { return `App(substitute(t1,x,u),substitute(t2,x,u)); }
      Abs(lam(v,t1)) -> { return `Abs(lam(v,substitute(t1,x,u))); }
      Fix(fixpoint(v,t1)) -> { return `Fix(fixpoint(v,substitute(t1,x,u))); }
      Branch(a,b,c) -> { return `Branch(substitute(a,x,u),substitute(b,x,u),substitute(c,x,u)); }
      Let(letin(v,t1,t2)) -> {
        return `Let(letin(v,substitute(t1,x,u),substitute(t2,x,u)));
      }
      Var(y) -> { if (`y.equals(x)) return u; else return t; }
      Plus(t1,t2) -> { return `Plus(substitute(t1,x,u),substitute(t2,x,u)); }
      Minus(t1,t2) -> { return `Minus(substitute(t1,x,u),substitute(t2,x,u)); }
      Times(t1,t2) -> { return `Times(substitute(t1,x,u),substitute(t2,x,u)); }
      GT(t1,t2) -> { return `GT(substitute(t1,x,u),substitute(t2,x,u)); }
      LT(t1,t2) -> { return `LT(substitute(t1,x,u),substitute(t2,x,u)); }
      Eq(t1,t2) -> { return `Eq(substitute(t1,x,u),substitute(t2,x,u)); }
      Or(t1,t2) -> { return `Or(substitute(t1,x,u),substitute(t2,x,u)); }
      And(t1,t2) -> { return `And(substitute(t1,x,u),substitute(t2,x,u)); }
      LNot(t1) -> { return `LNot(substitute(t1,x,u)); }
      Print(t1) -> { return `Print(substitute(t1,x,u)); }
      _ -> { return `t; }
    }
    throw new RuntimeException();
  }

  public static LTerm cpsaux(LTerm t) {
    %match(t) {
      (True|False|Var|Integer|Unit|Plus|Minus|Times|GT|LT|Eq|LNot|And|Or|Print)[] -> { 
        return t;
      }
      Abs(lam(x,m)) -> {
        return `Abs(lam(x,cps(m)));
      }
      Fix(fixpoint(f,Abs(lam(x,m)))) -> {
        return `Fix(fixpoint(f,Abs(lam(x,cps(m)))));
      }
    }
    return null;
  }

  public static LTerm cps(LTerm t) {
    %match(t) {
      (True|False|Integer|Plus|Minus|Times|GT|LT|Eq|LNot|And|Or|Print|Unit|Var|Abs|Fix)[] -> { 
        LVar k = LVar.freshLVar("k");
        return `Abs(lam(k,App(Var(k),cpsaux(t))));
      }
      App(a,b) -> {
        LVar k = LVar.freshLVar("k");
        LVar va = LVar.freshLVar("va");
        LVar vb = LVar.freshLVar("vb");
        return `Abs(lam(k,App(cps(a),Abs(lam(va,App(cps(b),Abs(lam(vb,App(App(Var(va),Var(vb)),Var(k))))))))));
      }
      Let(letin(x,a,b)) -> {
        LVar k = LVar.freshLVar("k");
        //return `Abs(lam(k,App(cps(a),Abs(lam(x,App(cps(b),Var(k)))))));
        LVar va = LVar.freshLVar("va");
        return `Abs(lam(k,App(cps(a),Abs(lam(va,Let(letin(x,Var(va),App(cps(b),Var(k)))))))));
      }
      Branch(a,b,c) -> {
        LVar k = LVar.freshLVar("k");
        LVar va = LVar.freshLVar("va");
        return `Abs(lam(k,App(cps(a),Abs(lam(va,Branch(Var(va),App(cps(b),Var(k)),App(cps(c),Var(k))))))));
      }
      CallCC(a) -> {
        LVar k = LVar.freshLVar("k");
        LVar va = LVar.freshLVar("va");
        return `Abs(lam(k,App(cps(a),Abs(lam(va,App(App(Var(va),Var(k)),Var(k)))))));
      }
      Throw(a,b) -> {
        LVar k = LVar.freshLVar("k");
        LVar va = LVar.freshLVar("va");
        LVar vb = LVar.freshLVar("vb");
        return `Abs(lam(k,App(cps(a),Abs(lam(va,App(cps(b),Abs(lam(vb,App(Var(va),Var(vb))))))))));
      }
    }
    return null;
  }

  %strategy Admin() extends Identity() {
    visit LTerm {
      App(Abs(lam(k,App(Var(k),v@(True|False|Integer|Plus|Minus|Times|GT|LT|Eq|LNot|And|Or|Print|Unit|Var|Abs|Fix)[]))),Abs(lam(x,a))) -> substitute(a,x,v)
      //eta reduction
      Abs(lam(x,App(t,Var(x)))) -> {
       if (`isFree(x,t)) {
         return `t;
       }
      }
    }
  }

  public static boolean isFree(LVar var, LTerm t) {
    try {
      `TopDown(IsFree(var)).visitLight(t);
    } catch(VisitFailure e) {
      return false;
    }
    return true;
  }

  %strategy IsFree(var: LVar) extends Identity() {
    visit LTerm {
      Var(v) && v==var -> { throw new VisitFailure(); }
    }
  }

  public static LTerm admin(LTerm t) {
    try { return (LTerm) `InnermostId(Admin()).visit(t); }
    catch(Exception e) { throw new RuntimeException(); }
  }

  public static Value lookup(Env e, LVar x) {
    %match(e) {
      Env(_*,EPair(y,v),_*) && y << LVar x -> { return `v; }
    }
    throw new RuntimeException("Variable out of scope : " + `x);
  }

  public static Value eval(Env e, LTerm t) {
    %match(t) {
      Var(v) -> { return `lookup(e,v); }
      Integer(i) -> { return `VInt(i); }
      True() -> { return `VBool(true); }
      False() -> { return `VBool(false); }
      Abs[] -> { return `VClos(t,e); }
      Fix[] -> { return `VClos(t,e); }
      Unit() -> { return `VUnit(); }
      Branch(a,b,c) -> {
        %match(eval(e,a)) {
          VBool(x) -> { return (`x) ? `eval(e,b) : `eval(e,c); }
        }
      }
      Let(letin(x,a,b)) -> {
        Value v = `eval(e,a);
        return `eval(Env(EPair(x,v),e*),b);
      }
      Plus(a,b) && VInt(n) << eval(e,a) && VInt(m) << eval(e,b) -> {
        return `VInt(n + m);
      }
      Minus(a,b) && VInt(n) << eval(e,a) && VInt(m) << eval(e,b) -> {
        return `VInt(n - m);
      }
      Times(a,b) && VInt(n) << eval(e,a) && VInt(m) << eval(e,b) -> {
        int r = `(n) * `(m);
        return `VInt(r);
      }
      GT(a,b) && VInt(n) << eval(e,a) && VInt(m) << eval(e,b) -> {
        return `VBool(n > m);
      }
      LT(a,b) && VInt(n) << eval(e,a) && VInt(m) << eval(e,b) -> {
        return `VBool(n < m);
      }
      Print(x) -> {
        System.out.println("prints: " + Printer.pretty(`eval(e,x)));
        return `VUnit();
      }
      Eq(a,b) -> {
        return `VBool(eval(e,a).equals(eval(e,b)));
      }
      Or(a,b) && VBool(x) << eval(e,a) && VBool(y) << eval(e,b) -> {
        return `VBool(x || y);
      }
      And(a,b) && VBool(x) << eval(e,a) && VBool(y) << eval(e,b) -> {
        return `VBool(x && y);
      }
      LNot(a) && VBool(x) << eval(e,a) -> {
        return `VBool(!x);
      }
      App(a,b) && VClos(Abs(lam(x,c)),ee) << eval(e,a) -> {
        Value v = `eval(e,b);
        return `eval(Env(EPair(x,v),ee*),c);
      }
      App(a,b) && cl@VClos(Fix(fixpoint(f,Abs(lam(x,c)))),ee) << eval(e,a) -> {
        Value v = `eval(e,b);
        Env env = `Env(EPair(f,cl),EPair(x,v),ee*);
        return `eval(env,c);
      }
    }
    //System.out.println(t);
    throw new RuntimeException();
  }

  /* parser loop */
  public static void main(String[] args) {
    try{
      LambdaLexer lexer = new LambdaLexer(new ANTLRInputStream(System.in));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      LambdaParser parser = new LambdaParser(tokens);
      for(RawLTerm rt:parser.toplevel()) {
        System.out.println("\n------------------------------------------");
        System.out.println("\nparsed: " + Printer.prettyp5(rt));
        LTerm ct = rt.convert(); 
        System.out.print("\ncps translation ... ");
        System.out.flush();
        LTerm cpst = cps(ct);
        System.out.println("optimization ...");
        LTerm ocpst = admin(cpst);
        //System.out.println("\nparsed: " + Printer.prettyp5(ocpst.export()));
        LVar fresh = LVar.freshLVar("x");
        LTerm id = `Abs(lam(fresh,Var(fresh)));
        System.out.println("\ncps translated: " + Printer.prettyp5(ocpst.export()));
        System.out.println("\nevaluation: " + Printer.pretty(`eval(Env(),App(ocpst,id))));
        System.out.println("\nevaluation: " + Printer.pretty(`eval(Env(),App(ocpst,id))));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
