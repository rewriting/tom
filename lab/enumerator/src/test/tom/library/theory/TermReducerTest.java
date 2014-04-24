package test.tom.library.theory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.sl.Visitable;
import tom.library.theory.shrink.TermReducer;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;

public class TermReducerTest {
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testGetValueSources() throws Throwable {
		AList term = AList.fromString("con(cs(3),con(cs(6),empty()))");
		AList expected1 = AList.fromString("empty()");
		AList expected2 = AList.fromString("con(cs(6),empty())");
		List<PotentialAssignment> assignments = TermReducer.build(term, AList.getEnumeration()).getValueSources();
		for (PotentialAssignment potentialAssignment : assignments) {
			System.out.println(potentialAssignment.getValue());
		}
		assertThat(assignments.size(), is(2));
		assertThat((Visitable) assignments.get(0).getValue(), is((Visitable)expected1));
		assertThat((Visitable) assignments.get(1).getValue(), is((Visitable)expected2));
	}
	
	@Test
	public void testGetValueSourcesCS() throws Throwable {
		Elem term = Elem.fromString("cs(3)");
		Elem expected1 = Elem.fromString("cs(3)");
		List<PotentialAssignment> assignments = TermReducer.build(term, Elem.getEnumeration()).getValueSources();
		assertThat(assignments.size(), is(1));
		assertThat((Visitable) assignments.get(0).getValue(), is((Visitable)expected1));
		
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
