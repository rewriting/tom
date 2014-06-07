package test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;
import examples.shop.Cart;
import examples.shop.InventoryException;
import examples.shop.Shop;
import examples.shop.ShopFactory;
import examples.shop.boutique.types.Inventory;
import examples.shop.boutique.types.Item;
import examples.shop.boutique.types.ShoppingCart;

@RunWith(PropCheck.class)
public class ShopTest {

	//private Shop classUnderTest;
	private ShopFactory factory;
	@Before
	public void setUp() throws Exception {
		//classUnderTest = new Shop();
		factory = ShopFactory.getInstance();
	}

	/**
	 * <p>
	 * Total quantity of the added items in the inventory 
	 * should be equal to the new item's quantity plus the original quantity.
	 * </p>
	 * has(inventory, item) && (quantity >= 0) => 
	 * getItemQuantity(add(inventory, item, quantity), item) = getItemQuantity(inventory, item) + quantity
	 * 
	 * @param inventory
	 * @throws InventoryException 
	 */
	@Theory
	public void testInventoryAddItemQuantity(
			@ForSome(minSampleSize = 0, maxSampleSize = 60) Inventory inventory,
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Item item,
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Integer quantity) throws InventoryException {
		
		examples.shop.Inventory inv = factory.makeInventory(inventory);
		examples.shop.Item it = factory.makeItem(item);
		examples.shop.Inventory invClone = factory.makeInventory(inventory);
		
		assumeThat(inv.has(it), is(true));
		assumeThat(quantity, greaterThan(0));
		
		assertThat(inv.add(it, quantity).getItemQuantity(it), is(invClone.getItemQuantity(it) + quantity));
	}
	
	/**
	 * <p>
	 * The the quantity of a new item should be equal to the quantity
	 * </p>
	 * !has(inventory, item) && (quantity >= 0) => 
	 * getItemQuantity(add(inventory, item, quantity), item) = quantity
	 * 
	 * @param inventory
	 * @throws InventoryException 
	 */
	@Theory
	public void testInventoryAddNewItem(
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Inventory inventory,
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Item item,
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Integer quantity) throws InventoryException {
		
		examples.shop.Inventory inv = factory.makeInventory(inventory);
		examples.shop.Item it = factory.makeItem(item);
		
		assumeThat(inv.has(it), is(false));
		assumeThat(quantity, greaterThanOrEqualTo(0));
		
		assertThat(inv.add(it, quantity).getItemQuantity(it), is(quantity));
	}
	
	/**
	 * <p>
	 * Quantity of an item after being taken form an inventory 
	 * should be equal to the initial item quantity in inventory 
	 * minus the taken quantity 
	 * 
	 * </p>
	 * has(inventory, item) && (getItemQuantity(inventory, item) >= quantity) => 
	 * getItemQuantity(get(inventory, item, quantity), item) =  getItemQuantity(inventory, item) - quantity 
	 * 
	 * @param inventory
	 * @throws InventoryException 
	 */
	@Theory
	public void testInventoryGetItemFromInventoryWithQuantity(
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Inventory inventory,
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Item item,
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Integer quantity) throws InventoryException {
		
		examples.shop.Inventory inv = factory.makeInventory(inventory);
		examples.shop.Item it = factory.makeItem(item);
		examples.shop.Inventory invClone = factory.makeInventory(inventory);
		
		assumeThat(inv.has(it), is(true));
		assumeThat(inv.getItemQuantity(it), greaterThanOrEqualTo(quantity));
		
		inv.get(it, quantity);
		
		assertThat(inv.getItemQuantity(it), is(invClone.getItemQuantity(it) - quantity));
	}
	
	/**
	 * 
	 * ForAll item in cart, 
	 * has(inventory, item) => 
	 * getItemQuantity(getInventory(buy(inventory, cart)), item) = getItemQuantity(inventory, item) - getQuantity(cart', item)
	 * where cart' = getLatestSell(buy(inventory, cart))
	 * 
	 * @param inventory
	 * @param cart
	 * @throws InventoryException
	 */
	@Theory
	public void testBuy(
			@ForSome(minSampleSize = 0, maxSampleSize = 30) Inventory inventory,
			@ForSome(minSampleSize = 0, maxSampleSize = 20) ShoppingCart cart) throws InventoryException {
		
		//examples.shop.Inventory inv = factory.makeInventory(inventory);
		Shop shop = factory.makeShop(inventory);
		examples.shop.Cart crt = factory.makeCart(cart);
		examples.shop.Inventory invClone = factory.makeInventory(inventory);
		
		for (examples.shop.LineItem li : crt.getLineItems()) {
			examples.shop.Item item = li.getItem();
			assumeThat(shop.has(item), is(true));
		}
		
		/*
		 * 
		classUnderTest.setInventory(inv).buy(crt);
		for (examples.shoppingcart.LineItem li : crt.getLineItems()) {
			examples.shoppingcart.Item item = li.getItem();
			int cartItemQuantity = li.getQuantity();
			assertThat(classUnderTest.getInventoryQuantity(item), is(invClone.getItemQuantity(item) - cartItemQuantity));
		}
		 */
		//classUnderTest.setInventory(inv).buy(crt);
		shop.buy(crt);
		Cart boughtItems = shop.getLatestSell();
		for (examples.shop.LineItem li : boughtItems.getLineItems()) {
			examples.shop.Item item = li.getItem();
			int cartItemQuantity = li.getQuantity();
			assertThat(shop.getInventoryQuantity(item), is(invClone.getItemQuantity(item) - cartItemQuantity));
		}
	}
	
	/**
	 * 
	 * 
	 * @param inventory
	 * @param item
	 * @param quantity
	 */
	@Theory
	public void testInventoryNeverNegative(
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Inventory inventory,
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Item item,
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Integer quantity) {
		examples.shop.Item it = factory.makeItem(item);
		Cart cart = new Cart();
		cart.addToCart(it, quantity);
		Shop shop = factory.makeShop(inventory);
		//classUnderTest.setInventory(inventory.translateInventory());
		
		shop.buy(cart);
		
		assertThat(shop.getInventoryQuantity(it), greaterThanOrEqualTo(0));
	}
	
	
	@Ignore
	@Theory
	public void testInventory(@ForSome(minSampleSize = 30, maxSampleSize = 102) Inventory inventory) {
		System.out.println(inventory);
		examples.shop.Inventory inv = factory.makeInventory(inventory);
		System.out.println(inv);
		fail("Not yet implemented");
	}

	@Ignore
	@Theory
	public void testCart(@ForSome(minSampleSize = 30, maxSampleSize = 102) Inventory inventory) {
		System.out.println(inventory);
		examples.shop.Inventory inv = factory.makeInventory(inventory);
		System.out.println(inv);
		fail("Not yet implemented");
	}
	

}
