package iwm.tomtest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import strings.Anagram;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	private Anagram test;

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
		test = new Anagram();

	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(test.isAnagram("aaa", "aaa"));
		assertFalse(test.isAnagram("aaa", "aa"));
		assertTrue(test.isAnagram("ressasser", "ressasser"));
		assertTrue(test.isAnagram("tom marvolo riddle ", "i am lord voldemort"));
		assertFalse(test.isAnagram("this is not an anagram",
				"that is not an anagram"));
	}
}
