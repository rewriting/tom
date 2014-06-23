package tom.library.shrink.reducers;

import java.util.Collection;
import java.util.HashSet;

import tom.library.shrink.tools.TermWrapper;
import tom.library.sl.Visitable;

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

	@Override
	public Collection<Object> reduce() {
		addTerminalConstructor();
		getSubtermsWithSameType(term);
		Collection<Object> terms = reducer.reduce();
		terms.addAll(results);
		return terms;
	}

	private void getSubtermsWithSameType(Visitable term) {
		for (Visitable child : term.getChildren()) {
			//if (isChildHasSameTypeAsRoot(child)) {
			if (isChildHasSameTypeAsRootAndNotTerminal(child)) {
				results.add(child);
			} else {
				getSubtermsWithSameType(child);
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
	
	private void addTerminalConstructor() {
		terminals.addAll(TermWrapper.build(term).getTerminalConstructor());
		results.addAll(terminals);
	}

}
