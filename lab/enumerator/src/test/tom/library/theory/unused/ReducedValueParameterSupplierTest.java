package test.tom.library.theory.unused;

import static org.junit.Assert.*;
import static org.hamcrest.number.OrderingComparison.*;
import static tom.library.theory.shrink.tools.VisitableTools.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.theories.PotentialAssignment;
import org.junit.contrib.theories.PotentialAssignment.CouldNotGenerateValueException;

import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;
import tom.library.sl.Visitable;
import tom.library.theory.shrink.suppliers.ReducedValueParameterSupplier;

public class ReducedValueParameterSupplierTest {

	private ReducedValueParameterSupplier classUnderTest;
	@Before
	public void setUp() throws Exception {
		classUnderTest = new ReducedValueParameterSupplier();
	}

	@Test
	public void testGetValueSourcesTerm1() throws CouldNotGenerateValueException {
		AList term = AList.fromString("con(cs(-1),con(cs(1),con(cs(0),con(cs(-1),con(cs(0),con(cs(-2),con(cs(1),con(cs(1),con(cs(0),con(cs(-1),con(cs(-2),con(cs(0),con(cs(1),empty())))))))))))))");
		List<PotentialAssignment> results = classUnderTest.getValueSources(term);
		for (PotentialAssignment pa : results) {
			Visitable res = (Visitable) pa.getValue();
			assertThat(res.toString(), size(res), lessThan(size(term)));
		}
	}

	@Test
	public void testGetValueSourcesTerm2() throws CouldNotGenerateValueException {
		Elem term = Elem.fromString("cs(-2)");
		List<PotentialAssignment> results = classUnderTest.getValueSources(term);
		for (PotentialAssignment pa : results) {
			System.out.println(pa.getValue());
			Visitable res = (Visitable) pa.getValue();
			assertThat(res.toString(), size(res), lessThan(size(term)));
		}
	}
	
	@Test
	public void testGetValueSourcesInteger() throws CouldNotGenerateValueException {
		int term = 6;
		List<PotentialAssignment> results = classUnderTest.getValueSources(term);
		for (PotentialAssignment pa : results) {
			assertThat((Integer) pa.getValue(), lessThan(term));
		}
	}
}
