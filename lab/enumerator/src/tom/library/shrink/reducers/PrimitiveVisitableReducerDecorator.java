package tom.library.shrink.reducers;

import static tom.library.shrink.tools.VisitableTools.buildVisitableFromPrimitives;
import static tom.library.shrink.tools.VisitableTools.getValueFromTermInteger;
import static tom.library.shrink.tools.VisitableTools.getValueFromTermString;
import static tom.library.shrink.tools.VisitableTools.isValueInstanceOfInteger;
import static tom.library.shrink.tools.VisitableTools.isValueInstanceOfString;

import java.util.Collection;
import java.util.HashSet;

import tom.library.sl.Visitable;

/**
 * A {@code Reducer} for an instance of {@code Visitable} whose value is a primitive.
 * An example of this is a primitive argument of a constructor, e.g. 1 in cs(1).
 * In a Gom term, the 1 is wrapped in a {@code VisitableBuiltin} type.
 * @author nauval
 *
 */
public class PrimitiveVisitableReducerDecorator extends ReducerDecorator{

	private Collection<Visitable> results;
	private Visitable term;
	
	public PrimitiveVisitableReducerDecorator(Reducer reducer) {
		super(reducer);
		results = new HashSet<Visitable>();
		term = (Visitable) reducer.getTerm();
	}

	@Override
	public Object getTerm() {
		return term;
	}

	/**
	 * <p>
	 * Reduces a term of a {@code VisitableBuiltin} type.
	 * It reduces the term based on the contained type.
	 * A {@code VisitableBuiltin} is an instance of a
	 * {@code Visitable} used to wrap a leaf of a term,
	 * i.e. a value of a term.
	 * </p>
	 * <p>
	 * It is done by inspecting the actual type of the
	 * contained term and reduce it using an appropriate
	 * {@code Reducer}.
	 * </p>
	 */
	@Override
	public Collection<Object> reduce() {
		reduceValue();
		Collection<Object> terms = reducer.reduce();
		terms.addAll(results);
		return terms;
	}
	
	private void reduceValue() {
		if (isValueInstanceOfString(term)) {
			reduceStringValues();
		} else if (isValueInstanceOfInteger(term)){
			reduceIntegerValues();
		}
	}

	private void reduceIntegerValues() {
		int intValue = getValueFromTermInteger(term);
		Reducer reducer = ReducerFactory.getInstance(intValue).createReducer();
		results.addAll(buildVisitableFromPrimitives(reducer.reduce().toArray()));
	}

	private void reduceStringValues() {
		String stringValue = getValueFromTermString(term);
		Reducer reducer = ReducerFactory.getInstance(stringValue).createReducer();
		results.addAll(buildVisitableFromPrimitives(reducer.reduce().toArray()));
	}

}
