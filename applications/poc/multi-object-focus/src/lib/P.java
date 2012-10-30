package lib;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 23/10/12
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */
// Pair Constructor: (X,Y) lib.P(X, Y)
public class P<X,Y> {
    public X left ;
    public Y right;

    public P(X l, Y r) {
        left  = l;
        right = r;
    }

    public String toString() {
        return "lib.P(" + left.toString() + " , " + right.toString() + ")";
    }

    public boolean equals(Object o) {
        if(o instanceof P) {
            P n = (P) o;
            return left.equals(n.left) && right.equals(n.right);
        }
        return false;
    }

    public static <X,Y> P<X,Y> mkP(X l,Y r) { return new P<X,Y>(l,r); }
}