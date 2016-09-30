import static org.junit.Assert.*;
import org.junit.Test;
import testmatchinference.test.types.*;

public class TestMatchInference {
	public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestMatchInference.class.getName());
	}
  
%gom {
  module test
  imports int String
  abstract syntax
  Term = a()
       | f(s:Term)
}

  @Test
  public void test1() {
    %match(int 5) {
      4 -> { fail("4 does not match"); }
      5 -> { return; }
      6 -> { fail("6 does not match"); }
    }
    fail("5 should match");
  }

  @Test
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
  @Test
  public void test3() {
    %match(f(a()),ff(a())) {
      f(a()),a() -> { fail(); }
      a(),f(a()) -> { fail(); }
      f(a()),f(_x) -> { return; }
    }
    fail("a,f(a) hould match");
  }

}
