package mi;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;


import mi.data.types.*;
import mi.data.types.t1.*;
import mi.data.types.t2.*;
import tom.library.sl.*;

public class MainGomMapping extends TestCase {
  %include { mi/data/datamapping.tom }
  %include { sl.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(MainGomMapping.class));
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
      T1 res = (T1) `Repeat(OnceBottomUp(Rule())).visitLight(subject, new LocalIntrospector());
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
