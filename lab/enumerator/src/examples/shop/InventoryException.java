package examples.shop;

public class InventoryException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InventoryException() {
		super("Error while running an operation on Inventory");
	}
	
	public InventoryException(String message) {
		super(message);
	}
}
