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
        this.declaringCons.getDeclaringClass().addDependency(this.getType());
    }

    @Override
    public String getType() {
        return param.getCanonicalName();
    }

    @Override
    public String enumerate() {
        StringBuilder enumStatement = new StringBuilder();
        enumStatement.append("final Enumeration<");
        enumStatement.append(this.getType());
        enumStatement.append("> ");
        enumStatement.append(paramName + "Enum");
        enumStatement.append(" = ");
        enumStatement.append(this.getType() + "Factory");
        enumStatement.append(".getEnumeration()");
        return enumStatement.toString();
    }

}
