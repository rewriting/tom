package tom.library.theory.shrink.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExpandingList<T> {
	
	private int index;
	private List<T> list;
	
	public ExpandingList() {
		list = new ArrayList<T>();
	}
	
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
	
	public boolean hasNext() {
		return index < list.size();
	}
	
	public T getNext() {
		return list.get(index++);
	}
	
	public void clear() {
		index = 0;
		list.clear();
	}
}
