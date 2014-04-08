package examples;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.accessibility.AccessibleKeyBinding;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import tom.library.sl.Visitable;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;
import examples.lists.blist.types.BList;

public class TestSubterms {
	public static void main(String[] args) {
		TestSubterms test = new TestSubterms();
		//test.testList();
		
//		System.out.println("\n\nElem");
//		test.testElem();
		
//		System.out.println("\n\nBlist");
//		test.testBlist();;
		
		System.out.println("Shrink test");
		test.testShrink();
	}
	
	public void testShrink() {
		
		AList l = getTerm(AList.getEnumeration(), 23);
		Shrink<AList> shrink = new Shrink<AList>(l);
		
		AList co = shrink.getConstant();
		System.out.println("type: " + l.getClass());
		System.out.println("term: " + l);
		System.out.println("constant : " + co);
		
		System.out.println("subterm : " + shrink.getSubterm());
//		System.out.println("subterm : " + shrink.getSubterm());
//		System.out.println("subterm : " + shrink.getSubterm());
	}
	
	public void testBlist() {
		BList l = getTerm(BList.getEnumeration(), 20);
		System.out.println("term: " + l);
		System.out.println("child count: " + l.getChildCount());
		System.out.println("head: " + l.gethead());
		System.out.println("tail: " + l.gettail());
//		for (Visitable child : l.getChildren()) {
//			System.out.println(child);
//		}
		
		getConstans(BList.getEnumeration());
	}
	
	public void testElem() {
		Elem e = getTerm(Elem.getEnumeration(), 5);
		
		System.out.println("term: " + e);
		System.out.println(e.getChildren() + "  " + e.getChildCount());
	}
	
	public void testList() {
		AList l = getTerm(AList.getEnumeration(), 23);
		System.out.println("term: " + l);
		System.out.println("child count: " + l.getChildCount());
		System.out.println("head: " + l.gethead());
		System.out.println("tail: " + l.gettail());
	}

	private <A> A getTerm(Enumeration<A> aEnum, int depth) {
		Random random = new Random();
		BigInteger card, index;
		
		LazyList<Finite<A>> parts = aEnum.parts();
		for (int i = 0; i < depth; i++) {
			parts = parts.tail();
		}
		card = parts.head().getCard();
		index = new BigInteger(card.bitLength(), random);
		
		A a =  aEnum.get(index);
		return a;
	}
	
	public <A> void getConstans(Enumeration<A> aEnum) {
		for (int i = 0; i < aEnum.parts().head().getCard().intValue(); i++) {
			System.out.println(aEnum.get(BigInteger.valueOf(i)));
		}
	}
	
	// TODO shrink to nil()
	// give all subterms 
	// give recursive of subterm
	class Shrink<A> {
		A term;
		List<A> subterms;
		int stIndex = 0;
		
		public Shrink(A term) {
			this.term = term;
			subterms = new ArrayList<A>();
		}
		
		public Enumeration<A> getEnumFromTerm() {
			return (Enumeration<A>) invokeNoArgsMethodFromSuperclass(term.getClass(), "getEnumeration");
		}
		
		public A getConstant() {
			Enumeration<A> en = getEnumFromTerm();
			BigInteger card = en.parts().head().getCard();
			//System.out.println("TestSubterms.Shrink.getConstant() card: " + card);
			BigInteger index = card.compareTo(BigInteger.ONE) == 0? BigInteger.ZERO: new BigInteger(card.bitLength(), new Random());
			//System.out.println("TestSubterms.Shrink.getConstant() index: " + index);
			A constant = en.get(index);
			return constant;
		}
		
		public A getConstant(Enumeration<A> aEnum) {
			BigInteger card = aEnum.parts().head().getCard();
			BigInteger index = new BigInteger(card.bitLength(), new Random());
			A constant = aEnum.get(index);
			return constant;
		}
		
		public int getChildCount() {
			return (Integer) invokeMethodFromInstance(term.getClass(), "getChildCount");
		}
		
		public A getChild(Integer i) {
			return (A) invokeMethodFromInstance(term.getClass(), "getChildAt", i.getClass(), i);
		}
		
		public Object invokeNoArgsMethodFromSuperclass(Class<?> instance, String method) {
			Object result = null;
			try {
				result = instance.getSuperclass().getDeclaredMethod(method, null).invoke(null, null);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
		
		public Object invokeMethodFromInstance(Class<?> instance, String method) {
			Object result = null;
			try {
				System.out
						.println("TestSubterms.Shrink.invokeMethodFromInstance() instance: " + instance + ":" + instance.getDeclaredMethod(method, null));
					result = instance.getDeclaredMethod(method, null).invoke(instance.newInstance(), null);
				
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
		
		public Object invokeMethodFromInstance(Class<?> instance, String method, Class<?> paramType, Object paramVal) {
			Object result = null;
			try {
					result = instance.getDeclaredMethod(method, paramType).invoke(null, paramVal);
				
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
		
		public void iterateSubterm(){
			subterms.clear();
			for (int i = 0; i < getChildCount(); i++) {
				A child = getChild(i);
				subterms.add(child);
//				if (getChild(i).getClass().isInstance(term.getClass())) {
//					
//				}
			}
		}
		
		public A getSubterm() {
			if (stIndex == getChildCount() - 1) {
				stIndex = 0;
			}
			A st = subterms.get(stIndex);
			
			return st;
		}
	}
	

}
