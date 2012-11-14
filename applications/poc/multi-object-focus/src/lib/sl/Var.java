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
public class Var<X,Y> extends VisitorProxy<X,Y> {
    /**
     * Initialise the visitor with null (handle with care!)
     */
    public Var()                    { visitor = null;   }

    /**
     * Equivalent to: new Var<X,Y>().set(v)
     * @param v
     */
    public Var(Visitor<X,Y> v)      { visitor = v;      }

    public Visitor<X,Y> set(Visitor<X,Y> v) { visitor = v; return visitor; }
}
