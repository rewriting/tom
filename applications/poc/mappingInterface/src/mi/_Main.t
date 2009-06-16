package mi;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;


import mi._.data.types.*;
import mi._.data.types.t1.*;
import mi._.data.types.t2.*;
import tom.library.sl.*;

public class _Main extends TestCase {
  %include { mi/_/data/data.tom }
  %include { sl.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(_Main.class));
  }

  public void testVisit() {
    T1 subject = new f(new f(new a(),new b()), new g(new b()));
    System.out.println("subject = " + subject);

    %match(subject) {
      f(x,g(y)) -> { System.out.println(`x + " -- " + `y); }
    }

    try {
      T1 res = (T1) `Repeat(OnceBottomUp(Rule())).visitLight(subject, new LocalIntrospector());
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
