package tom.library.theory.shrink.suppliers.reducers2;

import java.util.ArrayList;
import java.util.List;

import tom.library.sl.Visitable;
import tom.library.theory.shrink.tools.TermClass;
/**
 * Reduces a given term to its terminal constructor and immediate sub-terms that have same type
 * 
 * @author nauval
 *
 */
public class SubtermReducer implements Reducer<Visitable> {

	private List<Visitable> results;
	private List<Visitable> terminals;
	private Visitable root;
	
	private void initialize(Visitable term) {
		root = term;
		if (results == null) {
			results = new ArrayList<Visitable>();
		} else {
			results.clear();
		}
		
		if (terminals == null) {
			terminals = new ArrayList<Visitable>();
		} else {
			terminals.clear();
		}
	}
	
	@Override
	public List<Visitable> reduce(Visitable term) {
		initialize(term);
		addTerminalConstructor();
		getSubtermsWithSameType(root);
		return results;
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
		return term.getClass().getSuperclass().equals(this.root.getClass().getSuperclass());
	}
	
	private void addTerminalConstructor() {
		terminals.addAll(TermClass.build(root).getTerminalConstructor());
		results.addAll(terminals);
	}
}
