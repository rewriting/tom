package examples.shoppingcart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Shop {
	private Inventory inventory;
	private List<Cart> carts;
	
	public Shop() {
		inventory = new Inventory();
		carts = new ArrayList<Cart>();
	}
	
	public void buy(Cart cart) throws InventoryException {
		// re-calculate the quantity
		for (LineItem lineItem : cart.getLineItems()) {
			inventory.get(lineItem.getItem(), lineItem.getQuantity());
		}
		// put the cart in the list
		carts.add(cart);
	}
	
	public void addInventory(Item item, int quantity) {
		inventory.add(item, quantity);
	}
	
	public int getInventoryQuantity(Item item) throws InventoryException {
		return inventory.getItemQuantity(item);
	}
	
	public void addInventory(Collection<LineItem> items) {
		inventory.addAll(items);
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
}
