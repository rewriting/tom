package examples.factory;

import java.math.BigInteger;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import tom.library.enumerator.P1;

public class ListStackFactory {

	private static Enumeration<ListStack> enumListStack = null;

	public static Enumeration<ListStack> getEnumeration() {

		boolean canBeNull = false;
		// if(@Generator(canBeNull)){
		//     canBeNull = true;
	    // }
		
		// we need at least one base case constructor method
		// could be a constructor
		// or a method - in this case we should specify a constructor for the
		// this used to call the method (something like EnumerationBase)
		Enumeration<ListStack> enumRes = null;

		// constructor with no arguments
		ListStack _listStack_base_constructor = new ListStack();

		// F< arg1, F< arg2, ... F <argn, Student>...>
		F<Enumeration<ListStack>, F<Enumeration<Integer>, Enumeration<ListStack>>> _listStack_push = new F<Enumeration<ListStack>, F<Enumeration<Integer>, Enumeration<ListStack>>>() {
			public F<Enumeration<Integer>, Enumeration<ListStack>> apply(
					final Enumeration<ListStack> t1) {
				return new F<Enumeration<Integer>, Enumeration<ListStack>>() {
					public Enumeration<ListStack> apply(
							final Enumeration<Integer> t2) {
						return Enumeration.apply(Enumeration.apply(Enumeration.singleton((F<ListStack, F<Integer, ListStack>>) new F<ListStack, F<Integer, ListStack>>() {
							public F<Integer, ListStack> apply(
									final ListStack t1) {
								return new F<Integer, ListStack>() {
									public ListStack apply(final Integer t2) {
										// should build a completely new object which does not interfere with the others
										return (ListStack) t1.clone().push(t2);
									}
								};
							}
						}), t1), t2).pay();
					}
				};
			}
		};

		// //@Enumerate(maxSize=4)
		// int no,
		final Enumeration<Integer> noEnum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(4)));

		// the "this" used in the call to push
		Enumeration<ListStack> tmpListStackEnum = new Enumeration<ListStack>((LazyList<Finite<ListStack>>) null);

		// (constructor) base method (with no arguments)
		Enumeration<ListStack> _listStack_empty = Enumeration.singleton((ListStack) _listStack_base_constructor.empty());

		// other method without parameters but not a base case
		F<Enumeration<ListStack>, Enumeration<ListStack>> _listStack_extend = new F<Enumeration<ListStack>, Enumeration<ListStack>>() {
			public Enumeration<ListStack> apply(final Enumeration<ListStack> e) {
				return Enumeration.apply(Enumeration.singleton((F<ListStack, ListStack>) new F<ListStack, ListStack>() {
					public ListStack apply(final ListStack e) {
						return (ListStack) e.clone().extend();
					}
				}), e).pay();
			}
		};

		// base case
		enumListStack = _listStack_empty;

		// method
		enumListStack = enumListStack.plus(_listStack_push.apply(tmpListStackEnum).apply(noEnum));

		// generation method without parameters
//		enumListStack = enumListStack.plus(_listStack_extend.apply(tmpListStackEnum));

		tmpListStackEnum.p1 = new P1<LazyList<Finite<ListStack>>>() {
			public LazyList<Finite<ListStack>> _1() {
				return enumListStack.parts();
			}
		};

		if (canBeNull) {
			Enumeration<ListStack> emptyEnum = Enumeration.singleton(null);
			emptyEnum = emptyEnum.plus(enumListStack.pay());
			enumRes = emptyEnum;
		} else {
			enumRes = enumListStack;
		}

		return enumRes;
	}
}
