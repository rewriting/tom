import junit.framework.TestCase;
import junit.framework.TestSuite;
import testmatchinference.test.types.*;

public class TestMatchInference extends TestCase {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestMatchInference.class));
	}
  
%gom {
  module test
  imports int
  abstract syntax
  Term = a()
       | f(s:Term)
}

  public void test1() {
    %match(5) {
      4 -> { fail("4 does not match"); }
      5 -> { return; }
      6 -> { fail("6 does not match"); }
    }
    fail("5 should match");
  }

  public void test2() {
    %match(5,"foo") {
      5,"goo" -> { fail("5,goo does not match"); }
      5,"bar" -> { fail("5,bar does not match"); }
      5,"foo" -> { return; }
      6,"foo" -> { fail("6,foo does not match"); }
    }
    fail("5,foo should match");
  }

  private Term ff(Term x) { return `f(x); }
  public void test3() {
    %match(f(a()),ff(a())) {
      f(a()),a() -> { fail(); }
      a(),f(a()) -> { fail(); }
      f(a()),f(x) -> { return; }
    }
    fail("a,f(a) hould match");
  }

}
