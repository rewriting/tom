package examples;

import tom.library.enumerator.*;

import java.math.BigInteger;

import examples.data.treenat.Nat;
import examples.data.treenat.Tree;

public class Demo {

	public static void main(String args[]) {
		listOfBool();
		treeOfNatural();
		System.out.println("END");
	}

	/**
	 * List of booleans
	 */
	public static void listOfBool() {
		// examples for booleans
		Enumeration<Boolean> trueEnum = Enumeration.singleton(true);
		Enumeration<Boolean> falseEnum = Enumeration.singleton(false);
		final Enumeration<Boolean> boolEnum = trueEnum.plus(falseEnum).pay();
		//System.out.println(boolEnum.get(0));

		// examples for list of booleans
		final F2<Boolean,LList,LList> cons = new F2<Boolean,LList,LList>() {
			public LList apply(Boolean head,LList tail) { return new Cons(head,tail); } 
		};

		final Enumeration<LList> nilEnum = Enumeration.singleton((LList)new Nil());

		F<Enumeration<LList>,Enumeration<LList>> f = new F<Enumeration<LList>,Enumeration<LList>>() {
			public Enumeration<LList> apply(final Enumeration<LList> e) {
				//return nilEnum.plus(consEnum(cons.curry(),boolEnum,e)).pay();
				return consEnum(cons.curry(),boolEnum,e).plus(nilEnum).pay();
			}
		};

		Enumeration<LList> listEnum = Enumeration.fix(f);

		LazyList<Finite<LList>> parts = listEnum.parts();
		for(int i=0 ; i<8 && !parts.isEmpty() ; i++) {
			System.out.println(i + " --> " + parts.head());
			parts=parts.tail();
		}

		//for(BigInteger i=Integer.MAX_VALUE-1 ; i<= Integer.MAX_VALUE ; i=i+10000) {
		//	System.out.println(i + " --> " + listEnum.get(i));
		//}
		/*
		for(int p=0 ; p<10000 ; p=p+100) {
			BigInteger i = java.math.BigInteger.TEN.pow(p);
			System.out.println("10^" + p + " --> " + listEnum.get(i).size());
		}
		 */
		//System.out.println(listEnum.pay().get(0));
		//listEnum.pay().get(0);
	}

	private static Enumeration<LList> consEnum(F<Boolean,F<LList,LList>> cons, Enumeration<Boolean> boolEnum, Enumeration<LList> e) {
		return Enumeration.apply(Enumeration.apply(Enumeration.singleton(cons),boolEnum),e);
	}


	private static abstract class LList {
		public abstract int size();
	}

	private static class Nil extends LList {
		public String toString() {
			return "nil";
		}
		public int size() {
			return 0;
		}
	}

	private static class Cons extends LList {
		private Boolean head;
		private LList tail;

		public Cons(Boolean h, LList t) {
			this.head = h;
			this.tail = t;
		}

		public String toString() {
			return head + ":" + tail;
		}

		public int size() {
			return 1 + tail.size();
		}
	}


	/**
	 * Tree of Nat
	 */

	public static void treeOfNatural() {

		LazyList<Finite<Nat>> parts = Nat.getEnumeration().parts();

		for(int i=0;i<10 && !parts.isEmpty();i++) {
			System.out.println(i + " --> " + parts.head());
			parts=parts.tail();
		}

		Enumeration<Tree<Nat>> treeEnum = Tree.getEnumeration(Nat.getEnumeration());
		LazyList<Finite<Tree<Nat>>> tparts = treeEnum.parts();

		for(int i=0;i<10 && !tparts.isEmpty();i++) {
			System.out.println(i + " --> " + tparts.head());
			tparts=tparts.tail();
		}

		BigInteger card50 = treeEnum.parts().index(BigInteger.valueOf(50)).getCard();
		System.out.println("#trees of size 50 = card(parts[50]) = " + card50);
		System.out.println("#trees of size 500 = card(parts[500]) = " + treeEnum.parts().index(BigInteger.valueOf(500)).getCard());

		int n = 650;
		BigInteger i = java.math.BigInteger.TEN.pow(n);
		Tree<Nat> t = treeEnum.get(i);
		System.out.println("10^" + n + "-th tree has size " + t.size());
		System.out.println("t = " + t);

	}

}
