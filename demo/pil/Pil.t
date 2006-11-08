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
 
  public final static void main(String[] args) {
    Expr p1 = `Let(Var("x"),a(), Let(Var("y"),b(),Var("x")));
    System.out.println("p1 = " + p1);
    //System.out.println(pretty(p1));
    
    //System.out.println(`RenameVar("x","z").apply(p1));

    //System.out.println("renamed p1   = " + `BottomUp(RenameVar("x","z")).apply(p1));
    //System.out.println("optimized p1 = " + `BottomUp(RemoveLet()).apply(p1));

  }

  %strategy RenameVar(n1:String,n2:String) extends Identity() {
    visit Expr {
      Var(n) -> { if(`n==n1) return `Var(n2); }
    }
  }
  /*
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
  */
  public static String pretty(Object e) {
    %match(e) {
      Var(name) -> { 
	return `name; 
      }
      Let(var,expr,body) -> {
	return "let " + pretty(`var) + "<-" + pretty(`expr) + " in " + pretty(`body);
      }
      Seq(i1,i2) -> {
	return pretty(`i1) + " ; " + pretty(`i2);
      }
      If(c,i1,i2) -> {
	return "if(" + pretty(`c) + ") " + pretty(`i1) + " else " + pretty(`i2) + " end";
      }
    }
    %match(e) {
      Eq(e1,e2) -> {
	return pretty(`e1) + " = " + pretty(`e2);
      }
    }
    return e.toString();
  }
}
