package examples.adt.stack;

import java.util.ArrayList;
import java.util.List;

import examples.adt.stack.stackgom.types.Elem;
import examples.adt.stack.stackgom.types.Stack;
import examples.adt.stack.stackgom.types.stack.empty;

public class ListStack implements IStack {

	private List<Integer> stack;
	
	public ListStack() {
		stack = new ArrayList<Integer>();
	}
	
	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public Integer top() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		return stack.get(stack.size() - 1);
	}

	@Override
	public Integer pop() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		Integer e = top();
		stack.remove(size() - 1);
		return e;
	}

	@Override
	public void push(Integer elem) {
		stack.add(elem);
	}

	@Override
	public int size() {
		return stack.size();
	}

	@Override
	public IStack empty() {
		return new ListStack();
	}

//	@Override
//	public Stack getStack() {
//		Stack s = empty.make();
//		for (Integer e : stack) {
//			s = examples.adt.stack.stackgom.types.stack.push.make(e, s);
//		}
//		return s;
//	}

}
