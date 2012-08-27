package enumerator;

import java.math.BigInteger;
import tom.library.enumerator.*;

public class Bug {

	public static void main(String args[]) {
		final Enumeration<T> aEnum = Enumeration.singleton((T)new A());
		
		F<Enumeration<T>,Enumeration<T>> foo = new F<Enumeration<T>,Enumeration<T>>() {
			public Enumeration<T> apply(final Enumeration<T> e) {
				final F<T,T> fooFunction = new F<T,T>() { public T apply(T arg) { return new Foo(arg); } };
				return aEnum.plus(Enumeration.<T,T>apply(Enumeration.singleton(fooFunction),e).pay());
				//return aEnum.plus(e.map(fooFunction).pay());
			}
		};
		
		Enumeration<T> res = Enumeration.fix(foo);
		//final F<T,T> fooFunction = new F<T,T>() { public T f(T arg) { return new Foo(arg); } };
		//Enumeration<T> res = aEnum.plus(Enumeration.<T,T>apply(Enumeration.singleton(fooFunction),aEnum.pay()));
		
		//Enumeration<P2<T,T>> TEnum = aEnum.times(aEnum);
		//Enumeration<P2<T,T>> res  = TEnum.times(aEnum);
		//Enumeration<P2<T,T>> res  = aEnum.times(TEnum);
		
		//TEnum.pay().get(0);

		LazyList<Finite<T>> parts = res.parts();
		//LazyList<LazyList<Finite<T>>> parts = TEnum.parts().reversals();

		for(int i=0;i<10 && !parts.isEmpty();i++) {
			System.out.println(i + " --> " + parts.head());
			parts=parts.tail();
		}
		//System.out.println(TEnum.pay().get(0));
		//System.out.println(TEnum.pay().get(1));

		System.out.println("42 --> " + res.get(BigInteger.valueOf(42)));
	}

	private static class T {
	}

	private static class A extends T {
		public String toString() {
			return "a";
		}
	}

	private static class Foo extends T {
		private T arg;

		public Foo(T t) {
			this.arg = t;
		}

		public String toString() {
			return "foo(" + arg + ")";
		}
	}

}
