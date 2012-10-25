/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 24/10/12
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */

package lib.zip;

import lib.*;
import lib.fun.*;

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
}
