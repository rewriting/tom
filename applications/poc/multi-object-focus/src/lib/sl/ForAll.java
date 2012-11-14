package lib.sl;

import lib.*;
import org.omg.CORBA.AnySeqHolder;
import tom.library.sl.Visitable;

import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 08/11/12
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */

public class ForAll<T> extends Visitor<T,Visitable,Visitable,Zip<Visitable,Visitable>> {
    public <Ans> Cps<Ans,Zip<Visitable,Visitable>> visitZK(final Zip<T,Visitable> z, final Fun<Zip<Visitable,Visitable>,Cps<Ans,Zip<Visitable,Visitable>>> k)  {
        final Visitable   x        = z.focus;
        final Visitable[] children = x.getChildren();
        final int         length   = children.length;

        return new Cps<Ans,Zip<Visitable,Visitable>>() {
            public Ans apply(final Fun<Zip<Visitable,Visitable>,Ans> mk) throws MOFException {

                Fun<Integer,Ans> iter = new Fun<Integer,Ans>() {
                    public Ans apply(final Integer pos) throws MOFException {
                        final Fun<Integer,Ans> me = this;
                        if (pos < length) {

                            Fun<Zip<Visitable,Visitable>,Ans> kmk = new Fun<Zip<Visitable,Visitable>,Ans>() {
                                public Ans apply(Zip<Visitable,Visitable> y) throws MOFException {
                                      children[pos] = y.run();
                                      return me.apply(pos + 1);
                            }};

                            return k.apply(Zip.child(x,pos)).apply(kmk);
                        }
                        else return mk.apply(Zip.unit(x.setChildren(children)));
                    }
                };

                return iter.apply(0);
            }};
    }
}
