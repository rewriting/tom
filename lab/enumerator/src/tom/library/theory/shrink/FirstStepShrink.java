package tom.library.theory.shrink;

import org.junit.contrib.theories.PotentialAssignment;
import org.junit.contrib.theories.internal.Assignments;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import tom.library.theory.internal.CounterExample;

public class FirstStepShrink extends ShrinkTheoryAnchor{
	
	public static FirstStepShrink build(FrameworkMethod method, TestClass testClass, CounterExample counterExample) {
		return new FirstStepShrink(method, testClass, counterExample);
	}
	
	private FirstStepShrink(FrameworkMethod method, TestClass testClass, CounterExample counterExample) {
		super(method, testClass, counterExample, 0);
	}

	protected void runShrinkWithIncompleteAssignment(Assignments incomplete, CounterExample counterExamples) throws Throwable {
		for (PotentialAssignment source : shrinkSupplier.getNextReducedPotentialSources(incomplete.nextUnassigned(), counterExamples.getCounterExampleObject())) {
			runShrinkWithAssigments(incomplete.assignNext(source), counterExamples.nextCounterExample());
		}
	}

	protected void handleShrinkParameterizedFailure(Throwable e, Object...params) throws Throwable {
		repeatShrink(e, params);
		doNextShrinkStep(e, params);
		throwParameterizedAssertionFailure(e, params);
	}

	protected void doNextShrinkStep(Throwable e, Object... params) throws Throwable {
		SecondStepShrink.build(fTestMethod, fTestClass, CounterExample.build(params), fShrunkCount).evaluate();
	}
}
