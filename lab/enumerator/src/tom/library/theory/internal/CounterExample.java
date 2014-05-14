package tom.library.theory.internal;

import java.util.Arrays;
import java.util.List;

import tom.library.sl.Visitable;
import tom.library.theory.shrink.tools.VisitableTools;

public class CounterExample {
	private final List<Object> fUnassigned;
	
	private CounterExample(List<Object> counterExamples) {
		fUnassigned = counterExamples;
	}
	
	public static CounterExample build(Object...counterExamples) {
		return new CounterExample(Arrays.asList(counterExamples));
	}
	
	public CounterExample nextCounterExample() {
		return new CounterExample(fUnassigned.subList(1, fUnassigned.size()));
	}
	
	public Object getCounterExample() {
		return fUnassigned.get(0);
	}
	
	public boolean isEmpty() {
		return fUnassigned.isEmpty();
	}
	
	public boolean isEqualsTo(CounterExample counterExample) {
		boolean equal = true;
		if (fUnassigned.size() != counterExample.fUnassigned.size()) {
			return false;
		}
		for (int i = 0; i < fUnassigned.size(); i++) {
			if (!fUnassigned.get(i).equals(counterExample.fUnassigned.get(i))) {
				equal = false;
				break;
			}
		}
		return equal;
	}
	
	public boolean isSmallerThan(CounterExample counterExample) {
		int total1 = 0, total2 = 0;
		for (int i = 0; i < fUnassigned.size(); i++) {
			total1 += calculateTermSize(fUnassigned.get(i));
			total2 += calculateTermSize(counterExample.fUnassigned.get(i));
		}
		return total1 < total2;
	}
	
	private int calculateTermSize(Object term) {
		if (isInstanceofVisitable(term)) {
			return VisitableTools.size((Visitable) term);
		} else if (isInstanceOfString(term)) {
			String value = (String) term;
			return value.length();
		} else if (isInstanceOfInteger(term)) {
			return Math.abs((int) term);
		} else {
			return 0;
		}
	}
	
	private boolean isInstanceofVisitable(Object term) {
		return term instanceof Visitable;
	}
	
	private boolean isInstanceOfString(Object term) {
		return term instanceof String;
	}
	
	private boolean isInstanceOfInteger(Object term) {
		return term instanceof Integer;
	}
	
	public Object[] getCounterExamples() {
		return fUnassigned.toArray();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object c : fUnassigned) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(c);
		}
		return sb.toString();
	}
}
