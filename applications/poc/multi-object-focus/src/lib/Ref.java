package lib;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 19:01
 * To change this template use File | Settings | File Templates.
 */


/**
 * Implement a reference. Ref<X> is essentially a pointer to values of type X. Its main use here is
 * to enable mutable variables in anonymous classes. Indeed, all free variables of an anonynous class must be declared
 * final and so not mutable. A variable of type Ref<X> can be declared final while still being mutable by set.
 *
 * @param <X> the type of the inner value.
 */
public class Ref<X> {
    /**
     * The mutable variable
     */
    public X value ;

    /**
     * Build a Ref with inner value x
     * @param x
     * @return a Ref<X>
     */
    public      Ref(X x) { value = x; }

    /**
     * Set the inner value
     * @param x the new value
     */
    public void set(X x) { value = x; }
}
