package examples.shop;

public class BoutiqueFactory {
	private static BoutiqueFactory INSTANCE;
	
	private BoutiqueFactory() {}
	
	public static BoutiqueFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BoutiqueFactory();
		}
		return INSTANCE;
	}
	
	// take the absolute value to avoid negative ids/prices
	// this means that we suppose that all items are well formed
	// there should be some tests with items built out of possibly negative 
	// integers so we van test the behavior in these cases
	public Item makeItem(examples.shop.boutique.types.Item item) {
		return new Item(Math.abs(item.getid()), Math.abs(item.getprice()));
	}

	public Item makeItem(examples.shop.shop.types.Item item) {
		return new Item(Math.abs(item.getId()), Math.abs(item.getPrice()));
	}

	// take the absolute value to avoid quantities
	// could be generated automatically from the class LineItem
	public LineItem makeLineItem(examples.shop.boutique.types.LineItem lineItem) {
		return new LineItem(makeItem(lineItem.getitem()), Math.abs(lineItem.getquantity()));
	}

	public LineItem makeLineItem(examples.shop.shop.types.LineItem lineItem) {
		return new LineItem(makeItem(lineItem.getItem()), Math.abs(lineItem.getQuantity()));
	}
	
	
	
	public Inventory makeInventory(examples.shop.boutique.types.Inventory boutiqueInventory) {
		Inventory inventory = new Inventory();
		while (boutiqueInventory.isConsitems()) {
			inventory.add(makeLineItem(boutiqueInventory.getHeaditems()));
			boutiqueInventory = boutiqueInventory.getTailitems();
		}
		return inventory;
	}
	
	public Cart makeCart(examples.shop.boutique.types.ShoppingCart boutiqueShoppingCart) {
		Cart cart = new Cart();
		while (boutiqueShoppingCart.isConsitems()) {
			cart.addToCart(makeLineItem(boutiqueShoppingCart.getHeaditems()));
			boutiqueShoppingCart = boutiqueShoppingCart.getTailitems();
		}
		return cart;
	}
}
