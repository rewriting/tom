package examples.theory;

import static org.junit.Assert.assertEquals;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;

@RunWith(TomCheck.class)
public class TheoryTest {

	@Enum
	public static Enumeration<Tree<Nat>> treeEnum = TreeEnumerations.makeTreeNatEnumeration();

	@Enum	
	public static Enumeration<String> stringEnum = Combinators.makeString();
	
	//simple function to test theories
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
	public void testPlusOne1(@ExhaustiveForAll(sampleSize=10) Tree<Nat> t) {
		// just test if the size is preserved by plusOne
		System.out.println(t);
		assertEquals(plusOne(t).size(), t.size());
	}
	
	@Theory
	public void testPlusOne2(@RandomForAll(maxDepth=10) Tree<Nat> t) {
		// just test if the size is preserved by plusOne	
		System.out.println(t);
		assertEquals(plusOne(t).size(), t.size());
	}
	

	//	@Theory
	//	public void testString(@ExhaustiveForAll(sampleSize=1000) String s) {
	//		System.out.println(s);
	//	}


	@Theory
	public void testStringQuickcheck(@ForAll(sampleSize=20) @From({MyGenerator.class}) int n) {
		System.out.println("Quick: "+n);
	}




}