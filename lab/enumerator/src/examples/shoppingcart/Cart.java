package examples.shoppingcart;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Cart {
	private Collection<LineItem> items;
	
	public Cart() {
		items = new HashSet<LineItem>();
	}
	
	public void increaseQuantity(Item item) {
		for (Iterator<LineItem> iterator = items.iterator(); iterator.hasNext();) {
			LineItem lineItem = iterator.next();
			if (lineItem.getItem().equals(item)) {
				int oldQuantity = lineItem.getQuantity();
				lineItem.setQuantity(oldQuantity + 1);
			}
		}
	}
	
	public void decreaseQuantity(Item item) {
		for (Iterator<LineItem> iterator = items.iterator(); iterator.hasNext();) {
			LineItem lineItem = iterator.next();
			if (lineItem.getItem().equals(item)) {
				int oldQuantity = lineItem.getQuantity();
				lineItem.setQuantity(oldQuantity - 1);
			}
		}
	}
	
	public void addToCart(LineItem lineItem) {
		items.add(lineItem);
	}
	
	public void addToCart(Item item, int quantity) {
		items.add(new LineItem(item, quantity));
	}
	
	public void removeFromCart(Item item) {
		items.remove(new LineItem(item, 0));
	}
	
//	public int getItemQuantity(Item item) throws InventoryException {
//		for (Iterator<LineItem> iterator = items.iterator(); iterator.hasNext();) {
//			LineItem lineItem = iterator.next();
//			if (lineItem.getItem().equals(item)) {
//				return lineItem.getQuantity();
//			}
//		}
//		throw new InventoryException("Item: " + item + " not found in the cart");
//	}
	
	public Collection<LineItem> getLineItems() {
		return items;
	}
}
