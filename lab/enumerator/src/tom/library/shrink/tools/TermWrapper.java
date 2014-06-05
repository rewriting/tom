
package tom.library.shrink.tools;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import tom.library.enumerator.Enumeration;
import tom.library.sl.Visitable;

/**
 * <p>
 * Wrapper for a term where reflection calls are made.
 * </p>
 * @author nauval
 *
 */
public class TermWrapper {
	private static final String METHOD_GET_ENUMERATION = "getEnumeration";
	private Class<?> termClass;
	
	private Enumeration<?> enumeration;
	
	private TermWrapper(Visitable term) {
		termClass = term.getClass();
	}
	
	/**
	 * Returns new {@code TermClass} instance
	 * 
	 * @param term
	 * @return new {@code TermClass} instance
	 */
	public static TermWrapper build(Visitable term) {
		return new TermWrapper(term);
	}
	
	/**
	 * <p>
	 * Returns the enumeration of the given class.
	 * </p>
	 * @return enumeration
	 * @throws Exception 
	 */
	public Enumeration<?> getEnumerationFromClass() throws Exception {
		return (Enumeration<?>) MethodInvoker.invokeStaticMethodFromSuperclass(getTermClass(), METHOD_GET_ENUMERATION);
	}
	
	/**
	 * <p>
	 * Returns a list of terminal constructors of the wrapped term
	 * </p>
	 * @return list of terminal constructors
	 */
	public List<Visitable> getTerminalConstructor() {
		List<Visitable> results = new ArrayList<Visitable>();
		try {
			if (enumeration == null) {
				enumeration = getEnumerationFromClass();
			}
			results = TermWrapper.getTerminalConstructorFromEnumeration(enumeration);
		} catch (Exception e) {
			// do nothing, the return value should be an empty list
		}
		return results;
	}
	
	/**
	 * <p>
	 * Returns a list of terminal constructors from the given enumeration object
	 * </p>
	 * @param enumeration
	 * @return
	 */
	public static List<Visitable> getTerminalConstructorFromEnumeration(Enumeration<?> enumeration) {
		List<Visitable> constants = new ArrayList<Visitable>();
		BigInteger card = enumeration.parts().head().getCard();
		BigInteger index = BigInteger.ZERO;
		while (card.compareTo(index) > 0) {
			constants.add((Visitable) enumeration.get(index));
			index = index.add(BigInteger.ONE);
		}
		return constants;
	}

	/**
	 * <p>
	 * Returns the object {@code Class} of the wrapped term
	 * </p>
	 * @return {@code Class} of the wrapped term
	 */
	public Class<?> getTermClass() {
		return termClass;
	}

	/**
	 * <p>
	 * Sets the {@code Class} of the wrapped term
	 * </p>
	 * @param termClass
	 */
	public void setTermClass(Class<?> termClass) {
		this.termClass = termClass;
	}
}
