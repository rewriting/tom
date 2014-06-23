package tom.library.shrink.reducers;

import java.util.Collection;

public interface Reducer {
	public Object getTerm();
	public Collection<Object> reduce();
}
