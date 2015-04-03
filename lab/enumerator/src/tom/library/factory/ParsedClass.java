package tom.library.factory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * represents a data model for a class that has been parsed by the Parser
 * 
 * @author Ahmad
 * 
 */
public class ParsedClass {

    /**
     * class simple name
     */
    private String simpleName;

    /**
     * class canonical name (fully qualified)
     */
    private String canonicalName;

    /**
     * the no-arguments constructor of the class - if it exists -
     */
    private ConstructorWrapper noArgsConstructor;

    /**
     * list of class constructors that are annotated with @EnumerateGenerator and have parameters
     * and have parameters
     */
    private List<ConstructorWrapper> constructors = new ArrayList<ConstructorWrapper>();
    
    /**
     * constant representing line separator
     */
    public static final String ENDL = System.getProperty("line.separator");

    public <T> ParsedClass(Class<T> classToParse) {
        this.canonicalName = classToParse.getCanonicalName();
        this.simpleName = classToParse.getSimpleName();
	}

    public String getSimpleName() {
        return simpleName;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public List<ConstructorWrapper> getConstructors() {
        return constructors;
    }

    public ConstructorWrapper getConstructor(int index) {
        return constructors.get(index);
    }

    public ConstructorWrapper getNoArgsConstructor() {
    	return noArgsConstructor;
//    	if(hasNoArgsConstructor()){
//    		return this.constructors.get(0);
//    	}else{
//    		return null;
//    	}
    }

    /**
     * creates a wrapper for the no args constructor annotated with @EnumerateGenerator
     * 
     * @param noArgsConstructor
     *            constructor to wrap
     */
    public <T> void addNoArgsConstructor(Constructor<T> noArgsConstructor) {
        this.noArgsConstructor = new ConstructorWrapper(noArgsConstructor);
//        this.constructors.add(0,new ConstructorWrapper(noArgsConstructor, constructors.size()));
    }

    /**
     * adds a constructor annotated with @EnumerateGenerator to the list after
     * wrapping it in ConstructorWrapper the size of the list is also passed to
     * the wrapper constructor to indicate the index of the constructor in the
     * list
     * 
     * @param cons
     *            Constructor to add
     */
    public <T> void addConstructor(Constructor<T> cons) {
        this.constructors
                .add(new ConstructorWrapper(cons, constructors.size()));
    }

    /**
     * returns the factory name
     * 
     * @return factory name of the factory class to be generated
     */
    public String getFactoryClassName() {
        return this.simpleName + "Factory";
    }

    /**
	 * Whether this class has a no-args constructor or not
	 */
    public boolean hasNoArgsConstructor() {
        return this.noArgsConstructor != null;
//        return !this.constructors.isEmpty() && this.constructors.get(0).getParameters().size()==0;
    }
    
    /**
     * Concatenates all enumerations generated by all constructors for this class
     * i.e. generates the string noArgsConsEnum.plus(cons0Enum)....plus(consNEnum)
     * @return accumulated final enumeraion
     */
    public String getFinalEnum() {
        StringBuilder finalEnum = new StringBuilder();
        //TODO: find a cleaner way to specify first additional constructor
        int firstAdditionalCons = 0;
        if (hasNoArgsConstructor()) {
            finalEnum.append(this.getNoArgsConstructor().getEnumName());
        } else {
            finalEnum.append(this.getConstructor(0).getEnumName());
            firstAdditionalCons = 1;
        }
        // for every additional constructor add the corresponding generated enum
        for (int i = firstAdditionalCons; i<this.constructors.size(); i++) {
            finalEnum.append(".plus(");
            finalEnum.append(this.getConstructor(i).getEnumName());
            finalEnum.append(")");
        }
        return finalEnum.toString();
    }

}
