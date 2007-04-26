package sl;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import sl.testmutraveleroncetopdownid.test.types.*;

public class TestMuTravelerOnceTopDownId extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestMuTravelerOnceTopDownId.class));
  }
  
  %include { mustrategy.tom }
  %gom {
    module test
    imports
    abstract syntax
    Term = a()
         | b()
         | f(t1:Term,t2:Term)
  }

  public void test1() {
    Term t = `f(a(),a());
    try {
      t = (Term)`OnceTopDownId(Bug()).visit(t);
      assertEquals("It should rewite one a()",`f(b(),a()),t);
    } catch (jjtraveler.VisitFailure f) {
      fail("It should not fail");
    }
    try {
      t = (Term)`OnceTopDownId(Bug()).visit(t);
      fail("It should fail, but got "+t);
    } catch (jjtraveler.VisitFailure f) {
    }
  }

  %strategy Bug() extends Identity() {
    visit Term {
      a() -> { return `b(); }
      b() -> { throw new jjtraveler.VisitFailure(); }
    }
  }
}
