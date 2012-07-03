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
        double r = Math.random();
        if(r<0.5) {
          return `leaf.visit(`e);
        } else {
          return `branch.visit(`e);
        }
        //return `Pselect(1,2,leaf, branch).visit(`e);
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
    /*
    return
      `mu(
        MuVar("x"),
        ChoiceWithCondition(
          cond,
          ChoiceLeaf(),
          ChoiceBranch(MuVar("x"), MuVar("x"))
        )
      );
      */
    return
      `mu(
        MuVar("x"),
        ChoiceWithCondition(
          cond,
          Pselect(1,2, Make_zero(), Make_un()),
          Pselect(1,2, Make_plus(MuVar("x"),MuVar("x")), Make_mult(MuVar("x"),MuVar("x")))
        )
      );
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
      e && cond.i > 0 -> {
        System.out.println(cond.i);
        cond.i--;
        return `Pselect(1,2,leaf, branch).visitLight(`e);
      }
      e && cond.i <= 0 -> {
        // here condition can be < 0 because of the fact that cond 
        // is shared with all created strategies
        System.out.println("stop : " + cond.i);
        cond.i--;
        return leaf.visitLight(`e);
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
          cond
        )
      );
    return s;
  }
  
  /*============================================================*/
    
	public Strategy make_random(){
		return 
      `mu(
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
