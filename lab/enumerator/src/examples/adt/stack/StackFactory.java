package examples.adt.stack;

import examples.adt.stack.stack.types.Elem;
import examples.adt.stack.stack.types.Stack;

public class StackFactory {
	private static StackFactory TOM_INSTANCE;
	private static StackFactory LIST_INSTANCE;
	public static int TOM = 1;
	public static int LIST = 2;

	private IStack objectTOM = null;
	private IStack objectLIST = null;

	private StackFactory(int type) {
		if (type == TOM) {
			objectTOM = new TomStack();
		} else if (type == LIST) {
			objectLIST = new ListStack();
		}
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
		}
		// to do it better
		return null;
	}

	public IStack makeStack() {
		if (objectTOM != null) {
			return objectTOM;
		} else if (objectLIST != null) {
			return objectLIST;
		}
		// to do it better
		return null;
	}

	public IStack makeStack(Stack s) {
		IStack object = null;
		if (objectTOM != null) {
			object = objectTOM;
		} else if (objectLIST != null) {
			object =  objectLIST;
		}
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
