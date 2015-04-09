package examples.factory.handwritten;

import java.math.BigInteger;
import java.util.Random;
import examples.factory.ListStack;

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
		int maxSize = 8;
		boolean canBeNull = false; 
		int singleton = -1;

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


		
		// the "this" used in the call to enumerating methods with numberOfSamples>1 (push, extend, etc.)
		Enumeration<ListStack> tmpListStackEnum = new Enumeration<ListStack>((LazyList<Finite<ListStack>>) null);

		// begin PUSH
		// enumerating method 
		// @Enumerate
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
		// @Enumerate(...
		// Integer elem
		// @Enumerate(maxSize = 4, ...
		maxSize = 4; // try with 2 and singleton 8 to see how the try-catch works
		Enumeration<Integer> elemEnum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(maxSize)));
		// @Enumerate(... singleton = 3, ...
		singleton = -1; //3;
		if(singleton >= 0){
			Integer _elemSingleton=null;
			try{
				_elemSingleton = elemEnum.get(nextRandomBigInteger(BigInteger.valueOf(singleton)));
			}catch(RuntimeException exc){}  
			while(_elemSingleton==null){ // no element at randomly generated index 
				try{ // try exhaustively from singleton downwards
					_elemSingleton = elemEnum.get(BigInteger.valueOf(singleton--));
				}catch(RuntimeException exc){}  
			}
			// should we handle the case of an empty enumeration?
			elemEnum = Enumeration.singleton(_elemSingleton);
		}
		// @Enumerate(...  canBeNull = true
		canBeNull = false; //true;
		if(canBeNull){
			Enumeration<Integer> emptyIntegerEnum = Enumeration.singleton(null);
			elemEnum = elemEnum.plus(emptyIntegerEnum);
		}
		
		// METHOD level
		// @Enumerate(...
		maxSize = -1; // can't be used for enumerating this (or we can use it on enumCons but doesn't make too much sense)
		Enumeration<ListStack> pushThisEnum = enumCons;  //new Enumeration<ListStack>(enumCons.parts().take(BigInteger.valueOf(maxSize)));
		// @Enumerate(... singleton = 0, ...
		singleton = -1; // by default 
		if(singleton >= 0){
			ListStack _pushThisSingleton=null;
			try{
				_pushThisSingleton = pushThisEnum.get(nextRandomBigInteger(BigInteger.valueOf(singleton)));
			}catch(RuntimeException exc){}  
			while(_pushThisSingleton==null){ // no element at randomly generated index 
				try{ // try exhaustively from singleton downwards
					_pushThisSingleton = pushThisEnum.get(BigInteger.valueOf(singleton--));
				}catch(RuntimeException exc){}  
			}
			pushThisEnum = Enumeration.singleton(_pushThisSingleton);
		}else{
			pushThisEnum = tmpListStackEnum;
		}
		// @Enumerate(... 
		canBeNull = false; // only possible choice for @Enumerate on METHOD
		
		// as many apply as parameters
		Enumeration<ListStack>  enumPushListStack = _listStack_push.apply(tmpListStackEnum).apply(elemEnum);
		// end PUSH

		// begin EMPTY
		// enumerating method 
		// @Enumerate(...) 
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
		// METHOD level
		// @Enumerate(...
		maxSize = -1; // can't be used for enumerating this (or we can use it on enumCons but doesn't make too much sense)
		Enumeration<ListStack> emptyThisEnum = enumCons;  //new Enumeration<ListStack>(enumCons.parts().take(BigInteger.valueOf(maxSize)));
		// @Enumerate(... singleton = 0, ...
		singleton = 0;
		if(singleton >= 0){
			ListStack _emptyThisSingleton=null;
			try{
				_emptyThisSingleton = emptyThisEnum.get(nextRandomBigInteger(BigInteger.valueOf(singleton)));
			}catch(RuntimeException exc){}  
			while(_emptyThisSingleton==null){ // no element at randomly generated index 
				try{ // try exhaustively from singleton downwards
					_emptyThisSingleton = emptyThisEnum.get(BigInteger.valueOf(singleton--));
				}catch(RuntimeException exc){}  
			}
			emptyThisEnum = Enumeration.singleton(_emptyThisSingleton);
		}else{
			emptyThisEnum = tmpListStackEnum;
		}
		// @Enumerate(... 
		canBeNull = false; // only possible choice fro @Enumerate on METHOD
		
		Enumeration<ListStack> enumEmptyListStack = _listStack_empty.apply(emptyThisEnum);
		// end EMPTY
		

		// begin EXTEND
		// enumerating method 
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

		// METHOD level
		// @Enumerate(...
		maxSize = -1; // can't be used for enumerating this (or we can use it on enumCons but doesn't make too much sense)
		Enumeration<ListStack> extendThisEnum = enumCons;  //new Enumeration<ListStack>(enumCons.parts().take(BigInteger.valueOf(maxSize)));
		// @Enumerate(... singleton = 0, ...
		singleton = -1; // by default 
		if(singleton >= 0){
			ListStack _extendThisSingleton=null;
			try{
				_extendThisSingleton = pushThisEnum.get(nextRandomBigInteger(BigInteger.valueOf(singleton)));
			}catch(RuntimeException exc){}  
			while(_extendThisSingleton==null){ // no element at randomly generated index 
				try{ // try exhaustively from singleton downwards
					_extendThisSingleton = pushThisEnum.get(BigInteger.valueOf(singleton--));
				}catch(RuntimeException exc){}  
			}
			extendThisEnum = Enumeration.singleton(_extendThisSingleton);
		}else{
			extendThisEnum = tmpListStackEnum;
		}
		// @Enumerate(... 
		canBeNull = false; // only possible choice for @Enumerate on METHOD
		
		// as many apply as parameters
		Enumeration<ListStack>  enumExtendListStack = _listStack_extend.apply(extendThisEnum);
		// end EXTEND
		
		
		enumListStack = enumPushListStack;
		enumListStack = enumListStack.plus(enumEmptyListStack);
//		enumListStack = enumListStack.plus(enumExtendListStack);

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
		while (result.compareTo(n) > 0) {
			result = new BigInteger(n.bitLength(), rand);
		}
		return result;
	}
}
