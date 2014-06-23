package tom.library.shrink.reducers;

import static tom.library.shrink.tools.VisitableTools.buildVisitableFromPrimitives;
import static tom.library.shrink.tools.VisitableTools.getValueFromTermInteger;
import static tom.library.shrink.tools.VisitableTools.getValueFromTermString;
import static tom.library.shrink.tools.VisitableTools.isValueInstanceOfInteger;
import static tom.library.shrink.tools.VisitableTools.isValueInstanceOfString;

import java.util.Collection;
import java.util.HashSet;

import tom.library.sl.Visitable;

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
		Reducer reducer = new IntegerReducerDecorator(new BaseReducer(intValue));
		results.addAll(buildVisitableFromPrimitives(reducer.reduce().toArray()));
	}

	private void reduceStringValues() {
		String stringValue = getValueFromTermString(term);
		Reducer reducer = new StringReducerDecorator(new BaseReducer(stringValue));
		results.addAll(buildVisitableFromPrimitives(reducer.reduce().toArray()));
	}

}
