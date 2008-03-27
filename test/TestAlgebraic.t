import junit.framework.TestCase;
import junit.framework.TestSuite;
import testalgebraic.sig.*;
import testalgebraic.sig.types.*;
import tom.library.sl.*;

public class TestAlgebraic extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestAlgebraic.class));
  }

  %include { sl.tom }
%gom {
  module sig
  imports
  abstract syntax
  T1 = a() | b() | c()
    | f(x:T1)
  T2 = d()
}

  %strategy R() extends Identity() {
    visit T1 {
      a() -> b()
      b() -> c()
    }
  }

  public void test1() {
    T1 res = `f(a());
    Strategy s = `RepeatId(BottomUp(R()));
    try {
      res = (T1)s.visit(res);
      assertEquals(res,`f(c()));
      return;
    } catch(VisitFailure e) {
      System.out.println("Failure");
    }
    fail("should not be there");
  }
  
}
