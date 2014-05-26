package tom.library.shrink;

import static tom.library.shrink.tools.VisitableTools.isInstanceOfVisitable;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

import tom.library.shrink.reducers.ExplotionReducer;
import tom.library.shrink.reducers.IntegerReducer;
import tom.library.shrink.reducers.Reducer;
import tom.library.shrink.reducers.StringReducer;
import tom.library.shrink.reducers.SubtermReducer;
import tom.library.shrink.reducers.ValueReducer;
import tom.library.sl.Visitable;

public class SimpleShrink implements Shrink {

	private Collection<Object> terms;
	
	private Reducer<Visitable> subtermReducer;
	private Reducer<Visitable> explotionReducer;
	private Reducer<Visitable> valueReducer;
	private Reducer<String> stringReducer;
	private Reducer<Integer> intReducer;
	
	public SimpleShrink() {
		terms = new HashSet<Object>();
		
		subtermReducer = new SubtermReducer();
		explotionReducer = new ExplotionReducer();
		valueReducer = new ValueReducer();
		stringReducer = new StringReducer();
		intReducer = new IntegerReducer();
	}
	
	@Override
	public Collection<Object> shrink(Object term) {
		terms.clear();
		buildSmallerTerms(term);
		addTermIfBuiltTermsIsEmpty(term);
		return terms;
	}
	
	@Override
	public Collection<Object> shrink(Object term,
			Comparator<? super Object> comparator) {
		terms = new TreeSet<Object>(comparator);
		buildSmallerTerms(term);
		addTermIfBuiltTermsIsEmpty(term);
		return terms;
	}

	private void buildSmallerTerms(Object object) {
		if (isInstanceOfVisitable(object)) {
			Visitable term = (Visitable) object;
			terms.addAll(subtermReducer.reduce(term));
			terms.addAll(explotionReducer.reduce(term));
			terms.addAll(valueReducer.reduce(term));
		} else {
			reducePrimitives(object);
		}
	}
	
	private void reducePrimitives(Object object) {
		if (isInstanceOfString(object)) {
			String term = (String) object;
			terms.addAll(stringReducer.reduce(term));
		} else if (isInstanceOfInteger(object)) {
			int term = (int) object;
			terms.addAll(intReducer.reduce(term));
		}
	}
	
	private boolean isInstanceOfString(Object term) {
		return term instanceof String;
	}

	private boolean isInstanceOfInteger(Object term) {
		return term instanceof Integer;
	}
	
	private void addTermIfBuiltTermsIsEmpty(Object term) {
		if (terms.isEmpty()) {
			terms.add(term);
		}
	}
}
