package tom.library.theory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.contrib.theories.internal.Assignments;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import tom.library.enumerator.Enumeration;
import tom.library.theory.internal.AssignmentRunner;
import tom.library.theory.internal.ExecutionHandler;
import tom.library.theory.internal.TestObject;
import tom.library.theory.shrink.DefaultShrinkHandler;
import tom.library.theory.shrink.ShrinkHandler;

public final class TomCheck extends Theories {
	private static HashMap<Type, Enumeration<?>> map;

	public TomCheck(Class<?> klass) throws InitializationError {
		super(klass);
	}

	public static Enumeration<?> get(Type type) {
		return map.get(type);
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
				errors.add(new Error("Enum field " + field.getName()
						+ " must be static"));
			}
			if (!Modifier.isPublic(field.getModifiers())) {
				errors.add(new Error("Enum field " + field.getName()
						+ " must be public"));
			}
			if (!field.getType().equals(Enumeration.class)) {
				errors.add(new Error("Enum field " + field.getName()
						+ " must be of type Enumeration<?>"));
			}
		}
	}

	public static void initEnumerations(Class<?> testclass) {
		map = new HashMap<Type, Enumeration<?>>();
		for (Method method : testclass.getMethods()) {
			if (method.getAnnotation(Theory.class) != null) {
				Class[] parameterTypes = method.getParameterTypes();
				Type[] genericParameterTypes = method.getGenericParameterTypes();

				Annotation[][] parameterAnnotations = method.getParameterAnnotations();

				for (int i = 0; i < parameterTypes.length; i++) {
					if (parameterAnnotations[i].length > 0) {
						Type key = genericParameterTypes[i];
						
						//System.out.println(key + " ; " +  parameterTypes[i] + " ; " + parameterAnnotations[i][0]);
						
						for (Annotation annotation : parameterAnnotations[i]) {
							if (annotation.annotationType().equals(ForSome.class) && !map.containsKey(key)) {
								try {
									Method getEnumeration = parameterTypes[i].getMethod("getEnumeration", null);
									Enumeration<?> myEnum = (Enumeration<?>) getEnumeration.invoke(null, null); // static + no parameter
									map.put(key, myEnum);
								} catch (NoSuchMethodException | SecurityException | IllegalAccessException
										| IllegalArgumentException | InvocationTargetException e) {
									e.printStackTrace();
								}

							}
						}
						
					} else {
						// no annotation
					}
				}
				
				//System.out.println("print map " + map.keySet());

				

			}

		}
	}

	@Override
	// just for printing statistics
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
	// same as original code - just to be sure we use the inner class from
	// TomCheck
	public Statement methodBlock(FrameworkMethod method) {
		// get shrink annotation
		return new TheoryAnchor(method, getTestClass());
	}

	// protected in BlockJUnit4ClasRunner (parent of Theories) but not
	// overridden in Theories
	// same code as in BlockJUnit4ClasRunner
	protected boolean isIgnored(FrameworkMethod child) {
		return child.getAnnotation(Ignore.class) != null;
	}

	protected void printGeneratedDataStatistic(final FrameworkMethod method,
			final TheoryAnchor anchor) {
		System.out.println(String.format("%s\nGenerated test data: %s"
				+ "\nTested data: %s" + "\nUntested data: %s"
				+ "\nBad input: %s \n", method.getName(),
				getTotalGeneratedDataFromTheoryAnchor(anchor),
				getTotalTestedDataFromTheoryAnchor(anchor),
				getTotalUntestedDataFromTheoryAnchor(anchor),
				getTotalBadInputFromTheoryAnchor(anchor)));
	}

	protected int getTotalTestedDataFromTheoryAnchor(TheoryAnchor anchor) {
		return anchor.getSuccessCount() + anchor.getFailureCount();
	}

	protected int getTotalGeneratedDataFromTheoryAnchor(TheoryAnchor anchor) {
		return anchor.getTotalGeneratedData();
	}

	protected int getTotalUntestedDataFromTheoryAnchor(TheoryAnchor anchor) {
		return anchor.getViolationAssumptionCount();
	}

	protected int getTotalBadInputFromTheoryAnchor(TheoryAnchor anchor) {
		return anchor.getBadInputCount();
	}

	public static class TheoryAnchor extends Statement {
		private TestObject testObject;
		// private ShrinkHandler shrinkHandler;
		private ExecutionHandler handler;

		public TheoryAnchor(FrameworkMethod method, TestClass testClass) {
			testObject = new TestObject(method, testClass);
		}

		private ShrinkHandler newHandlerInstance() {
			ShrinkHandler handler = null;
			try {
				handler = getShrinkHandlerClass().getConstructor(
						TestObject.class).newInstance(testObject);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			return handler;
		}

		private Class<? extends ShrinkHandler> getShrinkHandlerClass() {
			Class<? extends ShrinkHandler> shrinkClass;
			if (testObject.getMethod().getAnnotation(Shrink.class) != null) {
				shrinkClass = testObject.getMethod()
						.getAnnotation(Shrink.class).handler();
			} else {
				shrinkClass = DefaultShrinkHandler.class;
			}
			return shrinkClass;
		}

		@Override
		public void evaluate() throws Throwable {
			handler = new ExecutionHandler(newHandlerInstance());
			AssignmentRunner runner = new AssignmentRunner(testObject, handler);
			runner.runWithAssignment(getUnassignedAssignments());

			if (handler.getSuccessCount() == 0) {
				Assert.fail("Never found parameters that satisfied method assumptions or parameters are bad.\n"
						+ "  Violated assumptions: "
						+ handler.getInvalidParameters());
			}
		}

		private Assignments getUnassignedAssignments() throws Exception {
			return Assignments.allUnassigned(testObject.getMethod(),
					testObject.getTestClass());
		}

		public int getSuccessCount() {
			return handler.getSuccessCount();
		}

		public int getViolationAssumptionCount() {
			return handler.getAssumptionViolationCount();
		}

		public int getFailureCount() {
			return handler.getFailureCount();
		}

		public int getBadInputCount() {
			return handler.getBadInputCount();
		}

		public int getTotalGeneratedData() {
			int total = handler.getSuccessCount();
			total += handler.getFailureCount();
			total += handler.getAssumptionViolationCount();
			total += handler.getBadInputCount();
			return total;
		}
	}
}
