package tom.library.shrink.ds.zipper;

public class ZipperException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ZipperException() {
		super("Exception raised while processing the zipper");
	}
	
	public ZipperException(String message) {
		super(message);
	}
}
