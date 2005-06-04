package builtin;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestApiInteger extends TestCase {
	private ApiInteger test;

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestApiInteger.class));
  }

  public void setUp() {
    test = new ApiInteger();
  }
	
	public void testRun() {
    String res = test.run();
    String[] resTab = res.split("\n");
		assertEquals("The output should be composed of two lines",
								 2,resTab.length);
		assertEquals("The first match should answer \"10\", but got " + resTab[0],
								 "10",resTab[0]);
		assertEquals("The second match should answer \"32\", but got " + resTab[1],
								 "32",resTab[1]);
	}

}
