package tom.library.shrink.reducers;

import tom.library.sl.VisitableBuiltin;

/**
 * A factory to create a reducer for a given term.
 * As the instance of this factory is tied down to
 * a term, it does not use the singleton pattern
 * and return a new instance when {@code getInstance}
 * method is called.
 * 
 * @author nauval
 *
 */
public class ReducerFactory {
	
	private enum TermType{
		VISITABLE,
		VISITABLE_BUILTIN,
		INTEGER,
		STRING
	}
	
	private Object term;
	
	private TermType type;
	
	private ReducerFactory(Object term, TermType type) {
		this.term = term;
		this.type = type;
	}
	
	/**
	 * Returns new instance of a ReducerFactory with the
	 * corresponding term's type
	 * @param term
	 * @return {@code ReducerFactory}
	 */
	public static ReducerFactory getInstance(Object term) {
		if (term instanceof String) {
			return new ReducerFactory(term, TermType.STRING);
		} else if (term instanceof Integer) {
			return new ReducerFactory(term, TermType.INTEGER);
		} else if (term instanceof VisitableBuiltin<?>) {
			/*
			 * This block is to handle the VisitableBuiltin type,
			 * An instance of Visitable which contains a value
			 * instead of term, i.e. the leaf of a term.
			 */
			return new ReducerFactory(term, TermType.VISITABLE_BUILTIN);
		}
		return new ReducerFactory(term, TermType.VISITABLE);
	}

	/**
	 * A {@code Reducer} for the given term.
	 * For each type, the reducer is different.
	 * @return {@code Reducer}
	 */
	public Reducer createReducer() {
		switch (type) {
		case INTEGER:
			return buildIntegerReducer();
		case STRING:
			return buildStringReducer();
		case VISITABLE_BUILTIN:
			return buildVisitableBuiltinReducer();
		default:
			return buildVisitableReducer();
		}
	}

	/**
	 * Returns a {@code PrimitiveVisitableReducerDecorator}, 
	 * a {@code Reducer} for {@code VisitableBuiltin}
	 * @return
	 */
	private Reducer buildVisitableBuiltinReducer() {
		Reducer reducer = new BaseReducer(term);
		reducer = new PrimitiveVisitableReducerDecorator(reducer);
		return reducer;
	}

	/**
	 * Returns a {@code Reducer} for integer
	 * @return
	 */
	private Reducer buildIntegerReducer() {
		Reducer reducer = new BaseReducer(term);
		reducer = new IntegerReducerDecorator(reducer);
		return reducer;
	}
	
	/**
	 * Returns a {@code Reducer} for string 
	 * @return
	 */
	private Reducer buildStringReducer() {
		Reducer reducer = new BaseReducer(term);
		reducer = new StringReducerDecorator(reducer);
		return reducer;
	}

	/**
	 * Returns a {@code Reducer} for a {@code Visitable}
	 * object. The reducers represent the three mutation rules
	 * to find smaller terms. {@code SubtermsReducerDecorator} 
	 * is representing the first, {@code ConstantsReducerDecorator}
	 * is representing the second and {@code ValueReducerDecorator} is
	 * representing the last rule.
	 * @return
	 */
	private Reducer buildVisitableReducer() {
		Reducer reducer = new BaseReducer(term);
		reducer = new SubtermsReducerDecorator(reducer);
		reducer = new ConstantsReducerDecorator(reducer);
		reducer = new ValueReducerDecorator(reducer);
		return reducer;
	}
}
