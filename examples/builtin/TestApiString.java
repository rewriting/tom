package builtin;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestApiString extends TestCase {
	private ApiString test;

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestApiString.class));
  }

  public void setUp() {
    test = new ApiString();
  }
	
	public void testRun() {
    String res = test.run();
    String[] resTab = res.split("\n");
		assertEquals("The output should be composed of two lines",
								 2,resTab.length);
		assertEquals("The first match should answer \"Albert\", but got " + resTab[0],
								 "Albert",resTab[0]);
		assertEquals("The second match should answer \"Roger\", but got " + resTab[1],
								 "Roger",resTab[1]);
	}

}
