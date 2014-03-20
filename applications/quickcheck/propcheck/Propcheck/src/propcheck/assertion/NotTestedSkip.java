package propcheck.assertion;

public class NotTestedSkip extends AssertionError {
	public NotTestedSkip() {
		super("The test is skipped");
	}
	
	public NotTestedSkip(String message) {
		super(message);
	}
}
