package examples.shoppingcart;

import java.util.ArrayList;
import java.util.List;

public class Shop {
	private Inventory inventory;
	private List<Cart> carts;
	
	public Shop() {
		inventory = new Inventory();
		carts = new ArrayList<Cart>();
	}
	
	public void buy(Cart cart) {
		// put the cart in the list
		// re-calculate the quantity
	}
	
}
