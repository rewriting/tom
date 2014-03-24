package propcheck.product;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

/**
 * Generate an enumeration of a pair of given type.Given type {@code A, B}, 
 * the returned {@code Enumeration} is enumeration of {@code AxB} (a pair of {@code A} and {@code B}).
 *  
 * @author nauval
 *
 * @param <A>
 * @param <B>
 */
public class Product<A, B> {
	A p1;
	B p2;
	
	protected Product(A a, B b) {
		p1 = a;
		p2 = b;
	}
	
	/**
	 * Returns enumeration of product of A and B
	 * @param enumA
	 * @param enumB
	 * @return
	 */
	public static <A,B> Enumeration<Product<A, B>> enumerate(Enumeration<A> enumA, Enumeration<B> enumB) {
		
		/*build function*/
		F<A, F<B, Product<A, B>>> function = new F<A, F<B,Product<A,B>>>() {

			@Override
			public F<B, Product<A, B>> apply( final A a) {
				return new F<B, Product<A,B>>() {

					@Override
					public Product<A, B> apply(final B b) {
						return new Product<A, B>(a, b);
					}
				};
			}
			
		};
		
		return Enumeration.apply(Enumeration.apply(Enumeration.singleton(function), enumA), enumB);
	}
	
	public A p1() {
		return p1;
	}
	
	public B p2() {
		return p2;
	}
	
	@Override
	public String toString() {
		return "{" + p1 + "," + p2 + "}";
	}
}
