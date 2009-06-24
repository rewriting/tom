package mi2.mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nick-vintila
 * @date Jun 23, 2009  9:03:26 PM
 */
public abstract class List_Mapping<T> extends List_Introspector<T> {

    // Test
    public boolean isSym(Object t) {
        return (t != null) && (t instanceof List);
    }

    public boolean isEmpty(List<T> l) {
        return l.isEmpty();
    }

    public List<T> makeEmpty() {
        return new ArrayList<T>();
    }

    public List<T> makeInsert(T o, List<T> l) {
        List<T> res = deepClone(l);
        res.add(0, o);
        return res;
    }

    public T getHead(List<T> l) {
        return l.get(0);
    }

    public List<T> getTail(List<T> l) {
        List<T> res = deepClone(l);
        res.remove(0);
        return res;
    }

    private static <T> List<T> deepClone(List<T> l) {
        List<T> l_ = new ArrayList<T>(l);
        return l_;
    }
}
