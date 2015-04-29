package tom.library.factory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * wraps a List parameter
 * @author ahmad
 *
 */
public class ListWrapper extends ParamWrapper {

    /**
     * Actual type of the List
     * e.g. for List<String> the genericType would be the StringWrapper that wraps java.lang.String
     * whereas for List<Student> the generic type would be ObjectWrapper that wraps a Student class
     */
    private ParamWrapper genericType;
    
    /**
     * construct the wrapper by calling the ParamWrapper constructor
     * 
     * @param param
     *            object to wrap
     * @param paramIndex
     *            index of the parameter among the constructor parameters
     * @param paramAnnotations
     */
    
    
    public ListWrapper(Class param, int paramIndex, ConstructorWrapper declaringCons) {
        super(param, paramIndex, declaringCons);
        ParameterizedType parameterizedType = (ParameterizedType) declaringCons.getConstructor().getGenericParameterTypes()[paramIndex];
        this.genericType = ParamFactory.createParamWrapper((Class) parameterizedType.getActualTypeArguments()[0], 0, declaringCons);
    }

    @Override
    public String getType() {
        return param.getCanonicalName()+"<"+genericType.getType()+">";
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
        enumStatement.append("tom.library.factory.ListFactory.getEnumeration(");
        enumStatement.append(genericType.enumCreate());
        enumStatement.append(")");
        return enumStatement.toString();
    }

}
