package tom.library.enumerator;

public abstract class F3<A, B, C, D> {
	/*
	 * apply the function to A,B,C
	 */
	public abstract D apply(A a, B b, C c);

	/*
	 * curryfy the binary function
	 */
	public final F<A, F<B, F<C, D>>> curry() {
		return new F<A, F<B, F<C, D>>>() {
			public F<B, F<C, D>> apply(final A a) {
				return new F<B, F<C, D>>() {
					public F<C, D>  apply(final B b) {
            return new F<C, D>() {
              public D apply(final C c) {
                return F3.this.apply(a, b, c);
              }
            };
          }
        };
      }
		};
	}
}
