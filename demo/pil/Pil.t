import pil.term.types.*;

import java.util.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

class Pil {
  %include { mustrategy.tom }

  %gom {
    module Term
    imports int String
    abstract syntax
    Bool =
         | True()
         | False() 
         | Eq(e1:Expr, e2:Expr) 

    Expr = 
         | Var(name:String) 
         | Let(var:Expr, e:Expr, body:Expr) 
         | Seq(i1:Expr, i2:Expr)
         | If(cond:Bool, e1:Expr, e2:Expr) 
         | a()
         | b()
  }

  public static String pretty(Object e) {
    %match(e) {
      Var(name) -> { return `name; }
      Let(var,expr,body) -> { return "let " + pretty(`var) +
                                     " <- " + pretty(`expr) + 
                                     " in " + pretty(`body); }
    }     
    return e.toString();
  }
 
  public final static void main(String[] args) {
    Expr p1 = `Let(Var("x"),a(), Var("x"));
    System.out.println("p1 = " + p1);
    System.out.println(pretty(p1));

    System.out.println("renamed p1   = " + `RenameVar("x","z").apply(p1));
    //System.out.println("optimized p1 = " + `BottomUp(RemoveLet()).apply(p1));

  }

  %strategy RenameVar(n1:String,n2:String) extends Identity() {
    visit Expr {
      Var(n) -> { if(`n==n1) return `Var(n2); }
    }
  }
    
  %strategy RemoveLet() extends Identity() {
    visit Expr {
      Let(Var(n),expr,body) -> { 
        if(`body == `TopDown(RenameVar(n,"_"+n)).apply(`body)) {
	      // if Var(n) is not used
	      return `body;
	    }
      }
    }
  }


}
