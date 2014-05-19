package examples.adt.stack;

import examples.adt.stack.stack.types.*;

public class StackEvaluator {
	%include{ stack/Stack.tom }

	public static Stack createEmpty() {
		return `empty();
	}

	public static boolean isEmpty(Stack stack) {
		return stack.isempty();
	}

	public static Elem top(Stack stack) throws EmptyStackException {
		%match(stack) {
			push(x, _) -> { return `x; }
// 			pop(x) -> { return `top(x); }
		} 
		throw new EmptyStackException();
	}

	public static Stack push(Stack stack, Elem element) {
		return `push(element, stack);
	}

	public static Stack pop(Stack stack)  throws EmptyStackException {
		%match(stack) {
			push(_, y) -> { return `y; }
		}
		throw new EmptyStackException();
	}

	public static Stack evaluate(Stack stack) {
		%match(stack) {
			empty() -> { return `empty(); }
// 			pop(x) -> { return `evaluate(x); }
			push(x, y) -> { return `push(x, evaluate(y)); }
		}
		throw new RuntimeException("bad term " + stack);
	}

	public static int size(Stack stack) {
		%match(stack) {
			empty() -> { return 0; }
			push(_, y) -> { return 1 + `size(y); }
// 			pop(y) -> { return `size(y)-1; }
		}
		throw new RuntimeException("bad term " + stack);
	}

	public static class EmptyStackException extends Exception {
		public EmptyStackException() {
			super("Stack is empty, cannot do further operation");
		}

		public EmptyStackException(String message) {
			super(message);
		}
	}
}
