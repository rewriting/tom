package tom.library.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

enum DependencyType {
    SIMPLE, SUPERTYPE, RECURSIVE, MUTUALLY_RECURSIVE
}

/**
 * encapsulates a class to be enumerated along with possibly empty set of subtypes of this class
 * after parsing these classes (typeClass and subTypes) all information is saved in corresponding pasedClass and parsedClasses
 * 
 * @author ahmad
 *
 */
public class EnumerableType {
    
    private final Class<?> typeClass;
    private final Set<Class<?>> subTypes = new HashSet<Class<?>>();
    private DependencyType dependencyType;
    private ParsedClass parsedClass;
    private Set<ParsedClass> parsedClasses = new HashSet<ParsedClass>();
    private EnumerableType referencingType;
    private List<EnumerableType> mutualRecTypes = new ArrayList<EnumerableType>();
    
    public EnumerableType(Class enumerableClass, EnumerableType declaringType) {
        this.dependencyType = DependencyType.SIMPLE;
        this.typeClass = enumerableClass;
        this.referencingType = declaringType;
    }

    public EnumerableType(Class enumerableClass, EnumerableType declaringType, Class<?>[] concreteClasses) {
        this.dependencyType = DependencyType.SUPERTYPE;
        this.typeClass = enumerableClass;
        this.referencingType = declaringType;
        Collections.addAll(subTypes, concreteClasses);
    }

    public Class<?> getTypeClass() {
        return typeClass;
    }
    
    public ParsedClass getParsedClass() {
        return parsedClass;
    }
    
    public DependencyType getDependencyType() {
        return dependencyType;
    }

    public Set<Class<?>> getSubTypes() {
        return subTypes;
    }
    
    public Set<ParsedClass> getParsedClasses() {
        return parsedClasses;
    }
    
    public EnumerableType getReferencingType() {
        return referencingType;
    }
    
    public List<EnumerableType> getMutualRecTypes() {
        return mutualRecTypes;
    }
    
    public void setReferencingType(EnumerableType referencingType) {
        this.referencingType = referencingType;
    }
    
    public void setDependencyType(DependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }
    
    public void setMutualRecTypes(List<EnumerableType> mutualRecTypes) {
        this.mutualRecTypes = mutualRecTypes;
    }

    public void parse() {
        this.parsedClass = parseClass(typeClass);
        for (Class<?> classToParse : subTypes) {
            ParsedClass parsedClass = parseClass(classToParse);
            this.parsedClasses.add(parsedClass);
        }
    }
    /**
     * Parses a class and stores parsed information into the ParsedClass
     * 
     * @param classToParse
     *            the class to be parsed
     * @return parsedClass object holding the parsed information (constructors,
     *         annotations...)
     */
    public ParsedClass parseClass(Class<?> classToParse) {
        ParsedClass parsedClass = new ParsedClass(classToParse, this);
        // load all constructors having @Enumerate annotations
        for (Constructor<?> cons : classToParse.getDeclaredConstructors()) {
            if (cons.isAnnotationPresent(Enumerate.class)) {
                if (cons.getParameterTypes().length == 0) {
                    // no args cons
                    parsedClass.addNoArgsConstructor(cons);
                } else {
                    // cons with parameters
                    parsedClass.addConstructor(cons);
                }

            }
        }
        // load all methods annotated as @Enumerate
        for (Method method : classToParse.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Enumerate.class)) {
                parsedClass.addMethod(method);

            }
        }
        return parsedClass;
    }
    /**
     * Returns a set of referenced types by this type and its subtypes
     * @return set of referenced types
     */
    public Set<EnumerableType> getDependencies() {
        Set<EnumerableType> dependencies = new HashSet<EnumerableType>();
        dependencies.addAll(parsedClass.getDependencies());
        for (ParsedClass parsedClass : parsedClasses) {
            dependencies.addAll(parsedClass.getDependencies());
        }
        return dependencies;
    }
    
    public String getPackageName() {
        return typeClass.getPackage().getName();
    }
    
    public String getFactoryName() {
        return typeClass.getSimpleName() + "Factory";
    }
    
    public String getSimpleName() {
        return typeClass.getSimpleName();
    }
    
    public String getCanonicalName() {
        return typeClass.getCanonicalName();
    }
    
    /**
     * For super types it accumulates all enumerations of all subtypes
     * otherwise it is just the enumeration of that type
     * @return the final enumeration for this Type
     */
    public String getFinalEnum() {
        StringBuilder finalEnum = new StringBuilder();
        if (dependencyType.equals(DependencyType.SIMPLE)) {
            return parsedClass.getFinalEnumName();
        } else if (dependencyType.equals(DependencyType.SUPERTYPE) && parsedClasses.size()>0) {
            Iterator<ParsedClass> it = parsedClasses.iterator();
            ParsedClass concreteClass = it.next();
            finalEnum.append(concreteClass.getFinalEnumName());
            while (it.hasNext()) {
                finalEnum.append(".plus(");
                finalEnum.append(it.next().getFinalEnumName());
                finalEnum.append(")");
            }
        }
        return finalEnum.toString();
    }
}
