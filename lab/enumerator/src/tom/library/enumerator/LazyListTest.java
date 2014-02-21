package tom.library.enumerator;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class LazyListTest {

	private static LazyList<Integer> consint(Integer x, LazyList<Integer> l) {
		return LazyList.cons(x,l);
	}
	
	@Test
	public void testNil() {
		LazyList<Integer> nil = LazyList.nil();
		assertEquals(nil.isEmpty(), true);
	}
	
	@Test
	public void testSingleton() {
		LazyList<Integer> l1 = LazyList.singleton(1);
		assertEquals(l1.isEmpty(), false);
		assertEquals(l1.head(), new Integer(1));
		assertEquals(l1.tail().isEmpty(), true);
	}
	
	@Test
	public void testCons() {
		assertEquals(LazyList.cons("a",LazyList.nil()).head(), "a");
		assertEquals(LazyList.cons('a',LazyList.nil()).head(), 'a');
		assertEquals(LazyList.cons(1,LazyList.nil()).head(), 1);
		
		List<Integer> l = Arrays.asList(1,2,3,4,5);
		LazyList<Integer> ll = consint(1,consint(2,consint(3,consint(4,consint(5,LazyList.<Integer> nil())))));
		//LazyList<Integer> ll2 = LazyList.cons(1,LazyList.cons(2,LazyList.cons(3,LazyList.cons(4,LazyList.cons(5,LazyList.<Integer> nil())))));
		
		for(int i=0 ; i<5 ; i++) {
			assertEquals(l.get(i), ll.head());
			ll = ll.tail();
		}		
	}
	
	@Test
	public void testAppend() {
		assertEquals(LazyList.<String> nil().append(LazyList.singleton("a")).head(), "a");
		assertEquals(LazyList.<Character> nil().append(LazyList.singleton('a')).head(), new Character('a'));
		assertEquals(LazyList.<Integer> nil().append(LazyList.singleton(1)).head(), new Integer(1));

		List<Integer> l = Arrays.asList(1,2,3,4,5);
		LazyList<Integer> ll = LazyList.<Integer> nil();
		for(int i=0 ; i<5 ; i++) {
			ll = ll.append(LazyList.singleton(l.get(i)));
		}
		
		for(int i=0 ; i<5 ; i++) {
			assertEquals(l.get(i), ll.head());
			ll = ll.tail();
		}		
	}
	
	
	
	@Test
	public void testToList() {
		List<Integer> l = Arrays.asList(1,2,3);
		LazyList<Integer> ll = LazyList.fromList(l);
		assertEquals(ll.toList(), l);
		
	}
	
	@Test
	public void testFromList() {
		List<Integer> l = Arrays.asList(1,2,3,4,5);
		LazyList<Integer> ll = LazyList.fromList(l);
		for(int i=0 ; i<5 ; i++) {
			assertEquals(l.get(i), ll.head());
			ll = ll.tail();
		}	
	}
	
	private static LazyList<Integer> naturals(final int n) {
		return LazyList.<Integer>cons(n, new P1<LazyList<Integer>>() {
			public LazyList<Integer> _1() { return naturals(n+1); }
		});
	}
	
	@Test
	public void testInfiniteList() {
		LazyList<Integer> nat = naturals(7);
		
		for(int i = 7 ; i<100 ; i++) {
			assertEquals(new Integer(i), nat.head());
			nat = nat.tail();
		}
	}

}
