package enumerator;

public abstract class F2<A, B, C> {
	  public abstract C apply(A a, B b);
	  
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

