package zipper;

public class ZipperException extends Exception {
	public ZipperException() {
		super("Exception raised while processing the zipper");
	}
	
	public ZipperException(String message) {
		super(message);
	}
}
