package tom.library.theory.shrink;

import java.util.ArrayList;
import java.util.List;

import org.junit.contrib.theories.PotentialAssignment;
import org.junit.contrib.theories.PotentialAssignment.CouldNotGenerateValueException;
import org.junit.contrib.theories.Theory;
import org.junit.contrib.theories.internal.Assignments;
import org.junit.contrib.theories.internal.ParameterizedAssertionError;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import tom.library.theory.internal.CounterExample;
import tom.library.theory.internal.ParameterizedAssertionFailure;

public abstract class ShrinkTheoryAnchor extends Statement{
	
	protected final FrameworkMethod fTestMethod;
	protected final TestClass fTestClass;
	private final List<AssumptionViolatedException> fInvalidParameters =
			new ArrayList<AssumptionViolatedException>();

	protected final CounterExample initialCounterExample;
	protected CounterExample counterExample;

	protected int fShrunkCount = 0;

	protected ShrinkValueSupplier shrinkSupplier;
	
	protected ShrinkTheoryAnchor(FrameworkMethod method, TestClass testClass, CounterExample counterExample, int shrunkCount) {
		fTestMethod = method;
		fTestClass = testClass;
		initialCounterExample = counterExample;
		this.counterExample = counterExample;
		shrinkSupplier = new ShrinkValueSupplier();
	}

	private TestClass getTestClass() {
		return fTestClass;
	}

	@Override
	public void evaluate() throws Throwable {
		doShrink(initialCounterExample);
		
	}
	
	// TODO rename method
	protected void doShrink(CounterExample counterExamples) throws Throwable {
		runShrinkWithAssigments(Assignments.allUnassigned(fTestMethod.getMethod(), getTestClass()), counterExamples);
	}
	
	protected void runShrinkWithAssigments(Assignments parameterAssigments, CounterExample counterExamples) throws Throwable {
		if (!parameterAssigments.isComplete()) {
			runShrinkWithIncompleteAssignment(parameterAssigments, counterExamples);
		} else {
			runShrinkWithCompleteAssignment(parameterAssigments);
		}
	}

	protected void runShrinkWithIncompleteAssignment(Assignments incomplete, CounterExample counterExamples) throws Throwable {
		for (PotentialAssignment source : shrinkSupplier.getNextReducedPotentialSources(incomplete.nextUnassigned(), counterExamples.getCounterExampleObject())) {
			runShrinkWithAssigments(incomplete.assignNext(source), counterExamples.nextCounterExample());
		}
	}

	protected void runShrinkWithCompleteAssignment(final Assignments complete) throws Throwable {
		statementShrinkForCompleteAssignment(complete).evaluate();
	}


	private Statement statementShrinkForCompleteAssignment(final Assignments complete)
			throws InitializationError {
		return new BlockJUnit4ClassRunner(getTestClass().getJavaClass()) {
			@Override
			protected void collectInitializationErrors(List<Throwable> errors) {
				// do nothing
			}

			@Override
			public Statement methodBlock(FrameworkMethod method) {
				final Statement statement = super.methodBlock(method);
				
				return new Statement() {
					@Override
					public void evaluate() throws Throwable {
						try {
							statement.evaluate();
						} catch (AssumptionViolatedException e) {
							handleAssumptionViolation(e);
						} catch (Throwable e) {
							handleShrinkParameterizedFailure(e, complete.getMethodArguments(nullsOk()));
						}
					}
				};
			}

			@Override
			protected Statement methodInvoker(FrameworkMethod method, Object test) {
				return methodWithCompleteParameters(method, complete, test);
			}

			@Override
			public Object createTest() throws Exception {
				return getTestClass().getOnlyConstructor().newInstance(
						complete.getConstructorArguments(nullsOk()));
			}
		}.methodBlock(fTestMethod);
	}
	
	private Statement methodWithCompleteParameters(final FrameworkMethod method,
			final Assignments complete, final Object freshInstance) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				try {
					final Object[] values = complete.getMethodArguments(nullsOk());
					method.invokeExplosively(freshInstance, values);
				} catch (CouldNotGenerateValueException e) {
					// ignore
				}
			}
		};
	}

	protected void handleAssumptionViolation(AssumptionViolatedException e) {
		fInvalidParameters.add(e);
	}
	
	protected void increaseShrunkCount() {
		fShrunkCount++;
	}

	protected abstract void handleShrinkParameterizedFailure(Throwable e, Object...params) throws Throwable;

	protected void repeatShrink(Throwable e, Object... params) throws Throwable {
		CounterExample temp = CounterExample.build(params);
		if (temp.isSmallerThan(counterExample)) {
			increaseShrunkCount();
			counterExample = temp;
			doShrink(counterExample);
		}
	}
	protected abstract void doNextShrinkStep(Throwable e, Object... params) throws Throwable;

	protected void throwParameterizedAssertionFailure(Throwable e, Object... params) throws Throwable {
		if (params.length == 0) {
			throw e;
		}
		throw new ParameterizedAssertionFailure(e, fTestMethod.getName(), fShrunkCount, initialCounterExample.getCounterExamples(), params);
	}

	protected void reportParameterizedError(Throwable e, Object... params) throws Throwable {
		if (params.length == 0) {
			throw e;
		}

		throw new ParameterizedAssertionError(e, fTestMethod.getName(), params);
	}

	private boolean nullsOk() {
		Theory annotation = fTestMethod.getMethod().getAnnotation(Theory.class);
		return annotation != null && annotation.nullsAccepted();
	}
	
	public CounterExample getCounterExample() {
		return counterExample;
	}
}
