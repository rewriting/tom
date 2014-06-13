package examples.shop;

public class Item {
	private int id;
	private int price;
	
	public Item(int id, int price) {
		if (price < 0) {
			this.price = 0;
			this.id = -1;
		} else {
			this.price = price;
			this.id = id;
		}
	}
	
	public int getId() {
		return id;
	}
	public Item setId(int id) {
		this.id = id;
		return this;
	}
	public int getPrice() {
		return price;
	}
	public Item setPrice(int price) {
		this.price = price;
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		if (!(obj instanceof Item)) {
			return false;
		}
		Item other = (Item) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "Item: " + id;
	}
}
