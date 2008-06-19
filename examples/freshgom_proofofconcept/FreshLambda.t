package freshgom_proofofconcept;

import freshgom_proofofconcept.lambda.types.*;

public class FreshLambda {

  %include { tweaked_lambda.tom }

  public static LTerm RLTermToLTerm(RLTerm t) {
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
