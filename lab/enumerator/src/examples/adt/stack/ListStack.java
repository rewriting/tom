package examples.adt.stack;

import java.util.LinkedList;
import java.util.List;

public class ListStack implements IStack {

	private List<Integer> stack;

	public ListStack() {
		stack = new LinkedList<Integer>();
	}

	@Override
	public IStack empty() {
		return new ListStack();
	}

	@Override
	public ListStack push(Integer elem) {
		stack.add(elem);
		return this;
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
		Integer e = top();
		stack.remove(size() - 1);
		return e;
	}

	@Override
	public int size() {
		return stack.size();
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
		IStack other = (IStack) obj;
		if (this.isEmpty()) {
			return other.isEmpty();
		} else {
			try {
				if (this.top().equals(other.top())) {
					this.pop();
					other.pop();
					return this.equals(other);
				} else {
					return false;
				}
			} catch (EmptyStackException e) {
				return false;
			}
		}
	}

}
