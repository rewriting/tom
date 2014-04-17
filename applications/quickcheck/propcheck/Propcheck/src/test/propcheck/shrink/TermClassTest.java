package test.propcheck.shrink;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.List;

import org.junit.Test;

import propcheck.shrink.ShrinkException;
import propcheck.shrink.TermClass;
import tom.library.enumerator.Enumeration;
import examples.lists.alist.types.AList;

public class TermClassTest {
	
	private static AList term = AList.fromString(
			"con(cs(-31),con(cs(1),con(cs(-168),con(cs(6),con(cs(-19),con(cs(-4),empty()))))))");
	
	private TermClass classUnderTest = TermClass.make(term);
	
	@Test
	public void testGetEnumeration() {
		Enumeration<AList> en = null;
		try {
			en = (Enumeration<AList>) classUnderTest.getEnumeration();
		} catch (ShrinkException e) {
			
		}
		assertTrue("enumeration is not null", en != null);
		assertTrue("enumeration has terms", en.get(BigInteger.ZERO) != null);
		assertTrue("term is sort of AList", en.get(BigInteger.ZERO) instanceof AList);
	}

	@Test
	public void testGetConstants() {
		try {
			List<AList> constants = (List<AList>) classUnderTest.getConstants();
			assertTrue("constants is not empty", !constants.isEmpty());
			for (AList constant : constants) {
				assertTrue("constant is sort of AList", constant instanceof AList);
			}
		} catch (ShrinkException e) {
			fail("exception cathed: " + e.getMessage());
		}
	}
}
