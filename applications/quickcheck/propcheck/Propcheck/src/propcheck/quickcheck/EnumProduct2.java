package propcheck.quickcheck;

import java.math.BigInteger;
import java.util.Random;

import propcheck.product.Product2;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;

/**
 * Generates random value from the product of given enumerations. 
 * For pair see {@link EnumProduct}
 * 
 * TODO merge {@link EnumProduct} and {@link EnumProduct2} to a class if possible, or create a factory
 * 
 * @author nauval
 *
 * @param <A>
 * @param <B>
 * @param <C>
 */
public class EnumProduct2<A, B, C> {
	
	Enumeration<Product2<A, B, C>> product;
	LazyList<Finite<Product2<A, B, C>>> parts;
	
	BigInteger index;
	BigInteger card;
	
	Random random;
	
	public EnumProduct2(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC) {
		buildProduct(enumA, enumB, enumC);
	}
	
	public void buildProduct(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC) {
		product = Product2.enumerate(enumA, enumB, enumC);
		parts = product.parts();
	}
	
	void buildIndex() {
		if (random == null) {
			random = new Random();
		}
		card = parts.head().getCard();
		index = new BigInteger(card.bitLength(), random);
	}
	
	public Product2<A, B, C> generateNext() {
		//Product2<A, B, C> p = null;
		buildIndex();
		/*if (index.compareTo(card) < 0) {
			p = parts.head().get(index);
		}*/
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
