package tom.library.shrink.tools;

import java.lang.reflect.InvocationTargetException;


/**
 * <p>
 * A helper class to deal with reflective method calls.
 * </p>
 * @author nauval
 *
 */
public final class MethodInvoker {

	/**
	 * <p>
	 * Invokes the given {@code static} method from an instance.
	 * </p>
	 * @param instance the instance from which the method is invoked
	 * @param method the invoked static method name
	 * 
	 * @return the resulting object from method invocation
	 * 
	 * @throws Exception
	 */
	public static Object invokeStaticMethod(Object instance, String method) throws Exception {
		Object result = null;
		try {
			result = instance.getClass().getDeclaredMethod(method, new Class<?>[0]).invoke(new Object[0], new Object[0]);
		} catch (IllegalAccessException e) {
			throw new Exception(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			throw new Exception(e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			throw new Exception(e.getLocalizedMessage());
		} catch (NoSuchMethodException e) {
			throw new Exception(e.getLocalizedMessage());
		} catch (SecurityException e) {
			throw new Exception(e.getLocalizedMessage());
		}
		return result;
	}

	/**
	 * <p>
	 * Invokes the given {@code static} method from an instance's {@code Class<?>}.
	 * </p>
	 * @param instance the instance from which the method is invoked
	 * @param method the invoked static method name
	 * 
	 * @return the resulting object from method invocation
	 * 
	 * @throws Exception
	 */
	public static Object invokeStaticMethod(Class<?> instance, String method) throws Exception {
		Object result = null;
		try{
			result = instance.getDeclaredMethod(method, new Class<?>[0]).invoke(new Object[0], new Object[0]);
		} catch (IllegalAccessException e) {
			throw new Exception(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			throw new Exception(e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			throw new Exception(e.getLocalizedMessage());
		} catch (NoSuchMethodException e) {
			throw new Exception(e.getLocalizedMessage());
		} catch (SecurityException e) {
			throw new Exception(e.getLocalizedMessage());
		}
		return result;
	}
	
	public static Object invokeStaticMethodFromSuperclass(Object instance, String method) throws Exception {
		return invokeStaticMethod(instance.getClass().getSuperclass(), method);
	}

	public static Object invokeStaticMethodFromSuperclass(Class<?> instance, String method) throws Exception {
		return invokeStaticMethod(instance.getSuperclass(), method);
	}
	
	/**
	 * <p>
	 * Invokes the given method from an instance without parameters.
	 * </p>
	 * @param instance the instance from which the method is invoked
	 * @param method the invoked method name
	 * @return the resulting object from method invocation
	 * @throws Exception
	 */
	public static Object invokeMethod(Object instance, String method) throws Exception {
		return invokeMethod(instance, method, new Class<?>[0], new Object[0]); 
	
	}

	/**
	 * <p>
	 * Invokes the given method from the given instance using the given parameter types and values.
	 * </p>
	 * @param instance the instance from which the method is invoked
	 * @param method the method name
	 * @param paramType type of each parameters
	 * @param paramVal value of each parameters, accordingly to the type
	 * @return
	 * @throws Exception
	 */
	public static Object invokeMethod(Object instance, String method, Class<?>[] paramType, Object[] paramVal) throws Exception {
		Object result = null;
		try{
			result = instance.getClass().getDeclaredMethod(method, paramType).invoke(instance, paramVal);
		} catch (IllegalAccessException e) {
			throw new Exception(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			throw new Exception(e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			throw new Exception(e.getLocalizedMessage());
		} catch (NoSuchMethodException e) {
			throw new Exception(e.getLocalizedMessage());
		} catch (SecurityException e) {
			throw new Exception(e.getLocalizedMessage());
		}
		return result;
	}
}
