import junit.framework.TestCase;
import junit.framework.TestSuite;
import testlistnonlinear.test.types.*;



public class TestListNonLinear extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(Test.class));
  }
  
%gom {
  module test
  imports int
  abstract syntax
    IntList = intlist(int*)
    Term = f(l1:IntList,l2:IntList)
}


  public void test1() {
    IntList l1 = `intlist(1,2,4,5);
    IntList l2 = `intlist(1,2,3,4,5);
    %match(IntList l1, IntList l2) {
      (x*,y*),(x*,3,y*) -> {
        return;
      }
    }
    fail("ca marche pas");
  }

  public void test2() {
    IntList l1 = `intlist(1,2,4,5);
    IntList l2 = `intlist(1,2,3,4,5);
    Term t = `f(l1,l2);
    %match(t) {
      f((x*,y*),(x*,3,y*)) -> {
        return;
      }
    }
    fail("ca marche pas");
  }
}
