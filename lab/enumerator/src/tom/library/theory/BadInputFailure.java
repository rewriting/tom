package tom.library.theory;

/**
 * Marks that the generated value in a test is not good for testing.
 * The test will be skipped, makes it doesn't count.
 * 
 * @author nauval
 *
 */
public class BadInputFailure extends Throwable {

	private static final long serialVersionUID = 1L;
	
	public BadInputFailure() {
		super("Bad input, the test is skipped");
	}
	
	public BadInputFailure(String message) {
		super(message);
	}
}
