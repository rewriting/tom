package tom.library.enumerator;

import java.math.BigInteger;

public class Combinators {
	private static Enumeration<Integer> enumint = null;
	private static Enumeration<Boolean> enumboolean = null;
	private static Enumeration<String> enumstring = null;

	public static Enumeration<Integer> makeint() { return makeInt(); }
	public static Enumeration<Integer> makeInt() {
		if(enumint==null) {
			final Enumeration<Integer> zeroEnum = Enumeration.singleton((Integer)0);
			F<Enumeration<Integer>,Enumeration<Integer>> sucEnum = new F<Enumeration<Integer>,Enumeration<Integer>>() {
				public Enumeration<Integer> apply(final Enumeration<Integer> e) {
					F<Integer,Integer> _suc = new F<Integer,Integer>() { public Integer apply(Integer x) { return x+1; } };
					return zeroEnum.plus(Enumeration.apply(Enumeration.singleton(_suc),e)).pay();
				}
			};
			enumint = Enumeration.fix(sucEnum);
		}
		return enumint;
	}
	
	public static Enumeration<Boolean> makeboolean() { return makeBoolean(); }
	public static Enumeration<Boolean> makeBoolean() {
		if(enumboolean==null) {
			enumboolean = Enumeration.singleton(true).plus(Enumeration.singleton(false)).pay();
		}
		return enumboolean;
	}
	
	public static Enumeration<String> makeString() {
		if(enumstring==null) {
			/*
			 final cs = "abcdefghijklmnopqrstuvwxyz".splitChars();
   final chars = _foldLeft(cs.map(singleton), empty(), (e1, e2) => e1 + e2);
   final charsLists = listsOf(chars);
   return charsLists.map(Strings.concatAll);
			 */
		}
		return enumstring;
	}
	
	
	/*
	P2<Finite<Integer>, LazyList<Finite<Integer>>> p2 = new P2<Finite<Integer>, LazyList<Finite<Integer>>>() {
		  public Finite<Integer> _1() { return new Finite<Integer>(
				  BigInteger.valueOf(Long.MAX_VALUE), 
				  new F<BigInteger, Integer>() {
					  public Integer apply(BigInteger i) { return Integer.valueOf(i.intValue()); }
				  });
		  }
		  public LazyList<Finite<Integer>> _2() { return LazyList.nil(); }
	  };
	  Enumeration<Integer> enumint = new Enumeration<Integer>(LazyList.fromPair(p2));
*/
}
