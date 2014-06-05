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
	
	public Shop makeShop(examples.shop.shop.types.Inventory inventoryGom) {
		Inventory inventory = new Inventory();
		//buildInventory(inventoryGom, inventory);
		while (!inventoryGom.isEmptyInv()) {
			inventory.add(makeLineItem(inventoryGom.getLineItem()));
			inventoryGom = inventoryGom.getInventory();
		}
		Shop shop = new Shop();
		shop.setInventory(inventory);
		return shop;
	}
	
	public Inventory makeInventory(examples.shop.shop.types.Inventory inventoryGom) {
		Inventory inventory = new Inventory();
		//buildInventory(inventoryGom, inventory);
//		System.out.println(inventoryGom);
		while (!inventoryGom.isEmptyInv()) {
			inventory.add(makeLineItem(inventoryGom.getLineItem()));
			inventoryGom = inventoryGom.getInventory();
		}
		return inventory;
	}
	
//	private void buildInventory(examples.shop.shop.types.Inventory inventoryGom, Inventory inventory) {
//		if (inventoryGom.isInv()) {
//			inventory.add(makeLineItem(inventoryGom.getLineItem()));
//			examples.shop.shop.types.Inventory tail = inventoryGom.getInventory();
//			buildInventory(tail, inventory);
//		}
//	}
	
	public Cart makeCart(examples.shop.shop.types.ShoppingCart cartGom) {
		Cart cart = new Cart();
		buildCart(cartGom, cart);
		return cart;
	}
	
	private void buildCart(examples.shop.shop.types.ShoppingCart cartGom, Cart cart) {
		if (cartGom.isCart()) {
			cart.addToCart(makeLineItem(cartGom.getLineItem()));
			examples.shop.shop.types.ShoppingCart tail = cartGom.getShoppingCart();
			buildCart(tail, cart);
		}
	}
	
	public LineItem makeLineItem(examples.shop.shop.types.LineItem lItemGom) {
		int quantity = Math.abs(lItemGom.getQuantity());
		return new LineItem(makeItem(lItemGom.getItem()), quantity);
	}
	
	public Item makeItem(examples.shop.shop.types.Item itemGom) {
		int id = Math.abs(itemGom.getId());
		int price = Math.abs(itemGom.getPrice());
		return new Item(id, price);
	}
}
