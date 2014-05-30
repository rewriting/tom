package examples.adt.stack;

import java.util.Arrays;

public class ArrayStack implements IStack {

	private int[] stack;
	private int index, size;
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
	public ArrayStack push(Integer elem) {
		stack[index] = elem;
		index = (index+1) % SIZE;
		size++;
		return this;
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
		// initial code:
// 		return stack[index];
// 		return stack[index-1];
		return stack[(SIZE+index-1)%SIZE]; // -1 to fix the BUG
	}

	@Override
	public ArrayStack pop() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		index = (index - 1) % SIZE;
		size--;
		return this;
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

	@Override
	public String toString() {
		return "ArrayStack [stack=" + Arrays.toString(stack) + ", index="
				+ index + ", size=" + size + "]";
	}

	public static void main(String[] args) throws EmptyStackException {
		IStack stack = new ArrayStack();
		stack = stack.empty();
		stack.push(1);
		System.out.println(stack.top());

		stack = stack.empty();
		stack.push(2);
		System.out.println(stack);
		System.out.println("Before: "+stack.top());
		stack.push(1);
		stack.pop();
		System.out.println("After :"+stack.top());

		int n=5;
		System.out.println("After :"+int.class);
		
	}

}
