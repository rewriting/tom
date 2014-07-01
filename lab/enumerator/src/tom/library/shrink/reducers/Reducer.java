package tom.library.shrink.reducers;

import java.util.Collection;
/**
 * The interface to reduce a term to smaller terms.
 * @author nauval
 *
 */
public interface Reducer {
	public Object getTerm();
	public Collection<Object> reduce();
}
