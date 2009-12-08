package mi2.mapping;

import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.util.ListIterator;

/**
 * @author nick-vintila
 * @date Jun 23, 2009  9:36:31 PM
 */
public class ProjectionList<E> implements List<E> {

    private final List<E> delegate;
    private int offset;

    public ProjectionList(List<E> delegate, int offset) {

        assert delegate != null && delegate != this;
        assert delegate.size() >= offset;

        this.delegate = delegate;
        this.offset = offset;
    }

    public ProjectionList(List<E> delegate) {

        assert delegate != null && delegate != this;

        this.delegate = delegate;
        this.offset = 0;
    }

    public void advanceOffset() {
        this.offset++;
    }

    public int size() {
        return this.delegate.size() - this.offset;
    }

    public boolean isEmpty() {
        return this.delegate.size() == this.offset + 1;
    }

    public boolean contains(Object o) {
        return this.delegate.indexOf(o) >= this.offset;
    }

    public Iterator<E> iterator() {
        // todo: account for offset
        return this.delegate.iterator();
    }

    public Object[] toArray() {
        return this.delegate.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return this.delegate.toArray(a);
    }

    public boolean add(E o) {
        assert false : "Unexpected call!";
        return false;
    }

    public boolean remove(Object o) {
        assert false : "Unexpected call!";
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        return this.delegate.containsAll(c);
    }

    public boolean addAll(Collection<? extends E> c) {
        assert false : "Unexpected call!";
        return false;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        assert false : "Unexpected call!";
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        assert false : "Unexpected call!";
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        assert false : "Unexpected call!";
        return false;
    }

    public void clear() {
        assert false : "Unexpected call!";

    }

    public E get(int index) {
        return this.delegate.get(index + this.offset);
    }

    public E set(int index, E element) {
        assert false : "Unexpected call!";
        return null;
    }

    public void add(int index, E element) {
        assert false : "Unexpected call!";

    }

    public E remove(int index) {
        assert false : "Unexpected call!";
        return null;
    }

    public int indexOf(Object o) {
        return this.delegate.indexOf(o) - this.offset;
    }

    public int lastIndexOf(Object o) {
        return this.delegate.lastIndexOf(o) - this.offset;
    }

    public ListIterator<E> listIterator() {

        return this.delegate.listIterator();
    }

    public ListIterator<E> listIterator(int index) {
        return this.delegate.listIterator(index + this.offset);
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return this.delegate.subList(fromIndex + this.offset, toIndex + this.offset);
    }
}
