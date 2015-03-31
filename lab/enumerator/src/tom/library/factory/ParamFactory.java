package tom.library.factory;

import java.lang.annotation.Annotation;

/**
 * simple factory for ParamWrapper objects
 * generates the suitable wrapper type based in the parameter type
 * 
 * @author Ahmad
 *
 */
public class ParamFactory {
	
	/**
	 * create a wrapper object around the passed parameter
	 * @param param parameter to be wrapped
	 * @param paramIndex index of the parameter among the constructor parameters
	 * @param paramAnnotations 
	 * @return newly created ParamWrapper object
	 */
	public static ParamWrapper createParamWrapper(Class param, int paramIndex, Annotation[] paramAnnotations) {
	
		if (param.isPrimitive()) {
			return new PrimitiveWrapper(param, paramIndex, paramAnnotations);
		} else if (param.equals(String.class)) {
			return new StringWrapper(param, paramIndex, paramAnnotations);
		} else {
			return new ObjectWrapper(param, paramIndex, paramAnnotations);
		}
	}
}