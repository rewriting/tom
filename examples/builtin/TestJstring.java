package builtin;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestJstring extends TestCase {
	private Jstring test;

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestJstring.class));
  }

  public void setUp() {
    test = new Jstring();
  }
	
	public void testFcoucou() {
		assertEquals("input coucou gets coucou",
								 "coucou",test.f("coucou"));
	}

	public void testFhello() {
		assertEquals("input hello gets hello",
								 "hello",test.f("hello"));
	}

	public void testFelse() {
		assertEquals("input something gets unknown",
								 "unknown",test.f("something"));
	}

}
