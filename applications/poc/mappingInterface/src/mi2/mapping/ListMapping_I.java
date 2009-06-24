package mi2.mapping;

import java.util.List;

/**
 * @author nick-vintila
 * @date Jun 23, 2009  9:06:06 PM
 */
public interface ListMapping_I<T> {

    boolean isSym(Object t);

    boolean isEmpty(List<T> l);

    List<T> makeEmpty();

    List<T> makeInsert(T o, List<T> l);

    T getHead(List<T> l);

    List<T> getTail(List<T> l);
}
