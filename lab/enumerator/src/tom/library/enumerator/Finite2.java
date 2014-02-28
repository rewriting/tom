package tom.library.enumerator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;

/*
 * This class represent Finite2 sets
 * Basics constructors are empty and singleton
 * They can be combined with plus, times and apply operators
 */
public class Finite2<A> {
	private BigInteger card;
	private F<BigInteger, A> indexer;

	/**
	 * constructors
	 */
	public Finite2(BigInteger c, F<BigInteger, A> f) {
		this.card = c;
		this.indexer = f;
	}

	public static <A> Finite2<A> empty() {
		return new Finite2<A>(ZERO, new F<BigInteger, A>() {
			public A apply(BigInteger i) {
				throw new RuntimeException("index out of range");
			}
		});
	}

	public static <A> Finite2<A> singleton(final A x) {
		return new Finite2<A>(ONE, new F<BigInteger, A>() {
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
	public Finite2<A> plus(final Finite2<A> other) {
		final BigInteger newCard = card.add(other.card);
		return new Finite2<A>(newCard, new F<BigInteger, A>() {
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
	public <B> Finite2<P2<A, B>> times(final Finite2<B> other) {
		final BigInteger newCard = card.multiply(other.card);

		return new Finite2<P2<A, B>>(newCard,
				new F<BigInteger, P2<A, B>>() {
					public P2<A, B> apply(BigInteger i) {
						if(i.compareTo(newCard) < 0) {
							BigInteger[] qr = i.divideAndRemainder(other.card);
							final BigInteger q = qr[0];
							final BigInteger r = qr[1];

							return new P2<A, B>() {
								public A _1() { return Finite2.this.indexer.apply(q); }
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
	public <B> Finite2<B> map(final F<A,B> f) {
		return new Finite2<B>(card,f.o(indexer));
	}

	/**
	 * apply a set of functions to a set of elements (cartesian product)
	 */
	public <B> Finite2<B> applyFinite2Functions(final Finite2<F<A,B>> subject) {
		F<P2<F<A,B>,A>,B> pair = new F<P2<F<A,B>,A>,B>() {
			// pair : f x a -> f(a) 
			public B apply(P2<F<A,B>,A> p) { return p._1().apply(p._2()); }
		};
		return subject.times(Finite2.this).map(pair);
	}
	
	List<A> toList() {
		List<A> res = new ArrayList<A>();
		for(BigInteger i=ZERO ; i.compareTo(getCard())<0; i=i.add(ONE)) {
			A elt = get(i);
			res.add(elt);
		}
		return res;
	}
	
	public static <A> Finite2<A> fromList(List<A> arg) {
		Finite2<A> res = Finite2.empty();
		for(A e:arg) {
			res = res.plus(Finite2.singleton(e));
		}
		return res;
	}
	
	public String toString() {
		return toList().toString();
	}
	
	@Override
	public boolean equals(Object obj) {
	if (obj instanceof Finite2) {
		return toList().equals(((Finite2)obj).toList());
	}
		return super.equals(obj);
	}

}
