
package tom.library.shrink.tools;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import tom.library.enumerator.Enumeration;
import tom.library.sl.Visitable;

/**
 * Wrapper for term class where reflection calls are made.
 * 
 * @author nauval
 *
 */
public class TermClass {
	private static final String METHOD_GET_ENUMERATION = "getEnumeration";
	private Class<?> klass;
	
	private Enumeration<?> enumeration;
	
	private TermClass(Visitable term) {
		this.setKlass(term.getClass());
	}
	
	public static TermClass build(Visitable term) {
		return new TermClass(term);
	}
	
	/**
	 * Returns enumeration of the given class.
	 * @return enumeration
	 * @throws Exception 
	 */
	public Enumeration<?> getEnumerationFromClass() throws Exception {
		return (Enumeration<?>) MethodInvoker.invokeStaticMethodFromSuperclass(getKlass(), METHOD_GET_ENUMERATION);
	}
	
	public List<Visitable> getTerminalConstructor() {
		List<Visitable> results = new ArrayList<Visitable>();
		try {
			if (enumeration == null) {
				enumeration = getEnumerationFromClass();
			}
			results = TermClass.getTerminalConstructorFromEnumeration(enumeration);
		} catch (Exception e) {
			// do nothing, the return value should be an empty list
		}
		return results;
	}
	
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

	public Class<?> getKlass() {
		return klass;
	}

	public void setKlass(Class<?> klass) {
		this.klass = klass;
	}
}
