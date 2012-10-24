import lib.MOFException;
import lib.fun.Fun;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 23/10/12
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
// Terms
public abstract class Term {
    public abstract <X>     X   accept   (TermVisitor<X>        v)                    throws MOFException;
    public abstract <Ans,Y> Ans acceptCPS(TermVisitorCPS<Ans,Y> v, Fun<Y,Ans> k) throws MOFException;
}