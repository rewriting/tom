import pil.term.types.*;
import java.util.*;

class Pil {
  %include { mustrategy.tom }
  %include { java/util/types/Collection.tom }

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
    Expr p1 = `Let(Var("x"),a(), Seq(a(),Let(Var("y"),b(),Var("x"))));
    System.out.println("p1 = " + p1);
    System.out.println(pretty(p1));

Collection pt = new ArrayList();
    //System.out.println("renamed p1   = " + `TopDown(RenameVar("x","z")).apply(p1));
    System.out.println("optimized p1 = " + `BottomUp(RemoveLet(pt)).apply(p1));
System.out.println("pt = " + pt);
  }
   
  // Renaming
  %strategy RenameVar(n1:String,n2:String) extends Identity() {
    visit Expr {
      Var(n) -> { if(`n==n1) return `Var(n2); }
    }
  }

 // Optimize
  %strategy RemoveLet(pt:Collection) extends Identity() {
    visit Expr {
      Let(Var(n),expr,body) -> { 
        if(`body == `TopDown(RenameVar(n,"_"+n)).apply(`body)) {
	      // if Var(n) is not used
pt.add(getPosition());
	      return `body;
	    }
      }
    }
  }

}

//make(v) { `Choice(v,Identity()) }
//make(v) { `mu(MuVar("_x"),Try(Sequence(v,MuVar("_x")))) }
