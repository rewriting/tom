package examples.factory;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import tom.library.enumerator.P1;

public class ListStackFactory {

	public static F<Enumeration<ListStack>, F<Enumeration<Integer>, Enumeration<ListStack>>> funMake_push() {
		return new F<Enumeration<ListStack>, F<Enumeration<Integer>, Enumeration<ListStack>>>() {
			public F<Enumeration<Integer>, Enumeration<ListStack>> apply(
					final Enumeration<ListStack> t1) {
				return new F<Enumeration<Integer>, Enumeration<ListStack>>() {
					public Enumeration<ListStack> apply(
							final Enumeration<Integer> t2) {
						return Enumeration
								.apply(Enumeration.apply(
										Enumeration
												.singleton((F<ListStack, F<Integer, ListStack>>) new F<ListStack, F<Integer, ListStack>>() {
													public F<Integer, ListStack> apply(
															final ListStack t1) {
														return new F<Integer, ListStack>() {
															public ListStack apply(final Integer t2) {
//																System.out.println("PUSH "+t1+ " + "+t2);
																return (ListStack) t1.push(t2);
																// (ListStack) new ListStack().push(2);
															}
														};
													}
												}), t1), t2).pay();
					}
				};
			}
		};
	}

//	public static F<Enumeration<ListStack>, Enumeration<ListStack>> funMake_empty() {
//		return new F<Enumeration<ListStack>, Enumeration<ListStack>>() {
//			public Enumeration<ListStack> apply(final Enumeration<ListStack> e) {
//				return Enumeration
//						.apply(Enumeration.singleton((F<ListStack, ListStack>) new F<ListStack, ListStack>() {
//							public ListStack apply(final ListStack e) {
//								System.out.println("EMPTY "+e);
//								return (ListStack) e.empty();
//								// (ListStack) new ListStack().push(2);
//							}
//						}), e).pay();
//			}
//		};
//	}

	public static F<Enumeration<ListStack>, Enumeration<ListStack>> funMake_empty() {
		return new F<Enumeration<ListStack>, Enumeration<ListStack>>() {
			public Enumeration<ListStack> apply(final Enumeration<ListStack> e) {
				return Enumeration.singleton( (ListStack) new ListStack().empty() );
			}
		};
	}

	protected static Enumeration<ListStack> enumListStack = null;

	public static final Enumeration<ListStack> tmpenumListStack = new Enumeration<ListStack>(
			(LazyList<Finite<ListStack>>) null);

	public static Enumeration<ListStack> getEnumeration() {
		if (enumListStack == null) {
			enumListStack = 
					funMake_empty().apply(
					ListStackFactory.tmpenumListStack)
					.plus(
					funMake_push().apply(ListStackFactory.tmpenumListStack)
							.apply(Combinators.makeint()));

			tmpenumListStack.p1 = new P1<LazyList<Finite<ListStack>>>() {
				public LazyList<Finite<ListStack>> _1() {
					return enumListStack.parts();
				}
			};

		}
		return enumListStack;
	}

}
