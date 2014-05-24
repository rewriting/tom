package test.tom.library.theory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import tom.library.theory.shrink.suppliers.reducers2.StringReducer;

public class StringReducerTest {

	private StringReducer classUnderTest;
	
	@Before
	public void setUp() throws Exception {
		classUnderTest = new StringReducer();
	}

	@Test
	public void testReduceEmptyString() {
		String value = "";
		
		List<String> results = classUnderTest.reduce(value);
		
		assertThat(results.size(), is(0));
	}
	
	@Test
	public void testReduceSmallString() {
		String value = "small";
		
		List<String> results = classUnderTest.reduce(value);
		
		assertThat(results.size(), is(value.length()));
		for (String val : results) {
			assertThat(val.length(), lessThan(value.length()));
		}
	}
	
	@Test
	public void testReduceBigString() {
		String value = "Lorem ipsum dolor sit amet, eu nam dico populo quaeque, "
				+ "no justo doming vis. Ad harum viderer democritum nam. Aliquam "
				+ "consulatu eum id, suavitate theophrastus pri et, et sit iudico "
				+ "doctus nominati. Ex sonet accusam his, ne vix virtute voluptatum, "
				+ "id augue persius quaestio vix. Vix adversarium deterruisset id.";
		
		List<String> results = classUnderTest.reduce(value);
		
		assertThat(results.size(), is(10));
		for (String val : results) {
			assertThat(val.length(), lessThan(value.length()));
		}
	}
}
