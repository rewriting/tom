package freshgom_proofofconcept;

import tom.library.sl.*;
import freshgom_proofofconcept.lambda.types.*;
import java.util.*;

public class FreshLambda {

  %include { sl.tom }
  %include { tweaked_lambda.tom }

  /* how the gom module would look like *

     module lambda
     imports int String
     abstract syntax
     
     atom Atom
     
     LTerm =
       | app(t1:LTerm,t2:LTerm)
       | abs(x:Atom, inner t:LTerm) binds Atom
       | var(x:Atom)
  */

  // returns t[u/x]
  public static LTerm substitute(LTerm t, Atom x, LTerm u) {
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
    RLTerm rt = `rabs("y",rapp(rabs("x",rabs("y",rapp(rvar("x"),rvar("y")))),rvar("y")));
    RLTerm expected = `rabs("x",rabs("y",rapp(rvar("x"),rvar("y"))));
    System.out.println(rt);
    LTerm t = beta(rt.convert());
    System.out.println(t.export());
    System.out.println(t.equals(expected.convert()));
  }
}
