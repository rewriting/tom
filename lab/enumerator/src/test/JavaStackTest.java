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
import examples.adt.stack.IStack;
import examples.adt.stack.StackFactory;
import examples.adt.stack.stack.types.Stack;

@RunWith(PropCheck.class)
public class JavaStackTest {

	private static StackFactory factory= StackFactory.getInstance(StackFactory.ARRAY);
	private static IStack init= factory.makeStack();

	@Test
	public void testIsEmptyEmpty() {
		IStack s = init.empty();
		assertThat(s.isEmpty(), is(true));
	}

	@Test
	public void testEmptySize() {
		IStack s = init.empty();
		assertThat(s.size(), is(0));
	}

	@Test(expected = EmptyStackException.class)
	public void testEmptyPop() throws EmptyStackException {
		IStack s = init.empty();
		s.pop();
	}

	@Test(expected = EmptyStackException.class)
	public void testEmptyTop() throws EmptyStackException {
		IStack s = init.empty();
		s.top();
	}

	@Theory
	public void testNonEmptySize(@ForSome Stack gs) {
		IStack s = factory.makeStack(gs);
		assumeThat(s.isEmpty(), is(false));
		assertThat(s.size(), greaterThan(0));
	}

	@Theory
	public void testPushSize(@ForSome Stack gs, @ForSome int n) {
		IStack s = factory.makeStack(gs);
		int initSize = s.size();
		int finalSize = s.push(n).size();
		assertThat(finalSize, is(initSize + 1));
	}

	@Theory
	public void testPopSize(@ForSome Stack gs)
			throws EmptyStackException {
		IStack s = factory.makeStack(gs);
		assumeThat(s.isEmpty(), equalTo(false));
		int initSize = s.size();
		assertThat(s.pop().size(), is(initSize - 1));
	}

	@Theory
	public void testPopPush(@ForSome Stack gs, @ForSome int n) 
			throws EmptyStackException {
		IStack s = factory.makeStack(gs);
		IStack sclone = factory.makeStack(gs);
		assertThat(s.push(n).pop(), is(sclone));
	}

	@Theory public void testTop(@ForSome Stack gs, @ForSome int n) 
					throws EmptyStackException {
		IStack s = factory.makeStack(gs);
		assertThat(s.push(n).top(), is(n));
	}

}
