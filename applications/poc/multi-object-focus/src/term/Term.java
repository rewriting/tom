/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 23/10/12
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
// Terms
package term;

import lib.MOFException;
import lib.fun.Fun;
import lib.sl.*;
import lib.zip.*;


public abstract class Term {
    public <Ans,Y> Ans accept(Visitor<Term,Y> v, Fun<Zip<Term,Y>,Ans> k) throws MOFException { return v.visit(this,k); }
}