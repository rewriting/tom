package mi1;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;


import mi1.data.types.*;
import mi1.data.types.t1.*;
import mi1.data.types.t2.*;
import tom.library.sl.*;

public class MainHand extends TestCase {
  %include { mi1/data/data.tom }
  %include { sl.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(MainHand.class));
  }

  public void testMatch() {
    T1 subject = `f(f(a(),b()),g(b()));
    %match(subject) {
      f(x,g(y)) -> { 
        assertEquals(`x,`f(a(),b()));
        assertEquals(`y,`b());
        return;
      }
    }
    fail();
  }

  public void testVisit() {
    T1 subject = `f(f(a(),b()),g(b()));
    try {
      T1 res = (T1) `Repeat(OnceBottomUp(Rule())).visitLight(subject, mapping.Introspector.instance);
      assertEquals(res, `a());
    } catch(VisitFailure e) {
      fail();
    }

  }


  %strategy Rule() extends Fail() {
    visit T1 {
      f(x,y) -> {
        return `x;
      }
    }
  }

}
