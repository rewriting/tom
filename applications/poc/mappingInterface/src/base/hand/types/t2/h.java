package base.hand.types.t2;

import base.hand.types.T2;
import base.hand.types.T1;

import java.util.List;

/**
 * @author nvintila
 * @date 3:47:05 PM Jun 13, 2009
 */
public class h extends T2 {

    List<T1> ts;

    public h(List<T1> ts) {
        this.ts = ts;
    }

    public List<T1> getTs() {
        return ts;
    }

    public void setTs(List<T1> ts) {
        this.ts = ts;
    }

    public boolean equals(Object o) {
        if (o instanceof h) {
            h oo = (h) o;
            return ts.equals(oo.getTs());
        }
        return false;
    }

    @Override
    public String toString() {
        return "h(" + ts + ")";
    }
}