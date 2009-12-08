package cps;

import tom.library.sl.*;
import cps.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

/**
 * Encoding of the first-order one-pass CPS transformation 
 * Only for App, Abs, Var and Integer constructors
 */

public class OnePassTransformation {

  %include { sl.tom }
  %include { lambda/lambda.tom }

  // returns t[u/x]
  public static LTerm substitute(LTerm t, LVar x, LTerm u) {
    %match(t) {
      App(t1,t2) -> { return `App(substitute(t1,x,u),substitute(t2,x,u)); }
      Abs(lam(v,t1)) -> { return `Abs(lam(v,substitute(t1,x,u))); }
      Var(y) -> { if (`y.equals(x)) return u; else return t; }
      _ -> { return `t; }
    }
    throw new RuntimeException("non exhaustive pattern-matching");
  }

  public static LTerm cps(LTerm e) {
    LVar k = LVar.freshLVar("k");
    return `Abs(lam(k,cps_e(e,k)));
  }

  public static LTerm cps_t(LTerm e) {
    %match(e) {
      (Integer|Var)[] -> { return e; }
      Abs(lam(x,t)) -> { 
        LVar k = LVar.freshLVar("k");
        return `Abs(lam(x,Abs(lam(k,cps_e(t,k)))));
      }
    }
    throw new RuntimeException("non exhaustive pattern-matching");
  }

  public static LTerm cps_s(LTerm e, LTerm K) {
    %match(e) {
      App(t0@(Integer|Var|Abs)[], t1@(Integer|Var|Abs)[]) -> { 
        return `App(App(cps_t(t0),cps_t(t1)),K); 
      }
      App(t0@(Integer|Var|Abs)[], s1@App[]) -> { 
        LVar x1 = LVar.freshLVar("x1");
        return `cps_s(s1, Abs(lam(x1,App(App(cps_t(t0),Var(x1)),K)))); 
      }
      App(s0@App[], t1@(Integer|Var|Abs)[]) -> {
        LVar x0 = LVar.freshLVar("x0");
        return `cps_s(s0, Abs(lam(x0,App(App(Var(x0),cps_t(t1)),K)))); 
      }
      App(s0@App[], s1@App[]) -> { 
        LVar x1 = LVar.freshLVar("x1");
        LVar x0 = LVar.freshLVar("x0");
        return `cps_s(s0, Abs(lam(x0,cps_s(s1,Abs(lam(x1,App(App(Var(x0),Var(x1)),K))))))); 
      }
    }
    throw new RuntimeException("non exhaustive pattern-matching");
  }

  public static LTerm cps_e(LTerm e, LVar k) {
    %match(e) {
      (Integer|Var|Abs)[] -> { return `App(Var(k), cps_t(e)); }
      s -> { return `cps_s(s,Var(k)); }
    }
    throw new RuntimeException("non exhaustive pattern-matching");
  }


  %strategy HeadBeta() extends Fail() {
    visit LTerm {
      App(Abs(lam(x,t)),u) -> substitute(t,x,u)
    }
  }

  public static LTerm eval(LTerm e) {
    try {
      return `Outermost(HeadBeta()).visit(e);
    } catch(VisitFailure ex) {
      throw new RuntimeException("Unexpected failure");
    }
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
        System.out.println("\ncps translation ...");
        LTerm cpst = cps(ct);
        LVar fresh = LVar.freshLVar("x");
        LTerm id = `Abs(lam(fresh,Var(fresh)));
        System.out.println("\ncps translated: " + Printer.prettyp5(cpst.export()));
        System.out.println("\nevaluation: " + Printer.pretty(eval(`App(cpst,id)).export()));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
  public static String pretty(RawLTerm t) {
    %match (t) {
      RawInteger(i) -> { return ""+`i; }
      RawSucc(t1) -> { return "succ("+`pretty(t1)+")"; }
      RawVar(x) -> { return `x; }
      RawAbs(Rawlam(x,u)) -> { return %[(fun @`x@ -> @`pretty(u)@)]%; }
      RawApp(u,v) -> { return %[(@`pretty(u)@ @`pretty(v)@)]%; }
    }
    return null;
  }

  public static String prettyp5(RawLTerm t) {
    try {
      String s = pretty(t);
      File tmp = File.createTempFile("output",".ml");
      FileWriter writer = new FileWriter(tmp);
      String path = tmp.getAbsolutePath();
      writer.write(s);
      writer.close();
      Runtime rt = Runtime.getRuntime();
      Process pr = rt.exec("camlp5 pa_o.cmo pa_op.cmo pr_o.cmo " + path);
      int ret = pr.waitFor(); 
      BufferedReader reader = 
        new BufferedReader(new InputStreamReader(pr.getInputStream()));
      StringBuffer buf = new StringBuffer();
      while(reader.ready()) {
        buf.append("\n");
        buf.append(reader.readLine());
      }
      return buf.toString();
    } catch (Exception e) {
      System.err.println(e);
      return "";
    }
  }
  */

}
