package mi;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;

import mi.data.types.*;
import tom.library.sl.*;

public class Main extends TestCase {
  %include { mi/data/data.tom }
  %include { sl.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(Main.class));
  }

  public void testVisit() {
    T1 subject = `f(f(a(),b()),g(b()));
    System.out.println("subject = " + subject);

    %match(subject) {
      f(x,g(y)) -> { System.out.println(`x + " -- " + `y); }
    }

    try {
      T1 res = (T1) `Repeat(OnceBottomUp(Rule())).visitLight(subject);
      System.out.println("res = " + res);
    } catch(VisitFailure e) {
      System.out.println("failure");
    }

  }

  %strategy Rule() extends Fail() {
    visit T1 {
      f(x,y) -> {
        System.out.println("Rule(): " + `x);
        return `x;
      }
    }
  }

}
