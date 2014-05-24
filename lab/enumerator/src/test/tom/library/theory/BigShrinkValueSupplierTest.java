package test.tom.library.theory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.text.StyleContext.SmallAttributeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.theories.PotentialAssignment;
import org.junit.contrib.theories.PotentialAssignment.CouldNotGenerateValueException;

import examples.lists.alist.types.AList;
import tom.library.sl.Visitable;
import tom.library.theory.shrink.suppliers.BigShrinkValueSupplier;

public class BigShrinkValueSupplierTest {

	private BigShrinkValueSupplier classUnderTest;
	
	@Before
	public void setUp() throws Exception {
		classUnderTest = new BigShrinkValueSupplier();
	}

	@Test
	public void testGetValueSourcesString() throws CouldNotGenerateValueException {
		String term = "Lorem ipsum dolor sit amet, eu nam dico populo quaeque, "
				+ "no justo doming vis. Ad harum viderer democritum nam. Aliquam "
				+ "consulatu eum id, suavitate theophrastus pri et, et sit iudico "
				+ "doctus nominati. Ex sonet accusam his, ne vix virtute voluptatum, "
				+ "id augue persius quaestio vix. Vix adversarium deterruisset id.";
		
		List<PotentialAssignment> assignments = classUnderTest.getValueSources(term);
		//System.out.println(assignments.size());
		for (PotentialAssignment pa : assignments) {
			String s = (String) pa.getValue();
			assertThat(s.length(), lessThanOrEqualTo(term.length()));
			//System.out.println(pa.getValue());
		}
	}
	
	@Test
	public void testGetValueSourcesEmptyString() throws CouldNotGenerateValueException {
		String term = "";
		
		List<PotentialAssignment> assignments = classUnderTest.getValueSources(term);
		
		assertThat(assignments.size(), is(1));
		String result = (String) assignments.get(0).getValue();
		assertThat(result, is(term));
		
	}

	@Test
	public void testGetValueSourcesInteger0() throws CouldNotGenerateValueException {
		int term = 0;
		
		List<PotentialAssignment> assignments = classUnderTest.getValueSources(term);
		
		assertThat(assignments.size(), is(1));
		int result = (int) assignments.get(0).getValue();
		assertThat(result, is(term));
	}
	
	@Test
	public void testGetValueSourcesInteger() throws CouldNotGenerateValueException {
		int term = 100;
		
		List<PotentialAssignment> assignments = classUnderTest.getValueSources(term);
		System.out.println(assignments.size());
		for (PotentialAssignment pa : assignments) {
			int s = (int) pa.getValue();
			assertThat(s, lessThanOrEqualTo(term));
			System.out.println(s);
		}
	}
	
	@Test
	public void testGetValueSourcesCons() throws CouldNotGenerateValueException {
		//AList term = AList.fromString("con(cs(7),con(cs(2), con(cs(-3),empty())))");
				AList term = AList.fromString("con(cs(7),con(cs(2),con(cs(2),"
				+ "con(cs(-6),con(cs(1),con(cs(0),con(cs(0),con(cs(-2),"
				+ "con(cs(-3),empty())))))))))");
		List<PotentialAssignment> assignments = classUnderTest.getValueSources(term);
		for (PotentialAssignment pa : assignments) {
			Visitable v = (Visitable) pa.getValue();
			System.out.println(v);
		}
	}
	
	@Test
	public void testGetValueSourcesMutualRecursive() {
		fail("Not yet implemented");
	}
	

	
	@Test
	public void testCompare() {
		Set<Visitable> tets = new TreeSet<Visitable>();
		AList a1 = AList.fromString("con(cs(7),empty())");
		AList a2 = AList.fromString("con(cs(0),con(cs(1),empty()))");
		AList a3 = AList.fromString("con(cs(7),con(cs(2), con(cs(-3),empty())))");
		tets.add(a1);
		tets.add(a3);
		tets.add(a2);
		
		System.out.println(a2.compareToLPO(a1));
	}
}
