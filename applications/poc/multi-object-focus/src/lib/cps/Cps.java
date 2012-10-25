package lib.cps;

import lib.MOFException;
import lib.fun.Fun;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 17:09
 * To change this template use File | Settings | File Templates.
 */
public abstract class Cps<Ans,X> extends Fun<Fun<X,Ans>,Ans> {
  public <Y> Cps<Ans,Y> bind(final Fun<X,Cps<Ans,Y>> f) {
     final Cps<Ans,X> t = this;
     return         new Cps<Ans,Y>() { public Ans apply(final Fun<Y,Ans> k) throws MOFException {
     return t.apply(new Fun<X,Ans>() { public Ans apply(      X          x) throws MOFException {
     return f.apply(x).apply(k) ;}});}};
  }
}
