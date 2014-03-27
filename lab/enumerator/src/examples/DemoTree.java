package examples;

import java.math.BigInteger;
import tom.library.enumerator.*;

public class DemoTree {

	public static void main(String args[]) {
		// examples for trees
		// TTree = Leaf(A) | Node(A,TTree,TTree)
		// A = a | b

		System.out.println("START");

		Enumeration<A> aEnum = Enumeration.singleton((A) new aa());
		Enumeration<A> bEnum = Enumeration.singleton((A) new bb());
		final Enumeration<A> AEnum = aEnum.plus(bEnum);
		final Enumeration<String> StringEnum = Enumeration.singleton("f").plus(Enumeration.singleton("g"));

		System.out.println("Enumerator for " + "A");
		for (int i = 0; i < 2; i++) {
			System.out.println("Get " + i + "th term: " + AEnum.get(BigInteger.valueOf(i)));
		}

		F<Enumeration<TTree>, Enumeration<TTree>> f = new F<Enumeration<TTree>, Enumeration<TTree>>() {
			public Enumeration<TTree> apply(final Enumeration<TTree> e) {
				return leafEnum(getLeafFunction(), AEnum).plus(nodeEnum(getNodeFunction(), StringEnum, e).pay());
			}
		};

		Enumeration<TTree> treeEnum = Enumeration.fix(f);

		//listEnum.pay();
		System.out.println("Enumerator for " + "TTree");
		for (int i = 0; i < 20; i++) {
			System.out.println("Get " + i + "th term: " + treeEnum.get(BigInteger.valueOf(i)));
		}

		LazyList<Finite<TTree>> parts = treeEnum.parts();
		for (int i = 0; i < 3 && !parts.isEmpty(); i++) {
			System.out.println(i + " --> " + parts.head());
			parts = parts.tail();
		}

		for (int p = 0; p < 500; p = p + 100) {
			BigInteger i = java.math.BigInteger.TEN.pow(p);
			System.out.println("10^" + p + " --> " + treeEnum.get(i).size());
		}
		System.out.println("END");
	}

	/*
	 * elem -> Leaf(elem)
	 */
	private static F<A, TTree> getLeafFunction() {
		return new F<A, TTree>() {
			public TTree apply(final A elem) {
				return new Leaf(elem);
			}
		};
	}

	/*
	 * head -> t1 -> t2 -> Node(head, t1, t2)
	 */
	private static F<String, F<TTree, F<TTree, TTree>>> getNodeFunction() {
		return new F<String, F<TTree, F<TTree, TTree>>>() {
			public F<TTree, F<TTree, TTree>> apply(final String head) {
				return new F<TTree, F<TTree, TTree>>() {
					public F<TTree, TTree> apply(final TTree t1) {
						return new F<TTree, TTree>() {
							public TTree apply(final TTree t2) {
								return new Node(head, t1, t2);
							}
						};
					}
				};
			}
		};
	}

	private static Enumeration<TTree> leafEnum(F<A, TTree> leaf, Enumeration<A> AEnum) {
		Enumeration<F<A, TTree>> singletonLeaf = Enumeration.singleton(leaf);
		Enumeration<TTree> res = Enumeration.apply(singletonLeaf, AEnum);
		return res;
	}

	private static Enumeration<TTree> nodeEnum(F<String, F<TTree, F<TTree, TTree>>> node, Enumeration<String> AEnum, Enumeration<TTree> e) {
		Enumeration<F<String, F<TTree, F<TTree, TTree>>>> singletonNode = Enumeration.singleton(node);
		Enumeration<F<TTree, F<TTree, TTree>>> singletonNodeHeadEnum = Enumeration.apply(singletonNode, AEnum);
		Enumeration<F<TTree, TTree>> singletonNodeArg1 = Enumeration.apply(singletonNodeHeadEnum, e);
		Enumeration<TTree> res = Enumeration.apply(singletonNodeArg1, e);
		return res;
	}

	// examples for trees
	// TTree = Leaf(A) | Node(String,TTree,TTree)
	// A = a | b
	// String = "f" | "g"
	private static abstract class TTree {
		public abstract int size();
	}

	private static class Leaf extends TTree {

		private A elem;

		public Leaf(A e) {
			this.elem = e;
		}

		public String toString() {
			//return "[" + elem + "]";
			return elem.toString();
		}

		public int size() {
			return 1 + elem.size();
		}
	}

	private static class Node extends TTree {

		private String head;
		private TTree t1, t2;

		public Node(String h, TTree t1, TTree t2) {
			this.head = h;
			this.t1 = t1;
			this.t2 = t2;
		}

		public String toString() {
			return head + "(" + t1 + "," + t2 + ")";
		}

		public int size() {
			return 1 + t1.size() + t2.size();
		}
	}

	private static abstract class A {

		public abstract int size();
	}

	private static class aa extends A {

		public String toString() {
			return "a";
		}

		public int size() {
			return 0;
		}
	}

	private static class bb extends A {

		public String toString() {
			return "b";
		}

		public int size() {
			return 0;
		}
	}
}
