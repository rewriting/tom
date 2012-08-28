package enumerator;

import tom.library.enumerator.*;
import java.util.HashMap;

import enumerator.mutual.types.A;
import enumerator.mutual.types.B;

/**
 * will be automatically generated
 */
public class MutualEnum {
	/**
	 * module enumerator.Mutual
	 * abstract syntax
	 * 
	 * A = a() | foo(b:B) | hoo(a:A)
	 * B = b() | grr(a:A)
	 */
	private void init() {
		Enumeration<A> enumA = new Enumeration<A>((LazyList<Finite<A>>) null);
		Enumeration<B> enumB = new Enumeration<B>((LazyList<Finite<B>>) null);

		final Enumeration<A> sortA = enumerator.mutual.types.a.hoo.funMake().apply(enumA).apply(enumB)
      .plus(enumerator.mutual.types.a.foo.funMake().apply(enumB))
      .plus(enumerator.mutual.types.a.a.funMake().apply(enumA));

		final Enumeration<B> sortB = enumerator.mutual.types.b.grr.funMake().apply(enumA)
      .plus(enumerator.mutual.types.b.b.funMake().apply(enumB));

		enumA.p1 = new P1<LazyList<Finite<A>>>() {
			public LazyList<Finite<A>> _1() {
				return sortA.parts();
			}
		};

		enumB.p1 = new P1<LazyList<Finite<B>>>() {
			public LazyList<Finite<B>> _1() {
				return sortB.parts();
			}
		};

    A.putEnumeration(sortA);
    B.putEnumeration(sortB);

	}
}
