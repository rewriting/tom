package propcheck.product;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

/**
 * Generates enumeration of triplet of given types. Given type {@code A, B, C}, 
 * the returned {@code Enumeration} is enumeration of {@code AxBxC}.
 * 
 * @author nauval
 *
 * @param <A>
 * @param <B>
 * @param <C>
 */
public class Product2<A, B, C> {
	A p1;
	B p2;
	C p3;
	
	protected Product2(A a, B b, C c) {
		p1 = a;
		p2 = b;
		p3 = c;
	}
	
	/**
	 * Returns Enumeration of product of A, B, C
	 * @param enumA
	 * @param enumB
	 * @param enumC
	 * @return
	 */
	public static <A,B, C> Enumeration<Product2<A, B, C>> enumerate(Enumeration<A> enumA, Enumeration<B> enumB, Enumeration<C> enumC) {
		
		/*build function*/
		F<A, F<B, F<C, Product2<A, B, C>>>> function = new F<A, F<B,F<C,Product2<A,B,C>>>>() {

			@Override
			public F<B, F<C, Product2<A, B, C>>> apply(final A a) {
				return new F<B, F<C,Product2<A,B,C>>>() {

					@Override
					public F<C, Product2<A, B, C>> apply(final B b) {
						return new F<C, Product2<A,B,C>>() {

							@Override
							public Product2<A, B, C> apply(final C c) {
								return new Product2<A, B, C>(a, b, c);
							}
						};
					}
				};
			}
		};
		
		return Enumeration.apply(Enumeration.apply(Enumeration.apply(Enumeration.singleton(function), enumA), enumB), enumC);
	}
	
	public A p1() {
		return p1;
	}
	
	public B p2() {
		return p2;
	}
	
	public C p3() {
		return p3;
	}
	
	@Override
	public String toString() {
		return "{" + p1 + "," + p2 + ", " + p3 + "}";
	}
}
