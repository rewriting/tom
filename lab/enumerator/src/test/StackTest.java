package test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.theory.BadInputException;
import tom.library.theory.ForSome;
import tom.library.theory.TomCheck;
import examples.adt.stack.EmptyStackException;
import examples.adt.stack.IStack;
import examples.adt.stack.StackFactory;
import examples.adt.stack.stack.types.Stack;
import examples.adt.stack.stacklanguage.types.StackL;

@RunWith(TomCheck.class)
public class StackTest {

	private IStack init;
	private StackFactory factory1;
	private StackFactory factory2;

	@Before
	public void setUp() {
		factory1 = StackFactory.getInstance(StackFactory.ARRAY);
		factory2 = StackFactory.getInstance(StackFactory.TOM);
		init = factory1.makeStack();
	}

	// @Ignore
	@Test
	public void testIsEmptyEmpty() {
		IStack s = init.empty();
		assertThat(s.isEmpty(), is(true));
	}

	// @Ignore
	@Test
	public void testEmptySize() {
		IStack s = init.empty();
		assertThat(s.size(), is(0));
	}

	@Ignore
	@Test(expected = EmptyStackException.class)
	public void testEmptyPop() throws EmptyStackException {
		IStack s = init.empty();
		s.pop();
	}

	// @Ignore
	@Test(expected = EmptyStackException.class)
	public void testEmptyTop() throws EmptyStackException {
		IStack s = init.empty();
		s.top();
	}

	// @Ignore
	@Theory
	public void testNonEmptySize(
			@ForSome(minSampleSize = 25, maxSampleSize = 30) Stack gs) {
		IStack s = factory1.makeStack(gs);
		assumeThat(s.isEmpty(), equalTo(false));
		assertThat(s.size(), is(not(0)));
	}

	// @Ignore
	@Theory
	public void testPushSize(
			@ForSome(minSampleSize = 25, maxSampleSize = 30) Stack gs,
			@ForSome(maxSampleSize = 10) int n) {
		IStack s = factory1.makeStack(gs);
		int initSize = s.size();
		s.push(n);
		int finalSize = s.size();
		assertThat(finalSize, is(initSize + 1));
	}

	// @Ignore
	@Theory
	public void testPopSize(
			@ForSome(minSampleSize = 0, maxSampleSize = 50, numberOfSamples = 100) Stack gs)
			throws EmptyStackException {
		IStack s = factory1.makeStack(gs);
		assumeThat(s.isEmpty(), equalTo(false));
		int initSize = s.size();
		s.pop();
		assertThat(s.size(), is(initSize - 1));
	}

	// @Ignore
	@Theory
	public void testPopPush(
			@ForSome(minSampleSize = 25, maxSampleSize = 30) Stack gs,
			@ForSome(maxSampleSize = 10) int n) throws EmptyStackException {
		IStack s = factory1.makeStack(gs);
		IStack sclone = factory1.makeStack(gs);
		s.push(n);
		s.pop();
		assertThat(s, is(sclone));
	}

	@Theory public void testTop(
			@ForSome(minSampleSize = 25, maxSampleSize = 50) Stack gs,
			@ForSome(maxSampleSize = 10) int n) throws EmptyStackException {
		IStack s = factory1.makeStack(gs);
		s.push(n);
		assertThat(s.top(), is(n));
	}

	@Theory
	public void testSameBehaviour(
			@ForSome(minSampleSize = 25, maxSampleSize = 30, numberOfSamples = 20) StackL gs)
			throws EmptyStackException, BadInputException {
		IStack stack1 = factory1.evaluateStack(gs);
		IStack stack2 = factory2.evaluateStack(gs);

		 assertThat(stack1.isEmpty(), is(stack2.isEmpty()));
		
		 assertThat(stack1.size(), is(stack2.size()));
		
		 assumeThat(stack1.isEmpty(), equalTo(false));
		 assertThat(stack1.top(), is(stack2.top()));

		Integer sRes = stack1.pop();
		Integer sBisRes = stack2.pop();
		// same result
		assertThat(sRes, is(sBisRes));
		// same side-effect
		assertThat(stack1.isEmpty(), is(stack2.isEmpty()));

		assertThat(stack1.size(), is(stack2.size()));

		assumeThat(stack1.isEmpty(), equalTo(false));
		assertThat(stack1.top(), is(stack2.top()));

	}
}
