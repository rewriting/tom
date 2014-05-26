package tom.library.theory.shrink.suppliers;

import java.util.Comparator;
import java.util.List;

import org.junit.contrib.theories.PotentialAssignment;

import tom.library.shrink.BigShrink;
import tom.library.shrink.Shrink;
import tom.library.sl.Visitable;
import tom.library.theory.tools.TheoryVisitableTools;

public class BigShrinkValueSupplier implements ShrinkParameterSupplier {
	
	@Override
	public List<PotentialAssignment> getValueSources(
			Object counterExample) {
		Shrink shrink = new BigShrink();
		Object[] terms = shrink.shrink(counterExample, new TermComparator()).toArray();
		return SupplierHelper.buildPotentialAssignments(terms);
	}
	
	public static class TermComparator implements Comparator<Object> {

		@Override
		public int compare(Object o1, Object o2) {
			if (areObjectsInstanceOfVisitable(o1, o2)) {
				Visitable t1 = (Visitable) o1;
				Visitable t2 = (Visitable) o2;
				int[] s1 = TheoryVisitableTools.pairSize(t1);
				int[] s2 = TheoryVisitableTools.pairSize(t2);
				if (s1[0] == s2[0]) {
					return s1[1] - s2[1];
				} else {
					return s1[0] - s2[0];
				}
			}
			return o1.hashCode() - o2.hashCode();
		}
		
		private boolean areObjectsInstanceOfVisitable(Object o1, Object o2) {
			return TheoryVisitableTools.isInstanceOfVisitable(o1) 
					&& TheoryVisitableTools.isInstanceOfVisitable(o2);
		}
		
	}
}
