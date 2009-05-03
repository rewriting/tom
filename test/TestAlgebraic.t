import org.junit.Test;
import org.junit.Assert;
import testalgebraic.sig.*;
import testalgebraic.sig.types.*;
import tom.library.sl.*;

public class TestAlgebraic {
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestAlgebraic.class.getName());
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

  @Test
  public void test1() {
    T1 res = `f(a());
    Strategy s = `RepeatId(BottomUp(R()));
    try {
      res = s.visit(res);
      Assert.assertEquals(res,`f(c()));
      return;
    } catch(VisitFailure e) {
      System.out.println("Failure");
    }
    Assert.fail("should not be there");
  }
}
