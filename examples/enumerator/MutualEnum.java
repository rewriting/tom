package enumerator;

import tom.library.enumerator.*;
import java.util.HashMap;

import enumerator.mutual.types.A;
import enumerator.mutual.types.B;

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
		Enumeration<A> enumA = new Enumeration<A>((LazyList<Finite<A>>) null);
		Enumeration<B> enumB = new Enumeration<B>((LazyList<Finite<B>>) null);

		final Enumeration<A> _a = enumerator.mutual.types.a.a.funMake().apply(enumA);
		final Enumeration<A> _foo = enumerator.mutual.types.a.foo.funMake().apply(enumB);
		final Enumeration<A> _hoo = enumerator.mutual.types.a.hoo.funMake().apply(enumA).apply(enumB);
		final Enumeration<B> _b = enumerator.mutual.types.b.b.funMake().apply(enumB);
		final Enumeration<B> _grr = enumerator.mutual.types.b.grr.funMake().apply(enumA);

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
