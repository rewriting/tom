import lib.MOFException;
import lib.fun.Fun;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 24/10/12
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class OneCPS<Ans> extends TermVisitorCPS<Ans,Term>  {

    public Ans visitL_CPS(L l, Fun<Term,Ans> k) throws MOFException { throw new MOFException(); }

    public Ans visitF_CPS(F f, Fun<Term,Ans> k) throws MOFException {
        try { return k.apply(f.left ); } catch (MOFException e) { return k.apply(f.right); }
    }
}
