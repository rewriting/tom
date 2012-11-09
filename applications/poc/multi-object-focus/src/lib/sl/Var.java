package lib.sl;

import lib.MOFException;
import lib.Zip;
import lib.Fun;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 08/11/12
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */

/**
 * Var<X,Y> is a visitor variable. It can be used as any Visitor<X,Y> but its behavior can be changed by
 * calling method #set
 */
public class Var<X,Y> extends Visitor<X,Y> {
    private Visitor<X,Y> visitor = null;

    /**
     * Set the behavior of this to be the one of the visitor v
     * @param v
     */
    public Visitor<X,Y> set(Visitor<X,Y> v) {
        visitor = v;
        return this;
    }

    /**
     * Initialise the visitor with null (handle with care!)
     */
    public Var()                    { visitor = null;   }

    /**
     * Equivalent to: new Var<X,Y>().set(v)
     * @param v
     */
    public Var(Visitor<X,Y> v)      { visitor = v;      }

    public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Ans> k) throws MOFException { return visitor.visitZK(z,k); }
}
