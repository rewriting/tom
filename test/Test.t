import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Test extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(Test.class));
  }
  
%gom {
  module test
  imports
  abstract syntax
}

  public void test1() {
    if(true) {
      return ;
    } 
    fail("should not be there");
  }

}
