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
	
	public Shop empty() {
		return new Shop();
	}
	
	public Shop buy(int id, Item item, int quantity) {
		inventory.add(new LineItem(item, -quantity));
		Cart cart = new Cart(id);
		cart.add(new LineItem(item, quantity));
		carts.add(cart);
		return this;
	}
	
	public int getInvQuantity(Item item) {
		return inventory.getQuantity(item);
	}
	
	public int getCartQuantity(Item item) {
		int sum = 0;
		for (Cart cart : carts) {
			sum += cart.getQuantity(item);
		}
		return sum;
	}
	
	public boolean isEmpty() {
		return inventory.isEmpty();
	}
	
	public Inventory inventory() {
		return inventory;
	}
//	public Shop buy(Cart cart) {
//		Cart boughtItems = new Cart();
//		// re-calculate the quantity
//		for (LineItem lineItem : cart.getLineItems()) {
//			try {
//				/*
//				 * Bug after revealed after fix for inventoryNeverNegative()
//				 */
//				//inventory.get(lineItem.getItem(), lineItem.getQuantity())
//				boughtItems.addToCart(inventory.get(lineItem.getItem(), lineItem.getQuantity()));
//			} catch (InventoryException e) {
//				// do nothing
//			}
//		}
//		// put the cart in the list
//		//carts.add(cart);
//		carts.add(boughtItems);
//		return this;
//	}
	
//	public Shop addInventory(Item item, int quantity) {
//		inventory.add(item, quantity);
//		return this;
//	}
	
//	public int getInventoryQuantity(Item item) {
//		try {
//			return inventory.getQuantity(item);
//		} catch (InventoryException e) {
//			return 0;
//		}
//	}
	
//	public Shop addInventory(Collection<LineItem> items) {
//		inventory.addAll(items);
//		return this;
//	}
	
	public Shop setInventory(Inventory inventory) {
		this.inventory = inventory;
		return this;
	}
	
	public Cart getLatestSell() {
		return carts.get(carts.size() - 1);
	}
	
	public boolean has(Item item) {
		return inventory.contains(item);
	}
}
