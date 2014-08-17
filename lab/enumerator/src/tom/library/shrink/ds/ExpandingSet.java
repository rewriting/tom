package tom.library.shrink.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * A naive implementation of set using List that provides a capability
 * to add elements and give the next element that not yet been explored.
 * </p>
 * @author nauval
 *
 * @param <T>
 */
public class ExpandingSet<T> {
	
	private int index;
	private List<T> list;
	
	public ExpandingSet() {
		list = new ArrayList<T>();
	}
	
	/**
	 * Adds all member of a given {@link Collection} to the set.
	 * The Collection values is checked first so that no duplication
	 * occurs.
	 * @param c
	 */
	public void addAll(Collection<? extends T> c) {
		for (T t : c) {
			if (!list.contains(t)) {
				list.add(t);
			}
		}
	}
	
	public void add(T t) {
		list.add(t);
	}
	
	/**
	 * Returns a boolean whether there are elements that not
	 * yet been explored.
	 * @return
	 */
	public boolean hasNext() {
		return index < list.size();
	}
	
	/**
	 * Get the next element to be explored.
	 * @return
	 */
	public T getNext() {
		return list.get(index++);
	}
	
	public void clear() {
		index = 0;
		list.clear();
	}
}
