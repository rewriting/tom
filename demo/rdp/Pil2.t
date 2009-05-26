import pil2.term.types.*;
import java.util.*;
import tom.library.sl.*;

class Pil2 {
  %include { sl.tom }
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
         | If(cond:Bool, e1:Expr, e2:Expr) 
         | a()
         | b()
  }

  public final static void main(String[] args) {
    try {
    
      Expr prg = `Let(Var("x"),a(), Var("x"));
      System.out.println("prg = " + prg);
      //System.out.println("new prg = " + `TopDown(Rename("x","z")).visit(prg));

      Collection bag = new HashSet();
      `TopDown(CollectVar(bag)).visit(prg);
      System.out.println("bag = " + bag);

    } catch(VisitFailure e) {
      System.out.println("failure");
    }
  }
  
  // Replace a by b
  %strategy ReplaceAB() extends Identity() {
    visit Expr {
      a() -> { return `b(); }
    }
  }

  // Renaming
  %strategy RenameVar(n1:String,n2:String) extends Identity() {
    visit Expr {
      Var(n) -> { if(`n==n1) return `Var(n2); }
    }
  }

 // Collect variable
  %strategy CollectVar(c:Collection) extends Identity() {
    visit Expr {
      Var(n) -> { c.add(`n); }
    }
  }


// Optimize
  %strategy RemoveLet(pt:Collection) extends Identity() {
    visit Expr {
      Let(Var(n),expr,body) -> { 
        if(`body == `TopDown(RenameVar(n,"_"+n)).visit(`body)) {
              // if Var(n) is not used
          pt.add(getEnvironment().getPosition());
          return `body;
            }
      }
    }
  }

}

/*
 * replace a by b
 * rename x in z
 * collect variables
 * Let(Var("x"),a(), Let(Var("y"),b(), Var("x")))
 * HashSet, ArrayList
 */
