package tom.library.shrink.reducers;

import java.util.Collection;
import java.util.HashSet;

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
