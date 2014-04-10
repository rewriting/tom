package propcheck.quickcheck;

import propcheck.assertion.NotTestedSkip;
import propcheck.property.Property;
import propcheck.property.Property2;
import propcheck.property.Property3;
import propcheck.shrink.PropcheckShrink;
import propcheck.shrink.Shrink;
import propcheck.shrink.TermReducer;
import propcheck.tools.SimpleLogger;

public class ShrinkRunner3<A, B, C> implements ShrinkRunner {

	private A rootTermA;
	private B rootTermB;
	private C rootTermC;
	private Property3<A, B, C> property;
	private int shrunkCount = 0;

	public ShrinkRunner3(A termA, B termB, C termC, Property3<A, B, C> property) {
		this.rootTermA = termA;
		this.rootTermB = termB;
		this.rootTermC = termC;
		this.property = property;
	}

	@Override
	public void run() {
		Shrink<A> shrinkerA = new PropcheckShrink<A>(rootTermA);
		Shrink<B> shrinkerB = new PropcheckShrink<B>(rootTermB);
		Shrink<C> shrinkerC = new PropcheckShrink<C>(rootTermC);
		A inputA = rootTermA;
		B inputB = rootTermB;
		C inputC = rootTermC;

		// shrink part 1: get the least subterm fails the property
		// apply for inputA
		A cexA = applyA(shrinkerA, inputA, inputB, inputC);

		// apply for inputB
		B cexB = applyB(shrinkerB, inputA, inputB, inputC);

		// apply for inputB
		C cexC = applyC(shrinkerC, inputA, inputB, inputC);

		// shrink part 2: reduce the size of subterm
		TermReducer<A> reducerA = new TermReducer<A>(cexA);
		TermReducer<B> reducerB = new TermReducer<B>(cexB);
		TermReducer<C> reducerC = new TermReducer<C>(cexC);

		// apply for inputA
		cexA = applyA(reducerA, cexA, cexB, cexC);

		// apply for inputB
		cexB = applyB(reducerB, cexA, cexB, cexC);

		// apply for inputB
		cexC = applyC(reducerC, cexA, cexB, cexC);

		print(shrunkCount, cexA, cexB, cexC);
	}

	private B applyB(TermReducer<B> reducer, A inputA, B inputB, C inputC) {
		B cex = inputB;
		while (reducer.isReducable()) {
			inputB = reducer.reduce();
			try {
				property.apply(inputA, inputB, inputC);
			} catch (NotTestedSkip skip) {
				// do nothing
			} catch (AssertionError error) {
				// assign shrinker to shrink the counter example
				reducer.setRootTerm(inputB);
				cex = inputB;
				shrunkCount ++;
			}
		}
		return cex;
	}

	private A applyA(TermReducer<A> reducer, A inputA, B inputB, C inputC) {
		A cex = inputA;
		while (reducer.isReducable()) {
			inputA = reducer.reduce();
			try {
				property.apply(inputA, inputB, inputC);
			} catch (NotTestedSkip skip) {
				// do nothing
			} catch (AssertionError error) {
				// assign shrinker to shrink the counter example
				reducer.setRootTerm(inputA);
				cex = inputA;
				shrunkCount ++;
			}
		}
		return cex;
	}

	private C applyC(TermReducer<C> reducer, A inputA, B inputB, C inputC) {
		C cex = inputC;
		while (reducer.isReducable()) {
			inputC = reducer.reduce();
			try {
				property.apply(inputA, inputB, inputC);
			} catch (NotTestedSkip skip) {
				// do nothing
			} catch (AssertionError error) {
				// assign shrinker to shrink the counter example
				reducer.setRootTerm(inputC);
				cex = inputC;
				shrunkCount ++;
			}
		}
		return cex;
	}

	private B applyB(Shrink<B> shrinkerB, A inputA, B inputB, C inputC) {
		B cex = inputB;
		while (shrinkerB.hasNextSubterm()) {
			inputB = shrinkerB.getNextshrinkedTerm();
			try {
				property.apply(inputA, inputB, inputC);
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

	private A applyA(Shrink<A> shrinkerA, A inputA, B inputB, C inputC) {
		A cex = inputA;
		while (shrinkerA.hasNextSubterm()) {
			inputA = shrinkerA.getNextshrinkedTerm();
			try {
				property.apply(inputA, inputB, inputC);
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

	private C applyC(Shrink<C> shrinkerC, A inputA, B inputB, C inputC) {
		C cex = inputC;
		while (shrinkerC.hasNextSubterm()) {
			inputC = shrinkerC.getNextshrinkedTerm();
			try {
				property.apply(inputA, inputB, inputC);
			} catch (NotTestedSkip skip) {
				// do nothing
			} catch (AssertionError error) {
				// assign shrinker to shrink the counter example
				shrinkerC.setCurrentTerm(inputC);
				cex = inputC;
				shrunkCount ++;
			}
		}
		return cex;
	}

	void print(int shrunk, A inputA, B inputB, C inputC) {
		String message = String.format("Shrunk %s times, counter example:\n%s\n%s\n%s", shrunk, inputA, inputB, inputC);
		SimpleLogger.log(message);
	}

}
