//package quickcheck;

import sort.strategy.expr.*;
import sort.types.*;
import sort.types.expr.*;
import definitions.*;
import tom.library.sl.*;

public class BugLight {
  %include { sl.tom }
  %include {sort/Sort.tom}

  private class Condition {
    private int i;
    Condition(int i) {
      this.i = i;
    }

    public boolean isTrue() {
      return i>0;
    }

    public void dec() {
      this.i--;
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
          cond.dec();
          System.out.println("case branch");
          return `branch.visitLight(`e);
        } else {
          // here condition can be < 0 because of the fact that cond 
          // is shared with all created strategies
          System.out.println("stop");
          cond.dec();
          return `leaf.visitLight(`e);
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
          Make_zero(),
          Make_plus(MuVar("x"), MuVar("x")),
          cond
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
      b=s.visit(`zero());
    } catch (VisitFailure e) {
      System.out.println("failure");
    }
    System.out.println("result = " + b + "\n\n");
    Representation.represente(b, "test.dot");
    Representation.representeHash(b, "test_hash.dot");
  }
}
