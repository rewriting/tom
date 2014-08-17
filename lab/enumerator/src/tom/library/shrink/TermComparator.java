package tom.library.shrink;

import static tom.library.shrink.tools.VisitableTools.*;
import java.util.Comparator;

import tom.library.sl.Visitable;

public class TermComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		if (areObjectsInstanceOfVisitable(o1, o2)) {
			Visitable t1 = (Visitable) o1;
			Visitable t2 = (Visitable) o2;
			int[] s1 = pairSize(t1);
			int[] s2 = pairSize(t2);
			if (s1[0] == s2[0]) {
				return s1[1] - s2[1];
			} else {
				return s1[0] - s2[0];
			}
		}
		return o1.hashCode() - o2.hashCode();
	}
	
	private boolean areObjectsInstanceOfVisitable(Object o1, Object o2) {
		return isInstanceOfVisitable(o1) 
				&& isInstanceOfVisitable(o2);
	}
}
