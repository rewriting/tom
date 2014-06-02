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

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.theory.internal.AssignmentRunner;
import tom.library.theory.internal.ExecutionHandler;
import tom.library.theory.internal.TestObject;
import tom.library.theory.shrink.DefaultShrinkHandler;
import tom.library.theory.shrink.ShrinkHandler;

public final class PropCheck extends Theories {
	private static HashMap<Type, Enumeration<?>> map;

	public PropCheck(Class<?> klass) throws InitializationError {
		super(klass);
	}

	public static Enumeration<?> get(Type type) {
		return map.get(type);
	}

	@Override
	public void run(RunNotifier notifier) {
		PropCheck.initEnumerations(getTestClass().getJavaClass());
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

	/*
	 * code for Combinators.methodName()
	 */
	private static Enumeration<?> getCombinator(String methodName) {
		Class<?> c;
		try {
			//c = Class.forName("jtom.library.enumerator.Combinator");
			c = Combinators.class;
			//Object t = c.newInstance();
			Method[] allMethods = c.getDeclaredMethods();
			for (Method m : allMethods) {
				if(m.getName().equals(methodName)) {
					Enumeration<?> myEnum = (Enumeration<?>) m.invoke(null, null); // static + no parameter
					return myEnum;
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void initEnumerations(Class<?> testclass) {
		map = new HashMap<Type, Enumeration<?>>();
		
		map.put(int.class, getCombinator("makeint"));
		map.put(Integer.class, getCombinator("makeInteger"));
		map.put(String.class, getCombinator("makeString"));
		map.put(Boolean.class, getCombinator("makeBoolean"));
		map.put(Character.class, getCombinator("makeCharacter"));
		map.put(Long.class, getCombinator("makeLong"));
		map.put(long.class, getCombinator("makelong"));
		//map.put(Float.class, getCombinator("makeFloat"));
		//map.put(float.class, getCombinator("makefloat"));
				
		/*
		 * retrieve enumerations from @Enum annotations
		 */
		Field[] fields = testclass.getFields();
		for(Field field: fields) {
			if (field.getAnnotation(Enum.class) != null) {
				try {
					ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
					Type dataType = fieldType.getActualTypeArguments()[0];
					map.put(dataType, (Enumeration<?>) field.get(null));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		/*
		 * retrieve enumerations from type of parameters
		 */
		for (Method method : testclass.getMethods()) {
			if (method.getAnnotation(Theory.class) != null) {
				Class[] parameterTypes = method.getParameterTypes();

				Annotation[][] parameterAnnotations = method.getParameterAnnotations();

				for (int i = 0; i < parameterTypes.length; i++) {
					if (parameterAnnotations[i].length > 0) {
						Type key = parameterTypes[i];
						
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
            printGeneratedDataStatistic((TheoryAnchor) statement);
        }        
	}

	@Override
	// same as original code - just to be sure we use the inner class from TomCheck
    public Statement methodBlock(FrameworkMethod method) {
		return new TheoryAnchor(method, getTestClass());
    }
	
	// protected in BlockJUnit4ClasRunner (parent of Theories) but not overridden in Theories
	// same code as in BlockJUnit4ClasRunner
	protected boolean isIgnored(FrameworkMethod child) {
		return child.getAnnotation(Ignore.class) != null;
	}

	protected void printGeneratedDataStatistic(final TheoryAnchor anchor) {
		System.out.println(anchor.generateStatistic() + "\n");
	}

	public static class TheoryAnchor extends Statement {
		private TestObject testObject;
	    private ExecutionHandler handler;
	    
	    public TheoryAnchor(FrameworkMethod method, TestClass testClass) {
	       testObject = new TestObject(method, testClass);
	    }
	    
	    // TODO move to another class!!
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
	    	
	        if (handler.isTestNeverSucceed()) {
	            Assert.fail("Never found parameters that satisfied method assumptions or parameters are bad.\n"
	                    + "  Violated assumptions: " + handler.getInvalidParameters());
	        }
	    }

		private Assignments getUnassignedAssignments() throws Exception {
			return Assignments.allUnassigned(testObject.getMethod(),
					testObject.getTestClass());
		}
		
		public String generateStatistic() {
			return String.format("Testing %s().\n%s", 
					testObject.getMethodName(), 
					handler.getStatistic().generateStatistic());
		}
	}
}
