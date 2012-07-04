//package quickcheck;

import sort.strategy.expr.*;
import sort.types.*;
import sort.types.expr.*;
import definitions.*;
import tom.library.sl.*;

public class Bug {
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
  
  %strategy ChoiceLeafLight() extends Identity(){
    visit Expr {
      e -> {
        System.out.println("leaf generated");
        return `Pselect(1,2, Make_zero(), Make_un()).visitLight(`e);
      }
    }
  }
  
  %strategy ChoiceBranchLight(retour:Strategy, retour2:Strategy) extends Identity(){
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
  
  %strategy ChoiceWithConditionLight(leaf:Strategy, branch:Strategy, cond:Condition) extends Identity() {
    visit Expr {
      e -> {
        if(cond.isTrue()) {
          cond.dec();
          //return `Pselect(1,2,leaf, branch).visitLight(`e);
          /*
          if(Math.random() < 0.5) {
            System.out.println("case leaf");
            Expr res = `leaf.visitLight(`e);
            System.out.println("=> " + res);
            return res;
          } else {
            System.out.println("case branch");
            Expr res = `branch.visitLight(`e);
            System.out.println("=> " + res);
            return res;
          }*/
            System.out.println("case branch");
            Expr res = `branch.visitLight(`e);
            System.out.println("=> " + res);
            return res;

        } else {
          // here condition can be < 0 because of the fact that cond 
          // is shared with all created strategies
          System.out.println("stop");
          cond.dec();
          Expr res = leaf.visitLight(`e);
          System.out.println("=> " + res);
          return res;
        }
      }
    }
  }
  
  public Strategy genStrategy(int depth) {
    Condition cond = new Condition(depth);
    Strategy s = 
      `mu(
        MuVar("x"),
        ChoiceWithConditionLight(
          Make_zero() 
          /*Pselect(1,2, Make_zero(), Make_un())*/
          /*ChoiceLeafLight()*/,
          Make_plus(MuVar("x"), MuVar("x"))
          /*Pselect(1,2, Make_plus(MuVar("x"), MuVar("x")), Make_mult(MuVar("x"), MuVar("x")))*/
          /*ChoiceBranchLight(MuVar("x"), MuVar("x"))*/,
          cond
        )
      );
    return s;
  }
  
  /*============================================================*/
    
  public static void main(String[] args) {
    Bug generator = new Bug();
    
    Strategy s = generator.genStrategy(3);
    Expr b = null;
    try {
      b=s.visitLight(`zero());
    } catch (VisitFailure e) {
      System.out.println("failure");
    }
    System.out.println("result = " + b + "\n\n");
    //Representation.represente(b, "test.dot");
    //Representation.representeHash(b, "test_hash.dot");
  }
}
