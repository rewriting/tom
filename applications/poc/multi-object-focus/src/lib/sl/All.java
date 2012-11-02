/**
 * Created with IntelliJ IDEA.
 * User: tof
 * Date: 11/2/12
 * Time: 4:16 AM
 * To change this template use File | Settings | File Templates.
 */
package lib.sl;

import lib.Fun;
import lib.MOFException;
import lib.Zip;
import tom.library.sl.Visitable;


/**
 * A visitor that apply another visitor (s: Visitor<Y,Z>) to every children of a Visitable node
 * and fails it at least one does: All(s)(f(t1,..,tn)) = f(s(t1),..,s(tn))
 * <p>
 * IMPORTANT: this component uses getChildAt to a get a child and cast it to Y. X can be used in
 *  to ways:
 *  - Instantiate X by Visitable to avoid cast errors if getChildAt can return values not of type X.
 *  - If getChildAt returns ALWAYS a value on type X on the input then use a more specific X.
 *
 * @param <X> the type of the Visitable node
 */
public class All<X extends Visitable> extends Visitor<X,X> {
    /*
      visitor is the actual visitor. It is set by the constructor.
     */
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


    /*
      This is the actual recursive function for All. It applies s on every child of the input by:

        let Vi = tChild(i).seq(s).up() the visitor that focus on a child of its input, apply s and rebuild the
         parent.

        z0 = input zipper
        z1 = V0(z0)
        z2 = V1(z1)
        ...
        zn = output zipper

        Then zn is given to the continuation.
     */
    private <Y extends Visitable, Z, T, Ans> Ans all_aux(Visitor<Y,Z> s, Fun<Zip<X,X>,Ans>  k, Zip<T,X> z, int i) throws MOFException {
        X t = z.focus;
        if (i >= t.getChildCount()) { return k.apply(Zip.unit(t)); }
        else { return all_aux(s,k, (new Child<X,Y>(i)).seq(s).up().visitZ(z),i+1); }
    }




    public static <X extends Visitable> Visitor<X,X> bottomUp(final Visitor<X,X> s) throws MOFException {
        return fix(new Fun<Visitor<X,X>,Visitor<X,X>>(){ public Visitor<X,X> apply(Visitor<X,X> v) throws MOFException {
          return (new All<X>(v)).seq(s);
        }});
    }


    public static <X extends Visitable> Visitor<X,X> topDown(final Visitor<X,X> s) throws MOFException {
        return fix(new Fun<Visitor<X,X>,Visitor<X,X>>(){ public Visitor<X,X> apply(Visitor<X,X> v) throws MOFException {
            return s.seq(new All<X>(v));
        }});
    }

    public static <X extends Visitable> Visitor<X,X> innerMost(final Visitor<X,X> s) throws MOFException {
        return fix(new Fun<Visitor<X,X>,Visitor<X,X>>(){ public Visitor<X,X> apply(Visitor<X,X> v) throws MOFException {
            return (new All<X>(v)).seq(Visitor.sltry(s.seq(v)));
        }});
    }

}