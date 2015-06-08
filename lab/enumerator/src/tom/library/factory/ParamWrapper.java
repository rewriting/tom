package tom.library.factory;

import java.lang.annotation.Annotation;

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
     * represents the name of the parameter 
     * e.g. _cons0_arg0, _method0_<methodname>_arg0, ...
     */
    protected String paramName;
    
    /**
     * represents the name of the enumeration generated for parameter 
     * e.g. _cons0_arg0Enum, _method0_<methodname>_arg0 ...
     */
    protected String paramEnumName;

    /**
     * the @Enumerate annotation on wrapped parameter
     */
    protected Enumerate enumerateAnnotation;
    
    /**
     * the constructor or method where this parameter was declared
     */
    protected GeneratorWrapper declaringGenerator;
    
    /**
     * flag for recursive parameters
     */
    protected boolean recursive;
    
    /**
     * flag for mutually-recursive parameters
     */
    protected boolean mutuallyRecursive;
    
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
        this.paramEnumName = paramName + "Enum";
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
     * gets a string representing the name of the enumeration of this parameter
     * 
     * @return parameter enumeration name
     */
    public String getEnumName() {
        return this.paramEnumName;
    }
    
    /**
     * returns Enumerate object represtenting the @Enumerate annotaion on this parameter
     * 
     * @return parameter annotation
     */
    public Enumerate getEnumerateAnnotation() {
        return this.enumerateAnnotation;
    }
    
    public boolean isRecursive() {
        return recursive;
    }
    
    public boolean isMutuallyRecursive() {
        return mutuallyRecursive;
    }
    
    public boolean isInRecChainOf(EnumerableType enumeableType) {
        if (param.getCanonicalName().equals(enumeableType.getCanonicalName())) {
            return true;
        } else {
            for (EnumerableType mutRecType : enumeableType.getMutualRecTypes()) {
                if (param.getCanonicalName().equals(mutRecType.getCanonicalName())) {
                    return true;
                }
            }
        }
        return false;
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
