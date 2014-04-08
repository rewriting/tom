package propcheck.quickcheck;

import propcheck.assertion.NotTestedSkip;
import propcheck.property.Property;
import propcheck.property.Property2;
import propcheck.shrink.PropcheckShrink;
import propcheck.shrink.Shrink;
import propcheck.tools.SimpleLogger;

public class ShrinkRunner2<A, B> implements ShrinkRunner {

	private A rootTermA;
	private B rootTermB;
	private Property2<A, B> property;
	private int shrunkCount = 0;
	
	public ShrinkRunner2(A termA, B termB, Property2<A, B> property) {
		this.rootTermA = termA;
		this.rootTermB = termB;
		this.property = property;
	}
	
	@Override
	public void run() {
		Shrink<A> shrinkerA = new PropcheckShrink<A>(rootTermA);
		Shrink<B> shrinkerB = new PropcheckShrink<B>(rootTermB);
		A inputA = rootTermA;
		B inputB = rootTermB;
		
		// apply for inputA
		A cexA = applyA(shrinkerA, inputA, inputB);
		
		// apply for inputB
		B cexB = applyB(shrinkerB, inputA, inputB);
		
		print(shrunkCount, cexA, cexB);
	}

	private B applyB(Shrink<B> shrinkerB, A inputA, B inputB) {
		B cex = inputB;
		while (shrinkerB.hasNextSubterm()) {
			inputB = shrinkerB.getNextshrinkedTerm();
			try {
				property.apply(inputA, inputB);
			} catch (NotTestedSkip skip) {
				// do nothing
			} catch (AssertionError error) {
				// assign shrinker to shrink the counter example
				shrinkerB.setCurrentTerm(inputB);
				cex = inputB;
				shrunkCount ++;
			}
		}
		return cex;
	}

	private A applyA(Shrink<A> shrinkerA, A inputA, B inputB) {
		A cex = inputA;
		while (shrinkerA.hasNextSubterm()) {
			inputA = shrinkerA.getNextshrinkedTerm();
			try {
				property.apply(inputA, inputB);
			} catch (NotTestedSkip skip) {
				// do nothing
			} catch (AssertionError error) {
				// assign shrinker to shrink the counter example
				shrinkerA.setCurrentTerm(inputA);
				cex = inputA;
				shrunkCount ++;
			}
		}
		return cex;
	}
	
	void print(int shrunk, A inputA, B inputB) {
		String message = String.format("Shrunk %s times, counter example:\n%s\n%s", shrunk, inputA, inputB);
		SimpleLogger.log(message);
	}

}
