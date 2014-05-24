package tom.library.theory.shrink.suppliers;

import static tom.library.theory.shrink.tools.VisitableTools.*;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.contrib.theories.PotentialAssignment;

import tom.library.sl.Visitable;
import tom.library.theory.shrink.ShrinkParameterSupplier;
import tom.library.theory.shrink.ds.ExpandingList;
import tom.library.theory.shrink.suppliers.reducers2.ExplotionReducer;
import tom.library.theory.shrink.suppliers.reducers2.IntegerReducer;
import tom.library.theory.shrink.suppliers.reducers2.Reducer;
import tom.library.theory.shrink.suppliers.reducers2.StringReducer;
import tom.library.theory.shrink.suppliers.reducers2.SubtermReducer;
import tom.library.theory.shrink.suppliers.reducers2.ValueReducer;

public class BigShrinkValueSupplier implements ShrinkParameterSupplier {

	//private Set<Object> termsToExplore;
	private SortedSet<Object> exploredTerms;
	
	private ExpandingList<Object> listTerms;
	
	private Reducer<Visitable> subtermReducer;
	private Reducer<Visitable> explotionReducer;
	private Reducer<Visitable> valueReducer;
	
	public BigShrinkValueSupplier() {
		//termsToExplore = new HashSet<Object>();
		exploredTerms = new TreeSet<Object>(new TermComparator());
		
		listTerms = new ExpandingList<Object>();
		
		subtermReducer = new SubtermReducer();
		explotionReducer = new ExplotionReducer();
		valueReducer = new ValueReducer();
	}
	
	@Override
	public List<PotentialAssignment> getValueSources(
			Object counterExample) {
		listTerms.clear();
		exploredTerms.clear();
		// check if counter example is instance of visitable
		if (isInstanceOfVisitable(counterExample)) {
			// do term shrink
			exploreVisitableTerm((Visitable) counterExample);
		} else {
			// do primitive shrink
			explorePrimitiveTerm(counterExample);
		}
		
		Object[] arrays = exploredTerms.toArray();
		List<PotentialAssignment> assignments = SupplierHelper.buildPotentialAssignments(arrays);
		return assignments;
	}

	private void exploreVisitableTerm(Visitable term) {
		//termsToExplore.add(term);
		listTerms.add(term);
		while (listTerms.hasNext()) {
			Visitable iteratedTerm = (Visitable) listTerms.getNext();
			
			// first step
			listTerms.addAll(subtermReducer.reduce(iteratedTerm));
			// second step
			listTerms.addAll(explotionReducer.reduce(iteratedTerm));
			// third step
			listTerms.addAll(valueReducer.reduce(iteratedTerm));
			// add iteratedTerm to explored
			exploredTerms.add(iteratedTerm);
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
		Reducer<String> reducer = new StringReducer();
		exploredTerms.addAll(reducer.reduce(term));
	}
	
	private void exploreIntegerTerm(Integer term) {
		Reducer<Integer> reducer = new IntegerReducer();
		exploredTerms.addAll(reducer.reduce(term));
	}
	
	
	private static class TermComparator implements Comparator<Object> {

		@Override
		public int compare(Object o1, Object o2) {
			if (o1.equals(o2) || 01 == 02) {
				return 0;
			}
			
			if (isInstanceOfVisitable(o1) && isInstanceOfVisitable(o2)) {
				int[] s1 = pairSize((Visitable) o1);
				int[] s2 = pairSize((Visitable) o2);
				// same number of constractor
				if (s1[0] == s2[0]) {
					// compare the value
					return s1[1] - s2[1];
				} else {
					// compare the constructor
					return s1[0] - s2[0];
				}
				
			}
			return o1.hashCode() - o2.hashCode();
		}
		
	}
}
