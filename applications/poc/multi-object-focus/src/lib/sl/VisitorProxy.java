package lib.sl;

import lib.Fun;
import tom.library.sl.VisitFailure;
import lib.Zip;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 14/11/12
 * Time: 19:01
 * To change this template use File | Settings | File Templates.
 */

/**
 * VisitorProxy<X,Y> holds a protected field #visitor that implements the real behavior.
 * It is used as a base for Var and others.
 *
 * @param <X>
 * @param <Y>
 */

public class VisitorProxy<X,Y> extends Visitor<X,Y> {
    /**
     * The real visitor.
     */
    protected Visitor<X,Y> visitor = null;

    /**
     * Just call this.visitor.visitZK
     */
    public <T> X visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Zip<X,Y>> k) throws VisitFailure { return visitor.visitZK(z,k); }
}
