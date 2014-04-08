package examples;

import java.math.BigInteger;

import propcheck.shrink.PropcheckShrink;
import propcheck.shrink.ShrinkTools;
import propcheck.shrink.SubtermTraverser;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import examples.data.types.types.A;
import examples.data.types.types.B;
import examples.data.types.types.C;
import examples.data.types.types.D;
import examples.data.types.types.E;

public class TestTypes {
	
	public static void main(String[] args) {
		TestTypes test = new TestTypes();
		test.test1();
	}
	
	<A> void printLesserTerm(A term) {
		Enumeration<A> e = SubtermTraverser.make(term).getEnumerationFromTerm(term);
		LazyList<Finite<A>> parts = e.parts();
		BigInteger index = BigInteger.ZERO;
		
	
	}
	
	public void test1() {
		A termA1 = Tool.getTerm(A.getEnumeration(), 10);
		B termB1 = Tool.getTerm(B.getEnumeration(), 5);
		C termC1 = Tool.getTerm(C.getEnumeration(), 10);
		D termD1 = Tool.getTerm(D.getEnumeration(), 10);
		E termE1 = Tool.getTerm(E.getEnumeration(), 5);
		
		System.out.println(termA1);
		System.out.println("subterms:");
		printSubterms(termA1);
		
		System.out.println("\n" + termB1);
		System.out.println("subterms:");
		printSubterms(termB1);
		
		System.out.println("\n" + termC1);
		System.out.println("subterms:");
		printSubterms(termC1);
		
		System.out.println("\n" + termD1);
		System.out.println("subterms:");
		printSubterms(termD1);
		
		System.out.println("\n" + termE1);
		System.out.println("subterms:");
		printSubterms(termE1);
	}
	
	<T> void printSubterms(T term) {
		PropcheckShrink<T> shrink = new PropcheckShrink<T>(term);
		while (shrink.hasNextSubterm()) {
			System.out.println(shrink.getNextshrinkedTerm());
		}
	}
}
