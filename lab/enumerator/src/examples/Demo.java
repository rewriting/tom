package examples;

import tom.library.enumerator.*;

import java.math.BigInteger;

public class Demo {
	public static void main(String args[]) {
		listOfBool();
		treeOfNatural();
		
		System.out.println("the end");
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
		final F2<Boolean,LList,LList> cons = 
				new F2<Boolean,LList,LList>() { public LList apply(Boolean head,LList tail) { return new Cons(head,tail); } };

		final Enumeration<LList> nilEnum = Enumeration.singleton((LList)new Nil());

		F<Enumeration<LList>,Enumeration<LList>> f = new F<Enumeration<LList>,Enumeration<LList>>() {
			public Enumeration<LList> apply(final Enumeration<LList> e) {
				return nilEnum.plus(consEnum(cons.curry(),boolEnum,e)).pay();
			}
		};
		
		Enumeration<LList> listEnum = Enumeration.fix(f);

		LazyList<Finite<LList>> parts = listEnum.parts();
		for(int i=0 ; i<5 && !parts.isEmpty() ; i++) {
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
		Enumeration<F<Boolean,F<LList,LList>>> singletonCons = Enumeration.singleton(cons);
		Enumeration<F<LList,LList>> singletonConsBoolEnum = Enumeration.apply(singletonCons,boolEnum);
		Enumeration<LList> res = Enumeration.apply(singletonConsBoolEnum,e);
		return res;
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

		final Enumeration<Nat> zeroEnum = Enumeration.singleton((Nat)new Zero());
		
		F<Enumeration<Nat>,Enumeration<Nat>> sucEnum = new F<Enumeration<Nat>,Enumeration<Nat>>() {
			public Enumeration<Nat> apply(final Enumeration<Nat> e) {
				F<Nat,Nat> _suc = new F<Nat,Nat>() { public Nat apply(Nat x) { return new Suc(x); } };
				return zeroEnum.plus(sucEnumAux(_suc, e)).pay();
			}
		};
		
		final Enumeration<Nat> natEnum = Enumeration.fix(sucEnum);
		
		LazyList<Finite<Nat>> parts = natEnum.parts();
		for(int i=0;i<10 && !parts.isEmpty();i++) {
			System.out.println(i + " --> " + parts.head());
			parts=parts.tail();
		}
		
		F<Nat,Tree<Nat>> _leaf = new F<Nat,Tree<Nat>>() { public Tree<Nat> apply(Nat x) { return new Leaf<Nat>(x); } };
		final Enumeration<Tree<Nat>> leafEnum = Enumeration.apply(Enumeration.singleton(_leaf),natEnum);
		
		final F2<Tree<Nat>,Tree<Nat>,Tree<Nat>> _fork = new F2<Tree<Nat>,Tree<Nat>,Tree<Nat>>() { public Tree<Nat> apply(Tree<Nat> l, Tree<Nat> r) { return new Fork<Nat>(l,r); } };
		F<Enumeration<Tree<Nat>>,Enumeration<Tree<Nat>>> forkEnum = new F<Enumeration<Tree<Nat>>,Enumeration<Tree<Nat>>>() {
			public Enumeration<Tree<Nat>> apply(final Enumeration<Tree<Nat>> e) {
				return leafEnum.plus(Enumeration.apply(Enumeration.apply(Enumeration.singleton(_fork.curry()),e),e)).pay();
				//return leafEnum.plus(forkEnumAux(_fork.curry(), e, e)).pay();
			}
		};
		
		final Enumeration<Tree<Nat>> treeEnum = Enumeration.fix(forkEnum);

		LazyList<Finite<Tree<Nat>>> tparts = treeEnum.parts();
		for(int i=0;i<10 && !tparts.isEmpty();i++) {
			System.out.println(i + " --> " + tparts.head());
			tparts=tparts.tail();
		}
		
		BigInteger card50 = treeEnum.parts().index(BigInteger.valueOf(50)).getCard();
		System.out.println("#trees of size 50 = card(parts[50]) = " + card50);
		System.out.println("#trees of size 500 = card(parts[500]) = " + treeEnum.parts().index(BigInteger.valueOf(500)).getCard());

		for(int p=0 ; p<=1000 ; p=p+100) {
			BigInteger i = java.math.BigInteger.TEN.pow(p);
			System.out.println("10^" + p + "-th tree has size " + treeEnum.get(i).size());
		}
		
	}
	
	private static Enumeration<Nat> sucEnumAux(F<Nat,Nat> suc, Enumeration<Nat> e) {
		Enumeration<F<Nat,Nat>> singletonSuc = Enumeration.singleton(suc);
		Enumeration<Nat> res = Enumeration.apply(singletonSuc,e);
		return res;
	}
	
	private static Enumeration<Tree<Nat>> forkEnumAux(F<Tree<Nat>,F<Tree<Nat>,Tree<Nat>>> f, Enumeration<Tree<Nat>> e1, Enumeration<Tree<Nat>> e2) {
		return Enumeration.apply(Enumeration.apply(Enumeration.singleton(f),e1),e2);
	}
	
	private static abstract class Tree<A> {
		public abstract int size();
	}

	private static class Leaf<A> extends Tree<A> {
		A value;
		
		public Leaf(A t) {
			this.value=t;
		}
		
		public String toString() {
			return "Leaf(" + value + ")";
		}
		
		public int size() {
			return 0;
		}

	}

	private static class Fork<A> extends Tree<A> {
		private Tree<A> left;
		private Tree<A> right;

		public Fork(Tree<A> l, Tree<A> r) {
			this.left = l;
			this.right = r;
		}

		public String toString() {
			return "Fork(" + left + "," + right + ")";
		}
		
		public int size() {
			return 1 + left.size() + right.size();
		}
	}
	
	private static abstract class Nat {
		public abstract int toInt();
	}

	private static class Zero extends Nat {
		public String toString() {
			return "Zero";
		}
		
		public int toInt() { return 0; }
	}
	
	private static class Suc extends Nat {
		Nat p;
		
		public Suc(Nat t) {
			this.p=t;
		}
		
		public String toString() {
			return "Suc(" + p + ")";
		}

		public int toInt() { return 1+p.toInt(); }
	}
	

	
	
}
