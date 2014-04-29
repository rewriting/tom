package tom.library.theory.internal;

import java.util.List;

import org.junit.contrib.theories.Theory;
import org.junit.contrib.theories.PotentialAssignment.CouldNotGenerateValueException;
import org.junit.contrib.theories.internal.Assignments;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class StatementBuilder {
	private TestObject testObject;
	private ExecutionHandler handler;
	
	public StatementBuilder(TestObject testObject, ExecutionHandler handler) {
		this.testObject = testObject;
		this.handler = handler;
	}
	
	public Statement buildStatementForCompleteAssignment(final Assignments complete) throws InitializationError {
		return new BlockJUnit4ClassRunner(testObject.getTestClass().getJavaClass()) {
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
                            handler.handleSuccess();
                        } catch (AssumptionViolatedException e) {
                        	handler.handleAssumptionViolation(e);
                        } catch (Throwable e) {
                        	handler.handleFailures(e, complete.getMethodArguments(nullsOk()));
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
        }.methodBlock(testObject.getFrameworkMethod());
	}
	
	 protected Statement methodWithCompleteParameters(final FrameworkMethod method,
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
	 
	 private boolean nullsOk() {
	        Theory annotation = testObject.getFrameworkMethod().getMethod().getAnnotation(Theory.class);
	        return annotation != null && annotation.nullsAccepted();
	    }
}
