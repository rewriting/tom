package examples.tomchecktest.treenat;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.enumerator.F2;

public abstract class Tree<A> {

	public abstract int size();

	public static final <T> Enumeration<Tree<T>> getEnumeration(
			Enumeration<T> enumeration) {
		F<T, Tree<T>> _leaf = new F<T, Tree<T>>() {
			public Tree<T> apply(T x) {
				return new Leaf<T>(x);
			}
		};
		final Enumeration<Tree<T>> leafEnum = Enumeration.apply(
				Enumeration.singleton(_leaf), enumeration);
		final F2<Tree<T>, Tree<T>, Tree<T>> _fork = new F2<Tree<T>, Tree<T>, Tree<T>>() {
			public Tree<T> apply(Tree<T> l, Tree<T> r) {
				return new Fork<T>(l, r);
			}
		};
		F<Enumeration<Tree<T>>, Enumeration<Tree<T>>> forkEnum = new F<Enumeration<Tree<T>>, Enumeration<Tree<T>>>() {
			public Enumeration<Tree<T>> apply(final Enumeration<Tree<T>> e) {
				return leafEnum.plus(
						Enumeration.apply(Enumeration.apply(
								Enumeration.singleton(_fork.curry()), e), e))
						.pay();
			}
		};
		return Enumeration.fix(forkEnum);
	}

}