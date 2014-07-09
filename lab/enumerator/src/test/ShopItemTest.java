package test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import examples.shop.Item;
import tom.library.theory.PropCheck;

@RunWith(PropCheck.class)
public class ShopItemTest {

	private Item item;
	
	/**
	 * price < 0 ⇒ item(id, price) = item(−1, 0)
	 */
	@Test
	public void testItemWithNegativePrice() {
		item = new Item(20, -200);
		assertThat(item.getId(), is(-1));
		assertThat(item.getPrice(), is(0));
	}

	/**
	 * price > 0 ⇒ getP rice(item(id, price)) = price
	 */
	@Test
	public void testGetPrice() {
		item = new Item(20, 200);
		assertThat(item.setPrice(100).getPrice(), is(100));
	}
}
