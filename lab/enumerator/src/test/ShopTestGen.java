package test;

import org.junit.Ignore;
import org.junit.contrib.theories.DataPoints;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;

import test.junit.quickcheck.MyGenerator;
import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;

@RunWith(PropCheck.class)
public class ShopTestGen {

	@DataPoints
	public static int[] n = { 1, 2, 3, 4 };

	@Ignore
	@Theory
	public void testBoutique(
			@ForSome(minSampleSize = 1, maxSampleSize = 100) examples.shop.boutique.types.Shop inventory) {
		System.out.println(inventory);

	}

	@Theory
	public void testMix(@ForSome(minSampleSize = 1, maxSampleSize = 10) int n,
						int m, 
						@ForAll(sampleSize = 20) @From({ MyGenerator.class }) int p) {
		System.out.println(n + " -- " + m + " -- " + p);

	}
}
