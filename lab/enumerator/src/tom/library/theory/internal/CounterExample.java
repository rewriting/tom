package tom.library.theory.internal;

import java.util.Arrays;
import java.util.List;

import tom.library.theory.shrink.ShrinkAssignmentRunner;
import tom.library.theory.tools.TheoryVisitableTools;
import tom.library.sl.Visitable;

/**
 * <p>
 * Stores a counter-example of each theory's parameters.
 * This class is used when generating counter-examples
 * candidate and assign them to appropriate theory's
 * parameter that happens in {@link ShrinkAssignmentRunner} class.
 * A method to check whether the counter-examples contained in the class
 * is smaller than a given {@code CounterExample} is also provided 
 * in the class.
 * </p>
 * @author nauval
 *
 */
public class CounterExample {
	private final List<Object> fUnassigned;
	
	private CounterExample(List<Object> counterExamples) {
		fUnassigned = counterExamples;
	}
	
	/**
	 * Returns a new instance of the {@code CounterExample} containing
	 * the given objects.
	 * 
	 * @param counterExamples
	 * @return
	 */
	public static CounterExample build(Object...counterExamples) {
		return new CounterExample(Arrays.asList(counterExamples));
	}
	
	/**
	 * Returns a new instance of {@code CounterExample} containing
	 * only the next counter-examples, i.e. counter-examples of next
	 * parameters.
	 * @return
	 */
	public CounterExample nextCounterExample() {
		return new CounterExample(fUnassigned.subList(1, fUnassigned.size()));
	}
	
	/**
	 * Return a counter-example of the current theory's parameter.
	 * @return
	 */
	public Object getCounterExample() {
		return fUnassigned.get(0);
	}
	
	public boolean isEmpty() {
		return fUnassigned.isEmpty();
	}
	
	/**
	 * <p>
	 * Returns true if the aggregated size of the contained counter-examples
	 * is equal to the aggregated size of a given {@code CounterExample}
	 * </p>
	 * <p>
	 * To ensure that the {@code CounterExample}s are comparable, both need
	 * to have the same number of counter-examples inside.
	 * </p>
	 * @param counterExample
	 * @return
	 */
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
