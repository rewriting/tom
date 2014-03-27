package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import tom.library.enumerator.P2;

public class EnumerationTest {

	@Test
	public void testEmpty() {
		Enumeration<Object> empty = Enumeration.empty();
		assertEquals(LazyList.nil(), empty.parts());
	}

	@Test
	public void testSingleton() {
		Enumeration<String> singleton = Enumeration.singleton("foo");
		assertEquals(Arrays.asList(Arrays.asList("foo")), singleton.toList());
	}

	@Test
	public void testPay() {
		Enumeration<Object> empty = Enumeration.empty();
		Enumeration<Object> expected = new Enumeration<Object>(LazyList.singleton(Finite.empty()));
		assertEquals(expected, empty.pay());
		Enumeration<Integer> e = Enumeration.fromList(
				Arrays.asList(Arrays.asList(1,2),new ArrayList<Integer>(),Arrays.asList(3)));
		Enumeration<Integer> expected2 = Enumeration.fromList(
				Arrays.asList(new ArrayList<Integer>(),Arrays.asList(1,2),new ArrayList<Integer>(),Arrays.asList(3)));
		assertEquals(expected2, e.pay());
	}

	@Test
	public void testPlus() {
		Enumeration<Integer> e1 = Enumeration.fromList(
				Arrays.asList(Arrays.asList(1,2),Arrays.asList(3,4)));
		Enumeration<Integer> e2 = Enumeration.fromList(
				Arrays.asList(new ArrayList<Integer>(), Arrays.asList(5,6), Arrays.asList(7)));
		
		Enumeration<Integer> empty = Enumeration.empty();
		assertEquals(empty,empty.plus(empty));
		assertEquals(e1,e1.plus(empty));
		assertEquals(e1,empty.plus(e1));
		
		// [[1,2], [3,4]] + [[], [5,6], [7]] = [[1,2], [3,4,5,6], [7]]
		Enumeration<Integer> expected1 = Enumeration.fromList(
				Arrays.asList(Arrays.asList(1,2), Arrays.asList(3,4,5,6), Arrays.asList(7)));
		assertEquals(expected1,e1.plus(e2));
		
		// [[], [5,6], [7]] + [[1,2], [3,4]] = [[1,2], [5,6,3,4], [7]]
		Enumeration<Integer> expected2 = Enumeration.fromList(
				Arrays.asList(Arrays.asList(1,2), Arrays.asList(5,6,3,4), Arrays.asList(7)));
		assertEquals(expected2,e2.plus(e1));
		
	}

	private P2<Integer,Integer> p(final Integer i, final Integer j) {
		return new P2<Integer, Integer>() {
			public Integer _1() { return i; }
			public Integer _2() { return j; }
		};
	}

	@Test
	public void testTimes() {
		Enumeration<Integer> e1 = Enumeration.fromList(
				Arrays.asList(Arrays.asList(1),Arrays.asList(2,3),Arrays.asList(4)));
		Enumeration<Integer> e2 = Enumeration.fromList(
				Arrays.asList(Arrays.asList(5,6),Arrays.asList(7),Arrays.asList(8)));
		Enumeration<P2<Integer,Integer>> expected = Enumeration.fromList(
				Arrays.asList(
						Arrays.asList(p(1, 5), p(1, 6)),
						Arrays.asList(p(1, 7), p(2, 5), p(2, 6), p(3, 5), p(3, 6)),
						Arrays.asList(p(1, 8), p(2, 7), p(3, 7), p(4, 5), p(4, 6)),
						Arrays.asList(p(2, 8), p(3, 8), p(4, 7)),
						Arrays.asList(p(4, 8)),
						new ArrayList<P2<Integer,Integer>>()));
		/*
		 * [[1], [2,3], [4]] x [[5,6], [7], [8]] = [
		 *                                          [(1,5), (1,6)],
		 *                                          [(1,7), (2,5), (2,6), (3,5), (3,6)],
		 *                                          [(1,8), (2,7), (3,7), (4,5), (4,6)],
		 *                                          [(2,8), (3,8), (4,7)],
		 *                                          [(4,8)],
		 *                                          []
		 *                                         ]
		 */
		assertEquals(Enumeration.empty().times(e1), Enumeration.empty());
		assertEquals(e1.times(Enumeration.empty()), Enumeration.empty());
		assertEquals(expected, e1.times(e2));
	}

