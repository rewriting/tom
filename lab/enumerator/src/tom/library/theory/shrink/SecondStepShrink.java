package tom.library.theory.shrink;

import org.junit.contrib.theories.PotentialAssignment;
import org.junit.contrib.theories.internal.Assignments;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import tom.library.theory.internal.CounterExample;

public class SecondStepShrink extends ShrinkTheoryAnchor {

	public static SecondStepShrink build(FrameworkMethod method, TestClass testClass,
			CounterExample counterExample, int prevShrunkCount) {
		return new SecondStepShrink(method, testClass, counterExample, prevShrunkCount);
	}
	
	private SecondStepShrink(FrameworkMethod method, TestClass testClass,
			CounterExample counterExample, int prevShrunkCount) {
		super(method, testClass, counterExample, prevShrunkCount);
	}

	@Override
	protected void runShrinkWithIncompleteAssignment(Assignments incomplete,
			CounterExample counterExamples) throws Throwable {
		for (PotentialAssignment source : shrinkSupplier.getNextExplodedPotentialSources(counterExamples.getCounterExampleObject())) {
			runShrinkWithAssigments(incomplete.assignNext(source), counterExamples.nextCounterExample());
		}
	}
	
	@Override
	protected void handleShrinkParameterizedFailure(Throwable e,
			Object... params) throws Throwable {
		repeatShrink(e, params);
		throwParameterizedAssertionFailure(e, params);
	}

	@Override
	protected void doNextShrinkStep(Throwable e, Object... params)
			throws Throwable {
		// do nothing
	}
}
