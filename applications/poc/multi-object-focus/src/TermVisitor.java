import lib.MOFException;
import lib.fun.Fun;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 23/10/12
 * Time: 19:10
 * To change this template use File | Settings | File Templates.
 */
public abstract class TermVisitor<X> extends Fun<Term,X> {
    public X apply(Term t) throws MOFException { return t.accept(this); }

    public abstract X visitL(L leaf) throws MOFException ;
    public abstract X visitF(F node) throws MOFException ;
}
