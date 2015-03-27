package tom.library.factory;

/**
 * wraps a String parameter
 * @author Ahmad
 *
 */
public class StringWrapper extends ParamWrapper {
	
	/**
	 * construct the wrapper by calling the ParamWrapper constructor
	 * @param param
	 */
	public StringWrapper(Class param) {
		super(param);
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
