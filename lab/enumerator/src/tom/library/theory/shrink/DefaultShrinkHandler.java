package tom.library.theory.shrink;

import org.junit.contrib.theories.internal.Assignments;

import tom.library.theory.internal.CounterExample;
import tom.library.theory.internal.ExecutionHandler;
import tom.library.theory.internal.ParameterizedAssertionFailure;
import tom.library.theory.internal.TestObject;
import tom.library.theory.shrink.suppliers.ExplodedTermParameterSupplier;
import tom.library.theory.shrink.suppliers.ReducedTermsParameterSupplier;

public class DefaultShrinkHandler implements ShrinkHandler {
	private TestObject testObject;
	private int shrunkCount = 0;
	private CounterExample initialCounterExample;
	private CounterExample currentCounterExample;
	
	public DefaultShrinkHandler(TestObject testObject) {
		this.testObject = testObject;
	}
	
	@Override
	public void shrink(CounterExample counterExample) throws Throwable {
		initializeCounterExamples(counterExample);
		doFirstStep();
	}
	
	protected void doFirstStep() throws Exception, Throwable {
		ExecutionHandler handler = new ExecutionHandler(this) {
			@Override
			public void handleFailures(Throwable e, String methodName, Object... params) throws Throwable {
				repeatFirstStep(e, params);
				// Chain the second step directly after the first step is finished.
				doSecondStep();
				throwParameterizedAssertionFailureWithCounterExamples(e, params);
			}
		};
		evaluateAssignment(handler, new ReducedTermsParameterSupplier());
	}
	
	protected void doSecondStep() throws Throwable {
		ExecutionHandler handler = new ExecutionHandler(this) {
			@Override
			public void handleFailures(Throwable e, String methodName, Object... params) throws Throwable {
				repeatSecondStep(e, params);
				// If there is another step after second step, it should be placed here
				// before throwing the exception.
				throwParameterizedAssertionFailureWithCounterExamples(e, params);
			}
		};
		evaluateAssignment(handler, new ExplodedTermParameterSupplier());
	}

	private void evaluateAssignment(ExecutionHandler handler,
			ShrinkParameterSupplier supplier) throws Throwable {
		ShrinkAssignmentRunner runner = new ShrinkAssignmentRunner(testObject, handler, supplier);
    	runner.runWithAssignment(getUnassignedAssignments(), currentCounterExample);
	}

	private Assignments getUnassignedAssignments() throws Exception {
		return Assignments.allUnassigned(testObject.getMethod(), testObject.getTestClass());
	}
	
	protected void repeatFirstStep(Throwable e, Object... params) throws Throwable {
		// TODO revise so it can be as planned
		CounterExample temp = CounterExample.build(params);
		if (temp.isSmallerThan(currentCounterExample)) {
			increaseShrunkCount();
			currentCounterExample = temp;
			doFirstStep();
		}
	}
	
	protected void repeatSecondStep(Throwable e, Object... params) throws Throwable {
		CounterExample temp = CounterExample.build(params);
		// TODO revise so it can be as planned
		if (temp.isSmallerThan(currentCounterExample)) {
			increaseShrunkCount();
			currentCounterExample = temp;
			doSecondStep();
		}
	}
	
	private void increaseShrunkCount() {
		shrunkCount++;
	}

	protected void initializeCounterExamples(CounterExample counterExample) {
		initialCounterExample = counterExample;
		currentCounterExample = counterExample;
	}
	
	protected void throwParameterizedAssertionFailureWithCounterExamples(Throwable e, Object... params) throws Throwable {
		if (params.length == 0) {
			throw e;
		}
		throw new ParameterizedAssertionFailure(e, testObject.getMethodName(), shrunkCount, 
				initialCounterExample.getCounterExamples(), params);
	}
}
