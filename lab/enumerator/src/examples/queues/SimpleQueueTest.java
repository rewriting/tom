package examples.queues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.ForSome;
import tom.library.theory.TomCheck;
import examples.queues.queue.types.Elem;
import examples.queues.queue.types.Queue;

@RunWith(TomCheck.class)
public class SimpleQueueTest {
	
	@Enum public static Enumeration<Queue> enumeration1 = Queue.getEnumeration();
	@Enum public static Enumeration<Elem> enumeration2 = Elem.getEnumeration();
	@Enum public static Enumeration<Integer> enumeration5 = Combinators.makeint();
	
	//@Theory
	public void testInsertElemEmpty(@ForSome(maxSampleSize=100) Elem e) {
        SimpleQueue sq = new SimpleQueue();
        try {
        	int v = SimpleQueue.makeFromElem(e);
    		System.out.println("addEmpty " + e);

        	sq.add(v);
        	assertEquals(1,sq.size());
        } catch(Exception ex) {
    		//System.out.println("elim " + e);

        }
	}
	
	//@Theory
	public void testInsertIntQueue(
			@ForSome(maxSampleSize=100) Integer v,
			@ForSome(maxSampleSize=100) Queue q
			) {
        try {
        	SimpleQueue sq = SimpleQueue.makeFromQueue(q);
    		System.out.println("add " + q);
    		int oldSize = sq.size();
        	sq.add(v);
        	assertEquals(oldSize+1, sq.size());
        } catch(Exception ex) {
    		//System.out.println("elim " + e);

        }
	}
	
	@Theory
	public void testRemoveQueue(
			//@RandomForAll(sampleSize=1000) Queue q
			//@TomForAll @RandomCheck(sampleSize=1000) Queue q
			@ForSome(exhaustive=true, maxSampleSize=12) Queue q
			) {
        try {
        	SimpleQueue sq = SimpleQueue.makeFromQueue(q);
        	assumeTrue(sq.size() > 0);
    		System.out.println("remove " + q);
    		int oldSize = sq.size();
        	sq.remove();
        	assertEquals(oldSize - 1, sq.size());
        } catch(Exception ex) {
    		//System.out.println("elim " + q);

        }
	}

}