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

import tom.library.enumerator.Enumeration;
import tom.library.theory.BadInputException;
import tom.library.theory.Enum;
import tom.library.theory.TomCheck;
import tom.library.theory.ForSome;
import examples.adt.stack.EmptyStackException;
import examples.adt.stack.IStack;
import examples.adt.stack.StackFactory;
import examples.adt.stack.stack.types.Elem;
import examples.adt.stack.stack.types.Stack;
import examples.adt.stack.stacklanguage.types.ElemL;
import examples.adt.stack.stacklanguage.types.StackL;

@RunWith(TomCheck.class)
public class StackTest {
	@Enum
	public static Enumeration<Stack> enumStack = Stack.getEnumeration();
	@Enum
	public static Enumeration<Elem> enumElem = Elem.getEnumeration();
	@Enum
	public static Enumeration<StackL> enumStackL = StackL.getEnumeration();
	@Enum
	public static Enumeration<ElemL> enumElemL = ElemL.getEnumeration();

	private IStack init;
	private StackFactory factory1;
	private StackFactory factory2;

	@Before
	public void setUp() {
		factory1 = StackFactory.getInstance(StackFactory.ARRAY);
		factory2 = StackFactory.getInstance(StackFactory.TOM);
		init = factory1.makeStack();
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
			@ForSome(minSampleSize = 25, maxSampleSize = 30) Stack gs) {
		IStack s = factory1.makeStack(gs);
		assumeThat(s.isEmpty(), equalTo(false));
		assertThat(s.size(), is(not(0)));
	}

	@Theory
	public void testPushSize(
			@ForSome(minSampleSize = 25, maxSampleSize = 30) Stack gs,
			@ForSome(maxSampleSize = 10) Elem e) {
		IStack s = factory1.makeStack(gs);
		Integer n = factory1.makeInteger(e);
		int initSize = s.size();
		s.push(n);
		int finalSize = s.size();
		assertThat(finalSize, is(initSize + 1));
	}

	@Theory
	public void testPopSize(
			@ForSome(minSampleSize = 25, maxSampleSize = 50, numberOfSamples = 100) Stack gs)
			throws EmptyStackException {
		IStack s = factory1.makeStack(gs);
		assumeThat(s.isEmpty(), equalTo(false));
		int initSize = s.size();
		s.pop();
		int finalSize = s.size();
		assertThat(finalSize, is(initSize - 1));
	}

	@Theory
	public void testPopPush(
			@ForSome(minSampleSize = 25, maxSampleSize = 30) Stack gs,
			@ForSome(maxSampleSize = 10) Elem e)
			throws EmptyStackException {
		IStack s = factory1.makeStack(gs);
		IStack sclone = factory1.makeStack(gs);
		Integer n = factory1.makeInteger(e);
		s.push(n);
		s.pop();
		assertThat(s, is(sclone));
	}

	@Theory
	public void testTop(
			@ForSome(minSampleSize = 25, maxSampleSize = 50) Stack gs,
			@ForSome(maxSampleSize = 10) Elem e)
			throws EmptyStackException {
		IStack s = factory1.makeStack(gs);
		Integer n = factory1.makeInteger(e);
		s.push(n);
		assertThat(s.top(), is(n));
	}

	//@Ignore
	@Theory
	public void testSameBehaviour(
			@ForSome(minSampleSize = 25, maxSampleSize = 30) StackL gs)
			throws EmptyStackException, BadInputException {
		IStack stack1 = factory1.evaluateStack(gs);
		IStack stack2 = factory2.evaluateStack(gs);
		
		assertThat(stack1.isEmpty(), is(stack2.isEmpty()));
		
		assertThat(stack1.size(), is(stack2.size()));
		
		assumeThat(stack1.isEmpty(), equalTo(false));
		assertThat(stack1.top(), is(stack2.top()));

		Integer sRes = stack1.pop();
		Integer sBisRes = stack2.pop();
		//same result
		assertThat(sRes, is(sBisRes));
		//same side-effect
		assertThat(stack1.isEmpty(), is(stack2.isEmpty()));
		
		assertThat(stack1.size(), is(stack2.size()));
		
		assumeThat(stack1.isEmpty(), equalTo(false));
		assertThat(stack1.top(), is(stack2.top()));

	}
}
