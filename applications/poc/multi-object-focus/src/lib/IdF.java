package lib;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 14/11/12
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */

/**
 * The identity function over X.
 *
 * @param <X> the type of the domain and codomain.
 */
public class IdF<X> extends Fun<X,X> {
    public X apply(X x) { return x; }
}
