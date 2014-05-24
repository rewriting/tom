package tom.library.theory.shrink.suppliers.reducers2;

import java.util.ArrayList;
import java.util.List;

import tom.library.theory.shrink.tools.RandomValueGenerator;

public class StringReducer implements Reducer<String>{

	private static final int PART = 3;
	private static final int MAX_NUMBER = 10;
	private static final int PART_CARDINALITY = MAX_NUMBER/PART;
	
	private List<String> results;
	
	private String value;
	
	public StringReducer() {
		results = new ArrayList<String>();
	}
	
	@Override
	public List<String> reduce(String value) {
		results.clear();
		this.value = value;
		buildReducedValue();
		return results;
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
