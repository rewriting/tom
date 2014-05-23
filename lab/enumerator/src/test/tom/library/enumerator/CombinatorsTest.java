package test.tom.library.enumerator;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;

public class CombinatorsTest {

	@Test
	public void testMakeBoolean() {
		Enumeration<Boolean> e = Combinators.makeboolean();
		Boolean b1 = e.get(ZERO);
		assertTrue(b1.equals(true) || b1.equals(false));
		Boolean b2 = e.get(ONE);
		assertNotEquals(b2, b1);
		
		for (int i = 2; i < 100; i++) {
			try {
				e.get(BigInteger.valueOf(i));
				fail( "My method didn't throw when I expected it to" );
			} catch (RuntimeException expectedException) {}
		}
	}

	@Test
	public void testMakeNatural() {
		Enumeration<Integer> e = Combinators.makenat();
		Set<Integer> bag = new HashSet<Integer>();

		for (int i = 0; i < 100; i++) {
			bag.add(e.get(BigInteger.valueOf(i)));
			//System.out.println(e.get(BigInteger.valueOf(i)));
		}
		assertEquals(100, bag.size());
	}

	@Test
	public void testMakeInteger() {
		Enumeration<Integer> e = Combinators.makeint();
		Set<Integer> bag = new HashSet<Integer>();

		for (int i = 0; i < 100; i++) {
			bag.add(e.get(BigInteger.valueOf(i)));
			//System.out.println(e.get(BigInteger.valueOf(i)));
		}
		assertEquals(100, bag.size());
	}
	
	@Test
	public void testMakeLong() {
		Enumeration<Long> e = Combinators.makelong();
		Set<Long> bag = new HashSet<Long>();

		for (int i = 0; i < 100; i++) {
			bag.add(e.get(BigInteger.valueOf(i)));
		}
		assertEquals(100, bag.size());
	}
	
	@Test
	public void testListBoolean() {
		Enumeration<Boolean> e = Combinators.makeboolean();
		Enumeration<List<Boolean>> el = Combinators.listOf(e);
		int size = 0;

		for (int i = 0; i < 100; i++) {
			List<Boolean> l = el.get(BigInteger.valueOf(i));
			//System.out.println(l);
			assertTrue(l.size() >= size);
			size = l.size();
		}
	}
	
	@Test
	public void testListInteger() {
		Enumeration<Integer> e = Combinators.makeint();
		Enumeration<List<Integer>> el = Combinators.listOf(e);
		Set<List<Integer>> bag = new HashSet<List<Integer>>();

		for (int i = 0; i < 100; i++) {
			List<Integer> l = el.get(BigInteger.valueOf(i));
			//System.out.println(l);
			bag.add(l);
		}
		assertEquals(100, bag.size());
	}
	
	@Test
	public void testSetBoolean() {
		Enumeration<Boolean> e = Combinators.makeboolean();
		Enumeration<List<Boolean>> el = Combinators.listOf(e);

		for (int i = 0; i < 100; i++) {
			List<Boolean> l = el.get(BigInteger.valueOf(i));
			System.out.println(l);
		}
	}
	
	@Test
	public void testSetInteger() {
		Enumeration<Integer> e = Combinators.makeint();
		Enumeration<Set<Integer>> el = Combinators.setOf(e);
		Set<Set<Integer>> bag = new HashSet<Set<Integer>>();

		for (int i = 0; i < 100; i++) {
			Set<Integer> l = el.get(BigInteger.valueOf(i));
			System.out.println(l);
			bag.add(l);
		}
		//assertEquals(100, bag.size());
	}
}
