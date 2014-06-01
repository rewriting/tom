package examples.shoppingcart;

import java.util.Collection;
import java.util.HashSet;

import examples.shoppingcart.shop.types.Inventory;
import examples.shoppingcart.shop.types.ShoppingCart;


public class ShopGomTranslator {
	private Collection<LineItem> lineItems;
	private Cart shoppingCart;
	private Item shopItem;
	
	public Collection<LineItem> translateInventory(Inventory inventory) {
		lineItems  = new HashSet<LineItem>();
		buildInventory(inventory);
		return lineItems;
	}
	
	private void buildInventory(Inventory inventory) {
		if (inventory.isInv()) {
			int itemId = inventory.getLineItem().getItem().getId();
			int itemPrice = inventory.getLineItem().getItem().getPrice();
			int itemQuantity = inventory.getLineItem().getQuantity();
			Item item = new Item(itemId, itemPrice);
			LineItem lineItem = new LineItem(item, itemQuantity);
			lineItems.add(lineItem);
			buildInventory(inventory.getInventory());
		}
	}
	
	public Cart translaCartCart(ShoppingCart cart) {
		shoppingCart = new Cart();
		buildCart(cart);
		return shoppingCart;
	}
	
	private void buildCart(ShoppingCart cart) {
		if (cart.isCart()) {
			int itemId = cart.getLineItem().getItem().getId();
			int itemPrice = cart.getLineItem().getItem().getPrice();
			int itemQuantity = cart.getLineItem().getQuantity();
			Item item = new Item(itemId, itemPrice);
			shoppingCart.addToCart(item, itemQuantity);
			buildCart(cart.getShoppingCart());
		}
	}
	
	public Item translateItem(examples.shoppingcart.shop.types.Item item) {
		shopItem = new Item(item.getId(), item.getPrice());
		return shopItem;
	}
}
