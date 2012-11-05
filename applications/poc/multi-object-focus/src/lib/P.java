package lib;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 23/10/12
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */

import tom.library.sl.Visitable;

// Pair Constructor: (X,Y) lib.P(X, Y)
public class P<X,Y> implements Visitable {
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


    public Visitable[] getChildren() {
        Visitable[] arr  = new Visitable[2];
        arr[0] = (Visitable)left ;
        arr[1] = (Visitable)right;
        return arr;
    }

    public P<X,Y> setChildAt(int i, Visitable v) {
        if (i == 0) return mkP((X)v, right);
        else        return mkP(left, (Y)v);
    }

    public P<X,Y> setChildren(Visitable[] c) {
        return mkP((X)(c[0]), (Y)(c[1]));
    }

    public Visitable getChildAt(int i) {
        if (i == 0) return (Visitable)left;
        else        return (Visitable)right;
    }

    public int getChildCount() {
        return 2;
    }
}