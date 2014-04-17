package propcheck.shrink;

import java.lang.reflect.InvocationTargetException;

import propcheck.shrink.ShrinkException;

public final class ShrinkTools {

	/**
	 * Invokes the given {@code static} method from an instance.
	 *  
	 * @param instance the instance from which the method is invoked
	 * @param method the invoked static method name
	 * 
	 * @return the resulting object from method invocation
	 * 
	 * @throws ShrinkException
	 */
	public static Object invokeStaticMethod(Object instance, String method) throws ShrinkException {
		Object result = null;
		try {
			result = instance.getClass().getDeclaredMethod(method, new Class<?>[0]).invoke(new Object[0], new Object[0]);
		} catch (IllegalAccessException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		} catch (NoSuchMethodException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		} catch (SecurityException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		}
		return result;
	}

	/**
	 * Invokes the given {@code static} method from an instance's {@code Class<?>}.
	 *  
	 * @param instance the instance from which the method is invoked
	 * @param method the invoked static method name
	 * 
	 * @return the resulting object from method invocation
	 * 
	 * @throws ShrinkException
	 */
	public static Object invokeStaticMethod(Class<?> instance, String method) throws ShrinkException {
		Object result = null;
		try{
			result = instance.getDeclaredMethod(method, new Class<?>[0]).invoke(new Object[0], new Object[0]);
		} catch (IllegalAccessException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		} catch (NoSuchMethodException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		} catch (SecurityException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		}
		return result;
	}
	
	public static Object invokeStaticMethodFromSuperclass(Object instance, String method) throws ShrinkException {
		return invokeStaticMethod(instance.getClass().getSuperclass(), method);
	}

	public static Object invokeStaticMethodFromSuperclass(Class<?> instance, String method) throws ShrinkException {
		return invokeStaticMethod(instance.getSuperclass(), method);
	}
	/**
	 * Invokes the given method from an instance.
	 * 
	 * @param instance the instance from which the method is invoked
	 * @param method the invoked static method name
	 * @return the resulting object from method invocation
	 * @throws ShrinkException
	 */
	public static Object invokeMethod(Object instance, String method) throws ShrinkException {
		return invokeMethod(instance, method, new Class<?>[0], new Object[0]); 
	
	}

	public static Object invokeMethod(Object instance, String method, Class<?>[] paramType, Object[] paramVal) throws ShrinkException {
		Object result = null;
		try{
			result = instance.getClass().getDeclaredMethod(method, paramType).invoke(instance, paramVal);
		} catch (IllegalAccessException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		} catch (NoSuchMethodException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		} catch (SecurityException e) {
			throw new ShrinkException(e.getLocalizedMessage());
		}
		return result;
	}
}
