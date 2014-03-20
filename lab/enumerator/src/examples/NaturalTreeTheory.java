package examples;

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
public class NaturalTreeTheory {

	public static abstract class Tree<A> {
		public abstract int size();
	}

	public static class Leaf<A> extends Tree<A> {
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

	public static class Fork<A> extends Tree<A> {
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


	public static abstract class Nat {
		public abstract int toInt();
	}

	public static class Zero extends Nat {
		public String toString() {
			return "Zero";
		}

		public int toInt() { return 0; }

	}

	public static class Suc extends Nat {
		Nat p;

		public Suc(Nat t) {
			this.p=t;
		}

		public String toString() {
			return "Suc(" + p + ")";
		}

		public int toInt() { return 1+p.toInt(); }
	}


	@DataPoint
	public static final Tree<Nat> value = treeOfNatural(1000);

	public static final Tree<Nat> treeOfNatural(int i) {
		final Enumeration<Nat> zeroEnum = Enumeration.singleton((Nat)new Zero());

		F<Enumeration<Nat>,Enumeration<Nat>> sucEnum = new F<Enumeration<Nat>,Enumeration<Nat>>() {
			public Enumeration<Nat> apply(final Enumeration<Nat> e) {
				F<Nat,Nat> _suc = new F<Nat,Nat>() { public Nat apply(Nat x) { return new Suc(x); } };
				return zeroEnum.plus(sucEnumAux(_suc, e)).pay();
			}
		};

		final Enumeration<Nat> natEnum = Enumeration.fix(sucEnum);

		F<Nat,Tree<Nat>> _leaf = new F<Nat,Tree<Nat>>() { public Tree<Nat> apply(Nat x) { return new Leaf<Nat>(x); } };
		final Enumeration<Tree<Nat>> leafEnum = Enumeration.apply(Enumeration.singleton(_leaf),natEnum);

		final F2<Tree<Nat>,Tree<Nat>,Tree<Nat>> _fork = new F2<Tree<Nat>,Tree<Nat>,Tree<Nat>>() { public Tree<Nat> apply(Tree<Nat> l, Tree<Nat> r) { return new Fork<Nat>(l,r); } };
		F<Enumeration<Tree<Nat>>,Enumeration<Tree<Nat>>> forkEnum = new F<Enumeration<Tree<Nat>>,Enumeration<Tree<Nat>>>() {
			public Enumeration<Tree<Nat>> apply(final Enumeration<Tree<Nat>> e) {
				return leafEnum.plus(Enumeration.apply(Enumeration.apply(Enumeration.singleton(_fork.curry()),e),e)).pay();
			}
		};
		final Enumeration<Tree<Nat>> treeEnum = Enumeration.fix(forkEnum);
		return treeEnum.get(BigInteger.valueOf(i));
	}


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
	public void testPlusOne(Tree<Nat> t) {
		System.out.println(t);
		// just test if the size is preserved by plusOne	
		assertEquals(plusOne(t).size(), t.size());
	}

	private static Enumeration<Nat> sucEnumAux(F<Nat,Nat> suc, Enumeration<Nat> e) {
		Enumeration<F<Nat,Nat>> singletonSuc = Enumeration.singleton(suc);
		Enumeration<Nat> res = Enumeration.apply(singletonSuc,e);
		return res;
	}


}