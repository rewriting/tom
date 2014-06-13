package test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
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

@RunWith(PropCheck.class)
public class ShopTest {

	private ShopFactory factory;
	private Shop init;
	private examples.shop.Item dummyItem;
	
	@Before
	public void setUp() throws Exception {
		factory = ShopFactory.getInstance();
		init = factory.makeShop();
		dummyItem = new examples.shop.Item(2, 3);
	}

	/**
	 * isEmpty(empty) = true
	 */
	@Test
	public void testIsEmpty() {
		Shop empty = init.empty();
		assertThat(empty.isEmpty(), is(true));
	}
	
	/**
	 * getInvQuantity(empty, item) = 0
	 */
	@Test
	public void testInvQuantityEmpty() {
		Shop empty = init.empty();
		assertThat(empty.getInvQuantity(dummyItem), is(0));
	}
	
	/**
	 * getCartQuantity(empty, item) = 0
	 */
	@Test
	public void testCartQuantityEmpty() {
		Shop empty = init.empty();
		assertThat(empty.getCartQuantity(dummyItem), is(0));
	}
	/**
	 * isEmpty(inventory(shop)) = isEmpty(shop)
	 */
	@Theory
	public void testShopInventoryRelation(
			@ForSome(minSampleSize=0, maxSampleSize=20) Inventory inventory) {
		Shop shop = factory.makeShop(inventory);
		
		assertThat(shop.inventory().isEmpty(), is(shop.isEmpty()));
	}
	
	/**
	 * getInvQuantity(shop, item) > quantity ∧ quantity > 0 ⇒
	 * getInvQuantity(buy(shop, id, item, quantity), item) = getInvQuantity(shop, item) − quantity ∧
	 * getCartQuantity(buy(shop, id, item, quantity), item) = getCartQuantity(shop, item) + quantity
	 */
	@Theory
	public void testBuy(
			@ForSome(minSampleSize=0, maxSampleSize=20) Inventory inventory,
			@ForSome(minSampleSize=0, maxSampleSize=20) Item item,
			@ForSome(minSampleSize=0, maxSampleSize=20) int quantity,
			@ForSome(minSampleSize=0, maxSampleSize=20) int id) {
		Shop shop = factory.makeShop(inventory);
		Shop shopInit = factory.makeShop(inventory);
		
		examples.shop.Item shopItem = factory.makeItem(item);
		
		assumeThat(shop.getInvQuantity(shopItem), greaterThan(quantity));
		assumeThat(quantity, greaterThan(0));
		
		assertThat(shop.buy(id, shopItem, quantity).getInvQuantity(shopItem), 
				is(shopInit.getInvQuantity(shopItem) - quantity));
		assertThat(shop.getCartQuantity(shopItem), is(shopInit.getCartQuantity(shopItem) - quantity));
	}
	
	/**
	 * getInvQuantity(shop, item) = 0 and quantity > 0 ⇒
	 * getCartQuantity(buy(shop, id, item, quantity), item) = getCartQuantity(shop, item)
	 * 
	 */
	@Theory
	public void testBuyWithNoQuantityInInventory(
			@ForSome(minSampleSize=0, maxSampleSize=20) Inventory inventory,
			@ForSome(minSampleSize=0, maxSampleSize=20) Item item,
			@ForSome(minSampleSize=0, maxSampleSize=20) int quantity,
			@ForSome(minSampleSize=0, maxSampleSize=20) int id) {
		Shop shop = factory.makeShop(inventory);
		Shop shopInit = factory.makeShop(inventory);
		
		examples.shop.Item shopItem = factory.makeItem(item);
		
		assumeThat(shop.getInvQuantity(shopItem), is(0));
		assumeThat(quantity, greaterThan(0));
		
		assertThat(shop.buy(id, shopItem, quantity).getCartQuantity(shopItem), 
				is(shopInit.getCartQuantity(shopItem)));
	}
	
	/**
	 * quantity > 0 and getInvQuantity(shop, item) < quantity and contains(inventory(shop), item) = true ⇒
	 * getInvQuantity(buy(shop, id, item, quantity), item) = 0∧
	 * getCartQuantity(buy(shop, id, item, quantity), item) = getCartQuantity(shop, item)+
	 * getInvQuantity(shop, item)
	 * 
	 */
	@Theory
	public void testBuyWithQuantityGreaterThanInventory(
			@ForSome(minSampleSize=0, maxSampleSize=20) Inventory inventory,
			@ForSome(minSampleSize=0, maxSampleSize=20) Item item,
			@ForSome(minSampleSize=0, maxSampleSize=20) int quantity,
			@ForSome(minSampleSize=0, maxSampleSize=20) int id) {
		Shop shop = factory.makeShop(inventory);
		Shop shopInit = factory.makeShop(inventory);
		
		examples.shop.Item shopItem = factory.makeItem(item);
		
		assumeThat(quantity, greaterThan(0));
		assumeThat(shop.getInvQuantity(shopItem), lessThan(quantity));
		assumeThat(shop.inventory().contains(shopItem), is(true));
		
		assertThat(shop.buy(id, shopItem, quantity).getInvQuantity(shopItem), is(0));
		assertThat(shop.getCartQuantity(shopItem), 
				is(shopInit.getCartQuantity(shopItem) + shopInit.getInvQuantity(shopItem)));
	}
}
