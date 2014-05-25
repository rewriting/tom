package tom.library.theory.shrink.suppliers.reducers;

import java.util.ArrayList;
import java.util.List;

import tom.library.theory.shrink.tools.RandomValueGenerator;


public class IntegerReducer implements Reducer<Integer>{

	private static final int PART = 3;
	private static final int MAX_NUMBER = 10;
	private static final int PART_CARDINALITY = MAX_NUMBER/PART;
	
	private List<Integer> results;
	
	private int value;
	
	public IntegerReducer() {
		results = new ArrayList<Integer>();
	}
	
	public List<Integer> reduce(Integer value) {
		this.value = value;
		results.clear();
		reduceIntegerValues();
		return results;
	}

	private void reduceIntegerValues() {
		if (Math.abs(value) < MAX_NUMBER) {
			generateSequentialInteger();
		} else {
			generateRandomInteger();
		}
	}

	private void generateSequentialInteger() {
		int absValue = Math.abs(value);
		for (int i = 0; i < absValue; i++) {
			int newValue = value < 0? i * -1 : i;
			results.add(newValue);
		}
	}
	
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
			results.add(newValue);
		}
	}
	
	private int getRandomValue(int min, int max) {
		return RandomValueGenerator.generateRandomFromRange(min, max);
	}
}
