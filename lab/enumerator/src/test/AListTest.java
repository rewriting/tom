package test;

import static examples.lists.DemoAList.addFirst;
import static examples.lists.DemoAList.contains;
import static examples.lists.DemoAList.get;
import static examples.lists.DemoAList.getIndexOf;
import static examples.lists.DemoAList.isEmpty;
import static examples.lists.DemoAList.size;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

import org.junit.Ignore;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.theory.ForSome;
import tom.library.theory.TomCheck;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;

@RunWith(TomCheck.class)
public class AListTest {

	@Ignore
	@Theory
	public void testInsertWith2Elements(
			@ForSome(maxSampleSize = 10) AList list,
			@ForSome(maxSampleSize = 10) Elem e1,
			@ForSome(maxSampleSize = 10) Elem e2) {
		assumeThat(true, is(not(isEmpty(list))));
		assumeThat(true, is(contains(list, e1)));
		assertTrue(getIndexOf(list, e1) + 1 == getIndexOf(addFirst(list, e2),
				e1));
	}

	@Ignore
	@Theory
	public void testInsertWithIndex(
			@ForSome(minSampleSize = 0, maxSampleSize = 10) AList list,
			@ForSome(maxSampleSize = 10) Elem element,
			@ForSome(maxSampleSize = 10) Integer index) {
		assumeThat(index, greaterThanOrEqualTo(0));
		assumeThat(isEmpty(list), is(false));
		assumeThat(size(list), greaterThan(index));
		assumeThat(element, is(not(get(list, index))));
		assertThat(
				getIndexOf(list, get(list, index)),
				equalTo(getIndexOf(addFirst(list, element), get(list, index)) - 1));
	}

	@Theory
	public void testInsertNotSoRandom(
			@ForSome(minSampleSize = 0, maxSampleSize = 10) AList list,
			@ForSome(maxSampleSize = 10) Integer index) {
		Elem element = get(list, size(list)-1);
		
		assumeThat(index, greaterThanOrEqualTo(0));
		assumeThat(isEmpty(list), is(false));
		assumeThat(size(list), greaterThan(index));
		assumeThat(element, is(not(get(list, index))));
		assertThat(
				getIndexOf(list, get(list, index)),
				equalTo(getIndexOf(addFirst(list, element), get(list, index)) - 1));
	}

	@Theory
	public void testInsertTom(
			@ForSome(minSampleSize = 0, maxSampleSize = 10) AList list,
			@ForSome(maxSampleSize = 10) Elem e1,
			@ForSome(maxSampleSize = 10) Elem e2) {
		assumeThat(isEmpty(list), is(false));
		assumeThat(contains(list, e1), is(true));
		// uncomment one the two to get the right specification
		// assumeThat(contains(list, e2),is(false));
		assumeThat(e2, is(not(e1)));
		assertThat(getIndexOf(list, e1) + 1,
				equalTo(getIndexOf(addFirst(list, e2), e1)));
	}
}
