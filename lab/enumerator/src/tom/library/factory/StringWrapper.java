package examples.factory;

import java.lang.reflect.Parameter;

import tom.library.factory.Enumerate;

/**
 * wraps a String parameter
 * @author Ahmad
 *
 */
public class StringWrapper implements IParamWrapper {
	/**
	 * parameter to wrap
	 */
	private Parameter param;
	
	/**
	 * the @Enumerate annotation on wrapped parameter
	 */
	private Enumerate enumerateAnnotation;
	
	/**
	 * initializes the wrapper
	 * @param param parameter to wrap
	 */
	public StringWrapper(Parameter param) {
		this.param = param;
		this.enumerateAnnotation = param.getAnnotation(Enumerate.class);
	}
	
	@Override
	public String getType() {
		return "String";
	}
	
	@Override
	public String enumerate() {
		StringBuilder enumStatement = new StringBuilder();
		enumStatement.append("final Enumeration<String> ");
		enumStatement.append(param.getName()+"Enum");
		enumStatement.append(" = new Enumeration<String>(Combinators.makeString().parts()");
		
		if (enumerateAnnotation != null) {
			enumStatement.append(".take(BigInteger.valueOf(");
			enumStatement.append(enumerateAnnotation.maxSize());
			enumStatement.append("))");
		}
		
		enumStatement.append(")");
		return enumStatement.toString();
	}

}
