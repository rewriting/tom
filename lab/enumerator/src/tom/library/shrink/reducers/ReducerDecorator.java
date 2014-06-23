package tom.library.shrink.reducers;


public abstract class ReducerDecorator implements Reducer {
	protected Reducer reducer;
	public ReducerDecorator(Reducer reducer) {
		this.reducer = reducer;
	}
}
