package sl;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import sl.testoncetopdownid.test.types.*;
import tom.library.sl.*;

public class TestOnceTopDownId extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestOnceTopDownId.class));
  }
  
  %include { sl.tom }
  %gom {
    module test
    imports
    abstract syntax
    Term = a()
         | b()
         | c()
         | f(t1:Term,t2:Term)
  }

  public void test1() throws VisitFailure{
    Term t = `f(a(),a());
    try {
      t = (Term)`OnceTopDownId(Bug()).visitLight(t);
      assertEquals("It should rewite one a()",`f(b(),a()),t);
    } catch (tom.library.sl.VisitFailure f) {
      fail("It should not fail");
    }
    try {
      t = (Term)`OnceTopDownId(Bug()).visitLight(t);
      fail("It should fail, but got "+t);
    } catch (tom.library.sl.VisitFailure f) {
    }

    Term t2 = `f(c(),c());
    t = (Term)`OnceTopDownId(Bug()).visit(t2);
    assertEquals("they should be equal",t,t2);
  }

  %strategy Bug() extends Identity() {
    visit Term {
      a() -> { return `b(); }
      b() -> { throw new tom.library.sl.VisitFailure(); }
    }
  }
}
