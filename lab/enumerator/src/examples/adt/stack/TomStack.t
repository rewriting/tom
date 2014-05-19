package examples.adt.stack;

import examples.adt.stack.stackgom.types.*;

public class TomStack implements IStack {

	%include{ stackgom/StackGom.tom }

	private Stack stack;

	public TomStack() {
		stack = `empty();
	}

	@Override
	public boolean isEmpty() {
		return stack.isempty();
	}

	@Override
	public Elem top() throws EmptyStackException {
		%match(stack) {
			push(x, y) -> { return `x; }
		}
		throw new EmptyStackException();
	}

	@Override
	public Elem pop() throws EmptyStackException {
		%match(stack) {
			push(x, y) -> { 
				stack = `y;
				return `x;
			}
		}
		throw new EmptyStackException();
	}

	@Override
	public void push(Elem elem) {
		stack = `push(elem, stack);
	}

	@Override
	public int size() {
		%match(stack) {
			push(x, y) -> { return 1 + `calculateSize(y); }
		}
		return 0;
	}

	private int calculateSize(Stack s) {
		%match(s) {
			push(x, y) -> { return 1 + `calculateSize(y); }
		}
		return 0;
	}

	@Override
	public Stack getStack() {
		return stack;
	}
}

