package examples.adt.queue;

import examples.adt.queue.queue.types.*;

public class SimpleQueue {
	%include{ queue/Queue.tom }
	
	private Queue queue;

	private SimpleQueue() {
		queue = `empty();
	}
	
	private SimpleQueue(Queue queue) {
		this.queue = queue;
	}
	
	public static SimpleQueue createQueue() {
		return new SimpleQueue();
	}

	public static SimpleQueue createQueue(Queue queue) {
		return new SimpleQueue(queue);
	}

	public boolean isEmpty() {
		return queue.isempty();
	}

	public void enqueue(Elem element) {
		Queue tmp = queue;
		queue = `list(element, tmp);
	}

	public Elem front() throws EmptyQueueException {
		return getFront(queue);
	}

	protected Elem getFront(Queue q) throws EmptyQueueException {
		%match(q) {
			list(x,y) -> {
				if(`y == `empty())
					return `x;
				else
					return `getFront(y);
			}
		}
		throw new EmptyQueueException(); 
	}

	public Elem dequeue() throws EmptyQueueException {
		Elem elem = getFront(queue);
		queue = removeFirst(queue);
		return elem;
	}
	
	protected Queue removeFirst(Queue q) throws EmptyQueueException {
		%match(q) {
			list(_, empty()) -> { return `empty(); }
			list(x, list(_, empty())) -> { return `list(x, empty()); }
			list(x,y) -> { return `list(x,removeFirst(y)); }
		}
		throw new EmptyQueueException();
	}

	public Queue getQueue() {
		return queue;
	}

	public int size() {
		return getSize(queue);
	}

	protected int getSize(Queue q) {
		%match(q) {
			empty() -> { return 0; }
			list(x, y) -> { 
				if(`y == `empty())	
					return 1;
				else
					return 1 + `getSize(y); 
			}
		}
		return -1;
	}
	
	public static class EmptyQueueException extends Exception {
		public EmptyQueueException() {
			super("Queue is empty.");
		}

		public EmptyQueueException(String message) {
			super(message);
		}
	}
} 

