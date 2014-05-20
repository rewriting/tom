package test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.RandomCheck;
import tom.library.theory.TomCheck;
import tom.library.theory.TomForAll;
import examples.adt.stack.EmptyStackException;
import examples.adt.stack.IStack;
import examples.adt.stack.MyFactory;
import examples.adt.stack.TomStack;
import examples.adt.stack.stack.types.Elem;
import examples.adt.stack.stack.types.Stack;
//import examples.adt.stack.StackFactory;

@RunWith(TomCheck.class)
public class StackTest {
	@Enum
	public static Enumeration<Stack> enumStack = Stack.getEnumeration();
	@Enum
	public static Enumeration<Elem> enumElem = Elem.getEnumeration();

	private IStack init;
	private MyFactory factory;

	@Before
	public void setUp() {
		init = new TomStack();
		factory = MyFactory.getInstance();
	}

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
	public void testNonEmptySize(
			@TomForAll @RandomCheck(minSampleSize = 25, sampleSize = 30) Stack gs) {
		IStack s = factory.makeStack(gs);
		assumeThat(s.isEmpty(), equalTo(false));
		assertThat(s.size(), is(not(0)));
	}

	@Theory
	public void testPushSize(
			@TomForAll @RandomCheck(minSampleSize = 25, sampleSize = 30) Stack gs,
			@TomForAll @RandomCheck(sampleSize = 10) Elem e) {
		IStack s = factory.makeStack(gs);
		Integer n = factory.makeInteger(e);
		int initSize = s.size();
		s.push(n);
		int finalSize = s.size();
		assertThat(finalSize, is(initSize + 1));
	}

	@Theory
	public void testPopSize(
			@TomForAll @RandomCheck(minSampleSize = 25, sampleSize = 30) Stack gs)
			throws EmptyStackException {
		IStack s = factory.makeStack(gs);
		assumeThat(s.isEmpty(), equalTo(false));
		int initSize = s.size();
		s.pop();
		int finalSize = s.size();
		assertThat(finalSize, is(initSize - 1));
	}

	@Theory
	public void testPopPush(
			@TomForAll @RandomCheck(minSampleSize = 25, sampleSize = 30) Stack gs,
			@TomForAll @RandomCheck(sampleSize = 10) Elem e)
			throws EmptyStackException {
		IStack s = factory.makeStack(gs);
		IStack sclone = factory.makeStack(gs);
		Integer n = factory.makeInteger(e);
		s.push(n);
		s.pop();
		assertThat(s, is(sclone));
	}

	@Theory
	public void testTop(
			@TomForAll @RandomCheck(minSampleSize = 25, sampleSize = 30) Stack gs,
			@TomForAll @RandomCheck(sampleSize = 10) Elem e)
			throws EmptyStackException {
		IStack s = factory.makeStack(gs);
		Integer n = factory.makeInteger(e);
		s.push(n);
		assertThat(s.top(), is(n));
	}

}
