package test;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;

@RunWith(PropCheck.class)
public class ShopTestGen {

	
	@Theory
	public void testBoutique(@ForSome(minSampleSize = 1, maxSampleSize = 100) examples.shop.boutique.types.Shop inventory) {
		System.out.println(inventory);
		
	}
}
