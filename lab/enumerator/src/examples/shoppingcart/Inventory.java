package examples.shoppingcart;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Inventory {
	private Collection<LineItem> inventory;
	
	public Inventory() {
		inventory = new HashSet<LineItem>();
	}
	
	public Inventory add(Item item, int quantity) {
		if (has(item)) {
			addItemQuantity(item, quantity);
		} else {
			addNewItem(item, quantity);
		}
		return this;
	}
	
	private Inventory addItemQuantity(Item item, int quantity) {
		for (Iterator<LineItem> iterator = inventory.iterator(); iterator.hasNext();) {
			LineItem lineItem = iterator.next();
			if (lineItem.getItem().equals(item)) {
				/*
				 * lineItem.setQuantity(quantity); 
				 */
				
				int oldQuantity = lineItem.getQuantity();
				lineItem.setQuantity(oldQuantity + quantity);
			}
		}
		return this;
	}

	private Inventory addNewItem(Item item, int quantity) {
		inventory.add(new LineItem(item, quantity));
		return this;
	}
	
	public LineItem get(Item item, int quantity) throws InventoryException {
		if (has(item)) {
			/**
			 * fix bug revealed by testInventoryGetItemFromInventoryWithQuantity()
			 */
			reduceItemQuantity(item, quantity);
			return new LineItem(item, quantity);
		} else {
			throw new InventoryException("no item: " + item + " in inventory");
		}
	}
	
	private void reduceItemQuantity(Item item, int quantity) {
		for (Iterator<LineItem> iterator = inventory.iterator(); iterator.hasNext();) {
			LineItem lineItem = iterator.next();
			if (lineItem.getItem().equals(item)) {
				int oldQuantity = lineItem.getQuantity();
				lineItem.setQuantity(oldQuantity - quantity);
			}
		}
	}
	
	public LineItem get(int id, int quantity) throws InventoryException {
		return get(new Item(id, 0), quantity);
	}
	
	public Inventory delete(Item item) {
		inventory.remove(new LineItem(item, 0));
		return this;
	}
	
	public Inventory delete(int id) {
		delete(new Item(id, 0));
		return this;
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
	
	public Inventory addAll(Collection<LineItem> items) {
		inventory.addAll(items);
		return this;
	}
	
	public Collection<LineItem> getInventory() {
		return inventory;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inventory == null) ? 0 : inventory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Inventory)) {
			return false;
		}
		Inventory other = (Inventory) obj;
		if (inventory == null) {
			if (other.inventory != null) {
				return false;
			}
		} else if (!inventory.equals(other.inventory)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (LineItem li : inventory) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(li);
		}
		return "[" + sb.toString() + "]";
	}
}
