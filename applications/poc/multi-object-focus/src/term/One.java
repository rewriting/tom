package term;

import lib.MOFException;
import lib.fun.Fun;
import lib.zip.*;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 24/10/12
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class One extends TermVisitor<Term> {

    public <Ans> Ans visit(L l, Fun<Zip<Term, Term>,Ans> k) throws MOFException { throw new MOFException(); }

    public <Ans> Ans visit(final F f, Fun<Zip<Term, Term>,Ans> k) throws MOFException {
        try                    { return k.apply(new Zip<Term, Term> ( new Fun<Term, Term>(){
                                                                            public Term apply(Term l){
                                                                                return new F(f.fsym, l, f.right ) ;
                                                                            } }
                                                                   , f.left
                                                                   ));
                               }
        catch (MOFException e) { return k.apply(new Zip<Term, Term> ( new Fun<Term, Term>(){
                                                                            public Term apply(Term r){
                                                                                return new F(f.fsym, f.left, r ) ;
                                                                            } }
                                                                    , f.right
                                                                    ));
                               }
    }
}
