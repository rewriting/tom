import lib.MOFException;
import lib.fun.Fun;
import lib.fun.FunCPS;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 24/10/12
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
public abstract class TermVisitorCPS<Ans,Y> implements FunCPS<Ans,Term,Y> {
   public Ans applyCPS(Term t, Fun<Y,Ans> k) throws MOFException { return t.acceptCPS(this, k); }

   public abstract Ans visitL_CPS(L leaf, Fun<Y,Ans> k) throws MOFException;
   public abstract Ans visitF_CPS(F node, Fun<Y,Ans> k) throws MOFException;
}
