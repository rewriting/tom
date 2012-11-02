package lib.sl;

import lib.Fun;
import lib.MOFException;
import lib.Zip;
import tom.library.sl.Visitable;

/**
 * Created with IntelliJ IDEA.
 * User: tof
 * Date: 11/2/12
 * Time: 3:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class Child <X extends Visitable, Y extends Visitable> extends Visitor<X,Y> {
    private Visitor<X,Y> visitor = null;


    public Child(int i) {
        visitor = at(i);
    }


    public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Ans> k) throws MOFException {
     return visitor.visitZK(z,k);
    }



    public Visitor<X,Y> at (final int i) {
        return new Visitor<X, Y>() {
            public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Ans> k) throws MOFException {
                final X t = z.focus;
                if (i >= t.getChildCount()) throw new MOFException() ;
                else return k.apply(Zip.mkZip( new Fun<Y,X>() { public X apply(Y y) {
                    return (X)(t.setChildAt(i,y)); }}
                        , (Y)(t.getChildAt(i))
                ));
            }};
    }

}
