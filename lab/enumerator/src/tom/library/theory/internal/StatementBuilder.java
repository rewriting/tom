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

import tom.library.theory.BadInputException;

/**
 * This class is where the behavior at runtime of a test is defined, i.e. 
 * how a test method is evaluate and what should be done if a test
 * is fail.
 * 
 * </br>
 * 
 * The {@code Statement} is built on top of the default {@code Statement}  
 * generated from {@code BlockJUnit4ClassRunner}'s {@code methodBlock()}. 
 * The default {@code Statement} includes the default behaviour such as
 * handles when a test expects an exception, a test with {@code @Before}
 * and {@code @After} and a test with rules.
 * 
 * @author nauval
 *
 */
public class StatementBuilder {
	private TestObject testObject;
	private ExecutionHandler handler;
	
	public StatementBuilder(TestObject testObject, ExecutionHandler handler) {
		this.testObject = testObject;
		this.handler = handler;
	}
	
	/**
	 * Returns a {@code Statement} from a given test method
	 * @param complete
	 * @return
	 * @throws InitializationError
	 */
	public Statement buildStatementForCompleteAssignment(final Assignments complete) throws InitializationError {
		return new BlockJUnit4ClassRunner(testObject.getTestClass().getJavaClass()) {
            @Override
            protected void collectInitializationErrors(List<Throwable> errors) {
                // do nothing
            }

            @Override
            public Statement methodBlock(final FrameworkMethod method) {
                final Statement statement = super.methodBlock(method);
                
                return new Statement() {
                    @Override
                    public void evaluate() throws Throwable {
                        try {
                            statement.evaluate();
                            handler.handleSuccess();
                        } catch (AssumptionViolatedException e) {
                        	handler.handleAssumptionViolation(e);
                        } catch (BadInputException e) {
                        	handler.handleBadInputFailures();
                        } catch (Throwable e) {
                        	handler.handleFailures(e, method.getName(), complete.getMethodArguments(nullsOk()));
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
	
	/**
	 * Returns a new {@code Statement} when it is evaluated it invokes
	 * the given {@code FrameworkMethod} from the given {@code Object} 
	 * instance and with values of the method's parameters extracted
	 * from the given {@code Assignments}.
	 *  
	 * @param method
	 * @param complete
	 * @param freshInstance
	 * @return
	 */
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
	        Theory annotation = testObject.getMethod().getAnnotation(Theory.class);
	        return annotation != null && annotation.nullsAccepted();
	    }
}
