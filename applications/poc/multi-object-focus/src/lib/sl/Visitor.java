/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */

package lib.sl;

import lib.*;

import javax.swing.text.ZoneView;
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
public abstract class Visitor<T,X,Y,A> {

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
    public abstract <Ans> Cps<Ans,A> visitZK(Zip<T,X> z, Fun<Zip<X,Y>, Cps<Ans,A>> k) throws MOFException;


    /**
     * visitZ(zx) = visitZK(x , id) where id is the identity function. So it returns the zipper Zip<T,Y>.
     *
     * @param zx the input zipper.
     * @param <T>
     * @return the zipper Zip<T,Y>
     * @throws MOFException
     */
    public static <T,X,Y> Zip<X,Y> visitZ(Visitor<T,X,Y,Zip<X,Y>> v, Zip<T,X> zx) throws MOFException {
       return Cps.run(v.visitZK(zx, new Return<Zip<X, Y>, Zip<X, Y>>()));
    }


    public static <X,Y> Zip<X,Y> visitUZ(Visitor<X,X,Y,Zip<X,Y>> v, X x) throws MOFException {
        return visitZ(v, Zip.unit(x));
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
    public static <X,Y> X visit(Visitor<X,X,Y,Zip<X,Y>> v, X x) throws MOFException {
        return visitZ(v, Zip.unit(x)).run();
    }




    /**
     * A visitor backtracks by capturing its context as the continuation k. reset prevents this visitor from capturing
     * the context further than this point. For example: consider the visitor this.reset().seq(another_visotor), thanks
     * to reset, this can not capture antother_visitor. Se Shift/Reset in CPS for more details.
     *
     * @return The same visitor as this but which can not capture its context further.
     */
    public static <T,X,Y,A> Visitor<T,X,Y,Zip<X,Y>> reset(final Visitor<T,X,Y,Zip<X,Y>> v) {
        return new Visitor<T,X,Y,Zip<X, Y>>() {
            public <Ans> Cps<Ans,Zip<X,Y>> visitZK(final Zip<T,X> z, final Fun<Zip<X,Y>,Cps<Ans,Zip<X,Y>>> k) throws MOFException {
               return new Cps<Ans,Zip<X, Y>>() {
                   public Ans apply(final Fun<Zip<X,Y>,Ans> mk) throws MOFException {
                       return v.visitZK(z, new Return<Ans, Zip<X, Y>>()).apply(new Fun<Zip<X,Y>,Ans>() { public Ans apply(Zip<X,Y> zxy) throws MOFException {
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
    public Visitor<T,X,Y,A> or(final Visitor <T,X,Y,A> v) {
        final Visitor<T,X,Y,A> t = this;
        return new Visitor<T,X,Y,A>() {
            public <Ans> Cps<Ans,A> visitZK(final Zip<T,X> z, final Fun<Zip<X,Y>,Cps<Ans,A>> k) throws MOFException {
                try                    { return t.visitZK(z, k); }
                catch (MOFException e) { return v.visitZK(z, k); }
            }};
    }


    /**
     * It applies this and then compose by the zipper's context.
     *
     * @return the same visitor as this but with the focus set on the root of the term.
     */
    public Visitor<T,X,X,A> up() {
        final Visitor<T,X,Y,A> t = this;
        return new Visitor<T,X,X,A>() {
            public <Ans> Cps<Ans,A> visitZK(final Zip<T,X> x, final Fun<Zip<X,X>,Cps<Ans,A>> k) throws MOFException {
                return t.visitZK(x, new Fun<Zip<X, Y>, Cps<Ans, A>>() {
                    public Cps<Ans,A> apply(Zip<X, Y> z) throws MOFException {
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
    public static <T,X,R,Y,S> Visitor<T,P<X,R>,P<Y,S>,P<Zip<X,Y>,Zip<R,S>>> times (final Visitor<T,X,Y,Zip<X,Y>> t, final Visitor<T,R,S,Zip<R,S>> v) {
        return new Visitor<T,P<X,R>,P<Y,S>,P<Zip<X,Y>,Zip<R,S>>>() {
            public <Ans> Cps<Ans,P<Zip<X,Y>,Zip<R,S>>> visitZK(final Zip<T,P<X,R>> zxr, final Fun< Zip<P<X,R>,P<Y,S>> , Cps<Ans,P<Zip<X,Y>,Zip<R,S>>>> k) throws MOFException {
                return new Cps<Ans,P<Zip<X,Y>,Zip<R,S>>>() { public Ans apply(final Fun<P<Zip<X,Y>,Zip<R,S>>,Ans> mk) throws MOFException {

                    // The zipper for this
                    final Zip<T,X> zt = zxr.merge(zxr.focus.zipLeft());

                    // The zipper for v
                    final Zip<T,R> zv = zxr.merge(zxr.focus.zipRight());

                    Fun<Zip<X,Y>,Ans> mkt = new Fun<Zip<X,Y>, Ans>() { public Ans apply(final Zip<X,Y> zxy) throws MOFException {
                            return v.visitZK(zv, new Return<Ans,Zip<R,S>>()).apply( new Fun<Zip<R,S>,Ans>() { public Ans apply(Zip<R,S> zrs) throws MOFException {
                                return mk.apply(P.mkP(zxy,zrs));
                            }});
                    }};


                    // the continuation for this
                    Fun<Zip<X,Y>,Cps<Ans,Zip<X,Y>>> kt = new Fun<Zip<X,Y>,Cps<Ans,Zip<X,Y>>>() { public Cps<Ans,Zip<X,Y>> apply(final Zip<X,Y> zxy) {
                        return new Cps<Ans,Zip<X, Y>>() { public Ans apply(final Fun<Zip<X,Y>,Ans> ktmk) throws MOFException {

                            Fun<Zip<R,S>,Ans> mkv = new Fun<Zip<R,S>,Ans>() { public Ans apply(Zip<R,S> zrs) throws MOFException {
                                return ktmk.apply(zxy);

                            }};

                            Fun<Zip<R,S>,Cps<Ans,Zip<R, S>>> kv = new Fun<Zip<R, S>, Cps<Ans, Zip<R,S>>>() {
                                public Cps<Ans, Zip<R,S>> apply(final Zip<R, S> zrs) throws MOFException {
                                    return new Cps<Ans,Zip<R,S>>() { public Ans apply(final Fun<Zip<R,S>,Ans> kvmk) throws MOFException {
                                        return k.apply(zxy.times(zrs)).apply( new Fun<P<Zip<X,Y>,Zip<R,S>>,Ans>() { public Ans apply(P<Zip<X,Y>,Zip<R,S>> p) {
                                           return kvmk.apply(p.right)
                                        }});


                                    }};
                            }};



                            return v.visitZK(zv,kv).apply();
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
    public <Z> Visitor<T,X,Z,A> seq(final Visitor <T,Y,Z,A> v) {
        final Visitor<T,X,Y,A> t = this;
        return new Visitor<T,X,Z,A>() {
            public <Ans> Cps<Ans,A> visitZK(final Zip<T,X> z, final Fun<Zip<X,Z>,Cps<Ans,A>> k) throws MOFException {
                return t.visitZK(z           , new Fun<Zip<X,Y>,Cps<Ans,A>>() { public Cps<Ans,A> apply(final Zip<X,Y> zt) throws MOFException {
                return v.visitZK(z.merge(zt) , new Fun<Zip<Y,Z>,Cps<Ans,A>>() { public Cps<Ans,A> apply(final Zip<Y,Z> zv) throws MOFException {
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
    public static <T,X,A> Visitor<T,X,X,A> map(final Fun<X,X> f) {
        return new Visitor<T,X,X,A>() {
            public <Ans> Cps<Ans,A> visitZK(Zip<T,X> z, Fun<Zip<X,X>,Cps<Ans,A>> k) throws MOFException {
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
    public static <T,X,Y,A> Visitor<T,X,Y,A> lift(final Fun<X,Zip<X,Y>> f) {
        return new Visitor<T,X,Y,A>() {
            public <Ans> Cps<Ans,A> visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Cps<Ans,A>> k) throws MOFException {
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
    public static <T,X,Y,A> Visitor<T,X,Y,A> fix(final Fun<Visitor<T,X,Y,A>,Visitor<T,X,Y,A>> f) throws MOFException {
        final Ref<Visitor<T,X,Y,A>> fixpoint = new Ref<Visitor<T,X,Y,A>>(null) ;
        fixpoint.set( new Visitor<T,X,Y,A>() {
            public <Ans> Cps<Ans,A> visitZK(Zip<T,X> z, Fun<Zip<X, Y>, Cps<Ans,A>> k) throws MOFException {
                return f.apply(fixpoint.value).visitZK(z,k);
            }});
        return fixpoint.value;
    }



    public static <T,X,A> Visitor<T,X,X,A> sltry(Visitor<T,X,X,A> s) throws MOFException {
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
    public static <T,X,A> Visitor<T,X,X,A> not(final Visitor<T,X,X,A> v) {
       return new Visitor<T,X,X,A>() { public <Ans> Cps<Ans,A> visitZK(Zip<T,X> z, Fun<Zip<X,X>,Cps<Ans,A>> k) throws MOFException {
         try { v.visitZK(z,k);
              throw new MOFException();
             }
         catch (MOFException e) { return k.apply(Zip.unit(z.focus)); }
       }};
    }


    /**
     * Make a visitor that behave like this but traces inputs and outputs
     */
    public Visitor<T,X,Y,A> trace(final String name) {
        final Visitor<T,X,Y,A> v = this;
        return new Visitor<T,X,Y,A>() { public <Ans> Cps<Ans,A> visitZK(final Zip<T,X> z, final Fun<Zip<X,Y>,Cps<Ans,A>> k) throws MOFException {
            System.out.println("<" + name + ">");
            System.out.println("<input>" + z.toString() + "</input>");

            Fun<Zip<X,Y>,Cps<Ans,A>> tracedk = new Fun<Zip<X,Y>,Cps<Ans, A>>() { public Cps<Ans,A> apply(Zip<X,Y> output) throws MOFException {
                System.out.println("<trying><output>" + output.toString() + "</output>");
                try                    { Cps<Ans,A> res = k.apply(output);
                                         System.out.println("<continuation-success/>");
                                         System.out.println("</trying>");
                                         return res;
                                       }
                catch (MOFException e) {  System.out.println("<contiuation-failure/>");
                                          System.out.println("</trying>");
                                          throw new MOFException();
                                       }
            }};

            try                    { Cps<Ans,A> ans = v.visitZK(z,tracedk);
                                     System.out.println("<visitor-success/></" + name + ">");
                                     return ans;
                                   }
            catch (MOFException e) { System.out.println("<visitor-failure/></" + name + ">");
                                     throw new MOFException();
                                   }
        }};
    }


}