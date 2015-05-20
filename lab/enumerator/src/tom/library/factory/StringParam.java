package tom.library.factory;

/**
 * wraps a String parameter
 * 
 * @author Ahmad
 * 
 */
public class StringParam extends ParamWrapper {

    /**
     * construct the wrapper by calling the ParamWrapper constructor
     * 
     * @param param
     *            string to wrap
     * @param paramIndex
     *            index of the parameter among the constructor parameters
     * @param paramAnnotations
     */
    public StringParam(Class param, int paramIndex, GeneratorWrapper declaringGenerator) {
        super(param, paramIndex, declaringGenerator);
    }

    @Override
    public String getType() {
        return "String";
    }

    @Override
    public String enumDeclare() {
        StringBuilder enumStatement = new StringBuilder();
        enumStatement.append("Enumeration<String> ");
        enumStatement.append(paramEnumName);
        
        return enumStatement.toString();
    }
    
    @Override
    public String enumCreate() {
        StringBuilder enumStatement = new StringBuilder();
        enumStatement.append("new Enumeration<String>(Combinators.makeString().parts()");

        if (enumerateAnnotation != null) {
            enumStatement.append(".take(BigInteger.valueOf(");
            enumStatement.append(enumerateAnnotation.maxSize());
            enumStatement.append("))");
        }

        enumStatement.append(")");
        return enumStatement.toString();
    }

}
