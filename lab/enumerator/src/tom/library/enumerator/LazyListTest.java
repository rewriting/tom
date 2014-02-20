package tom.library.enumerator;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class LazyListTest {
	
	@Test
	public void testToList() {
		List<Integer> l = Arrays.asList(1,2,3);
		LazyList<Integer> ll = LazyList.fromList(l);
		assertEquals(ll.toList(), l);
		
	}
	@Test
	public void testFromList() {
		List<Integer> l = Arrays.asList(1,2,3);
		LazyList<Integer> ll = LazyList.fromList(l);
		assertEquals(ll.toList(), l);
	}

}
