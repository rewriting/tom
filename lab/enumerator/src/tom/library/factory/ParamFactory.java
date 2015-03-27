package tom.library.factory;

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
	 * @return newly created ParamWrapper object
	 */
	public static ParamWrapper createParamWrapper(Class param) {
	
		if (param.isPrimitive()) {
			return new PrimitiveWrapper(param);
		} else if (param.equals(String.class)) {
			return new StringWrapper(param);
		} else {
			return new ObjectWrapper(param);
		}
	}
}