	@Test
	public void testMap() {
		Enumeration<Integer> e = Enumeration.fromList(
				Arrays.asList(
						Arrays.asList(1,2),
						Arrays.asList(3,4,5),
						new ArrayList<Integer>(),
						Arrays.asList(6,7)));
		Enumeration<Integer> expected = Enumeration.fromList(
				Arrays.asList(
						Arrays.asList(2,3),
						Arrays.asList(4,5,6),
						new ArrayList<Integer>(),
						Arrays.asList(7,8)));

		F<Integer, Integer> f = new F<Integer, Integer>() {

			@Override
			public Integer apply(Integer a) {
				return a + 1;
			}
		};
		assertEquals(expected, e.map(f));
	}

	@Test
	public void testApply() {
		F<Integer, Integer> f1 = new F<Integer, Integer>() {

			@Override
			public Integer apply(Integer n) {
				return n;
			}
		};
		F<Integer, Integer> f2 = new F<Integer, Integer>() {

			@Override
			public Integer apply(Integer n) {
				return n * 2;
			}
		};
		F<Integer, Integer> f3 = new F<Integer, Integer>() {

			@Override
			public Integer apply(Integer n) {
				return n * 3;
			}
		};
		Enumeration<F<Integer, Integer>> f = Enumeration.fromList(
				Arrays.asList(
						Arrays.asList(f1,f2),
						Arrays.asList(f3)));

		Enumeration<Integer> e = Enumeration.fromList(
				Arrays.asList(
						Arrays.asList(1,3),
						Arrays.asList(5,7)));
		Enumeration<Integer> expected = Enumeration.fromList(
				Arrays.asList(
						Arrays.asList(1, 3, 1 * 2, 3 * 2),
						Arrays.asList(5, 7, 5 * 2, 7 * 2, 1 * 3, 3 * 3),
						Arrays.asList(5 * 3, 7 * 3),
						new ArrayList<Integer>()));
		/*
		 *  [[*1,*2],[*3]].[[1,3],[5,7]] = [
		 *                                  [1*1,3*1, 1*2,3*2],
		 *                                  [5*1,7*1, 5*2,7*2, 1*3, 3*3],
		 *                                  [5*3, 7*3]
		 *                                 ]
		 */
		assertEquals(expected, Enumeration.apply(f, e));
	}

	@Test
	public void testFix() {
		F<Enumeration<String>, Enumeration<String>> f = new F<Enumeration<String>, Enumeration<String>>() {
			@Override
			public Enumeration<String> apply(Enumeration<String> e) {
				return Enumeration.singleton("foo").plus(e.pay());
			}
		};
		Enumeration<String> e = Enumeration.fix(f);
		List<Finite<String>> prefix = new ArrayList<Finite<String>>();
		for (int i = 0; i < 100; i++) {
			prefix.add(Finite.singleton("foo"));
		}
		List<Finite<String>> enumPrefix = e.parts()
				.prefix(prefix.size())
				.toList();
		assertEquals(prefix, enumPrefix);
	}

	@Test
	public void testKnot() {
		F<Enumeration<String>, Enumeration<String>> f = new F<Enumeration<String>, Enumeration<String>>() {
			@Override
			public Enumeration<String> apply(Enumeration<String> e) {
				return Enumeration.singleton("foo").plus(e.pay());
			}
		};
		Enumeration<String> e = Enumeration.fix(f);
		assertSame(e.parts(), e.parts().tail());
	}

}
