package test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assume.assumeThat;
import static org.junit.Assert.assertThat;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.theory.ForSome;
import tom.library.theory.TomCheck;
import examples.adt.stack.EmptyStackException;

import examples.adt.stack.stack.types.*;

import static examples.adt.stack.StackEvaluator.*;

@RunWith(TomCheck.class)
public class TomStackEvaluatorTest {

	%include{ ../examples/adt/stack/stack/Stack.tom }


	@Theory
	public void testNonEmptySize(
			@ForSome(minSampleSize = 25, maxSampleSize = 30) Stack s) {
		assumeThat(isEmpty(s), equalTo(false));
		assertThat(size(s), is(not(0)));
	}

	@Theory 
	public void testTop(
			@ForSome(minSampleSize = 25, maxSampleSize = 50) Stack s,
			@ForSome(maxSampleSize = 10) int n) throws EmptyStackException {
		assertThat(top(push(s,n)), is(n));
	}

}
