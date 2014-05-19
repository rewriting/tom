package test.tom.library.theory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static tom.library.theory.shrink.tools.VisitableTools.*;

import java.util.List;

import org.junit.Test;

import tom.library.sl.Visitable;
import tom.library.theory.shrink.ShrinkException;
import tom.library.theory.shrink.suppliers.reducers.TermValueReducer;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;

public class TermValueReducerTest {

	@Test
	public void testBuildTermsWithReducedValueCs78() throws ShrinkException {
		AList term = AList.fromString("con(cs(78),empty())");
		TermValueReducer reducer = TermValueReducer.build(term);
		List<Object> results = reducer.buildTermsWithReducedValue();
		assertThat(results.size(), equalTo(10));

		for (Object object : results) {
			System.out.println(object);
		}
		
//		Object expected0 = AList.fromString("con(cs(0),empty())");
//		assertThat(results.get(0), equalTo(expected0));
//		
//		Object expected1 = AList.fromString("con(cs(1),empty())");
//		assertThat(results.get(1), equalTo(expected1));
//		
//		Object expected2 = AList.fromString("con(cs(2),empty())");
//		assertThat(results.get(2), equalTo(expected2));
	}
	
	@Test
	public void testBuildTermsWithReducedValue() throws ShrinkException {
		AList term = AList.fromString("con(cs(3),empty())");
		TermValueReducer reducer = TermValueReducer.build(term);
		List<Object> results = reducer.buildTermsWithReducedValue();
		assertThat(results.size(), equalTo(3));

		for (Object object : results) {
			System.out.println(object);
		}
		
		Object expected0 = AList.fromString("con(cs(0),empty())");
		assertThat(results.get(0), equalTo(expected0));
		
		Object expected1 = AList.fromString("con(cs(1),empty())");
		assertThat(results.get(1), equalTo(expected1));
		
		Object expected2 = AList.fromString("con(cs(2),empty())");
		assertThat(results.get(2), equalTo(expected2));
	}
	
	@Test
	public void testBuildTermsWithReducedValue1() throws ShrinkException {
		AList term = AList.fromString("con(cs(1),con(cs(0),empty()))");
		TermValueReducer reducer = TermValueReducer.build(term);
		List<Object> results = reducer.buildTermsWithReducedValue();
		assertThat(results.size(), equalTo(1));

		for (Object object : results) {
			System.out.println(object);
		}
		
		Object expected0 = AList.fromString("con(cs(0),con(cs(0),empty()))");
		assertThat(results.get(0), equalTo(expected0));
	}

	@Test
	public void testBuildTermsWithReducedValue2() throws ShrinkException {
		AList term = AList.fromString("con(cs(3),con(cs(78), empty()))");
		TermValueReducer reducer = TermValueReducer.build(term);
		List<Object> results = reducer.buildTermsWithReducedValue();
		
		for (Object object : results) {
			Visitable v = (Visitable) object;
			assertThat(size(v), lessThan(size(term)));
		}
		
	}
	
	@Test
	public void testBuildTermsWithReducedCs1() throws ShrinkException {
		System.out
				.println("TermValueReducerTest.testBuildTermsWithReducedCs1()");
		Elem term = Elem.fromString("cs(3)");
		TermValueReducer reducer = TermValueReducer.build(term);
		List<Object> results = reducer.buildTermsWithReducedValue();
		
		for (Object object : results) {
			System.out.println(object);
			Visitable v = (Visitable) object;
			assertThat(size(v), lessThan(size(term)));
		}
		
	}
	
	@Test
	public void testBuildTermsWithReducedCsNegative() throws ShrinkException {
		System.out
				.println("TermValueReducerTest.testBuildTermsWithReducedCsNegative()");
		Elem term = Elem.fromString("cs(-2)");
		TermValueReducer reducer = TermValueReducer.build(term);
		List<Object> results = reducer.buildTermsWithReducedValue();
		
		for (Object object : results) {
			System.out.println(object);
			Visitable v = (Visitable) object;
			assertThat(size(v), lessThan(size(term)));
		}
		
	}
	
	@Test(expected=ShrinkException.class)
	public void testBuildTermsWithReducedIntegerExpectsException() throws ShrinkException {
		int term = 6;
		TermValueReducer reducer = TermValueReducer.build(term);
		reducer.buildTermsWithReducedValue();
	}
}
