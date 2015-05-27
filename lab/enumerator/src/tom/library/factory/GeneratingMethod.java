package tom.library.factory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import tom.library.enumerator.Enumeration;

/**
 * This class wraps the java.lang.reflect.Method object It adds some
 * functionalities like getting the curried representation of the method
 * 
 * @author Ahmad
 * 
 */
public class GeneratingMethod extends GeneratorWrapper{

    /**
     * the wrapped method object it must be set on instantiation
     */
    private final Method method;
    
    /**
     * name of enumeration that represent instance of the enumerated type
     */
    private final String thisEnumName;
    
    /**
     * instantiating GeneratingMethod to wrap a method annotated with @Enumerate
     * 
     * @param method
     *            The method object to wrap
     * @param index
     *            The index of the method in the methods list of the class
     * @param declaringClass
     *            The parsed class containing this method
     */
    public GeneratingMethod(Method method, int index, ParsedClass declaringClass) {
        this.method = method;
        this.declaringClass = declaringClass;
        this.variableName = "_method" +index+"_"+ declaringClass.getSimpleName() + "_" + method.getName();
        this.enumName = this.variableName + "Enum";
        this.thisEnumName = this.variableName + "ThisEnum";
        this.enumerateAnnotation = method.getAnnotation(Enumerate.class);
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
     * getter method for name of the enumeration used for "this" instance
     * 
     */
    public String getThisEnumName() {
        return thisEnumName;
    }

    /**
     * {@inheritDoc}
     */
    public String getCurriedType(int index) {

        StringBuilder curriedType = new StringBuilder();
        
        if (index == -1) {
            // first call, prepend enumeration for "this"
            curriedType.append("F<");
            curriedType.append(this.declaringClass.getSimpleName());
            curriedType.append(", ");
            curriedType.append(this.getCurriedType(index + 1));
            curriedType.append(">");
        } else if (this.parameters.size() == 0) {
            // a method with no input parameters
            curriedType.append(this.declaringClass.getSimpleName());
        } else if (index == this.parameters.size() - 1) {
            // last input parameter, so append the output (the enumerated Type)
            curriedType.append("F<");
            curriedType.append(this.parameters.get(index).getType());
            curriedType.append(", ");
            curriedType.append(this.declaringClass.getSimpleName());
            curriedType.append(">");
        } else {
            // partial application, append the currying of the output (a new function)
            curriedType.append("F<");
            curriedType.append(this.parameters.get(index).getType());
            curriedType.append(", ");
            curriedType.append(this.getCurriedType(index + 1));
            curriedType.append(">");
        }
        
        return curriedType.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String getCurriedDefinition(int index) {

        StringBuilder curriedDef = new StringBuilder();
        if (index == -1 && this.parameters.size() == 0) {
            curriedDef.append("public ");
            curriedDef.append(this.getCurriedType(index + 1));
            curriedDef.append(" apply(final ");
            curriedDef.append(this.declaringClass.getSimpleName());
            curriedDef.append(" instance) { return (");
            curriedDef.append(this.declaringClass.getSimpleName());
            curriedDef.append(") instance.");
            curriedDef.append(this.method.getName());
            curriedDef.append("(); }");
        } else if (index == -1 && this.parameters.size() > 0) {
            curriedDef.append("public ");
            curriedDef.append(this.getCurriedType(index + 1));
            curriedDef.append(" apply(final ");
            curriedDef.append(this.declaringClass.getSimpleName());
            curriedDef.append(" instance) { return new ");
            curriedDef.append(this.getCurriedType(index + 1));
            curriedDef.append("() {");
            curriedDef.append(this.getCurriedDefinition(index + 1));
            curriedDef.append(" }; }");
        } else if (index == this.parameters.size() - 1) {
            // last parameter, apply the method with all the parameters
            curriedDef.append("public ");
            curriedDef.append(this.declaringClass.getSimpleName());
            curriedDef.append(" apply(final ");
            curriedDef.append(this.parameters.get(index).getType());
            curriedDef.append(" ");
            curriedDef.append(this.parameters.get(index).getName());
            curriedDef.append(") { return ");
            curriedDef.append("("+this.declaringClass.getSimpleName()+") instance.");
            curriedDef.append(this.method.getName());
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

        String applyExpr = "Enumeration.apply(Enumeration.singleton(" + this.variableName + "), "+ this.thisEnumName +")";

        for (ParamWrapper param: parameters) {
            applyExpr = "Enumeration.apply(" + applyExpr + ", " + param.getName() + "Enum)";
        }

        return applyExpr+".pay()";
    }

}
