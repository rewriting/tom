package test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.RandomCheck;
import tom.library.theory.TomCheck;
import tom.library.theory.TomForAll;
import examples.adt.stack.StackEvaluator;
import examples.adt.stack.StackEvaluator.EmptyStackException;
import examples.adt.stack.stack.types.Elem;
import examples.adt.stack.stack.types.Stack;

@RunWith(TomCheck.class)
public class StackTest {
	@Enum
	public static Enumeration<Stack> enumStack = Stack.getEnumeration();
	@Enum
	public static Enumeration<Elem> enumElem = Elem.getEnumeration();

	@Test
	public void testIsEmptyEmpty() {
		Stack s = StackEvaluator.createEmpty();
		assertThat(StackEvaluator.isEmpty(s), is(true));
	}

	@Test
	public void testEmptySize() {
		Stack s = StackEvaluator.createEmpty();
		assertThat(StackEvaluator.size(s), is(0));
	}

	@Test(expected = EmptyStackException.class)
	public void testEmptyPop() {
		Stack s = StackEvaluator.createEmpty();
		StackEvaluator.pop(s);
	}

	@Test(expected = EmptyStackException.class)
	public void testEmptyTop() throws EmptyStackException {
		Stack s = StackEvaluator.createEmpty();
		StackEvaluator.top(s);
	}

	@Theory
	public void testNonEmptySize(@TomForAll @RandomCheck(minSampleSize = 25, sampleSize = 30) Stack s) {
		assertTrue(!(StackEvaluator.isEmpty(s)));
		assertThat(StackEvaluator.size(s), is(not(0)));
	}

	@Theory
	public void testPushSize(
			@TomForAll @RandomCheck(minSampleSize = 25, sampleSize = 30) Stack s,
			@TomForAll @RandomCheck(sampleSize = 10) Elem e) {
		assertThat(StackEvaluator.size(StackEvaluator.push(s, e)), is(StackEvaluator.size(s)+1));
	}

	@Theory
	public void testPopSize(
			@TomForAll @RandomCheck(minSampleSize = 25, sampleSize = 30) Stack s) {
		assertTrue(!(StackEvaluator.isEmpty(s)));
		assertThat(StackEvaluator.size(StackEvaluator.pop(s)), is(StackEvaluator.size(s)-1));
	}

	@Theory
	public void testPopPush(
			@TomForAll @RandomCheck(minSampleSize = 25, sampleSize = 30) Stack s,
			@TomForAll @RandomCheck(sampleSize = 10) Elem e) {
		assertThat(StackEvaluator.pop(StackEvaluator.push(s, e)), is(s));
	}

	@Theory
	public void testTop(
			@TomForAll @RandomCheck(minSampleSize = 25, sampleSize = 30) Stack s,
			@TomForAll @RandomCheck(sampleSize = 10) Elem e)
			throws EmptyStackException {
		assertThat(StackEvaluator.top(StackEvaluator.push(s, e)), is(e));
	}

	@Theory
	public void testEvaluateSize(
			@TomForAll @RandomCheck(minSampleSize = 25, sampleSize = 30) Stack s) {
		Stack result = StackEvaluator.evaluate(s);
		int size1 = StackEvaluator.size(s);
		int size2 = StackEvaluator.size(result);
		System.out.println("s: " + s + " - " + size1);
		System.out.println("result: " + result + " - " + size2);
		assertThat(size1, is(size2));
	}
}
