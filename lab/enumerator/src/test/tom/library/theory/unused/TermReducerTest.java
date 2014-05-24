package test.tom.library.theory.unused;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import tom.library.sl.Visitable;
import tom.library.theory.shrink.suppliers.reducers.TermReducer;
import examples.data.mutual.recursive.types.D;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;

public class TermReducerTest {
	
	@Test
	public void testGetValueSourcesDebug1() throws Throwable {
		AList term = AList.fromString("con(cs(7),con(cs(2),con(cs(2),con(cs(-6),con(cs(1),con(cs(0),con(cs(0),con(cs(-2),con(cs(-3),empty())))))))))");
		AList expected1 = AList.fromString("empty()");
		AList expected2 = AList.fromString("con(cs(2),con(cs(2),con(cs(-6),con(cs(1),con(cs(0),con(cs(0),con(cs(-2),con(cs(-3),empty()))))))))");
		List<Object> assignments = TermReducer.build(term, AList.getEnumeration()).getInputValues();
		for (Object potentialAssignment : assignments) {
			System.out.println(potentialAssignment);
		}
		assertThat(assignments.size(), is(2));
		assertThat((Visitable) assignments.get(0), is((Visitable)expected1));
		assertThat((Visitable) assignments.get(1), is((Visitable)expected2));
	}
	
	@Test
	public void testGetValueSourcesFd() throws Throwable {
		D term = D.fromString("fd1(fd1(d(),c1()),fc1(c1(),fd1(fd1(d(),c1()),c1())))");
		D expected1 = D.fromString("d()");
		D expected2 = D.fromString("fd1(d(),c1())");
		D expected3 = D.fromString("fd1(fd1(d(),c1()),c1())");
		List<Object> assignments = TermReducer.build(term, D.getEnumeration()).getInputValues();
		for (Object potentialAssignment : assignments) {
			System.out.println(potentialAssignment);
		}
		assertThat(assignments.size(), is(3));
		assertThat((Visitable) assignments.get(0), is((Visitable)expected1));
		assertThat((Visitable) assignments.get(1), is((Visitable)expected2));
		assertThat((Visitable) assignments.get(2), is((Visitable)expected3));
	}
	
	@Test
	public void testGetValueSources() throws Throwable {
		AList term = AList.fromString("con(cs(3),con(cs(6),empty()))");
		AList expected1 = AList.fromString("empty()");
		AList expected2 = AList.fromString("con(cs(6),empty())");
		List<Object> assignments = TermReducer.build(term, AList.getEnumeration()).getInputValues();
		for (Object potentialAssignment : assignments) {
			System.out.println(potentialAssignment);
		}
		assertThat(assignments.size(), is(2));
		assertThat((Visitable) assignments.get(0), is((Visitable)expected1));
		assertThat((Visitable) assignments.get(1), is((Visitable)expected2));
	}
	
	@Test
	public void testGetValueSourcesCS() throws Throwable {
		Elem term = Elem.fromString("cs(3)");
		Elem expected1 = Elem.fromString("cs(3)");
		List<Object> assignments = TermReducer.build(term, Elem.getEnumeration()).getInputValues();
		assertThat(assignments.size(), is(1));
		assertThat((Visitable) assignments.get(0), is((Visitable)expected1));
		
	}

	@Ignore("temporarily disabled")
	@Test
	public void testGetTerminalConstructorFromEnumeration() throws Throwable {
		AList term = AList.fromString("con(cs(3),con(cs(6),empty()))");
		AList expected = AList.fromString("empty()");
		List<Visitable> results = TermReducer.build(term, AList.getEnumeration()).getTerminalConstructorFromEnumeration();
		assertThat(results.size(), is(1));
		assertThat(results.get(0), is((Visitable) expected));
	}
	
}
