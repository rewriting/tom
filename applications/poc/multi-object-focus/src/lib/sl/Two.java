/**
 * Created with IntelliJ IDEA.
 * User: tof
 * Date: 11/2/12
 * Time: 4:04 AM
 * To change this template use File | Settings | File Templates.
 */


package lib.sl;

import lib.Fun;
import lib.MOFException;
import lib.Zip;
import tom.library.sl.Visitable;
import lib.P;


/**
 * new Two<X>(s:Visitor<P<Y,Y>,Z>) = new SelectChild2<X,Y>().seq(s).reset().up()
 *   with X,Y <= Visitable.
 * One tries to apply s to exactly two children (left to right) of its input.
 *
 * @param <X> the type of input term
 */
public class Two<X extends Visitable> extends Visitor<X,X> {
    private Visitor<X,X> visitor = null;

    public <Y extends  Visitable,Z> Two(Visitor<P<Y,Y>,Z> s) {
        visitor = new SelectChild2<X,Y>().seq(s).reset().up();
    }

    public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,X>,Ans> k) throws MOFException {
        return visitor.visitZK(z,k);
    }
}
