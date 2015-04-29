package tom.library.factory;

import java.lang.annotation.Annotation;

/**
 * wraps an object parameter
 * 
 * @author Ahmad
 */
public class ObjectWrapper extends ParamWrapper {

    /**
     * construct the wrapper by calling the ParamWrapper constructor
     * 
     * @param param
     *            object to wrap
     * @param paramIndex
     *            index of the parameter among the constructor parameters
     * @param paramAnnotations
     */
    public ObjectWrapper(Class param, int paramIndex, ConstructorWrapper declaringCons) {
        super(param, paramIndex, declaringCons);
//        this.declaringCons.getDeclaringClass().addDependency(this.getType());
        this.declaringCons.getDeclaringClass().addDependency(param);
    }

    @Override
    public String getType() {
        return param.getCanonicalName();
    }

    @Override
    public String enumDeclare() {
        StringBuilder enumStatement = new StringBuilder();
        enumStatement.append("final Enumeration<");
        enumStatement.append(this.getType());
        enumStatement.append("> ");
        enumStatement.append(paramName + "Enum");
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
