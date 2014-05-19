package examples.adt.stack;

//import examples.adt.stack.stackgom.types.Elem;
//import examples.adt.stack.stackgom.types.Stack;

public interface IStack {
	public IStack empty();
	public void push(Integer elem);
	public boolean isEmpty();
	public Integer top() throws EmptyStackException;
	public Integer pop() throws EmptyStackException;
	public int size();
//	public Stack getStack();
}
