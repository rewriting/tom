package examples.theory;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.contrib.theories.DataPoint;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.enumerator.F2;

@RunWith(Theories.class)
public class ExhaustiveForAllTest {

	//	@DataPoint
	//	public static final Tree<Nat> value = treeOfNatural(1000);


	// TODO: rewrite later with Tom strategies
	public Tree<Nat> plusOne(Tree<Nat> v) {
		if (v instanceof Leaf) {
			Leaf<Nat> t = (Leaf<Nat>) v;
			Nat newval = new Suc(t.value);
			return new Leaf<Nat>(newval);
		} else {
			Fork<Nat> t = (Fork<Nat>) v;
			return new Fork<Nat>(plusOne(t.left), plusOne(t.right));
		}
	}

	@Theory
	public void testPlusOne(@ExhaustiveForAll(sampleSize=200, enumerationName = "TreeNat") Tree<Nat> t) {
		System.out.println(t);
		// just test if the size is preserved by plusOne	
		assertEquals(plusOne(t).size(), t.size());
	}
	
	@Theory
	public void testString(@ExhaustiveForAll(sampleSize=10, enumerationName = "String") String s) {
		System.out.println(s);
	}



}