package examples;

import java.math.BigInteger;
import java.util.Random;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;

public final class Tool {
	public static <A> A getTerm(Enumeration<A> aEnum, int depth) {
		Random random = new Random();
		BigInteger card, index;
		
		LazyList<Finite<A>> parts = aEnum.parts();
		for (int i = 0; i < depth; i++) {
			parts = parts.tail();
		}
		card = parts.head().getCard();
		index = new BigInteger(card.bitLength(), random);
		
		A a =  aEnum.get(index);
		return a;
	}
}
