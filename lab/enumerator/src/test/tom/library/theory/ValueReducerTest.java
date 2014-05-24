package test.tom.library.theory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static tom.library.theory.shrink.tools.VisitableTools.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import examples.lists.alist.types.AList;
import tom.library.sl.Visitable;
import tom.library.theory.shrink.suppliers.reducers2.ValueReducer;

public class ValueReducerTest {
	
	private ValueReducer classUnderTest;

	@Before
	public void setUp() throws Exception {
		classUnderTest = new ValueReducer();
	}

	@Test
	public void testReduceConsEmpty() {
		AList term = AList.fromString("empty()");
		List<Visitable> results = classUnderTest.reduce(term);
		assertThat(results.size(), is(0));
	}

	@Test
	public void testReduceConsPositiveSmall() {
		AList term = AList.fromString("con(cs(3),empty())");
		
		List<Visitable> results = classUnderTest.reduce(term);
		
		assertThat(results.size(), is(3));
		
		Object expected0 = AList.fromString("con(cs(0),empty())");
		assertThat(results.get(0), equalTo(expected0));
		
		Object expected1 = AList.fromString("con(cs(1),empty())");
		assertThat(results.get(1), equalTo(expected1));
		
		Object expected2 = AList.fromString("con(cs(2),empty())");
		assertThat(results.get(2), equalTo(expected2));
	}
	
	@Test
	public void testReduceConsPositiveBig() {
		AList term = AList.fromString("con(cs(78),empty())");
		
		List<Visitable> results = classUnderTest.reduce(term);
		
		assertThat(results.size(), equalTo(10));

		for (Visitable v : results) {
			assertThat(size(v), lessThan(size(term)));
		}
	}
	
	@Test
	public void testReduceConsNegativeSmall() {
		AList term = AList.fromString("con(cs(-3),empty())");
		
		List<Visitable> results = classUnderTest.reduce(term);
		
		assertThat(results.size(), equalTo(3));

		for (Visitable v : results) {
			assertThat(size(v), lessThan(size(term)));
		}
	}
	
	@Test
	public void testReduceConsNegativeBig() {
		AList term = AList.fromString("con(cs(-1300),empty())");
		
		List<Visitable> results = classUnderTest.reduce(term);
		
		assertThat(results.size(), equalTo(10));

		for (Visitable v : results) {
			assertThat(size(v), lessThan(size(term)));
		}
	}
}
