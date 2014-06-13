package test;

import static org.junit.Assert.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import org.junit.Before;
import org.junit.Test;

import examples.shop.Item;
import examples.shop.LineItem;

public class SopTestLineItem {
	private LineItem lineItem;
	
	/**
	 * getQuantity(setQuantity(lineItem, quantity)) = quantity
	 */
	@Test
	public void testGetSetQuantity() {
		int quantity = 10;
		int price = 100;
		int newQuantity = 5;
		Item item = new Item(0, price);
		lineItem = new LineItem(item, quantity); 
		
		assertThat(lineItem.setQuantity(newQuantity).getQuantity(), is(newQuantity));
	}

}
