/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 24/10/12
 * Time: 18:00
 * To change this template use File | Settings | File Templates.
 */

package lib.fun;

public class FunLib {
    public static <T> Fun<T,T> id(T t) { return new Fun<T,T>() { public T apply(T u) { return u; } }; }
}
