package tom.library.theory.internal;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.AssumptionViolatedException;

import tom.library.theory.shrink.ShrinkHandler;

public class ExecutionHandler {
	private int successCount;
	private int assumptionViolationCount;
	private int failureCount;
	private int badInputCount;
	
	private final List<AssumptionViolatedException> invalidParameters =
            new ArrayList<AssumptionViolatedException>();
	
	private ShrinkHandler shrinkHandler;
	
	private CounterExample counterExample;
	
	public ExecutionHandler(ShrinkHandler handler) {
		shrinkHandler = handler;
	}
	
	public void handleSuccess() {
		increaseSuccessCount();
	}
	
	protected void increaseSuccessCount() {
    	successCount++;
    }
	
	public void handleAssumptionViolation(AssumptionViolatedException exception) {
		invalidParameters.add(exception);
		increaseAssumtionViolationCount();
	}
	
	protected void increaseAssumtionViolationCount() {
		assumptionViolationCount++;
    }
	
	public void handleBadInputFailures() {
		increateBadInputCount();
	}
	
	protected void increateBadInputCount() {
		badInputCount++;
	}

	public void handleFailures(Throwable exception, String methodName, Object...params) throws Throwable {
		increaseFailureCount();
		counterExample = CounterExample.build(params);
		shrinkHandler.shrink(counterExample);
		throwParameterizedAssertionFailure(exception, methodName, params);
	}
	
	protected void increaseFailureCount() {
    	failureCount++;
    }
	
	protected void throwParameterizedAssertionFailure(Throwable e, String methodName, Object... params) throws Throwable {
		if (params.length == 0) {
			throw e;
		}
		throw new ParameterizedAssertionFailure(e, methodName, params);
	}

	public int getSuccessCount() {
		return successCount;
	}

	public int getAssumptionViolationCount() {
		return assumptionViolationCount;
	}


	public int getFailureCount() {
		return failureCount;
	}

	public List<AssumptionViolatedException> getInvalidParameters() {
		return invalidParameters;
	}
	
	public CounterExample getCounterExample() {
		return counterExample;
	}

	/**
	 * @return the badInputCount
	 */
	public int getBadInputCount() {
		return badInputCount;
	}

	/**
	 * @param badInputCount the badInputCount to set
	 */
	public void setBadInputCount(int badInputCount) {
		this.badInputCount = badInputCount;
	}
}
