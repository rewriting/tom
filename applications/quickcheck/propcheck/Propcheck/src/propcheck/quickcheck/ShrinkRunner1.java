package propcheck.quickcheck;

import propcheck.assertion.NotTestedSkip;
import propcheck.property.Property;
import propcheck.shrink.PropcheckShink;
import propcheck.shrink.Shrink;
import propcheck.tools.SimpleLogger;

public class ShrinkRunner1<A> implements ShrinkRunner {

	private A rootTerm;
	private Property<A> property;
	private int shrunkCount = 0;
	
	public ShrinkRunner1(A term, Property<A> property) {
		this.rootTerm = term;
		this.property = property;
	}
	
	@Override
	public void run() {
		Shrink<A> shrinker = new PropcheckShink<A>(rootTerm);
		A input = rootTerm;
		A cex = input;
		while (shrinker.hasNextSubterm()) {
			input = shrinker.getNextshrinkedTerm();
			try {
				property.apply(input);
			} catch (NotTestedSkip skip) {
				// do nothing
			} catch (AssertionError error) {
				// assign shrinker to shrink the counter example
				shrinker.setCurrentTerm(input);
				cex = input;
				shrunkCount ++;
			}
		}
		print(shrunkCount, cex);
	}
	
	void print(int shrunk, A input) {
		String message = String.format("Shrunk %s times, counter example:\n%s", shrunk, input);
		SimpleLogger.log(message);
	}

}
