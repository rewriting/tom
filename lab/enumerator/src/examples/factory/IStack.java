package examples.factory;

public interface IStack {
	public IStack empty();
	public IStack push(Integer elem);
	public boolean isEmpty();
	public Integer top() throws EmptyStackException;
	public IStack pop() throws EmptyStackException;
	public int size();
}
