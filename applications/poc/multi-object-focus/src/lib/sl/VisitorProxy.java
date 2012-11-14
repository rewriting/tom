package lib.sl;

import lib.Fun;
import lib.MOFException;
import lib.Zip;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 14/11/12
 * Time: 19:01
 * To change this template use File | Settings | File Templates.
 */
public class VisitorProxy<X,Y> extends Visitor<X,Y> {
    protected Visitor<X,Y> visitor = null;
    public <T> X visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Zip<X,Y>> k) throws MOFException { return visitor.visitZK(z,k); }
}
