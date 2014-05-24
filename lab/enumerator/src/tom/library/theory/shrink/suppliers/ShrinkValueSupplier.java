package tom.library.theory.shrink.suppliers;

import static tom.library.theory.shrink.tools.VisitableTools.*;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.contrib.theories.PotentialAssignment;

import tom.library.sl.Visitable;
import tom.library.theory.shrink.ShrinkParameterSupplier;
import tom.library.theory.shrink.suppliers.reducers2.ExplotionReducer;
import tom.library.theory.shrink.suppliers.reducers2.IntegerReducer;
import tom.library.theory.shrink.suppliers.reducers2.Reducer;
import tom.library.theory.shrink.suppliers.reducers2.StringReducer;
import tom.library.theory.shrink.suppliers.reducers2.SubtermReducer;
import tom.library.theory.shrink.suppliers.reducers2.ValueReducer;

public class ShrinkValueSupplier implements ShrinkParameterSupplier {

	private SortedSet<Object> sortedTerms;
	
	private Reducer<Visitable> subtermReducer;
	private Reducer<Visitable> explotionReducer;
	private Reducer<Visitable> valueReducer;
	private Reducer<String> stringReducer;
	private Reducer<Integer> intReducer;
	
	public ShrinkValueSupplier() {
		sortedTerms = new TreeSet<Object>(new TermComparator());
		subtermReducer = new SubtermReducer();
		explotionReducer = new ExplotionReducer();
		valueReducer = new ValueReducer();
		stringReducer = new StringReducer();
		intReducer = new IntegerReducer();
	}
	
	@Override
	public List<PotentialAssignment> getValueSources(
			Object counterExample) {
		//terms.clear();
		sortedTerms.clear();
		buildSmallerTerms(counterExample);
		//terms.add(counterExample);
		sortedTerms.add(counterExample);
		return SupplierHelper.buildPotentialAssignments(sortedTerms.toArray());
	}
	
	private void buildSmallerTerms(Object object) {
		if (isInstanceOfVisitable(object)) {
			Visitable term = (Visitable) object;
//			terms.addAll(subtermReducer.reduce(term));
//			terms.addAll(explotionReducer.reduce(term));
//			terms.addAll(valueReducer.reduce(term));
			sortedTerms.addAll(subtermReducer.reduce(term));
			sortedTerms.addAll(explotionReducer.reduce(term));
			sortedTerms.addAll(valueReducer.reduce(term));
		} else {
			reducePrimitives(object);
		}
	}
	
	private void reducePrimitives(Object object) {
		if (isInstanceOfString(object)) {
			String term = (String) object;
			//terms.addAll(stringReducer.reduce(term));
			sortedTerms.addAll(stringReducer.reduce(term));
		} else if (isInstanceOfInteger(object)) {
			int term = (int) object;
			//terms.addAll(intReducer.reduce(term));
			sortedTerms.addAll(intReducer.reduce(term));
		}
	}
	
	private boolean isInstanceOfString(Object term) {
		return term instanceof String;
	}

	private boolean isInstanceOfInteger(Object term) {
		return term instanceof Integer;
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
