package tom.library.factory;

import java.lang.reflect.ParameterizedType;

/**
 * wraps a List parameter
 * @author ahmad
 *
 */
public class ListWrapper extends ParamWrapper {

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
        System.out.println(((ParameterizedType)param.getGenericInterfaces()[0]).getActualTypeArguments()[0]);
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
        enumStatement.append("tom.library.factory.ListFactory.getEnumeration()");
        return enumStatement.toString();
    }

}
