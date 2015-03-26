package examples.factory;

/**
 * wraps a parameter object of a method 
 * @author Ahmad
 *
 */
public interface IParamWrapper {
	/**
	 * gets parameter type
	 * @return string representing the type of the parameter
	 */
	public String getType();
	
	/**
	 * generates the enumerate expression used to enumerate wrapped parameter 
	 * @return string enumerate expression
	 */
	public String enumerate();
}
