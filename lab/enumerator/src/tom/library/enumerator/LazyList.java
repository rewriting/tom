package tom.library.enumerator;

import java.math.BigInteger;

import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;

import java.util.ArrayList;
import java.util.List;

/**
 * A lazy list, possibly infinite.
 */

public abstract class LazyList<A> {
	/**
	 * head and tail stored in a pair this ensures lazyness
	 */

	/**
	 * constructors
	 */

	public static <A> LazyList<A> nil() {
		return new Empty<A>();
	}

	public static <A> LazyList<A> singleton(final A x) {
		return cons(x, new P1<LazyList<A>>() {
			@Override
			public LazyList<A> _1() {
				return LazyList.<A> nil();
			}
		});
	}

	public static <A> LazyList<A> cons(final A e, final P1<LazyList<A>> p1) {
		return new Cons<A>(e, p1);
	}

	/**
	 * access to the head of the list store the result in cacheHead for further
	 * access
	 */
	public abstract A head();

	/**
	 * access to the tail of the list store the result in cacheTail for further
	 * access
	 */
	public abstract LazyList<A> tail();

	/**
	 * true when the list is empty
	 */
	public abstract boolean isEmpty();

	/**
	 * [: [a,b,c,d].zipWith([x,y,z], f) :] is [: [f(a,z), f(b,y), f(c,z)] :].
	 */
	public final <B, C> LazyList<C> zipWith(final LazyList<B> ys,
			final F<A, F<B, C>> f) {
		if (this.isEmpty() || ys.isEmpty()) {
			return LazyList.<C> nil();
		}
		return cons(f.apply(head()).apply(ys.head()),
				new P1<LazyList<C>>() {
			@Override
			public LazyList<C> _1() {
				return 	tail().zipWith(ys.tail(), f);
			}
		});	
	}

	public final <B, C> LazyList<C> zipWith(final LazyList<B> ys,
			final F2<A, B, C> f) {
		return zipWith(ys, f.curry());
	}

	/**
	 * [s] appended to [this].
	 */
	public LazyList<A> append(final LazyList<A> s) {
		if (this.isEmpty()) {
			return s;
		}
		return cons(head(), new P1<LazyList<A>>() {
			public LazyList<A> _1() {
				return tail().append(s);
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
		return cons(this, new P1<LazyList<LazyList<A>>>() {
			public LazyList<LazyList<A>> _1() {
				return LazyList.this.tail().tails();
			}
		});
	}

	/**
	 * [: _reversals([1,2,3,...]) :] is [: [[1], [2,1], [3,2,1], ...] :].
	 */
	public LazyList<LazyList<A>> reversals() {
		return reversalsAux(LazyList.<A> nil());
	}

	private LazyList<LazyList<A>> reversalsAux(final LazyList<A> rev) {
		if (isEmpty()) {
			return nil();
		}
		final LazyList<A> newrev = cons(head(), new P1<LazyList<A>>() {
			public tom.library.enumerator.LazyList<A> _1() {
				return rev;
			};
		});
		return cons(newrev, new P1<LazyList<LazyList<A>>>() {
			public LazyList<LazyList<A>> _1() {
				return LazyList.this.tail().reversalsAux(newrev);
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
		return cons(f.apply(head()), new P1<LazyList<B>>() {
			public LazyList<B> _1() {
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

	public LazyList<A> take(final long maxSize) {
		if (maxSize > 0) {
			return LazyList.<A> cons(this.head(), new P1<LazyList<A>>() {
				public LazyList<A> _1() {
					return LazyList.this.tail().take(maxSize - 1);
				}
			});
		} else {
			return LazyList.nil();
		}
	}

	public List<A> toList() {
		List<A> res = new ArrayList<A>();
		for (LazyList<A> xs = this; !xs.isEmpty(); xs = xs.tail()) {
			res.add(xs.head());
		}
		return res;
	}

	public static <A> LazyList<A> fromList(List<A> l) {
		LazyList<A> res = LazyList.nil();
		for (int i = l.size() - 1; i >= 0; i--) {
			final LazyList<A> tail = res;
			res = cons(l.get(i), new P1<LazyList<A>>() {
				public tom.library.enumerator.LazyList<A> _1() {
					return tail;
				};
			});
		}
		return res;
	}

	public String toString() {
		return toList().toString();
	}

	/*
	 * Empty
	 */
	private static class Empty<A> extends LazyList<A> {
		protected Empty() {
			super();
		}

		public A head() {
			throw new RuntimeException("cannot get head of an empty list");
		}

		public LazyList<A> tail() {
			throw new RuntimeException("cannot get tail of an empty list");
		}

		public boolean isEmpty() {
			return true;
		}

	}

	/*
	 * Cons
	 */
	private static class Cons<A> extends LazyList<A> {
		private final A head;
		private final P1<LazyList<A>> lazyTail;
		private LazyList<A> cacheTail;

		protected Cons(A x, P1<LazyList<A>> p1) {
			super();
			head = x;
			lazyTail = p1;
		}

		public A head() {
			return head;
		}

		public LazyList<A> tail() {
			if (cacheTail == null) {
				cacheTail = lazyTail._1();
				// lazyTail = null;
			}
			return cacheTail;
		}

		public boolean isEmpty() {
			return false;
		}

	}

	public LazyList<A> prefix(int size) {
		LazyList<A> prefix = LazyList.nil();
		for (int i = 0; i < size; i++) {
			prefix = prefix.append(LazyList.singleton(index(BigInteger
					.valueOf(i))));
		}
		return prefix;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LazyList) {
			return toList().equals(((LazyList<?>) obj).toList());
		}
		return super.equals(obj);
	}

}
