package examples.shop;

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
		Cart boughtItems = new Cart();
		// re-calculate the quantity
		for (LineItem lineItem : cart.getLineItems()) {
			try {
				/*
				 * Bug after revealed after fix for inventoryNeverNegative()
				 */
				//inventory.get(lineItem.getItem(), lineItem.getQuantity())
				boughtItems.addToCart(inventory.get(lineItem.getItem(), lineItem.getQuantity()));
			} catch (InventoryException e) {
				// do nothing
			}
		}
		// put the cart in the list
		//carts.add(cart);
		carts.add(boughtItems);
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
	
	public Cart getLatestSell() {
		return carts.get(carts.size() - 1);
	}
	
	public boolean has(Item item) {
		return inventory.has(item);
	}
}
