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
import static examples.lists.DemoAList.*;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;

@RunWith(TomCheck.class)
public class AListTest {

	@Ignore
	@Theory
	public void testInsertWith2Elements(
			@ForSome(maxSampleSize = 10)  AList list,
			@ForSome(maxSampleSize = 10)  Elem e1,
			@ForSome(maxSampleSize = 10)  Elem e2) {
		assumeThat(true, is(not(isEmpty(list))));
		assumeThat(true, is(contains(list, e1)));
		assertTrue(getIndexOf(list, e1) + 1 == getIndexOf(
				addFirst(list, e2), e1));
	}

	
	@Theory
	public void testInsertWithIndex(@ForSome(minSampleSize=50, maxSampleSize = 100) AList list,
			@ForSome(maxSampleSize = 10) Elem element,
			@ForSome(maxSampleSize = 10) Integer index) {
		assumeThat(index, greaterThanOrEqualTo(0));
		assumeThat(true, is(not(isEmpty(list))));
		assumeThat(true, is(size(list) > index));
		int lhs = getIndexOf(list, get(list, index)) + 1;
		int rhs = getIndexOf(addFirst(list, element),
						get(list, index));
		assertThat(lhs, equalTo(rhs));
	}

	
	@Theory
	public void testInsertTom(@ForSome(minSampleSize=0, maxSampleSize = 10) AList list,
			@ForSome(maxSampleSize = 10) Elem e1,
			@ForSome(maxSampleSize = 10) Elem e2) {
		assumeThat(isEmpty(list), is(false));
		assumeThat(contains(list, e1),is(true));
		// uncomment to get the right specification
//		assumeThat(contains(list, e2),is(false));
		assertThat(getIndexOf(list, e1) + 1, equalTo(getIndexOf(addFirst(list, e2), e1)));
	}
}
