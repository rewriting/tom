/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 24/10/12
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */

package lib;

public class Zip<T,S> {
    public Fun<S,T> context;
    public S        focus;

    public Zip(Fun<S, T> c, S f) {
        context = c;
        focus   = f;
    }


    public T run() throws MOFException { return context.apply(focus); }

    public Zip<T,S> replace(S s) { return new Zip<T,S>(context,s); }

    public <R> Zip<T,R> refocus(Fun<S, Zip<S,R>> f) throws MOFException {
        final Zip<T,S> thiszip = this;
        final Zip<S,R> zf      = f.apply(thiszip.focus);
        return new Zip<T, R>( this.context.compose(zf.context)
                            , zf.focus
                            ) ;
    }

    public <A,B> Zip<P<T,A>,P<S,B>> times(Zip<A,B> z) {
        return new Zip<P<T,A>,P<S,B>>( this.context.times(z.context) , new P<S, B>(this.focus,z.focus) ) ;
    }

// Static Methods

    public static <T,S> Zip<T,S> mkZip(Fun<S,T> c, S f) { return new Zip<T,S>(c,f); }

    public static <T,S extends T> Zip<T,S> unit(S s) {
        return new Zip<T,S>( new Fun<S, T>() {
                               public T apply(S arg) throws MOFException { return arg; }
                             }
                           , s
                           );

    }

    public static <T,S,R> Zip<T,R> join(final Zip<T,Zip<S,R>> z) {
        final Zip<S,R> zs = z.focus;
        return new Zip<T, R>( new Fun<R,T>() { public T apply(R r) throws MOFException { return z.context.apply(zs.replace(r)); } }
                , zs.focus
        ) ;
    }

    public static <X,Y> Fun<X,Zip<Y,Y>> lift(final Fun<X,Y> f) {
        return new Fun<X,Zip<Y,Y>>() {
            public Zip<Y,Y> apply(X x) throws MOFException { return unit(f.apply(x));}
        };
    }


}
