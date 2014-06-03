package examples.tomchecktest.treenat;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

public abstract class Nat {

	public abstract int toInt();

	public static final Enumeration<Nat> getEnumeration() {
		final Enumeration<Nat> zeroEnum = Enumeration.singleton((Nat) new Zero());

		F<Enumeration<Nat>, Enumeration<Nat>> sucEnum = new F<Enumeration<Nat>, Enumeration<Nat>>() {
			public Enumeration<Nat> apply(final Enumeration<Nat> e) {
				F<Nat, Nat> _suc = new F<Nat, Nat>() {
					public Nat apply(Nat x) {
						return new Suc(x);
					}
				};
				return zeroEnum.plus(sucEnumAux(_suc, e)).pay();
			}
		};
		return Enumeration.fix(sucEnum);
	}

	private static Enumeration<Nat> sucEnumAux(F<Nat, Nat> suc, Enumeration<Nat> e) {
		Enumeration<F<Nat, Nat>> singletonSuc = Enumeration.singleton(suc);
		Enumeration<Nat> res = Enumeration.apply(singletonSuc, e);
		return res;
	}

}