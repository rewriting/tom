package tom.library.theory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.contrib.theories.PotentialAssignment;
import org.junit.contrib.theories.PotentialAssignment.CouldNotGenerateValueException;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.contrib.theories.internal.Assignments;
import org.junit.contrib.theories.internal.ParameterizedAssertionError;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import tom.library.enumerator.Enumeration;
import tom.library.theory.internal.CounterExample;
import tom.library.theory.shrink.FirstStepShrink;

public final class TomCheck extends Theories {

	private static HashMap<Type, Enumeration<?>> enumerations;
	public TomCheck(Class<?> klass) throws InitializationError {
		super(klass);
	}

	public static Enumeration<?> get(Type type) {
		return enumerations.get(type);
	}

	@Override
	public void run(RunNotifier notifier) {
		TomCheck.initEnumerations(getTestClass().getJavaClass());
		super.run(notifier);
	}

	@Override
	protected void collectInitializationErrors(List<Throwable> errors) {
		validateEnumDecl(errors);
		super.collectInitializationErrors(errors);
	}

	private void validateEnumDecl(List<Throwable> errors) {
		Field[] fields = getTestClass().getJavaClass().getDeclaredFields();

		for (Field field : fields) {
			if (field.getAnnotation(Enum.class) == null) {
				continue;
			}
			if (!Modifier.isStatic(field.getModifiers())) {
				errors.add(new Error("Enum field " + field.getName() + " must be static"));
			}
			if (!Modifier.isPublic(field.getModifiers())) {
				errors.add(new Error("Enum field " + field.getName() + " must be public"));
			}
			if (! field.getType().equals(Enumeration.class)) {
				errors.add(new Error("Enum field " + field.getName() + " must be of type Enumeration<?>"));
			}
		}
	}



	public static void initEnumerations(Class<?> testclass) {
		enumerations = new HashMap<Type, Enumeration<?>>();
		Field[] fields = testclass.getFields();
		for(Field field: fields) {
			if (field.getAnnotation(Enum.class) != null) {
				try {
					ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
					Type dataType = fieldType.getActualTypeArguments()[0];
					enumerations.put(dataType, (Enumeration<?>) field.get(null));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		Statement statement = methodBlock(method);
        if (isIgnored(method)) {
            notifier.fireTestIgnored(description);
        } else {
            runLeaf(statement, description, notifier);
            printGeneratedDataStatistic(method, (TheoryAnchor) statement);
        }        
	}
	
	@Override
    public Statement methodBlock(FrameworkMethod method) {
        return new TheoryAnchor(method, getTestClass());
    }
	
	protected boolean isIgnored(FrameworkMethod child) {
		 return child.getAnnotation(Ignore.class) != null;
	}

	protected void printGeneratedDataStatistic(final FrameworkMethod method, final TheoryAnchor anchor) {
		System.out.println(String.format("%s\nGenerated test data: %s\nTested data: %s\nUntested data: %s\n", 
						method.getName(),
						getTotalGeneratedDataFromTheoryAnchor(anchor), 
						getTotalTestedDataFromTheoryAnchor(anchor),
						getTotalUntestedDataFromTheoryAnchor(anchor)));
	}

	protected int getTotalTestedDataFromTheoryAnchor(TheoryAnchor anchor) {
		return anchor.fSuccesses + anchor.fFailures;
	}

	protected int getTotalGeneratedDataFromTheoryAnchor(TheoryAnchor anchor) {
		return getTotalTestedDataFromTheoryAnchor(anchor) + anchor.fAssumtionViolated;
	}
	
	protected int getTotalUntestedDataFromTheoryAnchor(TheoryAnchor anchor) {
		return anchor.fAssumtionViolated;
	}
	
	public static class TheoryAnchor extends Statement {
	    private final FrameworkMethod fTestMethod;
	    private final TestClass fTestClass;
	    private final List<AssumptionViolatedException> fInvalidParameters =
	            new ArrayList<AssumptionViolatedException>();
	    private int fSuccesses = 0;
	    private int fAssumtionViolated = 0;
	    private int fFailures = 0;

	    public TheoryAnchor(FrameworkMethod method, TestClass testClass) {
	        fTestMethod = method;
	        fTestClass = testClass;
	    }

	    private TestClass getTestClass() {
	        return fTestClass;
	    }

	    @Override
	    public void evaluate() throws Throwable {
	        runWithAssignment(Assignments.allUnassigned(fTestMethod.getMethod(), getTestClass()));

	        if (fSuccesses == 0) {
	            Assert.fail("Never found parameters that satisfied method assumptions."
	                    + "  Violated assumptions: " + fInvalidParameters);
	        }
	    }

	    protected void runWithAssignment(Assignments parameterAssignment) throws Throwable {
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
	        statementForCompleteAssignment(complete).evaluate();
	    }
		
	    private Statement statementForCompleteAssignment(final Assignments complete)
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
	                            handleDataPointSuccess();
	                        } catch (AssumptionViolatedException e) {
	                        	increaseAssumtionViolationCounter();
	                            handleAssumptionViolation(e);
	                        } catch (Throwable e) {
	                        	increaseFailureCounter();
	                        	handleParameterizedFailure(e, complete.getMethodArguments(nullsOk()));
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

	    protected void increaseAssumtionViolationCounter() {
	    	fAssumtionViolated++;
	    }

	    protected void increaseFailureCounter() {
	    	fFailures++;
	    }
	    
	    protected void handleParameterizedFailure(Throwable e, Object... params) throws Throwable {
	    	doShrink(CounterExample.build(params));
		}

	    
	    // TODO rename method
	    protected void doShrink(CounterExample counterExamples) throws Throwable {
	    	FirstStepShrink.build(fTestMethod, fTestClass, counterExamples).evaluate();
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

	    protected void handleDataPointSuccess() {
	        fSuccesses++;
	    }
	}
}
