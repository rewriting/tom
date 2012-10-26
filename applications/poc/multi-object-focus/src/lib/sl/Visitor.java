/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */

package lib.sl;

import lib.*;

public abstract class Visitor<X,Y> {
  public abstract <Ans> Ans visit(X x, Fun<Zip<X, Y>, Ans> k) throws MOFException;

  public <R,S> Visitor<P<X,R>,P<Y,S>> times (final Visitor<R,S> v) {
     final Visitor<X,Y> t = this;
     return new Visitor<P<X,R>,P<Y,S>>() {
         public <Ans> Ans visit(final P<X,R> p, final Fun<Zip<P<X,R>,P<Y,S>>,Ans> k) throws MOFException {
             return t.visit(p.left , new Fun<Zip<X,Y>,Ans>() { public Ans apply(final Zip<X,Y> zl) throws MOFException {
             return v.visit(p.right, new Fun<Zip<R,S>,Ans>() { public Ans apply(final Zip<R,S> zr) throws MOFException {
             return k.apply(zl.times(zr)) ;}}) ;}}) ;}} ;
  }

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

  public Visitor<X,Y> or(final Visitor <X,Y> v) {
      final Visitor<X,Y> t = this;
      return new Visitor<X,Y>() {
         public <Ans> Ans visit(final X x, final Fun<Zip<X,Y>,Ans> k) throws MOFException {
           try                    { return t.visit(x , k); }
           catch (MOFException e) { return v.visit(x , k); }
      }};
  }

  public Zip<X,Y> run(X x) throws MOFException {
    return visit(x, new Fun<Zip<X,Y>,Zip<X,Y>>() { public Zip<X,Y> apply(Zip<X,Y> z) { return z; }} );
  }

  public Visitor<X,Y> reset() {
    final Visitor<X,Y> t = this;
    return new Visitor<X,Y>() {
      public <Ans> Ans visit(final X x, final Fun<Zip<X,Y>,Ans> k) throws MOFException {
        return k.apply(t.run(x));
    }};
  }

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
public static <X,Y extends X> Visitor<X,Y> map(final Fun<X,Y> f) {
    return new Visitor<X,Y>() {
        public <Ans> Ans visit(X x, Fun<Zip<X,Y>,Ans> k) throws MOFException { return k.apply((Zip<X,Y>)Zip.unit(f.apply(x))); } }; // Zip.unit(Y): Zip<X,Y extends X>
}

    public static <X,Y> Visitor<X,Y> lift(final Fun<X,Zip<X,Y>> f) {
        return new Visitor<X,Y>() {
            public <Ans> Ans visit(X x, Fun<Zip<X,Y>,Ans> k) throws MOFException {
                return k.apply(f.apply(x));
            }};
    }

    public static <X,Y> Visitor<X,Y> fix(final Fun<Visitor<X,Y>,Visitor<X,Y>> f) throws MOFException {
        final Ref<Visitor<X,Y>> fixpoint = null ;
        fixpoint.set( new Visitor<X, Y>() {
            public <Ans> Ans visit(X x, Fun<Zip<X, Y>, Ans> k) throws MOFException {
                return f.apply(fixpoint.value).visit(x,k);
            }});
        return fixpoint.value;
    }
}