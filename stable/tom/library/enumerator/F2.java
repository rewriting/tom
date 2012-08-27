package tom.library.enumerator;

public abstract class F2<A, B, C> {
	/*
	 * apply the function to A,B
	 */
	public abstract C apply(A a, B b);

	/*
	 * curryfy the binary function
	 */
	public final F<A, F<B, C>> curry() {
		return new F<A, F<B, C>>() {
			public F<B, C> apply(final A a) {
				return new F<B, C>() {
					public C apply(final B b) {
						return F2.this.apply(a, b);
					}
				};
			}
		};
	}
}
