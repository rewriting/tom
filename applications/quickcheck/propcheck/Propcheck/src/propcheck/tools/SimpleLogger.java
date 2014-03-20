package propcheck.tools;

public class SimpleLogger {
	// two space for tab
	private static final String TAB = "  ";
	public static void log(String message) {
		System.out.println(message);
	}
	
	public static void log(int num, String message) {
		System.out.printf("[%s] %s\n", num, message);
	}
	
	public static void logTabbed(String message) {
		System.out.printf("%s%s\n" + TAB, message);
	}
}
 