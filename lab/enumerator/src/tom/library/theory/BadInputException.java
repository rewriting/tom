package tom.library.theory;

/**
 * Marks that the generated value in a test is not good for testing.
 * The test will be skipped, makes it doesn't count.
 * 
 * @author nauval
 *
 */
public class BadInputException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BadInputException() {
		super("Bad input, the test is skipped");
	}
	
	public BadInputException(String message) {
		super(message);
	}
}
