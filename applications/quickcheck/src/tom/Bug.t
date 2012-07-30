package gen;

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
  
  %strategy ChoiceLeaf() extends Identity(){
    visit Expr {
      e -> {
        System.out.println("leaf generated");
        return `Pselect(1,2, Make_zero(), Make_un()).visit(`e);
      }
    }
  }
  
  %strategy ChoiceBranch(retour:Strategy, retour2:Strategy) extends Identity(){
    visit Expr {
      e -> {
        System.out.println("branch generated");
        `Make_plus(retour, retour2).visit(getEnvironment());
        return (Expr) getEnvironment().getSubject();
        
      }
    }
  }
  
  %strategy ChoiceWithCondition(leaf:Strategy, branch:Strategy, cond:Condition) extends Identity() {
    visit Expr {
      e -> {
        if(cond.isTrue()) {
          cond.dec();
            System.out.println("case branch");
            `branch.visit(getEnvironment());
            return (Expr) getEnvironment().getSubject();
        } else {
          // here condition can be < 0 because of the fact that cond 
          // is shared with all created strategies
          System.out.println("stop");
          cond.dec();
          `leaf.visit(getEnvironment());
          return (Expr) getEnvironment().getSubject();
        }
      }
    }
  }
  
  public Strategy genStrategy(int depth) {
    Condition cond = new Condition(depth);
    Strategy s = 
      `mu(
        MuVar("x"),
        ChoiceWithCondition(
          Make_zero(),
          ChoiceBranch(MuVar("x"), Make_un()),
          cond
        )
      );
    return s;
  }
  
  /*============================================================*/
    
  public static void main(String[] args) {
    Bug generator = new Bug();
    
    Strategy s = generator.genStrategy(5);
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
