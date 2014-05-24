package test.tom.library.theory.unused;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import tom.library.sl.Visitable;
import tom.library.sl.VisitableBuiltin;
import tom.library.theory.shrink.suppliers.reducers.StringReducer;

public class StringReducerTest {

	@Before
	public void setUp() throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetReducedValue() {
		System.out.println("StringReducerTest.testGetReducedValue()");
		String term = "hello";
		List<Visitable> results = StringReducer.build().getReducedVisitableValues(term);
		assertThat("size should be 5",results.size(), equalTo(5));
		for (Visitable visitable : results) {
			System.out.println(visitable);
			VisitableBuiltin<String> res = (VisitableBuiltin<String>) visitable;
			assertThat(res.getBuiltin().length(), lessThan(term.length()));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetReducedValue12() {
		System.out.println("StringReducerTest.testGetReducedValue12()");
		String term = "aaaaaaaaaaaa";
		List<Visitable> results = StringReducer.build().getReducedVisitableValues(term);
		assertThat("size should be 10",results.size(), equalTo(10));
		for (Visitable visitable : results) {
			System.out.println(visitable);
			VisitableBuiltin<String> res = (VisitableBuiltin<String>) visitable;
			assertThat(res.getBuiltin().length(), lessThan(term.length()));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetReducedValueBig() {
		System.out.println("StringReducerTest.testGetReducedValue()");
		String term = "Lorem ipsum dolor sit amet, eu nam dico populo quaeque, no justo doming vis. Ad harum viderer democritum nam. Aliquam consulatu eum id, suavitate theophrastus pri et, et sit iudico doctus nominati. Ex sonet accusam his, ne vix virtute voluptatum, id augue persius quaestio vix. Vix adversarium deterruisset id.";
		List<Visitable> results = StringReducer.build().getReducedVisitableValues(term);
		assertThat("size should be 10",results.size(), equalTo(10));
		for (Visitable visitable : results) {
			System.out.println(visitable);
			VisitableBuiltin<String> res = (VisitableBuiltin<String>) visitable;
			assertThat(res.getBuiltin().length(), lessThan(term.length()));
		}
	}
	
	@Test
	public void testGetReducedValueEmpty() {
		List<Visitable> results = StringReducer.build().getReducedVisitableValues("");
		assertThat("size should be 0",results.size(), equalTo(0));
	}

}
