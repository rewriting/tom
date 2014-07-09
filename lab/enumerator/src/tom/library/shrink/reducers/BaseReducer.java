package tom.library.shrink.reducers;

import java.util.Collection;
import java.util.HashSet;

/**
 * <p>
 * A base implementation {@code Reducer} interface.
 * The {@code reduce} method does not reduce the term,
 * it only returns a {@code Collection} containing 
 * only the term itself. This is done to make sure
 * that the original term is included as the reduced 
 * term to ensure termination in the Shrink mechanism 
 * process in PropCheck
 * </p>
 * @author nauval
 *
 */
public class BaseReducer implements Reducer {

	private Collection<Object> terms;
	private Object term;
	
	public BaseReducer(Object term) {
		terms = new HashSet<Object>();
		this.term = term;
	}
	
	@Override
	public Object getTerm() {
		return term;
	}

	@Override
	public Collection<Object> reduce() {
		terms.add(term);
		return terms;
	}
}
