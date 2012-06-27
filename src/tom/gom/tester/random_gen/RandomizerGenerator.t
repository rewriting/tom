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
	
	
	
}