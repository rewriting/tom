/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 11:53
 * To change this template use File | Settings | File Templates.
 */

package lib.sl;

import lib.*;

public class Id<X> extends Visitor<X,X> {
  public <Ans> Ans visit(X x, Fun<Zip<X, X>,Ans> k) throws MOFException { return k.apply(Zip.unit(x)); }
}
