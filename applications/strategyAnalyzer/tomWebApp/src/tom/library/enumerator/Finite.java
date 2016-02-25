package tom.library.enumerator;

import java.math.BigInteger;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;

public class Finite<A> {
	private BigInteger card;
	private F<BigInteger, A> indexer;

	/**
	 * constructors
	 */
	public Finite(BigInteger c, F<BigInteger, A> f) {
		this.card = c;
		this.indexer = f;
	}

	public static <A> Finite<A> empty() {
		return new Finite<A>(ZERO, new F<BigInteger, A>() {
			public A apply(BigInteger i) {
				throw new RuntimeException("index out of range");
			}
		});
	}

	public static <A> Finite<A> singleton(final A x) {
		return new Finite<A>(ONE, new F<BigInteger, A>() {
			public A apply(BigInteger i) {
				if (i.signum() == 0) {
					return x;
				} else {
					throw new RuntimeException("index out of range");
				}
			}
		});
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
		return new Finite<A>(card.add(other.card), new F<BigInteger, A>() {
			public A apply(BigInteger i) {
				return (i.compareTo(card)<0) ? indexer.apply(i) : other.indexer.apply(i.subtract(card));
			}
		});
	}

	/**
	 * cartesian product of two sets
	 */
	public <B> Finite<P2<A, B>> times(final Finite<B> other) {
		return new Finite<P2<A, B>>(card.multiply(other.card),
				new F<BigInteger, P2<A, B>>() {
					public P2<A, B> apply(BigInteger i) {
						BigInteger[] qr = i.divideAndRemainder(other.card);
						final BigInteger q = qr[0];
						final BigInteger r = qr[1];
						return new P2<A, B>() {
							public A _1() { return Finite.this.indexer.apply(q); }
							public B _2() { return other.indexer.apply(r); }
						};
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
	public static <A,B> Finite<B> apply(final Finite<F<A,B>> subject, final Finite<A> other) {
		F<P2<F<A,B>,A>,B> pair = new F<P2<F<A,B>,A>,B>() {
			public B apply(P2<F<A,B>,A> p) { return p._1().apply(p._2()); }
		};
		return subject.times(other).map(pair);
	}
	
	public String toString() {
		String s = "[";
		for(BigInteger i=ZERO ; i.compareTo(getCard())<0; i=i.add(ONE)) {
			A elt = get(i);
			s += elt;
			s += ",";
		}
		s = s + "]";
		return s;
	}
}
