package examples.adt.stack;

import examples.adt.stack.stack.types.Elem;
import examples.adt.stack.stack.types.Stack;

public class StackFactory {
	private static StackFactory INSTANCE;

	private StackFactory() {
	}

	public static StackFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new StackFactory();
		}
		return INSTANCE;
	}

	public IStack makeStack(Stack s) {
		return stack2tomStack(s);
	}

	public Integer makeInteger(Elem e) {
		return elem2Integer(e);
	}

	// works only for s=empty | push(val(n),y)
	protected IStack stack2tomStack(Stack s) {
		IStack object = null;
		if (s.isempty()) {
			object = new TomStack();
		} else if (s.ispush()) {
			Elem elem = s.getelement();
			Stack y = s.getstack();
			Integer n = elem.getval();
			object = stack2tomStack(y);
			object.push(n);
		}
		return object;
	}

	protected Integer elem2Integer(Elem elem) {
		Integer n = elem.getval();
		return n;
	}

	// protected void evaluateStack(IStack object, Stack s) throws Exception {
	// %match(s) {
	// push(x, y) -> {
	// Elem e = null;
	// if (`x.isval()) {
	// e = `x;
	// } else {
	// e = `evaluateTop(x);
	// }
	// object.push(`x);
	// `evaluateStack(object, y);
	// }
	// }
	// }

	// protected Elem evaluateTop(Elem e) throws EmptyStackException {
	// %match(e) {
	// v@val(_) -> { return `v; }
	// top(push(x, y)) -> { return `x; }
	// }
	// throw new
	// EmptyStackException("Evaluate top on empty stack or bad term occured: " +
	// e);
	// }

	// public IStack createListStack(Stack s) throws Exception {
	// ListStack lStack = new ListStack();
	// evaluateStack(lStack, s);
	// return lStack;
	// }
}
