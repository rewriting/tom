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


/**
 * new One<X>(s:Visitor<Y,Z>) = new SelectChild<X,Y>().seq(s).reset().up()
 *   with X,Y <= Visitable.
 * One tries to apply s to exactly one child (left to right) of its input.
 *
 * @param <X> the type of input term
 */
public class One<X extends Visitable> extends Visitor<X,X> {
    private Visitor<X,X> visitor = null;

    public <Y extends  Visitable,Z> One(Visitor<Y,Z> s) {
        visitor = new SelectChild<X,Y>().seq(s).reset().up();
    }

    public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,X>,Ans> k) throws MOFException {
        return visitor.visitZK(z,k);
    }


    public static <X extends Visitable> Visitor<X,X> onceBottomUp(final Visitor<X,X> s) throws MOFException {
        return fix(new Fun<Visitor<X,X>,Visitor<X,X>>(){ public Visitor<X,X> apply(Visitor<X,X> v) throws MOFException {
            return new One<X>(v).or(s);
        }});
    }


}
