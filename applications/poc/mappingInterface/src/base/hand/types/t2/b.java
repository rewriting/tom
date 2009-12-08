package base.hand.types.t2;

import base.hand.types.T2;

/**
 * @author nvintila
 * @date 3:46:47 PM Jun 13, 2009
 */
public class b extends T2 {

    public boolean equals(Object o) {
        return (o instanceof b);
    }

    @Override
    public String toString() {
        return "b";
    }

}
