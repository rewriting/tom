package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;
import examples.adt.queue.ListQueue;
import examples.adt.queue.queuelanguage.types.Elem;
import examples.adt.queue.queuelanguage.types.Queue;


@RunWith(PropCheck.class)
public class ListQueueTest {
	
	//@Theory
	public void testInsertElemEmpty(@ForSome(maxSampleSize=100) Elem e) {
        ListQueue sq = new ListQueue();
        try {
        	int v = ListQueue.makeFromElem(e);
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
        	ListQueue sq = ListQueue.makeFromQueue(q);
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
        	ListQueue sq = ListQueue.makeFromQueue(q);
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
