package tom.library.factory;

/**
 * wraps an object parameter
 * 
 * @author Ahmad
 */
public class ObjectParam extends ParamWrapper {

    /**
     * construct the wrapper by calling the ParamWrapper constructor
     * 
     * @param param
     *            object to wrap
     * @param paramIndex
     *            index of the parameter among the constructor parameters
     * @param paramAnnotations
     */
    public ObjectParam(Class param, int paramIndex, GeneratorWrapper declaringGenerator) {
        super(param, paramIndex, declaringGenerator);
        if (this.enumerateAnnotation != null && this.enumerateAnnotation.concreteClasses().length >0) {
            this.declaringGenerator.getDeclaringClass().addDependency(new EnumerableType(param, enumerateAnnotation.concreteClasses()));
        } else {
            this.declaringGenerator.getDeclaringClass().addDependency(new EnumerableType(param));
        }
        
    }

    @Override
    public String getType() {
        return param.getCanonicalName();
    }

    @Override
    public String enumDeclare() {
        StringBuilder enumStatement = new StringBuilder();
        enumStatement.append("Enumeration<");
        enumStatement.append(this.getType());
        enumStatement.append("> ");
        enumStatement.append(paramEnumName);
        return enumStatement.toString();
    }
    
    @Override
    public String enumCreate() {
        StringBuilder enumStatement = new StringBuilder();
        enumStatement.append(this.getType() + "Factory"); //TODO: get corresponding factory from FactoryGenerator (handle different packages)
        enumStatement.append(".getEnumeration()");
        return enumStatement.toString();
    }

}
