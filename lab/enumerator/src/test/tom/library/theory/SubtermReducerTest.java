package test.tom.library.theory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import examples.data.mutual.recursive.types.D;
import examples.lists.alist.types.AList;
import tom.library.shrink.reducers.SubtermReducer;
import tom.library.sl.Visitable;

public class SubtermReducerTest {

	private SubtermReducer classUnderTest;
	
	@Before
	public void setUp() throws Exception {
		classUnderTest = new SubtermReducer();
	}

	@Test
	public void testReduceCons() {
		AList term = AList.fromString("con(cs(7),con(cs(2),con(cs(2),con(cs(-6),con(cs(1),con(cs(0),con(cs(0),con(cs(-2),con(cs(-3),empty())))))))))");
		AList expected0 = AList.fromString("empty()");
		AList expected1 = AList.fromString("con(cs(2),con(cs(2),con(cs(-6),con(cs(1),con(cs(0),con(cs(0),con(cs(-2),con(cs(-3),empty()))))))))");
		List<Visitable> results = classUnderTest.reduce(term);
		// assertThat(results.size(), is(1));
		// with constant
		assertThat(results.size(), is(2));
		assertThat((Visitable) results.get(0), is((Visitable)expected0));
		assertThat((Visitable) results.get(1), is((Visitable)expected1));
	}
	
	@Test
	public void testReduceD() {
		D term = D.fromString("fd1(fd1(d(),c1()),fc1(c1(),fd1(fd1(d(),c1()),c1())))");
		D expected0 = D.fromString("d()");
		D expected1 = D.fromString("fd1(d(),c1())");
		D expected2 = D.fromString("fd1(fd1(d(),c1()),c1())");
		
		List<Visitable> assignments = classUnderTest.reduce(term);
		
		assertThat(assignments.size(), is(3));
		assertThat(assignments.get(0), is((Visitable)expected0));
		assertThat(assignments.get(1), is((Visitable)expected1));
		assertThat(assignments.get(2), is((Visitable)expected2));
	}
	
	@Test
	public void testReduceD2() {
		D term = D.fromString("fd1(d(),c1()),c1())");
		D expected1 = D.fromString("d()");
		
		List<Visitable> assignments = classUnderTest.reduce(term);
		
		assertThat(assignments.size(), is(1));
		assertThat(assignments.get(0), is((Visitable)expected1));
	}

	@Test
	public void testReduceDTerminal() {
		D term = D.fromString("d()");
		
		List<Visitable> assignments = classUnderTest.reduce(term);
		
		assertThat(assignments.size(), is(1));
	}
}
