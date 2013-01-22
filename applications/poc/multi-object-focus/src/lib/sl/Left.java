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
public class Left<X,Y> extends Visitor<P<X,Y>,X> {
    public  <T> P<X,Y> visitZK(Zip<T,P<X,Y>> z, Fun<Zip<P<X,Y>,X>, Zip<P<X,Y>,X>> k) throws VisitFailure {
       return k.apply(z.focus.zipLeft()).run();
    }
}
