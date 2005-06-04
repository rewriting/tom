package builtin;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestPathFinder extends TestCase {
	private PathFinder test;

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestPathFinder.class));
  }

  public void setUp() {
    test = new PathFinder();
  }
	
	public void testf2() {
    String s1 = "aaaabaaaabaaaabaaaabaaaabaaaabaaaabaaabaa";
    String res = test.f2(s1);
    String[] resTab = res.split("\n");
		assertEquals("There are 28 ways to find 6 \"b\" among 8",
								 28,resTab.length);
	}

	public void testdoubleB() {
    String s1 = "aaaabaaaabaaaabaaaabaaaabaaaabaaaabaaabaa";
    int res = test.doubleB(s1);
		assertEquals("There are 28 ways to find 2 \"b\" among 8",
								 28,res);
	}

	public void testdoubleBwhen() {
    String s1 = "aaaabaaaabaaaabaaaabaaaabaaaabaaaabaaabaa";
    int res = test.doubleBwhen(s1);
		assertEquals("There are 28 ways to find 2 \"b\" among 8",
								 28,res);
	}
}
