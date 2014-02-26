package test;

import static org.junit.Assert.*;

import org.junit.Test;

import tom.library.enumerator.F;
import tom.library.enumerator.Finite;
import tom.library.enumerator.P2;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;

public class FiniteTest {

	@Test
	public void testCardinalOfSum() {
		Finite<String> empty = Finite.empty();
		Finite<String> a = Finite.singleton("a");
		Finite<String> b = Finite.singleton("b");
		Finite<String> c = Finite.singleton("c");
		assertEquals(empty.plus(empty).getCard(), ZERO);
		assertEquals(empty.plus(a).getCard(), ONE);
		assertEquals(a.plus(empty).getCard(), ONE);
		assertEquals(a.plus(b).getCard(), BigInteger.valueOf(2));
		assertEquals(a.plus(b).plus(c).getCard(), BigInteger.valueOf(3));
		assertEquals(a.plus(b.plus(c)).getCard(), BigInteger.valueOf(3));
	}

	@Test
	public void testCardinalOfProd() {
		Finite<String> empty = Finite.empty();
		Finite<String> a = Finite.singleton("a");
		Finite<String> b = Finite.singleton("b");
		Finite<String> ab = a.plus(b);
		assertEquals(empty.times(empty).getCard(), ZERO);
		assertEquals(empty.times(a).getCard(), ZERO);
		assertEquals(a.times(empty).getCard(), ZERO);
		assertEquals(a.times(b).getCard(), ONE);
		assertEquals(ab.times(a).getCard(), BigInteger.valueOf(2));
		assertEquals(a.times(ab).getCard(), BigInteger.valueOf(2));
		assertEquals(ab.times(ab).getCard(), BigInteger.valueOf(4));
		assertEquals(ab.times(ab).times(ab).getCard(), BigInteger.valueOf(8));
		assertEquals(ab.times(ab.times(ab)).getCard(), BigInteger.valueOf(8));
	}

	@Test
	public void testCardinalOfMap() {
		Finite<Integer> empty = Finite.empty();
		Finite<Integer> a = Finite.singleton(1);
		Finite<Integer> b = Finite.singleton(2);
		F<Integer,Integer> f = new F<Integer,Integer>() {
			public Integer apply(Integer arg) {
				return arg + 1 ;
			}
		};

		assertEquals(empty.map(f).getCard(), empty.getCard());
		assertEquals(a.map(f).getCard(), a.getCard());
		assertEquals(a.plus(b).map(f).getCard(), a.plus(b).getCard());

		F<P2<Integer,Integer>,Integer> g = new F<P2<Integer,Integer>,Integer>() {
			public Integer apply(P2<Integer,Integer> p) {
				return p._1() + p._2() ;
			}
		};
		assertEquals(a.plus(b).times(a).map(g).getCard(), a.plus(b).times(a).getCard());
	}

	@Test
	public void testCardinalOfApply() {
		F<Integer,Integer> f1 = new F<Integer,Integer>() {
			public Integer apply(Integer arg) {
				return arg + 1 ;
			}
		};
		F<Integer,Integer> f2 = new F<Integer,Integer>() {
			public Integer apply(Integer arg) {
				return arg + 2 ;
			}
		};

		Finite<F<Integer,Integer>> empty = Finite.empty();
		Finite<F<Integer,Integer>> fun1 = Finite.singleton(f1);
		Finite<F<Integer,Integer>> fun2 = Finite.singleton(f2);
		Finite<Integer> one = Finite.singleton(1);
		Finite<Integer> two = Finite.singleton(2);

		assertEquals(one.applyFiniteFunctions(empty).getCard(), ZERO);
		assertEquals(one.plus(two).applyFiniteFunctions(empty).getCard(), ZERO);
		assertEquals(one.applyFiniteFunctions(fun1).getCard(), ONE);
		assertEquals(one.plus(two).applyFiniteFunctions(fun1).getCard(), BigInteger.valueOf(2));
		assertEquals(one.applyFiniteFunctions(fun1.plus(fun2)).getCard(), BigInteger.valueOf(2));
		assertEquals(one.plus(two).applyFiniteFunctions(fun1.plus(fun2)).getCard(), BigInteger.valueOf(4));
	}

	//@Rule
	//public ExpectedException exception = ExpectedException.none();

	@Test
	public void testIndexEmpty() {
		Finite<String> empty = Finite.empty();
		for (int i = 0; i < 100; i++) {
			try {
				empty.get(BigInteger.valueOf(i));
				fail( "My method didn't throw when I expected it to" );
			} catch (RuntimeException expectedException) {}
		}
	}

	@Test
	public void testIndexSingleton() {
		Finite<String> a = Finite.singleton("a");
		for (int i = 1; i < 100; i++) {
			try {
				a.get(BigInteger.valueOf(i));
				fail( "My method didn't throw when I expected it to" );
			} catch (RuntimeException expectedException) {}
		}
		assertEquals(a.get(ZERO), "a");
	}

