//package quickcheck;

import sort.strategy.expr.*;
import sort.types.*;
import sort.types.expr.*;
import tom.library.sl.*;


public class Main {
  %include { sl.tom }
  %include {sort/Sort.tom}

  public static void main(String[] args) {
    RandomizerGenerator generator = new RandomizerGenerator();
    
    
    /* debut test */
    // Strategy s_test = generator.make_random_with_depth(5);
    Strategy s_test = generator.testStrategyLight(100);
    Representation.representeStrategy(s_test);
    System.out.println(s_test);
    Expr b = null;
    try{
      b=s_test.visitLight(`zero());
    } catch (VisitFailure e){
      System.out.println("erreur");
    }
    System.out.println(b + "\n\n");
    Representation.represente(b, "test.dot");
    Representation.representeHash(b, "test_hash.dot");
    /* fin test */
    
    
    // Strategy s = generator.make_random();
    // Expr a = null;
    // try{
    //   a = s.visit(`zero());
    // } catch (VisitFailure e){
    //   System.out.println("impossible");
    // }
    // 
    // System.out.println(a);
    // Representation.represente(a, "res.dot");
    // Representation.representeHash(a, "res_hash.dot");
  }
}
