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
    int i;
    Condition(int i){
      this.i = i;
    }
    public Condition dec(){
      this.i--;
      return this;
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
  
  %strategy ChoiceWithConditionLight(leaf:Strategy, branch:Strategy, cond:Condition) extends Identity(){
    visit Expr {
      e && cond.i > 0 -> {
        System.out.println(cond.i);
        cond.dec();
        //return `Pselect(1,2,leaf, branch).visitLight(`e);
        if(Math.random() < 0.5) {
          `leaf.visitLight(`e);
        } else {
          `branch.visitLight(`e);
        }
      }
      e && cond.i <= 0 -> {
        // here condition can be < 0 because of the fact that cond 
        // is shared with all created strategies
        System.out.println("stop : " + cond.i);
        cond.dec();
        return leaf.visitLight(`e);
      }
    }
  }
  
  public Strategy genStrategy(int depth) {
    Condition cond = new Condition(depth);
    Strategy s = 
      `mu(
        MuVar("x"),
        ChoiceWithConditionLight(
          ChoiceLeafLight(),
          ChoiceBranchLight(MuVar("x"), MuVar("x")),
          cond
        )
      );
    return s;
  }
  
  /*============================================================*/
    
  public static void main(String[] args) {
    Bug generator = new Bug();
    
    Strategy s = generator.genStrategy(100);
    Expr b = null;
    try{
      b=s.visitLight(`zero());
    } catch (VisitFailure e) {
      System.out.println("erreur");
    }
    System.out.println("result = " + b + "\n\n");
    Representation.represente(b, "test.dot");
    Representation.representeHash(b, "test_hash.dot");
  }
}
