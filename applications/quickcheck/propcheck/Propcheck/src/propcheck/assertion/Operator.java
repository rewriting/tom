package propcheck.assertion;

public class Operator {
	
	public static void equalsTo(Object actual, Object expected) {
		if (!actual.equals(expected)) {
			failNotEqual(actual, expected);
		}
	}
	
	/**
	 * the semantic of implies is if left holds than examine right,
	 * so it is non-commutative & in java
	 * @param leftPredicate
	 * @param rightPredicate
	 * @return
	 */
	public static void implies(boolean leftPredicate, boolean rightPredicate) {
		if (!leftPredicate) {
			skipNotTested(null);
		} else {
			if (!rightPredicate) {
				failImplication();
			}
		}
	}
	
	private static void skipNotTested(String message) {
		if (message == null) {
			throw new NotTestedSkip();
		}
		throw new NotTestedSkip(message);
	}
	
	private static void failNotEqual(Object actual, Object expected) {
		fail(formatNotEqual(actual, expected));
	}
	
	private static void failImplication() {
		fail("implication test fails");
	}
	
	private static String formatNotEqual(Object actual, Object expected) {
		return actual + " is not equal to " + expected;
	}
	
	private static void fail(String message) {
		if (message == null) {
			throw new AssertionError();
		}
		throw new AssertionError(message);
	}
}
