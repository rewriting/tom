package tom.library.shrink;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

import tom.library.shrink.reducers.ReducerFactory;



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
 * has been used to build the reducers. A {@code ReducerFactory} is used to
 * create the appropriate reducer for a given term.
 * </p>
 * 
 * @author nauval
 *
 */
public class SimpleShrink implements Shrink {


	/**
	 * Returns an unsorted collection of terms after the mutation process. 
	 */
	@Override
	public Collection<Object> shrink(Object term) {
		Collection<Object> terms = new HashSet<Object>();
		terms.addAll(ReducerFactory.getInstance(term).createReducer().reduce());
		return terms;
	}


	/**
	 * Returns a sorted collection of terms after the mutation process
	 */
	@Override
	public Collection<Object> shrink(Object term,
			Comparator<? super Object> comparator) {
		Collection<Object>  terms = new TreeSet<Object>(comparator);
		terms.addAll(ReducerFactory.getInstance(term).createReducer().reduce());
		return terms;
	}
}
