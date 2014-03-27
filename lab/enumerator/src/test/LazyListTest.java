package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import tom.library.enumerator.F;
import tom.library.enumerator.LazyList;
import tom.library.enumerator.P1;

public class LazyListTest {

	private static LazyList<Integer> consint(Integer x, final LazyList<Integer> l) {
		return LazyList.cons(x, new P1<LazyList<Integer>>() {
			public LazyList<Integer> _1() { return l; };
		});
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
		assertEquals(LazyList.singleton("a").head(), "a");
		assertEquals(LazyList.singleton(new Integer(1)).head(), new Integer(1));

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

	@Test
	public void testTails() {
		LazyList<Integer> nat = naturals(7);
		LazyList<LazyList<Integer>> tails = nat.tails();

		for(int i = 7 ; i<100 ; i++) {
			LazyList<Integer> l = tails.head();
			assertEquals(new Integer(i), l.head());
			tails = tails.tail();
		}
	}

	@Test
	public void testReversals() {
		LazyList<Integer> nat = naturals(1);
		LazyList<LazyList<Integer>> rev = nat.reversals();

		for(int i = 1 ; i<100 ; i++) {
			LazyList<Integer> l = rev.head();
			assertEquals(new Integer(i), l.head());
			rev = rev.tail();
		}
	}

	@Test
	public void testMap() {
		F<Integer, Integer> f2 = new F<Integer, Integer>() {
			public Integer apply(Integer n) {
				return n * 2;
			}
		};

		LazyList<Integer> nat = naturals(1);
		LazyList<Integer> l = nat.map(f2);
		for(int i = 1 ; i<100 ; i++) {
			assertEquals(f2.apply(i), l.head());
			l = l.tail();
		}
	}

}
