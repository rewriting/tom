package test.shop;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.theory.ForSome;
import tom.library.theory.TomCheck;
import examples.shoppingcart.LineItem;
import examples.shoppingcart.Shop;
import examples.shoppingcart.shop.types.Inventory;

@RunWith(TomCheck.class)
public class ShopTest {

	private Shop classUnderTest;
	
	@Before
	public void setUp() throws Exception {
		classUnderTest = new Shop();
	}

	
	@Theory
	public void testInventory(@ForSome(minSampleSize = 99, maxSampleSize = 102) Inventory inventory) {
		System.out.println(inventory);
		examples.shoppingcart.Inventory inv = inventory.translateInventory();
		System.out.println(inv);
		fail("Not yet implemented");
	}

	@Theory
	public void testCart(@ForSome(minSampleSize = 99, maxSampleSize = 102) Inventory inventory) {
		System.out.println(inventory);
		examples.shoppingcart.Inventory inv = inventory.translateInventory();
		System.out.println(inv);
		fail("Not yet implemented");
	}
}
