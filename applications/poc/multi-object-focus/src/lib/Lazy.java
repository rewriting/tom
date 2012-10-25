package lib;

import lib.fun.Fun;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 19:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class Lazy<X> {
   private X value = null;

   public X get() {
       if (value == null) { value = eval(); }
       return value;
   }

   abstract public X eval();
}
