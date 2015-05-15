package tom.library.factory;

import java.lang.annotation.Annotation;
import java.util.List;
import org.apache.commons.lang.ClassUtils;

/**
 * simple factory for ParamWrapper objects generates the suitable wrapper type
 * based in the parameter type
 * 
 * @author Ahmad
 * 
 */
public class ParamFactory {

    /**
     * create a wrapper object around the passed parameter
     * 
     * @param param
     *            parameter to be wrapped
     * @param paramIndex
     *            index of the parameter among the constructor parameters
     * @param paramAnnotations
     * @return newly created ParamWrapper object
     */
    public static ParamWrapper createParamWrapper(Class param, int paramIndex, GeneratorWrapper declaringGenerator) {

        if (param.isPrimitive()) {
            return new PrimitiveParam(param, paramIndex, declaringGenerator);
        } else if (ClassUtils.wrapperToPrimitive(param) != null) {
            return new PrimitiveParam(ClassUtils.wrapperToPrimitive(param), paramIndex, declaringGenerator);
        } else if (param.equals(String.class)) {
            return new StringParam(param, paramIndex, declaringGenerator);
        } else if (List.class.isAssignableFrom(param)) {
            return new ListParam(param, paramIndex, declaringGenerator);
        } else {
            return new ObjectParam(param, paramIndex, declaringGenerator);
        }
    }

}