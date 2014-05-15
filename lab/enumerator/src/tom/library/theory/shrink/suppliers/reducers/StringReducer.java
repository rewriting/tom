package tom.library.theory.shrink.suppliers.reducers;

import java.util.ArrayList;
import java.util.List;

import tom.library.sl.Visitable;
import tom.library.sl.VisitableBuiltin;
import tom.library.theory.shrink.tools.RandomValueGenerator;

public class StringReducer {
	
	private static final int PART = 3;
	private static final int MAX_NUMBER_OF_VALUES = 10;
	private static final int PART_CARDINALITY = MAX_NUMBER_OF_VALUES/PART;

	public static StringReducer build() {
		return new StringReducer();
	}
	
	public List<Visitable> getReducedVisitableValue(String value) {
		return buildReducedValue(value);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getReducedStringValue(String value) {
		List<String> results = new ArrayList<String>();
		for(Visitable v : getReducedVisitableValue(value)) {
			VisitableBuiltin<String> vs = (VisitableBuiltin<String>) v;
			results.add(vs.getBuiltin());
		}
		if (results.isEmpty()) {
			results.add(value);
		}
		return results;
	}
	
	private List<Visitable> buildReducedValue(String term) {
		List<Visitable> inputs = new ArrayList<Visitable>();
		if (term.length() <= MAX_NUMBER_OF_VALUES) {
			inputs.addAll(generateSubstring(term));
		} else {
			inputs.addAll(generateRandomSubstring(term));
		}
		return inputs;
	}

	private List<Visitable> generateSubstring(String term) {
		List<Visitable> results = new ArrayList<Visitable>();
		for (int i = 0; i < term.length(); i++) {
			results.add(new VisitableBuiltin<String>(term.substring(0, i)));
		}
		return results;
	}

	private List<Visitable> generateRandomSubstring(String term) {
		List<Visitable> results = new ArrayList<Visitable>();
		int part = 0;
		int offset = term.length()/PART;
		int width = part + offset;
		for (int i = 0; i < MAX_NUMBER_OF_VALUES; i++) {
			if (i % PART_CARDINALITY == 0 && i > 0) {
				int tmp = part + offset;
				part += tmp >= term.length()? offset/2 : offset;
				tmp = part + offset;
				width = tmp > term.length()? term.length(): tmp; 
			}
			int substringTo = getRandomValue(part, width);
			System.out.println("part: " + part);
			System.out.println("width: " + width);
			System.out.println("substringTo: " + substringTo);
			results.add(new VisitableBuiltin<String>(term.substring(0, substringTo)));
		}
		return results;
	}
	
	private int getRandomValue(int min, int max) {
		return RandomValueGenerator.generateRandomFromRange(min, max);
	}
}
