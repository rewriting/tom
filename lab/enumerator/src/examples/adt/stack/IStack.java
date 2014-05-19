package examples.adt.stack;

import examples.adt.stack.stackgom.types.Elem;
import examples.adt.stack.stackgom.types.Stack;

public interface IStack {
	public boolean isEmpty();
	public Elem top() throws EmptyStackException;
	public Elem pop() throws EmptyStackException;
	public void push(Elem elem);
	public int size();
	public Stack getStack();
}
