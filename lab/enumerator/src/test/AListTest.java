package test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;

import org.junit.Ignore;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.TomCheck;
import tom.library.theory.ForSome;
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
			@ForSome(maxSampleSize = 10)  AList list,
			@ForSome(maxSampleSize = 10)  Elem e1,
			@ForSome(maxSampleSize = 10)  Elem e2) {
		assumeThat(true, is(not(DemoAList.isEmpty(list))));
		assumeThat(true, is(DemoAList.contains(list, e1)));
		assertTrue(DemoAList.getIndexOf(list, e1) + 1 == DemoAList.getIndexOf(
				DemoAList.addFirst(list, e2), e1));
	}

	
	@Theory
	public void testInsertWithIndex(@ForSome(minSampleSize=50, maxSampleSize = 100) AList list,
			@ForSome(maxSampleSize = 10) Elem element,
			@ForSome(maxSampleSize = 10) Integer index) {
		assumeThat(index, greaterThanOrEqualTo(0));
		assumeThat(true, is(not(DemoAList.isEmpty(list))));
		assumeThat(true, is(DemoAList.size(list) > index));
		int lhs = DemoAList.getIndexOf(list, DemoAList.get(list, index)) + 1;
		int rhs = DemoAList
				.getIndexOf(DemoAList.addFirst(list, element),
						DemoAList.get(list, index));
		assertThat(lhs, equalTo(rhs));
	}

	
	@Theory
	public void testInsertTom(@ForSome(minSampleSize=90, maxSampleSize = 100) AList list,
			@ForSome(maxSampleSize = 10) Elem e1,
			@ForSome(maxSampleSize = 10) Elem e2) {
		assumeThat(true, is(not(DemoAList.isEmpty(list))));
		assumeThat(true, is(DemoAList.contains(list, e1)));
		int lhs = DemoAList.getIndexOf(list, e1) + 1;
		int rhs = DemoAList.getIndexOf(DemoAList.addFirst(list, e2), e1);
		assertThat(lhs, equalTo(rhs));
	}
}
