/**
 * 
 */
package propcheck.shrink;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import propcheck.shrink.ShrinkTools;
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
	
	public static TermClass make(Visitable term) {
		return new TermClass(term);
	}
	
	/**
	 * Returns enumeration of the given class.
	 * @return enumeration
	 * @throws ShrinkException 
	 */
	public Enumeration<?> getEnumeration() throws ShrinkException {
		return (Enumeration<?>) ShrinkTools.invokeStaticMethodFromSuperclass(getKlass(), METHOD_GET_ENUMERATION);
	}
	
	public List<? extends Visitable> getConstants() throws ShrinkException {
		if (enumeration == null) {
			enumeration = getEnumeration();
		}
		return getConstants(enumeration);
	}
	
	public List<? extends Visitable> getConstants(Enumeration<?> enumeration) {
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
