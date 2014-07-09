package test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;
import examples.shop.Cart;
import examples.shop.ShopFactory;
import examples.shop.boutique.types.Carts;
import examples.shop.boutique.types.Inventory;
import examples.shop.boutique.types.Item;
import examples.shop.boutique.types.LineItem;

@RunWith(PropCheck.class)
public class ShopCartTest {
	private ShopFactory factory;
	private Cart init;
	private examples.shop.Item dummyItem;
	private examples.shop.LineItem dummyLineItem;
	
	@Before
	public void setUp() throws Exception {
		factory = ShopFactory.getInstance();
		init = factory.makeCart();
		dummyItem = new examples.shop.Item(0, 10);
		dummyLineItem = new examples.shop.LineItem(dummyItem, 2);
	}
	/**
	 * contains(empty, item) = false
	 */
	@Test
	public void testContainsEmpty() {
		Cart shoppingCart = init.empty();
		assertThat(shoppingCart.contains(dummyItem), is(false));
	}
	
	/**
	 * getQuantity(empty, item) = ���1
	 */
	@Test
	public void testGetQuantityEmpty() {
		Cart shoppingCart = init.empty();
		assertThat(shoppingCart.getQuantity(dummyItem), is(-1));
	}
	
	/**
	 * isEmpty(empty) = true
	 */
	@Test
	public void testIsEmptyEmpty() {
		Cart shoppingCart = init.empty();
		assertThat(shoppingCart.isEmpty(), is(true));
	}
	
	/**
	 * isEmpty(add(cart, lineItem)) = false
	 */
	@Test
	public void testIsEmptyAdd() {
		Cart shoppingCart = init.empty();
		assertThat(shoppingCart.add(dummyLineItem).isEmpty(), is(false));
	}
	
	/**
	 * getItem(lineItem) = item ��� contains(add(cart, lineItem), item) = true
	 */
	@Theory
	public void testContainsAdd(
			@ForSome(minSampleSize = 0, maxSampleSize = 20) Carts carts,
			@ForSome(minSampleSize = 0, maxSampleSize = 20) LineItem lineItem,
			@ForSome(minSampleSize = 0, maxSampleSize = 20) Item item
			) {
		Cart shopCart = factory.makeCart(carts);
		examples.shop.LineItem shopLineItem = factory.makeLineItem(lineItem);
		examples.shop.Item shopItem = factory.makeItem(item);
		
		assumeThat(shopLineItem.getItem(), is(shopItem));
		
		assertThat(shopCart.add(shopLineItem).isEmpty(), is(false));
	}
	
	/**
	 * getItem(lineItem) = item ��� getQuantity(add(empty, lineItem), item) = getQuantity(lineItem)
	 */
	@Theory
	public void testGetQuantityAdd(
			@ForSome(minSampleSize = 0, maxSampleSize = 20) LineItem lineItem,
			@ForSome(minSampleSize = 0, maxSampleSize = 20) Item item) {
		
		Cart empty = init.empty();
		examples.shop.LineItem shopLineItem = factory.makeLineItem(lineItem);
		examples.shop.Item shopItem = factory.makeItem(item);
		
		assumeThat(shopLineItem.getItem(), is(shopItem));
		
		assertThat(empty.add(shopLineItem).getQuantity(shopItem), is(shopLineItem.getQuantity()));
	}
	
	/**
	 * getItem(lineItem) = item ��� isEmpty(cart) = false (add) contains(cart, item) = true ���
	 * getQuantity(add(cart, lineItem), item) = getQuantity(cart, item) + getQuantity(lineItem)
	 */
	@Theory
	public void testGetQuantityAddCart(
			@ForSome(minSampleSize = 0, maxSampleSize = 20) Carts carts,
			@ForSome(minSampleSize = 0, maxSampleSize = 20) LineItem lineItem,
			@ForSome(minSampleSize = 0, maxSampleSize = 20) Item item
			) {
		Cart shopCart = factory.makeCart(carts);
		Cart shopCartInitial = factory.makeCart(carts);
		examples.shop.LineItem shopLineItem = factory.makeLineItem(lineItem);
		examples.shop.Item shopItem = factory.makeItem(item);
		
		assumeThat(shopLineItem.getItem(), is(shopItem));
		assumeThat(shopCart.isEmpty(), is(false));
		assumeThat(shopCart.contains(shopItem), is(true));
		
		assertThat(shopCart.add(shopLineItem).getQuantity(shopItem), 
				is(shopCartInitial.getQuantity(shopItem) + shopLineItem.getQuantity()));
	}
}
