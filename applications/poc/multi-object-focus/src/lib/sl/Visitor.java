/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */

package lib.sl;

import lib.*;

/**
 * Abstract class of strategies. A Visitor<X,Y> is essentially a function from X to a Y. Whereas functions Fun<X,Y>,
 * a visitor is able to use backtracking (thanks to CPS) and rebuild an X from an Y. Redefine the abstract method
 * #visitZK or use one of the static methods such as map or lift to get a concrete visitor.
 *
 * @param <X> Input  type.
 * @param <Y> Output type.
 * @return a visitor taking values of type X and returning values of type Y.
 */
public abstract class Visitor<X,Y> {

    /**
     * #visitZK is a CPS method taking a (sub)term and its context as argument (Zip<T,X>) and returning another (sub)term
     * and its context (Zip<T,Y>). As a CPS method it takes the rest of the computation (its own evaluation context) as
     * the argument #k. So #visit can easily implement some backtracking. See <b>Continuation Passing Style</b> for a
     * full description of this design pattern.
     *
     * @param z the input zipper.
     * @param k the continuation.
     * @return the final value of the computation.
     * @throws MOFException to backtrack.
     * @see Zip
     */
    public abstract <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Ans> k) throws MOFException;


    /**
     * visitZ(zx) = visitZK(x , id) where id is the identity function. So it returns the zipper Zip<T,Y>.
     *
     * @param zx the input zipper.
     * @param <T>
     * @return the zipper Zip<T,Y>
     * @throws MOFException
     */
    public <T> Zip<X,Y> visitZ(Zip<T,X> zx) throws MOFException {
       return visitZK(zx, new Fun<Zip<X,Y>,Zip<X,Y>>() { public Zip<X,Y> apply(Zip<X,Y> zy) { return zy; }} );
    }


    public Zip<X,Y> visitUZ(X x) throws MOFException {
        return visitZ(Zip.unit(x));
    }

    /**
     * #visit(x) run the visitor on x (the focus is placed at the root of x) and return the resulting term.
     * <p>
     * visit(x) = visitZ(Zip.unit(x)).run()
     *
     * @param x the input term.
     * @return the final term.
     * @throws MOFException
     */
    public X visit(X x) throws MOFException {
        return visitZ(Zip.unit(x)).run();
    }


    /**
     * A visitor backtracks by capturing its context as the continuation k. reset prevents this visitor from capturing
     * the context further than this point. For example: consider the visitor this.reset().seq(another_visotor), thanks
     * to reset, this can not capture antother_visitor. Se Shift/Reset in CPS for more details.
     *
     * @return The same visitor as this but which can not capture its context further.
     */
    public Visitor<X,Y> reset() {
        final Visitor<X,Y> t = this;
        return new Visitor<X,Y>() {
            public <T,Ans> Ans visitZK(final Zip<T,X> z, final Fun<Zip<X,Y>,Ans> k) throws MOFException {
                return k.apply(t.visitZ(z));
            }};
    }


    /**
     * Computes the choice of this visitor and the one given as argument. It first try to apply this on X, if this
     * throws a MOFException then it tries to apply v on X.
     *
     * @param v a visitor applied on X if this fails.
     * @return the choice of this and v.
     */
    public Visitor<X,Y> or(final Visitor <X,Y> v) {
        final Visitor<X,Y> t = this;
        return new Visitor<X,Y>() {
            public <T,Ans> Ans visitZK(final Zip<T,X> z, final Fun<Zip<X,Y>,Ans> k) throws MOFException {
                try                    { return t.visitZK(z, k); }
                catch (MOFException e) { return v.visitZK(z, k); }
            }};
    }


    /**
     * It applies this and then compose by the zipper's context.
     *
     * @return the same visitor as this but with the focus set on the root of the term.
     */
    public Visitor<X,X> up() {
        final Visitor<X,Y> t = this;
        return new Visitor<X,X>() {
            public <T,Ans> Ans visitZK(final Zip<T,X> x, final Fun<Zip<X,X>,Ans> k) throws MOFException {
                return t.visitZK(x, new Fun<Zip<X, Y>, Ans>() {
                    public Ans apply(Zip<X, Y> z) throws MOFException {
                        return k.apply(Zip.unit(z.run()));
                    }
                }
                );
            }};
    }

    /**
     * Computes the product of this visitor and the one given as argument. The product of a Visitor<X,Y> and a
     * Visitor<A,B> is a Visitor<X * A , Y * B> where X * A is the cartesian product of X and A (i.e. pairs (x,a)
     * for x:X and a:A).
     *
     * @param v the second visitor of the product.
     * @return the product of this visitor and v.
     * @see P
     */
    public <R,S> Visitor<P<X,R>,P<Y,S>> times (final Visitor<R,S> v) {
        final Visitor<X,Y> t = this;
        return new Visitor<P<X,R>,P<Y,S>>() {
            public <T,Ans> Ans visitZK(final Zip<T,P<X,R>> z, final Fun<Zip<P<X,R>,P<Y,S>>,Ans> k) throws MOFException {
                return t.visitZK( z.refocus( new Fun<P<X,R>,Zip<P<X,R>,X>>() { public Zip<P<X,R>,X> apply(final P<X,R> p) {
                                               return Zip.mkZip( new Fun<X,P<X,R>> () { public P<X,R> apply(X x) {
                                                                     return P.mkP(x, p.right) ; } }
                                                               , p.left
                                                               ); }} )
                                , new Fun<Zip<X,Y>,Ans>() { public Ans apply(final Zip<X,Y> zl) throws MOFException {
                return v.visitZK( z.refocus( new Fun<P<X,R>,Zip<P<X,R>,R>>() { public Zip<P<X,R>,R> apply(final P<X,R> p) {
                                               return Zip.mkZip( new Fun<R,P<X,R>> () { public P<X,R> apply(R r) {
                                                                      return P.mkP(p.left , r) ; } }
                                                               , p.right
                                                               ); }} )
                                , new Fun<Zip<R,S>,Ans>() { public Ans apply(final Zip<R,S> zr) throws MOFException {
                return k.apply(zl.times(zr)) ;}}) ;}}) ;}} ;
    }


    /**
     * Computes the sequence of this visitor and the one given as argument. The output of this (Zip<X,Y>) is given
     * directly to v as input.
     *
     * @param v a visitor takins results of this as input.
     * @return the sequence of this and v.
     */
    public <Z> Visitor<X,Z> seqS(final Visitor <Y,Z> v) {
        final Visitor<X,Y> t = this;
        return new Visitor<X,Z>() {
            public <T,Ans> Ans visitZK(final Zip<T,X> z, final Fun<Zip<X,Z>,Ans> k) throws MOFException {
                return t.visitZK(z  , new Fun<Zip<X,Y>,Ans>() { public Ans apply(final Zip<X,Y> zt) throws MOFException {
                return v.visitZK(zt , new Fun<Zip<Y,Z>,Ans>() { public Ans apply(final Zip<Y,Z> zv) throws MOFException {
                return k.apply(zt.merge(zv)); }}) ;}}) ;}} ;

    }


    /**
     * Computes the sequence of this visitor and the one given as argument. The output of this (Zip<X,Y>) is merged with
     * this' input (Zip<T,X>). The result (Zip<T,Y>) is then given to v.
     *
     * @param v a visitor takins results of this as input.
     * @return the sequence of this and v.
     */
    public <Z> Visitor<X,Z> seq(final Visitor <Y,Z> v) {
        final Visitor<X,Y> t = this;
        return new Visitor<X,Z>() {
            public <T,Ans> Ans visitZK(final Zip<T,X> z, final Fun<Zip<X,Z>,Ans> k) throws MOFException {
                return t.visitZK(z           , new Fun<Zip<X,Y>,Ans>() { public Ans apply(final Zip<X,Y> zt) throws MOFException {
                return v.visitZK(z.merge(zt) , new Fun<Zip<Y,Z>,Ans>() { public Ans apply(final Zip<Y,Z> zv) throws MOFException {
                return k.apply(zt.merge(zv)); }}) ;}}) ;}} ;

    }



    // Static Methods
    /**
     * map transform a function into a visitor. Very useful to define visitors without having to deal with boring
     * bookkeeping. Note that the function has to be an endomorphism (from X to X), Y has to be a subtype of X.
     *
     * @param f a function from X to Y WITH Y a subclass of X
     * @return the visitor applying f on its input.
     */
    public static <X> Visitor<X,X> map(final Fun<X,X> f) {
        return new Visitor<X,X>() {
            public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,X>,Ans> k) throws MOFException { return k.apply(Zip.unit(f.apply(z.focus))); } };
    }


    // Static Methods
    /**
     * map transform a function into a visitor. Wheras map, Y does not have to be a subtype of X BUT the function
     * must return a zipper Zip<X,Y>, and not just Y.
     *
     * @param f a function from X to Zip<X,Y>
     * @return the visitor applying f on its input.
     */
    public static <X,Y> Visitor<X,Y> lift(final Fun<X,Zip<X,Y>> f) {
        return new Visitor<X,Y>() {
            public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Ans> k) throws MOFException {
                return k.apply(f.apply(z.focus));
            }};
    }


    // Static Methods
    /**
     * fix compute the fixed point of a function from visitors to visitors. It is useful to define recursive visitors.
     *
     * @param f a function from visitors to visitors.
     * @return the fixed point of f.
     */
    public static <X,Y> Visitor<X,Y> fix(final Fun<Visitor<X,Y>,Visitor<X,Y>> f) throws MOFException {
        final Ref<Visitor<X,Y>> fixpoint = new Ref<Visitor<X, Y>>(null) ;
        fixpoint.set( new Visitor<X, Y>() {
            public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X, Y>, Ans> k) throws MOFException {
                return f.apply(fixpoint.value).visitZK(z,k);
            }});
        return fixpoint.value;
    }



    public static <X> Visitor<X,X> sltry(Visitor<X,X> s) throws MOFException {
        return s.or(new Id<X>());
    }


    public static <X> Visitor<X,X> repeat(Visitor<X,X> s) throws MOFException {
        return fix(new Fun<Visitor<X,X>,Visitor<X,X>>(){ public Visitor<X,X> apply(Visitor<X,X> v) throws MOFException {
            return v.seq(v).or(new Id<X>());
        }});
    }


    /**
     * Not inverses success and failure. On Failure, it is Id and on success it fails.
     *
     * @param <X> type if the input
     */
    public static <X> Visitor<X,X> not(final Visitor<X,X> v) {
       return new Visitor<X,X>() { public <T,Ans> Ans visitZK(Zip<T,X> z, Fun<Zip<X,X>,Ans> k) throws MOFException {
         try { v.visitZK(z,k);
              throw new MOFException();
             }
         catch (MOFException e) { return k.apply(Zip.unit(z.focus)); }
       }};
    }

}