package test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

import org.junit.Ignore;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.RandomCheck;
import tom.library.theory.RandomForAll;
import tom.library.theory.TomCheck;
import tom.library.theory.TomForAll;
import examples.lists.DemoAList;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;

@RunWith(TomCheck.class)
public class AListTest {
	@Enum
	public static Enumeration<AList> alistEnum = AList.getEnumeration();
	@Enum
	public static Enumeration<Elem> elemEnum = Elem.getEnumeration();
	@Enum
	public static Enumeration<Integer> intEnum = Combinators.makeInteger();

	@Ignore
	@Theory
	public void testInsertWith2Elements(
			@RandomForAll(sampleSize = 10) AList list,
			@RandomForAll(sampleSize = 10) Elem e1,
			@RandomForAll(sampleSize = 10) Elem e2) {
		assumeThat(true, is(not(DemoAList.isEmpty(list))));
		assumeThat(true, is(DemoAList.contains(list, e1)));
		assertTrue(DemoAList.getIndexOf(list, e1) + 1 == DemoAList.getIndexOf(
				DemoAList.addFirst(list, e2), e1));
	}

	
	@Theory
	public void testInsertWithIndex(@TomForAll @RandomCheck(minSampleSize=50, sampleSize = 100) AList list,
			@RandomForAll(sampleSize = 10) Elem element,
			@RandomForAll(sampleSize = 10) Integer index) {
		assumeThat(index, greaterThanOrEqualTo(0));
		assumeThat(true, is(not(DemoAList.isEmpty(list))));
		assumeThat(true, is(DemoAList.size(list) > index));
		assertTrue(DemoAList.getIndexOf(list, DemoAList.get(list, index)) + 1 == DemoAList
				.getIndexOf(DemoAList.addFirst(list, element),
						DemoAList.get(list, index)));
	}

	@Theory
	public void testInsertTom(@TomForAll @RandomCheck(minSampleSize=90, sampleSize = 100) AList list,
			@TomForAll @RandomCheck(sampleSize = 10) Elem e1,
			@TomForAll @RandomCheck(sampleSize = 10) Elem e2) {
		assumeThat(true, is(not(DemoAList.isEmpty(list))));
		assumeThat(true, is(DemoAList.contains(list, e1)));
		assertTrue(DemoAList.getIndexOf(list, e1) + 1 == DemoAList.getIndexOf(
				DemoAList.addFirst(list, e2), e1));
	}
}
