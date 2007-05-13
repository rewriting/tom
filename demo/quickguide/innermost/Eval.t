import eval.mydsl.types.*; 
import tom.library.sl.*; 

public class Eval {
  
  %include { sl.tom }

  %gom {
    module mydsl
      imports int String
      abstract syntax

      Expr = val(v:int)
           | var(n:String)
           | plus(Expr*)
           | mult(Expr*)
  }

  %strategy EvalExpr() extends `Identity() {
    visit Expr {
      plus(l1*,val(x),l2*,val(y),l3*) -> { return `plus(l1*,l2*,l3*,val(x + y)); }
      mult(l1*,val(x),l2*,val(y),l3*) -> { return `mult(l1*,l2*,l3*,val(x * y)); }
      plus(v@val(_)) -> { return `v; }
      mult(v@val(_)) -> { return `v; }
    }
  }

  public static void main(String[] args) throws VisitFailure {
    Expr e = `plus(val(2),var("x"),mult(val(3),val(4)),var("y"),val(5));
    Expr res = (Expr) `InnermostId(EvalExpr()).visit(e);
    System.out.println(e + "\n" + res);
  }
}

