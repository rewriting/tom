package fr.irisa.cairn.ecore.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A Switch that use reflexivity to find the right "case" function.<br>
 * <br>
 * For example if there is a case function for a <code>MyExample</code> class,
 * then searched function has following prototype
 * <code>T caseMyExample(MyExample o)</code>
 * 
 * @author antoine
 * 
 * @param <T>
 */
public abstract class ReflexiveSwitch<T> {

	@SuppressWarnings("unchecked")
	public T doSwitch(Object o) {
		Method method;
		try {
			method = this.getClass().getMethod(switchCallName(o),
					findObjectClass(o));
			Object result = method.invoke(this, o);
			return (T) result;
		} catch (NoSuchMethodException e) {
			return defaultCase(o);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public T defaultCase(Object object) {
		throw new UnsupportedOperationException("Unknown switch clause for: "
				+ object);
	}

	/**
	 * Get the name of the switch case function of an object.
	 * 
	 * @param o
	 * @return
	 */
	protected String switchCallName(Object o) {
		Class<? extends Object> objectClass = findObjectClass(o);
		return "case" + objectClass.getSimpleName();
	}

	private Class<? extends Object> findObjectClass(Object o) {
		Class<? extends Object> objectClass = o.getClass();
		if (objectClass.getInterfaces().length == 1)
			return objectClass.getInterfaces()[0];
		return objectClass;
	}
}
