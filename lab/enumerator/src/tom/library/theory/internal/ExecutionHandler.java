package tom.library.theory.internal;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.AssumptionViolatedException;

import tom.library.theory.shrink.ShrinkHandler;

public class ExecutionHandler {

	private final List<AssumptionViolatedException> invalidParameters =
			new ArrayList<AssumptionViolatedException>();
	
	
	private ShrinkHandler shrinkHandler;	
	private CounterExample counterExample;
	private Statistic statistic;
	
	public ExecutionHandler(ShrinkHandler handler) {
		shrinkHandler = handler;
		statistic = new Statistic();
	}
	
	public void handleSuccess() {
		increaseSuccessCount();
	}
	
	protected void increaseSuccessCount() {
    	statistic.increaseSuccessCount();
    }
	
	public void handleAssumptionViolation(AssumptionViolatedException exception) {
		invalidParameters.add(exception);
		increaseAssumtionViolationCount();
	}
	
	protected void increaseAssumtionViolationCount() {
		statistic.increaseAssumptionViolationCount();
    }
	
	public void handleBadInputFailures() {
		increateBadInputCount();
	}
	
	protected void increateBadInputCount() {
		statistic.increaseBadInputCount();
	}

	public void handleFailures(Throwable exception, String methodName, Object...params) throws Throwable {
		increaseFailureCount();
		counterExample = CounterExample.build(params);
		shrinkHandler.shrink(counterExample);
		throwParameterizedAssertionFailure(exception, methodName, params);
	}
	
	protected void increaseFailureCount() {
    	statistic.increaseFailureCount();
    }
	
	protected void throwParameterizedAssertionFailure(Throwable e, String methodName, Object... params) throws Throwable {
		if (params.length == 0) {
			throw e;
		}
		throw new ParameterizedAssertionFailure(e, methodName, params);
	}

	public List<AssumptionViolatedException> getInvalidParameters() {
		return invalidParameters;
	}
	
	public CounterExample getCounterExample() {
		return counterExample;
	}
	
	public Statistic getStatistic() {
		return statistic;
	}
	
	public boolean isTestNeverSucceed() {
		return statistic.getSuccessCount() == 0? true : false;
	}
}
