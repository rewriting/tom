package tom.library.theory.internal;

import java.lang.reflect.Method;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public final class TestObject {
	private final FrameworkMethod testMethod;
	private final TestClass testClass;

	public TestObject(FrameworkMethod testMethod, TestClass testClass) {
		this.testClass = testClass;
		this.testMethod = testMethod;
	}

	public FrameworkMethod getFrameworkMethod() {
		return testMethod;
	}

	public Method getMethod() {
		return testMethod.getMethod();
	}
	
	public String getMethodName() {
		return testMethod.getName();
	}
	
	public TestClass getTestClass() {
		return testClass;
	}
	
}
