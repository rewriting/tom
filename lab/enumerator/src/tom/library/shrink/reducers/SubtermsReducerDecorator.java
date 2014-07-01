package tom.library.shrink.reducers;

import java.util.Collection;
import java.util.HashSet;

import tom.library.shrink.tools.TermWrapper;
import tom.library.sl.Visitable;

/**
 * A {@code Reducer} to calculate the first step of the mutation rule:
 * get the constants and get the immediate subterm with
 * the same type out of a term.
 * @author nauval
 *
 */
public class SubtermsReducerDecorator extends ReducerDecorator {
	private Collection<Visitable> results;
	private Collection<Visitable> terminals;
	private Visitable term;
	
	public SubtermsReducerDecorator(Reducer reducer) {
		super(reducer);
		this.term = (Visitable) reducer.getTerm();
		results = new HashSet<Visitable>();
		terminals = new HashSet<Visitable>();
	}
	
	@Override
	public Object getTerm() {
		return term;
	}

	/**
	 * Returns a {@link Collection} containing the constants and the immediate
	 * subterm with the same type from a term.
	 */
	@Override
	public Collection<Object> reduce() {
		includeTerminalConstructorsToResults();
		extractImmediateSubterms(term);
		Collection<Object> terms = reducer.reduce();
		terms.addAll(results);
		return terms;
	}

	/**
	 * A method where the traversal process take place.
	 * It traverses down a term and if the the subterm 
	 * is not a constant and has the same type as the term,
	 * the the traversed subterm will be extracted.
	 * @param term
	 */
	private void extractImmediateSubterms(Visitable term) {
		for (Visitable child : term.getChildren()) {
			//if (isChildHasSameTypeAsRoot(child)) {
			if (isChildHasSameTypeAsRootAndNotTerminal(child)) {
				results.add(child);
			} else {
				extractImmediateSubterms(child);
			}
		}
	}
	
	private boolean isChildHasSameTypeAsRoot(Visitable term) {
		return isTermHaveTheSameSortAsRoot(term);
	}
	
	private boolean isChildHasSameTypeAsRootAndNotTerminal(Visitable term) {
		return isTermHaveTheSameSortAsRoot(term) && !terminals.contains(term);
	}
	
	private boolean isTermHaveTheSameSortAsRoot(Visitable term) {
		return term.getClass().getSuperclass().equals(this.term.getClass().getSuperclass());
	}
	
	
	private void includeTerminalConstructorsToResults() {
		terminals.addAll(TermWrapper.build(term).getTerminalConstructors());
		results.addAll(terminals);
	}

}
