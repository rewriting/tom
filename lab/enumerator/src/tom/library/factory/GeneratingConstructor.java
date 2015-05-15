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
public class GeneratingConstructor<T> extends GeneratorWrapper {

    /**
     * the wrapped constructor object it must be set on instantiation
     */
    private final Constructor<T> constructor;

    /**
     * instantiating GeneratingConstructor to wrap a constructor that have
     * parameters
     * 
     * @param cons
     *            The constructor object to wrap
     * @param index
     *            The index of the constructor in the constructor in the
     *            constructors list of the class
     * @param declaringClass
     *            The parsed class containing this constructor
     */
    public GeneratingConstructor(Constructor<T> cons, int index, ParsedClass declaringClass) {
        this.constructor = cons;
        this.declaringClass = declaringClass;
        this.variableName = "cons" + (index + 1);
        this.enumName = this.variableName + "Enum";
        this.parameters = new ArrayList<ParamWrapper>();
        for (int i = 0; i < cons.getParameterTypes().length; i++) {
            this.parameters.add(ParamFactory.createParamWrapper(cons.getParameterTypes()[i],
                i,
                this));
        }
    }

    /**
     * getter method for the underlying constructor
     * 
     * @return java.lang.reflect.Constructor
     */
    public Constructor<T> getConstructor() {
        return constructor;
    }

    /**
     * {@inheritDoc}
     */
    public String getCurriedType(int index) {

        StringBuilder curriedType = new StringBuilder();
        curriedType.append("F<");
        curriedType.append(this.parameters.get(index).getType());
        curriedType.append(", ");
        if (index == this.parameters.size() - 1) {
            // last input parameter, so append the output (of type T)
            curriedType.append(this.declaringClass.getSimpleName());
        } else {
            // partial application, append the currying of the output (a new function)
            curriedType.append(this.getCurriedType(index + 1));
        }
        curriedType.append(">");
        return curriedType.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String getCurriedDefinition(int index) {
        StringBuilder curriedDef = new StringBuilder();

        if (index == this.parameters.size() - 1) {
            // last parameter, apply the constructor with all the parameters
            curriedDef.append("public ");
            curriedDef.append(this.constructor.getName());
            curriedDef.append(" apply(final ");
            curriedDef.append(this.parameters.get(index).getType());
            curriedDef.append(" ");
            curriedDef.append(this.parameters.get(index).getName());
            curriedDef.append(") { return new ");
            curriedDef.append(this.constructor.getName());
            curriedDef.append("(");

            for (ParamWrapper param: parameters) {
                curriedDef.append(param.getName());
                curriedDef.append(", ");
            }
            curriedDef.replace(curriedDef.length()-2,curriedDef.length(),"); }");
        } else {
            // partial application
            curriedDef.append("public ");
            curriedDef.append(this.getCurriedType(index + 1));
            curriedDef.append(" apply(final ");
            curriedDef.append(this.parameters.get(index).getType());
            curriedDef.append(" ");
            curriedDef.append(this.parameters.get(index).getName());
            curriedDef.append(") { return new ");
            curriedDef.append(this.getCurriedType(index + 1));
            curriedDef.append("() {");
            curriedDef.append(this.getCurriedDefinition(index + 1));
            curriedDef.append(" }; }");
        }
        return curriedDef.toString();

    }

    /**
     * {@inheritDoc}
     */
    public String getEnumerationConstruction() {
        String applyExpr = "Enumeration.singleton(" + this.variableName + ")";

        for (ParamWrapper param: parameters) {
            applyExpr = "Enumeration.apply(" + applyExpr + ", " + param.getName() + "Enum)";
        }

        return applyExpr;

    }

}
