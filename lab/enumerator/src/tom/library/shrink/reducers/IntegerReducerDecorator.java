package tom.library.shrink.reducers;

import java.util.Collection;
import java.util.HashSet;

import tom.library.shrink.tools.RandomValueGenerator;

/**
 * A {@code Reducer} to reduce an integer value into smaller integers.
 * It is done by generating random number between 0 to the given integer.
 * The class uses the absolute value in the process, therefore -1 < -4.
 * @author nauval
 *
 */
public class IntegerReducerDecorator extends ReducerDecorator{

	private static final int PART = 3;
	private static final int MAX_NUMBER = 10;
	private static final int PART_CARDINALITY = MAX_NUMBER/PART;
	
	private Collection<Integer> results;
	private int value;
	
	public IntegerReducerDecorator(Reducer reducer) {
		super(reducer);
		results = new HashSet<Integer>();
		value = (int) reducer.getTerm();
	}

	@Override
	public Object getTerm() {
		return value;
	}

	/**
	 * Returns a {@code Collection} of reduced integers out of an integer.
	 * If the integer is less then {@code MAX_NUMBER} then a sequence of 
	 * integers from 0 to its value are generated otherwise
	 * a {@code MAX_NUMBER} of integers will be generated randomly.
	 */
	@Override
	public Collection<Object> reduce() {
		reduceIntegerValues();
		Collection<Object> terms = reducer.reduce();
		terms.addAll(results);
		return terms;
	}

	
	private void reduceIntegerValues() {
		if (Math.abs(value) < MAX_NUMBER) {
			generateSequentialInteger();
		} else {
			generateRandomInteger();
		}
	}

	/**
	 * Generates smaller integers sequentially
	 */
	private void generateSequentialInteger() {
		int absValue = Math.abs(value);
		for (int i = 0; i < absValue; i++) {
			int newValue = value < 0? i * -1 : i;
			while (results.contains(newValue)) {
				newValue = value < 0? i * -1 : i;
			}
			results.add(newValue);
		}
	}
	
	/**
	 * Generates smaller integers randomly. To ensure that the random
	 * generated integers not concentrated somewhere between 0 and 
	 * the given integer, first the range is divided by part, and for
	 * each part the method calculates how many integers needs to be
	 * generated and generate them randomly.
	 */
	private void generateRandomInteger() {
		int part = 0;
		int absTerm = Math.abs(value);
		int offset = absTerm/PART;
		int width = part + offset;
		for (int i = 0; i < MAX_NUMBER; i++) {
			if (i % PART_CARDINALITY == 0 && i > 0) {
				int tmp = part + offset;
				part += tmp >= absTerm? offset/2 : offset;
				tmp = part + offset;
				width = tmp > absTerm? absTerm: tmp; 
			}
			int tempValue = getRandomValue(part, width);
			int newValue = value < 0? tempValue * -1 : tempValue;
			while (results.contains(newValue)) {
				tempValue = getRandomValue(part, width);
				newValue = value < 0? tempValue * -1 : tempValue;
			}
			results.add(newValue);
		}
	}
	
	private int getRandomValue(int min, int max) {
		return RandomValueGenerator.generateRandomFromRange(min, max);
	}
}
