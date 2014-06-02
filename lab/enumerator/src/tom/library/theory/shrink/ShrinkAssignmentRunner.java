package tom.library.theory.shrink;

import java.util.List;

import org.junit.contrib.theories.PotentialAssignment;
import org.junit.contrib.theories.internal.Assignments;
import org.junit.runners.model.Statement;

import tom.library.theory.internal.CounterExample;
import tom.library.theory.internal.ExecutionHandler;
import tom.library.theory.internal.StatementBuilder;
import tom.library.theory.internal.TestObject;
import tom.library.theory.shrink.suppliers.ShrinkParameterSupplier;

public class ShrinkAssignmentRunner {
	private ShrinkParameterSupplier supplier;
	private TestObject testObject;
	private ExecutionHandler handler;
	
	
	public ShrinkAssignmentRunner(TestObject testObject,
			ExecutionHandler handler, ShrinkParameterSupplier supplier) {
		this.testObject = testObject;
		this.handler = handler;
		this.supplier = supplier;
	}
	
	public void runWithAssignment(Assignments parameterAssignment, CounterExample counterExample) throws Throwable {
		if (!parameterAssignment.isComplete()) {
            runWithIncompleteAssignment(parameterAssignment, counterExample);
        } else {
            runWithCompleteAssignment(parameterAssignment);
        }
	}
	
	public void runWithIncompleteAssignment(Assignments incomplete, CounterExample counterExample)
			throws Throwable {
		for (PotentialAssignment source : getValueSources(incomplete, counterExample)) {
            runWithAssignment(incomplete.assignNext(source), counterExample.nextCounterExample());
        }
	}

	private List<PotentialAssignment> getValueSources(Assignments incomplete,
			CounterExample counterExample) {
		return supplier.getValueSources(counterExample.getCounterExample());
	}
	
	protected void runWithCompleteAssignment(final Assignments complete) throws Throwable {
		StatementBuilder builder = new StatementBuilder(testObject, handler);
		Statement statement = builder.buildStatementForCompleteAssignment(complete);
		statement.evaluate();
    }

}
