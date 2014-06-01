package test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import org.junit.Test;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;
import examples.adt.stack.EmptyStackException;
import examples.adt.stack.stack.types.Stack;
import examples.adt.stack.stack.types.stack.empty;

@RunWith(PropCheck.class)
public class TomStackTest {
	@Test
	public void testIsEmptyEmpty() {
		Stack s = empty.make();
		assertThat(s.isEmpty(), is(true));
	}

	@Test
	public void testEmptySize() {
		Stack s = empty.make();
		assertThat(s.size(), is(0));
	}

	@Test(expected = EmptyStackException.class)
	public void testEmptyPop() throws EmptyStackException {
		Stack s = empty.make();
		s.pop();
	}

	@Test(expected = EmptyStackException.class)
	public void testEmptyTop() throws EmptyStackException {
		Stack s = empty.make();
		s.top();
	}

	@Theory
	public void testNonEmptySize(@ForSome(maxSampleSize = 30) Stack s) {
		assumeThat(s.isEmpty(), is(false));
		assertThat(s.size(), greaterThan(0));
	}

	@Theory
	public void testPushSize(@ForSome(maxSampleSize = 30) Stack s, 
			                 @ForSome(maxSampleSize = 10) int n) {
		int initSize = s.size();
		int finalSize = s.push(n).size();
		assertThat(finalSize, is(initSize + 1));
	}

	@Theory
	public void testPopSize(@ForSome(maxSampleSize = 50) Stack s) throws EmptyStackException {
		assumeThat(s.isEmpty(), equalTo(false));
		int initSize = s.size();
		assertThat(s.pop().size(), is(initSize - 1));
	}

	@Theory
	public void testPopPush(@ForSome(maxSampleSize = 100) Stack s, 
			                @ForSome(maxSampleSize = 10) int n) throws EmptyStackException {
		assertThat(s.push(n).pop(), is(s));
	}

	@Theory public void testTop(@ForSome(maxSampleSize = 100) Stack s, 
			                    @ForSome(maxSampleSize = 10) int n) throws EmptyStackException {
		assertThat(s.push(n).top(), is(n));
	}
}
