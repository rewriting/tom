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
	
	public Item makeItem(examples.shop.boutique.types.Item boutiqueItem) {
		return new Item(boutiqueItem.getid(), boutiqueItem.getprice());
	}
	
	public LineItem makeLineItem(examples.shop.boutique.types.LineItem boutiqueLineItem) {
		return new LineItem(makeItem(boutiqueLineItem.getitem()), boutiqueLineItem.getquantity());
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
