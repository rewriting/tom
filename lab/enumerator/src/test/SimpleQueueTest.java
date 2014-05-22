package test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import examples.adt.queue.SimpleQueue;
import examples.adt.queue.SimpleQueue.EmptyQueueException;
import examples.adt.queue.queue.types.Elem;
import examples.adt.queue.queue.types.Queue;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.Shrink;
import tom.library.theory.TomCheck;
import tom.library.theory.ForSome;
import tom.library.theory.internal.CounterExample;
import tom.library.theory.internal.ParameterizedAssertionFailure;
import tom.library.theory.internal.TestObject;
import tom.library.theory.shrink.DefaultShrinkHandler;

@RunWith(TomCheck.class)
public class SimpleQueueTest {
	private SimpleQueue classUnderTest;
	
	@Enum public static Enumeration<Elem> enumElem = Elem.getEnumeration(); 
	@Enum public static Enumeration<Queue> enumQueue = Queue.getEnumeration();
	
	@Before
	public void setUp() throws Exception {
		classUnderTest = SimpleQueue.createQueue();
	}
	
	@Theory 
	public void testCreateEnqueue() {
		assertTrue(classUnderTest.isEmpty());
	}
	
	@Theory
	public void testFrontOnEmpty() {
		try {
			classUnderTest.front();
		} catch (EmptyQueueException e) {
			assertThat(e, instanceOf(EmptyQueueException.class));
		}
	}
	
	@Theory
	public void testFront(@ForSome(minSampleSize=50, maxSampleSize = 100) Elem element,
			@ForSome(minSampleSize=50, maxSampleSize = 100) Elem element2) 
					throws EmptyQueueException {
		classUnderTest.enqueue(element);
		classUnderTest.enqueue(element2);
		assertThat(classUnderTest.isEmpty(), is(false));
		assertThat(classUnderTest.front(), is(element));
	}
	
	@Theory
	public void testDequeueOnEmpty() {
		try {
			classUnderTest.dequeue();
		} catch (EmptyQueueException e) {
			assertThat(e, instanceOf(EmptyQueueException.class));
		}
	}
	
	@Theory
	public void testDequeue(@ForSome(minSampleSize=50, maxSampleSize = 100) Elem element) {
		try {
			classUnderTest.enqueue(element);
			classUnderTest.dequeue();
			assertTrue(classUnderTest.isEmpty());
		} catch (EmptyQueueException e) {
			fail(e.getMessage());
		}
	}
	
	@Theory
	public void testAssociativityEnqueDequeue(
			@ForSome(maxSampleSize = 10) Elem element,
			@ForSome(minSampleSize=25, maxSampleSize = 30) Queue queue) throws EmptyQueueException {
		SimpleQueue q1 = SimpleQueue.createQueue(queue);
		SimpleQueue q2 = SimpleQueue.createQueue(queue);
		
		assumeThat(queue.isempty(), is(false));
		q1.enqueue(element);
		q1.dequeue();
		
		q2.dequeue();
		q2.enqueue(element);
		
		assertThat(q1.getQueue(), is(q2.getQueue()));
	}
	
	@Theory
	@Shrink(handler=CustomShrinkHandler.class)
	public void testAssociativityEnqueDequeueWithCustomShrink(
			@ForSome(maxSampleSize = 10) Elem element,
			@ForSome(minSampleSize=25, maxSampleSize = 30) Queue queue) throws EmptyQueueException {
		SimpleQueue q1 = SimpleQueue.createQueue(queue);
		SimpleQueue q2 = SimpleQueue.createQueue(queue);
		
		assumeThat(queue.isempty(), is(false));
		q1.enqueue(element);
		q1.dequeue();
		
		q2.dequeue();
		q2.enqueue(element);
		
		assertThat(q1.getQueue(), is(q2.getQueue()));
	}
	
	public static class CustomShrinkHandler extends DefaultShrinkHandler {

		public CustomShrinkHandler(TestObject testObject) {
			super(testObject);
		}
		
		@Override
		public void shrink(CounterExample counterExample) throws Throwable {
			// do nothing, no shrink defined
			throw new ParameterizedAssertionFailure(new Exception("no shrink"), "", counterExample.getCounterExamples());
		}
	}
}
