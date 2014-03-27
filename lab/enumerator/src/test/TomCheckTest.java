package test;

import static org.junit.Assert.assertEquals;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.ExhaustiveForAll;
import tom.library.theory.RandomForAll;
import tom.library.theory.TomCheck;
import examples.data.treenat.Fork;
import examples.data.treenat.Leaf;
import examples.data.treenat.Nat;
import examples.data.treenat.Suc;
import examples.data.treenat.Tree;

@RunWith(TomCheck.class)
public class TomCheckTest {

	@Enum
	public static Enumeration<Tree<Nat>> treeEnum = Tree.getEnumeration(Nat.getEnumeration());

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
	public void testPlusOne1(@ExhaustiveForAll(maxDepth=20) Tree<Nat> t) {
		// just test if the size is preserved by plusOne
		System.out.println(t);
		assertEquals(plusOne(t).size(), t.size());
	}
	
	@Theory
	public void testPlusOne2(@RandomForAll(sampleSize=20) Tree<Nat> t) {
		// just test if the size is preserved by plusOne	
		System.out.println(t);
		assertEquals(plusOne(t).size(), t.size());
	}
	

	@Theory
	public void testString(@RandomForAll(sampleSize=50) String s) {
			System.out.println(s);
	}




}