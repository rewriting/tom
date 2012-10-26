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
 * a visitor is able to use backtracking (thanks to CPS) and rebuild an X from an Y. Redefine the abstract method #visit
 * or use one of the static methods such as map or lift to get a concrete visitor.
 *
 * @param <X> Input  type.
 * @param <Y> Output type.
 * @return a visitor taking values of type X and returning values of type Y.
 */
public abstract class Visitor<X,Y> {

    /**
     * #visit is a CPS method from X to Zip<X,Y>. As a CPS method it takes the rest of the computation (its context) as
     * the argument #k. So #visit can easily implement some backtracking. See <b>Continuation Passing Style/b> for a
     * full description of this design pattern.
     * <p>
     * #visit returns a zipper Zip<X.Y> so a value X can be build from a value Y. This is needed to be able to apply a
     * a function on a subterm y:Y of x:X. A function on Y gives a function on X.
     *
     * @param x the input value.
     * @param k the continuation.
     * @return the final value of the computation.
     * @throws MOFException to backtrack.
     * @see Zip
     */
    public abstract <Ans> Ans visit(X x, Fun<Zip<X, Y>, Ans> k) throws MOFException;



    /**
     * Run the visitor on a value x:X and the identity continuation. It returns a zipper!
     *
     * @param x the value to give to this as input.
     * @return the resulting zipper Zip<X,Y>.
     * @see Zip
     */
    public Zip<X,Y> runZ(X x) throws MOFException {
        return visit(x, new Fun<Zip<X,Y>,Zip<X,Y>>() { public Zip<X,Y> apply(Zip<X,Y> z) { return z; }} );
    }

    /**
     * Run the visitor on a value x:X and the identity continuation. Whereas runZ, it does not return a zipper but the
     * whole value.
     *
     * @param x the value to give to this as input.
     * @return the resulting value of type X.
     */
    public X run(X x) throws MOFException {
        return runZ(x).run();
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
            public <Ans> Ans visit(final X x, final Fun<Zip<X,Y>,Ans> k) throws MOFException {
                return k.apply(t.runZ(x));
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
         public <Ans> Ans visit(final P<X,R> p, final Fun<Zip<P<X,R>,P<Y,S>>,Ans> k) throws MOFException {
             return t.visit(p.left , new Fun<Zip<X,Y>,Ans>() { public Ans apply(final Zip<X,Y> zl) throws MOFException {
             return v.visit(p.right, new Fun<Zip<R,S>,Ans>() { public Ans apply(final Zip<R,S> zr) throws MOFException {
             return k.apply(zl.times(zr)) ;}}) ;}}) ;}} ;
  }


    /**
     * Computes the sequence of this visitor and the one given as argument. The sequence take X as input, give it to
     * this to get an Y and give it to v to get a Z.
     *
     * @param v a visitor takins results of this as input.
     * @return the sequence of this and v.
     */
    public <Z> Visitor<X,Z> seq(final Visitor <Y,Z> v) {
      final Visitor<X,Y> t = this;
      return new Visitor<X,Z>() {
         public <Ans> Ans visit(final X x, final Fun<Zip<X,Z>,Ans> k) throws MOFException {
             return t.visit(x       , new Fun<Zip<X,Y>,Ans>() { public Ans apply(final Zip<X,Y> zt) throws MOFException {
             return v.visit(zt.focus, new Fun<Zip<Y,Z>,Ans>() { public Ans apply(final Zip<Y,Z> zv) throws MOFException {
             return k.apply(new Zip<X, Z>( zt.context.compose(zv.context)
                                         , zv.focus
                                         )) ; }}) ;}}) ;}} ;

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
         public <Ans> Ans visit(final X x, final Fun<Zip<X,Y>,Ans> k) throws MOFException {
           try                    { return t.visit(x , k); }
           catch (MOFException e) { return v.visit(x , k); }
      }};
  }

    /**
     * It applies this and thencompose by the zipper's context.
     *
     * @return the same visitor as this but with the focus set on the root of the term.
     */
    public Visitor<X,X> up() {
        final Visitor<X,Y> t = this;
        return new Visitor<X,X>() {
            public <Ans> Ans visit(final X x, final Fun<Zip<X,X>,Ans> k) throws MOFException {
                return t.visit(x , new Fun<Zip<X,Y>,Ans>() {
                    public Ans apply(Zip<X,Y> z) throws MOFException {
                        return k.apply(Zip.unit(z.run())); }}
                );
            }};
    }



    // Static Methods
    /**
     * map transform a function into a visitor. Very useful to define visitors without having to deal with boring
     * bookkeeping. Note that the function has to be an endomorphism (from X to X), Y has to be a subtype of X.
     *
     * @param f a function from X to Y WITH Y a subclass of X
     * @return the visitor applying f on its input.
     */
    public static <X,Y extends X> Visitor<X,Y> map(final Fun<X,Y> f) {
        return new Visitor<X,Y>() {
            public <Ans> Ans visit(X x, Fun<Zip<X,Y>,Ans> k) throws MOFException { return k.apply((Zip<X,Y>)Zip.unit(f.apply(x))); } }; // Zip.unit(Y): Zip<X,Y extends X>
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
            public <Ans> Ans visit(X x, Fun<Zip<X,Y>,Ans> k) throws MOFException {
                return k.apply(f.apply(x));
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
        final Ref<Visitor<X,Y>> fixpoint = null ;
        fixpoint.set( new Visitor<X, Y>() {
            public <Ans> Ans visit(X x, Fun<Zip<X, Y>, Ans> k) throws MOFException {
                return f.apply(fixpoint.value).visit(x,k);
            }});
        return fixpoint.value;
    }
}