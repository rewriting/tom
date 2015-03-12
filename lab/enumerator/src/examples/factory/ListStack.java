package examples.factory;

import java.util.LinkedList;
import java.util.List;

import tom.library.factory.EnumerateGenerator;

public class ListStack implements IStack {

	private List<Integer> stack;

	public ListStack() {
		stack = new LinkedList<Integer>();
	}

	@Override
	@EnumerateGenerator(canBeNull = false)
	public IStack empty() {
		return new ListStack();
	}

	@Override
	@EnumerateGenerator(canBeNull = false)
	public IStack push(Integer elem) {
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
	public IStack pop() throws EmptyStackException {
//		Integer e = top();
		stack.remove(size() - 1);
		return this;
//		return e;
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

	@Override
	public String toString() {
		return "ListStack [stack=" + stack + "]";
	}

	
}
