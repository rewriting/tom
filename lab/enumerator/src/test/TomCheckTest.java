package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.*;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.ExhaustiveCheck;
import tom.library.theory.ExhaustiveForAll;
import tom.library.theory.RandomCheck;
import tom.library.theory.RandomForAll;
import tom.library.theory.TomCheck;
import tom.library.theory.TomForAll;
import examples.data.treenat.Fork;
import examples.data.treenat.Leaf;
import examples.data.treenat.Nat;
import examples.data.treenat.Suc;
import examples.data.treenat.Tree;

@RunWith(TomCheck.class)
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

	//@Theory
	public void testTom1(@TomForAll @RandomCheck(sampleSize = 10) Tree<Nat> t) {
		System.out.println(t);
	}

	//@Theory
	public void testTom2(@TomForAll @ExhaustiveCheck(maxDepth = 10) Tree<Nat> t) {
		System.out.println(t);
	}
	
	//@Theory
	public void testPlusOne1(@ExhaustiveForAll(maxDepth = 20) Tree<Nat> t) {
		// just test if the size is preserved by plusOne
		System.out.println(t);
		assertEquals(plusOne(t).size(), t.size());
	}

	@Theory
	public void testPlusOne2(@RandomForAll(sampleSize = 15) Tree<Nat> t, @RandomForAll(sampleSize = 15) Tree<Nat> t2) {
		// just test if the size is preserved by plusOne
		//System.out.println(t + " | " + t.size());
		assumeTrue(t.size() > 2);
		assertEquals(plusOne(t).size(), t.size());
		assertEquals(plusOne(t2).size(), t2.size());
	}

	//@Theory
	public void testString(@RandomForAll(sampleSize = 50) String s) {
		System.out.println(s);
	}

	@Theory
	public void testMulti(@RandomForAll(sampleSize = 10) String s,
			@RandomForAll(sampleSize = 5) Integer i) {
		System.out.println(s + " " + i);
	}
}