package tom.library.shrink.reducers;

import java.util.Collection;
import java.util.HashSet;

import tom.library.shrink.tools.RandomValueGenerator;

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
