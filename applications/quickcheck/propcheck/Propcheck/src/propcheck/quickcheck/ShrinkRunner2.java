package propcheck.quickcheck;

import propcheck.assertion.NotTestedSkip;
import propcheck.property.Property;
import propcheck.property.Property2;
import propcheck.shrink.PropcheckShink;
import propcheck.shrink.Shrink;
import propcheck.tools.SimpleLogger;

public class ShrinkRunner2<A, B> implements ShrinkRunner {

	private A rootTermA;
	private B rootTermB;
	private Property2<A, B> property;
	private int shrunkCount = 1;
	
	public ShrinkRunner2(A termA, B termB, Property2<A, B> property) {
		this.rootTermA = termA;
		this.rootTermB = termB;
		this.property = property;
	}
	
	@Override
	public void run() {
		Shrink<A> shrinkerA = new PropcheckShink<A>(rootTermA);
		Shrink<B> shrinkerB = new PropcheckShink<B>(rootTermB);
		A inputA = rootTermA;
		B inputB = rootTermB;
		
		// apply for inputA
		inputA = applyA(shrinkerA, inputA, inputB);
		
		// apply for inputB
		inputB = applyB(shrinkerB, inputA, inputB);
		
		print(shrunkCount, inputA, inputB);
	}

	private B applyB(Shrink<B> shrinkerB, A inputA, B inputB) {
		while (shrinkerB.hasNextSubterm()) {
			inputB = shrinkerB.getNextshrinkedTerm();
			try {
				property.apply(inputA, inputB);
			} catch (NotTestedSkip skip) {
				// do nothing
			} catch (AssertionError error) {
				// assign shrinker to shrink the counter example
				shrinkerB.setCurrentTerm(inputB);
				shrunkCount ++;
			}
		}
		return inputB;
	}

	private A applyA(Shrink<A> shrinkerA, A inputA, B inputB) {
		while (shrinkerA.hasNextSubterm()) {
			inputA = shrinkerA.getNextshrinkedTerm();
			try {
				property.apply(inputA, inputB);
			} catch (NotTestedSkip skip) {
				// do nothing
			} catch (AssertionError error) {
				// assign shrinker to shrink the counter example
				shrinkerA.setCurrentTerm(inputA);
				shrunkCount ++;
			}
		}
		return inputA;
	}
	
	void print(int shrunk, A inputA, B inputB) {
		String message = String.format("Shrunk %s times, counter example:\n%s\n%s", shrunk, inputA, inputB);
		SimpleLogger.log(message);
	}

}
