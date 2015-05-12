package tom.library.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
 * wraps a parameter of a method
 * 
 * @author Ahmad
 * 
 */
public abstract class ParamWrapper {

    /**
     * parameter to wrap
     */
    protected Class param;

    /**
     * represents the name of the parameter e.g. arg0, arg1 ...
     */
    protected String paramName;

    /**
     * the @Enumerate annotation on wrapped parameter
     */
    protected Enumerate enumerateAnnotation;
    
    /**
     * the constructor or method where this parameter was declared
     */
    protected GeneratorWrapper declaringGenerator;

    /**
     * initializes the wrapper
     * 
     * @param param
     *            parameter to wrap
     * @param paramIndex
     *            index of the parameter among the constructor parameters
     * @param declaringCons
     *            the constructor where this parameter was declared
     */
    public ParamWrapper(Class param, int paramIndex, GeneratorWrapper declaringGenerator) {
        this.param = param;
        this.declaringGenerator = declaringGenerator;
        this.paramName = declaringGenerator.getVariableName() +"_arg" + paramIndex;
        for (Annotation annotation : declaringGenerator.getParameterAnnotations()[paramIndex]) {
            if (annotation instanceof Enumerate) {
                this.enumerateAnnotation = (Enumerate) annotation;
            }
        }
    }
    
    /**
     * gets parameter name
     * 
     * @return string representing the name of the parameter
     */
    public String getName() {
        return this.paramName;
    }
    
    /**
     * gets parameter type
     * 
     * @return string representing the type of the parameter
     */
    public abstract String getType();
    
    /**
     * generates the left-hand side of enumerate expression used to enumerate wrapped parameter
     * 
     * @return string enumerate expression
     */
    public abstract String enumDeclare();
    
    /**
     * generates the right-hand side of enumerate expression used to enumerate wrapped parameter
     * 
     * @return string enumerate expression
     */
    public abstract String enumCreate();

}
