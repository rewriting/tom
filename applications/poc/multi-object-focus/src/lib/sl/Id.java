/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 11:53
 * To change this template use File | Settings | File Templates.
 */

package lib.sl;

import lib.*;
import tom.library.sl.VisitFailure;

/** The Id Visitor. It just returns its input */
public class Id<X> extends Visitor<X,X> {
  public <T> X visitZK(Zip<T,X> z, Fun<Zip<X, X>,Zip<X, X>> k) throws VisitFailure { return k.apply(Zip.unit(z.focus)).run(); }
}
