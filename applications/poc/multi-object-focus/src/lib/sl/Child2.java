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
import lib.P;
import lib.Zip;
import tom.library.sl.Visitable;

/**
 * A visitor that take a Visitable node as input and set TWO focuses on its i-th and j-th children if they exits
 * and fail otherwise.
 * <p>
 * IMPORTANT: this component uses getChildAt to a get a child and cast it to Y. X,Y can be used in
 *  to ways:
 *  - Instantiate X by Visitable to avoid cast errors if getChildAt can return values not of Y.
 *  - If getChildAt returns ALWAYS a value on type Y on the input then use a more specific Y.
 *
 * @param <X> the type of the input Visitable node
 * @param <Y> the type of the i-th and j-th children.
 */
public class Child2 <X extends Visitable, Y extends Visitable> extends Visitor<X,P<Y,Y>> {
    /*
      visitor is the actual visitor. It is set by the constructor.
     */
    private Visitor<X,P<Y,Y>> visitor = null;


    /**
     * Build a Visitor setting the focus on the i-th child.
     *
     * @param i
     */
    public Child2(int i,int j) {
        visitor = at(i,j);
    }


    public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,P<Y,Y>>,Ans> k) throws MOFException {
        return visitor.visitZK(z,k);
    }



    /**
     * Build a Visitor setting the 2 focuses on the i-th and j-th children.
     *
     * @param i
     * @param j
     * @return a Visitor<X,Y> that sets the focus on the i-th child.
     */
    public Visitor<X,P<Y,Y>> at (final int i, final int j) {
        return new Visitor<X, P<Y, Y>>() {
            public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,P<Y,Y>>,Ans> k) throws MOFException {
                final X x = z.focus;
                if (Math.max(i,j) >= x.getChildCount() ) throw new MOFException() ;
                else return k.apply(Zip.mkZip( new Fun<P<Y,Y>,X>() { public X apply(P<Y,Y> p) {
                                                 Visitable[] childrens = x.getChildren();
                                                 childrens[i] = p.left;
                                                 childrens[j] = p.right;
                                                 return (X)(x.setChildren(childrens));
                                                }}


                         , P.mkP( (Y)(x.getChildAt(i)),
                                  (Y)(x.getChildAt(j))
                                )
                ));
            }};
    }

}
