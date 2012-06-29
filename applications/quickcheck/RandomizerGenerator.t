import sort.strategy.expr.*;
import sort.types.*;
import sort.types.expr.*;
import tom.library.sl.*;

public class RandomizerGenerator {
  %include { sl.tom }
  %include {sort/Sort.tom}
  
  %typeterm Integer {
    implement{Integer}
    is_sort(t){t instanceof Integer}
    equals(l1,l2)  { $l1.equals($l2) }
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
	/**
	 * @deprecated
	 */ 
	 
	%strategy Machin() extends Fail(){
  	visit Expr {
    	e -> {
      	Position position = getEnvironment().getPosition();
      	System.out.println("position actuelle de " + `e);
      	System.out.println(getEnvironment().depth());
      	return `e;
    	}
  	}
	}
	
	public Strategy testStrategy(){
  	return 
  	  `Mu(
  	    MuVar("x"),
  	    ChoiceUndet(
  	      Make_plus(MuVar("x"),MuVar("x")),
  	      Machin()));
	}

	public Strategy testStrategyRec(int i){
  	System.out.println(i);
  	if(i==0){
    	return `Pselect(1,2,Make_zero(), Make_un());
  	} else {
    	int choix = (int) (Math.random() * 2);
    	if(choix == 0){
      	return `Make_plus(testStrategyRec(i - 1), testStrategyRec(i - 1));
    	} else {
      	return `Pselect(1,2,Make_zero(), Make_un());
    	}
  	}
	}
	
	public Strategy testStrategyCons(){
	    return `ChoiceUndet(Make_plus(Machin(), Machin()));
	  }
	
	
	public Strategy testStrategyID(){
	    return 
	      `Mu(
	        MuVar("x"),
	        ChoiceUndet(
	          Make_plus(MuVar("x"),MuVar("x")),
	          Identity()));
	  }
	
	public Strategy testStrategy2(){
	    return 
	      `Mu(
	        MuVar("y"),
	        Mu(
	        MuVar("x"),
	        ChoiceUndet(
	          Make_plus(MuVar("x"),MuVar("y")),
	          Machin()))
	        );
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