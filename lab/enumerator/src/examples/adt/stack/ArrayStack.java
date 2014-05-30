package examples.adt.stack;

import java.util.Arrays;

import examples.adt.stack.stack.types.Stack;

public class ArrayStack implements IStack {

	private int[] stack;
	private int index, size;
	private final static int SIZE = 5;

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

	@Override
	public ArrayStack clone(){
		ArrayStack clone = new ArrayStack();
		clone.size=this.size;
		clone.index=this.index;
		for(int i=0; i<SIZE; i++){
			clone.stack[i]=this.stack[i];
		}
		return clone;
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
		ArrayStack other = (ArrayStack) obj;
		ArrayStack otherc = other.clone();
		ArrayStack thisc = this.clone();
		if (thisc.isEmpty()) {
			return otherc.isEmpty();
		} else {
			try {
				if (thisc.top().equals(otherc.top())) {
					thisc.pop();
					otherc.pop();
					return thisc.equals(otherc);
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
		stack.push(1).push(2).push(3).push(4).push(5);
		System.out.println(stack);

		stack = stack.empty();
		stack.push(2);
		System.out.println(stack);
		System.out.println("Before: "+stack.top());
		stack.push(1);
		stack.pop();
		System.out.println("After :"+stack.top());

		int n=5;
		System.out.println("After :"+int.class);
		
		 StackFactory factory = StackFactory.getInstance(StackFactory.ARRAY);
		 IStack s = factory.makeStack( Stack.fromString("push(push(push(push(push(empty(),1),-1),0),0),0)"));
			System.out.println(s);
		
	}

}
