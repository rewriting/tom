package examples.shop;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Cart {
	private Collection<LineItem> lines;
	
	public Cart() {
		lines = new HashSet<LineItem>();
	}
	
	public void increaseQuantity(Item item) {
		for (Iterator<LineItem> iterator = lines.iterator(); iterator.hasNext();) {
			LineItem lineItem = iterator.next();
			if (lineItem.getItem().equals(item)) {
				int oldQuantity = lineItem.getQuantity();
				lineItem.setQuantity(oldQuantity + 1);
			}
		}
	}
	
	public void decreaseQuantity(Item item) {
		for (Iterator<LineItem> iterator = lines.iterator(); iterator.hasNext();) {
			LineItem lineItem = iterator.next();
			if (lineItem.getItem().equals(item)) {
				int oldQuantity = lineItem.getQuantity();
				lineItem.setQuantity(oldQuantity - 1);
			}
		}
	}
	
	public void addToCart(LineItem lineItem) {
		lines.add(lineItem);
	}
	
	public void addToCart(Item item, int quantity) {
		lines.add(new LineItem(item, quantity));
	}
	
	public void removeFromCart(Item item) {
		lines.remove(new LineItem(item, 0));
	}
	
//	public int getItemQuantity(Item item) throws InventoryException {
//		for (Iterator<LineItem> iterator = lines.iterator(); iterator.hasNext();) {
//			LineItem lineItem = iterator.next();
//			if (lineItem.getItem().equals(item)) {
//				return lineItem.getQuantity();
//			}
//		}
//		throw new InventoryException("Item: " + item + " not found in the cart");
//	}
	
	public Collection<LineItem> getLineItems() {
		return lines;
	}
}
