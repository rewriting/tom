package tom.library.factory;

import java.lang.annotation.Annotation;

/**
 * wraps a String parameter
 * 
 * @author Ahmad
 * 
 */
public class StringWrapper extends ParamWrapper {

    /**
     * construct the wrapper by calling the ParamWrapper constructor
     * 
     * @param param
     *            string to wrap
     * @param paramIndex
     *            index of the parameter among the constructor parameters
     * @param paramAnnotations
     */
    public StringWrapper(Class param, int paramIndex, ConstructorWrapper declaringCons) {
        super(param, paramIndex, declaringCons);
    }

    @Override
    public String getType() {
        return "String";
    }

    @Override
    public String enumerate() {
        StringBuilder enumStatement = new StringBuilder();
        enumStatement.append("final Enumeration<String> ");
        enumStatement.append(paramName + "Enum");
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
