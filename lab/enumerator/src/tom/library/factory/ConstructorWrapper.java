package tom.library.factory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * This class wraps the java.lang.reflect.Constructor object It adds some
 * functionalities like getting the curried representation of the constructor
 * 
 * @author Ahmad
 * 
 */
public class ConstructorWrapper {

    /**
     * the wrapped constructor object it must be set on instantiation
     */
    private final Constructor constructor;

    /**
     * represents list of parameters of the constructor
     */
    private final List<ParamWrapper> parameters;

    /**
     * name of the variable representing this constructor in the generated
     * Factory
     */
    private String variableName;

    /**
     * name of the enumuration generated from that constructor
     */
    private String enumName;

    /**
     * instantiating ConstructorWrapper to wrap a constructor that have
     * parameters
     * 
     * @param cons
     *            The constructor object to wrap
     * @param index
     *            The index of the constructor in the constructor in the
     *            constructors list of the class
     */
    public ConstructorWrapper(Constructor cons, int index) {
        this.constructor = cons;
        this.variableName = "cons" + index;
        this.enumName = "cons" + index + "Enum";
        this.parameters = new ArrayList<ParamWrapper>();
        for (int i = 0; i < cons.getParameterTypes().length; i++) {
            this.parameters.add(ParamFactory.createParamWrapper(cons.getParameterTypes()[i],
                i,
                this));

        }
        this.variableName = "cons" + index;
        this.enumName = "cons" + index + "Enum";
    }

    /**
     * instantiating ConstructorWrapper to wrap a constructor that have no
     * parameters
     * 
     * @param noArgsConstructor
     *            constructor to wrap
     */
    public ConstructorWrapper(Constructor noArgsConstructor) {
        this.constructor = noArgsConstructor;
        this.parameters = null;
        this.enumName = "noArgsConsEnum";
    }

    /**
     * getter method for the underlying constructor
     * 
     * @return java.lang.reflect.Constructor
     */
    public Constructor getConstructor() {
        return constructor;
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
     * convenience method to get parameters count
     * 
     * @return number of parameters of constructor
     */
    public int getParamCount() {
        return this.constructor.getParameterTypes().length;
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
     * convenient method to get the type of index-th parameter of the wrapped
     * constructor
     * 
     * @param index
     *            the index of the parameter to fetch
     * @return canonical name of the type of parameter for primitives it returns
     *         corresponding wrapper
     */
    public String getParameterType(int index) {
        return this.parameters.get(index).getType();
    }

    /**
     * convenient method to get the name of index-th parameter of the wrapped
     * constructor
     * 
     * @param index
     *            the index of the parameter to fetch
     * @return name of the parameter
     */
    public String getParameterName(int index) {
        //return variableName + "_arg" + index;
        return this.parameters.get(index).getName();
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
    public String getCurriedType(int index) {

        StringBuilder curriedType = new StringBuilder();
        curriedType.append("F<");
        curriedType.append(this.getParameterType(index));
        curriedType.append(", ");
        if (index == this.getParamCount() - 1) {
            // last input parameter, so append the output (of type T)
            curriedType.append(this.getConstructor().getName());
        } else {
            // partial application, append the currying of the output (a new function)
            curriedType.append(this.getCurriedType(index + 1));
        }
        curriedType.append(">");
        return curriedType.toString();
    }

    /**
     * generates the representation of curried constructor call
     * 
     * @param index
     *            the starting point of currying.
     * @return string representing curried function application of the
     *         constructor
     * @see getCurriedType(int index)
     */
    public String getCurriedDefinition(int index) {
        StringBuilder curriedDef = new StringBuilder();

        if (index == this.getParamCount() - 1) {
            // last parameter, apply the constructor with all the parameters
            curriedDef.append("public ");
            curriedDef.append(this.constructor.getName());
            curriedDef.append(" apply(final ");
            curriedDef.append(this.getParameterType(index));
            curriedDef.append(" ");
            curriedDef.append(this.getParameterName(index));
            curriedDef.append(") { return new ");
            curriedDef.append(this.constructor.getName());
            curriedDef.append("(");

            for (int i = 0; i < this.getParamCount() - 1; i++) {
                curriedDef.append(this.getParameterName(i));
                curriedDef.append(", ");
            }
            curriedDef.append(this.getParameterName(index)); // last param
            curriedDef.append("); }");
        } else {
            // partial application
            curriedDef.append("public ");
            curriedDef.append(this.getCurriedType(index + 1));
            curriedDef.append(" apply(final ");
            curriedDef.append(this.getParameterType(index));
            curriedDef.append(" ");
            curriedDef.append(this.getParameterName(index));
            curriedDef.append(") { return new ");
            curriedDef.append(this.getCurriedType(index + 1));
            curriedDef.append("() {");
            curriedDef.append(this.getCurriedDefinition(index + 1));
            curriedDef.append(" };");
        }
        return curriedDef.toString();

    }

    /**
     * generates the apply expression for applying the constructor
     * 
     * @return String represents the application statement of the constructor
     */
    public String apply() {

        String applyExpr = "Enumeration.singleton(" + this.variableName + ")";

        for (int i = 0; i < this.getParamCount(); i++) {
            applyExpr = "Enumeration.apply(" + applyExpr + ", " + this.getParameterName(i) + "Enum)";
        }

        return applyExpr;

    }

}
