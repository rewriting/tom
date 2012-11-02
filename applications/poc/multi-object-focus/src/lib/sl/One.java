package lib.sl;

import lib.Fun;
import lib.MOFException;
import lib.Zip;
import tom.library.sl.Visitable;

/**
 * Created with IntelliJ IDEA.
 * User: tof
 * Date: 11/2/12
 * Time: 4:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class One<X extends Visitable> extends Visitor<X,X> {
    private Visitor<X,X> visitor = null;

    public <Y extends  Visitable,Z> One(Visitor<Y,Z> s) {
        visitor = new SelectChild<X,Y>().seq(s).reset().up();
    }

    public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,X>,Ans> k) throws MOFException {
        return visitor.visitZK(z,k);
    }
}
