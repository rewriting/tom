package examples.shoppingcart;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Inventory {
	private Collection<LineItem> inventory;
	
	public Inventory() {
		inventory = new HashSet<LineItem>();
	}
	
	public void add(Item item, int quantity) {
		if (has(item)) {
			addItemQuantity(item, quantity);
		} else {
			addNewItem(item, quantity);
		}
	}
	
	private void addItemQuantity(Item item, int quantity) {
		for (Iterator<LineItem> iterator = inventory.iterator(); iterator.hasNext();) {
			LineItem lineItem = iterator.next();
			if (lineItem.getItem().equals(item)) {
				int oldQuantity = lineItem.getQuantity();
				lineItem.setQuantity(oldQuantity + quantity);
			}
		}
	}

	private void addNewItem(Item item, int quantity) {
		inventory.add(new LineItem(item, quantity));
	}
	
	public LineItem get(Item item, int quantity) throws InventoryException {
		if (has(item)) {
			return new LineItem(item, quantity);
		} else {
			throw new InventoryException("no item: " + item + " in inventory");
		}
	}
	
	public LineItem get(int id, int quantity) throws InventoryException {
		return get(new Item(id, 0), quantity);
	}
	
	public void delete(Item item) {
		inventory.remove(new LineItem(item, 0));
	}
	
	public void delete(int id) {
		delete(new Item(id, 0));
	}
	
	public boolean isEmpty(Item item) {
		boolean empty = true;
		for (Iterator<LineItem> iterator = inventory.iterator(); iterator.hasNext();) {
			LineItem lineItem = iterator.next();
			if (lineItem.getItem().equals(item)) {
				empty = lineItem.getQuantity() > 0;
			}
		}
		return empty;
	}
	
	public boolean isEmpty(int id) {
		return isEmpty(new Item(id, 0));
	}
	
	public int getItemQuantity(Item item) throws InventoryException {
		int quantity = Integer.MIN_VALUE;
		for (Iterator<LineItem> iterator = inventory.iterator(); iterator.hasNext();) {
			LineItem lineItem = iterator.next();
			if (lineItem.getItem().equals(item)) {
				quantity = lineItem.getQuantity();
			}
		}
		if (quantity == Integer.MIN_VALUE) {
			throw new InventoryException("No inventory found for item: " + item);
		}
		return quantity;
	}
	
	public int getItemQuantity(int id) throws InventoryException {
		return getItemQuantity(new Item(id, 0));
	}
	
	public boolean has(Item item) {
		return inventory.contains(new LineItem(item, 0));
	}
	
	public boolean has(int id) {
		return has(new Item(id, 0));
	}
}
