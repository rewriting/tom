/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 24/10/12
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */

package lib;

import tom.library.sl.Visitable;
import tom.library.sl.VisitFailure;


/**
 * Class Zip implements a generic zipper. Let t be a term, you can think of a zipper on t as a pointer on a subterm
 * of t. A zipper is made of two things: a subterm u and the context of u (think of it as a function that when given u
 * returns t). Concretely a zipper is a pair of a term of type Y called the focus and a function from Y to X called the
 * context.
 * <p>
 * This implementations generalizes zipper to multi-focus (multiple pointers). Let p1..pn, n pointers to subterms of t
 * and t1..tn the corresponding subterms. The context is a multi-hole context that given the n subterms t1..tn, rebuild
 * t. So that the same as having a n-tupple (t1,..,tn) as the (single) focus and a function fron n-tupples to t. This
 * generalised to having a value of type Y as focus and a function from Y to X as context.
 * <p>
 * The focus is essentially a position but by abuse we may also call the focus, the subtern on which the focus is.
 *
 * @param <T> type of the context                    (codomaion of the context function).
 * @param <S> type of the term which has the focus   (domain    of the context function).
 * @return a zipper on T and S.
 */
public class Zip<T,S> {
    /**
     * The context function comtext: S->T
     */
    public final Fun<S,T> context;
    public final Env      env;

    /**
     * The term at the focus
     */
    public S        focus;

    /*
      cached = context(focus)
     */
    private T cached = null;

    /**
     * Make a zipper with context c and focus f.
     *
     * @param c the context function.
     * @param f the term at the focus.
     */
    public Zip(Fun<S, T> c, S f) {
        context = c;
        focus   = f;
        env     = new Env();
    }

    /**
     * Gives the whole term, i.e. the context applied to the term at the focus

     * @return the whole term.
     * @throws VisitFailure
     */
    public T run() throws VisitFailure {
        if (cached == null) cached = context.apply(focus);
        return cached;
    }

    /**
     * Run the zipper with a different subterm than the focus.
     *
     * @param s the subterm to use instead of the focus.
     * @throws VisitFailure
     */
    public T runWith(S s) throws VisitFailure { return context.apply(s); }


    /**
     * Replace the term at focus by another one. this is NOT modified, instead a new zipper is created!
     *
     * @param s the new subterm.
     * @return a new zipper with same context as this but with s as subterm.
     */
    public Zip<T,S> replace(S s) { return new Zip<T,S>(context,s); }


    /**
     * Set the focus as a zipper.
     *
     */
    public Zip<T,Zip<S,S>> extend() {
        final Zip<T,S> t = this;
        return mkZip( new Fun<Zip<S,S>,T>() { public T apply(Zip<S,S> z) throws VisitFailure { return t.runWith(z.run()); }}
                    , unit(focus)
                    );
    }


    /**
     * Move the focus. Given a function that when applied to the subterm u returns a zipper on u (which means that the
     * fonction f set a pointer on subterm w of u), it moves the focus of this from u to w. this is not modified,
     * instead a new zipper is created.
     *
     * @param f a function that given the subterm/focus of this, gives a zipper on it.
     * @return a new zipper with its focus moved.
     */
    public <R> Zip<T,R> refocus(Fun<S, Zip<S,R>> f) throws VisitFailure {
        final Zip<T,S> thiszip = this;
        final Zip<S,R> zf      = f.apply(thiszip.focus);
        return new Zip<T, R>( this.context.compose(zf.context)
                            , zf.focus
                            ) ;
    }



    public <R> Zip<T,R> merge(Zip<S,R> z) {
        return new Zip<T,R>( this.context.compose(z.context) , z.focus);
    }



    /**
     * The product of zippers. Given two zippers zxy:Zip<X,Y> and zab<A,B> (which means to terms X and A with two focus
     * Y and B), it returns a zipper z = zxy * zab : Zip< X * A , Y * B > (which means a zipper on the pair of
     * X and A (X*A) and the focus is the pair of focuses Y and B (Y*B) ).
     *
     * @param zab right member of the product.
     * @return the zipper this * zab.
     */
    public <A,B> Zip<P<T,A>,P<S,B>> times(Zip<A,B> zab) {
        return new Zip<P<T,A>,P<S,B>>( this.context.times(zab.context) , new P<S, B>(this.focus,zab.focus) ) ;
    }

// Static Methods

    /**
     * mkZip is the same as the constructor Zip. The goal is to avoid having to write type parameters.
     */
    public static <T,S> Zip<T,S> mkZip(Fun<S,T> c, S f) { return new Zip<T,S>(c,f); }

    /**
     * Given a term s:S it returns the zipper on s with the focus placed at its root. More concretely, a zipper
     * with an empty context (the identity function) and s as focus.
     *
     * @param <T> type of t.
     * @param t the focus.
     * @return a zipper with focus s and empty context.
     */
    public static <T> Zip<T,T> unit(T t) {
        return new Zip<T,T>( new Fun<T, T>() {
                               public T apply(T arg) throws VisitFailure { return arg; }
                             }
                           , t
                           );

    }

    /**
     * Kind of monadic join for zippers. Note that a join would have type Zip<T,Zip<T,R>> -> Zip<T,R> but we can only
     * get Zip<T,Zip<S,R>> -> Zip<T,R>
     */
    public static <T,S,R> Zip<T,R> join(final Zip<T,Zip<S,R>> z) {
        final Zip<S,R> zs = z.focus;
        return new Zip<T, R>( new Fun<R,T>() { public T apply(R r) throws VisitFailure { return z.context.apply(zs.replace(r)); } }
                , zs.focus
        ) ;
    }

    /**
     * lift f = unit o f
     */
    public static <X,Y> Fun<X,Zip<Y,Y>> lift(final Fun<X,Y> f) {
        return new Fun<X,Zip<Y,Y>>() {
            public Zip<Y,Y> apply(X x) throws VisitFailure { return unit(f.apply(x));}
        };
    }




    public String toString()  {
        String whole;
        try                    { whole = this.run().toString()    ; }
        catch (VisitFailure e) { whole = "Exception: VisitFailure"; }

        return "<Zip>\n<focus>" + focus.toString() +"</focus>\n<whole>" + whole + "</whole>\n</Zip>";

    }


    /**
     * From a Visitable term t, give the zipper whose focus is in the i-th child.
     *
     * @param t whole term
     * @param i the child at which the focus must be put on.
     * @return  the zipper.
     * @throws VisitFailure
     */
    public static Zip<Visitable,Visitable> child(final Visitable t, final int i) throws VisitFailure {
        final Visitable u = /*ConsWrapper.mk*/ t;
        if (i >= u.getChildCount()) throw new VisitFailure() ;
        else return mkZip( new Fun<Visitable,Visitable>() { public Visitable apply(Visitable y) {
                                   return u.setChildAt(i,y); }}
                         , u.getChildAt(i)
                         );
    }
}
