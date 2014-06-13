package examples.shop;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Cart {
	private int id;
	private Collection<LineItem> lines;
	
	public Cart(int id) {
		this.id = id;
		lines = new HashSet<LineItem>();
	}
	
	public Cart empty() {
		return new Cart(id);
	}
	

	public Cart add(LineItem lineItem) {
		lines.add(lineItem);
		return this;
	}
	
	public boolean contains(Item item) {
		return lines.contains(new LineItem(item, 0));
	}
	
	public int getQuantity(Item item) {
		int quantity = -1;
		for (Iterator<LineItem> iterator = lines.iterator(); iterator.hasNext();) {
			LineItem lineItem = iterator.next();
			if (lineItem.getItem().equals(item)) {
				quantity = lineItem.getQuantity();
			}
		}
		return quantity;
	}
	
	public void removeFromCart(Item item) {
		lines.remove(new LineItem(item, 0));
	}
	
	public boolean isEmpty() {
		return lines.isEmpty();
	}
	
	public int getId() {
		return id;
	}

//	public void addToCart(Item item, int quantity) {
//		lines.add(new LineItem(item, quantity));
//	}
//	public int getItemQuantity(Item item) throws InventoryException {
//		for (Iterator<LineItem> iterator = lines.iterator(); iterator.hasNext();) {
//			LineItem lineItem = iterator.next();
//			if (lineItem.getItem().equals(item)) {
//				return lineItem.getQuantity();
//			}
//		}
//		throw new InventoryException("Item: " + item + " not found in the cart");
//	}
	
//	public Collection<LineItem> getLineItems() {
//		return lines;
//	}
//	public void increaseQuantity(Item item) {
//	for (Iterator<LineItem> iterator = lines.iterator(); iterator.hasNext();) {
//		LineItem lineItem = iterator.next();
//		if (lineItem.getItem().equals(item)) {
//			int oldQuantity = lineItem.getQuantity();
//			lineItem.setQuantity(oldQuantity + 1);
//		}
//	}
//}
//
//public void decreaseQuantity(Item item) {
//	for (Iterator<LineItem> iterator = lines.iterator(); iterator.hasNext();) {
//		LineItem lineItem = iterator.next();
//		if (lineItem.getItem().equals(item)) {
//			int oldQuantity = lineItem.getQuantity();
//			lineItem.setQuantity(oldQuantity - 1);
//		}
//	}
//}

}
