package test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import examples.adt.queue.SimpleQueue;
import examples.adt.queue.SimpleQueue.EmptyQueueException;
import examples.adt.queue.queue.types.Elem;
import examples.adt.queue.queue.types.Queue;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.RandomCheck;
import tom.library.theory.TomCheck;
import tom.library.theory.TomForAll;

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
	public void testFront(@TomForAll @RandomCheck(minSampleSize=50, sampleSize = 100) Elem element) throws EmptyQueueException {
		classUnderTest.enqueue(element);
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
	public void testDequeue(@TomForAll @RandomCheck(minSampleSize=50, sampleSize = 100) Elem element) {
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
			@TomForAll @RandomCheck(minSampleSize=50, sampleSize = 100) Elem element,
			@TomForAll @RandomCheck(minSampleSize=10, sampleSize = 20) Queue queue) throws EmptyQueueException {
		SimpleQueue q1 = SimpleQueue.createQueue(queue);
		SimpleQueue q2 = SimpleQueue.createQueue(queue);
		//System.out.println("q1 #1: " + q1.getQueue());
		assumeThat(queue.isempty(), is(false));
		q1.enqueue(element);
		q1.dequeue();
		q2.dequeue();
		q2.enqueue(element);
		//System.out.println("q1: " + q1.getQueue());
		//System.out.println("q2: " + q2.getQueue());
		assertThat(q1.getQueue(), is(q2.getQueue()));
	}
}
