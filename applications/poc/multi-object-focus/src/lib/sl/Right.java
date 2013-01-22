package lib.sl;

import lib.*;
import tom.library.sl.VisitFailure;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 22/01/13
 * Time: 18:46
 * To change this template use File | Settings | File Templates.
 */
public class Right<X,Y> extends Visitor<P<X,Y>,Y> {
    public  <T> P<X,Y> visitZK(Zip<T,P<X,Y>> z, Fun<Zip<P<X,Y>,Y>, Zip<P<X,Y>,Y>> k) throws VisitFailure {
        return k.apply(z.focus.zipRight()).run();
    }
}
