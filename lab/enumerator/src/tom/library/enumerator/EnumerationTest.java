package tom.library.enumerator;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

public class EnumerationTest {

	@Test
	public void testPay() {
		Enumeration<Integer> empty = Enumeration.empty();
		//System.out.println(empty.pay());
		
		//Enumeration<Integer> a = Enumeration.singleton(1);

		//System.out.println(a);
		//System.out.println(a.pay());

		
		//assertEquals(empty.pay(), )
		
		
		/*
		checkEquals(empty().pay(), [[]]);

		  final e        = [    [1,2], [], [3]];
		  final expected = [[], [1,2], [], [3]];
		  checkEquals(listToEnum(e).pay(), expected);
*/
	}

	@Test
	public void testPlus() {
		Enumeration<Integer> e1 = Enumeration.fromList(
				Arrays.asList(Arrays.asList(1,2),Arrays.asList(3,4)));
		
		System.out.println(e1);
		
		
		
		//assertEquals(empty.pay(), )
		
		
		/*
		checkEquals(empty().pay(), [[]]);

		  final e        = [    [1,2], [], [3]];
		  final expected = [[], [1,2], [], [3]];
		  checkEquals(listToEnum(e).pay(), expected);
*/
	}

}
