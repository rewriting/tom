package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;
import examples.tomchecktest.treenat.Fork;
import examples.tomchecktest.treenat.Leaf;
import examples.tomchecktest.treenat.Nat;
import examples.tomchecktest.treenat.Suc;
import examples.tomchecktest.treenat.Tree;

@RunWith(PropCheck.class)
public class TomCheckTest {

	@Enum public static Enumeration<Tree<Nat>> treeEnum = Tree.getEnumeration(Nat.getEnumeration());

	@Enum public static Enumeration<String> stringEnum = Combinators.makeString();

	@Enum public static Enumeration<Integer> intEnum = Combinators.makeInteger();

	// simple function to test theories
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
	public void testTom1(@ForSome(maxSampleSize = 20, numberOfSamples = 30) Tree<Nat> t) {
		System.out.println("testTom1: " + t);
	}

	//@Theory
	public void testTom2(@ForSome(exhaustive=true, maxSampleSize = 15, numberOfSamples = 200) Tree<Nat> t) {
		System.out.println("testTom2: " + t);
	}
	
	//@Theory
	public void testPlusOne1(@ForSome(exhaustive=true, maxSampleSize = 20) Tree<Nat> t) {
		// just test if the size is preserved by plusOne
		System.out.println("testPlusOne1: " + t);
		assertEquals(plusOne(t).size(), t.size());
	}

	//@Theory
	public void testPlusOne2(@ForSome(maxSampleSize = 15) Tree<Nat> t, @ForSome(maxSampleSize = 15) Tree<Nat> t2) {
		// just test if the size is preserved by plusOne
		//System.out.println(t + " | " + t.size());
		assumeTrue(t.size() > 2);
		assertEquals(plusOne(t).size(), t.size());
		assertEquals(plusOne(t2).size(), t2.size());
	}

	//@Theory
	public void testString(@ForSome(maxSampleSize = 50) String s) {
		System.out.println(s);
	}

	//@Theory
	public void testMulti(@ForSome(maxSampleSize = 10) String s,
			@ForSome(maxSampleSize = 5) Integer i) {
		System.out.println(s + " " + i);
	}
}