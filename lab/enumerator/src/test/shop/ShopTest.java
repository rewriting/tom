package test.shop;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;
import examples.shoppingcart.Cart;
import examples.shoppingcart.InventoryException;
import examples.shoppingcart.LineItem;
import examples.shoppingcart.Shop;
import examples.shoppingcart.shop.types.Inventory;
import examples.shoppingcart.shop.types.Item;
import examples.shoppingcart.shop.types.ShoppingCart;

@RunWith(PropCheck.class)
public class ShopTest {

	private Shop classUnderTest;
	
	@Before
	public void setUp() throws Exception {
		classUnderTest = new Shop();
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
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Inventory inventory,
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Item item,
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Integer quantity) throws InventoryException {
		
		examples.shoppingcart.Inventory inv = inventory.translateInventory();
		examples.shoppingcart.Item it = item.translateItem();
		examples.shoppingcart.Inventory invClone = inventory.translateInventory();
		
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
		
		examples.shoppingcart.Inventory inv = inventory.translateInventory();
		examples.shoppingcart.Item it = item.translateItem();
		
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
		
		examples.shoppingcart.Inventory inv = inventory.translateInventory();
		examples.shoppingcart.Item it = item.translateItem();
		examples.shoppingcart.Inventory invClone = inventory.translateInventory();
		
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
			@ForSome(minSampleSize = 30, maxSampleSize = 60) Inventory inventory,
			@ForSome(minSampleSize = 30, maxSampleSize = 60) ShoppingCart cart) throws InventoryException {
		
		examples.shoppingcart.Inventory inv = inventory.translateInventory();
		examples.shoppingcart.Cart crt = cart.translateCart();
		examples.shoppingcart.Inventory invClone = inventory.translateInventory();
		
		for (examples.shoppingcart.LineItem li : crt.getLineItems()) {
			examples.shoppingcart.Item item = li.getItem();
			assumeThat(inv.has(item), is(true));
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
		classUnderTest.setInventory(inv).buy(crt);
		for (examples.shoppingcart.LineItem li : crt.getLineItems()) {
			examples.shoppingcart.Item item = li.getItem();
			int cartItemQuantity = li.getQuantity();
			assertThat(classUnderTest.getInventoryQuantity(item), is(invClone.getItemQuantity(item) - cartItemQuantity));
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
		examples.shoppingcart.Item it = item.translateItem();
		Cart cart = new Cart();
		cart.addToCart(it, quantity);
		
		classUnderTest.setInventory(inventory.translateInventory());
		
		classUnderTest.buy(cart);
		
		assertThat(classUnderTest.getInventoryQuantity(it), greaterThanOrEqualTo(0));
	}
	
	
	@Ignore
	@Theory
	public void testInventory(@ForSome(minSampleSize = 30, maxSampleSize = 102) Inventory inventory) {
		System.out.println(inventory);
		examples.shoppingcart.Inventory inv = inventory.translateInventory();
		System.out.println(inv);
		fail("Not yet implemented");
	}

	@Ignore
	@Theory
	public void testCart(@ForSome(minSampleSize = 30, maxSampleSize = 102) Inventory inventory) {
		System.out.println(inventory);
		examples.shoppingcart.Inventory inv = inventory.translateInventory();
		System.out.println(inv);
		fail("Not yet implemented");
	}
}
