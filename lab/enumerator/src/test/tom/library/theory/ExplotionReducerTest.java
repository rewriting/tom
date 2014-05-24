package test.tom.library.theory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static tom.library.theory.shrink.tools.VisitableTools.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import examples.data.mutual.recursive.types.D;
import examples.lists.alist.types.AList;
import tom.library.sl.Visitable;
import tom.library.theory.shrink.suppliers.reducers2.ExplotionReducer;

public class ExplotionReducerTest {
	
	private ExplotionReducer classUnderTest;
	
	@Before
	public void setUp() throws Exception {
		classUnderTest = new ExplotionReducer();
	}
	
	@Test
	public void testReduceCons() {
		AList term = AList.fromString("con(cs(0),con(cs(1),con(cs(1),con(cs(3),con(cs(-2),empty())))))");
		
		List<Visitable> results = classUnderTest.reduce(term);
		
		assertThat(results.size(), is(4));
		for (Visitable v : results) {
			assertThat(size(v), lessThan(size(term)));
		}
	}
	
	@Test
	public void testReduceMutualRecursive() {
		D term = D.fromString("fd1(fd1(d(),c1()),fc1(c1(),d()))");
		Object ex1 = D.fromString("fd1(d(),fc1(c1(),d()))");
		Object ex2 = D.fromString("fd1(fd1(d(),c1()),c1())");
		
		List<Visitable> terms = classUnderTest.reduce(term);
		
		assertTrue(terms.size() == 2);
		assertThat(terms.get(0), is(ex1));
		assertThat(terms.get(1), is(ex2));
	}
	
	@Test
	public void testReduceTerminal() {
		AList term = AList.fromString("empty()");
		
		List<Visitable> results = classUnderTest.reduce(term);
		
		assertThat(results.isEmpty(), is(true));
	}
}
