package examples.jcheck;

import java.math.BigInteger;
import java.util.Random;

import org.jcheck.generator.Gen;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;

public class JCheckGenerator implements Gen<Integer> {
	private int next = 0;

	Enumeration<Integer> enumInt = Combinators.makeInteger();

	@Override
	public Integer arbitrary(Random random, long size) {
		return enumInt.get(BigInteger.valueOf(next++));

	}

}