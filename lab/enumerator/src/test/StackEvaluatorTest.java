package test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import examples.adt.stack.StackEvaluator;
import examples.adt.stack.StackEvaluator.EmptyStackException;
import examples.adt.stack.stack.types.Elem;
import examples.adt.stack.stack.types.Stack;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.RandomCheck;
import tom.library.theory.TomCheck;
import tom.library.theory.TomForAll;

@RunWith(TomCheck.class)
public class StackEvaluatorTest {
	@Enum public static Enumeration<Stack> enumStack = Stack.getEnumeration();
	@Enum public static Enumeration<Elem> enumElem = Elem.getEnumeration();
	
	@Theory
	public void testIsEmpty(@TomForAll @RandomCheck(sampleSize = 10) Elem e) {
		Stack s = StackEvaluator.createEmpty();
		assertTrue(StackEvaluator.isEmpty(s));
		assertThat(StackEvaluator.size(s), is(0));
		s = StackEvaluator.push(s, e);
		assertThat(StackEvaluator.isEmpty(s), is(not(true)));
		assertThat(StackEvaluator.size(s), is(1));
	}
	
	@Theory
	public void testTop(
			@TomForAll @RandomCheck(minSampleSize=25, sampleSize = 30) Stack s,
			@TomForAll @RandomCheck(sampleSize = 10) Elem e) throws EmptyStackException {
		Stack stack = StackEvaluator.push(s, e);
		Elem elem = StackEvaluator.top(stack);
		assertThat(StackEvaluator.isEmpty(s), is(not(equalTo(true))));
		assertThat(elem, is(equalTo(e)));
	}
	
	@Theory
	public void testEvaluateSize(@TomForAll @RandomCheck(minSampleSize=25, sampleSize = 30) Stack s) {
		Stack result = StackEvaluator.evaluate(s);
		int size1 = StackEvaluator.size(s);
		int size2 = StackEvaluator.size(result);
		System.out.println("s: " + s + " - " + size1);
		System.out.println("result: " + result + " - " + size2);
		assertThat(size1, is(size2));
	}
}
