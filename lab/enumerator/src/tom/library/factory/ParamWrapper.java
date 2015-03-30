package tom.library.factory;

import java.lang.annotation.Annotation;

import tom.library.factory.Enumerate;

/**
 * wraps a parameter of a method 
 * @author Ahmad
 *
 */
public abstract class ParamWrapper {
	
	/**
	 * parameter to wrap
	 */
	protected Class param;
	
	/**
	 * represents the name of the parameter
	 * e.g. arg0, arg1 ...
	 */
	protected String paramName;
	
	/**
	 * the @Enumerate annotation on wrapped parameter
	 */
	protected Enumerate enumerateAnnotation;
		
	/**
	 * initializes the wrapper
	 * @param param parameter to wrap
	 * @param paramIndex index of the parameter among the constructor parameters
	 */
	public ParamWrapper(Class param, int paramIndex, Annotation[] paramAnnotations) {
		this.param = param;
		this.paramName = "arg"+paramIndex;
		for (Annotation annotation : paramAnnotations) {
			if (annotation instanceof Enumerate) {
				this.enumerateAnnotation = (Enumerate) annotation;
			}
		}
	}
	
	/**
	 * gets parameter type
	 * @return string representing the type of the parameter
	 */
	public abstract String getType();
	
	/**
	 * generates the enumerate expression used to enumerate wrapped parameter 
	 * @return string enumerate expression
	 */
	public abstract String enumerate();
	
}
