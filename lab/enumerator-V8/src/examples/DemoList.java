package examples;

import tom.library.enumerator.*;

import java.math.BigInteger;

public class DemoList {

	public static void main(String args[]) {
		// examples for booleans
		Enumeration<Boolean> trueEnum = Enumeration.singleton(true);
		Enumeration<Boolean> falseEnum = Enumeration.singleton(false);
		final Enumeration<Boolean> boolEnum = trueEnum.plus(falseEnum).pay();
// 		final Enumeration<Boolean> boolEnum = trueEnum.plus(falseEnum);

		System.out.println("Enumerator for bool");
		for (int i = 0; i < 2; i++) {
			System.out.println("Get " + i + "th term: " + boolEnum.get(BigInteger.valueOf(i)));
		}

		// examples for list of booleans
		final F2<Boolean,LList,LList> cons = new F2<Boolean,LList,LList>() {
			public LList apply(Boolean head,LList tail) { return new Cons(head,tail); } 
		};

		final Enumeration<LList> nilEnum = Enumeration.singleton((LList)new Nil());

		F<Enumeration<LList>,Enumeration<LList>> f = new F<Enumeration<LList>,Enumeration<LList>>() {
			public Enumeration<LList> apply(final Enumeration<LList> e) {
        // 				return nilEnum.plus(consEnum(cons.curry(),boolEnum,e)).pay();
				return consEnum(cons.curry(),boolEnum,e).plus(nilEnum).pay();
			}
		};

		Enumeration<LList> listEnum = Enumeration.fix(f);

		LazyList<Finite<LList>> parts = listEnum.parts();
		for(int i=0 ; i<8 && !parts.isEmpty() ; i++) {
			System.out.println(i + " --> " + parts.head());
			parts=parts.tail();
		}

		for (int p = 0; p < 500; p = p + 100) {
			BigInteger i = java.math.BigInteger.TEN.pow(p);
			System.out.println("10^" + p + " --> " + listEnum.get(i).size());
		}
	}

	private static Enumeration<LList> consEnum(F<Boolean,F<LList,LList>> cons, Enumeration<Boolean> boolEnum, Enumeration<LList> e) {
		return Enumeration.apply(Enumeration.apply(Enumeration.singleton(cons),boolEnum),e);
	}


	private static abstract class LList {
		public abstract int size();
	}

	private static class Nil extends LList {
		public String toString() {
			return "nil";
		}
		public int size() {
			return 0;
		}
	}

	private static class Cons extends LList {
		private Boolean head;
		private LList tail;

		public Cons(Boolean h, LList t) {
			this.head = h;
			this.tail = t;
		}

		public String toString() {
			return head + ":" + tail;
		}

		public int size() {
			return 1 + tail.size();
		}
	}


}
