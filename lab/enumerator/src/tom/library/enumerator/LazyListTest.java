package tom.library.enumerator;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class LazyListTest {

	private static LazyList<Integer> naturals(final int n) {
		return LazyList.<Integer>fromPair(new P2<Integer,LazyList<Integer>>() {
			public Integer _1() { return n; }
			public LazyList<Integer> _2() { return naturals(n+1); }
		});
	}
	
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
