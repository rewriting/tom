package propcheck.generator.quickcheck;

import java.math.BigInteger;
import java.util.Random;

import propcheck.product.Product;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;

/**
 * Generates random value from the product of given enumerations. 
 * For triplet see {@link RandomGenerator3} 
 * 
 * TODO merge {@link RandomGenerator1}, {@link RandomGenerator2} and {@link RandomGenerator3} to a class if possible, or create a factory
 * 
 * @author nauval
 *
 * @param <A>
 * @param <B>
 */
public class RandomGenerator2<A, B> {
	
	Enumeration<Product<A, B>> product;
	LazyList<Finite<Product<A, B>>> parts;
	
	BigInteger index;
	BigInteger card;
	
	Random random;
	
	public RandomGenerator2(Enumeration<A> enumA, Enumeration<B> enumB) {
		buildEnumeration(enumA, enumB);
	}
	
	/**
	 * Builds enumerations of product of enumerations
	 * @param enumA
	 * @param enumB
	 */
	public void buildEnumeration(Enumeration<A> enumA, Enumeration<B> enumB) {
		product = Product.enumerate(enumA, enumB);
		parts = product.parts();
	}
	
	void buildIndex() {
		if (random == null) {
			random = new Random();
		}
		card = parts.head().getCard();
		index = new BigInteger(card.bitLength(), random);
	}
	
	public Product<A, B> generateNext() {
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
