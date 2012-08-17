package enumerator;

import java.math.BigInteger;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;

public class Finite<A> {
	private BigInteger card;
	private F<BigInteger, A> indexer;

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

	public BigInteger getCard() {
		return card;
	}

	public Finite<A> setCard(BigInteger newCard) {
		return new Finite<A>(newCard, indexer);
	}

	public <A> Finite<A> setIndexer(F<BigInteger, A> newIndexer) {
		return new Finite<A>(card, newIndexer);
	}

	public A get(BigInteger i) {
		return indexer.apply(i);
	}

	public Finite<A> plus(final Finite<A> other) {
		return new Finite<A>(card.add(other.card), new F<BigInteger, A>() {
			public A apply(BigInteger i) {
				return (i.compareTo(card)<0) ? get(i) : other.get(i.subtract(card));
			}
		});
	}

	public <B> Finite<P2<A, B>> times(final Finite<B> other) {
		return new Finite<P2<A, B>>(card.multiply(other.card),
				new F<BigInteger, P2<A, B>>() {
					public P2<A, B> apply(BigInteger i) {
						BigInteger[] qr = i.divideAndRemainder(other.card);
						final BigInteger q = qr[0];
						final BigInteger r = qr[1];
						return new P2<A, B>() {
							public A _1() { return Finite.this.get(q); }
							public B _2() { return other.get(r); }
						};
					}
				});
	}
	
	public static <E> F2<Finite<E>, Finite<E>, Finite<E>> functorPlus() {
		return new F2<Finite<E>, Finite<E>, Finite<E>>() {
			public Finite<E> apply(Finite<E> x, Finite<E> y) {
				return x.plus(y);
			}
		};
	}

	public static <A,B> F2<Finite<A>, Finite<B>, Finite<P2<A, B>>> functorTimes() {
		return new F2<Finite<A>, Finite<B>, Finite<P2<A, B>>>() {
			public Finite<P2<A, B>> apply(Finite<A> x, Finite<B> y) {
				return x.times(y);
			}
		};
	}

	public <B> Finite<B> map(final F<A,B> f) {
		//return setIndexer(indexer.andThen(f));
		return setIndexer(f.o(indexer));
	}

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
			if(elt instanceof P2) {
				P2 p = (P2) elt;
				s = s + "(" + p._1() + "," + p._2() + ")";
			} else {
				s = s + elt;
			}
			s += ",";
		}
		s = s + "]";
		return s;
	}
}
