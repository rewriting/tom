package tom.library.shrink;

import java.util.Collection;
import java.util.Comparator;

/**
 * Shrinks a given term to a smaller terms.
 * 
 * @author nauval
 *
 */
public interface Shrink {
	public Collection<Object> shrink(Object term);
	public Collection<Object> shrink(Object term, Comparator<? super Object> comparator);
}
