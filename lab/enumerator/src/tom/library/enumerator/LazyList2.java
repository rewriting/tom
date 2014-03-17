package tom.library.enumerator;

import java.math.BigInteger;

import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;

import java.util.ArrayList;
import java.util.List;

/**
 * A lazy list, possibly infinite.
 */

public class LazyList2<A> {
	/**
	 * head and tail stored in a pair this ensures lazyness
	 */
	private P2<A, LazyList2<A>> pair = null;

	private A cacheHead;
	private LazyList2<A> cacheTail;

	private LazyList2() {
	}

	/**
	 * constructors
	 */
	public static <A> LazyList2<A> fromPair(P2<A, LazyList2<A>> p) {
		LazyList2<A> res = new LazyList2<A>();
		res.pair = p;
		return res;
	}

	public static <A> LazyList2<A> nil() {
		return fromPair(null);
	}

	public static <A> LazyList2<A> singleton(final A x) {
		/*
		return fromPair(new P2<A, LazyList2<A>>() {
			public A _1() {
				return x;
			}

			public LazyList2<A> _2() {
				return LazyList2.nil();
			}
		});
		*/
		return cons(x, LazyList2.<A> nil()); //!\\ notation
	}

	public static <A> LazyList2<A> cons(final A e, final LazyList2<A> s) {
		return fromPair(new P2<A, LazyList2<A>>() {
			public A _1() {
				return e;
			}

			public LazyList2<A> _2() {
				return s;
			}
		});
	}

	/**
	 * access to the head of the list store the result in cacheHead for further
	 * access
	 */
	public A head() {
		if(pair == null) {
			throw new RuntimeException("cannot get head of an empty list");
		}
		if (cacheHead == null) {
			cacheHead = pair._1();
		}
		return cacheHead;
	}

	/**
	 * access to the tail of the list store the result in cacheTail for further
	 * access
	 */
	public LazyList2<A> tail() {
		if(pair == null) {
			throw new RuntimeException("cannot get tail of an empty list");
		}
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
	public final <B, C> LazyList2<C> zipWith(final LazyList2<B> ys, final F<A, F<B, C>> f) {
		if (this.isEmpty() || ys.isEmpty()) {
			return LazyList2.<C> nil();
		}
		return cons(f.apply(head()).apply(ys.head()), tail().zipWith(ys.tail(), f));
		/*
		return fromPair(new P2<C, LazyList2<C>>() {
			public C _1() {
				return f.apply(LazyList2.this.head()).apply(ys.head());
			}

			public LazyList2<C> _2() {
				return LazyList2.this.tail().zipWith(ys.tail(), f);
			}
		});
		*/
	}

	public final <B, C> LazyList2<C> zipWith(final LazyList2<B> ys, final F2<A, B, C> f) {
		return zipWith(ys, f.curry());
	}
	
	/**
	 * [s] appended to [this].
	 */
	public LazyList2<A> append(final LazyList2<A> s) {
		if (this.isEmpty()) {
			return s;
		}
		return cons(head(),tail().append(s));
		/*
		return fromPair(new P2<A, LazyList2<A>>() {
			public A _1() {
				return LazyList2.this.head();
			}

			public LazyList2<A> _2() {
				return LazyList2.this.tail().append(s);
			}
		});*/
	}

	/**
	 * [: 1,2,3 :].tails() is [: [:1,2,3:], [:2,3;], [:3:] :]
	 */
	public final LazyList2<LazyList2<A>> tails() {
		if (this.isEmpty()) {
			return LazyList2.singleton(LazyList2.<A> nil());
		}
		return cons(this,tail().tails());
		/*
		return fromPair(new P2<LazyList2<A>, LazyList2<LazyList2<A>>>() {
			public LazyList2<A> _1() {
				return LazyList2.this;
			}

			public LazyList2<LazyList2<A>> _2() {
				return LazyList2.this.tail().tails();
			}
		});
		*/
	}

	/**
	 *  [: _reversals([1,2,3,...]) :] is [: [[1], [2,1], [3,2,1], ...] :].
	 */
	public LazyList2<LazyList2<A>> reversals() {
		return reversalsAux(LazyList2.<A> nil());
	}

	private LazyList2<LazyList2<A>> reversalsAux(final LazyList2<A> rev) {
		if (isEmpty()) {
			return nil();
		}
		final LazyList2<A> newrev = cons(head(),rev);
		return cons(newrev,tail().reversalsAux(newrev));
		/*
		final LazyList2<A> newrev = LazyList2.fromPair(new P2<A, LazyList2<A>>() {
			public A _1() {
				return head();
			}

			public LazyList2<A> _2() {
				return rev;
			}
		});
		return LazyList2.fromPair(new P2<LazyList2<A>, LazyList2<LazyList2<A>>>() {
			public LazyList2<A> _1() {
				return newrev;
			}

			public LazyList2<LazyList2<A>> _2() {
				return tail().reversalsAux(newrev);
			}
		});
		*/
	}

	/**
	 * apply f to all elements of the list
	 */
	public final <B> LazyList2<B> map(final F<A, B> f) {
		if (this.isEmpty()) {
			return LazyList2.<B> nil();
		}
		return cons(f.apply(head()), tail().map(f));
		/*
		return fromPair(new P2<B, LazyList2<B>>() {
			public B _1() {
				return f.apply(LazyList2.this.head());
			}

			public LazyList2<B> _2() {
				return LazyList2.this.tail().map(f);
			}
		});
		*/
	}

	/**
	 * [: [a,b,c].foldLeft(zero, +) :] is [: zero + a + b + c :].
	 */
	public final <B> B foldLeft(final B neutral, final F<B, F<A, B>> f) {
		B res = neutral;
		for (LazyList2<A> xs = this; !xs.isEmpty(); xs = xs.tail()) {
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
			LazyList2<A> xs = this;
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
		for (LazyList2<A> xs = this; !xs.isEmpty(); xs = xs.tail()) {
			res.add(xs.head());
		}
		return res;
	}

	public static <A> LazyList2<A> fromList(List<A> l) {
		LazyList2<A> res = LazyList2.nil();
		for(int i=l.size()-1 ; i>=0 ; i--) {
			res = cons(l.get(i),res);
		}
    	return res;
	}
	
	public String toString() {
		return toList().toString();
	}
}
