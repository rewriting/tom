package term;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 26/10/12
 * Time: 10:12
 * To change this template use File | Settings | File Templates.
 */
import lib.*;
import lib.sl.*;

public class TSL {
    %include{Term.tom}

    public static final Visitor<Term,Term> l = Visitor.map( new Fun<Term,Term>(){
        public Term apply(Term t) throws MOFException {
            %match(t) {
                L(_) -> { return t; }
                _    -> { throw new MOFException() ; }
            }
            return null;
        }
    });

    public static final Visitor<Term,Term> f1 = Visitor.lift( new Fun<Term,Zip<Term,Term>>(){
        public Zip<Term,Term> apply(final Term t) throws MOFException {
            %match(t) {
                F(s,u,v) -> { return new Zip<Term,Term>( new Fun<Term,Term>() { public Term apply(Term w) { return `F(s,w,v); } }
                                                       , `u
                                                       ) ;
                            }
                _        -> { throw new MOFException() ; }
            }
            return null;
        }
    });

    public static final Visitor<Term,Term> f2 = Visitor.lift( new Fun<Term,Zip<Term,Term>>(){
        public Zip<Term,Term> apply(final Term t) throws MOFException {
            %match(t) {
                F(s,u,v) -> { return new Zip<Term,Term>( new Fun<Term,Term>() { public Term apply(Term w) { return `F(s,u,w); } }
                                                       , `v
                                                       ) ;
                            }
                _        -> { throw new MOFException() ; }
            }
            return null;
        }
    });


    public static final Visitor<Term,Term> one = f1.or(f2) ;


    public static final Visitor<Term,P<Term,Term>> two = Visitor.lift( new Fun<Term,Zip<Term,P<Term,Term>>>(){
        public Zip<Term,P<Term,Term>> apply(final Term t) throws MOFException {
            %match(t) {
                F(s,u,v) -> { return new Zip<Term,P<Term, Term>>( new Fun<P<Term,Term>,Term>() { public Term apply(P<Term,Term> p) { return `F(s,p.left,p.right); } }
                                                                , new P(`u,`v)
                                                                ) ;
                            }
                _        -> { throw new MOFException() ; }
            }
            return null;
        }
    });



}
