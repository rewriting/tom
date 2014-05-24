package examples.adt.stack;

import tom.library.theory.BadInputException;
import examples.adt.queue.queue.types.Elem;
import examples.adt.stack.stack.types.Stack;
import examples.adt.stack.stacklanguage.types.ElemL;
import examples.adt.stack.stacklanguage.types.StackL;

public class StackFactory {
	private static StackFactory TOM_INSTANCE;
	private static StackFactory LIST_INSTANCE;
	private static StackFactory ARRAY_INSTANCE;

	public final static int TOM = 1;
	public final static int LIST = 2;
	public final static int ARRAY = 3;

	private int type = -1;

	private StackFactory(int type) {
		this.type = type;
	}

	public static StackFactory getInstance(int type) {
		if (type == TOM) {
			if (TOM_INSTANCE == null) {
				TOM_INSTANCE = new StackFactory(type);
			}
			return TOM_INSTANCE;
		} else if (type == LIST) {
			if (LIST_INSTANCE == null) {
				LIST_INSTANCE = new StackFactory(type);
			}
			return LIST_INSTANCE;
		} else if (type == ARRAY) {
			if (ARRAY_INSTANCE == null) {
				ARRAY_INSTANCE = new StackFactory(type);
			}
			return ARRAY_INSTANCE;
		}
		// to do it better
		return null;
	}

	public IStack makeStack() {
		IStack object = null;
		if (this.type == TOM) {
			object = new TomStack();
		} else if (this.type == LIST) {
			object = new ListStack();
		} else if (this.type == ARRAY) {
			object = new ArrayStack();
		}
		// to do it better
		return object;
	}

	public IStack makeStack(Stack s) {
		IStack object = makeStack();
		if (s.isempty()) {
			object = object.empty(); // or makeStack() directly
		} else if (s.ispush()) {
			Stack y = s.getstack();
			Integer n = s.getelement();
			object = makeStack(y);
			object.push(n);
		}
		return object;
	}


	public IStack evaluateStack(StackL s) throws BadInputException {
		IStack object = null;
		if (this.type == TOM) {
			object = new TomStack();
		} else if (this.type == LIST) {
			object = new ListStack();
		} else if (this.type == ARRAY) {
			object = new ArrayStack();
		}

		if (s.isempty()) {
			object = object.empty();
		} else if (s.ispush()) {
			ElemL elem = s.getelement();
			StackL y = s.getstack();
			Integer n = elem.getval();
			object = evaluateStack(y);
			object.push(n);
		} else if (s.ispop()) {
			StackL y = s.getstack();
			object = evaluateStack(y);
			try {
				object.pop();
			} catch (EmptyStackException e) {
				// throw an exception to skip the particular test
				throw new BadInputException();
			}
		}
		return object;
	}
}