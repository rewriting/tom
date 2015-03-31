package examples;

import java.math.BigInteger;

import tom.library.enumerator.*;

public class BinaryTree {

	public static void main(String args[]) {
		// examples for trees
		// TTree = Leaf() | Node(TTree,TTree)

		System.out.println("START");

		
		final Enumeration<TTree> leafEnum = Enumeration.singleton((TTree)new Leaf());
				
		F<Enumeration<TTree>, Enumeration<TTree>> f = new F<Enumeration<TTree>, Enumeration<TTree>>() {
			public Enumeration<TTree> apply(final Enumeration<TTree> e) {
				return leafEnum.plus(nodeEnum(getNodeFunction(), e).pay());
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
	 * t1 -> t2 -> Node(t1, t2)
	 */
	private static F<TTree, F<TTree, TTree>> getNodeFunction() {
		return new F<TTree, F<TTree, TTree>>() {
			public F<TTree, TTree> apply(final TTree t1) {
				return new F<TTree, TTree>() {
					public TTree apply(final TTree t2) {
						return new Node(t1, t2);
					}
				};
			}
		};
	}

	private static Enumeration<TTree> nodeEnum(F<TTree, F<TTree, TTree>> node, Enumeration<TTree> e) {
		Enumeration<F<TTree, F<TTree, TTree>>> singletonNode = Enumeration.singleton(node);
		Enumeration<F<TTree, TTree>> singletonNodeArg1 = Enumeration.apply(singletonNode, e);
		Enumeration<TTree> res = Enumeration.apply(singletonNodeArg1, e);
		return res;
	}

	// examples for trees
	// TTree = Node(String,TTree,TTree)
	private static abstract class TTree {
		public abstract int size();
	}

	private static class Leaf extends TTree {
		public Leaf() {
		}

		public String toString() {
			return "o";
		}

		public int size() {
			return 1;
		}
	}

	private static class Node extends TTree {

		private TTree t1, t2;

		public Node(TTree t1, TTree t2) {
			this.t1 = t1;
			this.t2 = t2;
		}

		public String toString() {
			if(t1==t2) {
				return  "(" + t1 + "=" + t2 + ")";
			} else {
				return  "(" + t1 + "/\\" + t2 + ")";
			}
		}

		public int size() {
			return 1 + t1.size() + t2.size();
		}
	}

	
}
