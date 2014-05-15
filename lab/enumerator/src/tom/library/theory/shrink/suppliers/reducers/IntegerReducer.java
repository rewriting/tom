package tom.library.theory.shrink.suppliers.reducers;

import java.util.ArrayList;
import java.util.List;

import tom.library.sl.Visitable;
import tom.library.sl.VisitableBuiltin;
import tom.library.theory.shrink.tools.RandomValueGenerator;

public class IntegerReducer  {
	
	private static final int PART = 3;
	private static final int MAX_NUMBER_OF_VALUES = 10;
	private static final int PART_CARDINALITY = MAX_NUMBER_OF_VALUES/PART;

	public static IntegerReducer build() {
		return new IntegerReducer();
	}
	
	public List<Visitable> getReducedVisitableValue(int value) {
		return buildReducedValue(value);
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getReducedIntegerValue(int value) {
		List<Integer> results = new ArrayList<Integer>();
		for (Visitable v : getReducedVisitableValue(value)) {
			VisitableBuiltin<Integer> vi = (VisitableBuiltin<Integer>) v;
			results.add(vi.getBuiltin());
		}
		if (results.isEmpty()) {
			results.add(value);
		}
		return results;
	}
	
	private List<Visitable> buildReducedValue(int term) {
		List<Visitable> results = new ArrayList<Visitable>();
		if (Math.abs(term) <= MAX_NUMBER_OF_VALUES) {
			results.addAll(generateSequentialInteger(term));
		} else {
			results.addAll(generateRandomInteger(term));
		}
		return results;
	}
	
	private List<Visitable> generateSequentialInteger(int term) {
		List<Visitable> results = new ArrayList<Visitable>();
		int absTerm = Math.abs(term);
		for (int i = 0; i < absTerm; i++) {
			int value = term < 0? i * -1 : i;
			results.add(new VisitableBuiltin<Integer>(value));
		}
		return results;
	}
	
	private List<Visitable> generateRandomInteger(int term) {
		int part = 0;
		int absTerm = Math.abs(term);
		int offset = absTerm/PART;
		int width = part + offset;
		List<Visitable> results = new ArrayList<Visitable>();
		for (int i = 0; i < MAX_NUMBER_OF_VALUES; i++) {
			if (i % PART_CARDINALITY == 0 && i > 0) {
				int tmp = part + offset;
				part += tmp >= absTerm? offset/2 : offset;
				tmp = part + offset;
				width = tmp > absTerm? absTerm: tmp; 
			}
			int tmpVal = getRandomValue(part, width);
			int value = term < 0? tmpVal * -1 : tmpVal;
			results.add(new VisitableBuiltin<Integer>(value));
		}
		return results;
	}

	private int getRandomValue(int min, int max) {
		return RandomValueGenerator.generateRandomFromRange(min, max);
	}
}
