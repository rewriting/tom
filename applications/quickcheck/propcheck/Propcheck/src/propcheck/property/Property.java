package propcheck.property;

/**
 * Place where properties are defined. This interface only for a property
 * with one parameter
 * 
 * @author nauval
 *
 */
public interface Property<A> {
	
	/**
	 * Property is defined in here
	 * 
	 * @param a
	 * @return true if the property holds, otherwise false
	 */
	public void apply(A a);
}
