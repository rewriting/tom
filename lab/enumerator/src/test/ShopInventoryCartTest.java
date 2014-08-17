package test;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import examples.shop.Cart;
import examples.shop.ShopFactory;
import examples.shop.boutique.types.Carts;
import examples.shop.boutique.types.Inventory;
import examples.shop.boutique.types.Item;
import examples.shop.boutique.types.LineItem;
import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;

@RunWith(PropCheck.class)
public class ShopInventoryCartTest {
	private ShopFactory factory;
	
	@Before
	public void setUp() throws Exception {
		factory = ShopFactory.getInstance();
	}
	
	@Theory
	public void testGetQuantityAddInventory(
			@ForSome Inventory inventory,
			@ForSome LineItem lineItem) {
		
		examples.shop.Inventory shopInventory = factory.makeInventory(inventory);
		examples.shop.Inventory shopInventoryInitial = factory.makeInventory(inventory);
		examples.shop.LineItem shopLineItem = factory.makeLineItem(lineItem);
		examples.shop.Item shopItem = shopLineItem.getItem();
		
		assumeThat(shopInventory.isEmpty(), is(false));
		
		assertThat(
				shopInventory.add(shopLineItem).getQuantity(shopItem), 
				is(shopInventoryInitial.getQuantity(shopItem) + shopLineItem.getQuantity()));
	}
	
	@Theory
	public void testGetQuantityAddCart(
			@ForSome Carts carts,
			@ForSome LineItem lineItem
			) {
		Cart shopCart = factory.makeCart(carts);
		Cart shopCartInitial = factory.makeCart(carts);
		examples.shop.LineItem shopLineItem = factory.makeLineItem(lineItem);
		examples.shop.Item shopItem = shopLineItem.getItem();
		
		assumeThat(shopCart.isEmpty(), is(false));
		
		assertThat(shopCart.add(shopLineItem).getQuantity(shopItem), 
				is(shopCartInitial.getQuantity(shopItem) + shopLineItem.getQuantity()));
	}
}
