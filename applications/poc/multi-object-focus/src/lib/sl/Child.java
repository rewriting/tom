/**
 * Created with IntelliJ IDEA.
 * User: tof
 * Date: 11/2/12
 * Time: 3:45 AM
 * To change this template use File | Settings | File Templates.
 */
package lib.sl;

import lib.Fun;
import lib.MOFException;
import lib.Zip;
import tom.library.sl.Visitable;

/**
 * A visitor that take a Visitable node as input and set the focus on its i-th child if it exits
 * and fail otherwise.
 * <p>
 * IMPORTANT: this component uses getChildAt to a get a child and cast it to Y. X,Y can be used in
 *  to ways:
 *  - Instantiate X by Visitable to avoid cast errors if getChildAt can return values not of Y.
 *  - If getChildAt returns ALWAYS a value on type Y on the input then use a more specific Y.
 *
 * @param <X> the type of the input Visitable node
 * @param <Y> the type of the i-th child.
 */
public class Child <X extends Visitable, Y extends Visitable> extends Visitor<X,Y> {
    /*
      visitor is the actual visitor. It is set by the constructor.
     */
    private Visitor<X,Y> visitor = null;


    /**
     * Build a Visitor setting the focus on the i-th child.
     *
     * @param i
     */
    public Child(int i) {
        visitor = at(i);
    }


    public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Ans> k) throws MOFException {
     return visitor.visitZK(z,k);
    }



    /**
     * Build a Visitor setting the focus on the i-th child.
     *
     * @param i
     * @return a Visitor<X,Y> that sets the focus on the i-th child.
     */
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
