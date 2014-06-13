package examples.shop;

public class LineItem {
	private Item item;
	private int quantity;
	
	public LineItem(Item item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}
	
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public int getQuantity() {
		return quantity;
	}
	public LineItem setQuantity(int quantity) {
		this.quantity = quantity;
		return this;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
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
		if (!(obj instanceof LineItem)) {
			return false;
		}
		LineItem other = (LineItem) obj;
		if (item == null) {
			if (other.item != null) {
				return false;
			}
		} else if (!item.equals(other.item)) {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "[" + item + ", quantity=" + quantity + "]";
	}
	
	
}
