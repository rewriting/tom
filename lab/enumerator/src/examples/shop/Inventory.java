package examples.shop;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Inventory {
	//private Collection<LineItem> items;
	private Map<Item, Integer> items;

	public Inventory() {
		//items = new HashSet<LineItem>();
		items = new HashMap<Item, Integer>();
	}

	public Inventory empty() {
		return new Inventory();
	}
	
	public Inventory add(LineItem lineItem) {
		/*
		 * bug found, test testGetQuantityItemInInventory
		 * add item directly instead of add the quantity if exist
		 */
		// fix
		if (contains(lineItem.getItem())) {
			addLineItemQuantity(lineItem);
		} else {
			//items.add(lineItem);
			items.put(lineItem.getItem(), lineItem.getQuantity());
		}
		return this;
	}

	private void addLineItemQuantity(LineItem lineItem) {
		int quantity = items.get(lineItem.getItem()) + lineItem.getQuantity();
		items.put(lineItem.getItem(), quantity);
	}

	public Inventory delete(Item item) {
		items.remove(new LineItem(item, 0));
		return this;
	}

	public Inventory delete(int id) {
		delete(new Item(id, 0));
		return this;
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}
	
	public boolean isEmpty(Item item) {
		return items.containsKey(item);
	}

	public int getQuantity(Item item) {
		if (items.containsKey(item)) {
			return items.get(item);
		} else {
			return -1;
		}
	}

	public boolean contains(Item item) {
		return items.containsKey(item);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
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
		if (items == null) {
			if (other.items != null) {
				return false;
			}
		} else if (!items.equals(other.items)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Item, Integer> item : items.entrySet()) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append("[item: " + item.getKey() + ", quantity: " + item.getValue() + "]");
		}
		return "[" + sb.toString() + "]";
	}
}
