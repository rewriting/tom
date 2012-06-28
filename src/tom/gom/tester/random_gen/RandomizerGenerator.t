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
  	//int i = 0;
  	return 
  	  `Mu(
  	    MuVar("x"),
  	    ChoiceUndet(
  	      Make_plus(MuVar("x"),MuVar("x")),
  	      Machin()));
	}
	
	public Strategy testStrategyID(){
	    //int i = 0;
	    return 
	      `Mu(
	        MuVar("x"),
	        ChoiceUndet(
	          Make_plus(MuVar("x"),MuVar("x")),
	          Identity()));
	  }
	
	public Strategy testStrategy2(){
  	//int i = 0;
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
	    return 
	      `Mu(
	        MuVar("x"),
	        ChoiceUndet(
	          Make_zero(),
	          Make_un(),
	          Make_plus(MuVar("x"), MuVar("x")),
	          Make_mult(MuVar("x"), MuVar("x"))));
	  }
}