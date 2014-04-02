package test.propcheck.shrink;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import propcheck.shrink.PropcheckShink;
import examples.lists.alist.types.AList;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;

public class PropcheckShinkTest {
	
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

	AList smallTerm;
	AList bigTerm;
	AList constant;
	
	PropcheckShink<AList> classUnderTest;
	PropcheckShink<AList> classUnderTest2;
	@Before
	public void setUp() throws Exception {
		bigTerm = getTerm(AList.getEnumeration(), 150);
		smallTerm = getTerm(AList.getEnumeration(), 6);
		constant = getTerm(AList.getEnumeration(), 1);
		classUnderTest = new PropcheckShink<AList>(smallTerm);
		classUnderTest2 = new PropcheckShink<AList>(bigTerm);
	}

	@Test
	public void testHasNextSubterm() {
		System.out.println("PropcheckShinkTest.testHasNextSubterm()");
		System.out.println("term: " + bigTerm);
		assertTrue(classUnderTest2.hasNextSubterm());
	}

	@Test
	public void testRetrieveSubterms() {
		System.out.println("\nPropcheckShinkTest.testRetrieveSubterms()");
		System.out.println("term: " + smallTerm);
		List<AList> list = classUnderTest.retrieveSubterms(smallTerm);
		for (AList s : list) {
			System.out.println("subterm: " + s);
		}
		assertTrue(list.size() == 1);
	}

	@Test
	public void testRetrieveSubtermsFromConstant() {
		System.out.println("\nPropcheckShinkTest.testRetrieveSubtermsFromConstant()");
		System.out.println("term: " + constant);
		classUnderTest.setCurrentTerm(constant);
		List<AList> list = classUnderTest.retrieveSubterms(constant);
		for (AList s : list) {
			System.out.println("subterm: " + s);
		}
		assertTrue(list.size() == 0);
	}
	
	@Test
	public void testShrink() {
		System.out.println("\nPropcheckShinkTest.testShrink()");
		System.out.println("term: " + smallTerm);
		while (classUnderTest.hasNextSubterm()) {
			AList st = classUnderTest.getNextshrinkedTerm();
			System.out.println(st);
		}
	}

}
