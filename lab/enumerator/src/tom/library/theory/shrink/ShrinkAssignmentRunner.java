package tom.library.theory.shrink;

import java.util.List;

import org.junit.contrib.theories.PotentialAssignment;
import org.junit.contrib.theories.internal.Assignments;
import org.junit.runners.model.Statement;

import tom.library.theory.internal.AssignmentRunner;
import tom.library.theory.internal.CounterExample;
import tom.library.theory.internal.ExecutionHandler;
import tom.library.theory.internal.StatementBuilder;
import tom.library.theory.internal.TestObject;
import tom.library.theory.shrink.suppliers.ShrinkParameterSupplier;

/**
 * Similar to {@code AssignmentRunner}, this class assigns values to test method's parameter.
 * However, the values are generated based on the given counter-example. The value generation
 * process is taken care of by an instance of {@code ShrinkParameterSupplier}.
 * 
 * @author nauval
 *
 */
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
	
	/**
	 * <p>
	 * Assigns test method's parameters with values and then runs the test methods 
	 * after the assignment is complete. The values are coming from the generation
	 * of counter-example candidates using the {@code Shrink} library.
	 * then 
	 * </p>
	 * @param parameterAssignment
	 * @param counterExample
	 * @throws Throwable
	 */
	public void runWithAssignment(Assignments parameterAssignment, CounterExample counterExample) throws Throwable {
		if (!parameterAssignment.isComplete()) {
            runWithIncompleteAssignment(parameterAssignment, counterExample);
        } else {
            runWithCompleteAssignment(parameterAssignment);
        }
	}
	
	/**
	 * <p>
	 * Retrieves the counter-example candidates for a parameter and then
	 * assign them to the parameter.
	 * </p>
	 * @param incomplete
	 * @param counterExample
	 * @throws Throwable
	 */
	public void runWithIncompleteAssignment(Assignments incomplete, CounterExample counterExample)
			throws Throwable {
		for (PotentialAssignment source : getValueSources(incomplete, counterExample)) {
            runWithAssignment(incomplete.assignNext(source), counterExample.nextCounterExample());
        }
	}

	/**
	 * Returns the counter-example candidates from a given counter-example.
	 * @param incomplete
	 * @param counterExample
	 * @return a list of {@code PotentialAssignment}
	 */
	private List<PotentialAssignment> getValueSources(Assignments incomplete,
			CounterExample counterExample) {
		return supplier.getValueSources(counterExample.getCounterExample());
	}
	
	/**
	 * Runs a test after all its parameters has been assigned with values.
	 * The test is wrapped in a {@code Statement} that later on is evaluated
	 * by the method.
	 * @param complete
	 * @throws Throwable
	 */
	protected void runWithCompleteAssignment(final Assignments complete) throws Throwable {
		StatementBuilder builder = new StatementBuilder(testObject, handler);
		Statement statement = builder.buildStatementForCompleteAssignment(complete);
		statement.evaluate();
    }

}
