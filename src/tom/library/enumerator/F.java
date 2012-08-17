package enumerator;

public abstract class F<A, B> {
	  public abstract B apply(A a);
	  /*
	   * this o g = this(g)
	   */
	  public final <C> F<C, B> o(final F<C, A> g) {
		    return new F<C, B>() {
		      public B apply(final C c) {
		        return F.this.apply(g.apply(c));
		      }
		    };
		  }

}