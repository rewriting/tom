package examples.adt.stack;

public class ArrayStack implements IStack {

	private int[] stack;
	private int index;
	private int size;
	private final static int SIZE = 8;
	
	public ArrayStack() {
		stack = new int[SIZE];
		index = 0;
		size = 0;
	}

	@Override
	public IStack empty() {
		return new ArrayStack();
	}

	@Override
	public void push(Integer elem) {
		stack[index++] = elem;
		index %= SIZE;
		size++;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Integer top() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		return stack[index];
	}

	@Override
	public Integer pop() throws EmptyStackException {
		Integer res = top();
		index = (index - 1) % SIZE;
		size--;
		return res;
	}

	@Override
	public int size() {
		return size;
	}

	// could be moved to the interface
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		IStack other = (IStack) obj;
		if (this.isEmpty()) {
			return other.isEmpty();
		} else {
			try {
				if (this.top().equals(other.top())) {
					this.pop();
					other.pop();
					return this.equals(other);
				} else {
					return false;
				}
			} catch (EmptyStackException e) {
				return false;
			}
		}
	}

}
