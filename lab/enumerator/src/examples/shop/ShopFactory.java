package examples.shop;

public class ShopFactory {
	private static ShopFactory INSTANCE;
	
	private ShopFactory() {}
	
	public static ShopFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ShopFactory();
		}
		return INSTANCE;
	}
	
	// take the absolute value to avoid negative ids/prices
	// this means that we suppose that all items are well formed
	// there should be some tests with items built out of possibly negative 
	// integers so we van test the behavior in these cases
	public Item makeItem(examples.shop.boutique.types.Item item) {
		return new Item(item.getid(), item.getprice());
	}

	public Item makeItem(examples.shop.shop.types.Item item) {
		return new Item(Math.abs(item.getId()), Math.abs(item.getPrice()));
	}

	
	// take the absolute value to avoid quantities
	// could be generated automatically from the class LineItem
	public LineItem makeLineItem(examples.shop.boutique.types.LineItem lineItem) {
		return new LineItem(makeItem(lineItem.getitem()), lineItem.getquantity());
	}

	public LineItem makeLineItem(examples.shop.shop.types.LineItem lineItem) {
		return new LineItem(makeItem(lineItem.getItem()), Math.abs(lineItem.getQuantity()));
	}
	
	
	// make inventories from (Assoc) list of lineItems
	public Inventory makeInventory(examples.shop.boutique.types.Inventory boutiqueInventory) {
		Inventory inventory = new Inventory();
		while (boutiqueInventory.isConsitems()) {
			inventory.add(makeLineItem(boutiqueInventory.getHeaditems()));
			boutiqueInventory = boutiqueInventory.getTailitems();
		}
		return inventory;
	}
	
	public Inventory makeInventory(examples.shop.shop.types.Inventory inventoryGom) {
		Inventory inventory = new Inventory();
		while (!inventoryGom.isEmptyInv()) {
			inventory.add(makeLineItem(inventoryGom.getLineItem()));
			inventoryGom = inventoryGom.getInventory();
		}
		return inventory;
	}
	

	// make inventories from (Assoc) list of lineItems
	public Cart makeCart(examples.shop.boutique.types.Inventory boutiqueShoppingCart) {
		Cart cart = new Cart();
		while (boutiqueShoppingCart.isConsitems()) {
			cart.addToCart(makeLineItem(boutiqueShoppingCart.getHeaditems()));
			boutiqueShoppingCart = boutiqueShoppingCart.getTailitems();
		}
		return cart;
	}
	

	public Cart makeCart(examples.shop.shop.types.ShoppingCart boutiqueShoppingCart) {
		Cart cart = new Cart();
		while (!boutiqueShoppingCart.isEmptyCart()) {
			cart.addToCart(makeLineItem(boutiqueShoppingCart.getLineItem()));
			boutiqueShoppingCart = boutiqueShoppingCart.getShoppingCart();
		}
		return cart;
	}
	
	

	// how to generate shops with non-empty cart ?
	public Shop makeShop(examples.shop.boutique.types.Inventory inventoryGom) {
		Inventory inventory = makeInventory(inventoryGom);
		Shop shop = new Shop();
		shop.setInventory(inventory);
		return shop;
	}

	public Shop makeShop(examples.shop.shop.types.Inventory inventoryGom) {
		Inventory inventory = makeInventory(inventoryGom);
		Shop shop = new Shop();
		shop.setInventory(inventory);
		return shop;
	}
	
	
}
