package tom.library.shrink;

import static tom.library.shrink.tools.VisitableTools.isInstanceOfVisitable;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

import tom.library.shrink.ds.ExpandingSet;
import tom.library.shrink.reducers.ExplotionReducer;
import tom.library.shrink.reducers.IntegerReducer;
import tom.library.shrink.reducers.Reducer;
import tom.library.shrink.reducers.StringReducer;
import tom.library.shrink.reducers.SubtermReducer;
import tom.library.shrink.reducers.ValueReducer;
import tom.library.sl.Visitable;

public class BigShrink implements Shrink{

	private ExpandingSet<Object> toExplore;
	private Collection<Object> terms;
	
	private Reducer<Visitable> subtermReducer;
	private Reducer<Visitable> explotionReducer;
	private Reducer<Visitable> valueReducer;
	private Reducer<String> stringReducer;
	private Reducer<Integer> intReducer;
	
	public BigShrink() {
		toExplore = new ExpandingSet<Object>();
		terms = new HashSet<Object>();
		
		subtermReducer = new SubtermReducer();
		explotionReducer = new ExplotionReducer();
		valueReducer = new ValueReducer();
		stringReducer = new StringReducer();
		intReducer = new IntegerReducer();
	}
	
	@Override
	public Collection<Object> shrink(Object term) {
		reset();
		buildSmallerTerms(term);
		return terms;
	}
	
	@Override
	public Collection<Object> shrink(Object term,
			Comparator<? super Object> comparator) {
		reset(comparator);
		buildSmallerTerms(term);
		return terms;
	}
	
	private void reset() {
		terms.clear();
		toExplore.clear();
	}
	
	private void reset(Comparator<? super Object> comparator) {
		terms = new TreeSet<Object>(comparator);
		toExplore.clear();
	}

	private void buildSmallerTerms(Object object) {
		if (isInstanceOfVisitable(object)) {
			Visitable term = (Visitable) object;
			exploreVisitableTerm(term);
		} else {
			explorePrimitiveTerm(object);
		}
	}
	
	private void exploreVisitableTerm(Visitable term) {
		toExplore.add(term);
		while (toExplore.hasNext()) {
			Visitable iteratedTerm = (Visitable) toExplore.getNext();
			
			// first step
			toExplore.addAll(subtermReducer.reduce(iteratedTerm));
			// second step
			toExplore.addAll(explotionReducer.reduce(iteratedTerm));
			// third step, this makes the generation process takes 
			// too long. 
			//toExplore.addAll(valueReducer.reduce(iteratedTerm));
			// add iteratedTerm to explored
			terms.add(iteratedTerm);
		}
	}
	
	private void explorePrimitiveTerm(Object term) {
		if (isInstanceOfString(term)) {
			exploreStringTerm((String) term);
		} else if (isInstanceOfInteger(term)) {
			exploreIntegerTerm((Integer) term);
		}
	}

	private boolean isInstanceOfString(Object term) {
		return term instanceof String;
	}

	private boolean isInstanceOfInteger(Object term) {
		return term instanceof Integer;
	}
	
	private void exploreStringTerm(String term) {
		terms.addAll(stringReducer.reduce(term));
		addTermIfBuiltTermsIsEmpty(term);
	}
	
	private void exploreIntegerTerm(Integer term) {
		terms.addAll(intReducer.reduce(term));
		addTermIfBuiltTermsIsEmpty(term);
	}
	
	private void addTermIfBuiltTermsIsEmpty(Object term) {
		if (terms.isEmpty()) {
			terms.add(term);
		}
	}
}
