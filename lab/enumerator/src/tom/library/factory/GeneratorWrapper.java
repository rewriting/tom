package tom.library.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class wraps a java.lang.reflect.Constructor or a java.lang.reflect.Mehod
 * that are annotated with @EnumerateGenerator
 * it adds functionalities like getting the curried representation of the generator (the method or the constructor)
 * 
 * @author Ahmad
 * 
 */
public abstract class GeneratorWrapper {

    /**
     * The parsed class containing this generator
     */
    protected ParsedClass declaringClass;
    
    /**
     * represents list of parameters of the generator
     */
    protected List<ParamWrapper> parameters;

    /**
     * name of the variable representing this generator in the generated
     * Factory
     */
    protected String variableName;

    /**
     * name of the enumeration generated from this generator
     */
    protected String enumName;
    
    /**
     * getter method for the declaring class
     * @return the parsed class which declared this constructor
     */
    public ParsedClass getDeclaringClass() {
        return declaringClass;
    }

    /**
     * getter for parameters
     * 
     * @return list of ParameterWrapper object corresponding to the constructor
     *         parameters
     */
    public List<ParamWrapper> getParameters() {
        return parameters;
    }

    /**
     * getter for variable name
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * getter for enum name
     * 
     * @return name of the enumeration generated from this constructor
     */
    public String getEnumName() {
        return enumName;
    }
    
    /**
     * Delegates the call to the underlying constructor or method 
     * @return an array of arrays that represent the annotations on the formal parameters of underlying method or constructor
     */
    public Annotation[][] getParameterAnnotations() {
        if (this instanceof GeneratingConstructor) {
            return ((GeneratingConstructor) this).getConstructor().getParameterAnnotations();
        } else {
            return ((GeneratingMethod) this).getMethod().getParameterAnnotations();
        }
    }
    
    /**
     * Delegates the call to the underlying constructor or method
     * @return an array of Type objects that represent the formal parameter types
     */
    public Type[] getGenericParameterTypes() {
        if (this instanceof GeneratingConstructor) {
            return ((GeneratingConstructor<?>) this).getConstructor().getGenericParameterTypes();
        } else {
            return ((GeneratingMethod) this).getMethod().getGenericParameterTypes();
        }
    }

    /**
     * generates a string representing the type of the constructor in curried
     * form i.e. F<arg1, F<arg2,...,F<argn, T>...> for a constructor with n
     * arguments that generates object T
     * 
     * @param index
     *            the starting point of currying. index 0 start from first
     *            argument (i.e. returns F<arg1, F<arg2,...,F<argn, T>...>)
     *            index 1 start from second argument(i.e. returns
     *            F<arg2,..,F<argn, T>..>) and so on...
     * @return the type of the constructor in curried form
     */
    public abstract String getCurriedType(int index);

    /**
     * generates the representation of curried constructor call
     * 
     * @param index
     *            the starting point of currying.
     * @return string representing curried function application of the
     *         constructor
     * @see getCurriedType(int index)
     */
    public abstract String getCurriedDefinition(int index);

    /**
     * generates the apply expression for applying the constructor
     * 
     * @return String represents the application statement of the constructor
     */
    public abstract String getEnumerationConstruction();

}
