package enumerator;

import java.math.BigInteger;

import fj.F;
import fj.F2;
import fj.Function;
import fj.Unit;
public class Demo {

	public static void main(String args[]) {
		// enumerator for booleans

		Enumeration<Boolean> trueEnum = Enumeration.singleton(true);
		Enumeration<Boolean> falseEnum = Enumeration.singleton(false);
		final Enumeration<Boolean> boolEnum = trueEnum.plus(falseEnum).pay();
		//System.out.println(boolEnum.get(0));

		// enumerator for list of booleans
		final F<Boolean,F<LList,LList>> cons = Function.curry(
				new F2<Boolean,LList,LList>() { public LList f(Boolean head,LList tail) { return new Cons(head,tail); } });

		final Enumeration<LList> nilEnum = Enumeration.singleton((LList)new Nil());

		F<Enumeration<LList>,Enumeration<LList>> f = new F<Enumeration<LList>,Enumeration<LList>>() {
			public Enumeration<LList> f(final Enumeration<LList> e) {
				return nilEnum.plus(consEnum(cons,boolEnum,e)).pay();
			}
		};
		
		Enumeration<LList> listEnum = Enumeration.fix(f);

		//listEnum.pay();
		//listEnum.get(0);

		LazyList<Finite<LList>> parts = listEnum.parts();
		for(int i=0;i<10 && !parts.isEmpty();i++) {
			System.out.println(i + " --> " + parts.head());
			parts=parts.tail();
		}
		
		//for(BigInteger i=Integer.MAX_VALUE-1 ; i<= Integer.MAX_VALUE ; i=i+10000) {
		//	System.out.println(i + " --> " + listEnum.get(i));
		//}
		for(int p=0 ; p<10000 ; p=p+10) {
			BigInteger i = java.math.BigInteger.TEN.pow(p);
			System.out.println("10^" + p + " --> " /*+ listEnum.get(i)*/);
			listEnum.get(i);
		}
		//System.out.println(listEnum.pay().get(0));
		//listEnum.pay().get(0);
	}

	private static Enumeration<LList> consEnum(F<Boolean,F<LList,LList>> cons, Enumeration<Boolean> boolEnum, Enumeration<LList> e) {
		Enumeration<F<Boolean,F<LList,LList>>> singletonCons = Enumeration.singleton(cons);
		Enumeration<F<LList,LList>> singletonConsBoolEnum = Enumeration.apply(singletonCons,boolEnum);
		Enumeration<LList> res = Enumeration.apply(singletonConsBoolEnum,e);
		return res;
	}
	
	private static class LList {
	}

	private static class Nil extends LList {
		public String toString() {
			return "nil";
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
	}

}
