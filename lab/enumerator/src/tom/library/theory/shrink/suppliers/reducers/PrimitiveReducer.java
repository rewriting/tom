package tom.library.theory.shrink.suppliers.reducers;

import static tom.library.theory.shrink.tools.VisitableTools.*;

import java.util.ArrayList;
import java.util.List;

import tom.library.sl.Visitable;

public class PrimitiveReducer implements Reducer<Visitable> {

	private List<Visitable> results;
	private Visitable term;
	
	public PrimitiveReducer() {
		results = new ArrayList<Visitable>();
	}
	
	@Override
	public List<Visitable> reduce(Visitable term) {
		this.term = term;
		results.clear();
		reduceValue();
		return results;
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
		IntegerReducer reducer = new IntegerReducer();
		results.addAll(buildVisitableFromPrimitives(reducer.reduce(intValue)));
	}

	private void reduceStringValues() {
		String stringValue = getValueFromTermString(term);
		StringReducer reducer = new StringReducer();
		results.addAll(buildVisitableFromPrimitives(reducer.reduce(stringValue)));
	}
}
