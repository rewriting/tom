package freshgom_proofofconcept;

import tom.library.sl.*;
import freshgom_proofofconcept.lambda.types.*;
import java.util.*;

public class FreshLambda {

  %include { sl.tom }
  %include { tweaked_lambda.tom }

  private static class Context {
    private class Pair { 
      public String x;
      public Atom a;
      public Pair(String x, Atom a) {
        this.x = x;
        this.a = a;
      } 
    }
    private LinkedList<Pair> ctx = new LinkedList<Pair>();
    public void push(String x, Atom a) { ctx.addFirst(new Pair(x,a)); }
    public void pop() { ctx.removeFirst(); }
    public Atom get(String x) { 
      for (Pair p: ctx) {
        if (p.x.equals(x)) return p.a;
      }
      throw new RuntimeException();
    }
  }

  public static LTerm RLTermToLTerm(RLTerm t, Context ctx) {
    %match(t) {
      rapp(t1,t2) -> { 
        return `app(RLTermToLTerm(t1,ctx),RLTermToLTerm(t2,ctx)); 
      }
      rabs(x,t1) -> {
        Atom fresh = LTerm.freshAtom(`x);
        ctx.push(`x,fresh);
        LTerm nt1 = RLTermToLTerm(`t1,ctx);
        ctx.pop();
        return `abs(fresh,nt1);
      }
      rvar(x) -> { return `var(ctx.get(x)); }
    }
    return null;
  }

  // return t[u/x]
  public static LTerm substitute(LTerm t, Atom x, LTerm u) {
    System.out.println(t + "[" + u + "/" + x + "]");
    %match(t) {
      abs(y,t1) -> { return `abs(y,substitute(t1,x,u)); }
      app(t1,t2) -> { return `app(substitute(t1,x,u),substitute(t2,x,u)); }
      var(y) -> { if (`y.equals(x)) return u; else return t; }
    }
    throw new RuntimeException();
  }

  %strategy HeadBeta() extends Fail() {
    visit LTerm {
      app(abs(x,t),u) -> { return `substitute(t,x,u); }
    }
  }

  public static LTerm beta(LTerm t) {
    try { return (LTerm) `Innermost(HeadBeta()).visit(t); }
    catch (VisitFailure e) { return t; }
  }

  public static void main(String[] args) {
    RLTerm rt = `rabs("y",rapp(rabs("x",rvar("x")),rvar("y")));
    LTerm t = RLTermToLTerm(rt,new Context());
    System.out.println(t);
    t = beta(t);
    System.out.println(t);
  }
}
