package tom.library.shrink;

import static tom.library.shrink.tools.VisitableTools.isInstanceOfVisitable;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

import tom.library.shrink.ds.ExpandingSet;
import tom.library.shrink.reducers.BaseReducer;
import tom.library.shrink.reducers.ConstantsReducerDecorator;
import tom.library.shrink.reducers.IntegerReducerDecorator;
import tom.library.shrink.reducers.Reducer;
import tom.library.shrink.reducers.ReducerFactory;
import tom.library.shrink.reducers.StringReducerDecorator;
import tom.library.shrink.reducers.SubtermsReducerDecorator;
import tom.library.shrink.reducers.ValueReducerDecorator;
import tom.library.sl.Visitable;

/**
 * Generates terms from a given counter-example. The process includes:
 * <ul>
 * <li>Get immediate sub-terms that have the same type as the counter-example</li>
 * <li>Replace sub-terms with its terminal constants</li>
 * <li>Generate smaller terms with smaller integer/String values</li>
 * </ul>
 * Above steps will be iterated until all the generated terms cannot be reduced any longer.
 * The final result is a considerably big list of terms that are smaller or equal to the
 * counter-example.
 * 
 * <p>
 * Note: this approach is experimental. It takes too much time, especially during the computation of the last rule
 * </p>
 * @author nauval
 *
 */
public class BigShrink implements Shrink{

	private ExpandingSet<Object> toExplore;
	private Collection<Object> terms;
	
	private Reducer reducer;
	
	public BigShrink() {
		toExplore = new ExpandingSet<Object>();
		terms = new HashSet<Object>();
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
			//reducer = new BaseReducer(iteratedTerm);
			// first step
			//reducer = new SubtermsReducerDecorator(reducer);
			// second step
			//reducer = new ConstantsReducerDecorator(reducer);
			
			// third step, this makes the generation process takes 
			// too long. 
			//reducer = new ValueReducerDecorator(reducer);
			//Collection<Object> result = reducer.reduce();
			Collection<Object> result = ReducerFactory.getInstance(iteratedTerm).createReducer().reduce();
			result.remove(iteratedTerm);
			toExplore.addAll(result);
			
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
		reducer = new StringReducerDecorator(new BaseReducer(term));
		terms.addAll(reducer.reduce());
		addTermIfBuiltTermsIsEmpty(term);
	}
	
	private void exploreIntegerTerm(Integer term) {
		reducer = new IntegerReducerDecorator(new BaseReducer(term));
		terms.addAll(reducer.reduce());
		addTermIfBuiltTermsIsEmpty(term);
	}
	
	private void addTermIfBuiltTermsIsEmpty(Object term) {
		if (terms.isEmpty()) {
			terms.add(term);
		}
	}
}
