package test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import examples.shop.ShopFactory;
import examples.shop.boutique.types.Inventory;
import examples.shop.boutique.types.Item;
import examples.shop.boutique.types.LineItem;
import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;

@RunWith(PropCheck.class)
public class ShopInventoryTest {
	private ShopFactory factory;
	private examples.shop.Inventory init;
	
	@Before
	public void setUp() throws Exception {
		factory = ShopFactory.getInstance();
		init = factory.makeInventory();
	}

	/**
	 * contains(empty, item) = false
	 */
	@Test
	public void testContainsEmpty() {
		examples.shop.Inventory empty = init.empty();
		examples.shop.Item item = new examples.shop.Item(0, 10);
		
		assertThat(empty.contains(item), is(false));
	}

	/**
	 * getQuantity(empty, item) = −1
	 */
	@Test
	public void testGetQuantityEmpty() {
		examples.shop.Inventory empty = init.empty();
		examples.shop.Item item = new examples.shop.Item(0, 10);
		
		assertThat(empty.getQuantity(item), is(-1));
	}
	
	/**
	 * isEmpty(empty) = true
	 */
	@Test
	public void testIsEmptyEmpty() {
		examples.shop.Inventory empty = init.empty();
		
		assertThat(empty.isEmpty(), is(true));
	}
	
	/**
	 * isEmpty(add(inventory, lineItem)) = false
	 * @param inventory
	 * @param lineItem
	 */
	@Theory
	public void testIsEmptyAdd(
			@ForSome(minSampleSize = 0, maxSampleSize = 20) Inventory inventory,
			@ForSome(minSampleSize = 0, maxSampleSize = 20) LineItem lineItem) {
		examples.shop.Inventory shopInventory = factory.makeInventory(inventory);
		examples.shop.LineItem shopLineItem = factory.makeLineItem(lineItem);
		assertThat(shopInventory.add(shopLineItem).isEmpty(), is(false));
	}
	
	/**
	 * getItem(lineItem) = item ⇒ contains(add(inventory, lineItem), item) = true
	 * @param inventory
	 * @param lineItem
	 * @param item
	 */
	@Theory
	public void testContainsAdd(
			@ForSome(minSampleSize = 0, maxSampleSize = 20) Inventory inventory,
			@ForSome(minSampleSize = 0, maxSampleSize = 20) LineItem lineItem,
			@ForSome(minSampleSize = 0, maxSampleSize = 20) Item item) {
		
		examples.shop.Inventory shopInventory = factory.makeInventory(inventory);
		examples.shop.LineItem shopLineItem = factory.makeLineItem(lineItem);
		examples.shop.Item shopItem = factory.makeItem(item);
		
		assumeThat(shopLineItem.getItem(), is(shopItem));
		
		assertThat(shopInventory.add(shopLineItem).contains(shopItem), is(true));
	}
	
	/**
	 * getItem(lineItem) = item ⇒ getQuantity(add(empty, lineItem), item) = getQuantity(lineItem)
	 */
	@Theory
	public void testGetQuantityAddEmpty(
			@ForSome(minSampleSize = 0, maxSampleSize = 20) LineItem lineItem,
			@ForSome(minSampleSize = 0, maxSampleSize = 20) Item item) {
		
		examples.shop.Inventory empty = init.empty();
		examples.shop.LineItem shopLineItem = factory.makeLineItem(lineItem);
		examples.shop.Item shopItem = factory.makeItem(item);
		
		assumeThat(shopLineItem.getItem(), is(shopItem));
		
		assertThat(empty.add(shopLineItem).getQuantity(shopItem), is(shopLineItem.getQuantity()));
	}
	
	/**
	 * getItem(lineItem) = item ∧ inventory ̸= empty (add) and contains(inventory, item) = true ⇒
	 * getQuantity(add(inventory, lineItem), item) = getQuantity(inventory, item) + getQuantity(lineItem)
	 */
	@Theory
	public void testGetQuantityItemInInventory(
			@ForSome(minSampleSize = 0, maxSampleSize = 20) Inventory inventory,
			@ForSome(minSampleSize = 0, maxSampleSize = 20) LineItem lineItem,
			@ForSome(minSampleSize = 0, maxSampleSize = 20) Item item) {
		
		examples.shop.Inventory shopInventory = factory.makeInventory(inventory);
		examples.shop.Inventory shopInventoryInitial = factory.makeInventory(inventory);
		examples.shop.LineItem shopLineItem = factory.makeLineItem(lineItem);
		examples.shop.Item shopItem = factory.makeItem(item);
		
		assumeThat(shopLineItem.getItem(), is(shopItem));
		assumeThat(shopInventory.isEmpty(), is(false));
		assumeThat(shopInventory.contains(shopItem), is(true));
		
		assertThat(
				shopInventory.add(shopLineItem).getQuantity(shopItem), 
				is(shopInventoryInitial.getQuantity(shopItem) + shopLineItem.getQuantity()));
	}
	
	@Theory
	public void test(
			@ForSome(minSampleSize = 0, maxSampleSize = 20) Inventory inventory) {
		System.out.println(inventory);
	}
}
