package tom.library.factory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * represents a data model for a class that has been parsed by the Parser
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
	 */
	List<ConstructorWrapper> constructors = new ArrayList<ConstructorWrapper>();
	
	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	public void setCanonicalName(String canonicalName) {
		this.canonicalName = canonicalName;
	}
	
	public List<ConstructorWrapper> getConstructors() {
		return constructors;
	}
	
	public ConstructorWrapper getConstructor(int index) {
		return constructors.get(index);
	}

	public void setConstructors(List<ConstructorWrapper> constructors) {
		this.constructors = constructors;
	}
	public ConstructorWrapper getNoArgsConstructor() {
		return noArgsConstructor;
	}
	/**
	 * creates a wrapper for the no args constructor annotated with @EnumerateGenerator
	 * @param noArgsConstructor constructor to wrap
	 */
	public void setNoArgsConstructor(Constructor noArgsConstructor) {
		this.noArgsConstructor = new ConstructorWrapper(noArgsConstructor);
	}
	
	/**
	 * adds a constructor annotated with @EnumerateGenerator to the list after wrapping it in ConstructorWrapper
	 * the size of the list is also passed to the wrapper constructor to indicate the index of the constructor in the list
	 * @param cons Constructor to add
	 */
	public void addConstructor(Constructor cons) {
		this.constructors.add(new ConstructorWrapper(cons, constructors.size()));
	}
	
	/**
	 * returns the factory name 
	 * @return factory name of the factory class to be generated
	 */
	public String getFactoryClassName() {
		return this.simpleName+"Factory";
	}
	
	/**
	 * 
	 */
	public boolean hasNoArgsConstructor() {
		return this.noArgsConstructor != null;
	}
	
}
