package propcheck.generator.quickcheck;

import java.math.BigInteger;
import java.util.Random;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;

/**
 * Handles the generation of random term out of the given enumeration.
 * 
 * TODO merge {@link RandomGenerator1}, {@link RandomGenerator2} and {@link RandomGenerator3} to a class if possible, or create a factory
 * 
 * @author nauval
 *
 */
public class RandomGenerator1<A> {

	Enumeration<A> enumeration;
	LazyList<Finite<A>> parts;
	
	BigInteger index;
	BigInteger card;
	
	Random random;
	
	public RandomGenerator1(Enumeration<A> enumA) {
		buildEnumeration(enumA);
	}
	
	public void buildEnumeration(Enumeration<A> enumA) {
		enumeration = enumA;
		parts = enumeration.parts();
	}
	
	void buildIndex() {
		if (random == null) {
			random = new Random();
		}
		card = parts.head().getCard();
		index = new BigInteger(card.bitLength(), random);
	}
	
	public A generateNext() {
		buildIndex();
		while (index.compareTo(card) >= 0) {
			moveToNextPart();
			buildIndex();
		}
		return parts.head().get(index);
	}
	
	public void moveToNextPart() {
		parts = parts.tail();
	}
}
