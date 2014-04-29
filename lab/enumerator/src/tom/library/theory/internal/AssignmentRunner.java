package tom.library.theory.internal;

import org.junit.contrib.theories.PotentialAssignment;
import org.junit.contrib.theories.internal.Assignments;
import org.junit.runners.model.Statement;

public class AssignmentRunner {
	private TestObject testObject;
	private ExecutionHandler handler;

	public AssignmentRunner(TestObject testObject, ExecutionHandler handler) {
		this.testObject = testObject;
		this.handler = handler;
	}

	public void runWithAssignment(Assignments parameterAssignment) throws Throwable {
		if (!parameterAssignment.isComplete()) {
			runWithIncompleteAssignment(parameterAssignment);
		} else {
			runWithCompleteAssignment(parameterAssignment);
		}
	}

	protected void runWithIncompleteAssignment(Assignments incomplete) throws Throwable {
		for (PotentialAssignment source : incomplete.potentialsForNextUnassigned()) {
			runWithAssignment(incomplete.assignNext(source));
		}
	}

	protected void runWithCompleteAssignment(final Assignments complete) throws Throwable {
		StatementBuilder builder = new StatementBuilder(testObject, handler);
		Statement statement = builder.buildStatementForCompleteAssignment(complete);
		statement.evaluate();
	}
}
