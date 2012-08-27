/*
 * Created on Aug 22, 2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package tom.library.enumerator;

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
	
	/**
	 * module enumerator.Mutual
	 * abstract syntax
	 * 
	 * A = a() | foo(b:B) | hoo(a:A)
	 * B = b() | grr(a:A)
	 */
	private void init() {
		
		F<Enumeration<A>, Enumeration<A>> aFun = new F<Enumeration<A>, Enumeration<A>>() {
			public Enumeration<A> apply(final Enumeration<A> e) {
				return Enumeration.singleton((A) enumerator.mutual.types.a.a.make()); // !!!! NO pay (is a constant)
			}
		};

		F<Enumeration<B>, Enumeration<A>> fooFun = new F<Enumeration<B>, Enumeration<A>>() {
			public Enumeration<A> apply(final Enumeration<B> e) {
				F<B, A> foo = new F<B, A>() {
					public A apply(final B elem) {
						return enumerator.mutual.types.a.foo.make(elem);
					}
				};
				return Enumeration.apply(Enumeration.singleton(foo), e).pay();
			}
		};

		F<Enumeration<A>, Enumeration<A>> hooFun = new F<Enumeration<A>, Enumeration<A>>() {
			public Enumeration<A> apply(final Enumeration<A> e) {
				F<A, A> hoo = new F<A, A>() {
					public A apply(final A elem) {
						return enumerator.mutual.types.a.hoo.make(elem);
					}
				};
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
				 F<A, B> grr = new F<A, B>() {
					public B apply(final A elem) {
						return enumerator.mutual.types.b.grr.make(elem);
					}
				};
				return Enumeration.apply(Enumeration.singleton(grr), e).pay();
			}
		};


		Enumeration<A> enumA = new Enumeration<A>((LazyList<Finite<A>>) null);
		Enumeration<B> enumB = new Enumeration<B>((LazyList<Finite<B>>) null);

		final Enumeration<A> _a = aFun.apply(enumA);
		final Enumeration<A> _foo = fooFun.apply(enumB);
		final Enumeration<A> _hoo = hooFun.apply(enumA);
		final Enumeration<B> _b = bFun.apply(enumB);
		final Enumeration<B> _grr = grrFun.apply(enumA);

		final Enumeration<A> sortA = _foo.plus(_hoo).plus(_a);
		final Enumeration<B> sortB = _grr.plus(_b);

		enumA.p1 = new P1<LazyList<Finite<A>>>() {
			public LazyList<Finite<A>> _1() {
				return sortA.parts();
			}
		};

		enumB.p1 = new P1<LazyList<Finite<B>>>() {
			public LazyList<Finite<B>> _1() {
				return sortB.parts();
			}
		};

		map.put(B.class,sortB);
		map.put(A.class,sortA);

	}
        
    public static Enumeration<A> getA() {
//        Enumeration<A> enumA = (Enumeration<A>) MutualEnum.instance().map.get(A.class); // simpler but more unchecked cast (one per type)
        Enumeration<A> enumA = MutualEnum.instance().get(A.class);
        return enumA;
    }
    
    public static Enumeration<B> getB() {
        Enumeration<B> enumB = MutualEnum.instance().get(B.class);
        return enumB;
    }


}
