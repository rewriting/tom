package tom.library.enumerator;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import aterm.ATerm;

public class Combinators {
	private static Enumeration<Integer> enumint = null;
	private static Enumeration<Integer> enumpint = null;

	private static Enumeration<Boolean> enumboolean = null;
	
	private static Enumeration<Character> enumcharacter = null;
	
	private static Enumeration<String> enumstring = null;

	private static Enumeration<Long> enumlong = null;
	private static Enumeration<Long> enumplong = null;

	private static Enumeration<Float> enumfloat = null;
	private static Enumeration<Float> enumpfloat = null;
	
	private static F<Integer,Integer> plusOneInteger = new F<Integer,Integer>() { public Integer apply(Integer x) { return x+1; } };
	private static F<Integer,Integer> oppositeInteger = new F<Integer,Integer>() { public Integer apply(Integer x) { return -x; } };
	private static F<Long,Long> plusOneLong = new F<Long,Long>() { public Long apply(Long x) { return x+1; } };
	private static F<Long,Long> oppositeLong = new F<Long,Long>() { public Long apply(Long x) { return -x; } };
	
	public static Enumeration<Integer> makenat() { return makePositiveInteger(); }
	public static Enumeration<Integer> makePositiveInteger() {
		if(enumpint==null) {
			final Enumeration<Integer> zeroEnum = Enumeration.singleton(0);
			F<Enumeration<Integer>,Enumeration<Integer>> sucEnum = new F<Enumeration<Integer>,Enumeration<Integer>>() {
				public Enumeration<Integer> apply(final Enumeration<Integer> e) {
					return zeroEnum.plus(Enumeration.apply(Enumeration.singleton(plusOneInteger),e)).pay();
				}
			};
			enumpint = Enumeration.fix(sucEnum);
		}
		return enumpint;
	}

	public static Enumeration<Long> makelong() { return makeLong(); }
	public static Enumeration<Long> makeLong() {
		if(enumlong==null) {
			Enumeration<Long> natsPlusOne = makePositiveLong().map(plusOneLong);
			enumlong = Enumeration.singleton(0L).plus(natsPlusOne.plus(natsPlusOne.map(oppositeLong))).pay();
		}
		return enumlong;
	}

	public static Enumeration<Long> makePositiveLong() {
		if(enumplong==null) {
			final Enumeration<Long> zeroEnum = Enumeration.singleton(0L);
			F<Enumeration<Long>,Enumeration<Long>> sucEnum = new F<Enumeration<Long>,Enumeration<Long>>() {
				public Enumeration<Long> apply(final Enumeration<Long> e) {
					return zeroEnum.plus(Enumeration.apply(Enumeration.singleton(plusOneLong),e)).pay();
				}
			};
			enumplong = Enumeration.fix(sucEnum);
		}
		return enumplong;
	}

