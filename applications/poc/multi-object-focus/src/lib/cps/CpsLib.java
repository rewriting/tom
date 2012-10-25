package lib.cps;

import lib.MOFException;
import lib.fun.Fun;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
public class CpsLib {

   public static <Ans,X> Cps<Ans,X> unit(final X x) {
       return new Cps<Ans,X>() { public Ans apply(Fun<X,Ans> k) throws MOFException { return k.apply(x); } } ;
   }
}
