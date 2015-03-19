package examples.factory;

import java.math.BigInteger;
import java.util.Random;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import tom.library.enumerator.P1;
import tom.library.factory.Enumerate;

public class ListStackFactory {

	private static Enumeration<ListStack> enumListStack = null;

	public static Enumeration<ListStack> getEnumeration() {

		// final result
		Enumeration<ListStack> enumRes = null;

		// constructor with no arguments
		Enumeration<ListStack> enumCons1 = Enumeration.singleton(new ListStack());

		// constructor with arguments 
		// F< arg1, F< arg2, ... F <argn, Student>...>
		F<Integer, ListStack> _listStack_cons2 = new F<Integer, ListStack>() {
			public ListStack apply(final Integer t2) {
				return  new ListStack(t2);
			}
		};
		// //@Enumerate(maxSize=5)
		// int nbElem
		final Enumeration<Integer> nbElemEnum = new Enumeration<Integer>(
				Combinators.makeInteger().parts().take(BigInteger.valueOf(5)));
		Enumeration<ListStack> enumCons2 = Enumeration.apply(Enumeration.singleton(_listStack_cons2), nbElemEnum);

		// enumeration for all constructors (as many PLUS as constructors-1)
		Enumeration<ListStack>  enumCons = enumCons1.plus(enumCons2);
		
		// element in the constructor based enumeration that will be used in enumerating methods with numberOfSamples=1
		ListStack _listStack_base_constructor = enumCons.get(BigInteger.valueOf(0));

		// F< arg1, F< arg2, ... F <argn, Student>...>
		F<Enumeration<ListStack>, F<Enumeration<Integer>, Enumeration<ListStack>>> _listStack_push = new F<Enumeration<ListStack>, F<Enumeration<Integer>, Enumeration<ListStack>>>() {
			public F<Enumeration<Integer>, Enumeration<ListStack>> apply(
					final Enumeration<ListStack> t1) {
				return new F<Enumeration<Integer>, Enumeration<ListStack>>() {
					public Enumeration<ListStack> apply(final Enumeration<Integer> t2) {
						return Enumeration
								.apply(Enumeration.apply(
										Enumeration
												.singleton((F<ListStack, F<Integer, ListStack>>) new F<ListStack, F<Integer, ListStack>>() {
													public F<Integer, ListStack> apply(final ListStack t1) {
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
		// @Enumerate(maxSize = 4, ...
		// Integer elem
		Enumeration<Integer> elemEnum = new Enumeration<Integer>(
				Combinators.makeInteger().parts().take(BigInteger.valueOf(4)));
		// @Enumerate(... singleton = 20, ...
		boolean notFound=true;
		Integer _elemSingleton=null;
		do{
			try{
				_elemSingleton = elemEnum.get(nextRandomBigInteger(BigInteger.valueOf(3)));
				notFound=false;
			}catch(RuntimeException exc){}
		}while(notFound);
		elemEnum = Enumeration.singleton(_elemSingleton);
		// @Enumerate(...  canBeNull = true
		Enumeration<Integer> emptyEnum = Enumeration.singleton(null);
		elemEnum = emptyEnum.plus(elemEnum);

		// the "this" used in the call to enumerating methods with numberOfSamples>1 (push, extend, etc.)
		Enumeration<ListStack> tmpListStackEnum = new Enumeration<ListStack>((LazyList<Finite<ListStack>>) null);

		// enumerating method 
		F<Enumeration<ListStack>, Enumeration<ListStack>> _listStack_empty = new F<Enumeration<ListStack>, Enumeration<ListStack>>() {
			public Enumeration<ListStack> apply(final Enumeration<ListStack> e) {
				return Enumeration
						.apply(Enumeration.singleton((F<ListStack, ListStack>) new F<ListStack, ListStack>() {
							public ListStack apply(final ListStack e) {
								return (ListStack) e.clone().empty();
							}
						}), e).pay();
			}
		};

		// other method without parameters but not a base case (normally no difference to the others)
		F<Enumeration<ListStack>, Enumeration<ListStack>> _listStack_extend = new F<Enumeration<ListStack>, Enumeration<ListStack>>() {
			public Enumeration<ListStack> apply(final Enumeration<ListStack> e) {
				return Enumeration
						.apply(Enumeration.singleton((F<ListStack, ListStack>) new F<ListStack, ListStack>() {
							public ListStack apply(final ListStack e) {
								return (ListStack) e.clone().extend();
							}
						}), e).pay();
			}
		};

		// method with numberOfSamples=1 ==> applied to only one element (the default one)
		enumListStack = _listStack_empty.apply(Enumeration.singleton(_listStack_base_constructor));

		// method
		enumListStack = enumListStack.plus(_listStack_push.apply(
				tmpListStackEnum).apply(elemEnum));

		// generation method without parameters
//		 enumListStack = enumListStack.plus(_listStack_extend.apply(
//				 tmpListStackEnum));

		tmpListStackEnum.p1 = new P1<LazyList<Finite<ListStack>>>() {
			public LazyList<Finite<ListStack>> _1() {
				return enumListStack.parts();
			}
		};

		enumRes = enumListStack;
		
		return enumRes;
	}
	
	private static BigInteger nextRandomBigInteger(BigInteger n) {
		Random rand = new Random();
		BigInteger result = new BigInteger(n.bitLength(), rand);
		while (result.compareTo(n) >= 0) {
			result = new BigInteger(n.bitLength(), rand);
		}
		return result;
	}
}
