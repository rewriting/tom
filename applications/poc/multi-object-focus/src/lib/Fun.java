package lib;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 23/10/12
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class Fun<A,B> {
    public abstract B apply(A arg) throws MOFException;

    public <C> Fun<A,C> andthen (final Fun<B,C> g) {
        final Fun<A,B> f = this;
        return new Fun<A,C>() {  public C apply(A a) throws MOFException { return g.apply(f.apply(a)); } } ;
    }

    public <C> Fun<C,B> compose (final Fun<C,A> g) {
        final Fun<A,B> f = this;
        return new Fun<C,B>() { public B apply(C a) throws MOFException { return f.apply(g.apply(a)); } } ;
    }


    public <C,D> Fun<P<A,C>,P<B,D>> times(final Fun<C,D> g) {
        final Fun<A,B> f = this;
        return new Fun<P<A,C>,P<B,D>>() {
                 public P<B,D> apply(P<A,C> p) throws MOFException { return new P<B,D>(f.apply(p.left), g.apply(p.right)) ; }
               } ;
    }

    public static <T> Fun<T,T> id(T t) { return new Fun<T,T>() { public T apply(T u) { return u; } }; }
}
