package test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import org.hamcrest.core.IsInstanceOf;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import examples.adt.stack.EmptyStackException;
import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.theory.BadInputFailure;
import tom.library.theory.Enum;
import tom.library.theory.RandomCheck;
import tom.library.theory.TomCheck;
import tom.library.theory.TomForAll;

@RunWith(TomCheck.class)
public class BadInputTest {
	@Enum public static Enumeration<Integer> enumInt = Combinators.makeint();
	
	@Theory
	public void testBadInputFailure(
			@TomForAll @RandomCheck(sampleSize = 30) Integer input)
			throws EmptyStackException, BadInputFailure {
		if (Math.abs(input) < 5) {
			throw new BadInputFailure();
		}
		assertThat(Math.abs(input), is(greaterThanOrEqualTo(5)));
	}
}
