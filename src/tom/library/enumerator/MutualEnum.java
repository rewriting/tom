/*
 * Created on Aug 22, 2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package enumerator;

import enumerator.mutual.types.A;
import enumerator.mutual.types.B;
import java.util.HashMap;

/**
 * will be automatically generated
 */
public class MutualEnum {
	private HashMap<Class<?>,Enumeration<?>> map;
	
	private MutualEnum() {
		this.map = new HashMap<Class<?>,Enumeration<?>>();
	}
	private static MutualEnum instance = null;
	
	public static  MutualEnum instance() {
		if(instance==null) {
			instance = new MutualEnum();
			instance.init();
		}
		return instance;
	}
	
	public <T> Enumeration<T> get(Class<?> c) {
		return (Enumeration<T>) map.get(c);
	}
	
	private void init() {
		final F<B, A> foo =
			new F<B, A>() {
			public A apply(final B elem) {
				return enumerator.mutual.types.a.foo.make(elem);
			}
		};

		final F<A, A> hoo =
			new F<A, A>() {
			public A apply(final A elem) {
				return enumerator.mutual.types.a.hoo.make(elem);
			}
		};

		final F<A, B> grr =
			new F<A, B>() {
			public B apply(final A elem) {
				return enumerator.mutual.types.b.grr.make(elem);
			}
		};

		F<Enumeration<A>, Enumeration<A>> aFun = new F<Enumeration<A>, Enumeration<A>>() {
			public Enumeration<A> apply(final Enumeration<A> e) {
				return Enumeration.singleton((A) enumerator.mutual.types.a.a.make()); // !!!! NO pay (is a constant)
			}
		};

		F<Enumeration<B>, Enumeration<A>> fooFun = new F<Enumeration<B>, Enumeration<A>>() {
			public Enumeration<A> apply(final Enumeration<B> e) {
				return Enumeration.apply(Enumeration.singleton(foo), e).pay();
			}
		};

		F<Enumeration<A>, Enumeration<A>> hooFun = new F<Enumeration<A>, Enumeration<A>>() {
			public Enumeration<A> apply(final Enumeration<A> e) {
				return Enumeration.apply(Enumeration.singleton(hoo), e).pay();
			}
		};

		F<Enumeration<B>, Enumeration<B>> bFun = new F<Enumeration<B>, Enumeration<B>>() {
			public Enumeration<B> apply(final Enumeration<B> e) {
				return Enumeration.singleton((B) enumerator.mutual.types.b.b.make()); // !!!! NO pay (is a constant)
			}
		};

		F<Enumeration<A>, Enumeration<B>> grrFun = new F<Enumeration<A>, Enumeration<B>>() {
			public Enumeration<B> apply(final Enumeration<A> e) {
				return Enumeration.apply(Enumeration.singleton(grr), e).pay();
			}
		};

		//grrFun, bFun, fooFun, hooFun, aFun

		Enumeration<A> ea = new Enumeration<A>((LazyList<Finite<A>>) null);
		Enumeration<B> eb = new Enumeration<B>((LazyList<Finite<B>>) null);

		final Enumeration<B> resgrr = grrFun.apply(ea);
		final Enumeration<B> resb = bFun.apply(eb);
		final Enumeration<A> resfoo = fooFun.apply(eb);
		final Enumeration<A> reshoo = hooFun.apply(ea);
		final Enumeration<A> resa = aFun.apply(ea);

		final Enumeration<A> resA = resfoo.plus(reshoo).plus(resa);
		final Enumeration<B> resB = resgrr.plus(resb);

		ea.p1 = new P1<LazyList<Finite<A>>>() {

			public LazyList<Finite<A>> _1() {
				return resA.parts();
			}
		};

		eb.p1 = new P1<LazyList<Finite<B>>>() {

			public LazyList<Finite<B>> _1() {
				return resB.parts();
			}
		};

		map.put(B.class,resB);
		map.put(A.class,resA);

	}
        
    public static Enumeration<A> getA() {
        Enumeration<A> enumA = MutualEnum.instance().get(A.class);
        return enumA;
    }
    
    public static Enumeration<B> getB() {
        Enumeration<B> enumB = MutualEnum.instance().get(B.class);
        return enumB;
    }


}
