//package quickcheck;

import sort.strategy.expr.*;
import sort.types.*;
import sort.types.expr.*;
import definitions.*;
import tom.library.sl.*;

public class RandomizerGenerator {
  %include { sl.tom }
  %include {sort/Sort.tom}
  
  private class Condition {
    int i;
    Condition(int i){
      this.i = i;
    }
  }
  
  %typeterm Condition {
    implement{Condition}
    is_sort(t){t instanceof Condition}
    equals(l1,l2)  { $l1.equals($l2) }
  }
  
  %strategy ChoiceLeaf() extends Fail(){
    visit Expr {
      e -> {
        return `Pselect(1,2, Make_zero(), Make_un()).visit(`e);
      }
    }
  }
  
  %strategy ChoiceBranch(retour:Strategy, retour2:Strategy) extends Fail(){
    visit Expr {
      e -> {
        System.out.println(retour.hashCode() + " et " + retour2.hashCode());
        return `Pselect(
          1,
          2,
          Make_plus(retour, retour2),
          Make_mult(retour, retour2)
        ).visit(`e);
      }
    }
  }
  
  %strategy ChoiceWithCondition(cond:Condition, leaf:Strategy, branch:Strategy) extends Fail(){
    visit Expr {
      e && cond.i > 0 -> {
        System.out.println(cond.i);
        cond.i--;
        return `Pselect(1,2,leaf, branch).visit(`e);
      }
      e && cond.i == 0 -> {
        System.out.println("stop : " + cond.i);
        cond.i--;
        return leaf.visit(`e);
      }
    }
  }
  
  public Strategy testStrategy(int depth){
    Condition cond = new Condition(depth);
    return
      `Mu(
        MuVar("x"),
        ChoiceWithCondition(
          cond,
          ChoiceLeaf(),
          ChoiceBranch(MuVar("x"), MuVar("x"))
        )
      );
  }
    
	public Strategy make_random(){
		return 
      `Mu(
        MuVar("x"),
        ChoiceUndet(
          Make_zero(),
          Make_un(),
          Make_plus(MuVar("x"), MuVar("x")),
          Make_mult(MuVar("x"), MuVar("x"))));
	}
	
	public Strategy make_random_with_depth(int max_depth){
	        if(max_depth == 0){
	          return `Pselect(1,2,Make_zero(), Make_un());
	        } else {
	          int choice = (int) (Math.random() * 4);
	          if(choice == 0){
	            return `Make_plus(
	                      make_random_with_depth(max_depth - 1), 
	                      make_random_with_depth(max_depth - 1));
	          } if(choice == 1){
  	          return `Make_mult(
  	                    make_random_with_depth(max_depth - 1), 
  	                    make_random_with_depth(max_depth - 1));
	          } else {
	            return `Pselect(1,2,Make_zero(), Make_un());
	          }
	        }
	      }
}