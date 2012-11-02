package lib.sl;

import lib.Fun;
import lib.MOFException;
import lib.Zip;
import tom.library.sl.Visitable;

/**
 * Created with IntelliJ IDEA.
 * User: tof
 * Date: 11/2/12
 * Time: 3:42 AM
 * To change this template use File | Settings | File Templates.
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
