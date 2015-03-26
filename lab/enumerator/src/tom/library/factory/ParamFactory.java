package tom.library.factory;

import java.lang.reflect.Parameter;

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
	 * @return newly created IParamWrapper object
	 */
	public static IParamWrapper createParamWrapper(Parameter param) {
	
		if (param.getType().isPrimitive()) {
			return new PrimitiveWrapper(param);
		} else if (param.getType().equals(String.class)) {
			return new StringWrapper(param);
		} else {
			throw new IllegalArgumentException("Unknown parameter type.");
		}
	}
}
