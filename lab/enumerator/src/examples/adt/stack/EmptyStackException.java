package examples.adt.stack;

public class EmptyStackException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public EmptyStackException() {
		super("Stack is empty");
	}
	
	public EmptyStackException(String message) {
		super(message);
	}
}
