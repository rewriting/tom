package gen;

import sort.strategy.expr.*;
import sort.types.*;
import sort.types.expr.*;
import definitions.*;
import tom.library.sl.*;

public class BugLight {
  %include { sl.tom }
  %include { ../sort/Sort.tom}

  private class Condition {
    private int i;
    Condition(int i) {
      this.i = i;
    }

    public boolean isTrue() {
      return i>0;
    }

    public Condition dec() {
      this.i--;
      return this;
    }
  }

  %typeterm Condition {
    implement{Condition}
    is_sort(t){t instanceof Condition}
    equals(l1,l2)  { $l1.equals($l2) }
  }

  /*===================== TEST LIGHT =====================*/
  
  %strategy ChoiceLeafLight() extends Fail(){
    visit Expr {
      e -> {
        System.out.println("leaf generated");
        return `Pselect(1,2, Make_zero(), Make_un()).visitLight(`e);
      }
    }
  }
  
  %strategy ChoiceBranchLight(retour:Strategy, retour2:Strategy) extends Fail(){
    visit Expr {
      e -> {
        System.out.println("branch generated");
        return `Pselect(
          1,
          2,
          Make_plus(retour, retour2),
          Make_mult(retour, retour2)
        ).visitLight(`e);
      }
    }
  }
  
  %strategy ChoiceWithConditionLight(leaf:Strategy, branch:Strategy, cond:Condition) extends Fail(){
    visit Expr {
      e -> {
        if(cond.isTrue()) {
          //cond.dec();
          System.out.println("case branch : ");
          Expr res = branch.visitLight(`e);
          System.out.println(res);
          return res;
        } else {
          // here condition can be < 0 because of the fact that cond 
          // is shared with all created strategies
          System.out.print("stop : ");
          //cond.dec();
          Expr res = leaf.visitLight(`e);
          System.out.println(res);
          return res;
        }
      }
    }
  }
  
  public Strategy testStrategyLight(int depth){
    Condition cond = new Condition(depth);
    Strategy s = 
      `Mu(
        MuVar("x"),
        ChoiceWithConditionLight(
          ChoiceLeafLight(),
          ChoiceBranchLight(MuVar("x"), MuVar("x")),
          cond.dec()
        )
      );
    return s;
  }

  /*============================================================*/

  public static void main(String[] args) {
    BugLight generator = new BugLight();

    Strategy s = generator.testStrategyLight(10);
    Expr b = null;
    try {
      b=s.visitLight(`zero());
    } catch (VisitFailure e) {
      System.out.println("failure");
    }
    System.out.println("result = " + b + "\n\n");
    Representation.represente(b, "test.dot");
    Representation.representeHash(b, "test_hash.dot");
  }
}
