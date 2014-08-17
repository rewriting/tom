package tom.library.shrink.tools;

import java.util.ArrayList;
import java.util.List;

import tom.library.sl.Visitable;
import tom.library.sl.VisitableBuiltin;

/**
 * <p>
 * A helper class to deal with an instance of {@code Visitable} object
 * such as examining whether an object is an instance of {@code Visitable}
 * or {@code VisitableBuiltin}, build a list of {@code VisitableBuiltin} from
 * a list (or array) of a type and calculate a term's size.
 * </p>
 * @author nauval
 *
 */
public class VisitableTools {
	private static int size = 0;
	private static int val = 0;
	
	/**
	 * <p>
	 * Return size of a term including its values. The size is 
	 * calculated by counting the number of its constructors
	 * and add them with the total of its values. If the values
	 * are integers than the size will be calculated using their absolute
	 * value, if values are strings then their length will be used
	 * as the basis of the calculation.
	 * </p>
	 * @param term
	 * @return size of a term calculated including its values
	 */
	public static int size(Visitable term) {
		size = 0;
		if (term == null) {
			return Integer.MAX_VALUE;
		}
		if (term instanceof Visitable) {
			Visitable t = (Visitable) term;
			calculateSizeWithValue(t);
		}
		return size;
	}
	
	/**
	 * <p>
	 * Returns a pair of constructor size and total of term's value.
	 * In this method, the size of term's constructors and its values
	 * are calculated differently and returned as separated values.
	 * </p>
	 * <p>
	 * For example the pair size of this term {@code push(push(empty(), 3), 4)}
	 * is an array [3][7] where the value at zeroth index is the size of term's
	 * constructors and the value at the first one is the size of the values.
	 * </p> 
	 * @param term
	 * @return array of int with size 2. The zeroth index is the term's constructor
	 * size and the first is the size of its values.
	 */
	public static int[] pairSize(Visitable term) {
		size = 0;
		val = 0;
		if (term == null) {
			return new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE};
		}
		if (term instanceof Visitable) {
			Visitable t = (Visitable) term;
			calculateSizeValuePair(t);
		}
		
		return new int[] {size, val};
	}
	
	/**
	 * A recursive method to calculate the size of a given term including 
	 * its values.
	 * @param term
	 */
	private static void calculateSizeWithValue(Visitable term) {
		if (term.getChildCount() == 0) {
			// calculate value
			size += calculateValue(term);
		} else {
			size ++;
		}
		for (Visitable v : term.getChildren()) {
			calculateSizeWithValue(v);
		}
	}
	
	/**
	 * Calculate the size of a given term's constructor separately
	 * with the term's value.
	 * @param term
	 */
	private static void calculateSizeValuePair(Visitable term) {
		if (term.getChildCount() == 0) {
			// calculate value
			size ++;
			val += calculateValue(term);
		} else {
			size ++;
		}
		for (Visitable v : term.getChildren()) {
			calculateSizeValuePair(v);
		}
	}
	
	/**
	 * Calculates a value of a given {@code Visitable} object. if it is
	 * a string then the returned value is its length,
	 * if it is an integer than the returned value is
	 * its absolute value, if it is a term then it will
	 * return 1 (representing a constructor).
	 * @param term
	 * @return size of a {@code Visitable} object
	 */
	private static int calculateValue(Visitable term) {
		if (isValueInstanceOfInteger(term)) {
			return Math.abs(getValueFromTermInteger(term));
		} else if (isValueInstanceOfString(term)) {
			return getValueFromTermString(term).length();
		} else {
			return 1;
		}
	}
	
	/**
	 * Checks whether the given {@code Visitable} object
	 * is an integer.
	 * @param term
	 * @return
	 */
	public static boolean isValueInstanceOfInteger(Visitable term) {
		return castToVisitableBuiltin(term).getBuiltin() instanceof Integer;
	}
	
	/**
	 * Checks whether the given {@code Visitable} object is a string
	 * @param term
	 * @return
	 */
	public static boolean isValueInstanceOfString(Visitable term) {
		return castToVisitableBuiltin(term).getBuiltin() instanceof String;
	}
	
	/**
	 * Returns the string contained in a {@code Visitable} object
	 * @param term
	 * @return string
	 */
	public static String getValueFromTermString(Visitable term) {
		return (String) castToVisitableBuiltin(term).getBuiltin();
	}
	
	/**
	 * Returns the int contained in a {@code Visitable} object
	 * @param term
	 * @return int
	 */
	public static int getValueFromTermInteger(Visitable term) {
		return (Integer) castToVisitableBuiltin(term).getBuiltin(); 
	}
	
	/**
	 * Casting a {@code Visitable} to a {@code VisitableBuiltin}.
	 * A {@code VisitableBuiltin} an implementation of the {@code Visitable}
	 * interface which contains built in types such as integers and strings.
	 * @param term
	 * @return string
	 */
	private static VisitableBuiltin<?> castToVisitableBuiltin(Visitable term) {
		if (isInstanceOfVisitableBuiltin(term)) {
			return (VisitableBuiltin<?>) term;
		} else {
			return new VisitableBuiltin<Object>(null);
		}
	}
	
	private static boolean isInstanceOfVisitableBuiltin(Visitable term) {
		return term instanceof VisitableBuiltin<?>;
	}
	
	public static boolean isInstanceOfVisitable(Object term) {
		return term instanceof Visitable;
	}
	
	/**
	 * Returns a list of {@code Visitable} from a given array of a type.
	 * @param values
	 * @return
	 */
	public static<T> List<Visitable> buildVisitableFromPrimitives(T[] values) {
		List<Visitable> visitables = new ArrayList<Visitable>();
		for (int i = 0; i < values.length; i++) {
			visitables.add(new VisitableBuiltin<T>(values[i]));
		}
		return visitables;
	}
	
	/**
	 * Returns a list of {@code Visitable} from a given list of a type.
	 * @param values
	 * @return
	 */
	public static<T> List<Visitable> buildVisitableFromPrimitives(List<T> values) {
		List<Visitable> visitables = new ArrayList<Visitable>();
		for (T value : values) {
			visitables.add(new VisitableBuiltin<T>(value));
		}
		return visitables;
	}
}
