package tom.library.enumerator;

import java.math.BigInteger;

import aterm.ATerm;

public class Combinators {
	private static Enumeration<Integer> enumint = null;
	private static Enumeration<Boolean> enumboolean = null;
	private static Enumeration<Character> enumcharacter = null;
	private static Enumeration<String> enumstring = null;
	
	private static Enumeration<Integer> enumexpint = null;

	public static Enumeration<Integer> makeint() { return makeInt(); }
	public static Enumeration<Integer> makeLinearInt() {
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
	
	public static Enumeration<Integer> makeInt() {
		if(enumexpint==null) {
			enumexpint = Enumeration.singleton(0).plus(new Enumeration<Integer>(naturals(0)));
		}
		return enumexpint;
	}
	
	private static LazyList<Finite<Integer>> naturals(final int p) {
		return LazyList.fromPair(new P2<Finite<Integer>,LazyList<Finite<Integer>>>() {
			public Finite<Integer> _1() {
				// build Finite from 2^p to 2^(p+1)-1
				//p=3 => 8, 9, 10, 11, 12, 14, 14, 15
				final BigInteger two = BigInteger.valueOf(2);
				final BigInteger n = two.pow(p);
				return new Finite<Integer>(n.multiply(two),new F<BigInteger,Integer>() {
					public Integer apply(BigInteger index) {
						BigInteger qr[] = index.divideAndRemainder(two);
						if(qr[1].equals(BigInteger.ZERO)) {
							return (int) qr[0].add(n).longValue();
						} else {
							return -(int) qr[0].add(n).longValue();

						}
					}
				});	
			}
			public LazyList<Finite<Integer>> _2() {
				return naturals(p+1);
			}
		});
	}
	
	public static Enumeration<Boolean> makeboolean() { return makeBoolean(); }
	public static Enumeration<Boolean> makeBoolean() {
		if(enumboolean==null) {
			enumboolean = Enumeration.singleton(true).plus(Enumeration.singleton(false)).pay();
		}
		return enumboolean;
	}
	
	public static Enumeration<Character> makechar() { return makeCharacter(); }
	public static Enumeration<Character> makeCharacter() {
		if(enumcharacter==null) {
			char[] cs = "abcdefghijklmnopqrstuvwxyz".toCharArray();
			Enumeration<Character> charEnum = Enumeration.empty();
			for(char x:cs) {
				charEnum = charEnum.plus(Enumeration.singleton(x));
			}
			enumcharacter = charEnum;
			
		}
		return enumcharacter;
	}
	
	public static Enumeration<String> makeString() {
		if(enumstring==null) {
			/*
			char[] cs = "abcdefghijklmnopqrstuvwxyz".toCharArray();
			Enumeration<Character> res = Enumeration.empty();
			for(char x:cs) {
				res = res.plus(Enumeration.singleton(x));
			}
			final Enumeration<Character> charEnum = res;
			final Enumeration<String> emptyEnum = Enumeration.empty();

			final F2<Character,String,String> cons = new F2<Character,String,String>() { 
				public String apply(Character head,String tail) { return head+tail; } 
			};
			F<Enumeration<String>,Enumeration<String>> f = new F<Enumeration<String>,Enumeration<String>>() {
				public Enumeration<String> apply(final Enumeration<String> e) {
					return emptyEnum.plus(Enumeration.apply(Enumeration.apply(Enumeration.singleton(cons.curry()),charEnum),e)).pay();
				}
			};
			enumstring = Enumeration.fix(f);
			*/

			F2<Enumeration<Character>,Enumeration<String>,Enumeration<String>> funMake =
			    new F2<Enumeration<Character>,Enumeration<String>,Enumeration<String>>() {
			      public Enumeration<String> apply(final Enumeration<Character> e1, final Enumeration<String> e2) {
			            return Enumeration.apply(Enumeration.apply(Enumeration.singleton(
			            		new F2<Character,String,String>() {
			            			public String apply(Character t1, final String t2) {
			            				return t1+t2;
			            			}
			            		}.curry()),e1),e2).pay();
			      }
			};
			
			Enumeration<String> tmpenum = new Enumeration<String>((LazyList<Finite<String>>)null);
			enumstring = Enumeration.singleton("").plus(funMake.apply(makeCharacter(),tmpenum));
			
			tmpenum.p1 = new P1<LazyList<Finite<String>>>() {
				public LazyList<Finite<String>> _1() { return enumstring.parts(); }
			};

		}
		return enumstring;
	}
	
	/*
	 * stubs for other builtins
	 */
	public static Enumeration<Long> makelong() { return makeLong(); }
	public static Enumeration<Long> makeLong() {
		throw new RuntimeException("not yet implemented");
	}
	
	public static Enumeration<Float> makefloat() { return makeFloat(); }
	public static Enumeration<Float> makeFloat() {
		throw new RuntimeException("not yet implemented");
	}
	
	public static Enumeration<Double> makedouble() { return makeDouble(); }
	public static Enumeration<Double> makeDouble() {
		throw new RuntimeException("not yet implemented");
	}
	
	public static Enumeration<ATerm> makeATerm() {
		throw new RuntimeException("not yet implemented");
	}
}
