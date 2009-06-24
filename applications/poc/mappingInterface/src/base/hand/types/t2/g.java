package base.hand.types.t2;

import base.hand.types.T2;

/**
 * @author nvintila
 * @date 3:47:05 PM Jun 13, 2009
 */
public class g extends T2 {

    T2 s2;

    public g(T2 s2) {
        this.s2 = s2;
    }

    public T2 getS2() {
        return s2;
    }

    public void setS2(T2 s2) {
        this.s2 = s2;
    }

    public boolean equals(Object o) {
        if (o instanceof g) {
            g oo = (g) o;
            return s2.equals(oo.getS2());
        }
        return false;
    }

    @Override
    public String toString() {
        return "g(" + s2 + ")";
    }
}
