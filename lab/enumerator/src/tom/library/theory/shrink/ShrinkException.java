package tom.library.theory.shrink;

public class ShrinkException extends Exception {

	private static final long serialVersionUID = 1L;

	public ShrinkException() {
		super("Shrinking failed");
	}
	
	public ShrinkException(String message) {
		super(message);
	}
}
