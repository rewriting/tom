package propcheck.quickcheck;

import java.math.BigInteger;
import java.util.Random;

import propcheck.product.Product;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;

/**
 * Generates random value from the product of given enumerations. 
 * For triplet see {@link EnumProduct2} 
 * 
 * TODO merge {@link EnumProduct} and {@link EnumProduct2} to a class if possible, or create a factory
 * 
 * @author nauval
 *
 * @param <A>
 * @param <B>
 */
public class EnumProduct<A, B> {
	
	Enumeration<Product<A, B>> product;
	LazyList<Finite<Product<A, B>>> parts;
	
	BigInteger index;
	BigInteger card;
	
	Random random;
	
	public EnumProduct(Enumeration<A> enumA, Enumeration<B> enumB) {
		buildProduct(enumA, enumB);
	}
	
	public void buildProduct(Enumeration<A> enumA, Enumeration<B> enumB) {
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
