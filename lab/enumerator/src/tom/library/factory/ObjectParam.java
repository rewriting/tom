package tom.library.factory;

import java.util.ArrayList;
import java.util.List;

/**
 * wraps an object parameter
 * 
 * @author Ahmad
 */
public class ObjectParam extends ParamWrapper {
    
    /**
     * construct the wrapper by calling the ParamWrapper constructor
     * 
     * @param param
     *            object to wrap
     * @param paramIndex
     *            index of the parameter among the constructor parameters
     * @param paramAnnotations
     */
    public ObjectParam(Class param, int paramIndex, GeneratorWrapper declaringGenerator) {
        super(param, paramIndex, declaringGenerator);
        detectRecursion();
        detectMutualRecursion();
        // handle dependencies
        EnumerableType dependency;
        ParsedClass declaringClass = this.declaringGenerator.getDeclaringClass();
        EnumerableType declaringType = declaringClass.getEnumerableType();
        if (isRecursive()) {
            declaringType.setDependencyType(DependencyType.RECURSIVE);
            declaringGenerator.setRecursiveEnumName(paramEnumName);
            return;
        } else if (isMutuallyRecursive()) {
            declaringType.setDependencyType(DependencyType.MUTUALLY_RECURSIVE);
            declaringType.setMutualRecTypes(listMutuallyRecursiveTypes());
            declaringGenerator.setRecursiveEnumName(paramEnumName);
            return;
        } else if (isSuperType()) {
            dependency = new EnumerableType(param, declaringType, enumerateAnnotation.concreteClasses());
        } else {
            dependency = new EnumerableType(param, declaringType);
        }
        declaringClass.addDependency(dependency);
    }
    
    @Override
    public String getType() {
        return param.getCanonicalName();
    }

    @Override
    public String enumDeclare() {
        StringBuilder enumStatement = new StringBuilder();
        enumStatement.append("Enumeration<");
        enumStatement.append(this.getType());
        enumStatement.append("> ");
        enumStatement.append(paramEnumName);
        return enumStatement.toString();
    }
    
    @Override
    public String enumCreate() {
        StringBuilder enumStatement = new StringBuilder();
        enumStatement.append(this.getType() + "Factory"); //TODO: might be better to lookup the corresponding factory name from FactoryGenerator
        enumStatement.append(".getEnumeration()");
        return enumStatement.toString();
    }
    
    private void detectRecursion() {
        EnumerableType currentType = declaringGenerator.getDeclaringClass().getEnumerableType();
        this.recursive = currentType.getCanonicalName().equals(param.getCanonicalName());
    }
    
    private boolean isSuperType() {
        return this.enumerateAnnotation != null && this.enumerateAnnotation.concreteClasses().length >0;
    }
    
    private void detectMutualRecursion() {
        EnumerableType parentType = declaringGenerator.getDeclaringClass().getEnumerableType().getReferencingType();
        while (parentType != null) {
            if (parentType.getCanonicalName().equals(param.getCanonicalName())) {
                this.mutuallyRecursive = true;
                return;
            }
            parentType = parentType.getReferencingType();
        }
    }
    
    private List<EnumerableType> listMutuallyRecursiveTypes() {
        EnumerableType parentType = declaringGenerator.getDeclaringClass().getEnumerableType().getReferencingType();
        List<EnumerableType> mutualRecTypes = new ArrayList<EnumerableType>();
        while (!parentType.getCanonicalName().equals(param.getCanonicalName())) {
            mutualRecTypes.add(parentType);
            parentType = parentType.getReferencingType();
        }
        mutualRecTypes.add(parentType);
        return mutualRecTypes;
    }

}