	public static Enumeration<Integer> makeint() { return makeInteger(); }
	public static Enumeration<Integer> makeLinearInt() { return makePositiveInteger(); }
	public static Enumeration<Integer> makeInteger() {
		if(enumint==null) {
			//enumexpint = Enumeration.singleton(0).plus(new Enumeration<Integer>(naturals(0)));
			Enumeration<Integer> natsPlusOne = makePositiveInteger().map(plusOneInteger);
			enumint = Enumeration.singleton(0).plus(natsPlusOne.plus(natsPlusOne.map(oppositeInteger))).pay();
		}
		return enumint;
	}
	
	
	/*
	private static LazyList<Finite<Integer>> naturals(final int p) {
		// build Finite from 2^p to 2^(p+1)-1
		//p=3 => 8, 9, 10, 11, 12, 14, 14, 15
		final BigInteger two = BigInteger.valueOf(2);
		final BigInteger n = two.pow(p);
		Finite<Integer> head = new Finite<Integer>(n.multiply(two),new F<BigInteger,Integer>() {
			public Integer apply(BigInteger index) {
				BigInteger qr[] = index.divideAndRemainder(two);
				if(qr[1].equals(BigInteger.ZERO)) {
					return (int) qr[0].add(n).longValue();
				} else {
					return -(int) qr[0].add(n).longValue();

				}
			}
		});	
		LazyList<Finite<Integer>> tail = naturals(p+1);
		return LazyList.cons(head, tail);			

	}
*/
	
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
			int price = 0;
			for(char x:cs) {
				Enumeration e = Enumeration.singleton(x);
				//for(int i=0 ; i<price/2 ; i++) {
				//	e = e.pay();
				//}
				//price++;
				charEnum = charEnum.plus(e);
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
					Enumeration<String> res = Enumeration.apply(Enumeration.apply(Enumeration.singleton(
							new F2<Character,String,String>() {
								public String apply(Character t1, final String t2) {
									return t1+t2;
								}
							}.curry()),e1),e2);
					/*
					int price = 2;
					for(int i=0 ; i<price ; i++) {
						res = res.pay();
					}
					*/
					res = res.pay();
					return res;
				}
			};

			Enumeration<String> tmpenum = new Enumeration<String>((LazyList<Finite<String>>)null);
			enumstring = Enumeration.singleton("").plus(funMake.apply(makeCharacter(),tmpenum));

			tmpenum.p1 = new P1<LazyList<Finite<String>>>() {
				public LazyList<Finite<String>> _1() { return enumstring.parts().take(BigInteger.valueOf(100)); }
			};

		}
		return enumstring;
	}

	/*
	 * stubs for other builtins
	 */
	
	public static Enumeration<Float> makefloat() { return makeFloat(); }
	public static Enumeration<Float> makeFloat() {
		if(enumfloat==null) {
			enumfloat = null;
		}
		//return enumfloat;
		throw new RuntimeException("not yet implemented");

	}

	public static Enumeration<Float> makePositiveFloat() {
		if(enumpfloat==null) {
			enumpfloat = null;
		}
		//return enumpfloat;	
		throw new RuntimeException("not yet implemented");
	}
	
	public static Enumeration<Double> makedouble() { return makeDouble(); }
	public static Enumeration<Double> makeDouble() {
		throw new RuntimeException("not yet implemented");
	}

	public static Enumeration<ATerm> makeATerm() {
		throw new RuntimeException("not yet implemented");
	}
	
	/*
	 * lists
	 */
	public static <A> Enumeration<List<A>>  listOf(final Enumeration<A> e) {
		final F2<A,LList<A>,LList<A>> cons = 
				new F2<A,LList<A>,LList<A>>() { public LList<A> apply(A head,LList<A> tail) { return new Cons<A>(head,tail); } };
				
		final Enumeration<LList<A>> nilEnum = Enumeration.singleton((LList<A>)new Nil<A>());
		
		F<Enumeration<LList<A>>,Enumeration<LList<A>>> f = new F<Enumeration<LList<A>>,Enumeration<LList<A>>>() {
			public Enumeration<LList<A>> apply(final Enumeration<LList<A>> arg2) {
				return nilEnum.plus(consEnum(cons.curry(),e,arg2)).pay();
			}
		};
		
		final Enumeration<LList<A>> listEnum = Enumeration.fix(f);
		final F<LList<A>,List<A>> toList = new F<LList<A>,List<A>>() { public List<A> apply(LList<A> x) { return x.toList(); } };
		
		return listEnum.map(toList);
	}
	
	public static <A> Enumeration<Set<A>> setOf(final Enumeration<A> e) {
		final F<List<A>,Set<A>> toSet = new F<List<A>,Set<A>>() { public Set<A> apply(List<A> x) { 
			Set<A> res = new HashSet<A>();
			for(A a:x) { res.add(a); }
			return res;
		} };
		return listOf(e).map(toSet);
	}

	private static <A> Enumeration<LList<A>> consEnum(F<A,F<LList<A>,LList<A>>> cons, Enumeration<A> aEnum, Enumeration<LList<A>> e) {
		Enumeration<F<A,F<LList<A>,LList<A>>>> singletonCons = Enumeration.singleton(cons);
		Enumeration<F<LList<A>,LList<A>>> singletonConsBoolEnum = Enumeration.apply(singletonCons,aEnum);
		Enumeration<LList<A>> res = Enumeration.apply(singletonConsBoolEnum,e);
		return res;
	}
	
	private static abstract class LList<A> {
		public abstract int size();
		public abstract boolean isEmpty();
		public List<A> toList() {
			LList<A> tmp = this;
			List<A> res = new LinkedList<A>();
			while(!tmp.isEmpty()) {
				res.add(0,((Cons<A>)tmp).head);
				tmp = ((Cons<A>)tmp).tail;
			}
			return res;
		}
		
	}

	private static class Nil<A> extends LList<A> {
		public String toString() {
			return "nil";
		}
		public boolean isEmpty() {
			return true;
		}
		public int size() {
			return 0;
		}
	}

	private static class Cons<A> extends LList<A> {
		private A head;
		private LList<A> tail;

		public Cons(A h, LList<A> t) {
			this.head = h;
			this.tail = t;
		}

		public String toString() {
			return head + ":" + tail;
		}
		
		public boolean isEmpty() {
			return false;
		}
		public int size() {
			return 1 + tail.size();
		}
	}
	
}
