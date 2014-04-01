package propcheck.shrink;

public class ShrinkException extends Exception {
	public ShrinkException() {
		super("Exceptions is raised during shrinking process");
	}
	
	public ShrinkException(String message) {
		super(message);
	}
}
