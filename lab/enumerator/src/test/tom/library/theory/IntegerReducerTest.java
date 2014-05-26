package test.tom.library.theory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import tom.library.shrink.reducers.IntegerReducer;

public class IntegerReducerTest {
	
	private IntegerReducer classUnderTest;
	
	@Before
	public void setUp() throws Exception {
		classUnderTest = new IntegerReducer();
	}
	
	@Test
	public void testReduce0() {
		int value = 0;
		
		List<Integer> results = classUnderTest.reduce(value);
		
		assertThat(results.size(), is(0));
		
	}

	@Test
	public void testReduce10() {
		int value = 10;
		
		List<Integer> results = classUnderTest.reduce(value);
		
		assertThat(results.size(), is(10));
		for (Integer val : results) {
			assertThat(val, lessThan(value));
		}
	}
	
	@Test
	public void testReduceLessThan10() {
		int value = 8;
		
		List<Integer> results = classUnderTest.reduce(value);
		
		assertThat(results.size(), is(8));
		for (Integer val : results) {
			assertThat(val, lessThan(value));
		}
	}

	@Test
	public void testReduceNegative() {
		int value = -8;
		
		List<Integer> results = classUnderTest.reduce(value);
		
		assertThat(results.size(), is(8));
		for (Integer val : results) {
			assertThat(val, allOf(greaterThan(value), lessThanOrEqualTo(0)));
		}
	}
	
	@Test
	public void testReduceMoreThan10() {
		int value = 300;
		
		List<Integer> results = classUnderTest.reduce(value);
		
		assertThat(results.size(), is(10));
		for (Integer val : results) {
			assertThat(val, lessThanOrEqualTo(value));
		}
	}
	
	@Test
	public void testReduceNegativeMoreThan10() {
		int value = -450;
		
		List<Integer> results = classUnderTest.reduce(value);
		
		assertThat(results.size(), is(10));
		for (Integer val : results) {
			assertThat(val, allOf(greaterThan(value), lessThanOrEqualTo(0)));
		}
	}
}
