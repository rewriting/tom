package freshgom_proofofconcept;

import freshgom_proofofconcept.lambda.types.*;

import java.util.*;

public class FreshLambda {

  %include { tweaked_lambda.tom }

  private class Context {
    private class Pair { 
      public String x;
      public Atom a;
      public Pair(String x, Atom a) {
        this.x = x;
        thsi.a = a;
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
      rapp(t1,t2) -> { return `app(RLTermToLTerm(t1,map),RLTermToLTerm(t2,map)); }
      rabs(x,t1) -> {
        Atom fresh = LTerm.freshAtom();
        ctx.push(x,fresh);
        nt1 = RLTermToLTerm(t1,ctx);
        ctx.pop();
        return `abs(fresh,nt1);
      }
    }
    return null;
  }

    // return t[u/x]
    public static LTerm substitute(LTerm t, Atom x, LTerm u) {
      %match(t) {
        abs(y,t1) -> { return `abs(y,substitute(t1,y,u)); }
        app(t1,t2) -> { return `app(t1,t2); }
        var(y) -> { if (x.equals(`y)) return u; else return t; }
      }
      throw new RuntimeException();
    }

    public static void main(String[] args) {
      LTerm t = `abs(atom(1,"x"),app(var(atom(1,"x")),var(atom(1,"x"))));
      System.out.println(t);
      for(int i=0;i<10;i++) {
        %match(t) {
          abs(x,t1) -> { System.out.println("la " + `x + "." + `t1); }
        }
      }
    }
  }
