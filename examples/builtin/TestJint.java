package builtin;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestJint extends TestCase {
	private Jint test;

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestJint.class));
  }

  public void setUp() {
    test = new Jint();
  }
	
	public void testFib() {
		assertEquals("Fibonacci 10 is 89",89,test.fib(10));
	}

}
