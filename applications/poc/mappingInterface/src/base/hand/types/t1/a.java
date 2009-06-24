package base.hand.types.t1;

import base.hand.types.T1;

/**
 * @author nvintila
 * @date 2:32:56 PM Jun 13, 2009
 */
public class a extends T1 {


    public boolean equals(Object o) {
        return o instanceof a;
    }

    @Override
    public String toString() {
        return "a";
    }
}
