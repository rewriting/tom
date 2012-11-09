/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */

package lib.sl;

import lib.*;

import java.util.zip.ZipOutputStream;

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
    public abstract <T,R,A> Cps<R,A> visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Cps<R,A>> k) throws MOFException;


    /**
     * visitZ(zx) = visitZK(x , id) where id is the identity function. So it returns the zipper Zip<T,Y>.
     *
     * @param zx the input zipper.
     * @param <T>
     * @return the zipper Zip<T,Y>
     * @throws MOFException
     */
    public <T> Zip<X,Y> visitZ(Zip<T,X> zx) throws MOFException {
       return Cps.run(visitZK(zx, new Return<Zip<X,Y>,Zip<X,Y>>()));
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
            public <T,R,A> Cps<R,A> visitZK(final Zip<T,X> z, final Fun<Zip<X,Y>,Cps<R,A>> k) throws MOFException {
               return new Cps<R,A>() {
                   public R apply(final Fun<A,R> mk) throws MOFException {
                       return t.visitZK(z, new Return<R, Zip<X, Y>>()).apply(new Fun<Zip<X,Y>,R>() { public R apply(Zip<X,Y> zxy) throws MOFException {
                           return k.apply(zxy).apply(mk);
                       }});
                   }};
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
            public <T,R,A> Cps<R,A> visitZK(final Zip<T,X> z, final Fun<Zip<X,Y>,Cps<R,A>> k) throws MOFException {
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
            public <T,R,A> Cps<R,A> visitZK(final Zip<T,X> x, final Fun<Zip<X,X>,Cps<R,A>> k) throws MOFException {
                return t.visitZK(x, new Fun<Zip<X, Y>, Cps<R, A>>() {
                    public Cps<R,A> apply(Zip<X, Y> z) throws MOFException {
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
            public <T,A,Z,Q> Cps<A,P<Z,Q>> visitZK(final Zip<T,P<X,R>> zxr, final Fun< Zip<P<X,R>,P<Y,S>> , Cps<A,P<Z,Q>> > k) throws MOFException {
                return new Cps<A,P<Z,Q>>() { public A apply(final Fun<P<Z,Q>,A> mk) throws MOFException {

                    // The zipper for this
                    final Zip<T,X> zt = zxr.merge(zxr.focus.zipLeft());

                    // The zipper for v
                    final Zip<T,R> zv = zxr.merge(zxr.focus.zipRight());

                    // the meta continuation for this
                    final Fun<Z,A> mkt = (Fun<Z,A>)Fun.curry(mk);

                    // the continuation for this
                    Fun<Zip<X,Y>,Cps<A,Z>> kt = new Fun<Zip<X,Y>,Cps<A,Z>>() { public Cps<A,Z> apply(final Zip<X,Y> zxy) {
                        return new Cps<A,Z>() { public A apply(final Fun<Z,A> ktmk) throws MOFException {

                            // the meta contination for v
                            final Fun<Q,A> mkv = (Fun<Q,A>)Fun.swap((Fun<Z,Fun<Q,A>>)ktmk);


                            // the continuation for v
                            Fun<Zip<R,S>,Cps<A,Q>> kv = new Fun<Zip<R,S>,Cps<A,Q>>() { public Cps<A,Q> apply(final Zip<R,S> zrs) {
                              return new Cps<A,Q>() { public A apply(final Fun<Q,A> kvmk) throws MOFException {

                                  // Meta continuation for k
                                  Fun<P<Z,Q>,A> kmk = new Fun<P<Z,Q>,A>() { public A apply(P<Z,Q> p) throws MOFException {
                                      return ((Fun<Q,Fun<Z,A>>)kvmk).apply(p.right).apply(p.left);
                                  }};

                                  return k.apply(zxy.times(zrs)).apply(kmk);
                              }};
                            }};



                            return v.visitZK(zv,kv).apply(mkv);
                        }};
                    }};

                    return t.visitZK(zt,kt).apply(mkt);
                }};
        }};
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
            public <T,R,A> Cps<R,A> visitZK(final Zip<T,X> z, final Fun<Zip<X,Z>,Cps<R,A>> k) throws MOFException {
                return t.visitZK(z           , new Fun<Zip<X,Y>,Cps<R,A>>() { public Cps<R,A> apply(final Zip<X,Y> zt) throws MOFException {
                return v.visitZK(z.merge(zt) , new Fun<Zip<Y,Z>,Cps<R,A>>() { public Cps<R,A> apply(final Zip<Y,Z> zv) throws MOFException {
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
            public <T,R,A> Cps<R,A> visitZK(Zip<T,X> z, Fun<Zip<X,X>,Cps<R,A>> k) throws MOFException {
                return k.apply(Zip.unit(f.apply(z.focus)));
            } };
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
            public <T,R,A> Cps<R,A> visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Cps<R,A>> k) throws MOFException {
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
            public <T,R,A> Cps<R,A> visitZK(Zip<T,X> z, Fun<Zip<X, Y>, Cps<R,A>> k) throws MOFException {
                return f.apply(fixpoint.value).visitZK(z,k);
            }});
        return fixpoint.value;
    }



    public static <X> Visitor<X,X> sltry(Visitor<X,X> s) throws MOFException {
        return s.or(new Id<X>());
    }


    public static <X> Visitor<X,X> repeat(final Visitor<X,X> s) throws MOFException {
        return fix(new Fun<Visitor<X,X>,Visitor<X,X>>(){ public Visitor<X,X> apply(Visitor<X,X> v) throws MOFException {
            return sltry(s.seq(v));
        }});
    }


    /**
     * Not inverses success and failure. On Failure, it is Id and on success it fails.
     *
     * @param <X> type if the input
     */
    public static <X> Visitor<X,X> not(final Visitor<X,X> v) {
       return new Visitor<X,X>() { public <T,R,A> Cps<R,A> visitZK(Zip<T,X> z, Fun<Zip<X,X>,Cps<R,A>> k) throws MOFException {
         try { v.visitZK(z,k);
              throw new MOFException();
             }
         catch (MOFException e) { return k.apply(Zip.unit(z.focus)); }
       }};
    }


    /**
     * Make a visitor that behave like this but traces inputs and outputs
     */
    public Visitor<X,Y> trace(final String name) {
        final Visitor<X,Y> v = this;
        return new Visitor<X,Y>() { public <T,R,A> Cps<R,A> visitZK(final Zip<T,X> z, final Fun<Zip<X,Y>,Cps<R,A>> k) throws MOFException {
            System.out.println("<" + name + ">");
            System.out.println("<input>" + z.toString() + "</input>");

            Fun<Zip<X,Y>,Cps<R,A>> tracedk = new Fun<Zip<X,Y>,Cps<R, A>>() { public Cps<R,A> apply(Zip<X,Y> output) throws MOFException {
                System.out.println("<trying><output>" + output.toString() + "</output>");
                try                    { Cps<R,A> res = k.apply(output);
                                         System.out.println("<continuation-success/>");
                                         System.out.println("</trying>");
                                         return res;
                                       }
                catch (MOFException e) {  System.out.println("<contiuation-failure/>");
                                          System.out.println("</trying>");
                                          throw new MOFException();
                                       }
            }};

            try                    { Cps<R,A> ans = v.visitZK(z,tracedk);
                                     System.out.println("<visitor-success/></" + name + ">");
                                     return ans;
                                   }
            catch (MOFException e) { System.out.println("<visitor-failure/></" + name + ">");
                                     throw new MOFException();
                                   }
        }};
    }


}