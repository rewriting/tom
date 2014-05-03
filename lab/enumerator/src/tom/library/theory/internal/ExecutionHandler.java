package tom.library.theory.internal;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.AssumptionViolatedException;

import tom.library.theory.shrink.ShrinkHandler;

public class ExecutionHandler {
	private int successCount;
	private int assumptionViolationCount;
	private int failureCount;
	
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
	
	public void handleAssumptionViolation(AssumptionViolatedException exception) {
		invalidParameters.add(exception);
		increaseAssumtionViolationCount();
	}
	
	public void handleFailures(Throwable e, String methodName, Object...params) throws Throwable {
		increaseFailureCount();
		counterExample = CounterExample.build(params);
		shrinkHandler.shrink(counterExample);
		throwParameterizedAssertionFailure(e, methodName, params);
	}
	
	protected void throwParameterizedAssertionFailure(Throwable e, String methodName, Object... params) throws Throwable {
		if (params.length == 0) {
			throw e;
		}
		throw new ParameterizedAssertionFailure(e, methodName, params);
	}
	
	protected void increaseAssumtionViolationCount() {
		assumptionViolationCount++;
    }

    protected void increaseFailureCount() {
    	failureCount++;
    }
    
    protected void increaseSuccessCount() {
    	successCount++;
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
}
