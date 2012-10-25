package term;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 24/10/12
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
import lib.MOFException;
import lib.fun.Fun;
import lib.sl.*;
import lib.zip.Zip;

public abstract class TermVisitor<Y> extends Visitor<Term,Y> {
   public          <Ans> Ans visit(Term t, Fun<Zip<Term,Y>,Ans> k) throws MOFException { return t.accept(this,k); }
   public abstract <Ans> Ans visit(L leaf, Fun<Zip<Term,Y>,Ans> k) throws MOFException;
   public abstract <Ans> Ans visit(F node, Fun<Zip<Term,Y>,Ans> k) throws MOFException;
}