	@Test
	public void testIndexSum() {
		Finite<String> a = Finite.singleton("a");
		Finite<String> b = Finite.singleton("b");
		Finite<String> c = Finite.singleton("c");

		Finite<String> sum1 = a.plus(b).plus(c);
		Finite<String> sum2 = a.plus(b.plus(c));

		assertEquals(sum1.get(ZERO), "a");
		assertEquals(sum1.get(ONE), "b");
		assertEquals(sum1.get(BigInteger.valueOf(2)), "c");
		for (int i = 3; i < 100; i++) {
			try {
				sum1.get(BigInteger.valueOf(i));;
				fail( "My method didn't throw when I expected it to" );
			} catch (RuntimeException expectedException) {}
		}

		assertEquals(sum2.get(ZERO), "a");
		assertEquals(sum2.get(ONE), "b");
		assertEquals(sum2.get(BigInteger.valueOf(2)), "c");
		for (int i = 3; i < 100; i++) {
			try {
				sum2.get(BigInteger.valueOf(i));
				fail( "My method didn't throw when I expected it to" );
			} catch (RuntimeException expectedException) {}
		}

	}

	@Test
	public void testIndexProd() {
		Finite<String> a = Finite.singleton("a");
		Finite<String> b = Finite.singleton("b");
		Finite<P2<String,String>> prod = a.plus(b).times(a.plus(b));

		assertEquals(prod.get(BigInteger.valueOf(0))._1(),"a");
		assertEquals(prod.get(BigInteger.valueOf(0))._2(),"a");
		assertEquals(prod.get(BigInteger.valueOf(1))._1(),"a");
		assertEquals(prod.get(BigInteger.valueOf(1))._2(),"b");
		assertEquals(prod.get(BigInteger.valueOf(2))._1(),"b");
		assertEquals(prod.get(BigInteger.valueOf(2))._2(),"a");
		assertEquals(prod.get(BigInteger.valueOf(3))._1(),"b");
		assertEquals(prod.get(BigInteger.valueOf(3))._2(),"b");
		
		for (int i = 4; i < 5; i++) {
			try {
				prod.get(BigInteger.valueOf(i));
				fail( "My method didn't throw when I expected it to");
			} catch (RuntimeException expectedException) {}
		}
	}
	
	@Test
	public void testIndexMap() {
		 Finite<Integer> fin = Finite.singleton(1).plus(Finite.singleton(2)).plus(Finite.singleton(3));
		 F<Integer,Integer> f = new F<Integer,Integer>() {
			 public Integer apply(Integer arg) {
				 return 2*arg ;
			 }
		 };
		 Finite<Integer> finDoubled = fin.map(f);
		 
		 assertEquals(finDoubled.get(BigInteger.valueOf(0)), new Integer(2));
		 assertEquals(finDoubled.get(BigInteger.valueOf(1)), new Integer(4));
		 assertEquals(finDoubled.get(BigInteger.valueOf(2)), new Integer(6));
	}
	



	@Test
	public void testIndexApply() {
		F<Integer,Integer> f1 = new F<Integer,Integer>() {
			public Integer apply(Integer arg) {
				return arg * 2 ;
			}
		};
		F<Integer,Integer> f2 = new F<Integer,Integer>() {
			public Integer apply(Integer arg) {
				return arg * 3 ;
			}
		};
		Finite<F<Integer, Integer>> fs = Finite.singleton(f1).plus(Finite.singleton(f2));
		Finite<Integer> xs = Finite.singleton(1).plus(Finite.singleton(2));
		Finite<Integer> applied = xs.applyFiniteFunctions(fs);
		assertEquals(applied.get(BigInteger.valueOf(0)),new Integer(2));
		assertEquals(applied.get(BigInteger.valueOf(1)),new Integer(4));
		assertEquals(applied.get(BigInteger.valueOf(2)),new Integer(3));
		assertEquals(applied.get(BigInteger.valueOf(3)),new Integer(6));
		for (int i = 4; i < 100; i++) {
			try {
				applied.get(BigInteger.valueOf(i));;
				fail( "My method didn't throw when I expected it to" );
			} catch (RuntimeException expectedException) {}
		}
	}

	@Test
	public void testFromList() {
		assertEquals(Finite.fromList(Arrays.asList()).getCard(), ZERO);
		assertEquals(Finite.fromList(Arrays.asList(1)).get(ZERO), new Integer(1));

		List<Integer> l = Arrays.asList(1,2,3);
		Finite<Integer> a = Finite.fromList(l); 
		assertEquals(a.getCard(), BigInteger.valueOf(l.size()));
	}

}
