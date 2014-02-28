package tom.library.enumerator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;

/*
 * This class represent finite sets
 * Basics constructors are empty and singleton
 * They can be combined with plus, times and apply operators
 */
public class Finite<A> {
	private BigInteger card;
	private Operation op;

	/**
	 * constructors
	 */
	public Finite(BigInteger c, Operation o) {
		this.card = c;
		this.op = o;
	}

	public static <A> Finite<A> empty() {
		return new Finite<A>(ZERO, new OEmpty());
	}

	public static <A> Finite<A> singleton(final A x) {
		return new Finite<A>(ONE, new OSingleton<A>(x));
	}

	/**
	 * cardinality of the set
	 */
	public BigInteger getCard() {
		return card;
	}

	/**
	 * retrieve an element
	 */
	public A get(BigInteger i) {
		return indexer.apply(i);
	}

	/**
	 * union of two sets
	 */
	public Finite<A> plus(final Finite<A> other) {
		final BigInteger newCard = card.add(other.card);
		return new Finite<A>(newCard, new F<BigInteger, A>() {
			public A apply(BigInteger i) {
				if(i.compareTo(newCard) < 0) {
					return (i.compareTo(card)<0) ? indexer.apply(i) : other.indexer.apply(i.subtract(card));
				} else {
					throw new RuntimeException("index out of range");
				}
			}
		});
	}

	/**
	 * cartesian product of two sets
	 */
	public <B> Finite<P2<A, B>> times(final Finite<B> other) {
		final BigInteger newCard = card.multiply(other.card);

		return new Finite<P2<A, B>>(newCard,
				new F<BigInteger, P2<A, B>>() {
					public P2<A, B> apply(BigInteger i) {
						if(i.compareTo(newCard) < 0) {
							BigInteger[] qr = i.divideAndRemainder(other.card);
							final BigInteger q = qr[0];
							final BigInteger r = qr[1];

							return new P2<A, B>() {
								public A _1() { return Finite.this.indexer.apply(q); }
								public B _2() { return other.indexer.apply(r); }
							};
						} else {
							throw new RuntimeException("index out of range");
						}
					}
				});
	}
	
	/**
	 * compose a function with the indexer
	 */
	public <B> Finite<B> map(final F<A,B> f) {
		return new Finite<B>(card,f.o(indexer));
	}

	/**
	 * apply a set of functions to a set of elements (cartesian product)
	 */
	public <B> Finite<B> applyFiniteFunctions(final Finite<F<A,B>> subject) {
		F<P2<F<A,B>,A>,B> pair = new F<P2<F<A,B>,A>,B>() {
			// pair : f x a -> f(a) 
			public B apply(P2<F<A,B>,A> p) { return p._1().apply(p._2()); }
		};
		return subject.times(Finite.this).map(pair);
	}
	
	List<A> toList() {
		List<A> res = new ArrayList<A>();
		for(BigInteger i=ZERO ; i.compareTo(getCard())<0; i=i.add(ONE)) {
			A elt = get(i);
			res.add(elt);
		}
		return res;
	}
	
	public static <A> Finite<A> fromList(List<A> arg) {
		Finite<A> res = Finite.empty();
		for(A e:arg) {
			res = res.plus(Finite.singleton(e));
		}
		return res;
	}
	
	public String toString() {
		return toList().toString();
	}
	
	@Override
	public boolean equals(Object obj) {
	if (obj instanceof Finite) {
		return toList().equals(((Finite)obj).toList());
	}
		return super.equals(obj);
	}
	
	/*
	 * classes for VM
	 */
	private static abstract class Instruction {
		protected int code;	
		protected static int DONE  = 1;
		protected static int MAP   = 2;
		protected static int PAIR1 = 3;
		protected static int PAIR2 = 4;

		public int getCode() { return this.code; }
	}
	
	private static class Done extends Instruction {
		public Done() {	this.code = DONE; }
	}
	
	private static class Map extends Instruction {
		public F fun;
		public Map(F f) { this.code = MAP; this.fun = f; }
	}
	
	private static abstract class Operation {
		protected int code;	
		protected static int ADD       = 1;
		protected static int MULT      = 2;
		protected static int EMPTY     = 3;
		protected static int SINGLETON = 4;
		protected static int MAP       = 5;

		public int getCode() { return this.code; }
	}
	
	private static class OEmpty extends Operation {
		public OEmpty() { this.code = EMPTY; }
	}
	
	private static class OSingleton<A> extends Operation {
		A value;
		public OSingleton(A v) { this.code = SINGLETON; this.value = v; }
	}
	
	private static class OMap extends Operation {
		public Finite finite;
		public F fun;
		public OMap(Finite fin, F f) { this.code = MAP; this.finite = fin; this.fun = f; }
	}
	
	
}
