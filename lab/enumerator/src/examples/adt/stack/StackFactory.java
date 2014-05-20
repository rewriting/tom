package examples.adt.stack;

import examples.adt.stack.stack.types.Elem;
import examples.adt.stack.stack.types.Stack;

public class StackFactory {
	private static StackFactory INSTANCE;
	public static int TOM = 1;
	public static int LIST = 2;
	public static int ARRAY = 3;

	private IStack object = null;

	private StackFactory(int type) {
		if (type == TOM) {
			object = new TomStack();
		} else if (type == LIST) {
			object = new ListStack();
		} else if (type == ARRAY) {
			object = new ArrayStack();
		}
	}

	public static StackFactory getInstance(int type) {
		if (INSTANCE == null) {
			INSTANCE = new StackFactory(type);
		}
		return INSTANCE;
	}

	public IStack makeStack() {
		return object;
	}

	public IStack makeStack(Stack s) {
		if (s.isempty()) {
			object = object.empty();
		} else if (s.ispush()) {
			Elem elem = s.getelement();
			Stack y = s.getstack();
			Integer n = elem.getval();
			object = makeStack(y);
			object.push(n);
		}
		return object;
	}

	public Integer makeInteger(Elem elem) {
		Integer n = elem.getval();
		return n;
	}

}
