package examples.adt.queue;

import java.math.BigInteger;
import java.util.LinkedList;

import tom.library.enumerator.Enumeration;
import examples.adt.queue.queuelanguage.types.*;
import examples.adt.queue.queuelanguage.types.Queue;

public class ListQueue {
	private LinkedList<Integer> list = new LinkedList<Integer>();

	//private ListQueue() {}
	
	public static ListQueue makeFromQueue(Queue q) {	
		if(q.isempty()) {
			return new ListQueue();
		} else if(q.isadd()) {
			ListQueue res = makeFromQueue(q.getq());
			res.add(q.gete().getv());
			return res;
		} else if(q.isremove()) {
			ListQueue res = makeFromQueue(q.getq());
			res.remove();
			return res;
		}
		throw new RuntimeException("should not be there");	
	}
	
	public static int makeFromElem(Elem e) throws Exception {
		if(e.isval()) {
			return e.getv();
		}/* else if(e.istop()) {
			ListQueue sq = makeFromQueue(e.getq());
			if(sq.size() > 0) {
				return sq.top();
			} else {
				throw new Exception();
			}
		}*/
		throw new RuntimeException("should not be there");	
	}
	
	public int top() {
		if(!list.isEmpty()) {
			return list.getLast();
		}
		throw new RuntimeException("should not be there");	
	}
	
	public void remove() {
		list.removeLast();
	}
	
	public void add(int e) {
		list.addFirst(e);
	}
	
	public int size() {
		return list.size();
	}
	

	public static void main(String[] args) {
		int nb = 2000;
		BigInteger TWO = BigInteger.ONE.add(BigInteger.ONE);
		
		Enumeration<Queue> e = Queue.getEnumeration();
		long start = System.currentTimeMillis();
		BigInteger index = BigInteger.ONE;
		for(int i = 0 ; i<nb ; i++) {
			Queue q = e.get(index);
			//System.out.println(q);
			
			//index = index.add(TWO);
			index = index.multiply(TWO);
		}
		long stop = System.currentTimeMillis();

		System.out.println(nb + " elems in " + (stop-start));
	}
}
