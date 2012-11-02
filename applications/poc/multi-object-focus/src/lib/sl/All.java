package lib.sl;

import lib.Fun;
import lib.MOFException;
import lib.Zip;
import tom.library.sl.Visitable;

/**
 * Created with IntelliJ IDEA.
 * User: tof
 * Date: 11/2/12
 * Time: 4:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class All<X extends Visitable> extends Visitor<X,X> {
    private Visitor<X,X> visitor = null;

    public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,X>,Ans> k) throws MOFException {
        return visitor.visitZK(z,k);
    }

    public <Y extends Visitable, Z> All(final Visitor<Y,Z> s) {
        visitor = new Visitor<X, X>() {
            public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,X>,Ans>  k) throws MOFException {
                return all_aux(s,k,z,0);
            }};
    }


    private <Y extends Visitable, Z, T, Ans> Ans all_aux(Visitor<Y,Z> s, Fun<Zip<X,X>,Ans>  k, Zip<T,X> z, int i) throws MOFException {
        X t = z.focus;
        if (i >= t.getChildCount()) { return k.apply(Zip.unit(t)); }
        else { return all_aux(s,k, (new Child<X,Y>(i)).seq(s).up().visitZ(z),i+1); }
    }
}
