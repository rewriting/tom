package propcheck.generator.quickcheck;

import java.math.BigInteger;
import java.util.Random;

import propcheck.product.Product2;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;

/**
 * Generates random value from the product of given enumerations. 
 * For pair see {@link RandomGenerator2}
 * 
 * TODO merge {@link RandomGenerator1}, {@link RandomGenerator2} and {@link RandomGenerator3} to a class if possible, or create a factory
 * 
 * @author nauval
 *
 * @param <A>
 * @param <B>
 * @param <C>
 */
public class RandomGenerator3<A, B, C> {
	
	Enumeration<Product2<A, B, C>> product;
	LazyList<Finite<Product2<A, B, C>>> parts;
	
	BigInteger index;
	BigInteger card;
	
	Random random;
	
	public RandomGenerator3(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC) {
		buildProduct(enumA, enumB, enumC);
	}
	
	/**
	 * Builds the product of given enumerations
	 * 
	 * @param enumA
	 * @param enumB
	 * @param enumC
	 */
	public void buildProduct(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC) {
		product = Product2.enumerate(enumA, enumB, enumC);
		parts = product.parts();
	}
	
	/**
	 * Builds the index that are used to get a term from the enumeration randomly
	 */
	void buildIndex() {
		if (random == null) {
			random = new Random();
		}
		card = parts.head().getCard();
		index = new BigInteger(card.bitLength(), random);
	}
	
	/**
	 * Returns next randomly generated product of enumeration
	 * 
	 * @return {@code Product2<A, B, C>}
	 */
	public Product2<A, B, C> generateNext() {
		buildIndex();
		while (index.compareTo(card) >= 0) {
			moveToNextPart();
			buildIndex();
		}
		return parts.head().get(index);
	}
	
	/**
	 * Moves current part to the next part
	 */
	public void moveToNextPart() {
		parts = parts.tail();
	}
}
