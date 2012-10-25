/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
package lib.sl;

import lib.*;
import lib.MOFException;
import lib.fun.Fun;
import lib.zip.*;

public class VisitorLib {
    public static <X> Visitor<X,X> map(final Fun<X,X> f) {
       return new Visitor<X,X>() {
          public <Ans> Ans visit(X x, Fun<Zip<X,X>,Ans> k) throws MOFException { return k.apply(ZipLib.unit(f.apply(x))); } };
    }

    public static <X,Y> Visitor<X,Y> lift(final Fun<X,Zip<X,Y>> f) {
      return new Visitor<X,Y>() {
        public <Ans> Ans visit(X x, Fun<Zip<X,Y>,Ans> k) throws MOFException {
          return k.apply(f.apply(x));
        }};
    }

    public static <X,Y> Visitor<X,Y> fix(final Fun<Visitor<X,Y>,Visitor<X,Y>> f) throws MOFException {
        final Ref<Visitor<X,Y>> fixpoint = null ;
        fixpoint.set( new Visitor<X, Y>() {
                        public <Ans> Ans visit(X x, Fun<Zip<X, Y>, Ans> k) throws MOFException {
                            return f.apply(fixpoint.value).visit(x,k);
                        }});
        return fixpoint.value;
    }
}
