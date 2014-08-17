package tom.library.shrink.reducers;

/**
 * An abstract class for the decorator pattern.
 * @author nauval
 *
 */
public abstract class ReducerDecorator implements Reducer {
	protected Reducer reducer;
	public ReducerDecorator(Reducer reducer) {
		this.reducer = reducer;
	}
}
