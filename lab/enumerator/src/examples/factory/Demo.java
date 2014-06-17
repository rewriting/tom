package examples.factory;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;

public class Demo {
	public static void main(String[] args) {

		Enumeration<Integer> enumInt = Combinators.makeInteger();
		LazyList<Finite<Integer>> parts = enumInt.parts();
		for (int i = 0; i < 8 && !parts.isEmpty(); i++) {
			System.out.println(i + " --> " + parts.head());
			parts = parts.tail();
		}

		Enumeration<Tree<Integer>> enumTree = TreeFactory.getEnumeration(enumInt);
		LazyList<Finite<Tree<Integer>>> partsTree = enumTree.parts();
		for (int i = 0; i < 8 && !partsTree.isEmpty(); i++) {
			System.out.println(i + " --> " + partsTree.head());
			partsTree = partsTree.tail();
		}

	}
}
