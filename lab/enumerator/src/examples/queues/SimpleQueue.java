package examples.queues;

import java.util.LinkedList;

import examples.queues.queue.types.*;

public class SimpleQueue {
	private LinkedList<Integer> list = new LinkedList<Integer>();

	//private SimpleQueue() {}
	
	public static SimpleQueue makeFromQueue(Queue q) {	
		if(q.isempty()) {
			return new SimpleQueue();
		} else if(q.isadd()) {
			SimpleQueue res = makeFromQueue(q.getq());
			res.add(q.gete().getv());
			return res;
		} else if(q.isremove()) {
			SimpleQueue res = makeFromQueue(q.getq());
			res.remove();
			return res;
		}
		throw new RuntimeException("should not be there");	
	}
	
	public static int makeFromElem(Elem e) throws Exception {
		if(e.isval()) {
			return e.getv();
		} else if(e.istop()) {
			SimpleQueue sq = makeFromQueue(e.getq());
			if(sq.size() > 0) {
				return sq.top();
			} else {
				throw new Exception();
			}
		}
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
	
}
