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
			char[] cs = "abcdefghijklmnopqrstuvwxyz".toCharArray();
			Enumeration<String> res = Enumeration.singleton("");
			for(char x:cs) {
				res = res.plus(Enumeration.singleton(String.valueOf(x)));
			}
			
			final F2<String,String,String> cons = new F2<String,String,String>() { 
				public String apply(String head,String tail) { return head+tail; } 
			};
			F<Enumeration<String>,Enumeration<String>> f = new F<Enumeration<String>,Enumeration<String>>() {
				public Enumeration<String> apply(final Enumeration<String> e) {
					return Enumeration.apply(Enumeration.apply(Enumeration.singleton(cons.curry()),e),e).pay();
				}
			};
			enumstring = Enumeration.fix(f);
			/*
			 final cs = "abcdefghijklmnopqrstuvwxyz".splitChars();
   final chars = _foldLeft(cs.map(singleton), empty(), (e1, e2) => e1 + e2);
   final charsLists = listsOf(chars);
   return charsLists.map(Strings.concatAll);
			 */
		}
		return enumstring;
	}
	
}
