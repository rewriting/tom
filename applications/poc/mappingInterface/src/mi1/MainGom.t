package mi1;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;


import base.data.types.*;
import base.data.types.t1.*;
import base.data.types.t2.*;
import tom.library.sl.*;

public class MainGom extends TestCase {
  %include { gom/mapping.tom }
  %include { sl.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(MainGom.class));
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
