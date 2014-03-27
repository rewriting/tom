package examples;

import examples.data.treenat.Fork;
import examples.data.treenat.Leaf;
import examples.data.treenat.Nat;
import examples.data.treenat.Suc;
import examples.data.treenat.Tree;
import examples.data.treenat.Zero;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.enumerator.F2;

public final class Enumerations {
	
	public static final Enumeration<Nat> makeNatEnumeration() {
		final Enumeration<Nat> zeroEnum = Enumeration.singleton((Nat)new Zero());

		F<Enumeration<Nat>,Enumeration<Nat>> sucEnum = new F<Enumeration<Nat>,Enumeration<Nat>>() {
			public Enumeration<Nat> apply(final Enumeration<Nat> e) {
				F<Nat,Nat> _suc = new F<Nat,Nat>() { public Nat apply(Nat x) { return new Suc(x); } };
				return zeroEnum.plus(sucEnumAux(_suc, e)).pay();
			}
		};
		return Enumeration.fix(sucEnum);
	}

	public static final Enumeration<Tree<Nat>> makeTreeNatEnumeration() {
		F<Nat,Tree<Nat>> _leaf = new F<Nat,Tree<Nat>>() { public Tree<Nat> apply(Nat x) { return new Leaf<Nat>(x); } };
		final Enumeration<Tree<Nat>> leafEnum = Enumeration.apply(Enumeration.singleton(_leaf),makeNatEnumeration());

		final F2<Tree<Nat>,Tree<Nat>,Tree<Nat>> _fork = new F2<Tree<Nat>,Tree<Nat>,Tree<Nat>>() { public Tree<Nat> apply(Tree<Nat> l, Tree<Nat> r) { return new Fork<Nat>(l,r); } };
		F<Enumeration<Tree<Nat>>,Enumeration<Tree<Nat>>> forkEnum = new F<Enumeration<Tree<Nat>>,Enumeration<Tree<Nat>>>() {
			public Enumeration<Tree<Nat>> apply(final Enumeration<Tree<Nat>> e) {
				return leafEnum.plus(Enumeration.apply(Enumeration.apply(Enumeration.singleton(_fork.curry()),e),e)).pay();
			}
		};
		return Enumeration.fix(forkEnum);
	}

	private static Enumeration<Nat> sucEnumAux(F<Nat,Nat> suc, Enumeration<Nat> e) {
		Enumeration<F<Nat,Nat>> singletonSuc = Enumeration.singleton(suc);
		Enumeration<Nat> res = Enumeration.apply(singletonSuc,e);
		return res;
	}

}
