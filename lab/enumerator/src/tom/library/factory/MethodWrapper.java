package tom.library.factory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * This class wraps the java.lang.reflect.Method object It adds some
 * functionalities like getting the curried representation of the method
 * 
 * @author Ahmad
 * 
 */
public class MethodWrapper extends GeneratorWrapper{

    /**
     * the wrapped method object it must be set on instantiation
     */
    private final Method method;

    /**
     * instantiating MethodWrapper to wrap a method annotated with @Enumerate
     * 
     * @param method
     *            The method object to wrap
     * @param index
     *            The index of the method in the methods list of the class
     * @param declaringClass
     *            The parsed class containing this method
     */
    public MethodWrapper(Method method, int index, ParsedClass declaringClass) {
        this.method = method;
        this.declaringClass = declaringClass;
        this.variableName = "_method" +index+"_"+ method.getName();
        this.enumName = this.variableName + "Enum";
        this.parameters = new ArrayList<ParamWrapper>();
        for (int i = 0; i < method.getParameterTypes().length; i++) {
            this.parameters.add(ParamFactory.createParamWrapper(method.getParameterTypes()[i],
                i,
                this));
        }
    }

    /**
     * getter method for the underlying method
     * 
     * @return java.lang.reflect.Method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * {@inheritDoc}
     */
    public String getCurriedType(int index) {

        return "";
    }

    /**
     * {@inheritDoc}
     */
    public String getCurriedDefinition(int index) {

        return "";
    }

    /**
     * {@inheritDoc}
     */
    public String getEnumerationConstruction() {

        return "";
    }

}
