package tom.library.enumerator;

import java.math.BigInteger;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;
import java.util.ArrayList;
import java.util.List;

/**
 * A lazy list, possibly infinite.
 */

public class LazyList<A> {
	/**
	 * head and tail stored in a pair this ensures lazyness
	 */
	private P2<A, LazyList<A>> pair = null;

	private A cacheHead;
	private LazyList<A> cacheTail;

	private LazyList() {
	}

	/**
	 * constructors
	 */
	public static <A> LazyList<A> fromPair(P2<A, LazyList<A>> p) {
		LazyList<A> res = new LazyList<A>();
		res.pair = p;
		return res;
	}

	public static <A> LazyList<A> nil() {
		return fromPair(null);
	}

	public static <A> LazyList<A> singleton(final A x) {
		return fromPair(new P2<A, LazyList<A>>() {
			public A _1() {
				return x;
			}

			public LazyList<A> _2() {
				return LazyList.nil();
			}
		});
	}

	/**
	 * access to the head of the list store the result in cacheHead for further
	 * access
	 */
	public A head() {
		if (cacheHead == null) {
			cacheHead = pair._1();
		}
		return cacheHead;
	}

	/**
	 * access to the tail of the list store the result in cacheTail for further
	 * access
	 */
	public LazyList<A> tail() {
		if (cacheTail == null) {
			cacheTail = pair._2();
		}
		return cacheTail;
	}

	/**
	 * true when the list is empty
	 */
	public boolean isEmpty() {
		return pair == null;
	}

	/**
	 * [: [a,b,c,d].zipWith([x,y,z], f) :] is [: [f(a,z), f(b,y), f(c,z)] :].
	 */
	public final <B, C> LazyList<C> zipWith(final LazyList<B> ys, final F<A, F<B, C>> f) {
		if (this.isEmpty() || ys.isEmpty()) {
			return LazyList.<C> nil();
		}
		return fromPair(new P2<C, LazyList<C>>() {
			public C _1() {
				return f.apply(LazyList.this.head()).apply(ys.head());
			}

			public LazyList<C> _2() {
				return LazyList.this.tail().zipWith(ys.tail(), f);
			}
		});
	}

	public final <B, C> LazyList<C> zipWith(final LazyList<B> ys, final F2<A, B, C> f) {
		return zipWith(ys, f.curry());
	}

	/**
	 * [s] appended to [this].
	 */
	public LazyList<A> append(final LazyList<A> s) {
		if (this.isEmpty()) {
			return s;
		}
		return fromPair(new P2<A, LazyList<A>>() {
			public A _1() {
				return LazyList.this.head();
			}

			public LazyList<A> _2() {
				return LazyList.this.tail().append(s);
			}
		});
	}

	/**
	 * [: 1,2,3 :].tails() is [: [:1,2,3:], [:2,3;], [:3:] :]
	 */
	public final LazyList<LazyList<A>> tails() {
		if (this.isEmpty()) {
			return LazyList.singleton(LazyList.<A> nil());
		}
		return fromPair(new P2<LazyList<A>, LazyList<LazyList<A>>>() {
			public LazyList<A> _1() {
				return LazyList.this;
			}

			public LazyList<LazyList<A>> _2() {
				return LazyList.this.tail().tails();
			}
		});
	}

	/**
	 *  [: _reversals([1,2,3,...]) :] is [: [[1], [2,1], [3,2,1], ...] :].
	 */
	public LazyList<LazyList<A>> reversals() {
		return reversalsAux(LazyList.<A> nil());
	}

	private LazyList<LazyList<A>> reversalsAux(final LazyList<A> rev) {
		if (isEmpty()) {
			return LazyList.nil();
		}
		final LazyList<A> newrev = LazyList.fromPair(new P2<A, LazyList<A>>() {
			public A _1() {
				return head();
			}

			public LazyList<A> _2() {
				return rev;
			}
		});
		return LazyList.fromPair(new P2<LazyList<A>, LazyList<LazyList<A>>>() {
			public LazyList<A> _1() {
				return newrev;
			}

			public LazyList<LazyList<A>> _2() {
				return tail().reversalsAux(newrev);
			}
		});
	}

	/**
	 * apply f to all elements of the list
	 */
	public final <B> LazyList<B> map(final F<A, B> f) {
		if (this.isEmpty()) {
			return LazyList.<B> nil();
		}
		return fromPair(new P2<B, LazyList<B>>() {
			public B _1() {
				return f.apply(LazyList.this.head());
			}

			public LazyList<B> _2() {
				return LazyList.this.tail().map(f);
			}
		});
	}

	/**
	 * [: [a,b,c].foldLeft(zero, +) :] is [: zero + a + b + c :].
	 */
	public final <B> B foldLeft(final B neutral, final F<B, F<A, B>> f) {
		B res = neutral;
		for (LazyList<A> xs = this; !xs.isEmpty(); xs = xs.tail()) {
			res = f.apply(res).apply(xs.head());
		}
		return res;
	}

	public final A foldLeft(final A neutral, final F2<A, A, A> f) {
		return foldLeft(neutral, f.curry());
	}

	/**
	 * Linear indexing
	 */
	public final A index(final BigInteger i) {
		if (i.signum() < 0) {
			throw new RuntimeException("index " + i + " out of range");
		} else {
			LazyList<A> xs = this;
			for (BigInteger c = ZERO; c.compareTo(i) < 0; c = c.add(ONE)) {
				if (xs.isEmpty()) {
					throw new RuntimeException("index " + i + " out of range");
				}
				xs = xs.tail();
			}

			if (xs.isEmpty()) {
				throw new RuntimeException("index " + i + " out of range");
			}
			return xs.head();
		}
	}

	public List<A> toList() {
		List<A> res = new ArrayList<A>();
		for (LazyList<A> xs = this; !xs.isEmpty(); xs = xs.tail()) {
			res.add(xs.head());
		}
		return res;
	}

	public String toString() {
		return toList().toString();
	}
}
