/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */

package lib.sl;

import lib.*;
import tom.library.sl.Visitable;


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
    public abstract <T> X visitZK(Zip<T,X> z, Fun<Zip<X,Y>, Zip<X,Y>> k) throws MOFException;


    /**
     * visitZ(zx) = visitZK(x , id) where id is the identity function. So it returns the zipper Zip<T,Y>.
     *
     * @param zx the input zipper.
     * @param <T>
     * @return the zipper Zip<T,Y>
     * @throws MOFException
     */
    public <T,Z> X visitZ(Zip<T,X> zx) throws MOFException {
       return visitZK(zx, new IdF<Zip<X, Y>>());
    }


    public X visitUZ(X x) throws MOFException {
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
        return visitZ(Zip.unit(x));
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
            public <T> X visitZK(final Zip<T,X> z, final Fun<Zip<X,Z>,Zip<X,Z>> k) throws MOFException {
                return            t.visitZK(z          , new Fun<Zip<X,Y>,Zip<X,Y>>() { public Zip<X,Y> apply(final Zip<X,Y> zt) throws MOFException {
                    return zt.replace(v.visitZK(z.merge(zt), new Fun<Zip<Y,Z>,Zip<Y,Z>>() { public Zip<Y,Z> apply(final Zip<Y,Z> zv) throws MOFException {
                        return zv.replace(k.apply(zt.merge(zv)).focus);
                    }})) ;}}) ;}} ;
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
            public <T> X visitZK(final Zip<T,X> z, final Fun<Zip<X,Y>,Zip<X,Y>> k) throws MOFException {
                try                    { return t.visitZK(z, k); }
                catch (MOFException e) { return v.visitZK(z, k); }
            }};
    }



    /**
     * A visitor backtracks by capturing its context as the continuation k. reset prevents this visitor from capturing
     * the context further than this point. For example: consider the visitor this.reset().seq(another_visotor), thanks
     * to reset, this can not capture antother_visitor. Se Shift/Reset in CPS for more details.
     *
     * @return The same visitor as this but which can not capture its context further.
     */
    public Visitor<X,X> reset() {
        final Visitor<X,Y> t = this;
        return new Visitor<X,X>() {
            public <T> X visitZK(final Zip<T,X> z, final Fun<Zip<X,X>,Zip<X,X>> k) throws MOFException {
               return k.apply(Zip.unit(t.visitZ(z))).run();
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
            public <T> X visitZK(final Zip<T,X> x, final Fun<Zip<X,X>,Zip<X,X>> k) throws MOFException {
                return t.visitZK(x, new Fun<Zip<X, Y>, Zip<X, Y>>() {
                    public Zip<X,Y> apply(Zip<X, Y> z) throws MOFException {
                        return (Zip<X,Y>)(k.apply(Zip.unit(z.run())));
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
            public <T> P<X,R> visitZK(final Zip<T,P<X,R>> zxr, final Fun< Zip<P<X,R>,P<Y,S>> , Zip<P<X,R>,P<Y,S>> > k) throws MOFException {

               // A reference on a zipper on R
               final Ref<Zip<T,R>> zr = new Ref<Zip<T, R>>(zxr.merge(zxr.focus.zipRight()));

               // the continuation for this
               Fun<Zip<X,Y>,Zip<X,Y>> kt = new Fun<Zip<X,Y>,Zip<X, Y>>() { public Zip<X,Y> apply(final Zip<X,Y> zt) throws MOFException {

                   // A mutable variable to hold the current Zip<X,Y>
                   final Ref<Zip<X,Y>> zxy = new Ref<Zip<X,Y>>(zt);

                   // the continuation for v
                   Fun<Zip<R,S>,Zip<R,S>> kv = new Fun<Zip<R,S>,Zip<R, S>>() { public Zip<R,S> apply(final Zip<R,S> zrs) throws MOFException {
                       final Zip<P<X,R>,P<Y,S>> res = k.apply(zxy.value.times(zrs));

                       // The new zipper Zip<X,Y>
                       zxy.set( Zip.mkZip( new Fun<Y,X>() { public X apply(Y y) throws MOFException {
                                                   return res.context.apply(P.mkP(y, res.focus.right )).left; }}
                                         , res.focus.left
                                         ));

                       // The new zipper Zip<R,S>
                       return  Zip.mkZip( new Fun<S,R>() { public R apply(S s) throws MOFException {
                                                   return res.context.apply(P.mkP(res.focus.left, s )).right; }}
                                        , res.focus.right
                                        );
                   }};

                   // We let v do its job and give back an R
                   R r = v.visitZK(zr.value, kv);

                   // We set zr ready for next iteration or return
                   zr.set(zr.value.replace(r));

                   // We return the rewritten value of zxy
                   return zxy.value;
               }};

               X x = t.visitZK(zxr.merge(zxr.focus.zipLeft()), kt);

               return P.mkP(x,zr.value.focus);
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
    public static <X> Visitor<X,X> map(final Fun<X,X> f) {
        return new Visitor<X,X>() {
            public <T> X visitZK(Zip<T,X> z, Fun<Zip<X,X>,Zip<X,X>> k) throws MOFException {
                return k.apply(Zip.unit(f.apply(z.focus))).run();
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
            public <T> X visitZK(Zip<T,X> z, Fun<Zip<X,Y>,Zip<X,Y>> k) throws MOFException {
                return k.apply(f.apply(z.focus)).run();
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
            public <T> X visitZK(Zip<T,X> z, Fun<Zip<X, Y>, Zip<X, Y>> k) throws MOFException {
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
       return new Visitor<X,X>() { public <T> X visitZK(Zip<T,X> z, Fun<Zip<X,X>,Zip<X,X>> k) throws MOFException {
         try { v.visitZK(z,k);
              throw new MOFException();
             }
         catch (MOFException e) { return k.apply(Zip.unit(z.focus)).run(); }
       }};
    }


    /**
     * Make a visitor that behave like this but traces inputs and outputs
     */
    public Visitor<X,Y> trace(final String name) {
        final Visitor<X,Y> v = this;
        return new Visitor<X,Y>() { public <T> X visitZK(final Zip<T,X> z, final Fun<Zip<X,Y>,Zip<X,Y>> k) throws MOFException {
            System.out.println("<" + name + ">");
            System.out.println("<input>" + z.toString() + "</input>");

            Fun<Zip<X,Y>,Zip<X,Y>> tracedk = new Fun<Zip<X,Y>,Zip<X,Y>>() { public Zip<X,Y> apply(Zip<X,Y> output) throws MOFException {
                System.out.println("<trying><output>" + output.toString() + "</output>");
                try                    { Zip<X,Y> res = k.apply(output);
                                         System.out.println("<continuation-success/>");
                                         System.out.println("</trying>");
                                         return res;
                                       }
                catch (MOFException e) {  System.out.println("<contiuation-failure/>");
                                          System.out.println("</trying>");
                                          throw new MOFException();
                                       }
            }};

            try                    { X ans = v.visitZK(z,tracedk);
                                     System.out.println("<visitor-success/></" + name + ">");
                                     return ans;
                                   }
            catch (MOFException e) { System.out.println("<visitor-failure/></" + name + ">");
                                     throw new MOFException();
                                   }
        }};
    }




    public static Visitor<Visitable,Visitable> child(final int i) {
        return new Visitor<Visitable, Visitable>() {
            public <T> Visitable visitZK(Zip<T,Visitable> z, Fun<Zip<Visitable,Visitable>,Zip<Visitable,Visitable>> k) throws MOFException {
                Visitable t = z.focus;
                if (i >= t.getChildCount()) throw new MOFException() ;
                else return k.apply(Zip.child(t,i)).run();
            }};
    }



    /**
     * Build a Visitor setting the 2 focuses on the i-th and j-th children.
     *
     * @param i
     * @param j
     * @return a Visitor<Visitable,Visitable> that sets the focus on the i-th child.
     */
    public static Visitor<Visitable,P<Visitable,Visitable>> child2(final int i, final int j) {
        return new Visitor<Visitable, P<Visitable, Visitable>>() {
            public <T> Visitable visitZK(Zip<T,Visitable> z, Fun<Zip<Visitable,P<Visitable,Visitable>>,Zip<Visitable,P<Visitable,Visitable>>> k) throws MOFException {
                final Visitable x = z.focus;

                if (Math.max(i,j) >= x.getChildCount() ) throw new MOFException() ;
                else return k.apply(Zip.mkZip( new Fun<P<Visitable,Visitable>,Visitable>() { public Visitable apply(P<Visitable,Visitable> p) {
                    Visitable[] childrens = x.getChildren();
                    childrens[i] = p.left;
                    childrens[j] = p.right;
                    return x.setChildren(childrens);
                }}
                        , P.mkP(x.getChildAt(i), x.getChildAt(j))
                )).run();
            }};
    }


    /**
     * A visitor that take a Visitable node as input and set the focus on a child (left to right). If the visitor
     * chain fails on a subterm, it backtraks to use the next one. If all fail it fails.
     * <p>
     * IMPORTANT: this component uses getChildAt to a get a child and cast it to Y. Y can be used in
     *  to ways:
     *  - Instantiate Y by Visitable to avoid cast errors if getChildAt can return values not of type Y.
     *  - If getChildAt returns ALWAYS a value on type Y on the input then use a more specific Y.
     *
     * @param <X> the type of the Visitable node
     */
    public static Visitor<Visitable,Visitable> forSome = new Visitor<Visitable, Visitable>() {
        public <T> Visitable visitZK(Zip<T,Visitable> z, Fun<Zip<Visitable,Visitable>,Zip<Visitable,Visitable>> k) throws MOFException {
            for (int i = 0; i < z.focus.getChildCount(); i++)
            {
                try { return child(i).visitZK(z,k); }
                catch (MOFException e) {}
            }
            throw new MOFException();
        }
    };




    /**
     * A visitor that take a Visitable node as input and set TWO focuses on two childrens (left to right). If the visitor
     * chain fails on a subterm, it backtraks to use the next one. If all fail it fails.
     * <p>
     * IMPORTANT: this component uses getChildAt to a get a child and cast it to Y. Y can be used in
     *  to ways:
     *  - Instantiate Y by Visitable to avoid cast errors if getChildAt can return values not of type Y.
     *  - If getChildAt returns ALWAYS a value on type Y on the input then use a more specific Y.
     *
     * @param <X> the type of the Visitable node
     */
    public static Visitor<Visitable,P<Visitable,Visitable>> forSome2 = new Visitor<Visitable, P<Visitable, Visitable>>() {
        public <T> Visitable visitZK(Zip<T, Visitable> z, Fun<Zip<Visitable, P<Visitable, Visitable>>, Zip<Visitable, P<Visitable, Visitable>>> k) throws MOFException {
            for (int i = 0     ; i < z.focus.getChildCount(); i++)
                for (int j = i + 1 ; j < z.focus.getChildCount(); j++)
                {
                    try { return child2(i,j).visitZK(z,k); }
                    catch (MOFException e) {}
                }
            throw new MOFException();
        }
    };




    public static Visitor<Visitable,Visitable> forAll = new Visitor<Visitable, Visitable>() {
       public <T> Visitable visitZK(final Zip<T,Visitable> z, Fun<Zip<Visitable,Visitable>,Zip<Visitable,Visitable>> k) throws MOFException {
         final Visitable   x        = z.focus;
         final Visitable[] children = x.getChildren();
         final int         length   = children.length;

         for(int i= 0; i < children.length; i++) {
             children[i] = k.apply(Zip.child(x,i)).focus;
         }

         return x.setChildren(children);
        }
    };


















    /**
     * new One<X>(s:Visitor<Y,Z>) = new SelectChild<X,Y>().seq(s).reset().up()
     *   with X,Y <= Visitable.
     * One tries to apply s to exactly one child (left to right) of its input.
     */
    public static Visitor<Visitable,Visitable> one(Visitor<Visitable,Visitable> s) {
        return forSome.seq(s).reset().up();
    }

    public static Visitor<Visitable,Visitable> onceBottomUp(final Visitor<Visitable,Visitable> s) throws MOFException {
        return fix(new Fun<Visitor<Visitable,Visitable>,Visitor<Visitable,Visitable>>(){ public Visitor<Visitable,Visitable> apply(Visitor<Visitable,Visitable> v) throws MOFException {
                return one(v).or(s);
            }});
    }


    /**
     * new Two<X>(s:Visitor<P<Y,Y>,Z>) = new SelectChild2<X,Y>().seq(s).reset().up()
     *   with X,Y <= Visitable.
     * One tries to apply s to exactly two children (left to right) of its input.
     */
    public static Visitor<Visitable,Visitable> two(Visitor<P<Visitable,Visitable>,P<Visitable,Visitable>> s) {
        return forSome2.seq(s).reset().up();
    }



    /**
     * A visitor that apply another visitor (s: Visitor<Y,Z>) to every children of a Visitable node
     * and fails it at least one does: All(s)(f(t1,..,tn)) = f(s(t1),..,s(tn))
     */
    public static Visitor<Visitable,Visitable> all(Visitor<Visitable,Visitable> s) {
        return forAll.seq(s).reset();
    }

    public static Visitor<Visitable,Visitable> bottomUp(final Visitor<Visitable,Visitable> s) throws MOFException {
            return fix(new Fun<Visitor<Visitable,Visitable>,Visitor<Visitable,Visitable>>(){ public Visitor<Visitable,Visitable> apply(Visitor<Visitable,Visitable> v) throws MOFException {
                return (all(v)).seq(s);
            }});
        }


        public static Visitor<Visitable,Visitable> topDown(final Visitor<Visitable,Visitable> s) throws MOFException {
            return fix(new Fun<Visitor<Visitable,Visitable>,Visitor<Visitable,Visitable>>(){ public Visitor<Visitable,Visitable> apply(Visitor<Visitable,Visitable> v) throws MOFException {
                return s.seq(all(v));
            }});
        }

        public static Visitor<Visitable,Visitable> innerMost(final Visitor<Visitable,Visitable> s) throws MOFException {
            return fix(new Fun<Visitor<Visitable,Visitable>,Visitor<Visitable,Visitable>>(){ public Visitor<Visitable,Visitable> apply(Visitor<Visitable,Visitable> v) throws MOFException {
                return (all(v)).seq(Visitor.sltry(s.seq(v)));
            }});
        }

}
