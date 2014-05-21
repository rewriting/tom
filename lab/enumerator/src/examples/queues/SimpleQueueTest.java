package examples.queues;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;
import org.junit.Assume;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.junit.Assert.fail;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.ExhaustiveCheck;
import tom.library.theory.ExhaustiveForAll;
import tom.library.theory.RandomCheck;
import tom.library.theory.RandomForAll;
import tom.library.theory.TomCheck;
import tom.library.theory.TomForAll;
import examples.junit.quickcheck.ExpGenerator;
import examples.queues.queue.types.*;

@RunWith(TomCheck.class)
public class SimpleQueueTest {
	
	@Enum public static Enumeration<Queue> enumeration1 = Queue.getEnumeration();
	@Enum public static Enumeration<Elem> enumeration2 = Elem.getEnumeration();
	@Enum public static Enumeration<Integer> enumeration5 = Combinators.makeint();
	
	//@Theory
	public void testInsertElemEmpty(@RandomForAll(sampleSize=100) Elem e) {
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
			@RandomForAll(sampleSize=100) Integer v,
			@RandomForAll(sampleSize=100) Queue q
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
			@TomForAll @ExhaustiveCheck(maxSampleSize=12) Queue q
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