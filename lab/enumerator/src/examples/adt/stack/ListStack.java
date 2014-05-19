package examples.adt.stack;

import java.util.ArrayList;
import java.util.List;

import examples.adt.stack.stackgom.types.Elem;
import examples.adt.stack.stackgom.types.Stack;
import examples.adt.stack.stackgom.types.stack.empty;

public class ListStack implements IStack {

	private List<Elem> stack;
	
	public ListStack() {
		stack = new ArrayList<Elem>();
	}
	
	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public Elem top() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		return stack.get(stack.size() - 1);
	}

	@Override
	public Elem pop() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		Elem e = top();
		stack.remove(size() - 1);
		return e;
	}

	@Override
	public void push(Elem elem) {
		stack.add(elem);
	}

	@Override
	public int size() {
		return stack.size();
	}

	@Override
	public Stack getStack() {
		Stack s = empty.make();
		for (Elem e : stack) {
			s = examples.adt.stack.stackgom.types.stack.push.make(e, s);
		}
		return s;
	}

}
