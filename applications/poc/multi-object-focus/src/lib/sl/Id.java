/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 11:53
 * To change this template use File | Settings | File Templates.
 */

package lib.sl;

import lib.*;

/** The Id Visitor. It just returns its input */
public class Id<X> extends Visitor<X,X> {
  public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X, X>,Ans> k) throws MOFException { return k.apply(Zip.unit(z.focus)); }
}
