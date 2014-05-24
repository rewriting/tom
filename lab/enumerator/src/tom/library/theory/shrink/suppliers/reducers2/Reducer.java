package tom.library.theory.shrink.suppliers.reducers2;

import java.util.List;

public interface Reducer<T> {
	/**
	 * Reduces a term to smaller terms.
	 * 
	 * @param term
	 * @return list of reduced terms
	 */
	public List<T> reduce(T term);
}
