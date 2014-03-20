package test.propcheck.assertion;

import static org.junit.Assert.*;

import org.junit.Test;

import propcheck.assertion.NotTestedSkip;
import propcheck.assertion.Operator;;

public class TestAssert {

	@Test
	public void testEqualTrue() {
		try {
			Operator.equalsTo(1, 1);
			Operator.equalsTo("a", "a");
			Operator.equalsTo(new Integer(4), new Integer(4));
		} catch (AssertionError e) {
			fail(e.getMessage());
		}
	}
	
	@Test(expected = AssertionError.class)
	public void testEqualFalse() {
		Operator.equalsTo("a", "cd");
	}

	@Test
	public void testImpliesTrue() {
		int x = 7, y = 8;
		try {
			Operator.implies(y - x > 0, y > x);
			Operator.implies(y - x >= 1, y > x);
			Operator.implies(y > x, y - x > 0);
		} catch (AssertionError e) {
			fail(e.getMessage());
		}
	}
	
	@Test(expected = AssertionError.class)
	public void testImpliesFalse() {
		int x = 7, y = 8;
		Operator.implies(y - x > 0, y == x);
		Operator.implies("aa".length() > 1, "aa".length() > 0);
		Operator.implies(true, false);
	}
	
	@Test(expected = NotTestedSkip.class)
	public void testImpliesSkip() {
		Operator.implies(false, true);
		Operator.implies(false, false);
	}
	
	@Test
	public void testSkipInForTrue() {
		int max = 100;
		int t = 0;
		int t2 = 0;
		for (int i = 0; t2 < max; i++) {
			try {
				if (i < 78)
					Operator.implies(false, true);
				else  
					Operator.implies(true, true);
				t2 ++;
			} catch (NotTestedSkip e) {
				t++;
			} catch (AssertionError e) {
				fail(e.getMessage());
			}
		}
		//System.out.printf("t = %s\nt2 = %s", t, t2);
		assertEquals(t, 78);
		assertEquals(t2, 100);
	}
	
	@Test(expected = AssertionError.class)
	public void testSkipInForFalse() {
		int max = 100;
		int t = 0;
		int t2 = 0;
		for (int i = 0; i < max; i++) {
			try {
				Operator.implies(true, true);
				if (i == 78) {
					Operator.implies(true, false);
				}
				t2++;
			} catch (NotTestedSkip e) {
				t++;
			} 
		}
		System.out.printf("t = %s\nt2 = %s", t, t2);
	}
}
