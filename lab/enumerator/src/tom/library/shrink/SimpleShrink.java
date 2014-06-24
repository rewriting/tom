package tom.library.shrink;

import static tom.library.shrink.tools.VisitableTools.isInstanceOfVisitable;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

import tom.library.shrink.reducers.BaseReducer;
import tom.library.shrink.reducers.ConstansReducersDecorator;
import tom.library.shrink.reducers.IntegerReducerDecorator;
import tom.library.shrink.reducers.Reducer;
import tom.library.shrink.reducers.StringReducerDecorator;
import tom.library.shrink.reducers.SubtermsReducerDecorator;
import tom.library.shrink.reducers.ValueReducerDecorator;



/**
 * <p>
 * Generates terms from a given counter-example. The steps that are used in this process are:
 * <ul>
 * <li>Get immediate sub-terms that have the same type with the counter-examples</li>
 * <li>Replace subterms with their terminal constructor</li>
 * <li>Generate terms that have smaller int/String values based on the given counter-example</li>
 * </ul>
 * By using this process, the shrink mechanism should be done repetitively, i.e. the new counter-examples
 * need to be shrunk again using this class until no smaller counter-example found.
 * </p>
 * 
 * <p>
 * To apply the rules, {@code SimpleShrink} uses a {@code Reducer}. {@code Reducer}
 * is an interface to reduce a given term to some smaller terms. A decorator pattern
 * has been used to build the reducers.
 * </p>
 * 
 * @author nauval
 *
 */
public class SimpleShrink implements Shrink {

	private Collection<Object> terms;

	private Reducer reducer;

	public SimpleShrink() {
		
	}

	@Override
	public Collection<Object> shrink(Object term) {
		terms = new HashSet<Object>();
		buildSmallerTerms(term);
		return terms;
	}



	@Override
	public Collection<Object> shrink(Object term,
			Comparator<? super Object> comparator) {
		terms = new TreeSet<Object>(comparator);
		buildSmallerTerms(term);
		return terms;
	}

	private void buildSmallerTerms(Object term) {
		if (isInstanceOfVisitable(term)) {
			buildReducerForVisitable(term);
		} else if (isInstanceOfString(term)) {
			buildReducerForString(term);
		} else if (isInstanceOfInteger(term)) {
			buildReducerForInteger(term);
		}
		terms.addAll(reducer.reduce());
	}

	private boolean isInstanceOfString(Object term) {
		return term instanceof String;
	}

	private boolean isInstanceOfInteger(Object term) {
		return term instanceof Integer;
	}
	
	private void buildReducerForVisitable(Object term) {
		reducer = new BaseReducer(term);
		reducer = new SubtermsReducerDecorator(reducer);
		reducer = new ConstansReducersDecorator(reducer);
		reducer = new ValueReducerDecorator(reducer);
	}

	private void buildReducerForString(Object term) {
		reducer = new BaseReducer(term);
		reducer = new StringReducerDecorator(reducer);
	}
	
	private void buildReducerForInteger(Object term) {
		reducer = new BaseReducer(term);
		reducer = new IntegerReducerDecorator(reducer);
	}
}
