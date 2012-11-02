/**
 * Created with IntelliJ IDEA.
 * User: tof
 * Date: 11/2/12
 * Time: 3:42 AM
 * To change this template use File | Settings | File Templates.
 */
package lib.sl;

import lib.Fun;
import lib.MOFException;
import lib.Zip;
import tom.library.sl.Visitable;

/**
 * A visitor that take a Visitable node as input and set the focus on a child (left to right). If the visitor
 * chain fails on a subterm, it backtraks to use the next one. If all fail it fails.
 * <p>
 * IMPORTANT: this component uses getChildAt to a get a child and cast it to Y. Y can be used in
 *  to ways:
 *  - Instantiate Y by Visitable to avoid cast errors if getChildAt can return values not of type Y.
 *  - If getChildAt returns ALWAYS a value on type Y on the input then use a more specific Y.
 *
 * @param <X> the type of the Visitable node
 */
public class SelectChild<X extends Visitable, Y extends Visitable> extends Visitor<X,Y> {

    public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Ans> k) throws MOFException {
         return one_aux(z,k,0);
    }

    private <T,Ans> Ans one_aux(Zip<T,X> z, Fun<Zip<X,Y>,Ans>  k, int i) throws MOFException {
        if (i >= z.focus.getChildCount()) throw new MOFException();
        else { try { return new Child<X,Y>(i).visitZK(z,k); }
        catch (MOFException e) { return one_aux(z,k,i+1); }
        }
    }

}
