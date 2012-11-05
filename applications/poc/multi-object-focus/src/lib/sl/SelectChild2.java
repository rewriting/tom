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
import lib.P;
import tom.library.sl.Visitable;

/**
 * A visitor that take a Visitable node as input and set TWO focuses on two childrens (left to right). If the visitor
 * chain fails on a subterm, it backtraks to use the next one. If all fail it fails.
 * <p>
 * IMPORTANT: this component uses getChildAt to a get a child and cast it to Y. Y can be used in
 *  to ways:
 *  - Instantiate Y by Visitable to avoid cast errors if getChildAt can return values not of type Y.
 *  - If getChildAt returns ALWAYS a value on type Y on the input then use a more specific Y.
 *
 * @param <X> the type of the Visitable node
 */
public class SelectChild2<X extends Visitable, Y extends Visitable> extends Visitor<X,P<Y,Y>> {

    public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,P<Y,Y>>,Ans> k) throws MOFException {
        for (int i = 0     ; i < z.focus.getChildCount(); i++)
        for (int j = i + 1 ; j < z.focus.getChildCount(); j++)
        {
            try { return new Child2<X,Y>(i,j).visitZK(z,k); }
            catch (MOFException e) {}
        }
        throw new MOFException();
    }
}
