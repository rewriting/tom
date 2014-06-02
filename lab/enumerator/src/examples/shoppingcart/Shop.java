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
	
	public Shop buy(Cart cart) {
		// re-calculate the quantity
		for (LineItem lineItem : cart.getLineItems()) {
			try {
				inventory.get(lineItem.getItem(), lineItem.getQuantity());
			} catch (InventoryException e) {
				// do nothing
			}
		}
		// put the cart in the list
		carts.add(cart);
		return this;
	}
	
	public Shop addInventory(Item item, int quantity) {
		inventory.add(item, quantity);
		return this;
	}
	
	public int getInventoryQuantity(Item item) {
		try {
			return inventory.getItemQuantity(item);
		} catch (InventoryException e) {
			return 0;
		}
	}
	
	public Shop addInventory(Collection<LineItem> items) {
		inventory.addAll(items);
		return this;
	}
	
	public Shop setInventory(Inventory inventory) {
		this.inventory = inventory;
		return this;
	}
}
