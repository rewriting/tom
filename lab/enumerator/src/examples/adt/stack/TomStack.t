package examples.adt.stack;

import examples.adt.stack.stack.types.*;

public class TomStack implements IStack {

	%include{ stack/Stack.tom }

	private Stack stack;

	public TomStack() {
		stack = `empty();
	}

	@Override
	public  IStack empty() {
		return new TomStack();
	}

	@Override
	public void push(Integer elem) {
		stack = `push(val(elem), stack);
	}

	@Override
	public boolean isEmpty() {
		return stack.isempty();
	}

	@Override
	public Integer top() throws EmptyStackException {
		%match(stack) {
			push(val(x), y) -> { return `x; }
		}
		throw new EmptyStackException();
	}

	@Override
	public Integer pop() throws EmptyStackException {
		%match(stack) {
			push(val(x), y) -> { 
				stack = `y;
				return `x;
			}
		}
		throw new EmptyStackException();
	}

	@Override
	public int size() {
		return calculateSize(stack);
	}

	private int calculateSize(Stack s) {
		%match(s) {
			push(x, y) -> { return 1 + `calculateSize(y); }
		}
		return 0;
	}


// 	@Override
// 	public Stack getStack() {
// 		return stack;
// 	}

}

