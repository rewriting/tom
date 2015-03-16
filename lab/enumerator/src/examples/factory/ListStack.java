package examples.factory;

import java.util.LinkedList;
import java.util.List;

import tom.library.factory.Enumerate;
import tom.library.factory.EnumerateBase;
import tom.library.factory.EnumerateBaseConstructor;
import tom.library.factory.EnumerateGenerator;

public class ListStack implements IStack {

	private List<Integer> stack;

	@EnumerateBaseConstructor
	public ListStack() {
		stack = new LinkedList<Integer>();
	}

	// used in the factory
	@Override
	public ListStack clone() {
		ListStack ls = new ListStack();
		for (Integer n : this.stack) {
			ls.stack.add(n);
		}
		return ls;
	}

	@Override
	@EnumerateBase
	public IStack empty() {
		return new ListStack();
	}

	@Override
	@EnumerateGenerator(canBeNull = false)
	public IStack push(@Enumerate(maxSize = 4) Integer elem) {
		stack.add(elem);
		return this;
	}

	@EnumerateGenerator
	public IStack extend() {
		this.stack.add(100);
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
		// Integer e = top();
		stack.remove(size() - 1);
		return this;
		// return e;
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
