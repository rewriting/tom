package test;

import static org.junit.Assert.*;

import org.junit.Test;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.enumerator.Finite;
import tom.library.enumerator.P2;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;

public class CombinatorsTest {

	@Test
	public void testMakeBoolean() {
		Enumeration<Boolean> e = Combinators.makeboolean();
		Boolean b1 = e.get(ZERO);
		assertEquals(true || false, b1);
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
			System.out.println(e.get(BigInteger.valueOf(i)));
		}
		assertEquals(100, bag.size());
	}
}
