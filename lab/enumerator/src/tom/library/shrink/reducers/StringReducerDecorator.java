package tom.library.shrink.reducers;

import java.util.Collection;
import java.util.HashSet;

import tom.library.shrink.tools.RandomValueGenerator;

/**
 * A {@code Reducer} to reduce a string value into smaller strings w.r.t their size/length.
 * Similar to {@link IntegerReducerDecorator} it uses random generation approach.
 * However, if the length is less then the {@code MAX_NUMBER} a sequence of strings from 
 * an empty string to the substring of the given string starting from index 0 to its length - 1.
 * @author nauval
 *
 */
public class StringReducerDecorator extends ReducerDecorator {

	private static final int PART = 3;
	private static final int MAX_NUMBER = 10;
	private static final int PART_CARDINALITY = MAX_NUMBER/PART;
	
	private Collection<String> results;
	private String value;
	
	
	public StringReducerDecorator(Reducer reducer) {
		super(reducer);
		results = new HashSet<String>();
		value = (String) reducer.getTerm();
	}

	@Override
	public Object getTerm() {
		return value;
	}

	/**
	 * Returns a {@code Collection} of reduced strings out of a string. 
	 * If the string's length less than {@code MAX_NUMBER} the method
	 * returns a sequence of strings from an empty one to {@code string.length - 1}.
	 * Otherwise substrings from the original string will be generated 
	 * randomly.
	 */
	@Override
	public Collection<Object> reduce() {
		buildReducedValue();
		Collection<Object> terms = reducer.reduce();
		terms.addAll(results);
		return terms;
	}

	private void buildReducedValue() {
		if (value.length() <= MAX_NUMBER) {
			generateSubstring();
		} else {
			generateRandomSubstring();
		}
	}

	private void generateSubstring() {
		for (int i = 0; i < value.length(); i++) {
			results.add(value.substring(0, i));
		}
	}

	/**
	 * Generates smaller strings randomly. First
	 * the string is broken down into parts and 
	 * for each part, the method calculates the number
	 * of smaller strings need to generated. Then, it
	 * generates smaller strings by doing a substring
	 * from 0 index to a generated number in each part until
	 * the quota for the part is reached.
	 * 
	 */
	private void generateRandomSubstring() {
		int part = 0;
		int offset = value.length()/PART;
		int width = part + offset;
		for (int i = 0; i < MAX_NUMBER; i++) {
			if (i % PART_CARDINALITY == 0 && i > 0) {
				int tmp = part + offset;
				part += tmp >= value.length()? offset/2 : offset;
				tmp = part + offset;
				width = tmp > value.length()? value.length(): tmp; 
			}
			int substringTo = getRandomValue(part, width);
			String newValue = value.substring(0, substringTo);
			results.add(newValue);
		}
	}
	
	private int getRandomValue(int min, int max) {
		return RandomValueGenerator.generateRandomFromRange(min, max);
	}
}
