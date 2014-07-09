package examples.shop;

import java.util.ArrayList;
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
		/*
		 * bug revealed with test: testBuyWithNoQuantityInInventory.
		 * It should look if the inventory quantity is 0 first.
		 */
		int invItem = getInvQuantity(item);
		if (invItem <= 0) {
			return this;
		}
		/*
		 * bug revealed by test testBuyWithQuantityGreaterThanInventory.
		 * It should check if the requested quantity > inventory.
		 */
		// fix testBuyWithQuantityGreaterThanInventory
		int reducedQuantity = invItem < quantity ? invItem : quantity;
		//int reducedQuantity = quantity;
		inventory.add(new LineItem(item, reducedQuantity * -1));
		
		Cart cart = new Cart(id);
		cart.add(new LineItem(item, reducedQuantity));
		carts.add(cart);
		
		return this;
	}
	
	public int getInvQuantity(Item item) {
		// as the spec says empty inv = 0 instead -1
		// as in inventory
		// return inventory.getQuantity(item);
		// fix
		int quantity = inventory.getQuantity(item);
		return quantity < 0 ? 0 : quantity;
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
	
	public Inventory getInventory() {
		return inventory;
	}
	public Cart getLatestSell() {
		return carts.get(carts.size() - 1);
	}
	
	public boolean has(Item item) {
		return inventory.contains(item);
	}
}
