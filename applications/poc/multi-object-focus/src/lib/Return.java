package lib;

import lib.Cps;
import lib.Fun;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 09/11/12
 * Time: 18:20
 * To change this template use File | Settings | File Templates.
 */
public class Return<B,A> extends Fun<A,Cps<B,A>> {
    public Cps<B,A> apply(A a) { return Cps.unit(a); }
}
