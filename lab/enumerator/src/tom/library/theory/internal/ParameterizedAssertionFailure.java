package tom.library.theory.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class ParameterizedAssertionFailure extends AssertionError {
	private static final long serialVersionUID = 1L;

	public ParameterizedAssertionFailure(Throwable targetException, String methodName, Object... params) {
		super(buildDescription(targetException, methodName, params), targetException);
	}
	
	public ParameterizedAssertionFailure(Throwable targetException, String methodName, int shrunkCount, Object[] initialParams, Object... params) {
		super(buildDescriptionWithShrink(targetException, methodName, shrunkCount, initialParams, params));
	}

	public static String buildDescriptionWithShrink(Throwable targetException, String methodName, int shrunkCount, 
			Object[] initialCounterExamples, Object...params) {
		return String.format("\nError: %s%s%s", 
				getExceptionSimpleName(targetException),
				targetException.getMessage(), 
				buildDescriptionWithShrink(methodName, shrunkCount, initialCounterExamples, params));
	}
	
	private static String getExceptionSimpleName(Throwable exception) {
		return exception.getClass().getSimpleName() + ": ";
	}
	
	public static String buildDescriptionWithShrink(String methodName, int shrunkCount, 
			Object[] initialCounterExamples, Object...params) {
		return String.format("\n%s(%s)\nCounter example:\n%s\nShrunk %s times, counter example:\n%s", 
					methodName, join(", ", initialCounterExamples), join("\n", initialCounterExamples),
					shrunkCount, join("\n ", params));
	}
	
	public static String buildDescription(Throwable targetException, String methodName, Object...params) {
		return String.format("\n%s%s", targetException.getMessage(), buildDescription(methodName, params));
	}
	
	public static String buildDescription(String methodName, Object...params) {
		return String.format("\n%s(%s)\nCounter example:\n%s", methodName, join(", ", params), join("\n", params));
	}
	
	public static String join(String delimiter, Object... params) {
		return join(delimiter, Arrays.asList(params));
	}                                                                                                                                                                                                 

	public static String join(String delimiter, Collection<Object> values) {
		StringBuilder buffer = new StringBuilder();

		for (Iterator<Object> iter = values.iterator(); iter.hasNext(); ) {
			Object next = iter.next();
			buffer.append(stringValueOf(next));
			if (iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	private static String stringValueOf(Object next) {
		try {
			return String.valueOf(next);
		} catch (Throwable e) {
			return "[toString failed]";
		}
	}
}
