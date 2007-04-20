package lambdaupsilon;

import lambdaupsilon.upsilon.*;
import lambdaupsilon.upsilon.types.*;

class LambdaUpsilon {

  %include{ upsilon/upsilon.tom }

  public static void main(String [] argv) {
    LTerm t1 = `lappl(lambda(lappl(one(),one())),constant("a"));
    LTerm swap = `lambda(lambda(lambda(
                    lappl(lappl(suc(suc(one())),one()),suc(one())))));
    
    LTerm t2 = 
      `lappl(lappl(lappl(swap,constant("f")),constant("a")),constant("b"));

    LTerm t3 = `lappl(swap,constant("f"));

    System.out.println("(\\x.(x x) a) = " + t1);
    System.out.println("swap = \\fyz.(f y x) = " + swap);
    System.out.println("swap f a b = " + t2);
    System.out.println("swap f = " + t3);
  }
}
