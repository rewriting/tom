package test.tom.library.theory.unused;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.theories.PotentialAssignment.CouldNotGenerateValueException;

import tom.library.sl.Visitable;
import tom.library.sl.VisitableBuiltin;
import tom.library.theory.shrink.suppliers.reducers.IntegerReducer;

public class IntegerReducerTest_old {

	private IntegerReducer classUnderTest;
	
	@Before
	public void setUp() throws Exception {
		classUnderTest = new IntegerReducer();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetValueSourcesSmall() throws CouldNotGenerateValueException {
		List<Visitable> result = classUnderTest.getReducedVisitableValue(2);
		assertThat(result.size(), equalTo(2));
		VisitableBuiltin<Integer> res0 = (VisitableBuiltin<Integer>) result.get(0);
		VisitableBuiltin<Integer> res1 = (VisitableBuiltin<Integer>) result.get(1);
		System.out.println(res0);
		System.out.println(res1);
		assertThat(res0.getBuiltin(), allOf(greaterThanOrEqualTo(0), lessThan(2)));
		assertThat(res1.getBuiltin(), allOf(greaterThanOrEqualTo(0), lessThan(2)));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetValueSourcesMedium() throws CouldNotGenerateValueException {
		System.out.println("IntegerReducerTest.testGetValueSourcesMedium()");
		List<Visitable> results = classUnderTest.getReducedVisitableValue(13);
		assertThat(results.size(), equalTo(10));
		for (Visitable visitable : results) {
			System.out.println(visitable);
			VisitableBuiltin<Integer> res = (VisitableBuiltin<Integer>) visitable;
			assertThat(res.getBuiltin(), allOf(greaterThanOrEqualTo(0), lessThan(13)));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetValueSources() throws CouldNotGenerateValueException {
		System.out.println("IntegerReducerTest.testGetValueSources()");
		List<Visitable> results = classUnderTest.getReducedVisitableValue(100);
		assertThat(results.size(), equalTo(10));
		for (Visitable visitable : results) {
			System.out.println(visitable);
			VisitableBuiltin<Integer> res = (VisitableBuiltin<Integer>) visitable;
			assertThat(res.getBuiltin(), allOf(greaterThanOrEqualTo(0), lessThan(100)));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetValueSources78() throws CouldNotGenerateValueException {
		System.out.println("IntegerReducerTest.testGetValueSources78()");
		List<Visitable> results = classUnderTest.getReducedVisitableValue(78);
		assertThat(results.size(), equalTo(10));
		for (Visitable visitable : results) {
			System.out.println(visitable);
			VisitableBuiltin<Integer> res = (VisitableBuiltin<Integer>) visitable;
			assertThat(res.getBuiltin(), allOf(greaterThanOrEqualTo(0), lessThan(78)));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetValueSourcesNegative() throws CouldNotGenerateValueException {
		System.out.println("IntegerReducerTest.testGetValueSourcesNegative()");
		List<Visitable> results = classUnderTest.getReducedVisitableValue(-50);
		assertThat(results.size(), equalTo(10));
		for (Visitable visitable : results) {
			System.out.println(visitable);
			VisitableBuiltin<Integer> res = (VisitableBuiltin<Integer>) visitable;
			assertThat(res.getBuiltin(), allOf(lessThanOrEqualTo(0), greaterThan(-50)));
		}
	}
}
